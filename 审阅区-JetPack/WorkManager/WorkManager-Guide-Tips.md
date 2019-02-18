---
title: WorkManager-Guide&Tips
date: 2019-01-20 18:16:00
tags:
---
# WorkManager-Guide&Tips

[WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager/) 为了方便运行一些**不着急的**、**异步的**的**后台**任务而诞生. 大部分情况下, 只需要定义好自己想做的任务, 交给 `WorkManager` 去执行, 剩下就不用管了.

注意一下, 同样是后台线程, `WorkManager` 的重点在于保证**就算 App 关掉之后后台任务也能够被执行**. 而那种可以随着 App 退出而关闭的后台任务, 还是更适合使用 `ThreadPools`.

## 以前的实现方案

1. Service: 这是最常见的需要后台运行的方案了. 对比来说, Service 有以下几个问题: 

	* 可能会由于开发者的设置而疯狂运行, 这会导致手机电量被疯狂消耗. 对比之下 WorkManager 的同一个周期任务的最小间隔时间是15分钟.
	* `targetSdkVersion` 为 26 及以上的时候, 在不被允许创建后台服务的情况下, `startService()` 会抛出 `IllegalStateException`. 对比之下 WorkManager 会按照设定选择合适的时间运行.

2. JobScheduler: 这个最关键的就是只有 Android 5.0 以上才能使用, 其实 WorkManager 在 5.0 以上也是用这个实现的.
3. AlarmManager + BroadcastReceiver. 这个方案也是可以的, WorkManager 在 5.0 以下也是这样实现的, 只是封装了更好用的 API .

如果对更好用的 WorkManager 感兴趣, 就可以继续往下看了. 大概的介绍顺序是:

1. 导入
2. 一次性任务的使用
3. 周期性任务的使用
4. 任务如何取消
5. 给任务加上约束条件
6. 多个任务以特定顺序执行
7. 相同任务的重复处理策略
8. 任务的输入和输出
9. 一些需要注意的点

**以下代码都可以在 [Demo](https://github.com/niorgai/WorkManagerDemo) 中找到.**

## 导入

和其他 JetPack 的组件一样, 在 **project** 的 `build.gradle` 文件中添加 `google()` 源: 

``` groovy
allprojects {
    repositories {
        google()
        jcenter()
    }
}
```
然后在 **module** 的 `build.gradle` 中添加 **WorkManager** 的依赖:

``` groovy
dependencies {
    def work_version = "1.0.0-beta05"
    
    // Java 依赖版本
    implementation "android.arch.work:work-runtime:$work_version"
    
    // Kotlin 依赖版本, 和上面的依赖二选一即可
    implementation "android.arch.work:work-runtime-ktx:$work_version"
    
    // 可选 RxJava2 支持
    implementation "android.arch.work:work-rxjava2:$work_version"

    // 可选 测试支持
    androidTestImplementation "android.arch.work:work-testing:$work_version"
}
```
## 基本使用

使用起来就如上面所说, 首先你需要创建一个 `任务 (Worker) `, 然后丢给 `WorkManager`.

``` kotlin
Kotlin
class TestWorker(context: Context, params: WorkerParameters)
    : Worker(context, params) {

    override fun doWork(): Result {
    
		// 这里已经是后台线程了, 只需要实现自己的业务逻辑就好了
		// return Result.retry(); 重试
		// return Result.failure(); 不再重试
    	return Result.success()
    }

}
```

``` Java
Java
public class TestWorker extends Worker {

    public TestWorker(Context context, WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {

        // 这里已经是后台线程了, 只需要实现自己的业务逻辑就好了
        // return Result.retry(); 重试
        // return Result.failure(); 不再重试
        return Result.success();
    }
}
```

而 `Worker` 里面只声明要实现的任务, 其他的约束条件要在 `WorkRequest` 中设置, 把 `Worker` 变成 `WorkRequest`. 再交给 `WorkManager` 去执行就好了.

### 一次性任务

``` kotlin
Kotlin
val oneTimeWorker = OneTimeWorkRequest.Builder(TestWorker::class.java).build()
WorkManager.getInstance().enqueue(oneTimeWorker)
```

``` Java
Java
OneTimeWorkRequest oneTimeWorker =
        new OneTimeWorkRequest.Builder(TestWorker.class).build();
WorkManager.getInstance().enqueue(oneTimeWorker);
```

就是这么简单, 接下来 Worker 就会在后台线程运行了. 

### 周期性任务
周期性任务需要更加慎重一点. 开启之后如果不注意, 大部分情况下就会一直运行, 这可能带来很不好的用户体验.

设置周期性任务的时候, 需要设置 `repeatInterval(重复区间)` 和 `flexInterval(弹性区间)` 参数, 配合注释说明:

```Java
[  弹性区间外  |  弹性区间内 (flex Interval) ][  弹性区间外  |  弹性区间内 (flex Interval) ]...
[  任务不运行  |          任务可运行         ][  任务不运行  |          任务可运行         ]...
\_________________________________________/\________________________________________/...
       第一个区间 (repeat Interval)                 第二个区间 (repeat Interval)        ...(repeat)

```
`repeatInterval` 最小值是15分钟, 而 `flexInterval` 的最小值是5分钟, 如果 `flexInterval` 大于 `repeatInterval`, 也会被修改到和 `repeatInterval` 一样的值.

### 取消任务

但是从 API 可以看到, WorkManager 是将这个 Worker 入队了, 那既然是以队列维护的异步操作, 肯定会有重复的问题. WorkManager 默认的操作是遇到一样的 Worker 时, 新 Worker 会等旧 Worker 运行完再运行, 即顺序执行.

不过大部分情况下这都不是我们想要的模式, 所以在运行前最好取消相同的任务. 每个 `Worker` 都有一个唯一标识 UUID, 同时在构建 `WorkRequest` 的时候还可以添加任意个 Tag, 通过这两个标识都可以取消任务.

```Kotlin
Kotlin
// UUID 方式
val workId: UUID = oneTimeWorker.getId()
WorkManager.getInstance().cancelWorkById(workId)

// Tag 方式
val oneTimeWorker = OneTimeWorkRequest.Builder(TestWorker::class.java)
    .addTag("myTag")
    .build()
WorkManager.getInstance().cancelAllWorkByTag("myTag")
```

```Java
Java
// UUID 方式
UUID workId = oneTimeWorker.getId();
WorkManager.getInstance().cancelWorkById(workId);

// Tag 方式
OneTimeWorkRequest myTask = new OneTimeWorkRequest.Builder(TestWorker.class)
    .addTag("myTag")
    .build()
WorkManager.getInstance().cancelAllWorkByTag("myTag")
```

取消相同的任务已经避免了系统资源不必要的消耗, 不过为了防止 API 的滥用, 还推荐给任务加上一些约束条件, 方便任务在系统资源没那么紧张的时候再执行:


## 加上约束

所有的约束 `Constraints` 都是由 `Constraints.Builder()` 来创建的, Builder 提供了以下的约束方式:

```kotlin
Kotlin
// 设置网络类型
setRequiredNetworkType(networkType: NetworkType)
// 是否运行时电量不要太低
setRequiresBatteryNotLow(requiresBatteryNotLow: Boolean)
// 是否在充电时才运行
setRequiresCharging(requiresCharging: Boolean)
// 是否不太剩余存储空间过低时运行
setRequiresStorageNotLow(requiresStorageNotLow: Boolean)
// 是否在设备空闲时运行, 这个最低版本是 23
setRequiresDeviceIdle(requiresDeviceIdle: Boolean)
// 监听一个本地的 Uri, 第二个参数是否监听 Uri 的子节点. 在 Uri 的内容改变时运行任务, 最低版本是 24
addContentUriTrigger(uri: Uri, triggerForDescendants: Boolean)
```

```Java
Java
// 设置网络类型
setRequiredNetworkType(NetworkType networkType)
// 是否运行时电量不要太低
setRequiresBatteryNotLow(boolean requiresBatteryNotLow)
// 是否在充电时才运行
setRequiresStorageNotLow(boolean requiresStorageNotLow)
// 是否不太剩余存储空间过低时运行
setRequiresStorageNotLow(requiresStorageNotLow: Boolean)
// 是否在设备空闲时运行, 这个最低版本是 23
setRequiresDeviceIdle(boolean requiresDeviceIdle)
// 监听一个本地的 Uri, 第二个参数是否监听 Uri 的子节点. 在 Uri 的内容改变时运行任务, 最低版本是 24
addContentUriTrigger(Uri uri, boolean triggerForDescendants)
```

## 多个任务的执行顺序
WorkManager 提供了相应的 API 使任务可以使一个或多个 `OneTimeWorkerRequest` 按某个顺序执行.

``` Kotlin
// A, B, C 就会按顺序执行, 如果全部返回成功或者某一个返回失败, 那该任务链就会结束.
WorkManager.getInstance()
    .beginWith(workA)
    .then(workB)
    .then(workC)
    .enqueue()

// A, B 一起运行, 虽然这2个的开始顺序不定, 但是 C 一定是在这2个运行后才运行.
WorkManager.getInstance()
    .beginWith(Arrays.asList(workA, workB))
    .then(workC)
    .enqueue()
    
// B 一定会在 A 后面运行, D 也一定会在 C 后面运行, 但是 AB 与 CD 这两条链的运行顺序不定, 但是 E 一定是在 B 和 D 都结束后才运行.
val chain1 = WorkManager.getInstance()
    .beginWith(workA)
    .then(workB)
val chain2 = WorkManager.getInstance()
    .beginWith(workC)
    .then(workD)
val chain3 = WorkContinuation
    .combine(Arrays.asList(chain1, chain2))
    .then(workE)
chain3.enqueue()
```

``` Java
// A, B, C 就会按顺序执行, 如果全部返回成功或者某一个返回失败, 那该任务链就会结束.
WorkManager.getInstance()
    .beginWith(workA)
    .then(workB)
    .then(workC)
    .enqueue();

// A, B 一起运行, 虽然这2个的开始顺序不定, 但是 C 一定是在这2个运行后才运行.
WorkManager.getInstance()
    .beginWith(Arrays.asList(workA, workB))
    .then(workC)
    .enqueue();
    
// B 一定会在 A 后面运行, D 也一定会在 C 后面运行, 但是 AB 与 CD 这两条链的运行顺序不定, 但是 E 一定是在 B 和 D 都结束后才运行.
WorkContinuation chain1 = WorkManager.getInstance()
    .beginWith(workA)
    .then(workB);
WorkContinuation chain2 = WorkManager.getInstance()
    .beginWith(workC)
    .then(workD);
WorkContinuation chain3 = WorkContinuation
    .combine(Arrays.asList(chain1, chain2))
    .then(workE);
chain3.enqueue();
```
## 相同任务的重复策略

前面提到对于 Worker 来说, 可以通过 UUID 和 Tag 来保证其唯一性, 这样在需要的时候就可以避免任务重复执行. 但对于连续的任务链, 如果任务多了, 这样的方式会很繁琐. 于是, WorkerManager 也提供了相应的 API 来保证其唯一性.

```kotlin
Kotlin
beginUniqueWork(uniqueWorkName: String, existingWorkPolicy: ExistingWorkPolicy, work: OneTimeWorkRequest): WorkContinuation

beginUniqueWork(uniqueWorkName: String, existingWorkPolicy: ExistingWorkPolicy, work: List<OneTimeWorkRequest>): WorkContinuation
```

```Java
Java
WorkContinuation beginUniqueWork(String uniqueWorkName, ExistingWorkPolicy existingWorkPolicy, OneTimeWorkRequest work)

WorkContinuation beginUniqueWork(String uniqueWorkName, ExistingWorkPolicy existingWorkPolicy, List<OneTimeWorkRequest> work)
```

第一个参数就是这一个或者一系列 worker 的名字, 第二个参数就是重复时的操作, 有以下几种模式:

* ExistingWorkPolicy.APPEND : 如果上一个任务处于等待或者未完成的状态, 则把当前任务添加到其任务链的后面. 这样它就在上一个任务执行完后执行.
* ExistingWorkPolicy.KEEP : 如果上一个任务处于等待或者未完成的状态, 什么都不做(继续等上一个任务执行).
* ExistingWorkPolicy.REPLACE : 如果上一个任务处于等待或者未完成的状态, 取消并删除上一个, 执行新的.

## 输入和输出

Worker 的输入输出是用 `Map<String, Object>` 来存储的, 用 `Data` 类封装了一层. 输出用 `LiveData` 来监听.

```Kotlin
Kotlin

// 创建输入
val inputData = Data.Builder()
                    .putInt("KEY_FIRST", firstNumber)
                    .putInt("KEY_SECOND", secondNumber)
                    .build()
val worker = OneTimeWorkRequestBuilder<MathWorker>()
        .setInputData(inputData)
        .build()
WorkManager.getInstance().enqueue(worker)

// Worker 类:
class PlusWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val first = inputData.getInt("KEY_FIRST", 0)
        val second = inputData.getInt("KEY_SECOND", 0)
        val result = first + second // 1 + 2 = 3
        val output = Data.Builder()
                .putInt("KEY_RESULT", result)
                .build()
        return Result.success(output)
    }
}

// 监听返回
WorkManager.getInstance().getWorkInfoByIdLiveData(worker.id)
        .observe(this, Observer { info ->
            if (info != null && info.state.isFinished) {
               	// 获取返回结果, 应该是3
                val result = info.outputData.getInt("KEY_RESULT", 0)
            }
        })

```

```Java
Java
// 创建输入
Data inputData = new Data.Builder()
    .putInt("KEY_FIRST", 1)
    .putInt("KEY_SECOND", 2)
    .build();

OneTimeWorkRequest worker = new OneTimeWorkRequest.Builder(PlusWorker.class)
        .setInputData(inputData)
        .build();
WorkManager.getInstance().enqueue(worker);

// Worker 类:
public class PlusWorker extends Worker {

    public PlusWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        int first = getInputData().getInt("KEY_FIRST", 0);
        int second = getInputData().getInt("KEY_SECOND", 0);
        int result = first + second; // 1 + 2 = 3
        Data output = new Data.Builder()
                .putInt("KEY_RESULT", result)
                .build();
        return Result.success(output);
    }
}

// 监听返回
WorkManager.getInstance().getWorkInfoByIdLiveData(worker.getId())
    .observe(lifecycleOwner, info -> {
         if (info != null && info.getState().isFinished()) {
           // 获取返回结果, 应该是3
           int result = info.getOutputData().getInt(KEY_RESULT, 0));
         }
    });

```


## 一些需要注意的地方

* `WorkManager` 虽然在设计的时候是为了在 App 没运行的时候也能运行 Worker, 但是目前从 Google Issue Tracker 上的信息来看, 以下几种情况杀掉后任务的存活情况是这样的:

	1. 从任务管理器(最近使用)关掉: 原生的 Android 上 Worker 仍然会运行, 但是在[某些把这种操作当做强制停止的厂商](https://issuetracker.google.com/issues/110745313) 或 [一些中国厂商](https://issuetracker.google.com/issues/113676489) 的机型上, Worker 要等到下次打开 App 才会运行.
	2. 重启手机 (Worker 运行中的状态): 重启后 Worker 会继续运行.
	3. App 信息 -> 强制关闭: Worker 会再下次打开 App 的时候运行.
	4. 重启手机 (App 被强制关闭了): Worker 会再下次打开 App 的时候运行.
