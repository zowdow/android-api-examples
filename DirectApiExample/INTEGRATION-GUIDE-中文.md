# Zowdow搜索推荐API简介

本文档简单介绍了Zowdow搜索推荐API的用法。 API根据用户输入的片段、字、词返回搜索推荐以及相关的卡片内容，也可以用完整的关键词当参数来获取更多的相关关键词和内容。

搜索推荐API的终端为：

```
u.zowdow.com/v1/unified
```

API包含很多用来控制搜索推荐及卡片返回内容的参数。

**请注意：**

**如果没有提供所有必要的参数，API不会返回任何内容。** 通常来说，提供的参数越多，用户体验越佳（内容是根据用户调整的），变现能力也越好。

###必要的参数

一个API调用的**必要**参数是：

| 参数 | 值 | 使用 |
|:---------|:-----|:----|
|***app_id***|Zowdow分配给您应用的唯一标识符|用来筛选特定的内容，国家，卡片形式|
|***q***|查询的片段、字、词|返回的搜索推荐与这些片段、字、词有关|
|***device_id***|IDFA(iOS)/Advertiser ID或Android ID(Android)|为特定的用户定制内容，以及提供定位到设备的广告|

###推荐的参数

| 参数 | 值 | 使用 |
|:---------|:-----|:----|
|***os***|android,ios,windows|根据设备平台调整内容|
|***device_model***|详细的设备型号|用来定位与设备相关的内容|
|***system_ver***|设备使用的操作系统版本|用来内容定位|
|***network***|网络环境（3g/4g/lte/wifi）|用来内容定位|
|***locale***|设备区域语言码|用来设置卡片内容的语言偏好|
|***timezone***|时区码|用来进行跟时间段（早上/下午/晚上）相关的卡片时间的计算|
|***lat***|客户端所在的纬度坐标|用来返回本地化的内容|
|***long***|客户端所在的经度坐标|用来返回本地化的内容|
|***location_accuracy***|经纬度精确度，以米为单位|用来本地化内容定位|

###记录参数

还有一些参数是用来优化记录的信息（如果您想提供的话），包括流量和搜索推荐结果的有效性：

| 参数 | 值 |
|:---------|:-----|
|***app_ver***|应用的版本号|
|***app_build***|应用的build号|
|***screen_scale***|屏幕分辨率级数 1x,2x,3x,4x,5x...|
|***keyboard***|当前使用的键盘名称|

###限制和选项参数

还有一些参数是用来限制搜索推荐和卡片的最大数量：

| 参数 | 值 | 默认值 |
|:---------|:-----|:-------|
|***s_limit***|搜索推荐的最大数量（行数）|10|
|***c_limit***|每个搜索推荐的卡片的最大数量（每行的卡片数）|10|
|***card_format***|单个或多个（用分隔符&#124;&#124;来区分）卡片格式，卡片格式集合["stamp", "inline", and "ticket"]|inline|

##调用示例

用curl命令测试的调用示例以及返回数据：

```json
curl -XGET 'http://u.zowdow.com/v1/unified?app_id=com.zowdow.android.example&q=black&device_id=8de95467-1570-45bf-9732-178f3f80d3e6&os=Android&device_model=HUAWEI+VIE-L09&system_ver=6.0&network=wifi&locale=en_US&timezone=EDT&lat=32.79317&lon=-115.55478&location_accuracy=100&s_limit=2&c_limit=1'
```

##返回数据示例

返回数据是JSON格式的搜索推荐以及卡片的载荷。***_meta***部分返回的是搜索推荐的概要信息（返回的搜索推荐数量，***rid*** - 调用请求id，***status*** - 状态信息（通常是"SUCCESS"），***ttl*** - 数据存活时间（以秒为单位，只有成功返回数据的时候才会显示））。

***records***部分是一组***suggestion***数据，每一项***suggestion***数据都有一个代表卡片数量的键***cardCount***和***id*** - 唯一性的搜索推荐识别码。每一项搜索推荐还包含一个***suggRank*** - 搜索推荐显示顺序。

每一个卡片都包含***rank*** - 推荐显示顺序，***card_format*** - 大小及方向（水平或垂直），***id*** - 唯一性的卡片识别码，以及最多四张不同分辨率的卡片图片（基于大小/像素密度需求，从1倍分辨率到4倍，每张卡片图片包含了图片高度和宽度），以及***actions*** - 一组卡片行为，用来描述点击卡片触发的行为（可以是deep link，也可以是桌面端或者移动端的网址）。

每个卡片会包含一个***card_click_url***键和一个***card_impression_url***键，客户端应该在相应的事件（比如卡片显示在屏幕上，用户点击卡片）调用这两个URL（GET操作）。

```json
{
    "_meta": {
        "count": 2,
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
