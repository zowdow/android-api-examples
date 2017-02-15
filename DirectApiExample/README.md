# Demo App with direct Zowdow API integration

© 2015-2017 Zowdow, Inc.

This app is intended to represent the opportunities for Android developers to interact
with Zowdow API directly, without SDK.

## Version

Current version as of Feb, 7 2017 is 1.0.1.

## Overview

This application demonstrates basic interaction with Zowdow AutoSuggest API.
More detailed info about their usage is described below, after the Architecture section.

## Architecture

The project consists of 3 key packages:

*   **injection** consists of Dagger modules and components. For now, there is a single module
called `NetworkModule` and it provides the access to Retrofit-service classes that represent request calls
to Zowdow API. We use Dagger 2 in order to make this project unit-testable in a closest perspective & also to keep
its' architecture clean. `NetworkComponent` is initialized inside `ZowdowDirectApplication` class.

*   **network** consists of Retrofit-service classes, which purpose was described above & also entity-classes, which
mostly represent suggestions, cards & ad listings.

*   **ui** is for Activity classes, adapters, custom views & interfaces with callback-methods.
There only activity that plays such an important role in application's workflow is `HomeDemoActivity`.
The key events like Zowdow suggestions loading are happening inside this class.
`WebViewActivity` and `VideoActivity` just represent the cards' content in master-detail flow: it may be either web-
and video-content.

*   **utils** contains constants-interfaces, simple utility-classes for geolocation, runtime permissions checks, connectivity state observations, requests parameters collection & formatting
and other useful stuff.

## Interaction with Zowdow API ##

Unified API is the key Zowdow API to interact with in order to retrieve and process autosuggest data.
In this app's case, it is about search suggestions retrieval by multiple parameters and keywords, defined by developer.

We implemented `UnifiedApiService` which works with Unified API & some tracking events.

**Base URL for this API**

```
https://u.zowdow.com/v1/
```

All API endpoints constants are available in `network/ApiBaseUrls` interface.

**Consuming Unified API**

The example of network call to Unified API may be found in `HomeDemoActivity` class.

This method retrieves suggestions response and converts its' contents into
the list full of cards with the parameters we need to render cards in the suggestion carousels (lists).
If the server response is successful we are switching to the UI thread and passing retrieved and processed suggestions
into suggestions list view's adapter.

```
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

    private void processSuggestionsResponse(BaseResponse<UnifiedDTO> suggestionsResponse) {
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
```

**UI-representation of suggestions**

In the scope of this app we consider `stream` as carousel type. It is a simple list of items
 with horizontal scroll. More carousel types like `mid_stream`, `stack` or `rotary` are available in SDK.

`SuggestionsAdapter` performs the suggestions list rendering. Each suggestion row is bound by
view holder class called `SuggestionViewHolder`, which contains a RecyclerView for cards. `CardsAdapter` instance should be
attached to each of these RecyclerViews in view holder instances.

**Card formats**

You may also dynamically change the cards format by replacing the `card_format` value
for suggestions' retrieval query map.

All card formats are declared in the interface `utils/constants/CardFormats`.

**Tracking**

We use `clickUrl` and `impressionUrl` field values for cards interaction tracking. These fields are returned when you pass `tracking=1` in the query parameter call to the API.

The first one for click events, and the another one is for card appearance events. These URLs must be called during a click or an impression event in order for accurate tracking for monetization.

Impression events are processed directly in `CardImageView` class.

## Contact

For technical support please email support@zowdow.com

## Thank You
