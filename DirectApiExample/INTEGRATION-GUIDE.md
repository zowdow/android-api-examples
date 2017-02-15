### Getting Started - Zowdow Suggestion API

This is a brief summary of the Zowdow Suggestion API.  The basic purpose of the API is to provide very fast keyword and card suggestions based on a query fragment that a user has typed in for auto-suggest.  The API can also be used in a non-fragment sense -- just send an entire keyword as the argument to see the suggested related keywords and their content.

The endpoint of the Suggestions API is:
```
u.zowdow.com/v1/unified
```

The API supports many parameters that are used to customize the keywords and cards returned.  **The API will return no content unless the required parameters are provided.**  The **required** parameters for an API call are:
  * ***app_id*** -- the app name calling the API, and
  * ***q*** -- the query (fragment or a whole phrase/keyword)
  * ***device_id*** -- a unique identifier for the client device such as IDFA or Advertising Identifier or android id
  * ***os*** -- the operating system of the device (android|ios|windows|blackberry)
  * ***device_model*** -- the model name for the user's device
  * ***system_ver*** -- the verion number for the os (a string)
  * ***network*** -- the type of network the device is connected to (4G|3G|wifi|...)
  * ***locale*** -- used to set the language settings for cards
  * ***timezone*** -- used for time of day calculations (morning/afternoon/evening) for card selection
  * ***tracking*** -- set to 1 if you want impression and click URLs for each card.  Perform a get on these URLS respectively if the card is shown (impression) or clicked on (click).
  
The next set of important parameters define the client location, and include:
* ***lat*** and ***long*** -- latitude and longitude of the client (both floats).  These values are needed if any local content (i.e. cards about some specific location like nearby businesses, events, etc.) is to be served.  Without the lat & long -- only global content is served, limiting the coverage of cards and keywords.
* ***location_accuracy*** -- Meters of accuracy of the latitude and longitude

There are some other parameters that help in content selection:
* ***app_ver*** -- application version
* ***app_build*** -- build version of app
* ***screen_scale*** -- screen resolution in points (1x, 2x, 3x, 4x)
* ***keyboard*** -- Name of currently active keyboard

There are other parameters that specify the number of suggestions and cards you want for each suggestion, namely:
* ***s_limit*** -- the number of keyword suggestions (default is 10)
* ***c_limit*** -- the number of cards (max) for each keyword suggestion (default is 10)

Testing with curl, here's an example call and response:

```json
curl -XGET 'http://localhost:8880/v1/unified?app_id=com.zowdow.android.example&q=black&device_id=8de95467-1570-45bf-9732-178f3f80d3e6&os=Android&device_model=HUAWEI+VIE-L09&system_ver=6.0&network=wifi&locale=en_US&timezone=EDT&tracking=1&lat=32.79317&lon=-115.55478&location_accuracy=100&s_limit=2&c_limit=1'
```

The response will be the json payload of the suggestions and cards.  The ***_meta*** section returns information about the entire set of suggestions (count of suggested keywords, a request id ***rid***, a ***status*** (generally always "SUCCESS"), the calling lat and long (if provided), and the ***ttl*** or time-to-live for the set in seconds (if the calling code is caching).

The ***records*** section is an array of ***suggestion*** items, each with a count of cards ***cardCount*** and an ***id*** for the unique suggestion.  Suggestions also have a ***suggRank*** which is the preferred ranking of the suggestion.

Each card has a ***rank*** (suggested display order), a ***card_format*** which is the size and orientation of the card, a unique ***id***, up to 4 different images for the cards, based on the size/density wanted (x1 to x4), each with the image height and width, and an array of ***actions*** which describe the destinations for clicks (taps) on the cards -- can be a deep link, a web or mobile URL.  

If ***tracking*** is set to 1 in the call, then a ***card_click_url*** and a ***card_impression_url*** are provided for each card -- the client should call these URLs for the respective card events.

```json
{
    "_meta": {
        "count": 2,
        "latitude": "32.79317",
        "longitude": "-115.55478",
        "rid": 0,
        "status": "SUCCESS",
        "ttl": 0
    },
    "records": [
        {
            "suggestion": {
                "cardCount": 1,
                "cards": [
                    {
                        "actions": [
                            {
                                "action_target": "https://play.spotify.com/track/4xpa5zw0GsYI40h1BjZy59",
                                "action_type": "web_url"
                            },
                            {
                                "action_target": "spotify:track:4xpa5zw0GsYI40h1BjZy59",
                                "action_type": "deep_link"
                            },
                            {
                                "action_target": "https://p.scdn.co/mp3-preview/559bbbca082f9a6a707fa4a4b2fb425cb5f804ef?cid=null",
                                "action_type": "music_preview"
                            }
                        ],
                        "cardRank": 1,
                        "card_click_url": "https://u1.quick1y.com/r/c/dcb6c6fe1d352ce539fc1a1ed8e9c7b5edb55691d11569feb47f6a9e7f50aca716109e9ca468e97890ce2f56fd547a64735f69c18f136a579bc2ffef408acac4",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/25a335a01c0e1c4e284a7217761802f50a63b02523a417b8c2d885a12ffd6cd1b2595c048e0058f9ff5a3cc4f4905b037a37424aaa781f29a2d204db02a66c5e",
                        "id": "spotifytrack4xpa5zw0GsYI40h1BjZy59",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/spotifytrack/071216/f/FA85_inline_spotify_track_4xpa5zw0GsYI40h1BjZy59_1481140187975_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/spotifytrack/071216/f/FA85_inline_spotify_track_4xpa5zw0GsYI40h1BjZy59_1481140187975_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/spotifytrack/071216/f/FA85_inline_spotify_track_4xpa5zw0GsYI40h1BjZy59_1481140187975_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/spotifytrack/071216/f/FA85_inline_spotify_track_4xpa5zw0GsYI40h1BjZy59_1481140187975_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    }
                ],
                "id": 6508341,
                "queryFragment": "black",
                "suggRank": 0,
                "suggestion": "Black Knight Satellite - Single Version"
            }
        },
        {
            "suggestion": {
                "cardCount": 1,
                "cards": [
                    {
                        "actions": [
                            {
                                "action_target": "https://play.spotify.com/track/1VFiqRE4rjmvrSQMnVWN3a",
                                "action_type": "web_url"
                            },
                            {
                                "action_target": "spotify:track:1VFiqRE4rjmvrSQMnVWN3a",
                                "action_type": "deep_link"
                            },
                            {
                                "action_target": "https://p.scdn.co/mp3-preview/04dff1bf0ffecebe90280e33775830a06f3f195d?cid=null",
                                "action_type": "music_preview"
                            }
                        ],
                        "cardRank": 1,
                        "card_click_url": "https://u1.quick1y.com/r/c/6aa2f193f8c3eca6def021dc555402e0daafc7e2f7304acaf022ae3be033121ea620aaa60a4ec37b18f1b93de3f6d85a7db27ccde6bbc1349b6a434ca5a40621",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/07f2911a0cefb62403f4269a838c9950344e6b243abb7213e04eb2b4fc8d4f448d86968b07f0653cd12ba4af80956942a6c076971e3fb741f7473a5670cd3eb3",
                        "id": "spotifytrack1VFiqRE4rjmvrSQMnVWN3a",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/spotifytrack/071216/d/a8B8_inline_spotify_track_1VFiqRE4rjmvrSQMnVWN3a_1481139250929_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/spotifytrack/071216/d/a8B8_inline_spotify_track_1VFiqRE4rjmvrSQMnVWN3a_1481139250929_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/spotifytrack/071216/d/a8B8_inline_spotify_track_1VFiqRE4rjmvrSQMnVWN3a_1481139250929_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/spotifytrack/071216/d/a8B8_inline_spotify_track_1VFiqRE4rjmvrSQMnVWN3a_1481139250929_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    }
                ],
                "id": 6511658,
                "queryFragment": "black",
                "suggRank": 1,
                "suggestion": "Black Sails 2017 (Extended edit)"
            }
        }
    ]
}
```
### Error Handling

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

### Simple Authentication

Authentication is based on the **app_id**. 

Only a valid app_id will return results.
