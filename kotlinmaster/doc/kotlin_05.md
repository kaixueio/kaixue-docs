# Kotlin 的协程用力瞥一眼

码上开学 Kotlin 系列的文章，协程已经是第五期了，这里简单讲一下我们（扔物线和即刻 Android 团队）出这套 Kotlin 上手指南的一些原则：

- 官方文档有指定的格式，因为它是官方的，必须面面俱到，写作顺序不是由浅入深，不管你懂不懂，它都得讲。
- 网上的文章大都是从作者自身的角度出发，把自己已经掌握的知识教授与人，但是真正从读者的需求出发的少之又少，无法抓住读者的痛点，能够读完已经是很不容易了。
- 疲劳度是这一系列的一个重要的衡量指标，文章中出现大段大段的代码，疲劳度会急剧上升，不容易集中精神，甚至中途放弃。

我们期许基于上述的原则，把技术文章写得更加轻松易读，激发读者学习的兴趣，真正实现「上手」。

协程在 Kotlin 中是非常特别的一部分，和 Java 相比，它是一个新颖的概念。宣扬它的人都在说协程是多么好多么棒，但就目前而言不管是官方文档还是网络上的一些文章都让人难以读懂。

造成这种「不懂」的原因和大多数人在初学 RxJava 时所遇到的问题其实是一致的：对于 Java 开发者来说这是一个新东西。下面我们从「协程是什么」开始说起。

### 协程是什么

协程其实并不是 Kotlin 提出来的新概念，其他的一些编程语言，例如：Lua、Go、C#、Python 等都可以在语言层面上实现协程，甚至是 Java，也可以通过使用扩展库来间接地支持协程。

当在网上搜索协程时，我们会看到：

- Kotlin 官方文档上介绍“本质上，协程是轻量级的线程”
- 很多博客上所提到的”不需要从「用户态」切换到「内核态」“、”是「协作式」的“等等

作为 Kotlin 协程的初学者，这些概念或许并不是那么容易让人理解。毕竟这些权威的解释往往是设计者经过长时间的总结提炼出来的，只看结果，而不管过程就不能理解协程。

「协程 Coroutine」源自 `Simula` 和 `Modula-2` 语言，这个术语早在 1958 年就被 [Melvin Edward Conway]([https://zh.wikipedia.org/wiki/%E9%A9%AC%E5%B0%94%E6%96%87%C2%B7%E5%BA%B7%E5%A8%81](https://zh.wikipedia.org/wiki/马尔文·康威)) 发明并用于构建汇编程序。

协程设计的初衷是为了让 「协作式多任务」 表示起来更加方便。所谓协作式多任务，和「线程」是有差别的。线程是操作系统层面的一个概念，表示能够进行运算调度的最小单元，没有包含任务的分发与合并处理。而协作式多任务是一个更具体的概念，它并没有直接和任何操作系统相关联，关心的是任务的分发、 分发的数量和子任务的合并等这些更明确的操作。

我们学习 Kotlin 中的协程，一开始并不需要了解这么多概念。在 Kotlin 中，协程的一个非常典型的使用场景就是线程控制。就像 Java 中的 `Executor` 和 Android 中的 `AsyncTask`，Kotlin 中的协程也有对 Thread API 的封装，让我们可以在写代码时，不用关注多线程就能够很方便地写出并发操作。

在 Java 中要实现并发操作通常需要开启一个 `Thread` ：

```java
☕️
new Thread(new Runnable() {
    @Override
    public void run() {
        ...
    }
}).start();
```

这里仅仅只是开启了一个新线程，至于它何时结束、执行结果怎么样，我们在主线程中是无法直接知道的。

我们再看一下在 Kotlin 中是如何处理并发操作的。同样，我们先通过线程的方式去写：

```kotlin
🏝️
Thread({
    ...
}).start()
```

可以看到，相比 Java 摆脱不了直接使用线程的那些困难和不方便：

- 难以感知到线程什么时候执行结束
- 线程间的相互通信
- 多个线程的管理

为了解决以上的痛点，或许你会想到用 `executor`：

```kotlin
🏝️
val executor = Executors.newCachedThreadPool()
executor.execute({
    ...
})
```

又或许是用 Android 自带的 `AsyncTask` 去控制：

```kotlin
🏝️
object : AsyncTask<T0, T1, T2> { 
    override fun doInBackground(vararg args: T0): String {
        ...
    }
  
    override fun onProgressUpdate(vararg args: T1) {
        ...
    }
  
    override fun onPostExecute(t3: T3) {
        ...
    }
}
```

`AsyncTask` 是 Android 对线程池 `Executor` 的封装，但它的缺点也很明显：

- 需要处理很多回调
- 使用起来没有 `Executor` 灵活

看到这里你可能会想到使用 `RxJava` 解决回调地狱，它也可以很方便地实现并发逻辑。

`RxJava` 是一种响应式程序框架，通过它所提供的链式调用可以很好地消除回调，但这并不是本篇文章所讲述的重点，读者如果对它不熟悉并想进一步了解可以去[官网](http://reactivex.io/)看一些相关文档。

使用协程，同样可以像 `RxJava` 那样有效地消除回调地狱，不过无论是框架功能，还是代码风格，两者是有很大区别的。协程是为了解决协作式多任务而被设计出来的，在写法上和普通的顺序代码类似。

以通过协程进行网络请求获取用户信息并显示到 UI 控件上为例：

```kotlin
🏝️
launch(block = {
    val user = api.getUser() // 👈 网络请求（后台线程）
    nameTv.text = user.name  // 👈 更新 UI（主线程）
})
```

这里的 `launch` 函数再加上实现在 `block` 参数中具体的逻辑，就构成了一个协程。

如果 `block` 中的代码没有放在 `launch` 中去调用，而且 `getUser()` 的网络请求在后台线程进行，那么 `nameTv` 就无法显示出正确的 `user.name`。

而在这里， `getUser()` 是一个挂起函数，所以能够保证 `nameTv.text` 的正确赋值，这就涉及到了协程中最著名的「非阻塞式挂起」。这个名词看起来不是那么容易理解，我们后续的文章会专门对这个概念进行讲解。我们现在先把这个概念放下，只需要记住协程就是像这样写的就行了，来看看协程好在哪。

### 协程好在哪

#### 开始之前

在讲之前，我们需要先了解一下「闭包」这个概念。

调用 Kotlin 协程中的 API，经常会用到闭包写法。

其实闭包并不属于 Kotlin 中独有的概念，同样也不是什么新概念，在「Java 8」中就已经被支持。

我们先以 `Thread` 为例，来看看什么是闭包：

```kotlin
🏝️
// 创建一个 Thread 的完整写法
Thread(object : Runnable {
    override fun run() {
        ...
    }
})

// 使用闭包，可以简化为
Thread {
    ...
}
```

形如 `Thread {...}` 这样的结构就是一个闭包。

在 Kotlin 中有这样一个语法糖：当函数的最后一个参数是 lambda 表达式时，可以将 lambda 写在括号外 。

在这里需要一个类型为 `Runnable` 的参数，而 `Runnable` 是一个接口，且只定义了一个函数 `run()`，这种情况满足了 Kotlin 的 [SAM](https://medium.com/tompee/idiomatic-kotlin-lambdas-and-sam-constructors-fe2075965bfb)，可以转换成传递一个 lambda 表达式，而在这里同时也是最后一个参数，根据语法糖我们就可以直接写成 `Thread {...}` 的形式。

对于上文所使用的 `launch` 函数，`block` 是其最后一个参数，所以可以通过闭包来进行简化 ：

```kotlin
🏝️
launch {
    ...
}
```

#### 基本使用

在使用 Kotlin 协程 API 前，我们需要先创建协程，通常可以使用下面三种方法：

```kotlin
🏝️
// 方法一：使用 runBlocking 函数
runBlocking {
    getImage(imageId)
}

// 方法二：使用 GlobalScope 单例对象
//            👇 可以直接调用 launch 开启协程
GlobalScope.launch {
    getImage(imageId)
}

// 方法三：自行通过 CoroutineContext 创建一个 CoroutineScope 对象
//                                    👇 需要一个类型为 CoroutineContext 的参数
val coroutineScope = CoroutineScope(context)
```

方法一通常适用于单元测试的场景，而业务开发中不会用到这种方法，因为它是线程阻塞的。

方法二和使用 `runBlocking` 的区别是不会阻塞线程。但在 Android 开发中同样是不推荐这种用法，因为它的生命周期会和 app 一致，且不能提前取消。

方法三是比较推荐的使用方法，我们可以通过 `context` 参数去管理和控制协程的生命周期

关于 CoroutineScope 和 CoroutineContext 的更多内容后面章节会继续讲解。

协程最常用的功能是并发，也就是多线程。可以使用 `Dispatchers.IO` 参数把任务切到后台执行：

```kotlin
🏝️
coroutineScope.launch(Dispatchers.IO) {
    ...
}
```

也可以使用 `Dispatchers.Main` 参数切换到前台：

```kotlin
🏝️
coroutineScope.launch(Dispatchers.Main) {
    ...
}
```

所以在「协程是什么」一节中讲到的异步请求的例子完整写出来是这样的：

```kotlin
🏝️
coroutineScope.launch(Dispatchers.Main) {   // 👈 开启协程：主线程
    val user = api.getUser() // 👈 网络请求：后台线程
    nameTv.text = user.name  // 👈 更新 UI：主线程
}
```

而通过 Java 实现以上逻辑，我们通常需要这样写：

```java
☕️
api.getUser(new Callback<User>() {
    @Override
    public void success(User user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nameTv.setText(user.name);
            }
        })
    }
    
    @Override
    public void failure(Exception e) {
        ...
    }
});
```

这种回调式的写法，打破了代码的顺序结构，降低了可读性。

#### 协程的「1 到 0」

对于回调式的写法，如果并发场景再复杂一些，代码的嵌套可能会更多，这样的话维护起来就非常麻烦。但如果你使用了 Kotlin 协程，多层网络请求只需要这么写：

```kotlin
🏝️
coroutineScope.launch(Dispatchers.Main) {       // 👈 开始协程：主线程
    val token = api.getToken()                  // 👈 网络请求：后台线程
    val user = api.getUser(token)               // 👈 网络请求：后台线程
    nameTv.text = user.name                     // 👈 更新 UI：主线程
}
```

如果遇到的是多个网络请求并列的情况，需要等待所有请求结束之后再对 UI 进行更新。假设有如下请求：

```kotlin
🏝️
api.getAvatar(user)      // 获取用户头像
api.getCompanyLogo(user) // 获取用户所在公司的 logo
```

如果使用回调式的写法，那么代码可能写起来既困难又别扭。于是我们可能会选择妥协，通过先后请求代替并行请求：

```kotlin
🏝️
api.getAvatar(user) { avatar ->
    api.getCompanyLogo(user) { logo ->
        show(merge(avatar, logo))
    }
}
```

在实际开发中是绝对不能这样写的，本来能够并行处理的请求被强制通过串行的方式去实现，可能会导致等待时间长了一倍，也就是性能差了一倍。

而如果使用协程，可以直接把两个并行请求写成上下两行，最后再把结果进行合并即可：

```kotlin
🏝️
coroutineScope.launch(Dispatchers.Main) {
    //            👇  async 函数在下一节「协程怎么用」的「开始使用协程」小节中再进行讲解
    val avatar = async { api.getAvatar(user) }    // 👈 获取用户头像
    val logo = async { api.getCompanyLogo(user) } // 👈 获取用户所在公司的 logo
    val merged = suspendingMerge(avatar, logo)    // 👈 合并结果
    //                  👆
    show(merged) // 👈 显示 UI
}
```

可以看到，即便是比较复杂的并行网络请求，也能够通过协程写出结构清晰的代码。需要注意的是 `suspendingMerge` 并不是协程 API 中提供的方法，而是我们自定义的一个可「挂起」的结果合并方法。至于挂起具体是什么，可以看我们的下一篇文章。

让复杂的并发代码，写起来变得简单且清晰，是协程的优势所在。

在了解了协程的作用和优势之后，我们再来看看协程是怎么使用的。

### 协程怎么用

#### 在项目中配置对 Kotlin 协程的支持

Kotlin 语法中原生支持协程的关键字 `suspend`（`suspend` 具体是什么我们后面再讲），但这对于“使用协程”来说是不够的。在使用协程 api 之前，我们需要像以往使用其他第三方库一样，在 `build.gradle` 文件中增加对 Kotlin 协程的依赖：

- 项目根目录下的 `build.gradle` :

```groovy
buildscript {
    ...
    //                        👇 以 1.3.1 版本为例
    ext.kotlin_coroutines = '1.3.1'
    ...
}
```

- Module 下的 `build.gradle` :

```groovy
dependencies {
    ...
    // coroutines
    //                                       👇 依赖协程核心库
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines'
    //                                       👇 依赖当前平台所对应的平台库（这里是 Android 平台）
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines'
    ...
}
```

Kotlin 协程是以官方扩展库的形式进行支持的。而且，我们所使用的「核心库」和 「平台库」的版本应该保持一致。

- 核心库中包含的代码主要是协程的公共 API 部分。有了这一层公共代码，才使得协程在各个平台上的接口得到统一。
- 平台库中包含的代码主要是协程框架在具体平台的具体实现方式。因为多线程在各个平台的实现方式是有所差异的。

完成了以上的准备工作就可以开始使用协程了。

#### 开始使用协程

协程最简单的使用方法，其实在前面章节「协程好在哪」中就已经提到了。我们可以通过一个 `launch()` 函数或是一个 `async()` 函数实现线程切换的功能：

```kotlin
🏝️
//               👇
coroutineScope.launch(Dispatchers.IO) {
    // 进行异步操作
}

//.              👇
coroutineScope.async(Dispatchers.IO) {
    // 进行异步操作
}
```

这两个函数都具有一个共同的含义：在指定的线程（例子中是 IO 线程）中运行协程，而这个被运行的所谓“协程”就是你传递给 `launch()` 或 `async()` 的那些代码。

那是不是只有在你需要切换线程的时候才会使用到协程呢？可以先看一下以下代码：

```kotlin
🏝️
coroutineScope.launch(Dispatchers.IO) {
    val image = getImage(imageId)       // 👈 先在 IO 线程进行后台请求
    launch(Dispatch.Main) {
        avatarIv.setImageBitmap(image)  // 👈 然后在主线程更新界面
    }
}
```

如果只是使用的 `launch()` 函数，协程并不能比线程做更多的事。不过协程中却有一个很实用的函数：`withContext()` 。这个函数可以切换到指定的线程，并在闭包内的逻辑执行结束之后，自动把线程切回去继续执行。那么可以将上面的代码写成这样：

```kotlin
🏝️
coroutineScope.launch(Dispatchers.Main) {      // 👈 在 UI 线程开始
    val image = withContext(Dispatchers.IO) {  // 👈 切换到 IO 线程，并在执行完成后切回 UI 线程
        getImage(imageId)                      // 👈 将会运行在 IO 线程
    }
    avatarIv.setImageBitmap(image)             // 👈 回到 UI 线程更新 UI
} 
```

这种写法看上去好像和刚才那种区别不大，但如果你需要频繁地进行线程切换，这种写法的优势就会体现出来。可以参考下面的对比：

```kotlin
🏝️
// 第一种写法
coroutineScope.launch(Dispachers.IO) {
    ...
    launch(Dispachers.Main){
        ...
        launch(Dispachers.IO) {
            ...
            launch(Dispacher.Main) {
                ...
            }
        }
    }
}

// 通过第二种写法来实现相同的逻辑
coroutineScope.launch(Dispachers.Main) {
    ...
    withContext(Dispachers.IO) {
        ...
    }
    ...
    withContext(Dispachers.IO) {
        ...
    }
    ...
}
```

由于可以"自动切回来"，消除了并发代码在协作时的嵌套。由于消除了嵌套关系，我们甚至可以把 `withContext()` 放进一个单独的函数里面：

```kotlin
🏝️
launch(Dispachers.Main) {              // 👈 在 UI 线程开始
    val image = getImage(imageId)
    avatarIv.setImageBitmap(image)     // 👈 执行结束后，自动切换回 UI 线程
}
//                               👇
fun getImage(imageId: Int) = withContext(Dispatchers.IO) {
    ...
}
```

如果只是像这样写，编译器是会报错的，`withContext` 处会提示 `Suspend function'withContext' should be called only from a coroutine or anoher suspend funcion` 这样的错误。也就是说，`withContext()` 是一个可挂起函数，它只能够在协程里，或者是另一个可挂起函数中调用。那么什么是「可挂起」呢？这就需要提到 Kotlin 中和协程密切相关的关键词「suspend」。

### suspend

`suspend` 是 Kotlin 协程最核心的关键词，几乎所有介绍 Kotlin 协程的文章和演讲都会提到它。它的中文意思是「暂停」或者「可挂起」。如果你去看一些技术博客或官方文档的时候，大概可以了解到：「代码执行到 `suspend` 函数的时候会『挂起』，并且这个『挂起』是非阻塞式的，它不会阻塞你当前的线程。」

而上面报错的代码，其实只需要在前面加一个 `suspend` 就能够编译通过：

```kotlin
🏝️
//👇
suspend fun getImage(imageId: Int) = withContext(Dispatchers.IO) {
    ...
}
```

本篇文章到此结束，而 `suspend` 具体是什么，「非阻塞式」又是怎么回事，函数怎么被挂起... 这些所有疑问的答案，将在下一篇文章全部揭晓。

## 练习题

1. 通过 Kotlin 协程下载一张网络图片并显示出来。
2. 使用协程同时下载两张图片，并在两个下载都完成后再将两张图片同时显示出来。