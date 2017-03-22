# Zowdow Autosuggest Service

© 2015-2017 Zowdow, Inc.

The Zowdow Autosuggest service is intended to provide application developers with a new opportunity to monetize in the search autosuggest space.

We offer a robust, micro-SDK for Android that is currently deployed across many distribution partners.

We also offer access through a direct API as documented in the example here.

The application developer will require an app-id key in order to integrated and test. You can request an app-id key by sending an email to dev@zowdow.com.

# Impressions tracking
If you are using this example in your production application, you should take measures to correctly track cards impressions. We are using IAB standard for impressions tracking, introducing following rule: impression should be tracked, if at least 50% card is shown for at least 1 second. Also, timer should not invalidate if card persist between 2 consistent requests. We've created a class to simplify tracking work in you application.

Here is class diagram of impressions tracking classes.

![Class Diagram](ImpressionsTrackingUML.png)

You should use `CardImpressionsTracker` class for tracking.

1. When new data is obtained from API, call `setNewCardsData` method and pass new cards to it, collected from all suggestions retrieved for a given keyword.
2. Call `cardShown` method for all cards that are at least 50% visible.
3. Call `cardHidden` method for all other cards as soon as they get less than 50% visible or currently invisible at all.
4. Track all events that can change cards visibility (e.g. view scroll or device orientation change) and call `cardShown` and `cardHidden` methods for cards that changed visibility.

You can follow the approach of cards visibility state observation similar to `trackVisibleCard` method of application's `SuggestionViewHolder` class implementation. In our case this method is invoked every time the new cards data is set and both suggestions and cards lists are scrolled either vertically or horizontally.

`CardImpressionsTracker` will take care about operations such as cards persistence tracking while switching requests and their current visibility state.
Each `CardImpressionInfo` instance contains the information about visibility of the card it represents & its own timer which state is managed by `cardShown` and `cardHidden` methods. Card's impression will be tracked after as soon as this timer expires. Impression tracking is performed by `TrackingRequestManager`'s `trackCardImpression` method.
`TrackingRequestManager` is the interface which declares basic card's tracking actions. They can be customized in any way convenient for the existing project's architecture, inside the class that implements this interface.