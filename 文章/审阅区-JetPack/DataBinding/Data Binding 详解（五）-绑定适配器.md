# Data Binding 详解（五）-绑定适配器
>作者：木易  
知是行之始，行是知之成。  
[文章配套的 Demo](https://github.com/muyi-yang/DataBindingDemo)：https://github.com/muyi-yang/DataBindingDemo

绑定适配器就是把布局中的**属性表达式**转换成**对应的方法调用**以**设置值**。 一个例子是设置属性值，比如调用 `setText()` 方法。 或者是设置事件侦听器，比如调用 `setOnClickListener()` 方法。还允许你指定设置值的调用方法，提供你自己的绑定逻辑。
## 设置属性值
当在布局中使用属性绑定表达式时，每当绑定的变量值发生更改时，生成的绑定类必须使用绑定表达式调用 View 上的 setter 方法。你可以允许 Data Binding 自动确定方法、显式声明方法或提供自定义逻辑来选择方法。
### 自动选择方法
自动选择方法就是通过**属性名**和**接受值的类型**进行自动尝试查找**接受值兼容类型**作为参数，**属性名**对应的 setter 方法，然后调用此 setter 方法设置**接受值**。比如一个常见的例子，为 TextView 设置值：
``` xml
<!--activity_user.xml-->
    ...
       <TextView
            android:id="@+id/tv_name"
            ...
            android:text="@{@string/name(user.name), default=@string/default_name}"
            .../>
    ...
```
上面有一个 `android:text="@{@string/name(user.name), default=@string/default_name}"` 表达式，它接受的值是 `String` 类型，属性名是 `text`，那么 Data Binding 框架就会查找接受 `String` 类型参数的方法 `setText(String text)`。如果表达式返回的是 `int` 类型，将会查找接受 `int` 类型参数的方法 `setText(int resId)`，如果找不到相应参数和相应属性对应的方法则会编译出错。以下为 `int` 类型的 `setText` 示例：
``` xml
<!--activity_user.xml-->
    ...
    <variable
            name="stringResId"
            type="int" />
    ...
    <TextView
            ...
            android:text="@{stringResId}"
            .../>
    ...
```
``` java
public class UserActivity extends AppCompatActivity {
    ...
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user);
        ...
        binding.setStringResId(R.string.app_name);
    }
}
```
有些时候绑定的属性名不在 View 标准属性中，这样的绑定表达式任然有效，**Data Binding 允许你为任何 setter 方法创建绑定属性。**比如为 `RecyclerView` 类它有一个 `setOnScrollListener` 方法，但没有 `onScrollListener` 属性，我们任然可以设置一个绑定表达式：
``` xml
    <!--activity_list.xml-->
    ...
    <android.support.v7.widget.RecyclerView
            ...
            app:onScrollListener="@{activity.scrollListener}"
            ... />
    ...
```
它的规则是**根据属性名寻找对应的 setter 方法**，然后检测**绑定表达式返回类型相兼容的参数类型**的方法作为设置器。
>注意：只要项目中开启了 Data Binding，所有 View 的所有 setter 方法都将遵循这个规则。可以说只要有 setter 方法的地方就可以写绑定表达式。
### 自定义指定方法名称
有些 View 属性具有不按属性名匹配的 setter 方法，在这种情况下你可以使用 `@BindingMethods` 注解来关联对应的 setter 方法。注解是写在一个类上面，它可以包含多个 `@BindingMethod` 注解，每个注解对应着一个属性的关联方法。这些注解可以写在任何一个类上面，但是不推荐你任意写，最好做到分门别类，这样便于后期维护。在下面的示例中，展示了 `ImageView` 的 `android: tint` 属性与 `setImageTintList(ColorStateList)` 方法相关联，而不是 `setTint()` 方法:
``` java
@BindingMethods({@BindingMethod(type = android.widget.ImageView.class, attribute = "android:tint",
        method = "setImageTintList")})
public class BindAdapter {
    ...
}
```
在布局中的使用：
``` xml
<!--activity_adapter.xml-->
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
        <variable
            name="tintColor"
            type="android.content.res.ColorStateList" />
    </data>
    ...
        <ImageView
            ...
            android:tint="@{tintColor, default=@color/colorPrimary}"
            ... />
    ...
</layout>
```
大多数情况下，你不需要写这样的注解，因为大多数 View 的属性都有相匹配的 setter 方法，它会以**自动选择方法**的方式找到。其实在你的项目中连上面的例子提到的 `android: tint` 属性你都没必要写注解，因为 Data Binding 框架已经帮你预置了很多适配器。其中就包括 `android: tint` 的注解，我这里写出来只是为了一个演示，当你手动写了之后它会覆盖 Data Binding 预置的。

你可以看看 Data Binding 源码，其实大部分重要或常用的属性都已经写好了各种适配器，等待着你的使用。如果你懒得看源码，你也可以直接在布局中写你想绑定的属性，如果编译出错则说明没有预置这个适配器，多数情况是可以直接编过的。
### 提供自定义逻辑
有些属性需要自定义绑定逻辑。 例如，ImageView 的 `android:src` 属性，它没有相匹配的 setter 方法，但它有 `setImagexxx` 方法。 我们可以使用带有 `@BindingAdapte` 注解的静态绑定适配器方法来达到自定义调用 setter 方法。比如下面例子，我想在布局中动态为 ImageView 设置 resId：
``` java
public class BindAdapter {
    @BindingAdapter("app:image")
    public static void bindImage(ImageView view, int resId) {
        view.setImageResource(resId);
    }
}
```
这个自定义方法名可以任意取，方法参数类型很重要。 第一个参数确定与该属性关联的 View 的类型，也就是说为 ImageView 声明了一个 `app:image` 属性。 第二个参数确定给定属性的绑定表达式中接受的类型，也就是说 `app:image` 属性接受的数据类型是 `int` 型。以下为布局中的使用：
``` xml
<!--layout_avatar.xml-->
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <data>
        <variable name="resId" type="int" />
    </data>
    <ImageView
        ...
        app:image="@{resId}"
        ... />
</layout>
```
这样一个自定义逻辑的绑定方法就写好了，它的一个好处就是，你可以在方法中自定义任何逻辑，当有一些重复繁琐的操作时，很合适写一个自定义逻辑绑定适配器。

--------------
还可以声明接受多个属性的适配器。如下面例子所示：
``` java
    @BindingAdapter({"app:image", "app:error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        RequestOptions options = new RequestOptions().error(error);
        Glide.with(view).load(url).apply(options).into(view);
    }
```
这是同时为一个 View 设置了两个属性的适配器，第一个参数是关联的 View，第二个参数是第一个属性的接受值，第三个参数是第二个属性的接受值。如果你声明的是两个属性以上的适配器，参数对应关系以此类推。以下为布局中使用这个适配器：
``` xml
    <!--activity_adapter.xml-->
    ...
    <variable  name="imgUrl" type="String" />
    ...
    <ImageView
            ...
            app:error="@{@drawable/error}"
            abc:image="@{imgUrl}"/>
    ...
```
>Data Binding 会忽略自定义命名空间进行适配器匹配，比如上面适配器方法中声明的属性是 `app:image`，而布局中却是使用的 `abc:image`，这是因为 Data Binding 忽略了命名空间，只取 `:` 后面的名字进行匹配，所以布局中的命名空间可以任意写。声明适配器方法的属性时也可以不写命名空间，比如 `@BindingAdapter({"app:image", "app:error"})` 可以写成 `@BindingAdapter({"image", "error"})`，它们的效果是相等的，感兴趣的同学可以尝试尝试，但我们推荐使用 `app:xxx` 这种规范格式。

上面的声明的适配器方法有一个特点是必须在布局中同时使用这些声明的属性，如果少一个就会编译出错，提示找不到对应的适配器方法。如果你想实现在布局中使用某一个属性也能正常使用这个适配器方法，你可以在适配器中增加 `requireAll`  标志并赋值为 `false`，比如：
``` java
    @BindingAdapter(value = {"image", "app:placeholder", "app:error"}, requireAll = false)
    public static void loadImage(ImageView view, String url, Drawable placeholder, Drawable error) {
        if (TextUtils.isEmpty(url)) {
            view.setImageDrawable(placeholder);
        } else {
            RequestOptions options = new RequestOptions().placeholder(placeholder).error(error);
            Glide.with(view).load(url).apply(options).into(view);
        }
    }
```
这样在布局中就无需同时把所有属性都写上绑定表达式，你可选择性的去使用这些属性，比如只想加载一张图片，不想设置占位图和错误图：
``` xml
    <ImageView
            android:layout_width="match_parent"
            android:layout_height="300dp"
            android:scaleType="centerCrop"
            app:image="@{imgUrl}" />
```
------------
有些时候，我们在为属性设置新值时需要获取到老值来做一些逻辑判断，这时候你的自定义适配器可以这样做：
``` java
    @BindingAdapter("app:imageUrl")
    public static void bindImage(ImageView view, String oldUrl, String newUrl) {
        if (oldUrl == null || !oldUrl.equals(newUrl)) {
            Glide.with(view).load(newUrl).into(view);
        }
    }
```
方法的第一个参数是属性相关联的 View，第二个参数是属性的旧值，第三个参数是属性的新值。当一个自定义适配器只有一个属性，但有三个参数，且第二个和第三个参数类型一致时就会采用这种新旧值的规则。这里是判断图片的 url 如果没有变化则不再重新加载，以下为布局中的使用：
``` xml
    <ImageView
            ...
            app:imageUrl="@{switchUrl}"
            .../>
```
在 Demo 中我故意延迟了一段时间进行两次地址切换，以便体验适配器效果：
``` java
public class AdapterActivity extends AppCompatActivity {
    ...
    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_adapter);
        ...
        binding.setSwitchUrl("https://s2.ax1x.com/2019/03/03/kLWJ3D.jpg");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.setSwitchUrl("https://s2.ax1x.com/2019/03/03/kLOdSA.jpg");
            }
        }, 2000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.setSwitchUrl("https://s2.ax1x.com/2019/03/03/kLOdSA.jpg");
            }
        }, 4000);
    }
}
```
-------------
有些监听器会存在多个回调方法，如果你只想使用其中某一个回调方法并处理一些事物时，你可以将其拆分为多个自定义监听器，并封装成自定义适配器进行使用。比如 `
View.OnAttachStateChangeListener` 有两个回调方法: `onViewAttachedToWindow(View)` 和 `onViewDetachedFromWindow(View)`，我们将它拆分成两个自定义监听器：
``` java
    @TargetApi(VERSION_CODES.HONEYCOMB_MR1)
    public interface OnViewDetachedFromWindow {
        void onViewDetachedFromWindow(View v);
    }

    @TargetApi(VERSION_CODES.HONEYCOMB_MR1)
    public interface OnViewAttachedToWindow {
        void onViewAttachedToWindow(View v);
    }
```
然后创建一个自定义适配器将两个监听器分别关联不同的属性：
``` java
    @BindingAdapter(value = {"android:onViewDetachedFromWindow", "android:onViewAttachedToWindow"}, requireAll = false)
    public static void setOnAttachStateChangeListener(View view,
            final OnViewDetachedFromWindow detach, final OnViewAttachedToWindow attach) {
        final OnAttachStateChangeListener newListener;
        if (detach == null && attach == null) {
            newListener = null;
        } else {
            newListener = new OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    if (attach != null) {
                        attach.onViewAttachedToWindow(v);
                    }
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    if (detach != null) {
                        detach.onViewDetachedFromWindow(v);
                    }
                }
            };
        }
        final OnAttachStateChangeListener oldListener = ListenerUtil.trackListener(view,
                newListener, R.id.onAttachStateChangeListener);
        if (oldListener != null) {
            view.removeOnAttachStateChangeListener(oldListener);
        }
        if (newListener != null) {
            view.addOnAttachStateChangeListener(newListener);
        }
    }
```
最后在布局中使用它：
``` xml
    <!--activity_adapter.xml-->
    <ImageView
            ...
            android:onViewAttachedToWindow="@{attachListener}"
            android:onViewDetachedFromWindow="@{detachListener}"
            ... />
```
上面的例子中，使用到 `ListenerUtil` 类，它是 Data Binding 提供的一个工具类，它帮助记录已设置的监听器，以便需要的时候可以获取到。比如上面的示例，在设置新监听器时移除以前的监听器。
>注意：上面示例 `View.OnAttachStateChangeListener` 相关的部分代码在 Demo 中找不到，这是因为我直接使用了 Data Binding 中已经预制的适配器。源码在 `android.databinding.adapters.ViewBindingAdapter` 中，学到这里我觉得带大家熟悉一下 Data Binding 中的 API 也很有必要，因为熟悉已有的 API 是熟练掌握 Data Binding 的其中一环，因为我们要避免重复造轮子。

## 对象转换
### 自动对象转换
在布局中写绑定表达式时，Data Binding 会根据表达式返回的对象类型自动选择设置属性值的 setter 方法。它会自动寻找参数类型与返回类型相兼容的方法，然后把对象类型进行自动转换。比如以下示例：
``` xml
    <TextView
            ...
            android:text="@{user.task[`monday`]}"
            ... />
```
表达式 `user.task[monday]` 返回一个 String 类型，它会自动转换为 `setText(CharSequence)` 方法中的参数类型，如果表达式返回的参数类型不明确，你可能需要在表达式中进行强制转换，比如这样 `android:text="@{(CharSequence)user.task[monday]}"`。
### 自定义转换
有些时候我们需要在特定类型中进行自定义转换，比如一个 View 的显示和隐藏需求，往往数据类型是 `Boolean`，但是 `android:visibility` 属性需要的是一个 `int` 常量。比如：
``` xml
        <!--activity_adapter.xml-->
        ...
        <variable name="isShow" type="boolean" />
        ...
        <ImageView
            ...
            android:visibility="@{isShow}"
            ... />
```
上面 `android:visibility` 属性中绑定的是一个 `Boolean` 类型，但是它需要的是 `int` 型，当出现这个中情况时 Data Binding 会尝试寻找转换器，当寻找不到时会编译出错。转换器可以使用带有 `@BindingConversion` 注解的静态方法实现，比如：
``` java
    @BindingConversion
    public static int convertBooleanToVisible(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }
```
方法的参数是 `Boolean` 类型，返回值却是 `int` 类型，这样就实现了从 `Boolean` 转换 `int` 了。
**但是**，要特别注意一点的是，转换器是全局的，它适用于整个项目，所以要谨慎使用，以防误写而不自知。以下为**一个反面例子**：
``` xml
        <ImageView
            ...
            android:padding="@{isShow}"
            ... />
```
此处为 `android:padding` 属性误绑定了一个 `Boolean` 数据，本应该因为期望的数据类型不一致而编译出错，但是因为自定义了一个 `Boolean` 转换 `int` 类型的转换器而变得合法，导致编译器认为是正常情况，从而导致 UI 显示异常。