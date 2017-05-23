# Zowdow搜索推荐API简介

本文档简单介绍了Zowdow搜索推荐API的用法。 API根据用户输入的片段、字、词返回搜索推荐以及相关的卡片内容，也可以用完整的关键词当参数来获取更多的相关关键词和内容。

搜索推荐API的终端为：

```
https://u.zowdow.com/v1.1/unified
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
|***ua***|卡片点击后用来打开页面的浏览器的User Agent|变现需要|
|***lat***|设备所在的纬度（十进制）|用来提供本地化内容以及变现|
|***long***|设备所在的经度（十进制）|用来提供本地化内容以及变现|

###推荐的参数

| 参数 | 值 | 使用 |
|:---------|:-----|:----|
|***os***|android,ios,windows|根据设备平台调整内容|
|***device_model***|详细的设备型号|用来定位与设备相关的内容|
|***system_ver***|设备使用的操作系统版本|用来内容定位|
|***network***|网络环境（3g/4g/lte/wifi）|用来内容定位|
|***locale***|设备区域语言码|用来设置卡片内容的语言偏好|
|***timezone***|时区码|用来进行跟时间段（早上/下午/晚上）相关的卡片时间的计算|
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
curl -XGET 'http://u.zowdow.com/v1.1/unified?app_id=com.zowdow.android.example&q=black&device_id=8de95467-1570-45bf-9732-178f3f80d3e6&os=Android&device_model=HUAWEI+VIE-L09&system_ver=6.0&network=wifi&locale=en_US&timezone=EDT&lat=32.79317&lon=-115.55478&location_accuracy=100&s_limit=2&c_limit=1&ua=SAMSUNG-GT-B5310%2FB5310ACIK1+SHP%2FVPP%2FR5+Dolfin%2F1.5+Nextreaming+SMM-MMS%2F1.2.0+profile%2FMIDP-2.1+configuration%2FCLDC-1.1'
```

##返回数据示例

返回数据是JSON格式的搜索推荐以及卡片的载荷。***_meta***部分返回的是搜索推荐的概要信息（返回的搜索推荐数量，***rid*** - 调用请求id，***status*** - 状态信息（通常是"SUCCESS"），***ttl*** - 数据存活时间（以秒为单位，只有成功返回数据的时候才会显示））。

***records***部分是一组***suggestion***数据，每一项***suggestion***数据都有一个代表卡片数量的键***cardCount***和***id*** - 唯一性的搜索推荐识别码。每一项搜索推荐还包含一个***suggRank*** - 搜索推荐显示顺序。

每一个卡片都包含***rank*** - 推荐显示顺序，***card_format*** - 大小及方向（水平或垂直），***id*** - 唯一性的卡片识别码，以及最多四张不同分辨率的卡片图片（基于大小/像素密度需求，从1倍分辨率到4倍，每张卡片图片包含了图片高度和宽度），以及***actions*** - 一组卡片行为，用来描述点击卡片触发的行为（可以是deep link，也可以是桌面端或者移动端的网址）。

每个卡片会包含一组***card_click_urls***和一组***card_impression_urls***，客户端应该在相应的事件（比如卡片显示在屏幕上，用户点击卡片）调用这两个URL（GET操作）。对于impression URL，客户端应当遵照IAB标准来同时调用符合要求的URL，即：

***50%的卡片面积暴露至少1秒***

对于click URL，每当卡片被点击时就需要调用URL。

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
### 授权

授权机制基于***app_id***。
只有有效的***app_id***会返回结果。

App ID由Zowdow生成，作为产品介绍的一部分。

一个app_id只能用在一个操作系统上，如果API用户的产品发布在多个平台上，那么每个操作系统上的版本都有一个不同的app_id。
