# Getting Started with the Zowdow Suggestion API

This is a brief summary of the Zowdow Suggestion API.  The purpose of the API is to provide  keyword and card suggestions based on a query fragment that a user has typed in for auto-suggest.  

The API can also be used in a non-fragment sense -- just send an entire keyword as the argument to see the suggested related keywords and their content.

The endpoint of the API is:  

https://u.zowdow.com/v1.1/unified

The API supports many parameters that are used to customize the keywords and cards returned.

**Please Note:**

**The API will return no content unless the required parameters are provided.**  In general, the more information that is provided, the better the user experience (content is tuned for the user), as well as the monetization.

###Required Parameters

The **required** parameters for an API call are:

| Parameter | Value | Use |
|:---------|:-----|:----|
|***app_id***|The Zowdow-assigned identifier for your application|Settings for the specific content, countries, card types for you application|
|***q***|The query (or fragment of a query)|The keywords returned will be related to this query or query fragment|
|***device_id***|The IDFA (iOS) Advertiser ID (Android) or Android ID|Customizing content for the individual user as well as presenting device targeted ads|
|***ua***|User Agent of the browser that would render the content after clicking on a card (not of the library processing the API request)|Needed for monetization|
|***lat***|Latitude of the device (decimal)|Required for local content as well as monetization|
|***long***|Longitude of the device (decimal)|Required for local content as well as monetization|

###Recommended Parameters

The **recommended** parameters are as follows:

| Parameter | Value | Use |
|:---------|:-----|:----|
|***os***|android,ios,windows|Content tuning for the device platform|
|***device_model***|Detailed device model number|Useful for device type content targeting|
|***system_ver***|System software version number|Also useful for content targeting|
|***network***|wifi,4g,3g,...|For content targeting|
|***locale***|Default locale for the device|For setting language content preferences|
|***timezone***|Standard TZ values|For daily/time of day optimization|
|***location_accuracy***|Meters resolution for the device lat/long|For local content targeting|
  
###Reporting Parameters

There are some other parameters that can be used (if desired) for reporting traffic and results effectiveness:

| Parameter | Value |
|:---------|:-----|
|***app_ver***|Application version|
|***app_build***|Build version|
|***screen_scale***|1x,2x,3x,4x,5x...|
|***keyboard***|Name of the active keyboard|

###Limit & Selection Parameters

There are other parameters that specify the number of suggestions and cards you want for each suggestion, namely:

| Parameter | Value | Default |
|:---------|:-----|:-------|
|***s_limit***|Number of suggestions (max) to return (rows)|10|
|***c_limit***|Number of cards (max) to return for each suggestion (cards per row)|10|
|***card_format***|Double-pipe separated list of card formats to return from the set ["stamp", "inline", and "ticket"]|inline|

##Sample Call

Testing with curl, here's an example call and response:

**curl -XGET 'http://u.zowdow.com/v1.1/unified?app_id=com.zowdow.android.example&q=black&device_id=8de95467-1570-45bf-9732-178f3f80d3e6&os=Android&device_model=HUAWEI+VIE-L09&system_ver=6.0&network=wifi&locale=en_US&timezone=EDT&lat=32.79317&lon=-115.55478&location_accuracy=100&s_limit=2&c_limit=1&ua=SAMSUNG-GT-B5310%2FB5310ACIK1+SHP%2FVPP%2FR5+Dolfin%2F1.5+Nextreaming+SMM-MMS%2F1.2.0+profile%2FMIDP-2.1+configuration%2FCLDC-1.1'**

##Sample Response

The response will be the json payload of the suggestions and cards.  

The ***_meta*** section returns information about the entire set of suggestions (count of suggested keywords, a request id ***rid***, a ***status*** (generally always "SUCCESS"),  and the ***ttl*** or time-to-live for the set in seconds (if the calling code is caching).

The ***records*** section is an array of ***suggestion*** items, each with a count of cards ***cardCount*** and an ***id*** for the unique suggestion.  Suggestions also have a ***suggRank*** which is the required rank of the suggestion.

Each card has a ***rank*** (suggested display order), a ***card_format*** which is the size and orientation of the card, a unique ***id***, up to 4 different images for the cards, based on the size/density wanted (x1 to x4), each with the image height and width, and an array of ***actions*** which describe the destinations for clicks (taps) on the cards -- can be a deep link, a web or mobile URL.  

For each card there is an array of ***card_click_urls*** and an array of ***card_impression_urls*** -- the client should call these URLs (GET) for the respective card events (i.e. when the card image is displayed, and when a user taps on the card). For the impression URLs, the client should call the set of URLs in parallel for a card using the IAB standard impression definition:

***50% of the card is visable for at least one second***

For the click URL array, each URL should be called when the user taps on the card image.

```json
{
    "_meta": {
        "count": 1,
        "rid": "6e082ca3-861b-4dd0-cd0e-9640f55739aa",
        "status": "SUCCESS",
        "ttl": 0
    },
    "records": [
        {
            "suggCount": 0,
            "suggestion": {
                "cardCount": 1,
                "cards": [
                    {
                        "actions": [
                            {
                                "action_target": "https://play.spotify.com/artist/2P3cjUru4H3fhSXXNxE9kA",
                                "action_type": "web_url"
                            },
                            {
                                "action_target": "spotify:artist:2P3cjUru4H3fhSXXNxE9kA",
                                "action_type": "deep_link"
                            }
                        ],
                        "cardRank": 1,
                        "card_click_urls": [
                        "https://u4.zowdow.com/r/c/d8f9f1c7cd8fe919d51337500d5afa3a199d0ceaf9b1b7cca2934a4cc9f847683e63dc5b0d53bdc1d0c85387624c5e97db82ee35c86ba646fb538ee59f15ca39",
                        "https://we.re.zowdow.com/cd8fe919d51337500d5afa3a199d0ceaf9b1b7cca2934a4cc9f847683e63dc5b0d53bdc1d0c85387624c5e97db82ee35c86ba646fb538ee59f15ca39"
                        ],
                        "card_format": "inline",
                        "card_impression_urls": [ 
                          "https://u4.zowdow.com/r/i/cfdc230fad421357d10a414220087fb199df8874b236f71f658b71c6728dfb857913480393acb89a9382fa7bc35d3cfba2c9ef6d29dc02d73e1601d20504670b",
                          "https://we.re.zowdow.com/7d10a414220087fb199df8874b236f71f658b71c6728dfb857913480393acb89a9382fa7bc35d3cfba2c9ef6d29dc02d73e1601d20504670b"
                        ],
                        "id": "inline_spotify_artist_2P3cjUru4H3fhSXXNxE9kA",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/spotifyartist/111116/5/0d79_inline_spotify_artist_2P3cjUru4H3fhSXXNxE9kA_1478825093491_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/spotifyartist/111116/5/0d79_inline_spotify_artist_2P3cjUru4H3fhSXXNxE9kA_1478825093491_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/spotifyartist/111116/5/0d79_inline_spotify_artist_2P3cjUru4H3fhSXXNxE9kA_1478825093491_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/spotifyartist/111116/5/0d79_inline_spotify_artist_2P3cjUru4H3fhSXXNxE9kA_1478825093491_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    }
                ],
                "id": 7382091,
                "queryFragment": "black",
                "suggRank": 0,
                "suggestion": "blackstreet"
            }
        }
    ]
}
```
### Errors

Generally, the API returns a 200 every time -- regardless of invalid arguments.  The content may be empty (i.e. no suggestions or cards), but 200 is returned.

Here's the empty content result:

```json
{
    "_meta": {
        "count": 0,
        "rid": "b6edea47-df2c-4c6e-ccf3-92efd2c5dd1a",
        "status": "SUCCESS",
        "ttl": 0
    },
    "records": []
}
```

### Authentication

Authentication is based on the **app_id**. 

Only a valid app_id will return results.

Application IDs are generated by Zowdow as part of the application onboarding process.

An app_id is OS specific -- if an API user has versions of their application for multiple operating systems, then each OS version of the application will have a different app_id.
