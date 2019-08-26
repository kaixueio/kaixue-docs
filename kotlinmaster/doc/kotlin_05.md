# Kotlin 的协程 Coroutines

Kotlin 的协程是它非常特别的一块地方，对比 Java，Kotlin 中的协程是一个比较新颖的概念，它既非线程，又不是 RxJava 的替代品。宣扬它的人都在说协程是多么好多么棒，但就目前而言不管是官方文档还是网络上的一些文章都给人一种「劝退的感觉」。而造成这些「不安」的原因和大多数人在初学 RxJava 时所遇到的问题其实是一致的：对于 Java 开发者来说这是一个新东西。

初看协程似乎是一个很神秘的新东西，但它其实并不那么神秘。在我们的生活中其实也能遇到很多能够体现协程思想的例子，下面我将用最简单通俗的语言来告诉你「协程是什么」、「协程怎么用」以及「什么时候该使用协程」

## 协程是什么

协程其实并不是 Kotlin 提出来的新概念，其他的一些编程语言，例如：Swift、Lua、Go、C#、Python 等都可以在语言层面上实现协程，甚至我们从未使用过协程的 Java，也可以通过扩展库来间接地支持协程。

在网上搜「协程是什么」，会得到诸如“协程和线程类似”、“像一种轻量级的线程”、“不是线程”、“不需要从「用户态」切换到「内核态」”、“是「协作式」的，不需要线程的同步操作”这样的解释。这些描述难免让人感觉晦涩难懂。

但其实我们学习 Kotlin 中的协程，并不需要了解这么多概念。广义上讲的「协程」是和「线程」非常类似的用于处理多任务的概念，但在 Kotlin 中，协程其实就是由 Kotlin 官方所提供的线程控制API。就像 Java 中的 `Executor` 和 Android 中的 `AsyncTask` 和 `Handler`，Kotlin 中的协程也是一种对 Thread API 的封装，让我们可以在写代码时，不用去关注多线程间的同步操作也能够很方便地写出具有并发操作的代码。

在 Java 中要进行并发操作通常需要初始化并开启一个 Thread 👇

```java
☕️
// 线程
Thread thread = new Thread(new Runnable() {
    @Override
    public void run() {
        ...
    }
});
thread.start()
```

👆 仅仅只是开启了一个新线程，至于它何时结束、执行结果怎么样，我们在主线程是无法感知到的。

那么我们再看一下在 Kotlin 中是如何处理并发操作的。同样，我们先通过线程的方式去写 👇

```kotlin
🏝️
Thread {
    ...
}.start()
```

可以看到，相比 Java 可以少写很多，但和之前的 Java 代码确是完全等价的，摆脱不了难以控制的诟病。

或许你会想到用「线程池」进行管理 👇

```kotlin
🏝️
// Executor
val executor = Executors.newCachedThreadPool()
executor.execute {
    ...
}
```

又或许是用 Android 自带的 AsyncTask 去控制👇

```kotlin
🏝️
// AsyncTask
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
    
    ...
}
```

Handler 去处理异步消息 👇

```kotlin
🏝️
// Handler
object : Handler() {
    fun handleMessage(msg: Message) {
        ...
    }
}
```

搭配以上的方式，我们就能够实现一套完整的并发控制了。

但这些实现方式都太重了，而且代码的可读性也会非常差。有没有一种轻量且功能完备的并发控制方式呢？有，那就是协程 👇

```kotlin
🏝️
// 协程
//👇 调用需要在协程的上下文中
launch {
    ...
}
```

👆 这样就开启了一个新的协程。

看上去似乎和启动一个新线程的代码没有太大区别，它能有什么特别的好处吗？

协程的好处，不是在于它提供了什么超越线程能力的功能，而是能够让我们所写的异步代码看起来和同步代码一样。就像这样 👇

```kotlin
🏝️
val user = api.getUser() // 网络请求（后台线程）
nameTv.text = user.name  // 更新 UI（主线程）
```

👆 这种写法在不使用协程的情况下大概率不会得到正确的 user.name，但如果是放到协程中去处理，nameTv.text 的正确赋值是能够得到保证的，这就是 Kotlin 的协程最著名的「非阻塞式挂起」。这个名词看起来不是那么容易理解，别急，后面的部分会完全讲明白。我们先把这个概念放下，来看看协程好在哪。

## 协程好在哪

协程最基本的功能是并发，也就是多线程。用协程，你可以把任务切到后台执行 👇

```kotlin
🏝️
launch(Dispatchers.IO) {
    ...
}
```

也可以切换到前台 👇

```kotlin
🏝️
launch(Dispatchers.Main) {
    ...
}
```

如果只看代码简洁度，协程相比 `Thread ` 并没有体现出优势，因为 Kotlin 同样对线程的使用做了封装，使用起来也十分简洁 👇

```kotlin
🏝️
// Thread { ... }.start() 可简化为 👇
thread {
    ...
}
```

而 Kotlin 协程的好处，是在于你可以把运行在不同线程的代码，写到同一个代码块里面 👇

```kotlin
🏝️
launch(Dispatchers.Main) {   // 👈 开始协程：主线程
    val user = api.getUser() // 👈 网络请求：后台线程
    nameTv.text = user.name  // 👈 更新 UI：主线程
}
```

👆 这种同一个代码块中不同行的代码，运行在不同的线程，是用 Java 绝对做不到的。通过 Java 实现以上逻辑，我们通常需要这样写 👇

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

👆 这种写法需要把异步消息的处理放到回调的 block 中（不使用协程的 Kotlin 写法类似），打破了代码的顺序结构，降低了可读性。如果并发场景再复杂一些，代码的嵌套会更加复杂，这样的话维护起来就非常麻烦。但如果你使用了 Kotlin 协程，多层网络请求或许只需要这么写 👇

```kotlin
🏝️
launch(Dispatchers.Main) {       // 👈 开始协程：主线程
    val token = api.getToken()   // 👈 网络请求：后台线程
    val user = api.getUser(token)// 👈 网络请求：后台线程
    nameTv.text = user.name      // 👈 更新 UI：主线程
}
```

这样的话多层网络请求嵌套写起来会非常简洁。

如果遇到的是多个网络请求并列的情况，我们需要等待所有请求结束之后再对 UI 进行更新。假设有如下请求 👇

```kotlin
🏝️
api.getAvatar(user)      // 获取用户头像
api.getCompanyLogo(user) // 获取用户所在公司的 logo
```

如果使用回调式的写法，那么代码可能写起来既困难又别扭。于是我们可能会选择妥协，通过先后请求代替并行请求 👇

```kotlin
api.getAvatar(user) { avatar ->
    api.getCompanyLogo(user) { logo ->
        show(merge(avatar, logo))
    }
}
```

👆 在实际开发中是不能这样写的，本来能够并行处理的代码被强制通过串行的方法去实现，可能导致网络等待时间长了一倍，也就是性能差了一倍。

而如果使用协程，可以直接把两个并行请求写成上下两行，然后再把结果进行合并即可 👇

```kotlin
launch(Dispatchers.Main) {
    //            👇  async 其实和 launch 很类似，区别只是返回值带有异步请求的结果
    val avatar = async { api.getAvatar(user) }    // 获取用户头像
    val logo = async { api.getCompanyLogo(user) } // 获取用户所在公司的 logo
    val merged = suspendingMerge(avatar, logo)    // 合并
    //                  👆 这并不是协程提供的方法，而假设是我们自己定义的一个合并方法，它是一个挂起函数
    show(merged) // 显示
}
```

可以看到，即便是比较复杂的并行网络请求，也能够通过协程写出结构清晰的代码。

让复杂的并发代码，写起来变得简单且清晰，是协程的优势所在。

在了解了协程的作用和优势之后，我们再来看看协程具体怎么使用。

## 协程怎么用

### 在项目中配置对 Kotlin 协程的支持

在使用协程 api 之前，我们需要像以往使用其他第三方库一样，在 `build.gradle` 文件中增加对 Kotlin 协程的依赖：

- 项目更目录下的 `build.gradle` :

```groovy
buildscript {
    ...
    ext.kotlin_coroutines = '1.1.1'
    ...
}
```

- Module 下的 `build.gradle` :

```groovy
dependencies {
    ...
    // coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlin_coroutines'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlin_coroutines'
    ...
}
```

Kotlin 协程是以官方扩展库的形式进行支持的。

### 开始使用协程

协程最简单的使用方法，其实在前面 **`协程好在哪`** 的介绍中就已经提到了。我们通过一个 `launch()` 函数或是一个 `async()` 函数，就能够实现线程的切换 👇

```kotlin
🏝️
launch(Dispatchers.IO) {
    // 进行异步操作
}

async(Dispatchers.IO) {
    // 进行异步操作
}
```

👆 这两个函数都具有一个共同的含义：我要创建一个新的协程，并在指定的线程（例子中是 IO 线程）上运行它。而这个被创建、被运行的所谓「协程」就是你传递给 `launch()` 或 `async()` 的那些代码，这样的一段连续的代码叫做一个「协程」。

那是不是只有在你需要切换线程的时候才会使用到协程呢？可以先看一下以下代码 👇

```kotlin
🏝️
launch(Dispatchers.IO) {
    val image = getImage(imageId)       // 👈 先在 IO 线程进行后台请求
    launch(Dispatch.Main) {
        avatarIv.setImageBitmap(image)  // 👈 再在主线程更新界面
    }
}
```

如果只是使用的 `launch()` 函数，协程并不能比线程做更多的事。不过协程中却有一个很实用的函数：`withContext()` 。这个函数可以**指定线程来执行代码，并在执行完成之后自动把线程切换回来继续执行**。那么就可以将上面的代码写成这种样子 👇

```kotlin
🏝️
launch(Dispatchers.Main) {                          // 👈 在 UI 线程开始
    val image = withContext(Dispatchers.IO) {       // 👈 切换到 IO 线程，并将在 IO 线程执行完成后切回 UI 线程
        getImage(imageId)                           // 👈 将会运行在 IO 线程
    }
    avatarIv.setImageBitmap(image)                  // 👈 在 UI 线程更新 UI
} 
```

👆 这种写法看上去好像和刚才那种区别不大，但如果你需要频繁地进行线程切换，这种写法的优势就会体现出来。可以参考下面的对比👇

```kotlin
🏝️
// 第一种写法
launch(Dispachers.Main) {
    ...
    launch(Dispachers.IO) {
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
}

// 通过第二种写法来实现相同的逻辑
launch(Dispachers.Main) {
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

由于可以「自动切回来」，消除了并发代码在写协作时的嵌套。直接通过顺序代码就实现了多线程之间的协作，这就是「协程」，协作式的例程。由于消除了嵌套关系，我们甚至可以把 `withContext()` 放进一个单独的函数里面，用它包着函数的实际业务代码 👇

```kotlin
🏝️
launch(Dispachers.Main) {
    val image = getImage(imageId)
    avatarIv.set
}
//                               👇
fun getImage(imageId: Int) = withContext(Dispatchers.IO) {
    ...
}
```

不过，如果你只是像这样写，编译器是会报错的，「👇」所指向的代码会提示 `Suspend function'withContext' should be called only from a coroutine or anoher suspend funcion` 这样的错误。也就是说，`withContext()` 是一个可挂起函数，它只能够在协程里，或者是另一个可挂起函数中调用。那么什么是「可挂起」呢？下面会进一步讲解。

### 开始使用 suspend 

`suspend` 是 Kotlin 协程最核心的关键词，几乎所有介绍 Kotlin 协程的文章和演讲都会提到它。它的中文意思是「暂停」或者「可挂起」。如果你去看一些技术博客或官方文档的时候，大概可以了解到：「代码执行到 `suspend` 函数的时候会『挂起』，并且这个『挂起』是非阻塞式的，它不会阻塞你当前的线程。」

那么先来看一下挂起函数是怎么写的 👇

```kotlin
🏝️
//👇
suspend fun getImage(imageId: Int) = withContext(Dispatchers.IO) {
    ...
}
```

没错，它和普通函数的区别就只是多了一个 `suspend` 关键字作为函数的修饰符，对于一个普通函数，在「👇」所指向的位置加入 `suspend` 关键字，它就成为了一个可挂起函数。

现在，我们来解释一下上一节所提到的「`withContext()` 放在普通函数中去调用时会报错」的原因。

首先，我们可以看一下 `withContext()` 的源码：

```kotlin
🏝️
public suspend fun <T> withContext(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> T
): T = ...  // 具体的实现逻辑在此处省略 
```

可见 `withContext` 本身其实就是一个 `suspend` 函数。

然后我们再来理解一下之前的那一段报错信息，有看到 `should be called only from a coroutine or anoher suspend funcion` 。根据编译器提示，我们可以知道：凡是被 `suspend` 所修饰的函数，只能在协程代码块或其他 `suspend` 函数中进行调用。那么我们可以再对这句话进行归纳，也就是 **`只有在协程的上下文中才能够调用挂起函数`** 。因为我们的程序总是开始于非挂起函数的，所以当需要用到协程时就需要从非挂起函数切换到挂起函数，而要进行这一操作，就必须先创建出协程的上下文。

在 Kotlin 中创建一个协程的方法很多，这里先简单介绍三种：

```kotlin
🏝️
// 方法一：使用 Kotlin 提供的顶级函数：
runBlocking {   // 👈 这种用法只适合单元测试的场景，因为它是线程阻塞的，Android 中不需要、也不能用这种方法
    getImage(imageId)
}

// 方法二：使用 GlobalScope 单例对象
//            👇 只是以 launch() 为例，使用 async() 也可以（后面同理，不再赘述）
GlobalScope.launch {  // 👈 这也是一种顶级的用法，和 runBlocking 的区别是它不会线程阻塞。但在 Android 开发中同样是不推荐这种用法，因为它的生命周期会和 app 一致，且不能够提前取消
    getImage(imageId)
}

// 方法三：自行通过 CoroutineContext 创建一个 CoroutineScope 对象
//                                    👇 这是一个类型为 CoroutineContext 的参数
val coroutineScope = CoroutineScope(context)
coroutineScope.launch {  // 👈 这是比较推荐使用的方法，我们可以通过 context 参数去管理和控制协程的生命周期
    getImage(imageId)
}
```

通过以上方法，我们就可以开始调用协程提供的各种 API 了。关于 CoroutineScope 和 CoroutineContext 的更多内容后面章节会继续深入。

那到底是把谁挂起来了呢？当然是协程。当执行到一个 `suspend` 函数的时候，这个协程会被`suspend` ，而且是从当前线程挂起的。就像是顺序执行的代码，在执行到协程的代码块前，将协程中所包裹的代码给抽离了，那么当前线程将会跳过协程代码块，继续顺序往下执行。我们可以把包含协程的代码像线程那样分两条线来看：

首先是当前线程的顺序代码。协程被挂起时所进行的操作类似于往目标线程的 Handler `post()` 了一段代码块 👇

```kotlin
🏝️
handler.post {
    // 假设内容与写在协程代码块中的内容一致
    val image = getImage(imageId)
    avatarIv.setImageBitmap(image)
}
```

当协程被挂起后，当前线程视后面的代码逻辑，要么继续执行，要么执行结束。如果当前是 Android 的 UI 线程，那么它还是会以每秒60帧的速度在刷新界面。

然后我们再来看一下还未被执行的协程中代码。我们可以类比 Android 中的 Handler，协程中的代码也会被派发到指定的线程去执行，具体是哪一个线程，就看你在调用 `withContext()` 时所指定的参数。

使用 `withContext()` 的好处是当我们的 `suspend` 函数执行完成后，能够自动切换回当前线程。那么我们可以这么理解：**`withContext() 帮助我们阻塞了当前协程，却没有阻塞当前线程。`** 这样，即满足了我们需要的逻辑上并发的需求，也达成了代码结构上顺序连贯的特点。

看到这里，我们可以对协程的 「挂起」 `suspend` 可以做出一个比较通俗的解释了：协程在执行到有 `suspend` 标记的函数时，会被挂起，而所谓的被挂起，其实就和开启协程一样，就是切个线程；只不过区别在于，**挂起函数在执行完成之后，协程会被重新切回到之前的线程**。而这种「切回来」的操作，在 Koltlin 中被叫做 resume。

 ### CoroutineScope 的使用

上一小节有简单提到 `CoroutineScope` 的使用方法，使用它可以方便地创建出协程。`CoroutineScope` 直接翻译过来是「协程范围」，那为什么协程会有「范围」这种概念？这个「范围」有什么用？我们先通过源码来简单看看它是什么：

```kotlin
🏝️
// 接口定义 (省略了注释)
public interface CoroutineScope {
    /**
     * Context of this scope.
     */
    public val coroutineContext: CoroutineContext
}

// API 方法
public fun CoroutineScope(context: CoroutineContext): CoroutineScope =
    ContextScope(if (context[Job] != null) context else context + Job())
```

其实这就是一个接口，而定义得也非常简单，只有一个变量 `coroutineContext` 。只看代码我们并不能看出一个所以然，于是我们回过去再看一下注释，其中有一句写到：`[CoroutineScope] should be implemented on entities with well-defined lifecycle that are responsible for launching children coroutines.  Example of such entity on Android is Activity.` 可以看出其实 `CoroutineScope` 主要被用来控制协程的生命周期，一般在 Android 中需要在具备生命周期的 Activity 或 Service 中去使用。你可以像这样去使用它 👇

```kotlin
🏝️
//                                           👇 通过实现接口的方式使用 CoroutineScope
class MyActivity : AppCompatActivity(), CoroutineScope {
    lateinit var job: Job
    //                 👇 实现 CoroutineScope 时唯一需要实现的字段
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()     // 👈 在 Activity onCreate() 时初始化 job
      
        getImage("image_id")
    }
  
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()   // 👈 在 Activity onDestory() 时取消 job
    }
  
    //                                👇 在执行 'job.cancel()' 时，此处创建的协程也会被取消当前任务
    fun getImage(imageId: String) = launch {
        ...
    }
}
```

`coroutineContext` 和 `job` 的具体含义我们下一小节再去展开来讲。可以注意到 `getImage()` 方法中有通过 `launch` 去创建一个新的协程，而使用 CoroutineScope 的好处就是：`在执行取消操作时，能让所有嵌套在自身作用域中（包括间接嵌套）的协程都自动取消当前任务`。所以我们只需要在 `onDestory()` 中调用 `job.cancel()` ，而不必去关心具体的每一个协程。

那么，我们现在可以这样来理解：`CoroutineScope` 定义了一个可对 `子协程` 进行操作的 `作用域` ，通过它可以管理域内所有协程。正是因为有这种功能，它很适用于具有生命周期的场景。

上一小节所讲到的三种创建协程的方式，其实都直接或间接地用到了 `CoroutineScope` 。`GlobalScope` 直接继承自 CoroutineScope；而 `runBlocking` 在 API 的实现中用的是 `BlockingCoroutine` ，也是继承了 CoroutineScope。也就是说，只要涉及到「协程的创建」，就必然要用到 「CoroutineScope」。而这些创建方式间的主要区别是在于它们所具有的不同上下文 `coroutineContext` 。

### CoroutineContext 和 Job

把 `CoroutineContext` 和 `Job` 放在一起讲是由于后者是被包含在前者众多参数中的一个。

「CoroutineContext」 是 协程的上下文，它和 Android 中常遇到的上下文 `Context` 类似，包含了当前协程的信息，比如 Job、CoroutineName、CoroutineDispatcher 等。我们前面提到的协程「调度器」，如 `Dispatchers.IO` 、`Dispatchers.Main` 等其实都是 CoroutineDispatcher 的子类实例，而 Job、CoroutineName、CoroutineDispatcher 等也都是继承自 CoroutineContext。

// 在「CoroutineContext」中存储这些信息用的是 map，并做了巧妙的封装。map 的键其实就是每个子类所实现 // 的「伴生对象」

「Job」是 CoroutineContext 所包含的众多信息中比较实用的一个。上一小节所提到的 「CoroutineScope 的使用例子」中就是通过 `job.cancel()` 去取消 `子协程` 当前任务的。「Job」除了 `cancel()` 方法， 还有 `start()` 等方法，其主要作用是去管理 CoroutineContext 作用到的子协程。

通常，实际开发中我们可以把 「CoroutineDispatcher」 和 「Job」结合起来作为 `CoroutineScope` 的参数 `CoroutineContext` 使用 👇

```kotlin
🏝️
//                    👇 Job 的一个实现类
val serviceJob = SupervisorJob()
//                                                👇 将调度器和 Job 共同作为协程上下文使用
val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
```

这里用到了「操作符重载」的知识，实现了将多个协程上下文信息合并到一起的功能，关于这个概念，后面的文章会讲到。将 Job 增加到协程上下文中的好处就是能够去控制 `CoroutineScope` 中的子协程。

## 协程与线程

协程是基于线程实现的一种更上层的工具 API，类似于 `Executor` 系列 API 或者 `Handler` 系列 API。所以「线程」和「协程」不是同一个级别的东西，也就没办法进行比较。

#### 项目中可以用协程完全代替线程吗？

协程对于 Android 项目的开发可以说是完全够用了。因为在 Android 开发中，我们能够频繁使用到多线程的场景其实还是网络请求。像以往我们使用 Java 开发 Android 时所用到 `Executor` 或者 `Handler` 中的功能，协程都具备。

#### 项目中可以完全不用协程只用线程吗？

当然可以，因为我们说协程是对线程封装的上层工具 API，也就是说能够通过协程做的事，通过线程也同样可以，只不过实现起来可能会很复杂、很麻烦。

其实只需要弄清楚「协程不是线程」就可以了。官方文档或某些技术博客所说的「协程是轻量级的线程」是不严谨的，忘了就好；而那些写了一个 Demo 将「线程性能」和「协程性能」拿来做比较的，其实也没有这个必要了，做过优化封装的协程，肯定会比简单粗暴地直接使用 Thread 性能更好，如果非要进行比较也只能和同级别的 `Executor` 等 API 去比。而使用「协程」真正能体现出来的优势是其良好的「可读性」和「可维护性」。

也正是由于「协程不是线程」，协程的实现才能够脱离具体的操作系统，在语言的层面就能够实现。

## 协程与 RxJava

协程和 RxJava 在线程切换方面的功能是一样的，都能让你写出避免嵌套回调的复杂并发代码，不过协程的写法比 RxJava 还要更简单一些，因此连 RxJava 的操作符都省了。

## 练习题

1. 通过 Kotlin 协程下载一张网络图片并显示出来。
2. 使用 Kotlin 协程实现输出 1000 以内的素数，可以和传统的素数算法进行对比，效率方面会不会有提升。