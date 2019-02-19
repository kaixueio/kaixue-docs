# Data Binding 详解（一）-从零开始
>作者：木易  
成长向来就只是自己一个人的战争。  
[文章配套的 Demo](https://github.com/muyi-yang/DataBindingDemo)：https://github.com/muyi-yang/DataBindingDemo

[TOC]

## DataBinding介绍
2015年的Google IO大会上，Android 团队发布了一个数据绑定框架（Data Binding Library），它是为了解决数据和UI的绑定问题，同时也是对MVVM 模型的一个实践和引领。MVVM模型不了解的请自行补上。
### 优点
* 在XML中绑定数据，XML变成UI的唯一真实来源
* 去掉Activities & Fragments内的大部分UI代码（setOnClickListener, setText, findViewById)
* 数据变化可自动刷新UI，保证UI操作都在主线程运行
### 缺点
* IDE支持还不完善，在XML中代码提示、表达式验证都有很大缺失
* 报错信息不明显，初学者排错难度较大
* 重构支持较差，数据绑定写在XML中，丧失面向对象特性
## 开启DataBinding
### Gradle配置
要配置应用程序使用Data Binding，需在应用程序模块中添加 dataBinding 元素到 build.gradle 文件中，
此配置将会在你的项目里添加必需提供的Data Binding插件以及编译配置依赖。
``` gradle
android {
    ....
    dataBinding {
        enabled = true    
    }    
}
```
>注意：你必须为依赖于使用 Data Binding 的库（aar）的应用程序在 gradle 中增加开启 Data Binding 配置，即使这个 module 没有直接使用 Data Binding。
例如： A module 依赖 B module，B module 又依赖 C module，但只有 C module 中使用了 Data Binding ，这个时候 A B C 三个 module 都必须在 gradle 中增加以上配置，否则编译不过。

Android Gradle 插件在版本3.1.0-alpha06包含一个新的 Data Binding 编译器，用于生成 Binding 类。它的主要改变有：
1. 新的编译器是增量编译 Binding 类，这在大多数情况下加快了编译速度。
2. library模块的 Binding 类会被编译并打包到AAR文件中。 依赖这些库的应用程序不再需要重新生成 Binding 类。
3. 老版本在编译出错时，经常会出现一些与真真错误不符合的提示，这个问题在新版本中已经做了修改。
4. 绑定适配器（binding adapters）只影响自己 module 中的代码和 module 的使用者，它不能更改 module 依赖库中的适配器的行为。（ 不懂，没关系，后面我们会详细讲解适配器）

要启用新的 Data Binding 编译器，请在 gradle.properties 文件中添加以下选项:
``` gradle
android.databinding.enableV2=true
```
或者在 gradle 命令中通过添加以下参数来启用新的编译器:
``` gradle
-Pandroid.databinding.enableV2=true
```
但是这个编译器在 Android Studio 3.2 版本中已经是默认启用的状态，所以你如果是 Android Studio 3.2 版本及以上版本可以不用关心这个特征。
>注明：Data Binding提供了兼容，它可以支持Android 4.0（API 14）及以上系统。Data Binding插件需要Android Studio 1.3.0及以上版本，Gradle 1.5.0及以上版本。本文章及例子是基于Android Studio 3.3.1版本，Gradle 4.10.1 版本。
### XML写法
``` xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <android.support.constraint.ConstraintLayout xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">
        <TextView
            android:id="@+id/tv_info"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Hello World!"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent" />
    </android.support.constraint.ConstraintLayout>
</layout>
```
Data Binding layout文件有点不同的是：起始根标签是layout，layout标签里面的内容和普通的xml没有区别。

Data Binding 插件会检索所有布局文件，会把根标签为layout的xml编译出一个继承自ViewDataBinding 的类（build 目录下）。其命名规则是根据xml文件名来的，比如activity_main.xml，那么生成的类就是 ActivityMainBinding。ActivityMainBinding类是一个抽象类，它的实现类是ActivityMainBindingImpl（也在build目录下），它的作用就是实现了Data Binding的一系列功能和特征。什么功能和特征？？？别急！！！我们先看如何使用运用了Data Binding的布局。
## 布局使用
前面说了Data Binding的功能和特征在自动生产的ActivityMainBinding类中，那么我们在Activity中就不能直接调用setContentView（R.layout.activity_main）设置布局了，我们需要获取到ActivityMainBinding对象。
``` java
private ActivityMainBinding binding;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
}
```
在Activity中通过DataBindingUtil工具类的setContentView方法设置布局到Activity当中，并返回ActivityMainBinding对象。有了ActivityMainBinding对象，我们就可以去体验Data Binding的魅力了。

如果你是在代码运行时创建View，想使用Data Binding，你可以通过如下方式获取View：
``` java
MainActivityBinding binding = MainActivityBinding.inflate(getLayoutInflater());
```
如果你是在Fragment、ListView或者RecyclerView的适配器中使用Data Binding，你可以使用DataBindingUtil 类的 flatflate ()方法，如下面的代码示例所示:
``` java
ListItemBinding binding = ListItemBinding.inflate(layoutInflater, viewGroup, false);
//或者
ListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false);
```
## 获取View对象
使用了Data Binding我们不在需要findViewById()获取对象了，因为Data Binding编译插件会检索xml文件中的控件，把已经声明了id的View自动创建对象到ActivityMainBinding中，直接获取使用即可。对象的命名规则是根据id名来的，比如 android:id="@+id/tv_info"，自动生成后的对象名为 tvInfo（去除下划线，并以驼峰格式命名）。
``` java
private ActivityMainBinding binding;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    binding.tvInfo.setText("我是使用Data Binding的Demo");
}
```
通过ActivityMainBinding获取布局中TextView的对象tvInfo并设置新值。
## 数据绑定
要进行数据绑定，首先要在xml中声明绑定变量，声明变量需要使用data标签以及variable标签。data标签内用来做一些声明，比如声明变量，导入数据类型等。
``` xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <import type="com.example.databindingdemo.bean.UserInfo" />
        <variable
            name="user"
            type="UserInfo" />
    </data>
    .....
</layout>
```
这是一个用户信息界面的数据绑定，import标签是导入一个数据类型，variable标签为声明一个类型为UserInfo的变量user。
### 绑定属性
``` java
public class UserInfo {
    public String name;
    public int age;
    public int sex;
    public String sign;
}
```
完整布局：
``` xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <import type="com.example.databindingdemo.bean.UserInfo" />
        <variable
            name="user"
            type="UserInfo" />
    </data>
    <android.support.constraint.ConstraintLayout xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <TextView
            android:id="@+id/tv_name"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_margin="5dp"
            android:text="@{@string/name(user.name), default=姓名}"
            android:textSize="18sp"
            app:layout_constraintLeft_toLeftOf="parent" />
        
        <TextView
            android:id="@+id/tv_age"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_margin="5dp"
            android:text="@{@string/age(String.valueOf(user.age)), default=年龄}"
            android:textSize="18sp"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/tv_name" />

        <TextView
            android:id="@+id/tv_sex"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_margin="5dp"
            android:text="@{@string/sex(user.sex == 1?@string/sex_man:@string/sex_woman), default=性别}"
            android:textSize="18sp"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/tv_age" />

        <TextView
            android:id="@+id/tv_sign"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_margin="5dp"
            android:text="@{user.sign, default=个性签名}"
            android:textSize="18sp"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/tv_sex" />
    </android.support.constraint.ConstraintLayout>
</layout>
```
在layout中的表达式使用“@{}”语法在属性中编写，这里是为TextView设置值：
``` xml
<TextView
     android:id="@+id/tv_sign"
    ...
     android:text="@{user.sign, default=个性签名}"
    ...
 />
```
这里的“@{}”中为取值表达式，从user对象中获取个性签名属性绑定到了TextView中，`default`属性是用来设置布局预览时的值，它是可选字段，也可以直接写成`android:text="@{user.sign}"`，只不过这样写在布局预览时就不会显示内容。

以下为预览效果：![image.png](https://upload-images.jianshu.io/upload_images/4095450-9b9a55ce13bed062.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
### 绑定方法
除了绑定对象属性外，还可以绑定对象的方法，比如：
``` java
public class UserInfo {
    private String name;
    ...
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    ...
}
```
有很多时候我们的对象属性是私有的，我们提供了 Getter 和 Setter 方法，在绑定的时候我们可以这样写`android:text="@{user.name}"`， 为什么不是`android:text="@{user.getName()}"`呢？这是因为Data Binding内部做了处理，它会把getName()方法解析为 name()，所以我们可以直接使用表达式`@{user.name}`。
**`只要项目中开启了Data Binding功能，所有的 getxxx 方法都遵循这个规则。`**
表达式可以使用以下格式引用类中的属性，对于类中的字段、 getter方法和 ObservableField 对象都是一样的:
``` java
android:text="@{user.name}"
```
编译器生成的代码也会自动检查空值并避免空指针异常。 例如，在表达式`@{ user.name }`中，如果 user 为 null，则 user.name 的默认值为 null。 如果表达式 `@{ user.age }`，其中 age 类型为 int，那么数据绑定使用默认值0，其他数据类型类似。
### 设置数据
布局写好后，我们只需在代码中绑定对应数据即可：
``` java
    private ActivityUserBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);

        UserInfo info = new UserInfo();
        info.name = "木易";
        info.age = 28;
        info.sex = 1;
        info.sign = "问君能有几多愁，恰似一杯二锅头";
        binding.setUser(info);
    }
```
获取ActivityUserBinding对象，调用setUser方法绑定数据，setUser方法是自动编译生成的，命名规范是根据xml中的`<variable name="user" type="UserInfo" />`name字段而定，xml中声明的所有变量都会自动生成Getter 和 Setter 方法。当调用setUser方法时，框架会自动触发所有绑定了UserInfo对象的View刷新，使UserInfo中的信息显示在界面上。

至此 Data Binding 算上用上了，但仅仅只是打了个照面，好比能运行“hello word”了。这只是一个开始，接下来的几篇文章才是亲密接触 Data Binding。