# Rxjava的使用（所有示例基于rxjava2.0）

### Disposable的使用
[Disposable的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/DisposableExampleActivity.java)

Disposable类：
- dispose():主动解除订阅
- isDisposed():是否解除了订阅

CompositeDisposable的使用：快速接触所有添加的Disposable类，每当得到一个Disposable时就调用；
- CompositeDisposable.add()：将Disposable添加到容器中
- CompositeDisposable.clear():退出页面时调用该方法，可快速解除所有订阅

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

### 创建操作

##### Defer的使用
直到有观察者订阅的时候才创建Observable（通过使用Observable工厂方法生成一个新的Observable），并且为每个观察者创建一个新的Observable。
它对每个观察者都这样做，因此尽管每个订阅者都以为自己订阅的是同一个Observable，事实上每个订阅者获取的是它们自己单独的数据序列。
在某些情况下，等待直到订阅发生时才生成Observable，可以确保Observable包含最新的数据。

### 变换操作

##### Map操作符的使用
[Map的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/MapExampleActivity.java)

Map操作符对原始Observable发射的每一项数据应用一个你选择的函数，然后返回一个发射这些结果的Observable。
RxJava将这个操作符实现为map函数，这个操作符默认不在任何特定的调度器上执行。

##### Buffer的使用
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

### 结合操作

##### Zip的使用
[Zip的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/ZipExampleActivity.java)

zip操作符返回一个Observable,它使用这个函数按顺序结合两个或多个Observables发射的数据项，然后
它发射这个函数返回的结果；zip的最后一个参数接收每个Observable发射的数据，返回被压缩后的数据（最多可以有九个Observable参数）

##### Merge的使用
[Merge的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/MergeExampleActivity.java)
- merge：合并多个Observable的发射物；任何一个原始的Observable的onError通知会被立即传递给观察者，而且会终止合并后的Observable。
merge可能让合并的Observables发射的数据交错（类似的操作符Concat不会让数据交错，它会按顺序一个接一个发射多个Observables的发射物）
![](https://mcxiaoke.gitbooks.io/rxdocs/content/images/operators/merge.c.png)
- mergeWith: merge是静态方法，mergeWith是对象方法, Observable.merge(obser1, obser2)等价于Obser1.mergeWith(obser2)

### 过滤操作

##### Take的使用
[Take的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/TakeExampleActivity.java)

- take(n): 使用Take操作符可以修改Observable的行为，只发射前面的N项数据,然后发射完成通知，忽略剩余的数据。
(如果Observable发射的数据少于N项，take操作生成的Observable不会抛异常或发射onError通知，它会发射相同的少量数据)
![](https://mcxiaoke.gitbooks.io/rxdocs/content/images/operators/take.png)
- 变体take(long, TimeUnit): 这个变体接收的是一个时长而不是数量参数。它会发射Observable开始的那段时间发射的数据，时长和时间单位通过参数指定。
![](https://mcxiaoke.gitbooks.io/rxdocs/content/images/operators/take.t.png)
- akeLast（int）操作符：发射Observable的最后N项数据
![](https://mcxiaoke.gitbooks.io/rxdocs/content/images/operators/takeLast.c.png)
- 变体takeLast(long, TimeUnit): 这个变体接收的是一个时长而不是数量参数。它会发射Observable结束的那段时间发射的数据，时长和时间单位通过参数指定。

##### Filter的使用
[Filter的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/FilterExampleActivity.java)

![](https://mcxiaoke.gitbooks.io/rxdocs/content/images/operators/filter.c.png)
- Filter操作符，使用你指定的一个函数测试数据项，只有通过测试的数据才会发射。
- ofType是filter操作符的一个特殊形式，它过滤一个Observable只返回指定类型的数据。

##### Skip的使用
[Skip的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/SkipExampleActivity.java)

- skip(int): 你可以忽略Observable发射的前N项数据，只保留之后的数据。
![](https://mcxiaoke.gitbooks.io/rxdocs/content/images/operators/skip.c.png)
- skip（long, TimeUnit): skip的变体，这个变体接收一个时长，而不是数量参数。它会丢弃原始Observable开始的那段时间发射的数据，时长和时间单位通过参数指定
![](https://mcxiaoke.gitbooks.io/rxdocs/content/images/operators/skip.t.png)
- skipLast(int): 你可以忽略Observable发射的后N项数据，只保留之前的数据。
![](https://mcxiaoke.gitbooks.io/rxdocs/content/images/operators/skipLast.c.png)
- skipLast（long, TimeUnit): skipLast的变体，这个变体接收一个时长，而不是数量参数。它会丢弃原始Observable结束的那段时间发射的数据，时长和时间单位通过参数指定
![](https://mcxiaoke.gitbooks.io/rxdocs/content/images/operators/skipLast.t.png)


### 算术和聚合操作
[算术和聚合操作的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/ReduceExampleActivity.java)

##### concat、concatWith和merge操作符

![](https://mcxiaoke.gitbooks.io/rxdocs/content/images/operators/concat.c.png)

- concat：不交错的发射两个或多个Observable发射的数据
Concat操作符链接多个Observable的输出，第一个Observable发射的所有数据在第二个Observable发射的任何数据前面，以此类推。
- merge操作符和concat差不多，它结合两个或多个Observable的发射物，但是数据可能交错，而Concat不会让多个Observable的发射物交错。
- 还有一个实例方法叫concatWith，这两者是等价的：Observable.concat(a,b)和a.concatWith(b)。

##### reduce操作符

![](https://mcxiaoke.gitbooks.io/rxdocs/content/images/operators/reduce.c.png)
- reduce(Func2): 按顺序对Observable发射的每项数据应用一个函数并发射最终的值。
操作符对Observable发射数据的第一项应用到一个函数，然后再将返回的值与第二项数据一起传递给函数，以此类推，持续到最后一项数据并停止。并返回这个函数的最终值）
注意：如果原始Observable没有发射任何数据，reduce抛出异常IllegalArgumentException
- reduce(seed, Func2）：接受一个种子参数

### 连接操作

##### Replay
[Replay的示例](https://github.com/lxhAndSmh/MvpDemo/blob/todo-mvp-retrofit-rxjava/app/src/main/java/com/liu/mvpdemo/activity/operators/ReplayExampleActivity.java)
- 保证所有的观察者收到相同的数据序列，即使它们在Observable开始发射数据之后才订阅。
可连接的Observable（ConnectableObservable）与普通的Observable差不多，只不过它并不会在被订阅时开始发射数据，而是直接使用了Connect操作符时，才开始发射数据。
- 变体replay(int)和replay(long， TimeUnit):指定replay的最大缓存量。

