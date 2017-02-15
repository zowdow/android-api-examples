### 简介 - Zowdow搜索推荐API

本文档简单介绍了Zowdow搜索推荐API的用法。 API根据用户输入的片段、字、词返回搜索推荐以及相关的卡片内容，也可以用完整的关键词当参数来获取更多的相关关键词和内容。

搜索推荐API的终端为：

```
u.zowdow.com/v1/unified
```

API包含很多用来控制搜索推荐及卡片返回内容的参数。**如果没有提供所有必要的参数，API不会返回任何内容。** 一个API调用的**必要**的参数是：
  * ***app_id*** -- 调用API的App名字
  * ***q*** -- 用来查询的片段、字、词
  * ***device_id*** -- 客户端唯一标识符
  * ***os*** -- iOS或Android（用来筛选App推荐，也可以通过***app_id***来设置默认值）
  * ***device_model*** -- 设备的型号
  * ***system_ver*** -- 设备使用的操作系统版本
  * ***network*** -- 网络环境（3G/4G/LTE/WiFi）
  * ***locale*** -- 用来设置卡片的语言
  * ***timezone*** -- 用来进行跟时间段（早上/下午/晚上）相关的卡片时间的计算
  * ***tracking*** = 1 --API返回的每个卡片元数据里会包含两个URL，一个显示URL一个点击URL，当卡片发生相应行为的时候对URL进行GET操作

跟客户端位置信息有关的参数：
* ***lat*** 和 ***long*** -- 客户端所在的纬度和经度坐标。我们需要这些参数来返回本地化的内容（与具体位置相关的卡片，比如附近的商店、活动等）。没有经度和纬度，只有非本地化的内容会被返回
* ***location_accuracy*** -- 经纬度精确度，以米为单位

跟内容选择有关的参数：
* ***app_ver*** -- 应用的版本号
* ***app_build*** -- 应用的build号
* ***screen_scale*** -- 用来改变卡片大小（1x, 2x, 3x, 4x）
* ***keyboard*** -- 当前使用的键盘名称

下面的参数可以声明返回搜索推荐和卡片的最大数量：
* ***s_limit*** -- 搜索推荐的最大数量（默认值为10）
* ***c_limit*** -- 每个搜索推荐的卡片的最大数量（默认值为10）

用curl命令测试的调用示例以及返回数据：

```json
curl -XGET 'http://localhost:8880/v1/unified?app_id=com.zowdow.android.example&q=black&device_id=8de95467-1570-45bf-9732-178f3f80d3e6&os=Android&device_model=HUAWEI+VIE-L09&system_ver=6.0&network=wifi&locale=en_US&timezone=EDT&tracking=1&lat=32.79317&lon=-115.55478&location_accuracy=100&s_limit=2&c_limit=1'
```

返回数据是JSON格式的搜索推荐以及卡片的载荷。***_meta***部分返回的是搜索推荐的概要信息（返回的搜索推荐数量，***rid*** - 调用请求id，***status*** - 状态信息（通常是"SUCCESS"），经度和纬度（如果在API调用里提供了的话），***ttl*** - 数据存活时间（以秒为单位，只有成功返回数据的时候才会显示））。

***records***部分是一组***suggestion***数据，每一项***suggestion***数据都有一个代表卡片数量的键***cardCount***和***id*** - 唯一性的搜索推荐识别码。每一项搜索推荐还包含一个***suggRank*** - 搜索推荐显示顺序。

每一个卡片都包含***rank*** - 推荐显示顺序，***card_format*** - 大小及方向（水平或垂直），***id*** - 唯一性的卡片识别码，以及最多四张不同分辨率的卡片图片（基于大小/像素密度需求，从1倍分辨率到4倍，每张卡片图片包含了图片高度和宽度），以及***actions*** - 一组卡片行为，用来描述点击卡片触发的行为（可以是deep link，也可以是桌面端或者移动端的网址）。

如果***tracking*** 赋值为 1，每个卡片会包含一个***card_click_url***键和一个***card_impression_url***键，客户端应该在卡片显示和卡片点击时分别调用这两个URL。

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
### 错误信息

通常来说API每次都返回200状态码，无论参数有不有效。返回的内容可能是空的（没有搜索推荐或者卡片），但是200状态码总是会返回。

下面是一条内容为空的返回结果

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
### 简单的授权

授权机制基于***app_id***。
只有有效的***app_id***会返回结果。
