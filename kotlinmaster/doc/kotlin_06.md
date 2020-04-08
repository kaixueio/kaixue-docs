# Kotlin 协程「挂起」的本质

> 本期作者：
>
> 视频：扔物线（朱凯）
>
> 文章：[Hugo（谢晨成）](https://github.com/xcc3641)

大家好，我是扔物线朱凯，我回来啦。今天我们接着讲协程。

在上一期里，我介绍了 Kotlin 的协程到底是什么——它就是个线程框架。没什么说不清的，就这么简单，它就是个线程框架，只不过这个线程框架比较方便——另外呢，上期也讲了一下协程的基本用法，但到最后也留下了一个大问号：协程最核心的那个「非阻塞式」的「挂起」到底是怎么回事？今天，我们的核心内容就是来说一说这个「挂起」。

老规矩，全国最硬核的 Android 视频播主为你带来最硬核的视频：

<div class="aspect-ratio">
    <iframe src="https://player.bilibili.com/player.html?aid=68241619&page=1&high_quality=1" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true"> </iframe>
</div>

如果你看不到上面的哔哩哔哩视频，可以点击 [这里](https://youtu.be/mDtXcEuOXvk) 去 YouTube 看。



> 以下内容来自文章作者 [Hugo](https://github.com/xcc3641)。



## **上期回顾**

在协程上一期中我们知道了下面知识点：

- 协程究竟是什么
- 协程到底好在哪里
- 协程具体怎么用

大部分情况下，我们都是用 `launch` 函数来创建协程，其实还有其他两个函数也可以用来创建协程：

- `runBlocking`
- `async`

`runBlocking` 通常适用于单元测试的场景，而业务开发中不会用到这个函数，因为它是线程阻塞的。

接下来我们主要来对比 `launch` 与 `async` 这两个函数。

- 相同点：它们都可以用来启动一个协程，返回的都是 `Coroutine`，我们这里不需要纠结具体是返回哪个类。

- 不同点：`async` 返回的 `Coroutine` 多实现了 `Deferred` 接口。
  

关于 `Deferred` 更深入的知识就不在这里过多阐述，它的意思就是延迟，也就是结果稍后才能拿到。

我们调用 `Deferred.await()` 就可以得到结果了。

接下来我们继续看看 `async` 是如何使用的，先回忆一下上期中获取头像的场景：

```kotlin
🏝️
coroutineScope.launch(Dispatchers.Main) {
    //                      👇  async 函数启动新的协程
    val avatar: Deferred = async { api.getAvatar(user) }    // 获取用户头像
    val logo: Deferred = async { api.getCompanyLogo(user) } // 获取用户所在公司的 logo
    //            👇          👇 获取返回值
    show(avatar.await(), logo.await())                     // 更新 UI
}
```

可以看到 avatar 和 logo 的类型可以声明为 `Deferred` ，通过 `await` 获取结果并且更新到 UI 上显示。

`await` 函数签名如下：

```kotlin
🏝️
public suspend fun await(): T
```

前面有个关键字是之前没有见过的 —— `suspend`，这个关键字就对应了上期最后我们留下的一个问号：协程最核心的那个「非阻塞式」的「挂起」到底是怎么回事？

所以接下来，我们的核心内容就是来好好说一说这个「挂起」。



## 「挂起」的本质

协程中「挂起」的对象到底是什么？挂起线程，还是挂起函数？都不对，**我们挂起的对象是协程。**

还记得协程是什么吗？启动一个协程可以使用 `launch` 或者 `async` 函数，协程其实就是这两个函数中闭包的代码块。

`launch` ，`async` 或者其他函数创建的协程，在执行到某一个 `suspend` 函数的时候，这个协程会被「suspend」，也就是被挂起。

那此时又是从哪里挂起？**从当前线程挂起。换句话说，就是这个协程从正在执行它的线程上脱离。**

注意，不是这个协程停下来了！是脱离，当前线程不再管这个协程要去做什么了。

suspend 是有暂停的意思，但我们在协程中应该理解为：当线程执行到协程的 suspend 函数的时候，暂时不继续执行协程代码了。

我们先让时间静止，然后兵分两路，分别看看这两个互相脱离的线程和协程接下来将会发生什么事情：


**线程：**

前面我们提到，挂起会让协程从正在执行它的线程上脱离，具体到代码其实是：

协程的代码块中，线程执行到了 suspend 函数这里的时候，就暂时不再执行剩余的协程代码，跳出协程的代码块。

那线程接下来会做什么呢？

如果它是一个后台线程：

- 要么无事可做，被系统回收
- 要么继续执行别的后台任务

跟 Java 线程池里的线程在工作结束之后是完全一样的：回收或者再利用。

如果这个线程它是 Android 的主线程，那它接下来就会继续回去工作：也就是一秒钟 60 次的界面刷新任务。

一个常见的场景是，获取一个图片，然后显示出来：

```kotlin
🏝️
// 主线程中
GlobalScope.launch(Dispatchers.Main) {
  val image = suspendingGetImage(imageId)  // 获取图片
  avatarIv.setImageBitmap(image)           // 显示出来
}

suspend fun suspendingGetImage(id: String) = withContext(Dispatchers.IO) {
  ...
}
```

这段执行在主线程的协程，它实质上会往你的主线程 `post` 一个 `Runnable`，这个 `Runnable` 就是你的协程代码：

```kotlin
🏝️
handler.post {
  val image = suspendingGetImage(imageId)
  avatarIv.setImageBitmap(image)
}
```

当这个协程被挂起的时候，就是主线程 `post` 的这个 `Runnable` 提前结束，然后继续执行它界面刷新的任务。

关于线程，我们就看完了。
这个时候你可能会有一个疑问，那 `launch` 包裹的剩下代码怎么办？

所以接下来，我们来看看协程这一边。



**协程：**

线程的代码在到达 `suspend` 函数的时候被掐断，接下来协程会从这个 `suspend` 函数开始继续往下执行，不过是在**指定的线程**。

谁指定的？是 `suspend` 函数指定的，比如我们这个例子中，函数内部的 `withContext` 传入的 `Dispatchers.IO` 所指定的 IO 线程。

`Dispatchers` 调度器，它可以将协程限制在一个特定的线程执行，或者将它分派到一个线程池，或者让它不受限制地运行，关于 `Dispatchers` 这里先不展开了。

那我们平日里常用到的调度器有哪些？

常用的 `Dispatchers` ，有以下三种：

- `Dispatchers.Main`：Android 中的主线程
- `Dispatchers.IO`：针对磁盘和网络 IO 进行了优化，适合 IO 密集型的任务，比如：读写文件，操作数据库以及网络请求
- `Dispatchers.Default`：适合 CPU 密集型的任务，比如计算 

回到我们的协程，它从 `suspend` 函数开始脱离启动它的线程，继续执行在 `Dispatchers` 所指定的 IO 线程。

紧接着在 `suspend` 函数执行完成之后，协程为我们做的最爽的事就来了：会**自动帮我们把线程再切回来**。

这个「切回来」是什么意思？

我们的协程原本是运行在**主线程**的，当代码遇到 suspend 函数的时候，发生线程切换，根据 `Dispatchers` 切换到了 IO 线程；

当这个函数执行完毕后，线程又切了回来，「切回来」也就是协程会帮我再 `post` 一个 `Runnable`，让我剩下的代码继续回到主线程去执行。



我们从线程和协程的两个角度都分析完成后，终于可以对协程的「挂起」suspend 做一个解释：

协程在执行到有 suspend 标记的函数的时候，会被 suspend 也就是被挂起，而所谓的被挂起，就是切个线程；

不过区别在于，**挂起函数在执行完成之后，协程会重新切回它原先的线程**。

再简单来讲，在 Kotlin 中所谓的挂起，就是**一个稍后会被自动切回来的线程调度操作**。

>  这个「切回来」的动作，在 Kotlin 里叫做 [resume](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines.experimental/-continuation/resume.html)，恢复。

通过刚才的分析我们知道：挂起之后是需要恢复。

而恢复这个功能是协程的，如果你不在协程里面调用，恢复这个功能没法实现，所以也就回答了这个问题：为什么挂起函数必须在协程或者另一个挂起函数里被调用。

再细想下这个逻辑：一个挂起函数要么在协程里被调用，要么在另一个挂起函数里被调用，那么它其实直接或者间接地，总是会在一个协程里被调用的。

所以，要求 `suspend` 函数只能在协程里或者另一个 suspend 函数里被调用，还是为了要让协程能够在 `suspend` 函数切换线程之后再切回来。



## 怎么就「挂起」了？

我们了解到了什么是「挂起」后，再接着看看这个「挂起」是怎么做到的。

先随便写一个自定义的 `suspend` 函数：

```kotlin
🏝️
suspend fun suspendingPrint() {
  println("Thread: ${Thread.currentThread().name}")
}

I/System.out: Thread: main
```

输出的结果还是在主线程。

为什么没切换线程？因为它不知道往哪切，需要我们告诉它。

对比之前例子中 `suspendingGetImage` 函数代码：

```kotlin
🏝️
//                                               👇
suspend fun suspendingGetImage(id: String) = withContext(Dispatchers.IO) {
  ...
}
```

我们可以发现不同之处其实在于 `withContext` 函数。

其实通过 `withContext` 源码可以知道，它本身就是一个挂起函数，它接收一个 `Dispatcher` 参数，依赖这个 `Dispatcher` 参数的指示，你的协程被挂起，然后切到别的线程。

所以这个 `suspend`，其实并不是起到把任何把协程挂起，或者说切换线程的作用。

真正挂起协程这件事，是 Kotlin 的协程框架帮我们做的。

所以我们想要自己写一个挂起函数，仅仅只加上 `suspend` 关键字是不行的，还需要函数内部直接或间接地调用到 Kotlin 协程框架自带的 `suspend` 函数才行。



## suspend 的意义？

这个 `suspend` 关键字，既然它并不是真正实现挂起，那它的作用是什么？

**它其实是一个提醒。**

函数的创建者对函数的使用者的提醒：我是一个耗时函数，我被我的创建者用挂起的方式放在后台运行，所以请在协程里调用我。

为什么 `suspend` 关键字并没有实际去操作挂起，但 Kotlin 却把它提供出来？

因为它本来就不是用来操作挂起的。

挂起的操作 —— 也就是切线程，依赖的是挂起函数里面的实际代码，而不是这个关键字。

所以这个关键字，**只是一个提醒**。

还记得刚才我们尝试自定义挂起函数的方法吗？

```kotlin
🏝️
// 👇 redundant suspend modifier
suspend fun suspendingPrint() {
  println("Thread: ${Thread.currentThread().name}")
}
```

如果你创建一个 `suspend` 函数但它内部不包含真正的挂起逻辑，编译器会给你一个提醒：`redundant suspend modifier`，告诉你这个 `suspend` 是多余的。

因为你这个函数实质上并没有发生挂起，那你这个 `suspend` 关键字只有一个效果：就是限制这个函数只能在协程里被调用，如果在非协程的代码中调用，就会编译不通过。

所以，创建一个 `suspend` 函数，为了让它包含真正挂起的逻辑，要在它内部直接或间接调用 Kotlin 自带的 `suspend` 函数，你的这个 `suspend` 才是有意义的。



## 怎么自定义 suspend 函数？

在了解了 `suspend` 关键字的来龙去脉之后，我们就可以进入下一个话题了：怎么自定义 `suspend` 函数。

这个「怎么自定义」其实分为两个问题：

- 什么时候需要自定义 `suspend` 函数？
- 具体该怎么写呢？

### 什么时候需要自定义 suspend 函数

如果你的某个函数比较耗时，也就是要等的操作，那就把它写成 `suspend` 函数。这就是原则。

耗时操作一般分为两类：I/O 操作和 CPU 计算工作。比如文件的读写、网络交互、图片的模糊处理，都是耗时的，通通可以把它们写进 `suspend` 函数里。

另外这个「耗时」还有一种特殊情况，就是这件事本身做起来并不慢，但它需要等待，比如 5 秒钟之后再做这个操作。这种也是 `suspend` 函数的应用场景。

### 具体该怎么写

给函数加上 `suspend` 关键字，然后在 `withContext` 把函数的内容包住就可以了。

提到用 `withContext`是因为它在挂起函数里功能最简单直接：把线程自动切走和切回。

当然并不是只有 `withContext` 这一个函数来辅助我们实现自定义的 `suspend` 函数，比如还有一个挂起函数叫  `delay`，它的作用是等待一段时间后再继续往下执行代码。

使用它就可以实现刚才提到的等待类型的耗时操作：

```kotlin
🏝️
suspend fun suspendUntilDone() {
  while (!done) {
    delay(5)
  }
}
```

这些东西，在我们初步使用协程的时候不用立马接触，可以先把协程最基本的方法和概念理清楚。



## 总结

我们今天整个文章其实就在理清一个概念：什么是挂起？**挂起，就是一个稍后会被自动切回来的线程调度操作。**

好，关于协程中的「挂起」我们就解释到这里。

可能你心中还会存在一些疑惑：
- 协程中挂起的「非阻塞式」到底是怎么回事？
- 协程和 RxJava 在切换线程方面功能是一样的，都能让你写出避免嵌套回调的复杂并发代码，那协程还有哪些优势，或者让开发者使用协程的理由？

这些疑惑的答案，我们都会在下一篇中全部揭晓。



## 练习题

使用协程下载一张图，并行进行**两次**切割

- 一次切成大小相同的 4 份，取其中的第一份
- 一次切成大小相同的 9 份，取其中的最后一份

得到结果后，将它们展示在两个 ImageView 上。

 

## 作者介绍

### 视频作者

#### 扔物线（朱凯）

- 码上开学创始人、项目管理人、内容模块规划者和视频内容作者。
- [Android GDE](https://developers.google.com/experts/people/kai-zhu)（ Google 认证 Android 开发专家），前 Flipboard Android 工程师。
- GitHub 全球 Java 排名第 92 位，在 [GitHub](https://github.com/rengwuxian) 上有 6.6k followers 和 9.9k stars。
- 个人的 Android 开源库 [MaterialEditText](https://github.com/rengwuxian/MaterialEditText/) 被全球多个项目引用，其中包括在全球拥有 5 亿用户的新闻阅读软件 Flipboard 。
- 曾多次在 Google Developer Group Beijing 线下分享会中担任 Android 部分的讲师。
- 个人技术文章《[给 Android 开发者的 RxJava 详解](https://gank.io/post/560e15be2dca930e00da1083)》发布后，在国内多个公司和团队内部被转发分享和作为团队技术会议的主要资料来源，以及逆向传播到了美国一些如 Google 、 Uber 等公司的部分华人团队。
- 创办的 Android 高级进阶教学网站 [HenCoder](https://hencoder.com/) 在全球华人 Android 开发社区享有相当的影响力。
- 之后创办 Android 高级开发教学课程 [HenCoder Plus](https://plus.hencoder.com/) ，学员遍布全球，有来自阿里、头条、华为、腾讯等知名一线互联网公司，也有来自中国台湾、日本、美国等地区的资深软件工程师。

### 文章作者

#### Hugo（谢晨成）

[Hugo（谢晨成）](https://github.com/xcc3641)，即刻 Android 工程师。2017 年加入即刻，参与了即刻 3.0 到 6.0 版本的架构设计和产品迭代。多年 Android 开发经验，现在负责即刻客户端中台基础建设。