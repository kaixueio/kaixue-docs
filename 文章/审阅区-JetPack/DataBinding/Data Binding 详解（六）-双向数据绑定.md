# Data Binding 详解（六）-双向数据绑定
>作者：木易  
知是行之始，行是知之成。  
[文章配套的 Demo](https://github.com/muyi-yang/DataBindingDemo)：https://github.com/muyi-yang/DataBindingDemo

前面讲到的各种数据绑定都是单向绑定，都是由数据驱动 UI 变化，当 UI 发生变化时并不会引起数据的改变。当 UI 的变化需要反应到数据中时，我们一般采取向 View 设置相应的监听器，然后在监听器中修改相应的数据。这种即由数据驱动 UI 变化，又由 UI 变化引起数据改变的绑定称为**双向绑定**。比如 `CheckBox` 的选择状态：
``` xml
    <CheckBox
            ...
            android:checked="@{activity.isTwowayEnable}"
            android:onCheckedChanged="@{activity.listener}"
            ... />
```
`android:checked` 属性设置了选择状态，`android:onCheckedChanged` 属性设置了选择状态变化的监听器，在监听到状态变化时及时修改 `isTwowayEnable` 变量。

Data Binding 为这种双向绑定提供了更为快捷的实现方式。在写属性表达式时使用这种符号 `@={}`，重要的是 `=` 符号，这样写即接受数据的更改又监听用户操作引起的变化。比如：
``` xml
    <!--activity_twoway.xml-->
    ...
    <EditText
            android:id="@+id/et_input"
            ...
            android:text="@={activity.inputTxt}"
            ... />
    <CheckBox
            android:id="@+id/cb_twoway_enable"
            ...
            android:checked="@={activity.isTwowayEnable}"
            ... />
    ...
```
以下为绑定的属性：
``` java
public class TwowayActivity extends AppCompatActivity {
    private ActivityTwowayBinding binding;

    public ObservableField<String> inputTxt = new ObservableField<>();
    public ObservableBoolean isTwowayEnable = new ObservableBoolean(true);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_twoway);
        binding.setActivity(this);
    }
}
```
双向绑定通过变量的 getter 方法获取值设置到属性中，通过变量 setter 方法把 View 变化的值保存起来。这里是使用 Data Binding 提供的 Observable 类，你也可以继承 `BaseObservable` 自定义可观察的数据对象，相关知识可参考[《Data Binding 详解（三）-可观察（监听）的数据对象》](http://xxx)。
## 使用自定义属性的双向数据绑定
Data Binding 为常见的属性提供双向绑定的实现，比如上面例子中使用到的 `android:text` 和
 `android:checked` 属性，你可以在程序中直接使用它们。如果你想对自定义属性适配器实现双向数据绑定，则需要使用 `@InverseBindingAdapter` 和 `@InverseBindingMethod` 注解，他们适用于不同的场景，下面将为你分别讲解他们如果使用。
* **@InverseBindingAdapter**

很多时候我们会使用到第三方的开源库，一般情况下这些开源库并不会提供属性的双向数据绑定，这时候就需要你来自定义。比如我为一个开源进度条控件 `BubbleSeekBar` 自定义一个双向数据绑定的属性 `app:progress`，实现双向数据绑定需要如下三个步骤：
1. 使用 `@BindingAdapter` 注解实现一个用于设置值的绑定适配器：
``` java
    @BindingAdapter("app:progress")
    public static void setProgress(BubbleSeekBar seekBar, int progress){
        if(seekBar.getProgress() != progress){
            seekBar.setProgress(progress);
        }
    }
```
>注意：其实这个自定义是多余的，因为 `BubbleSeekBar` 本来就有一个 `setProgress` 方法，Data Binding 会使用**自动选择方法**的方式设置值，相关知识请看[《Data Binding 详解（五）-绑定适配器》]()。
2. 使用 `@InverseBindingAdapter` 注解从 View 中读取值:
``` java
    @InverseBindingAdapter(attribute = "app:progress", event = "app:progressChanged")
    public static int getProgress(BubbleSeekBar seekBar) {
        return seekBar.getProgress();
    }
```
`@InverseBindingAdapter` 注解的 `attribute` 参数一看就明白，是绑定的属性，`event` 参数它标明了一个数据改变的事件，它是可选字段，这里先不关注，第三步将详细讲解。这个静态方法的参数就是确定属性与之关联的 View。

到这里 Data Binding 知道当数据发送变化时会调用带 `@BindingAdapter` 注解的静态方法 `setProgress` 设置数据，当 View 属性发送改变时会调用 `@InverseBindingAdapter` 注解的静态方法 `getProgress` 获取值。但是它没法自动知道 View 的值何时发送了变化，所以需要第三步。
3. 使用 `@BindingAdapter` 注解实现一个数据变化的通知事件适配器：
``` java
    @BindingAdapter("app:progressChanged")
    public static void setProgressListener(BubbleSeekBar seekBar,
                                           final InverseBindingListener listener) {
        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress,
                                          float progressFloat, boolean fromUser) {
                listener.onChange();
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress,
                                              float progressFloat) {
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress,
                                             float progressFloat, boolean fromUser) {
            }
        });
    }
```
这样，一个完整的自定义双向数据绑定就完成了。上面的静态方法有两个参数，第一个参数是确定属性绑定的 View，第二个参数是`InverseBindingListener` 监听器，它是固定的，它就是专门用来处理属性改变时的通知。我们在方法里面设置 View 相应的监听器，当属性发送改变时回调 `InverseBindingListener` 的 `onChange()` 方法告知 Data Binding 系统属性已经发送更改，然后系统调用使用 `@InverseBindingAdapter` 注解的方法获取 View 的值保存到绑定的变量中。

**注意**，这里使用的也是 `@BindingAdapter` 注解，但是它的属性却是和**步骤2** `@InverseBindingAdapter` 注解的 `event` 参数一样的值，其实它就是**步骤2**中的 `event` 所需要的事件属性。前面说过 `event` 参数是可选，如果在 `@InverseBindingMethod` 里面没有定义，那么 Data Binding 会自动匹配查找。自动匹配查找的原则：根据定义的属性名后面追加 `AttrChanged` 形成默认属性名进行匹配查找。比如，如果自己定义了 `attribute = "app:progress"`，那么自动会匹配查找 `app:progressAttrChanged` 属性的适配器作为 `event`。上面例子，在**步骤2**中就已经声明了 `event = "app:progressChanged"`，那么**步骤3**中绑定的属性就必须是这个 `@BindingAdapter("app:progressChanged")`。我们也可以不定义 `event`，那么我们就需要这样写：
``` java
    @InverseBindingAdapter(attribute = "app:progress")
    public static int getProgress(BubbleSeekBar seekBar) {
        return seekBar.getProgress();
    }

    @BindingAdapter("app:progressAttrChanged")
    public static void setProgressListener(BubbleSeekBar seekBar, final InverseBindingListener listener) {
        ...
    }
```
**步骤1**和**步骤3**使用的都是 `@BindingAdapter` 注解，我们也可以把两个方法合并，写成这样：
``` java
    @BindingAdapter(value = {"app:progress", "app:progressChanged"}, requireAll = false)
    public static void setProgress(BubbleSeekBar seekBar, int progress, final InverseBindingListener listener) {
        if(seekBar.getProgress() != progress){
            seekBar.setProgress(progress);
        }
        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress,
                                          float progressFloat, boolean fromUser) {
                listener.onChange();
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress,
                                              float progressFloat) {
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress,
                                             float progressFloat, boolean fromUser) {
            }
        });
    }
```
>注意：使用双向数据绑定时，请注意不要引入无限循环。当用户更改属性时，将调用使用 `@InverseBindingAdapter` 注解的方法获取新值，并将该值设置到属性绑定的变量中。如果绑定的变量是一个可观察的对象，那么它的值发送改变，又将调用使用 `@BindingAdapter` 注解的 setter 方法，将值设置到 View 中去，这又将触发 `InverseBindingListener` 监听器，监听器又将触发使用 `@InverseBindingAdapter` 注解的方法获取新值并设置到变量中。以此类推会形成无限循环，因此需要通过比较使用 `@BindingAdapter` 注解的 setter 方法中的新旧值，来打破可能的无限循环。
* **@InverseBindingMethod**
 
有些时候我们会实现一些自定义 View，在为自定义 View 增加双向数据绑定时，你也可以使用 `@InverseBindingMethods` 注解，`@InverseBindingMethods` 注解和 `@BindingMethods` 注解的用法很像，它可以写在任何一个类上面，它可以包含多个 `@InverseBindingMethod` 注解，每个注解对应着一个 View 的属性与之关联的数据变化监听的方法。比如我自定义了一个进度条控件 `MySeekBar`：
``` java
@InverseBindingMethods({@InverseBindingMethod(type = MySeekBar.class, attribute = "app:progress",
        event = "progressAttrChanged")})
public class MySeekBar extends BubbleSeekBar {
    ...
    public void setProgressAttrChanged(final InverseBindingListener listener) {
        if (listener != null) {
            setOnProgressChangedListener(new OnProgressChangedListener() {
                @Override
                public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                    listener.onChange();
                }
                @Override
                public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress,
                                                  float progressFloat) {
                }
                @Override
                public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress,
                                                 float progressFloat, boolean fromUser) {
                }
            });
        }
    }
}
```
`@InverseBindingMethod` 注解中有三个参数，`type` 是指明关联的自定义类，`attribute` 是指在布局中使用的属性，`event` 是指明一个监听数据改变时进行通知的方法，它是可选参数。`event` 参数需要注意一下几点：
1. 如果在 `@InverseBindingMethod` 里面没有定义，那么 Data Binding 会自动匹配查找。自动匹配查找的原则：根据定义的 `attribute` 值后面追加 `AttrChanged` 形成默认方法名进行匹配查找。比如，如果自己定义了 `attribute = "app:progress"`，那么会自动匹配查找 `progressAttrChanged()` 方法或者 `setProgressAttrChanged()` 方法，它会优先找 `progressAttrChanged()` 方法，如果没有，则找 `setProgressAttrChanged()` 方法，二者都没有，则会编译报错。
2. 如果定义了 `event` 参数，那么必须确保 `type` 所对应的类里面有这个值的对应方法。比如，如果你在`@InverseBindingMethod` 注解里面任意定义了一个值 `event = "progressAttrChanged"`，那么必须在 `type` 所对应的类中有一个名为 `progressAttrChanged()` 或者 `setProgressAttrChanged()` 的方法，如果没有，就会编译出错。然后在方法里面设置监听属性变化的监听事件，当属性改变时调用`InverseBindingListener` 的 `onChange()` 方法通知 Data Binding 数据已经发送改变。
## 转换器
有些时候我们需要把绑定到 View 对象的变量在显示之前格式化、转换或以某种方式更改，也就是说存储的数据和显示的内容是不一样的格式或类型，这时我们一般会写一个转换的工具类来实现。比如，显示一个日期的示例：
``` xml
    <!--activity_twoway.xml-->
    <TextView
            ...
            android:text="@{Converter.dateToString(activity.curTime)}"
            ... />
```
``` java
public class TwowayActivity extends AppCompatActivity {
    ...
    public ObservableLong curTime = new ObservableLong(System.currentTimeMillis());
    ...
}
```
`curTime` 是一个 `Long` 型的容器，它里面是时间戳，但界面上需要显示具体的年月日，因此这里使用 `Converter` 转换器对数据其进行转化。

有时候我们需要使用双向数据绑定，比如我们要把时间戳转换成日期显示，但当 `TextView` 中的值发送改变时又需要把日期转换为 `Long` 型存储。这时需要一个逆转换器来让 Data Binding 知道如何将字符串转成数据类型，这个过程你可以使用 `@InverseMethod` 注解来完成，在注解的参数中引用逆转换器方法名。比如：
``` java
public class Converter {
    /**
     * 绑定方法
     */
    @InverseMethod("stringToDate")
    public static String dateToString(long value) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return s.format(new Date(value));
    }
    /**
     * 逆转换器方法
     */
    public static long stringToDate(String value) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = 0;
        try {
            Date date = s.parse(value);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
```
布局中的使用：
``` xml
    <!--activity_twoway.xml-->
    <TextView
            ...
            android:text="@={Converter.dateToString(activity.curTime)}"
            ... />
```
`@InverseMethod` 注解可以应用于双向数据绑定中使用的任何方法，以声明从 View 的属性值转换到绑定数据值时用于逆转换的方法。这个逆转换方法的参数数量必须和绑定方法相同，参数类型可以不同。绑定方法的参数类型必须匹配其逆转换方法的返回值，绑定方法的返回值必须匹配其逆转换方法的参数。双向绑定的效果可以结合 Demo 查看，在 Demo 中我展现了数据变化时的效果。
## 双向属性
Data Binding 库已经为你内置了很多支持双向数据绑定的属性。你也可以参照这些属性的绑定适配器来实现自定义属性：

| 类别 | 属性 | 绑定适配器 |
| ------------ | ------------ | ----|
| [`AdapterView`](https://developer.android.google.cn/reference/android/widget/AdapterView) | `android:selectedItemPosition` `android:selection` | [`AdapterViewBindingAdapter`](https://android.googlesource.com/platform/frameworks/data-binding/+/3b920788e90bb0abe615a5d5c899915f0014444b/extensions/baseAdapters/src/main/java/android/databinding/adapters/AdapterViewBindingAdapter.java) |
| [`CalendarView`](https://developer.android.google.cn/reference/android/widget/CalendarView) | `android:date` | [`CalendarViewBindingAdapter`](https://android.googlesource.com/platform/frameworks/data-binding/+/3b920788e90bb0abe615a5d5c899915f0014444b/extensions/baseAdapters/src/main/java/android/databinding/adapters/CalendarViewBindingAdapter.java) |
| [`CompoundButton`](https://developer.android.google.cn/reference/android/widget/CompoundButton) | [`android:checked`](https://developer.android.google.cn/reference/android/R.attr#checked) | [`CompoundButtonBindingAdapter`](https://android.googlesource.com/platform/frameworks/data-binding/+/3b920788e90bb0abe615a5d5c899915f0014444b/extensions/baseAdapters/src/main/java/android/databinding/adapters/CompoundButtonBindingAdapter.java) |
| [`DatePicker`](https://developer.android.google.cn/reference/android/widget/DatePicker) | `android:year` `android:month` `android:day` | [`DatePickerBindingAdapter`](https://android.googlesource.com/platform/frameworks/data-binding/+/3b920788e90bb0abe615a5d5c899915f0014444b/extensions/baseAdapters/src/main/java/android/databinding/adapters/DatePickerBindingAdapter.java) |
| [`NumberPicker`](https://developer.android.google.cn/reference/android/widget/NumberPicker) | [`android:value`](https://developer.android.google.cn/reference/android/R.attr#value) | [`NumberPickerBindingAdapter`](https://android.googlesource.com/platform/frameworks/data-binding/+/3b920788e90bb0abe615a5d5c899915f0014444b/extensions/baseAdapters/src/main/java/android/databinding/adapters/NumberPickerBindingAdapter.java) |
| [`RadioButton`](https://developer.android.google.cn/reference/android/widget/RadioButton) | [`android:checkedButton`](https://developer.android.google.cn/reference/android/R.attr#checkedButton) | [`RadioGroupBindingAdapter`](https://android.googlesource.com/platform/frameworks/data-binding/+/3b920788e90bb0abe615a5d5c899915f0014444b/extensions/baseAdapters/src/main/java/android/databinding/adapters/RadioGroupBindingAdapter.java) |
| [`RatingBar`](https://developer.android.google.cn/reference/android/widget/RatingBar) | [`android:rating`](https://developer.android.google.cn/reference/android/R.attr#rating) | [`RatingBarBindingAdapter`](https://android.googlesource.com/platform/frameworks/data-binding/+/3b920788e90bb0abe615a5d5c899915f0014444b/extensions/baseAdapters/src/main/java/android/databinding/adapters/RatingBarBindingAdapter.java) |
| [`SeekBar`](https://developer.android.google.cn/reference/android/widget/SeekBar) | [`android:progress`](https://developer.android.google.cn/reference/android/R.attr#progress) | [`SeekBarBindingAdapter`](https://android.googlesource.com/platform/frameworks/data-binding/+/3b920788e90bb0abe615a5d5c899915f0014444b/extensions/baseAdapters/src/main/java/android/databinding/adapters/SeekBarBindingAdapter.java) |
| [`TabHost`](https://developer.android.google.cn/reference/android/widget/TabHost) | `android:currentTab` | [`TabHostBindingAdapter`](https://android.googlesource.com/platform/frameworks/data-binding/+/3b920788e90bb0abe615a5d5c899915f0014444b/extensions/baseAdapters/src/main/java/android/databinding/adapters/TabHostBindingAdapter.java) |
| [`TextView`](https://developer.android.google.cn/reference/android/widget/TextView) | [`android:text`](https://developer.android.google.cn/reference/android/R.attr#text) | [`TextViewBindingAdapter`](https://android.googlesource.com/platform/frameworks/data-binding/+/3b920788e90bb0abe615a5d5c899915f0014444b/extensions/baseAdapters/src/main/java/android/databinding/adapters/TextViewBindingAdapter.java) |
| [`TimePicker`](https://developer.android.google.cn/reference/android/widget/TimePicker) |`android:hour` `android:minute` | [`TimePickerBindingAdapter`](https://android.googlesource.com/platform/frameworks/data-binding/+/3b920788e90bb0abe615a5d5c899915f0014444b/extensions/baseAdapters/src/main/java/android/databinding/adapters/TimePickerBindingAdapter.java) |

至此 Data Binding 的基础知识点已讲完，想要灵活的运用还需多动手练习，同时你也可以下载官方的示例学习：
 *   [Android Data Binding Library samples](https://github.com/googlesamples/android-databinding)
*   [Android Data Binding codelab](https://codelabs.developers.google.com/codelabs/android-databinding)
