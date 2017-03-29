# Zowdow API集成示例

© 2015-2017 Zowdow, Inc.

Zowodow搜索推荐服务旨在向应用开发者提供在搜索场景下变现的可能性。

很多合作伙伴正在使用我们提供的可靠的，轻量级的安卓SDK。同时我们也提供直接API接口以方便接入服务（此示例程序）。

应用开发者需要一个app-id来集成和测试。可以发送邮箱至dev@zowdow.com申请app-id。

## 版本

截止2017/2/7，示例版本为1.0.1。

## 概括

此示例程序展示了与Zowdow搜索推荐API的基本交互。更多API细节会在下文介绍。

## 架构

工程包含三个关键的包：

*   **injection** 由Dagger模块和组件构成。目前只有一个单一模块`NetworkModule`，它提供了Retrofit-service类（代表Zowdow API请求）的使用。为了方便工程的单元测试以及保持架构的整洁，我们使用了Dagger 2。`NetworkComponent`在`ZowdowDirectApplication`类里初始化。

*   **network** 由Retrofit-service类（如上面介绍）和Entity类（代表搜索推荐，卡片和广告）构成。

*   **ui** 被Activity类, adapters, 自定义视图和回调程序。程序工作流程中重要的活动是`HomeDemoActivity`。很多关键的事件比如搜索推荐加载都在这个类里发生。`WebViewActivity`和`VideoActivity`代表卡片的内容信息类型，可以是网页，也可以是视频。

*   **tracking**负责记录与卡片有关的多种互动，例如卡片点击和卡片暴露。

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

`clickUrl`用于卡片点击，`impressionUrl`用于卡片暴露。当用户点击卡片/卡片暴露给用户时，必须调用这两个URL，否则无法得到准确的变现信息。

卡片暴露事件在`CardImageView`类中处理。

**卡片暴露记录**

如果在您的程序中用到这个示例程序，在记录卡片暴露时需要特别注意。我们使用MRC标准来记录卡片暴露，即只有至少50%的卡片面积暴露时间超过1秒时才记录。同时当同一张卡片出现在两个连续的请求中时，计时器不应被中断。我们创建了一个类来简化这个过程，方便在您的程序中使用。

下面是卡片暴露记录类的结构图。

![Class Diagram](ImpressionsTrackingUML.png)

您应该用`CardImpressionsTracker`类来记录。

1. 当API返回新的数据，调用`setNewCardsData`方法传递新的卡片（指定关键词的所有搜索推荐的所有卡片）。
2. 对所有至少50%面积暴露的卡片，调用`cardShown`方法。
3. 对所有当前不暴露的卡片或者暴露面积小于50%的卡片，调用`cardHidden`方法。每当卡片从暴露变为不暴露也需要调用。
4. 每当发生改变卡片暴露面积的事件时（比如界面滑动或者设备朝向改变），对所有发生暴露面积改变的卡片，相应地调用`cardShown`和`cardHidden`方法。

您可以参考`SuggestionViewHolder`类的`trackVisibleCard`方法来实现卡片暴露与否状态观察。在我们的示例中，每当获取新的卡片数据时和水平方向垂直方向滑动时，我们都会调用这个方法。

`CardImpressionsTracker`会负责同一个卡片出现在连续请求的例子。每个`CardImpressionInfo`实例包含了它所代表的卡片的暴露情况以及由`cardShown`和`cardHidden`方法管理的计时器。一旦计时器结束，卡片暴露会被立刻记录。卡片暴露记录由`TrackingRequestManager`的`trackCardImpression`方法完成。`TrackingRequestManager`是一个声明了基本的卡片记录行为的接口。这些行为可以在实现了这个接口的任意类中根据现有的程序结构改变。

## 联系方式

如需技术支持请发送邮件至support@zowdow.com

## 感谢
