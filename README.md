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