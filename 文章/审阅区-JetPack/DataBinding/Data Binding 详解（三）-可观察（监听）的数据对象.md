# Data Binding 详解（三）-可观察（监听）的数据对象
>作者：木易  
知是行之始，行是知之成。  
[文章配套的 Demo](https://github.com/muyi-yang/DataBindingDemo)：https://github.com/muyi-yang/DataBindingDemo

可观察（observable）是指一个对象通知其他对象其数据的更改的能力。 Data Binding 库支持创建可观察的对象、字段或集合。普通的对象都可以用于 Data Binding，但是对象数据改变并不会自动更新 UI 。使用 Data Binding 可以使数据对象能够在其数据更改时通知其他对象(称为监听器)。 有三种不同类型的可观察类: 对象、字段和集合，当其中一个可观察的数据对象绑定到 UI 并且数据对象的值发生更改时，UI 将自动更新。
## 可观察的字段
当一个类的部分属性要被观察，想做到数据实时反应到 UI 时，你可以使用通用的 Observable 类和以下特定类型的类来使字段可以被观察：
*   [`ObservableBoolean`](https://developer.android.google.cn/reference/android/databinding/ObservableBoolean.html)
*   [`ObservableByte`](https://developer.android.google.cn/reference/android/databinding/ObservableByte.html)
*   [`ObservableChar`](https://developer.android.google.cn/reference/android/databinding/ObservableChar.html)
*   [`ObservableShort`](https://developer.android.google.cn/reference/android/databinding/ObservableShort.html)
*   [`ObservableInt`](https://developer.android.google.cn/reference/android/databinding/ObservableInt.html)
*   [`ObservableLong`](https://developer.android.google.cn/reference/android/databinding/ObservableLong.html)
*   [`ObservableFloat`](https://developer.android.google.cn/reference/android/databinding/ObservableFloat.html)
*   [`ObservableDouble`](https://developer.android.google.cn/reference/android/databinding/ObservableDouble.html)
*   [`ObservableParcelable`](https://developer.android.google.cn/reference/android/databinding/ObservableParcelable.html)
*   [`ObservableField`](https://developer.android.google.cn/reference/android/databinding/ObservableField)
从名字可以看出这些可观察的对象都是特定类型的，它只能包含一个特定类型的数据。在使用时，最好创建一个不许变的对象（在 Java 中使用 final 修饰，在 Kotlin 中使用 val 声明），比如：
``` java
public class ListActivity extends AppCompatActivity {
    ...
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableInt index = new ObservableInt();
    ...
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        adapter = new ListAdapter();
        ...
        adapter.setItemClickListener(new ListAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View v, int position, CelebrityInfo info) {
                name.set(info.getName());
                index.set(position);
            }
        });
        ...
    }
}
```
这里是声明了一个可观察的 String 类型和 int 类型数据，`ObservableField` 类是万能类，它是可以包含所有对象数据的容器，调用 `set()` 方法赋值，调用 `get()` 方法取值。因为它是一个可观察的数据对象（内部通过观察者模式实现），所以当你调用 `set()` 方法赋值时，它会自动通知绑定了 `get()` 方法的 UI 刷新数据，这里点击列表的 item 时界面上会自动更新名字和 item 的索引。布局中的使用：

``` xml
<!--activity_list.xml-->
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="activity"
            type="com.example.databindingdemo.ListActivity" />
        ...
    </data>
        ...
        <TextView
            ...
            android:text="@{@string/click_info(activity.name, activity.index)}"
            />
        ...
</layout>
``` 
可运行 Demo 查看具体效果。
## 可观察的集合
很多情况下我们会使用一些集合数据，Data Binding 中也提供了相应的可观察的集合类：[`ObservableArrayMap`](https://developer.android.google.cn/reference/android/databinding/ObservableArrayMap)，[`ObservableArrayList`](https://developer.android.google.cn/reference/android/databinding/ObservableArrayList)。比如：
``` java
public class ListActivity extends AppCompatActivity {
    ...
    public final ObservableArrayList<CelebrityInfo> listData = new ObservableArrayList<>();
    ...
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        adapter = new ListAdapter();
        binding.setAdapter(adapter);
        adapter.setData(listData);
        ...
    }
}
```
这里声明了一个 ObservableArrayList 集合，它里面装着列表中的数据。在布局中显示了列表的大小：
``` xml
<!--activity_list.xml-->
<TextView
            ...
            android:text="@{@string/list_size(activity.listData.size())}"
            ... />
```
当列表中的数据发生改变时，界面会自动刷新，并显示列表当前的大小。便于观察效果，我延迟了 2 秒后往 `listData` 中插入了一个数据：
``` java
 private void getCelebrityList() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CelebrityInfo brock = new CelebrityInfo();
                brock.setName("布洛克·莱斯纳");
                brock.setWeight(130);
                brock.setRetire(false);
                brock.setIncome(1000);
                brock.setPhoto(R.drawable.brock);
                listData.add(1, brock);
            }
        }, 2000);
      ...
}
```
当你运行 Demo 后可看到具体效果。

`ObservableArrayMap` 的使用也是类似，和普通 Map 的使用也没什么区别，只不过它是一个可观察的 Map，在数据发送改变时会通知绑定的 UI 进行界面更新。

在前面讲表达式的时候已经讲过集合在布局中的使用（可以使用`[]`运算符访问集合），这里不再重复讲解。
## 可观察的对象
实现 [`Observable`](https://developer.android.google.cn/reference/android/databinding/Observable.html)接口的类，它就是一个可观察的类，允许希望被通知的对象注册数据更改的监听器。

这个 Observable 接口具有添加和删除监听器的机制，但必须由你来决定什么时候发送通知。为了更方便的开发，Data Binding 提供了实现监听器注册机制的类 [`BaseObservable`](https://developer.android.google.cn/reference/android/databinding/BaseObservable.html)。当你自定义的类继承了数据类 `BaseObservable` 时，需要通过 [`@Bindable`](https://developer.android.google.cn/reference/android/databinding/Bindable.html) 注解标记 getter 方法为一个可观察的属性，然后通过[`notifyPropertyChanged()`](https://developer.android.google.cn/reference/android/databinding/BaseObservable.html#notifyPropertyChanged(int)) 方法来通知数据发生改变（通常是在 setter 方法中调用），如下所示：
``` java
package com.example.databindingdemo.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.databindingdemo.BR;


/**
 * @author yanglijun
 * @date 19-2-22
 */
public class CelebrityInfo extends BaseObservable {
    private String name;
    private int weight;
    private int photo;
    private boolean isRetire;
    private double income;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        notifyPropertyChanged(BR.weight);
    }

    @Bindable
    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
        notifyPropertyChanged(BR.photo);
    }

    @Bindable
    public boolean isRetire() {
        return isRetire;
    }

    public void setRetire(boolean retire) {
        isRetire = retire;
        notifyPropertyChanged(BR.retire);
    }

    @Bindable
    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
        notifyPropertyChanged(BR.income);
    }

    public void copyObj(CelebrityInfo info){
        this.name = info.getName();
        this.income = info.getIncome();
        this.isRetire = info.isRetire();
        this.photo = info.getPhoto();
        this.weight = info.getWeight();
        notifyPropertyChanged(BR._all);
    }
}
```
这里的所有 getter  方法上都有一个 `@Binding` 注解，所有的 setter 方法中都有一个 `notifyPropertyChanged(BR.xxx)` 方法，这两个得成对使用才能达到数据改变则自动刷新的功能。其中 `BR.xxx` 是你在 getter 方法上增加注解后编译器自动生成的，它类似于 Android 的 R 文件，它里面创建了一系列绑定的 ID，生成规则是根据 getter 方法名而定的（去掉 get，is，按驼峰规则命名）。

setter 方法中调用 `notifyPropertyChanged(BR.xxx)` 方法是通知这单个属性的数据变化，如果你想一次性通知所有字段的观察者刷新数据则可以调用 `notifyPropertyChanged(BR._all)` 方法，`BR._all` 是默认生成的 ID，使用它可以通知对象中所有带 `@Binding` 注解的方法数据改变。比如：
``` java
public class CelebrityInfo extends BaseObservable {
    ...
    public void copyObj(CelebrityInfo info){
        this.name = info.getName();
        this.income = info.getIncome();
        this.isRetire = info.isRetire();
        this.photo = info.getPhoto();
        this.weight = info.getWeight();
        notifyPropertyChanged(BR._all);
    }
}
```