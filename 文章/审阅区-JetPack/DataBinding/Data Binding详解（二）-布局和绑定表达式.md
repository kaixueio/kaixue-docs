# Data Binding 详解（二）-布局和绑定表达式
>作者：木易  
成长向来就只是自己一个人的战争。  
[文章配套的 Demo](https://github.com/muyi-yang/DataBindingDemo)：https://github.com/muyi-yang/DataBindingDemo

[TOC]

### 支持的表达式
在xml中支持很多表达式和关键字：
Mathematical + - / * %
String concatenation +
Logical && ||
Binary & | ^
Unary + - ! ~
Shift >> >>> <<
Comparison == > < >= <=
instanceof
Grouping ()
Literals - character, String, numeric, null
Cast 
Method calls 
Field access 
Array access []
Ternary operator ?:
例如：
``` java
android:text="@{String.valueOf(index + 1)}"
android:visibility="@{age > 13 ? View.GONE : View.VISIBLE}"
android:transitionName='@{"image_" + id}'
```
看上去支持大部分表达式，但是在写起来往往遇到问题，编译都不过，这是因为在xml中有很多符号是不能直接使用的，需要转义一下，比如小于号`<`要写成`&lt;`，转义知识请自行学习。例如：
``` xml
<!-- android:visibility="@{age < 10 ? View.GONE : View.VISIBLE}" 此句编译不过 -->
android:visibility="@{age &lt; 10 ? View.GONE : View.VISIBLE}"
```
如果这段代码你直接拷贝到你项目中，可能依然编译不过，这是因为你没有导入View包，在xml中写表达式的时候特别要注意这一点，因为java文件写惯了，导包都是自动的，而在xml中则需要手动导入，正确示例：
``` xml
...
<import type="android.view.View" />
...
android:visibility="@{age &lt; 10 ? View.GONE : View.VISIBLE}"
...
```
但是也不是所有包都需要自己导，基本数据类型，String，以及Data Binding自己本身提供的Observable相关的类编译器会自动导入。
### 空合并运算符
前面的运算符在java代码中应都用过，空合并运算符可能比较陌生，空合并运算符(? ?) 如果左操作数不为 null，则选择左操作数; 如果为 null，则选择右操作数。
``` java
android:text="@{user.remark ?? user.name}"
```
这在功能上等同于:
``` java
android:text="@{user.remark != null ? user.remark : user.name}"
```
### 表达式中使用集合
为了方便使用，可以使用[]运算符访问常用集合，如数组、List、和Map。
``` java
public class UserInfo {
    ...
    public String[] tripMode={"公交车","地铁","开车"};
    public List<String> colleague = new ArrayList<>();
    public Map<String, String> task = new HashMap<>();

    public UserInfo(){
        colleague.add("张三");
        colleague.add("李四");
        colleague.add("王五");

        task.put("monday","整理思路及确定整体框架");
        task.put("tuesday","开始进行编写");
        task.put("wednesday","检测及修订");
    }
}
```
``` xml
    <data>
     ...
        <variable
            name="index"
            type="int" />
    </data>
    ...
        <TextView
            ...
            android:text="@{@string/trip(user.tripMode[index]), default=上班出行方式}"
            ... />

        <TextView
            ...
            android:text="@{@string/colleague(user.colleague[index]), default=身边的同事}"
            ... />

        <TextView
            ...
            android:text="@{@string/task(user.task[`monday`]), default=今天任务}"
            .../>
```
``` java
public class UserActivity extends AppCompatActivity {
    ...
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        binding.setIndex(1);
        ...
    }
    ...
}
```
Map的使用也可以直接引用key值，写成这样：
``` xml
<TextView
            ...
            android:text="@{@string/task(user.task.monday), default=今天任务}"
            .../>
```
### 表达式中使用字符串
在xml中避免不了直接使用字符串，你可以使用单引号来包围属性值，这允许你在表达式中使用双引号，如下面的例子所示:
``` java
            android:text='@{@string/task(user.task["monday"]), default=今天任务}'
```
还可以使用双引号来包围属性值， 这个时候字符串文本就需要被反引号包围`（反引号就是键盘的第二排第一个这个键值）`:
``` java
            android:text="@{@string/task(user.task[`monday`]), default=今天任务}"
```
### 表达式中引用资源
使用正常的表达式来访问Resources也是可行的：
``` xml
    <TextView
            ...
            android:padding="@{@dimen/view_margin}"
            android:text="@{@string/name(user.name), default=姓名}"
            ... />

    //strings.xml
    <string name="name">姓名：%1$s</string>
```
这里引用了dimen和string，string还可以带格式化参数，当然也可以引用复数，但是一般情况下我们用不到，因为中文没有这个需求。比如：
``` java
android:text="@{@plurals/banana(bananaCount)}"
```
有些资源的引用需要明确指明类型，如下表所示:

| 类型 |正常引用 | 表达式引用 |
 |:-|:-|:-| 
| String[] | @array | @stringArray |
| int[] | @array | @intArray | 
| TypedArray | @array | @typedArray |
 | Animator | @animator | @animator | 
| StateListAnimator | @animator | @stateListAnimator |
 | color int | @color | @color |
 | ColorStateList | @color | @colorStateList |
 
## 事件绑定
Data Binding允许你编写表达式来处理view分派的事件。事件属性名字取决于监听器方法名字。例如View.OnClickListener有onClick()方法，View.OnLongClickListener有onLongClick()的方法，因此事件的属性是`android:onLongClick`，`android:onClick`。
对于 click 事件，为了避免多种click事件的冲突，Google也定义了一些专门的事件处理，比如：
| Class | 设置监听器的方法 | 绑定时的属性 |
 |:-|:-|:-| 
|SearchView|	setOnSearchClickListener(View.OnClickListener)	|android:onSearchClick|
|ZoomControls|	setOnZoomInClickListener(View.OnClickListener)|	android:onZoomIn|
|ZoomControls|	setOnZoomOutClickListener(View.OnClickListener)|	android:onZoomOut|
除了它们，Google还定义了其他一些常用的绑定事件的属性，这些可以阅读[Google官方Data Binding的API](https://developer.android.google.cn/reference/android/databinding/package-summary)。
你可以使用以下机制来处理事件绑定：
### 方法引用
可以引用绑定对象中已经定义好的特定规则的click方法：
``` java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.tvInfo.setText("我是使用Data Binding的Demo");
        binding.setActivity(this);
    }
    //被绑定的方法，注意参数
    public void userClick(View view){
        startActivity(new Intent(this, UserActivity.class));
    }
}
```
``` xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="activity"
            type="com.example.databindingdemo.MainActivity" />
    </data>
    ...
    <Button
           ...
            android:onClick="@{activity::userClick}"
           ... />
    ...
</layout>
```
>注意：被绑定的方法有一个View参数，这个参数是必须的，因为在方法引用中，方法的参数和返回值必须与事件监听器的参数和返回值相匹配，如果参数或者返回值不匹配在编译时就会报错。

当表达式计算为方法引用时，数据绑定在监听器中包装方法引用和所有者对象，并在目标视图上设置该监听器（监听器对象是在数据绑定的时候创建的），如果绑定的对象为空，这个监听器则不会创建。它的优点是找不到符合规定的方法则编译不过。
### 监听器绑定
监听器绑定是在xml中写一个lambda表达式，表达式是在事件发生时被求值。它类似于方法引用，但允许您运行任意的数据绑定表达式。**`这个特性是在 Android Gradle Plugin for Gradle version 2.0或更高版本中才支持。`**
``` java
public class MainActivity extends AppCompatActivity {
    ...
    public void startList(){
        startActivity(new Intent(this, ListActivity.class));
    }
}
```
把click事件绑定到startList()方法：
``` xml
<Button
            ...
            android:onClick="@{()->activity.startList()}"
            ... />
```
在方法引用中，方法的参数和返回值必须与事件监听器的参数匹配。 在监听器绑定中，则只要返回值与监听器的预期返回值匹配即可。 例如:
``` java
public class MainActivity extends AppCompatActivity {
    ...
    public boolean listLongClick() {
        //长按操作
        return true;
    }
}
```
``` xml
<Button
            ...
            android:onLongClick="@{()->activity.listLongClick()}"
            ... />
```
监听器绑定在编译时会自动创建必要的监听器并为它注册事件（click监听器是一开始就创建好了，等到触发时才会判断被绑定的对象是否为空，为空则不执行任何操作）。 当视图触发事件时，数据绑定计算给定的表达式。在计算这些表达式时可以获得数据绑定的 null 安全和线程安全性。

有些时候click方法可能需要带一些必要的参数，比如：
``` java
public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        binding.setActivity(this);
    }

    public void showSign(View v, UserInfo info) {
        Toast.makeText(v.getContext(), info.sign, Toast.LENGTH_LONG).show();
    }
}
```
``` xml
    <data>
        <import type="com.example.databindingdemo.bean.UserInfo" />
        <variable  name="user" type="UserInfo" />
        ...
        <variable
            name="activity"
            type="com.example.databindingdemo.UserActivity"/>
    </data>
    ...
    <Button
    ...
    android:onClick="@{(view)->activity.showSign(view, user)}"
    .../>
```
在上面例子中，showSign方法需要一个View和UserInfo对象，在xml中这样使用`@{(view)->activity.showSign(view, user)}`，view是lambada表达式中获取的，user是上面声明需绑定的对象。如果绑定方法是多个参数，或者监听器事件是带返回值的都是以此类推，保证参数和返回值匹配即可。
如果需要使用带有谓词的表达式(例如，三元表达式) ，可以使用监听器相匹配的返回值类型作为符号，比如onCLick属性使用void，onLongClick属性使用boolean。
``` java
    android:onClick="@{(view)->view.isEnabled()?activity.showSign(view, user):void}"
    android:onLongClick="@{(v)->v.isEnabled()?activity.showSign(user):false}"
```
>注意：监听器表达式非常强大，可以使您的代码简化，容易阅读。 另一方面，如果包含复杂表达式的监听器也会使您的布局难以阅读和维护。 xml中表达式应该尽量的简单，您应该在从监听器表达式调用的回调方法中实现相对复杂的业务逻辑。

## xml中的一些关键标签
Data Binding库提供了Imports, variables 以及 includes 标签，Imports使得可以在布局文件中引用类。variables允许你声明可在绑定表达式中使用的属性。 includes可以让你重用布局。
### Imports
Imports可以让你在布局中使用类，像前面文章提到的导入View类，导入 View 类允许您从绑定表达式引用它的常量 VISIBLE 和 GONE。
``` xml
...
<data>
    <import type="android.view.View"/>
</data>
...
<TextView
   ...
  android:visibility="@{age > 13 ? View.GONE : View.VISIBLE}"/>
```
当存在类名冲突时，还可以将其中一个类重命名为别名。 下面的示例将 com.example.databindingdemo.bean 包中的 View 类重命名为 Vista，这样可以使用 Vista 引用 com.example.databindingdemo.bean.View 和 View 来引用系统中的 android.View.View。
``` xml
    <import type="android.view.View" />
    <import
        alias="Vista"
        type="com.example.databindingdemo.bean.View" />
    ...
    <ImageView
            ...
            android:visibility="@{Vista.isShow?View.VISIBLE:View.GONE}"
            ... />
```

导入的类型可以用作变量和表达式中的类型引用。 下面的示例显示了用作变量类型的 UserInfo:
``` xml
<import type="com.example.databindingdemo.bean.UserInfo" />
<variable name="user" type="UserInfo" />
```
它等同于：
``` xml
<variable name="user" type="com.example.databindingdemo.bean.UserInfo" />
```
也可以导入类来做类型转换，或者导入工具类来引用静态方法：
``` xml
<data>
    <import type="com.example.MyStringUtils"/>
    <import name="user" type="com.example.User"/>
</data>
...
<TextView
   android:text="@{((User)(user.connection)).lastName}"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
…
<TextView
   android:text="@{MyStringUtils.capitalize(user.lastName)}"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```
### variables
可以在data标签内部使用多个variables。 每个variables描述一个属性，该属性可以在布局文件中的绑定表达式中使用。 下面的示例声明了UserInfo、Drawable和String变量:
``` xml
<data>
    <variable name="user" type="com.example.User"/>
    <variable name="image" type="android.graphics.drawable.Drawable"/>
    <variable name="note" type="String"/>
</data>
```
在自动生成的 xxxBinding 类中具有每个变量的 setter 和 getter 方法，这些变量在调用 setter 方法赋值之前都有默认值，对象为null，int为0，boolean为false等等。
同时也会生成一个名为 context 的特殊变量，以便根据需要在绑定表达式中使用。 context 的值是根 View 的 getContext ()方法中的 Context 对象。 以下为直接通过context变量获取程序包名：
``` xml
    <TextView
            ...
            android:text="@{context.packageName}"
            ... 
           />
```
 但context变量可以被具有该名称的显式变量声明所覆盖，比如声明了一个String类型的context变量：
``` xml
    <variable name="context" type="String"/>
```
这个时候就不能直接使用`@{context.packageName}`了，因为context已经被覆盖为String类型。
>注意：当设备针对横竖屏有不同的布局文件时，这些布局文件之间不能有冲突的变量定义，必须保证不同配置的布局文件中的变量是一致的。
### includes
include标签和普通布局中使用的include是一样的功能，都是导入一个已经存在的布局文件，来实现布局的重用。只不过在Data Binding中它多了绑定数据的功能。下面展示了来自layout_avatar.xml布局文件：
``` xml
<!--layout_avatar.xml-->
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
        <variable
            name="resId"
            type="int" />
    </data>
    <ImageView
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_margin="10dp"
        android:src="@drawable/default_mini_avatar"
        app:image="@{resId}"
        app:layout_constraintRight_toRightOf="parent" />
</layout>
```
这个布局很简单，里面只有一个ImageView，里面声明了一个表达式`app:image="@{resId}"`（这是一个自定义的适配器，自定义适配器后面会讲到，这里不深究），它需要一个resId的变量，接下来展示在activity_user.xml布局中的使用：
``` xml
    <data>

        <import type="com.example.databindingdemo.bean.UserInfo" />
        <variable name="user"  type="UserInfo" />
        ...
    </data>
    ...
    <include layout="@layout/layout_avatar"
            bind:resId="@{user.avatarId}"/>
    ...
```
在布局中 include 了 layout_avatar.xml 文件，并声明了一个属性绑定了表达式`bind:resId="@{user.avatarId}"`，这个属性就是 layout_avatar.xml 文件中的 resId 变量，它的规则就是被 include 的布局变量名就是这里绑定的属性名，遵循这个规则，就可以为 layout_avatar.xml 布局中的 resId 变量赋值。
>注意：Data Binding 不支持在 merge 标签中直接 include 布局。
