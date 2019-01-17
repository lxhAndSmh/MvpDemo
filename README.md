# Rxjava的使用（所有示例基于rxjava2.0）

### Disposable的使用
[Disposable的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/DisposableExampleActivity.java)

Disposable类：
- dispose():主动解除订阅
- isDisposed():是否解除了订阅

CompositeDisposable的使用：快速接触所有添加的Disposable类，每当得到一个Disposable时就调用；
- CompositeDisposable.add()：将Disposable添加到容器中
- CompositeDisposable.clear():退出页面时调用该方法，可快速解除所有订阅
### 使用Flowable和reduce操作
reduce:按顺序对Observable发射的每项数据应用一个函数并发射最终的值。
操作符对Observable发射数据的第一项应用到一个函数，然后再将返回的值与第二项数据一起传递给函数，以此类推，持续到最后一项数据并停止。并返回这个函数的最终值）

### Single的使用
Single是Obserable的变种，它总是只发射一个值，或者一个错误的通知，而不是发射一系列的值。
因此，订阅Single只需要两个方法：
 - onSuccess Single发射单个值到这个方法
 - onError 如果无法发射需要的值，Single发射一个Throwable对象到这个方法

Single只会调用这两个方法中的一个，而且只会调用一次，调用了任何一个方法之后，订阅关系终止。
##### Single的操作符
[Single的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/SingleObserverActivity.java)

Single也可以组合使用多种操作，一些操作符让你可以混合使用Observable和single:
- compose: 返回值Single,作用是创建一个自定义的操作符。
- concat and concatWith: 返回值Observable或者Flowable,作用是连接多个Single和Observable发射的数据
- create: 返回值Single，调用观察者的create方法创建一个Single
- error: 返回值Single，返回一个立即给订阅者发射错误通知的Single
- flatMap: 返回值Single，返回一个Single，它发射对原Single的数据执行flatMap操作后的结果
- flatMapObservable: 返回值DisposableObserver,返回一个DisposableObserver，它发射对原Single的数据执行flatMap操作后的结果
- from: 返回值Single,将Future转换成Single
- just: 返回值Single,返回一个发射一个指定值的Single
- map: 返回值Single,返回一个Single，它发射对原Single的数据执行map操作后的结果
- merge and mergeWith: 返回返回FlowableSubscriber,合并发射来自多个Single的数据
- observeOn: 返回Single，指示Single在指定的调度程序上调用订阅者的方法
- onErrorReturn: 返回Single，将一个发射错误通知的Single转换成发射特定数据项的Single
- subscribeOn: 返回Single，指示Single在指定的调度程序上执行操作
- timeout: 返回Single，它给原有的Single添加超时控制，如果超时了就发射一个错误通知
- zip and zipWith: 返回Single，将多个Single转换为一个，后者发射的数据是对前者应用一个函数后的结果

##### Completable的使用
[Completable的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/CompletableObserverExampleActivity.java)

用于不需要知道任何返回值，只需要知道是否结束或错误
使用场景:例如请求接口更新服务端数据，我们只需要知道是否更新成功，不需要知道更新后返回的数据。

##### Map操作符的使用
[Map的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/MapExampleActivity.java)

Map操作符对原始Observable发射的每一项数据应用一个你选择的函数，然后返回一个发射这些结果的Observable。
RxJava将这个操作符实现为map函数，这个操作符默认不在任何特定的调度器上执行。

##### zip的使用
[Zip的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/ZipExampleActivity.java)

zip操作符返回一个Observable,它使用这个函数按顺序结合两个或多个Observables发射的数据项，然后
它发射这个函数返回的结果；zip的最后一个参数接收每个Observable发射的数据，返回被压缩后的数据（最多可以有九个Observable参数）

##### buffer的使用
[Buffer的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/BufferExampleActivity.java)
定期收集Observable的数据放进一个数据包裹，然后发射这些数据包裹，而不是一次发射一个值。
Buffer操作符将一个Observable变换成另一个，原来的Observable正常发射数据，变换产生的Observable发射这些数据的缓存集合；
每一个缓存至多包含来自原始Observable的count项数据（最后发射的数据可能少于count项）

 > 注意：如果原来的Observable发射一个onError通知，Buffer会立即传递这个通知，而不是首先发射缓存数据，即使在之前的缓存中
 > 包含了原始Observable发射的数据。

buffer(count, skip)从原始Observable的第一项数据开始创建新的缓存，用count项数据填充缓存（开头的一项和count-1项中间的数据，
然后以列表List的形式发射缓存，这些缓存可能有重叠部分（比如skip < count时），也可能有间隙（如：skip > count时)
buffer(count) 不传skip时，缓存的数据不会有重叠,等效于传递一个count相同值的skip
![](https://mcxiaoke.gitbooks.io/rxdocs/content/images/operators/buffer4.png)