# Zowdow API集成示例

© 2015-2017 Zowdow, Inc.

此示例的目的是向Android开发者展示如何在无需SDK的情况下直接接入Zowdow API接口。

## 版本

截止2017/2/7，示例版本为1.0.1。

## 概括

此示例程序展示了与Zowdow搜索推荐API的基本交互。更多API细节会在下文介绍。

## 架构

工程包含三个关键的包：

*   **injection** 由Dagger模块和组件构成。目前只有一个单一模块`NetworkModule`，它提供了Retrofit-service类（代表Zowdow API请求）的使用。为了方便工程的单元测试以及保持架构的整洁，我们使用了Dagger 2。`NetworkComponent`在`ZowdowDirectApplication`类里初始化。

*   **network** 由Retrofit-service类（如上面介绍）和Entity类（代表搜索推荐，卡片和广告）构成。

*   **ui** 被Activity类, adapters, 自定义视图和回调程序。程序工作流程中重要的活动是`HomeDemoActivity`。很多关键的事件比如搜索推荐加载都在这个类里发生。`WebViewActivity`和`VideoActivity`代表卡片的内容信息类型，可以是网页，也可以是视频。

*   **utils** 包含常数，简单的地理位置信息获取，运行时间的权限检查，观察网络连接状态，调用参数集合等有用的信息。

##与Zowdow API交互##

Unified API负责获取和处理搜索推荐数据。在示例程序中，数据通过开发者设置多种参数和关键词来获取。

我们实现了`UnifiedApiService`来展示Unified API的用法和一些追踪记录事件。

**API基URL**

```
https://u.zowdow.com/v1/
```

所有API端点常数都在`network/ApiBaseUrls`中。

**使用Unified API**

调用Unified API的示例可在`HomeDemoActivity`类中找到。

这个方法将API返回的搜索推荐原始数据转化成可供渲染的卡片以及相关数据。如果服务器返回成功，我们就将由UI线程来渲染这些处理好的数据。

```
    private void findSuggestions(String searchKeyWord) {
        Map<String, Object> queryMap = QueryUtils.createQueryMapForUnifiedApi(this, searchKeyWord, currentCardFormat);
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

**搜索推荐的可视化UI**

此示例程序默认使用`stream`作为carousel类型，即一串可以左右滑动的卡片。 更多carousel类型`mid_stream`, `stack`和`rotary`可通过SDK获取。

`SuggestionsAdapter`用来渲染搜索推荐。每一行搜索推荐由`SuggestionViewHolder`类封装，包含了卡片的RecyclerView。这些RecyclerView应该附带一个`CardsAdapter`实例。 

**卡片格式**

您还可以通过替换`card_format`值来动态地改变卡片格式。

所有卡片格式在`utils/constants/CardFormats`中声明。

**追踪记录**

我们用`clickUrl`和`impressionUrl`来进行卡片交互的追踪记录。只有在API调用里设置`tracking=1`才能看到这两项。

`clickUrl`用于卡片点击，`impressionUrl`用于卡片展示。当用户点击卡片/卡片展示给用户时，必须调用这两个URL，否则无法得到准确的变现信息。

卡片展示事件在`CardImageView`类中处理。

## 联系方式

如需技术支持请发送邮件至support@zowdow.com

## 感谢
