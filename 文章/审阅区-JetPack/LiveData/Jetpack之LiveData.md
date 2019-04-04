# Jetpack 之 LiveData

## 简介
LiveData 是Jetpack中的一个组件，是一个可被观察的数据存储器类， 具有感知组件生命周期的能力，LiveData 可以感知组件生命周期活跃状态发送数据更新，在组件销毁时移除观察者对象，大多结合具有生命周期的组件一起使用（如 Activity、Fragment 或 Service，或实现了 LifecycleOwner 接口的对象）。


## 作用
那么 LiveData 有什么用呢？主要有如下两个作用：
- 实时刷新数据
- 防止内存泄漏

LiveData 采用的是观察者模式，当 LiveData 保存的数据发生变化时就会通知观察者，观察者接收到通知后可以进行 UI 数据刷新或者其他操作。

那它是怎么做到防止内存泄漏的呢 ？在给 LiveData 添加观察者对象的时候可以绑定一个具有生命周期的组件，当组件生命周期处于活跃状态（即 STARTED 、RESUMED 状态）时数据更新才会通知观察者，当组件被销毁时则会自动移除对应的观察者对象，从而防止一直持有对应组件防止内存泄漏。

## Hello LiveData

学习任何一个新技术我们习惯先来一个 Hello World，那么先来看一下 Hello LiveData 吧。

### 添加依赖
在 module 的 gradle.gradle 里引入 LiveData 包，如下：
```gradle
dependencies {
	def lifecycle_version = "1.1.1"
        implementation 'com.android.support:appcompat-v7:28.0.0'
	implementation "android.arch.lifecycle:livedata:$lifecycle_version"
}
```
如果使用 Androidx 的话需要引入 Androidx 下的对应 LiveData 包和 appcompat 包：
```gradle
dependencies {
	def lifecycle_version = "1.1.1"
        implementation 'androidx.appcompat:appcompat:1.0.0-beta01'
	implementation "androidx.lifecycle:lifecycle-livedata:$lifecycle_version"
}
```
### 使用
依赖包引入进来了，接下来看看怎么快速使用 `LiveData` 
 
创建一个 `MutableLiveData` 对象，它是 LiveData 的子类，然后给它添加观察者对象，代码如下：

**java**:
```java
final MutableLiveData<String> simpleLiveData = new MutableLiveData<>();

Observer<String> observer = new Observer<String>() {
    @Override
    public void onChanged(@Nullable String text) {
        mTextView.setText(text);
    }
};
simpleLiveData.observe(this, observer);
```
**kotlin**:
```kotlin
val simpleLiveData = MutableLiveData<String>()
val observer = Observer<String> { text ->
    mTextView.text  = text
}
simpleLiveData.observe(this, observer)
```
> observe 方法中的 `this` 是实现了`LifecycleOwner` 接口的对象，比如 support 里的 `AppCompatActivity` 等

当我们对 simpleLiveData 数据进行更新时且观察者绑定的生命周期组件（如 Activity / Fragment 等实现了`LifecycleOwner` 接口的对象）处于活跃状态即 `STARTED` 或 `RESUMED`  状态时就会触发 `Observer` 的回调从而更新 mTextView 的值，即进行 UI 数据更新。

比如点击按钮改变 simpleLiveData 的值为 "Hello LiveData" ，就会触发 Observer 的 onChanged 方法

```java
mButton.setOnClickListener(view -> {
     simpleLiveData.setValue("Hello LiveData") 
});
```
概括如下 ：
- 创建 LiveData 对象 ： `new MutableLiveData<>()`
- 创建观察者对象：`new Observer()`
- 绑定观察者对象：`LiveData.observe`
- 更新 LiveData  数据：` LiveData.setValue`

## 详细介绍
### 1、Api 介绍
LiveData 是一个带泛型的抽象类，有两个子类 `MutableLiveData` 和 `MediatorLiveData` 下面看一下 LiveData 类的关系图 ：
![kco47n.jpg](https://s2.ax1x.com/2019/02/19/kco47n.jpg)

- `public T getValue()` : 获取 LiveData 里的数据
- `public boolean hasActiveObservers()` ： 是否存在活跃的观察者对象
- `public boolean hasObservers()` ：是否有观察者对象
- `public void observe(LifecycleOwner owner, Observer<? super T> observer) ` ：添加感知生命周期的观察者对象
- `public void observeForever(Observer<? super T> observer)`：添加无生命周期感知的观察者对象
- `public void removeObserver(final Observer<? super T> observer)`：移除对应的观察者对象
- `public void removeObservers(final LifecycleOwner owner)` ：根据生命周期对象移除观察者对象
- `protected void setValue(T value)`：设置 LiveData 容器的数据
- `protected void postValue(T value)` ： 在主线程设置 LiveData 容器的数据
- `protected void onActive()`：当活跃的观察者对象数量大于 0 时调用，即有活跃的观察者对象时调用
- `protected void onInactive()`：当活跃的观察者对象数量等于 0 时调用，即无活跃的观察者对象时调用


`MutableLiveData`：可变的 LiveData，是我们最常用的 LiveData 子类。它的实现很简单，就是继承了 LiveData 然后向外暴露了 `setValue`、`postValue` 方法

`MediatorLiveData`：它继承自 MutableLiveData 可以监听多个 LiveData 数据源，或者调度多个 LiveData 数据源决定向观察者发送那个 LiveData 的数据更新。它新增了两个方法 `addSource` 、`removeSource` 用于添加和删除 LiveData 源

`Observer`: 观察者接口，通过该接口对 LiveData 数据进行观察

### 2、详细使用 
#### MutableLiveData 的使用
前面 Hello LiveData 简单展示了 LiveData 的使用

LiveData 除了依赖生命周期对象实现观察者的自动管理外，还可以添加忽略生命周期的观察者， 使用 `observeForever` 方法：

**java**:
```java
MutableLiveData<String> liveData = new MutableLiveData<>();
liveData.observeForever(new Observer<String>() {
    @Override
    public void onChanged(String s) {
        //do something
    }
});
```
**kotlin**:
```kotlin
val liveData = MutableLiveData<String>()
liveData.observeForever {
    //do something
}
```
这种情况当不需要进行观察的时候就需要手动调用 `removeObserver` 将观察者移除，防止内存泄漏。

#### 变换操作 Transformations
上面介绍了 LiveData 的基础使用，我们还可以使用 `Transformations` 对 LiveData 进行变换操作，它提供了两个操作符 `map` 和 `switchMap` 他们的作用都是将一个 LiveData 转换为另一个 LiveData 对象，当一个 LiveData 里的值发生改变时另一个 LiveData的值也随之发生改变。
看一下具体如何使用，
使用 `map` 将 `LiveData<User>` 转换为 `LiveData<String>`：

**java**:
```java
final MutableLiveData<User> userLiveData = new MutableLiveData<>();
final LiveData<String> userDescribe = Transformations.map(userLiveData, new Function<User, String>() {
     @Override
     public String apply(User user) {
         return "id:" + user.getId() + ", name:" + user.getName() + ", age:" + user.getAge();
     }
 });
```
**kotlin**:
```kotlin
val userLiveData = MutableLiveData<User>()
val userDescribe = Transformations.map(userLiveData) { user -> 
	"id: ${user.id}  name: ${user.name} age: ${user.age}" 
}
```
当 userLiveData 的值发生改变时，userDescribe 的值也会随之变化。

使用 `switchMap` 将 `LiveData<Long>` 转换为 `LiveData<User>`:

**java**:
```java
private LiveData<User> getUser(long id){
	//...
}
//....
final MutableLiveData<Long> userIdLiveData = new MutableLiveData<>();
final LiveData<User> userLiveData = Transformations.switchMap(userIdLiveData, new Function<Long, LiveData<User>>() {
    @Override
    public LiveData<User> apply(Long id) {
        return getUser(id);
    }
});
```
**kotlin**:
```kotlin
private fun getUser(id: Long): LiveData<User> {
	//...
}
//...
val userIdLiveData = MutableLiveData<Long>()
val userLiveData = Transformations.switchMap(userIdLiveData) { id -> 
	getUser(id)
}
```
#### MediatorLiveData
`MediatorLiveData` 继承自 MutableLiveData 可以添加多个 LiveData 数据源，可以观察或调度多个 LiveData 数据源。前面介绍 Transformations 的变换操作实际上就是返回的 MediatorLiveData ，看一下 `MediatorLiveData` 的使用：

**java**:
```java
MutableLiveData<User> userLiveData1 = new MutableLiveData<>();
MutableLiveData<User> userLiveData2 = new MutableLiveData<>();
MediatorLiveData<User> userMediatorLiveData = new MediatorLiveData<>();
userMediatorLiveData.addSource(userLiveData1, new Observer<User>() {
    @Override
    public void onChanged(User user) {
        userMediatorLiveData.setValue(user);
    }
});
userMediatorLiveData.addSource(userLiveData2, new Observer<User>() {
    @Override
    public void onChanged(User user) {
        userMediatorLiveData.setValue(user);
    }
});
```
**kotlin**:
```kotlin
val userLiveData1 = MutableLiveData<User>()
val userLiveData2 = MutableLiveData<User>()
val userMediatorLiveData = MediatorLiveData<User>()
userMediatorLiveData.addSource(userLiveData1) { user -> 
	userMediatorLiveData.value = user
}
userMediatorLiveData.addSource(userLiveData2) { user -> 
	userMediatorLiveData.value = user
}
```
上面我们为 `userMediatorLiveData` 添加了两个 LiveData 源 `userLiveData1` 和 `userLiveData2` ，当其中任意一个数据更新且在 userMediatorLiveData 的活跃生命周期内就会更新 userMediatorLiveData。
有的人可能会有疑问用 `MediatorLiveData` 有什么用 ？感觉直接用 LiveData 好像也能实现相同的效果，给 userLiveData1 和 userLiveData1 设置监听然后将变化的数据设置给另一个 LiveData 好像也能达到效果，如下：

```kotlin
val userLiveData1 = MutableLiveData<User>()
val userLiveData2 = MutableLiveData<User>()
val userLiveData = MutableLiveData<User>()
userLiveData1.observe(this, Observer { user -> 
    userLiveData.value = user
})  
userLiveData2.observe(this, Observer { user -> 
    userLiveData.value = user
})
```
这样确实能实现上述相同的效果，区别在于 userLiveData1 和 userLiveData2 分别要设置 `LifecycleOwner` 而 `MediatorLiveData` 能统一管理添加到它内部所有 LiveData 的生命周期， `MediatorLiveData` 重写了 LiveData 的 `onActive` 和 `onInactive` 方法统一去添加和移除它内部 LiveData 的 `Observer`

#### 自定义LiveData
除了使用库里提供的 `MutableLiveData` 和 `MediatorLiveData` 外我们还可以根据实际场景继承 `LiveData` 自定义我们自己的 LiveData，比如我们需要展示最新一条消息的 MessageLiveData ，看看怎么实现：

**java**:
```java
public class MessageLiveData extends LiveData<String> {
    private MessageManager messageManager;
    public MessageLiveData(){
        messageManager = MessageManager.getInstance();
    }

	//最新消息回调
    private MessageManager.MessageCallback messageCallback = new MessageManager.MessageCallback() {
        @Override
        public void onMessage(String message) {
            setValue(message);
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        messageManager.addMessageCallback(messageCallback);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        messageManager.removeMessageCallback(messageCallback);
    }
}
```
**kotlin**:
```kotlin
class MessageLiveData : LiveData<String>() {

    private val messageManager:MessageManager by lazy{
        MessageManager.getInstance()
    }

    private val messageCallback = MessageManager.MessageCallback { message ->
        value = message
    }

    override fun onActive() {
        super.onActive()
        messageManager.addMessageCallback(messageCallback)
    }

    override fun onInactive() {
        super.onInactive()
        messageManager.removeMessageCallback(messageCallback)
    }
}
```
MessageLiveData 继承自 LiveData 在 `onActive` 里注册消息监听，`onInactive` 里移除监听，这样我们就可以使用 MessageLiveData 对最新消息进行观察。


#### LiveData 结合 ViewModel 使用
前面介绍 LiveData 的使用时都是直接在Activity里使用的，但是真实开发场景中我们一般不直接在 Activity / Fragment 中使用而是在 ViewModel 中使用，然后在 Activity / Fragment 中观察 ViewModel 里 LiveData 数据的变化：

**java**:
```java
public class MainViewModel extends ViewModel {
    public MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public void loadUser(){
        //...

        userLiveData.setValue(user);
    }
}

//Activity
MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
viewModel.userLiveData.observe(this, new Observer<User>() {
    @Override
    public void onChanged(User user) {
        mTextView.setText(user.getName());
    }
});
```
**kotlin**:
```kotlin
class MainViewModel : ViewModel() {
    var userLiveData = MutableLiveData<User>()

    fun loadUser() {
        //...

        userLiveData.setValue(user)
    }
}
//Activity
val viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
viewModel.userLiveData.observe(this, Observer { user ->
    mTextView.text = user.name
})
```

> 关于 `ViewModel` 的详细介绍请参考 [Jetpack 之 ViewModel]()
 
#### LiveData 结合 DataBinding 使用
接下来看看 LiveData 结合 DataBinding 的使用，还是上面使用的 MainViewModel ：

**java**:
```java
ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
binding.setLifecycleOwner(this);
MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
binding.setVm(viewModel);
```
**kotlin**:
```kotlin
val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
binding.setLifecycleOwner(this)
val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
binding.vm = viewModel
```
**activity_main：**
```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="vm"
            type="com.example.livedata.MainViewModel"/>
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">

        <TextView
            android:id="@+id/text_view"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{vm.userLiveData.name}"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```
这里 ViewModel 里我们没有使用 DataBinding 的 Observable 而是使用的 LiveData ，在数据绑定的时候给 ViewDataBinding 设置了 `LifecycleOwner` 即 `binding.setLifecycleOwner(this)` ，当数据绑定时 ViewDataBinding 内部会自动给绑定的 LiveData 对象添加观察者对象观察数据的更新从而刷新 UI 数据。
> 关于 `DataBinding` 的详细介绍请参考 [Jetpack 之 DataBinding]()

## 原理
前面介绍了 LiveData 的使用，接下来看看 LiveData 内部是怎么实现只在生命周期活跃状态下回调观察者的观察方法的。

先看一下 `observe` 方法源码：
```java
MainThread
public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
    assertMainThread("observe");
    if (owner.getLifecycle().getCurrentState() == DESTROYED) {
        // ignore
        return;
    }
    LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer);
    ObserverWrapper existing = mObservers.putIfAbsent(observer, wrapper);
    if (existing != null && !existing.isAttachedTo(owner)) {
        throw new IllegalArgumentException("Cannot add the same observer"
                + " with different lifecycles");
    }
    if (existing != null) {
        return;
    }
    owner.getLifecycle().addObserver(wrapper);
}
```
首先检查是否在主线程，然后检查生命周期状态，如果是 `DESTROYED` 即销毁状态则直接 return ，然后将 `LifecycleOwner`  和 `Observer` 封装成 `LifecycleBoundObserver` 放入到 `mObservers` Map 里并将其添加到生命周期观察里。`LifecycleBoundObserver` 继承自 `ObserverWrapper` 并实现了 `GenericLifecycleObserver` 接口，在 `onStateChanged` 里监听了生命周期的变化：
```java
Override
public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
    if (mOwner.getLifecycle().getCurrentState() == DESTROYED) {
        removeObserver(mObserver);
        return;
    }
    activeStateChanged(shouldBeActive());
}
```
在生命周期 `DESTROYED` 状态将观察者移除，其他状态调用 `activeStateChanged` 方法去处理是否回调观察者的回调，这样就达到了根据生命周期自动管理观察者的目的。

然后再看 `setValue` 方法：
```java
@MainThread
protected void setValue(T value) {
    assertMainThread("setValue");
    mVersion++;
    mData = value;
    dispatchingValue(null);
}
```
继续 `dispatchingValue` 方法：
```java
void dispatchingValue(@Nullable ObserverWrapper initiator) {
    if (mDispatchingValue) {
        mDispatchInvalidated = true;
        return;
    }
    mDispatchingValue = true;
    do {
        mDispatchInvalidated = false;
        if (initiator != null) {
            considerNotify(initiator);
            initiator = null;
        } else {
            for (Iterator<Map.Entry<Observer<? super T>, ObserverWrapper>> iterator =
                    mObservers.iteratorWithAdditions(); iterator.hasNext(); ) {
                considerNotify(iterator.next().getValue());
                if (mDispatchInvalidated) {
                    break;
                }
            }
        }
    } while (mDispatchInvalidated);
    mDispatchingValue = false;
}
```
重点在 `considerNotify` 方法：
```java
private void considerNotify(ObserverWrapper observer) {
    if (!observer.mActive) {
        return;
    }
    // Check latest state b4 dispatch. Maybe it changed state but we didn't get the event yet.
    //
    // we still first check observer.active to keep it as the entrance for events. So even if
    // the observer moved to an active state, if we've not received that event, we better not
    // notify for a more predictable notification order.
    if (!observer.shouldBeActive()) {
        observer.activeStateChanged(false);
        return;
    }
    if (observer.mLastVersion >= mVersion) {
        return;
    }
    observer.mLastVersion = mVersion;
    //noinspection unchecked
    observer.mObserver.onChanged((T) mData);
}
```
先检查 `ObserverWrapper` 即前面 `observe` 方法里封装的 `LifecycleBoundObserver` 是否是活跃的，然后调用 `shouldBeActive` 方法，`LifecycleBoundObserver` 里其实就是判断生命周期是否处于活跃状态
```java
@Override
boolean shouldBeActive() {
    return mOwner.getLifecycle().getCurrentState().isAtLeast(STARTED);
}
```
然后是 `ObserverWrapper` 的最后版本与当前版本的比较，如果`>=` 则 return，每次调用 `setValue` 方法当前版本 `mVersion++`，最后则是调用观察者的回调，即我们传入的 `Observer` 的 `onChanged` 方法。

再来看 `observeForever` 方法：
```java
@MainThread
public void observeForever(@NonNull Observer<? super T> observer) {
    assertMainThread("observeForever");
    AlwaysActiveObserver wrapper = new AlwaysActiveObserver(observer);
    ObserverWrapper existing = mObservers.putIfAbsent(observer, wrapper);
    if (existing != null && existing instanceof LiveData.LifecycleBoundObserver) {
        throw new IllegalArgumentException("Cannot add the same observer"
                + " with different lifecycles");
    }
    if (existing != null) {
        return;
    }
    wrapper.activeStateChanged(true);
}
```
它将 `Observer` 封装成 `AlwaysActiveObserver`，它的 `shouldBeActive` 方法直接返回 `true` 并调用 `activeStateChanged(true);` 设置 active 为 true，也就是一直处于活跃状态，所以能一直观察数据的更新。

> 关于 `Lifecycle` 的详细介绍请参考 [Jetpack 之 Lifecycle]()
