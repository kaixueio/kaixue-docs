# Data Binding 详解（四）-生成的绑定类
>作者：木易  
知是行之始，行是知之成。  
[文章配套的 Demo](https://github.com/muyi-yang/DataBindingDemo)：https://github.com/muyi-yang/DataBindingDemo

Data Binding 生成用于访问布局变量和视图的绑定类，它将布局变量与布局中的视图链接起来。默认情况下，类的名称基于布局文件的名称，将其转换为Pascal大小写并向其添加Binding后缀。比如布局文件名是 activity_main.xml 相应生成的类 ActivityMainBinding，也可以自定义绑定类的名称和包。 所有以 `<data>` 为根标签的布局都会生成绑定类，都继承自 [`ViewDataBinding`](https://developer.android.google.cn/reference/android/databinding/ViewDataBinding.html)
类。这个类包含从布局属性(例如，声明的变量)到布局视图的所有绑定，并且知道如何为绑定表达式分配值，有兴趣的同学可以看看生成后的类是怎么实现的。
## 创建绑定对象
创建绑定类的对象有两种方式，一种是使用 [`DataBindingUtil`](https://developer.android.google.cn/reference/android/databinding/DataBindingUtil.html) 工具类，一种是直接使用绑定类的静态方法来获取。

在 Activity 中我们一般使用 DataBindingUtil 工具类来获取绑定对象，因为它有一个 setContentView 方法，里面调用了 Activity 的 setContentView 方法并返回了绑定类对象，比如：
``` java
ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
```
也可以通过 DataBindingUtil 工具类的 inflate 方法获取：
``` java
ActivityMainBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_main, null, false);
```
但在其他地方（Fragment、ListView 或 RecyclerView 等）我们一般会直接使用绑定类的静态方法来获取，比如：
``` java
LayoutCelebrityItemBinding binding = LayoutCelebrityItemBinding.inflate(inflater);
// 或者
LayoutCelebrityItemBinding binding = LayoutCelebrityItemBinding.inflate(inflater, viewGroup, false);
```
有时你可能需要使用老方式 inflate 一个布局，你可以这样获取绑定对象：
``` java
View viewRoot = LayoutInflater.from(this).inflate(layoutId, parent, attachToParent)
MyLayoutBinding binding = MyLayoutBinding.bind(viewRoot)
```
有时无法事先知道绑定类型，布局可能是动态的。 在这种情况下，可以使用 DataBindingUtil 类创建绑定，如下面的代码片段所示:
``` java
View viewRoot = LayoutInflater.from(this).inflate(layoutId, parent, attachToParent)
ViewDataBinding binding = DataBindingUtil.bind(viewRoot)
// 或者
ViewDataBinding binding = DataBindingUtil.inflate(getLayoutInflater(), layoutId, parent, attachToParent);
```
至此我们了解了两个获取绑定类对象的两种方式，可以根据不同的场景运用不同的方式，其实绑定类中的 inflate 方法的最终也是调用 DataBindingUtil 的 inflate 方法，bind 方法也是一样，有兴趣的同学可以阅读一下生成的绑定类代码。
## 带 ID 的 View
Data Binding 在绑定类中为每个在布局中具有 ID 的 View 创建不可变字段。例如，Data Binding 从以下布局创建类型为 TextView 的 tvInfo，类型为 Button 的 btnBinding 字段：
``` xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
     ...
    <android.support.constraint.ConstraintLayout xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">

        <TextView
            android:id="@+id/tv_info"
            ... />
        <Button
            android:id="@+id/btn_binding"
            ... />
        <Button
            android:id="@+id/btn_list"
            ... />
    </android.support.constraint.ConstraintLayout>
</layout>
```
在绑定类中已经生成了相应的字段，我们不在需要调用 findViewById () 方法获取，可以直接从绑定类中获取：
``` java
ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
binding.tvInfo.setText("我是使用Data Binding的Demo");
``` 
## 变量
Data Binding 为布局中声明的每个变量生成访问方法。 例如，下面的布局在绑定类中为 user、 index 变量生成 setter 和 getter 方法:
``` xml
<!--activity_user.xml-->
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:bind="http://schemas.android.com/tools">
    <data>
        <import type="com.example.databindingdemo.bean.UserInfo" />
        ...
        <variable
            name="user"
            type="UserInfo" />

        <variable
            name="index"
            type="int" />
     ...
    </data>
    ...
</layout>
```
在获取了绑定类对象的地方可以通过 setter 方法设置数据，通过 getter 方法获取数据：
``` java
public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        ...
        binding.setUser(info);
        binding.setIndex(1);
        ...
    }
}
```
## ViewStub的使用
ViewStub 与普通 View 不同，它开始时是一个不可见的 View。 当设置可见或者调用 inflate() 方法时，它们会在布局中通过 inflate 另一个布局来替换自己。

因为 ViewStub 中的布局实际上在 View 层次结构中并没有加载，所以在绑定对象中也不能直接创建 ViewStub 中的布局对象，必须要在使用的时候再创建，所以绑定类中使用 ViewStubProxy 对象取代了 ViewStub，你可以使用它来访问 ViewStub，当 ViewStub 被创建和加载后你可以通过它来访问具体的布局结构。以下为布局：
``` xml
<!--activity_binding.xml-->
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    ...
        <ViewStub
            android:id="@+id/vs_bar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout="@layout/layout_bar" />
        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:onClick="@{()->activity.showViewStub()}"
            android:text="@string/show_stub" />
    ...
</layout>

<!--layout_bar.xml-->
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
        <variable name="resId" type="int" />
        <variable name="name" type="String" />
    </data>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
        <ImageView
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_margin="10dp"
            android:src="@drawable/default_mini_avatar"
            app:image="@{resId}" />
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:layout_margin="10dp"
            android:gravity="center"
            android:text="@{name, default=@string/default_name}"
            android:textSize="20sp" />
    </LinearLayout>
</layout>
```
这里在布局中声明了一个 ViewStub，并为它设置 layout_bar.xml 布局，同时声明了一个 Button 并为它设置了点击事件（调用 showViewStub() 方法）。

当你想使用 ViewStub 中的布局时，你需要获取到里面的绑定类对象，你可以向 ViewStubProxy 设置一个 OnInflateListener 监听器，然后在监听器回调中获取绑定类。比如：
``` java
public class BindingClassActivity extends AppCompatActivity {
    private ActivityBindingBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_binding);
        binding.vsBar.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                LayoutBarBinding vsBarBinding = DataBindingUtil.bind(inflated);
                vsBarBinding.setName("木易");
                vsBarBinding.setResId(R.drawable.head);
            }
        });
       ...
    }
    ...
    public void showViewStub() {
        ViewStub viewStub = binding.vsBar.getViewStub();
        if (viewStub != null) {
            viewStub.inflate();
        }
    }
    ...
}
```
可以看到 showViewStub() 方法中调用 ViewStub 的 inflate() 方法，当 Button 被点击时就会触发布局的加载，加载完成后会触发 OnInflateListener 的回调，然后在回调方法中通过 `DataBindingUtil.bind(inflated)`获取到了 ViewStub 中的布局绑定类，继而进行数据绑定。
## 立即绑定
当变量或 observable 对象数据发生变化时，数据绑定将在 View 的下一帧刷新之前更改。 但是，有时必须立即执行数据绑定。 若要强制执行，可以使用 executePendingBindings ()方法。一般情况不需要这么做。
## 动态变量
有时候你的布局文件是动态的，比如在一个 RecyclerView 中展现一系列新闻内容，有些内容是带图片的，有些是纯文本，这时我们可以采用多个 item 布局文件来呈现不一样的 UI 效果。比如：
``` java
public class NewsAdapter extends RecyclerView.Adapter {
    ...
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ViewDataBinding binding;
        if (viewType == VIEW_TYPE_TEXT) {
            binding = LayoutNewsItemTextBinding.inflate(inflater, viewGroup, false);
        } else {
            binding = LayoutNewsItemPictureBinding.inflate(inflater, viewGroup, false);
        }
        return new NewsViewHolder(binding.getRoot(), binding);
    }
    ...
}
```
这里通过类型判断，分别使用了 `layout_news_item_text.xml` 和 `layout_news_item_picture.xml` 布局文件。这两个布局文件 UI 展现不一样，但需要的数据类型都是 NewsInfo：
``` xml
<!--layout_news_item_picture.xml-->
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
        <variable
            name="info"
            type="com.example.databindingdemo.bean.NewsInfo" />
    </data>
    ...
</layout>

<!--layout_news_item_text.xml-->
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
        <variable
            name="info"
            type="com.example.databindingdemo.bean.NewsInfo" />
    </data>
    ...
</layout>
```
这种情况下我们在 RecyclerView.Adapter 的 onBindViewHolder() 方法中就无法准确的知道绑定类类型，但是我们任然要为其绑定数据，我们可以这样做：
``` java
public class NewsAdapter extends RecyclerView.Adapter {
    private List<NewsInfo> data = new ArrayList<>();
    ...
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        NewsViewHolder holder = (NewsViewHolder) viewHolder;
        ViewDataBinding binding = holder.binding;
        binding.setVariable(BR.info, data.get(position));
    }
    ...
}
```
在这里我们并没有获取具体的绑定类，而是获取了 ViewDataBinding 类，它是一个抽象类，是所有绑定类的父类。它提供了一个 `setVariable(int variableId, Object value)` 方法（第一个参数为布局中声明的绑定变量 ID，第二个参数为要绑定的数据），通过这个方法我们可以动态的为布局中的变量绑定相应的数据。
>BR 是 Data Binding 自动生成的资源 ID 文件，它包含所有的数据绑定变量的 ID，类似于 Android 的 R 文件。在上面例子中，RB.info 是布局中 info 变量的 ID。
## 后台线程
你可以在后台线程中更改数据，只要它不是一个集合。Data Binding 会在计算时将每个变量/字段在各个线程中做一份数据拷贝，以避免同步问题。
## 自定义绑定类名称
默认情况下 Data Binding 将根据布局文件的名称生成绑定类，以大写字母开头，删除下划线，驼峰格式命名，并添加 Binding 后缀。该类放在模块包下的 databinding 包中。例如，布局文件 layout_custom.xml 生成 LayoutCustomBinding类，放在 com.example.databindingdemo.databinding包中。

通过调整 data 标签的 class 属性，可以重命名绑定类或将绑定类放在不同的包中。例如，以下布局在当前模块的 databinding 包中生成绑定类 MyCustom：
``` xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data class="MyCustom">
    </data>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />
    </LinearLayout>
</layout>
```
效果图：![image.png](https://upload-images.jianshu.io/upload_images/4095450-d2f5757ec163357a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
你可以通过在类名前加一个句点来使生成绑定类在模块包中：
``` xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data class=".MyCustom">
    </data>
    ...
</layout>
```
效果图：![image.png](https://upload-images.jianshu.io/upload_images/4095450-21ff0ba86b6972a9.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
你还可以在类名前使用完整的包名。下面的示例在 com.custom 包中创建 MyCustom 绑定类:
``` xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data class="com.custom.MyCustom">
    </data>
    ...
</layout>
```
效果图：![image.png](https://upload-images.jianshu.io/upload_images/4095450-6b596541899ba2a5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
