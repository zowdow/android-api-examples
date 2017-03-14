package com.zowdow.direct_api.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zowdow.direct_api.R;
import com.zowdow.direct_api.ZowdowDirectApplication;
import com.zowdow.direct_api.network.injection.NetworkComponent;
import com.zowdow.direct_api.network.models.base.BaseResponse;
import com.zowdow.direct_api.network.models.unified.UnifiedResponse;
import com.zowdow.direct_api.network.models.unified.suggestions.CardFormat;
import com.zowdow.direct_api.network.models.unified.suggestions.Suggestion;
import com.zowdow.direct_api.network.services.UnifiedApiService;
import com.zowdow.direct_api.ui.adapters.SuggestionsAdapter;
import com.zowdow.direct_api.utils.PermissionsUtils;
import com.zowdow.direct_api.utils.QueryUtils;
import com.zowdow.direct_api.utils.constants.CardFormats;
import com.zowdow.direct_api.utils.constants.ExtraKeys;
import com.zowdow.direct_api.utils.location.LocationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeDemoActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_RC_CODE = 42;
    private static final String TAG = HomeDemoActivity.class.getSimpleName();
    private static final String DEFAULT_CAROUSEL_TYPE = "stream";

    private boolean queryParamsInitialized;
    private Integer storedListViewPosition;
    private String currentCardFormat = CardFormats.CARD_FORMAT_INLINE;
    private String currentSearchKeyWord = "";
    private String[] locationPermissions = new String[] {
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
    };

    private SuggestionsAdapter suggestionsAdapter;
    private Subscription unifiedApiSubscription;
    private Subscription suggestionsSubscription;
    private Subscription queryParamsSubscription;

    @Inject UnifiedApiService unifiedApiService;

    @BindView(R.id.suggestion_query_edit_text) EditText suggestionQueryEditText;
    @BindView(R.id.suggestions_list_view) RecyclerView suggestionsListView;
    @BindView(R.id.placeholder_text_view) TextView noItemsPlaceholderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_demo);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            queryParamsInitialized = savedInstanceState.getBoolean(ExtraKeys.EXTRA_DEVICE_ROTATED);
            currentCardFormat = savedInstanceState.getString(ExtraKeys.EXTRA_RESTORED_CARD_FORMAT);
            currentSearchKeyWord = savedInstanceState.getString(ExtraKeys.EXTRA_RESTORED_SEARCH_KEYWORD);
        }

        if (!(PermissionsUtils.checkCoarseLocationPermission(this) || PermissionsUtils.checkFineLocationPermission(this)) && !queryParamsInitialized) {
            requestLocationPermissions();
        }

        NetworkComponent networkComponent = ZowdowDirectApplication.getNetworkComponent();
        networkComponent.inject(this);

        setupSuggestionsListView();
        if (queryParamsInitialized) {
            startTrackingSuggestionQueries();
            restoreSuggestions();
        } else {
            LocationManager.get().start(this);
            retrieveApiParams();
        }
    }

    /**
     * Sets an adapter for suggestions' list view. Each suggestion consists of cards array that's being
     * rendered inside the another nested adapter.
     * @see SuggestionsAdapter
     * @see com.zowdow.direct_api.ui.adapters.CardsAdapter
     */
    private void setupSuggestionsListView() {
        suggestionsAdapter = new SuggestionsAdapter(this, new ArrayList<>(), this::onCardClicked);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        suggestionsListView.setLayoutManager(layoutManager);
        suggestionsListView.setAdapter(suggestionsAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Handles card click event (for the cards with web-content only)
     * @param webUrl
     * @param suggestionTitle
     */
    private void onCardClicked(String webUrl, String suggestionTitle) {
        Intent webIntent;
        if (webUrl != null && webUrl.startsWith("http")) {
            webIntent = new Intent(this, WebViewActivity.class);
            webIntent.putExtra(ExtraKeys.EXTRA_ARTICLE_TITLE, suggestionTitle);
            webIntent.putExtra(ExtraKeys.EXTRA_ARTICLE_URL, webUrl);
            startActivity(webIntent);
        } else if (webUrl != null && !webUrl.isEmpty()) {
            webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
            webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(webIntent);
        }
    }

    /**
     * Retrieves query params for further interaction with Unified API.
     */
    private void retrieveApiParams() {
        queryParamsSubscription = QueryUtils.getQueryMapObservable(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(queryParams -> {
                    queryParamsInitialized = true;
                }, throwable -> Log.e(TAG, "Could not generate query params: " + throwable.getMessage()),
                        this::startTrackingSuggestionQueries);
    }

    /**
     * Sets suggestion query text field enabled, and
     * text change events tracking in order to load suggestions for the defined keyword
     * instantly.
     */
    public void startTrackingSuggestionQueries() {
        suggestionQueryEditText.setEnabled(true);
        suggestionQueryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String queryString = s.toString();
                findSuggestions(queryString);
                currentSearchKeyWord = queryString;
            }
        });
    }

    /**
     * Restores suggestions list after activity rotation. Basically it recalls the request.
     */
    private void restoreSuggestions() {
        if (!currentSearchKeyWord.isEmpty()) {
            findSuggestions(currentSearchKeyWord);
        }
    }

    /**
     * Looks the available suggestions up by calling Unified API.
     * @param searchKeyWord
     */
    private void findSuggestions(String searchKeyWord) {
        Map<String, Object> queryMap = QueryUtils.createQueryMapForUnifiedApi(searchKeyWord, currentCardFormat);
        unifiedApiSubscription = unifiedApiService.loadSuggestions(queryMap)
                .subscribeOn(Schedulers.io())
                .cache()
                .subscribe(this::processSuggestionsResponse, throwable -> {
                    Toast.makeText(HomeDemoActivity.this, "Could not load suggestions", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Could not load suggestions: " + throwable.getMessage());
                });
    }

    /**
     * Processes retrieved raw suggestions into listview's adapter-compatible format.
     * @param suggestionsResponse
     */
    private void processSuggestionsResponse(BaseResponse<UnifiedResponse> suggestionsResponse) {
        final String rId = suggestionsResponse.getMeta().getRid();
        suggestionsSubscription = Observable.just(suggestionsResponse)
                .subscribeOn(Schedulers.io())
                .flatMapIterable(BaseResponse::getRecords) // converts response wrapper into an iterable list of suggestions
                .map(suggestionItem -> // performs suggestion deserialization
                        suggestionItem
                                .getSuggestion()
                                .toSuggestion(rId, DEFAULT_CAROUSEL_TYPE, currentCardFormat)
                )
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuggestionsLoaded, throwable -> {
                    Log.e(TAG, "Could not load suggestions: " + throwable.getMessage());
                });
    }

    /**
     * Invoked when suggestions are retrieved from Unified API.
     * @param suggestions
     */
    public void onSuggestionsLoaded(List<Suggestion> suggestions) {
        suggestionsAdapter.setSuggestions(suggestions);
        if (suggestions != null && !suggestions.isEmpty()) {
            noItemsPlaceholderTextView.setVisibility(View.GONE);
            suggestionsListView.setVisibility(View.VISIBLE);
            restoreSuggestionsListViewPosition();
        } else {
            noItemsPlaceholderTextView.setVisibility(View.VISIBLE);
        }
    }

    private void restoreSuggestionsListViewPosition() {
        if (storedListViewPosition != null) {
            suggestionsListView.getLayoutManager().scrollToPosition(storedListViewPosition);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_card_types, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_label_card:
                onCardFormatChanged(CardFormats.CARD_FORMAT_INLINE);
                return true;
            case R.id.action_stamp_card:
                onCardFormatChanged(CardFormats.CARD_FORMAT_STAMP);
                return true;
            case R.id.action_ticket_card:
                onCardFormatChanged(CardFormats.CARD_FORMAT_TICKET);
                return true;
            case R.id.action_gif_card:
                onCardFormatChanged(CardFormats.CARD_FORMAT_ANIMATED_GIF);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Invoked when the another card format has been chosen by user at the top panel.
     * All suggestions and cards for the given keyword are requested again.
     * @param newCardFormat
     */
    private void onCardFormatChanged(@CardFormat String newCardFormat) {
        this.currentCardFormat = newCardFormat;
        findSuggestions(currentSearchKeyWord);
    }

    private void requestLocationPermissions() {
        int permissionStatus = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRationale = false;
        for (String permission : locationPermissions) {
            permissionStatus += ContextCompat.checkSelfPermission(this, permission);
            shouldShowRationale = shouldShowRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
        }
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRationale) {
                Snackbar.make(findViewById(android.R.id.content), R.string.home_permission_first_warning, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.home_action_accept, v -> ActivityCompat.requestPermissions(HomeDemoActivity.this, locationPermissions, LOCATION_PERMISSION_RC_CODE)).show();
            } else {
                ActivityCompat.requestPermissions(this, locationPermissions, LOCATION_PERMISSION_RC_CODE);
            }
        } else {
            onPermissionsGranted(LOCATION_PERMISSION_RC_CODE);
        }
    }

    private void onPermissionsGranted(int requestCode) {
        if (requestCode == LOCATION_PERMISSION_RC_CODE) {
            recreate();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionGrantedStatus = PackageManager.PERMISSION_GRANTED;
        for (int grantResult : grantResults) {
            permissionGrantedStatus += grantResult;
        }
        if (grantResults.length > 0 && permissionGrantedStatus == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        } else {
            Snackbar.make(findViewById(android.R.id.content), R.string.home_permission_second_warning, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.home_action_grant, v -> openAppSettings()).show();
        }
    }

    private void openAppSettings() {
        Intent settingsIntent = new Intent();
        settingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        settingsIntent.addCategory(Intent.CATEGORY_DEFAULT);
        settingsIntent.setData(Uri.parse("package:" + getPackageName()));
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(settingsIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationManager.get().stop();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            storedListViewPosition = savedInstanceState.getInt(ExtraKeys.EXTRA_RECYCLER_VIEW_STATE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ExtraKeys.EXTRA_RECYCLER_VIEW_STATE, ((LinearLayoutManager) suggestionsListView.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
        outState.putBoolean(ExtraKeys.EXTRA_DEVICE_ROTATED, queryParamsInitialized);
        outState.putString(ExtraKeys.EXTRA_RESTORED_CARD_FORMAT, currentCardFormat);
        outState.putString(ExtraKeys.EXTRA_RESTORED_SEARCH_KEYWORD, currentSearchKeyWord);
    }

    @Override
    protected void onDestroy() {
        if (suggestionsSubscription != null && !suggestionsSubscription.isUnsubscribed()) {
            suggestionsSubscription.unsubscribe();
        }
        if (unifiedApiSubscription != null && !unifiedApiSubscription.isUnsubscribed()) {
            unifiedApiSubscription.unsubscribe();
        }
        if (queryParamsSubscription != null && !queryParamsSubscription.isUnsubscribed()) {
            queryParamsSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}