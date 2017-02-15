### 简介 - 统一搜索推荐API

本文档简单介绍了Zowdow搜索推荐API的用法。 API根据用户输入的片段、字、词返回搜索推荐以及相关的卡片内容，也可以用完整的关键词当参数来获取更多的相关关键词和内容。

搜索推荐API的地址为：

```
u1.quick1y.com/v1/unified
```

一个API调用的**关键**参数是：
  * ***app_id*** -- 调用API的App名字
  * ***q*** -- 用来查询的片段、字、词
  * ***device_id*** -- 客户端唯一标识符
  
如果只设置这些参数，通常API会返回一些通用的(与区域/地理位置/时间/平台无关)内容。想要获得更多更具体的内容，需要设置下面的参数。

跟客户端位置信息有关的参数：
* ***cip*** -- 客户端IP地址。没有这个参数，我们无法定位客户端所在的国家地区，所以只有与国家地区无关的卡片会被返回。如果cip无法获取，可替代的参数是***country_code***。
* ***lat*** and ***long*** -- 客户端所在的纬度和经度坐标。我们需要这些参数来返回本地化的内容（与具体位置相关的卡片，比如附近的商店、活动等）。没有经度和纬度，只有非本地化的内容会被返回。
* ***locale*** -- 用来设置卡片的语言。
* ***timezone*** -- 用来进行跟时间段（早上/下午/晚上）相关的卡片时间的计算。

跟内容选择有关的参数：
* ***os*** -- iOS或Android（用来筛选App推荐，也可以通过***app_id***来设置默认值）。
* ***screen_scale*** -- 用来改变卡片格式（以及监测某一特定***app_id***的卡片大小）。
* ***app_ver*** -- 应用的版本号
* ***app_build*** -- 应用的build号
* ***system_ver*** -- 设备使用的操作系统版本
* ***device_model*** -- 设备的型号
* ***network*** -- 网络环境（3G/4G/LTE/WiFi）
* ***keyboard*** -- 当前使用的键盘名称

最后，跟踪记录卡片显示和卡片点击事件的参数：
* ***tracking*** = 1 --API返回的每个卡片元数据里会包含两个URL，一个显示URL一个点击URL

用curl命令测试的调用示例以及返回数据：

```json
curl -XGET 'u1.quick1y.com/v1/unified?app_id=com.zowdow.zow&q=am&tracking=1'  | python -mjson.tool
```

返回数据是JSON格式的搜索推荐以及卡片的载荷。***_meta***部分返回的是搜索推荐的概要信息（***count*** - 推荐的关键词数量，***rid*** - 调用请求id，***status*** - 状态信息（通常是"SUCCESS"），***ttl*** - 数据存活时间（以秒为单位，只有成功返回数据的时候才会显示））。 ***rid***可以用来跟踪记录客户端在某个特定搜索推荐的行为。

***records***部分是一组***suggestion***数据，每一项***suggestion***数据都有一个代表卡片数量的键***cardCount***。每一个卡片都包含***rank*** - 推荐显示顺序，***card_format*** - 大小及方向（水平或垂直），***id*** - 唯一性的识别码，以及最多四张不同分辨率的卡片图片（基于大小/像素密度需求，从1倍分辨率到4倍，每张卡片图片包含了图片高度和宽度），以及***actions*** - 一组卡片行为，用来描述点击卡片触发的行为（可以是deep link，也可以是桌面端或者移动端的网址）。

如果***tracking*** 赋值为 1，每个卡片会包含一个***card_click_url***键和一个***card_impression_url***键，客户端应该在卡片显示和卡片点击时分别调用这两个URL。

```json
{{
    "_meta": {
        "count": 5,
        "rid": "27bdd035-e503-4d60-cd1c-d41d158c0f65",
        "status": "SUCCESS",
        "ttl": 3600
    },
    "records": [
        {
            "suggestion": {
                "cardCount": 6,
                "cards": [
                    {
                        "actions": [
                            {
                                "action_target": "http://api.airfind.com/link/v1?id=582defff28efb9f9126268ae&clientId=50040&brand=zow",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 1,
                        "card_click_url": "https://u1.quick1y.com/r/c/6711d788f1e452974336b730d20b30a6527aca52bffa85f6855aba1fbbf2bc5852a85f1a2a0f38c146f8d41cfe21af8cfe08d00091756f291b3cdecbc8600be5",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/a5760b0a8c171d35a337d9d845727352bbbdbbce7a518d9de6403ece6620f6f0c6e38cc3e5cc9e2a788252758d85edbc691d1e3b8d4701cc54d062215cb21f50",
                        "id": "manual8379E213D627CB1CE7EC4A46830C8309",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/manual/081116/a/0A32_inline_manual_8379E213D627CB1CE7EC4A46830C8309_1478577421232_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/manual/081116/a/0A32_inline_manual_8379E213D627CB1CE7EC4A46830C8309_1478577421232_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/manual/081116/a/0A32_inline_manual_8379E213D627CB1CE7EC4A46830C8309_1478577421232_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/manual/081116/a/0A32_inline_manual_8379E213D627CB1CE7EC4A46830C8309_1478577421232_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "http://rover.ebay.com/rover/1/711-53200-19255-0/1?icep_ff3=1&pub=5575158256&toolid=10001&campid=5337946565&customid=zow&ipn=psmain&icep_vectorid=229466&kwid=902099&mtid=824&kw=lg",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 2,
                        "card_click_url": "https://u1.quick1y.com/r/c/44b47db318b304c25fe9c07f712fabbc6dd7a50ab2d5a42fa881b7af3af4da4bb7bba59e04533ca801fa2c91190c577d8cb0ad2ec6848657073ae7469aa2c36b",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/23e17643b8153e3ce7c40dba95fc0c79d2e9468ef7f5cfbee76a342fd5eb3beca04737ed8613ea3452671d64ecc369e5a8e4f9a3c1c13e104f9eb89e2bf65c22",
                        "id": "manual9B228C3CCA5FD26764F6957F95904452",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/manual/081116/a/5cF8_inline_manual_9B228C3CCA5FD26764F6957F95904452_1478577488508_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/manual/081116/a/5cF8_inline_manual_9B228C3CCA5FD26764F6957F95904452_1478577488508_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/manual/081116/a/5cF8_inline_manual_9B228C3CCA5FD26764F6957F95904452_1478577488508_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/manual/081116/a/5cF8_inline_manual_9B228C3CCA5FD26764F6957F95904452_1478577488508_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "http://linksynergy.walmart.com/fs-bin/click?id=0i28BiDB1/w&subid=&offerid=223073.1&type=10&tmpid=1082&u1=zow&RD_PARM1=https%253A%252F%252Fwww.walmart.com%252F",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 3,
                        "card_click_url": "https://u1.quick1y.com/r/c/85c6316950de33c099caa3e6275bfa14908b339080f5837ea5dac9b87ff290d446fa137e0ae64f4b1ce300c3651c0ed83b6263090da780b35f9f36960c2fa2aa",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/7b0f6ab69579dbcff5b3eefe6760938fca4260d5053c65dafd991848b21e7db860cfeb365205f1b780bda39083637d886693dab0d28de1e2765adaca72c8fec5",
                        "id": "manualCDE0FD2300EF06844B237483B1501DD3",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/manual/081116/f/d3ac_inline_manual_CDE0FD2300EF06844B237483B1501DD3_1478577647730_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/manual/081116/f/d3ac_inline_manual_CDE0FD2300EF06844B237483B1501DD3_1478577647730_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/manual/081116/f/d3ac_inline_manual_CDE0FD2300EF06844B237483B1501DD3_1478577647730_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/manual/081116/f/d3ac_inline_manual_CDE0FD2300EF06844B237483B1501DD3_1478577647730_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "http://goto.target.com/c/280223/81938/2092?subId1=zow",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 4,
                        "card_click_url": "https://u1.quick1y.com/r/c/e9b6a744552ca16246d9c29530aad106915a71668914baa5f0281eaf7d5c0750d433557c18f7b35d1d2fcf87a5810614aae0dbc02a033f505f18546075783af1",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/66a5311731607f1f329efcd97f3e0a26b1835a039df4cc0844edcc67a46263bc8be5e1672f31852d6641ee72707603fdf0035cf8cc4a77e9f3faccc8a9fe2cda",
                        "id": "manual9C0F0E0BA05747AA93070EB9DBBE3B43",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/manual/081116/b/A927_inline_manual_9C0F0E0BA05747AA93070EB9DBBE3B43_1478577491022_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/manual/081116/b/A927_inline_manual_9C0F0E0BA05747AA93070EB9DBBE3B43_1478577491022_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/manual/081116/b/A927_inline_manual_9C0F0E0BA05747AA93070EB9DBBE3B43_1478577491022_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/manual/081116/b/A927_inline_manual_9C0F0E0BA05747AA93070EB9DBBE3B43_1478577491022_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "http://www.craigslist.org",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 5,
                        "card_click_url": "https://u1.quick1y.com/r/c/78c44978b2d5f85982cd8769dc0d01aeee9e7520da5b78bc87c880c12b95de2a23bf1fbd8c088cad07a33b9c5676fe71730f75c0ffa72b73fe3be84dbbdde7aa",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/8327773b5f8f17c34d0fc402e9224812596ceb10d620e134865243d2780a2577a6dc64aa67cb2ae16adbaff63880333274f1e2e67f236fa64d07fb1ba48aad5f",
                        "id": "manual66C90B56F3C8E5284B1D3206407CBAED",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/manual/081116/0/9FF0_inline_manual_66C90B56F3C8E5284B1D3206407CBAED_1478577341890_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/manual/081116/0/9FF0_inline_manual_66C90B56F3C8E5284B1D3206407CBAED_1478577341890_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/manual/081116/0/9FF0_inline_manual_66C90B56F3C8E5284B1D3206407CBAED_1478577341890_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/manual/081116/0/9FF0_inline_manual_66C90B56F3C8E5284B1D3206407CBAED_1478577341890_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "http://www.costco.com/",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 6,
                        "card_click_url": "https://u1.quick1y.com/r/c/43e6263b8ab84478ec19a9681833e1fff93fa71111300beeba3c504e7ead77de70ccfe81cdcdca10b8f6eb615acf2dfb73f3b7e2e2041cbee488d545307dc026",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/ebe5472610f206e6f45fdf64e3f11f49e2fff5f25a5297cf8cfd444554d3b925f6be0588d18af573055d0e5bb6c92e39a5172cbce3c73dbd2cb9887761b14eaa",
                        "id": "manual809F1BD9E7FAF9341916C2ECD67E1757",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/manual/081116/e/D44C_inline_manual_809F1BD9E7FAF9341916C2ECD67E1757_1478577413802_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/manual/081116/e/D44C_inline_manual_809F1BD9E7FAF9341916C2ECD67E1757_1478577413802_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/manual/081116/e/D44C_inline_manual_809F1BD9E7FAF9341916C2ECD67E1757_1478577413802_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/manual/081116/e/D44C_inline_manual_809F1BD9E7FAF9341916C2ECD67E1757_1478577413802_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    }
                ],
                "id": 1727617,
                "queryFragment": "am",
                "suggRank": 0,
                "suggestion": "Amazon.com"
            }
        },
        {
            "suggestion": {
                "cardCount": 3,
                "cards": [
                    {
                        "actions": [
                            {
                                "action_target": "http://www.aol.com/",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 1,
                        "card_click_url": "https://u1.quick1y.com/r/c/b42ffa8d149a4a72f6b0a56786f71ceacfee8fdd8a4c72a7544f5f8f881d6c596e7e5f9a5bbfd21bf8493bc05ffacadc94e52bef15dabbdd6b5717528f77f3bd",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/f77311f3837327b89db1d27be32609ba3c5ada5b0bfd5d768c3c6f01d7277f580575c3f3b91bf96c86127313ef0c1955e6b76492ee5e8bdb3ef85b811cc69356",
                        "id": "manualE25E99600883F8F3EA1EF70CCE8784B7",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/manual/081116/b/85c8_inline_manual_E25E99600883F8F3EA1EF70CCE8784B7_1478577710306_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/manual/081116/b/85c8_inline_manual_E25E99600883F8F3EA1EF70CCE8784B7_1478577710306_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/manual/081116/b/85c8_inline_manual_E25E99600883F8F3EA1EF70CCE8784B7_1478577710306_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/manual/081116/b/85c8_inline_manual_E25E99600883F8F3EA1EF70CCE8784B7_1478577710306_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "https://mail.aol.com/",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 2,
                        "card_click_url": "https://u1.quick1y.com/r/c/aa00b95fb2a4f6eef93e95ce373a45b0cf1e3c239fcd3d6073d8228a0a8559d8620e42bcc32dbf3881b5fdff380f4b62cf8c57cb0a612f62d1bd414b06ca67cc",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/466d0b9fa9b6fb2816dfb2c84f1090f85d8c55abe54f005ad5d947d5158bdc75b072430cb0b8c76418f617cbc564f89b56ff0cddf8df734a49f6c18fac8495b7",
                        "id": "manual4FB7B38F258D8F967DADC7DECF6B688A",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/manual/081116/9/DEaa_inline_manual_4FB7B38F258D8F967DADC7DECF6B688A_1478577275116_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/manual/081116/9/DEaa_inline_manual_4FB7B38F258D8F967DADC7DECF6B688A_1478577275116_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/manual/081116/9/DEaa_inline_manual_4FB7B38F258D8F967DADC7DECF6B688A_1478577275116_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/manual/081116/9/DEaa_inline_manual_4FB7B38F258D8F967DADC7DECF6B688A_1478577275116_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "http://www.aol.com/entertainment/",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 3,
                        "card_click_url": "https://u1.quick1y.com/r/c/9d40fb4dd7a701c3f37b5d2341e190579e290ddc9747aec270ee20f6176d310f3f1c60530ad5a7cc3daea034e69f06df54dc8b9a5604c30fe8a3c2840241e9ab",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/27b48d4017d68c68bdd001c089897c3ce61238a4f885480ec7b20699f0d60cfceaf71d91470916fd3a7a38157f677ff5d9e95b7d9181ff0af6419884aaa10922",
                        "id": "manual24A99760CAE40D2BCB35C7B27FD97B00",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/manual/081116/7/7E37_inline_manual_24A99760CAE40D2BCB35C7B27FD97B00_1478577148083_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/manual/081116/7/7E37_inline_manual_24A99760CAE40D2BCB35C7B27FD97B00_1478577148083_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/manual/081116/7/7E37_inline_manual_24A99760CAE40D2BCB35C7B27FD97B00_1478577148083_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/manual/081116/7/7E37_inline_manual_24A99760CAE40D2BCB35C7B27FD97B00_1478577148083_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    }
                ],
                "id": 6529661,
                "queryFragment": "am",
                "suggRank": 1,
                "suggestion": "America Online"
            }
        },
        {
            "suggestion": {
                "cardCount": 4,
                "cards": [
                    {
                        "actions": [
                            {
                                "action_target": "http://www.cbsnews.com/news/carl-paladino-buffalo-school-board-makes-moves-to-oust/",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 1,
                        "card_click_url": "https://u1.quick1y.com/r/c/144d6751f62c156115cd25e38555d480a907d4c1214ebc5d2daa5e6cd5820fa5f021bb2aadd9be89f5b1b9832dd1fc4c45f4ea912d6a4f26e811683eb06f3287",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/e6e2c264e4678ef132a15d7edf65f1af2f5fa8463b8feec5315133ae8fd4f059f5860732561f5cfb0267f8d2d29582edb4b4565401cc3325e488ea4342191c39",
                        "id": "newsi1dac350045b1",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/news/301216/8/984f_inline_news_newsi1dac350045b1_1483096203207_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/news/301216/8/984f_inline_news_newsi1dac350045b1_1483096203207_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/news/301216/8/984f_inline_news_newsi1dac350045b1_1483096203207_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/news/301216/8/984f_inline_news_newsi1dac350045b1_1483096203207_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "http://www.politico.com/www.politico.eu/article/russia-has-not-closed-american-school-fake-news-maria-zakharova/",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 2,
                        "card_click_url": "https://u1.quick1y.com/r/c/1ae723fc3e643906d9cee52e1d3c9d9cb6eae1b62d813e3fc46f66b27c62e9b8a0cb070b50c88e354fab49218931ee5172deb5c67a81110d5d6d9d5a54dcdb33",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/2e67fddae4ff4bab08a207fba5ee6c89494f8f49b60ceaa83317addf0bb6c562730b86f3aa2f17e5b15e2d68720b16c8e144a5705205e084c51e0c53c67640bc",
                        "id": "newsi3b54e4555a51",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/news/301216/e/a0cd_inline_news_newsi3b54e4555a51_1483099770541_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/news/301216/e/a0cd_inline_news_newsi3b54e4555a51_1483099770541_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/news/301216/e/a0cd_inline_news_newsi3b54e4555a51_1483099770541_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/news/301216/e/a0cd_inline_news_newsi3b54e4555a51_1483099770541_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "http://www.cbsnews.com/news/carl-paladino-buffalo-school-board-makes-moves-to-oust/",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 3,
                        "card_click_url": "https://u1.quick1y.com/r/c/3c405e85aa655047e3c6e43e60288cdb2ca19d576ababb0d1b57960a3ba169625229eb1654ca44277ce1f6908004a0d6e15a29424693b743059193e70d9b844b",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/75c079506b2d693db019f7a6edd02b32a882780e358df04f1998b982e5fd2bf6c29badec9a866e8739badb5b572c1c4d2ee3c02e279152f3330ec5e5553013f6",
                        "id": "newsi7724c690c116",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/news/301216/e/bfFb_inline_news_newsi7724c690c116_1483096183251_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/news/301216/e/bfFb_inline_news_newsi7724c690c116_1483096183251_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/news/301216/e/bfFb_inline_news_newsi7724c690c116_1483096183251_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/news/301216/e/bfFb_inline_news_newsi7724c690c116_1483096183251_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "http://www.politico.com/story/2016/12/russia-american-school-sanctions-233048",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 4,
                        "card_click_url": "https://u1.quick1y.com/r/c/63806f1f7633d97e53fb9dc357b2a89800d500e81e62c0563b88fa8b0672bd16cef108ce11ade0214e5a3665eb886488d854c7a7fc4d0c8516c66bc07bed5b63",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/80d49717b694d622a21760ab0a5d17d91ece3b2235e8fb6474881688d94dbe008df0a4f2c1faa01fccbc1b618732977f17e0cbef8c12f2096a961bb2304aa51f",
                        "id": "newsiac4d16d39aed",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/news/301216/4/D594_inline_news_newsiac4d16d39aed_1483059870249_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/news/301216/4/D594_inline_news_newsiac4d16d39aed_1483059870249_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/news/301216/4/D594_inline_news_newsiac4d16d39aed_1483059870249_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/news/301216/4/D594_inline_news_newsiac4d16d39aed_1483059870249_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    }
                ],
                "id": 6671687,
                "queryFragment": "am",
                "suggRank": 2,
                "suggestion": "american school"
            }
        },
        {
            "suggestion": {
                "cardCount": 2,
                "cards": [
                    {
                        "actions": [
                            {
                                "action_target": "https://play.spotify.com/artist/6Q192DXotxtaysaqNPy5yR",
                                "action_type": "web_url"
                            },
                            {
                                "action_target": "spotify:artist:6Q192DXotxtaysaqNPy5yR",
                                "action_type": "deep_link"
                            }
                        ],
                        "cardRank": 1,
                        "card_click_url": "https://u1.quick1y.com/r/c/5544f91d5fd1cfae5504fda3a66f90222b8b481446cdfccabe0b907da1d7601283d129ca02e327cf9e3da55d631bb3f71b245f0b28ca4759390565f2d0fc5798",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/b5dd67a86a08976b25e67373d2821f532d1e1e136e93220a86a363297f562c4f9e320093b76dff6e9c6cbe121246fcc4033c7e37d8d11877aa967c9af3c90504",
                        "id": "spotifyartist6Q192DXotxtaysaqNPy5yR",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/spotifyartist/111116/b/10fd_inline_spotify_artist_6Q192DXotxtaysaqNPy5yR_1478828444739_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/spotifyartist/111116/b/10fd_inline_spotify_artist_6Q192DXotxtaysaqNPy5yR_1478828444739_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/spotifyartist/111116/b/10fd_inline_spotify_artist_6Q192DXotxtaysaqNPy5yR_1478828444739_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/spotifyartist/111116/b/10fd_inline_spotify_artist_6Q192DXotxtaysaqNPy5yR_1478828444739_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "http://en.wikipedia.org/wiki/Amy_Winehouse",
                                "action_type": "web_url"
                            },
                            {
                                "action_target": "http://www.amywinehouse.com",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 2,
                        "card_click_url": "https://u1.quick1y.com/r/c/9fb2a504199bab2d3fb958a8d8c261ede7881001cf9bd3bf2be06dc5ec2b4749fee1c72abfa6cc9a47417fdf9aa3180a8b67c2f66cdcf80c29b025626a07d3b3",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/6f132d5c09f1044d2ff1493ce531df950f39aa87f54cabcbbd10b50b6b42277a69bca7012c0ac2a9af2a7c3a0a1b338470bcebe1c92c22546a1ec94edc0e4808",
                        "id": "wikipediaperson939583_3C745",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/wikipedia/091116/6/d2f9_inline_wikipedia_person_wikipediaperson939583_3C745_1478667108995_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/wikipedia/091116/6/d2f9_inline_wikipedia_person_wikipediaperson939583_3C745_1478667108995_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/wikipedia/091116/6/d2f9_inline_wikipedia_person_wikipediaperson939583_3C745_1478667108995_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/wikipedia/091116/6/d2f9_inline_wikipedia_person_wikipediaperson939583_3C745_1478667108995_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    }
                ],
                "id": 2008464,
                "queryFragment": "am",
                "suggRank": 3,
                "suggestion": "Amy Winehouse"
            }
        },
        {
            "suggestion": {
                "cardCount": 5,
                "cards": [
                    {
                        "actions": [
                            {
                                "action_target": "https://play.spotify.com/artist/66N53pjZxSlsUb8gxgmVvy",
                                "action_type": "web_url"
                            },
                            {
                                "action_target": "spotify:artist:66N53pjZxSlsUb8gxgmVvy",
                                "action_type": "deep_link"
                            }
                        ],
                        "cardRank": 1,
                        "card_click_url": "https://u1.quick1y.com/r/c/d139c6416c870b8a437dc62eb78ff3eef29861adc9c248ee9291c99d7f2966efbdaca73b9c5bc898c7b4e5431c244aa222a79632a2f402f03080aaad900e7e88",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/3315814ec9e2ee1c9c6944bf49d51689064ef46ceeff0f12abc388566e0cb58457cf8572621b495d9687aecdfaf984d4a98132805afe295b0a538b3150dbc32b",
                        "id": "spotifyartist66N53pjZxSlsUb8gxgmVvy",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/spotifyartist/111116/2/686C_inline_spotify_artist_66N53pjZxSlsUb8gxgmVvy_1478827263393_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/spotifyartist/111116/2/686C_inline_spotify_artist_66N53pjZxSlsUb8gxgmVvy_1478827263393_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/spotifyartist/111116/2/686C_inline_spotify_artist_66N53pjZxSlsUb8gxgmVvy_1478827263393_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/spotifyartist/111116/2/686C_inline_spotify_artist_66N53pjZxSlsUb8gxgmVvy_1478827263393_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "https://play.spotify.com/track/1R1VxcIQva6mAFlfzmJ6he",
                                "action_type": "web_url"
                            },
                            {
                                "action_target": "spotify:track:1R1VxcIQva6mAFlfzmJ6he",
                                "action_type": "deep_link"
                            },
                            {
                                "action_target": "https://p.scdn.co/mp3-preview/efd0905bef90db538d3137fa7a5d5a30fffaee4d",
                                "action_type": "music_preview"
                            }
                        ],
                        "cardRank": 2,
                        "card_click_url": "https://u1.quick1y.com/r/c/a45eb27aa62efa917ba58dfcc90179a095f2c1e81dd73686991396581ecdcba88c3bbe1e63e9c3ff13ee299ed93c56dd98ed9b8b8bf766de0cc6d53c51ee7416",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/b0c5e6649334f83cf2f1b55fa4fd90947db27d2e22f391abfc5c1347de29cb6a08fea2b217f27d881488cc24a3417417bcdc79e1551875781e9a5b9b4d9d79cc",
                        "id": "spotifytrack1R1VxcIQva6mAFlfzmJ6he",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/spotifytrack/111116/d/CA2e_inline_spotify_track_1R1VxcIQva6mAFlfzmJ6he_1478889472868_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/spotifytrack/111116/d/CA2e_inline_spotify_track_1R1VxcIQva6mAFlfzmJ6he_1478889472868_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/spotifytrack/111116/d/CA2e_inline_spotify_track_1R1VxcIQva6mAFlfzmJ6he_1478889472868_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/spotifytrack/111116/d/CA2e_inline_spotify_track_1R1VxcIQva6mAFlfzmJ6he_1478889472868_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "https://play.spotify.com/album/1p3Pp4y38Ybq4rjTlS2exu",
                                "action_type": "web_url"
                            },
                            {
                                "action_target": "spotify:album:1p3Pp4y38Ybq4rjTlS2exu",
                                "action_type": "deep_link"
                            }
                        ],
                        "cardRank": 3,
                        "card_click_url": "https://u1.quick1y.com/r/c/271895ef3e444517b37e94c607a11c7de270eee41307e795eaa07f2a16bf805a3cb332a2f8b4989f8e67020472b590011ffa2eaabd103a9684125aa0795c3f07",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/f8740bb6dbd50e5e56f6eefe5b4cc6c29121c79b079a76dfa3e67b4fb70fbf9c31e432166da9260634226e6116373f485d651865f682b5efc225eea9fa047eef",
                        "id": "spotifyalbum1p3Pp4y38Ybq4rjTlS2exu",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/spotifyalbum/111116/b/3dEf_inline_spotify_album_1p3Pp4y38Ybq4rjTlS2exu_1478834804343_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/spotifyalbum/111116/b/3dEf_inline_spotify_album_1p3Pp4y38Ybq4rjTlS2exu_1478834804343_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/spotifyalbum/111116/b/3dEf_inline_spotify_album_1p3Pp4y38Ybq4rjTlS2exu_1478834804343_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/spotifyalbum/111116/b/3dEf_inline_spotify_album_1p3Pp4y38Ybq4rjTlS2exu_1478834804343_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "https://play.spotify.com/album/5aU9RHknenpN2YnLTkr9jU",
                                "action_type": "web_url"
                            },
                            {
                                "action_target": "spotify:album:5aU9RHknenpN2YnLTkr9jU",
                                "action_type": "deep_link"
                            }
                        ],
                        "cardRank": 4,
                        "card_click_url": "https://u1.quick1y.com/r/c/f95d087849d796eecb6517820630e48091b7965960a833a4a473a38af029c83ae2548cf5878eae29ba9c5e8037836393a1c941fefce37014b5e02c7703043a3e",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/593716153c2a902d4bfddeeab70c300c48cfe343e4432fdcb911dd949c1f26c93733560004db315e82df8fcb740b1270c7e4e1a00976caec0548be648580eea8",
                        "id": "spotifyalbum5aU9RHknenpN2YnLTkr9jU",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/spotifyalbum/111116/2/8789_inline_spotify_album_5aU9RHknenpN2YnLTkr9jU_1478842683558_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/spotifyalbum/111116/2/8789_inline_spotify_album_5aU9RHknenpN2YnLTkr9jU_1478842683558_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/spotifyalbum/111116/2/8789_inline_spotify_album_5aU9RHknenpN2YnLTkr9jU_1478842683558_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/spotifyalbum/111116/2/8789_inline_spotify_album_5aU9RHknenpN2YnLTkr9jU_1478842683558_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    },
                    {
                        "actions": [
                            {
                                "action_target": "http://finance.yahoo.com/q?s=AMG",
                                "action_type": "web_url"
                            }
                        ],
                        "cardRank": 5,
                        "card_click_url": "https://u1.quick1y.com/r/c/b7c2f01491aa97fc49901d07562761426146068dd7cf30123bf46059b1fa99053fe69d643621d7ea97da1da78f5510321443a18af497e7936e9d9c15ad7d2cd8",
                        "card_format": "inline",
                        "card_impression_url": "https://u1.quick1y.com/r/i/517d6b52fe925960bd6e45888d26ae5a31750047ac5410c6e92deabf539d40c5e11a2dc921e97c75e531671e50984beb4c2fa66a397dfb4b493991de9f7da4af",
                        "id": "stockAMG",
                        "x1": "http://d1jh24layu79ld.cloudfront.net/stock/081116/e/ef06_inline_stock_stockAMG_1478646280046_x1.jpeg",
                        "x1_h": 40,
                        "x1_w": 170,
                        "x2": "http://d1jh24layu79ld.cloudfront.net/stock/081116/e/ef06_inline_stock_stockAMG_1478646280046_x2.jpeg",
                        "x2_h": 80,
                        "x2_w": 340,
                        "x3": "http://d1jh24layu79ld.cloudfront.net/stock/081116/e/ef06_inline_stock_stockAMG_1478646280046_x3.jpeg",
                        "x3_h": 106,
                        "x3_w": 453,
                        "x4": "http://d1jh24layu79ld.cloudfront.net/stock/081116/e/ef06_inline_stock_stockAMG_1478646280046_x4.jpeg",
                        "x4_h": 160,
                        "x4_w": 680
                    }
                ],
                "id": 2021071,
                "queryFragment": "am",
                "suggRank": 4,
                "suggestion": "AMG"
            }
        }
    ]
}

```
### 永远返回200状态码

通常来说API每次都返回200状态码，无论参数有不有效。返回的内容可能是空的（没有搜索建议或者卡片），但是200状态码总是会返回。

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
### 让事情变得简单

授权机制基于***app_id***。
只有有效的***app_id***会返回结果。
