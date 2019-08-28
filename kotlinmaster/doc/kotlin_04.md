# Kotlin 的泛型



今天我们来聊一聊泛型。



下面这段代码在日常开发中应该很常见了：

```java
☕️
List<TextView> textViews = new ArrayList<TextView>();
```

其中 `List<TextView>` 表示这是一个泛型类型为 TextView 的列表。

可以想一下，为什么我们会需要泛型呢？

现在的程序开发都是面向对象的，平时会用到很多各种类型的对象，对象多了肯定需要用某种类型的容器来装它们，所以就有了一些容器类，比如 List、Map、Set 等。

但是这些容器类里面都是装的具体类型的对象，我们又不能实现无数个类似于 `TextViewList`、`ActivityList` 这样的具体类型的容器类。

所以就有了泛型这个东西，把具体的类型泛化，编码的时候用符号指代类型，在具体使用的时候，才会确定它的类型。

前面那个例子，`List<TextView>`指的就是一个装着对象类型为 TextView 的列表容器。



我们再看看另外一个例子：

```java
☕️

List<Activity> activities = new ArrayList<Activity>();
List<TextViews> textViews = activities;
// 👆 TextView 和 Activity 明显类型不同，所以两个列表类型肯定不一样嘛，这里就不能赋值

List<Button> buttons = new ArrayList<Button>();
List<TextView> textViews = buttons;
// 👆 IDE 会提示错误 incompatible types: List<Button> cannot be converted to List<TextView>
```

我们知道 Button 是继承自 TextView 的，那为什么 `List<Button>` 就不能赋值给 `List<TextView>` 呢？

这是因为 Java 的泛型本身具有不可变性(invariance)。

> 具体原因是 Java 的泛型类型会在编译时发生**类型擦除**，为了保证类型安全，不允许这样赋值。至于什么是类型擦除，这里就不展开了。


不过 Java 提供了泛型通配符 `? extends` 和 `? super` 来解决这问题。



### Java 中的 `? extends`

Java 提供了上界通配符 `? extends` 来使泛型具有协变性(covariance)。也就是这样：

```java
☕️
List<Button> buttons = new ArrayList<Button>();
      👇
List<? extends TextView> textViews = buttons;
```

用 `? extends TextView` 作为列表的泛型，也就是说泛型类型只要满足这个 extends 的条件即可。Button extends TextView，所以 buttons 能赋值到 textViews 变量上了。

这里要注意的是，这里定义的 `textViews` 变量，它内部装的对象的类型是某种**未知类型**，而这个未知类型必须是 TextView 或者它的子类型。




```java
☕️
List<Button> buttons = new ArrayList<>();
// ... 执行一些添加元素的操作

List<? extends TextView> textViews = buttons;
TextView textView = textViews.get(0); // 👈 能正常读取到 TextView 类型的对象
textViews.add(textView);
//             👆 IDE 报错，no suitable method found for add(TextView)
```

前面说到这个列表的泛型类型是一个未知类型，并不一定是 TextView 类型，所以 IDE 告诉我们没有合适的方法去添加 TextView，我们不能向列表添加新的元素。

不过我们可以从里面读取出 TextView 类型的对象，因为这个未知的泛型类型必然是 TextView 或者它的子类型了。



当你遇到「只需要读取，不需要写入」的场景，就可以用 `? extends` 来使 Java 泛型支持协变，以此来扩大变量或者参数的接受范围。



### Java 中的 `? super`

Java 中还提供了下界通配符 `? super` 来使泛型具有逆变性(contravariance)。比如这样：

```java
☕️
List<TextView> textViews = new ArrayList<TextView>();
     👇
List<? super Button> buttons = textViews;
```

这样定义的 `textViews` 变量，它内部装的对象的类型也是一个**未知类型**，这个未知类型必须是 Button 或其父类型。

```java
☕️
List<TextView> textViews = new ArrayList<TextView>();
// ... 执行一些添加元素的操作

List<? super Button> buttons = textViews;

Object object = buttons.get(0); // 👈 这里读取到的是 Object 类型的对象

// Button button = ...
buttons.add(button); // 👈 这里可以向列表中添加 Button 对象
```

因为这个列表中元素的类型，是一个未知类型，所以就只能从里面读到 Object 类型啦。

我们可以向列表里添加 Button 类型的对象。



当你遇到「只想修改，不需要读取使用」的场景，就可以用 `? super` 来使 Java 泛型支持逆变，以此来反向扩大变量或参数的接收范围。



Java 的泛型本身是不支持协变和逆变的。可以使用泛型通配符 `? extends` 来使泛型支持协变，但是「只能读不能写」；可以使用泛型通配符 `? super` 来使泛型支持逆变，但是「不能读只能写」。这也被成为 PECS 法则：*Producer-Extends, Consumer-Super*。









### Kotlin 中的 `out` 和 `in`

Kolin 中的泛型也是不支持协变和逆变的，需要使用关键字 `out` 和 `in`，等同于 Java 中的 `? extends` 和 `? super`。

```kotlin
🏝️
//                  👇 相当于 Java 中的 <? extends TextView>
var textViews: List<out TextView>
var textViews: List<in TextView>
//                  👆 相当于 Java 中的 <? super TextView>
```

和字面意思一样，`out` 表示这个变量或者参数只用来输出，不用来输入，也就是「只能读不能写」；`in` 则相反，表示这个变量或者参数只能用来输入，不用来输出，也就是「不能读只能写」。




### 声明处的 `out` 和 `in`

Kotlin 中的 `out` 和 `in` 关键字，不仅能用在变量和参数的声明上，还能直接用在类或接口的类型参数上：

```kotlin
🏝️                 👇
interface Producer<out T>{
    fun produce(): T
}
                   👇
interface Consumer<in T>{
    fun consume(product: T)
}
```



这种写法直接表明泛型 `T`只是用来输出或者只用来输入的，避免了在每个用到泛型类型的地方都需要加上 `out` 或 `in`，一劳永逸了。



### `*` 号

前面讲到了 Java 中的两个泛型的通配符，其实单个 `?` 号也能作为泛型通配符使用，相当于 `? extends Object`。

```java
☕️  👇
List<?> list;
```

在 Kotlin 中有等效的写法：`*` 号，相当于 `out Any`。

```kotlin
🏝️            👇
var list: List<*>
```





### `where` 关键字

Java 中声明类或接口的时候，可以使用 `extends` 来设置边界，将泛型类型参数限制为某个类型的子集：

```java
☕️                
//                👇  T 的类型必须是 Animal 的子类型
class Monster<T extends Animal>{
}
```

同时这个边界是可以设置多个，用 `&` 符号连接：

```java
☕️
//                            👇  T 的类型必须同时是 Animal 和 Food 的子类型
class Monster<T extends Animal & Food>{ 
}
```



Kotlin 只是把 `extends` 换成了 `:`冒号。

```kotlin
🏝️            👇
class Monster<T: Animal>
```

设置多个边界可以使用 `where` 关键字：

```kotlin
🏝️                👇
class Monster<T> where T: Animal, T: Food
```



### `reified` 关键字

由于 Java 中的泛型存在类型擦除的情况，任何在运行时需要知道泛型确切类型信息的操作都没法用了。比如你不能检查一个对象是否为泛型类型 `T` 的实例：

```java
☕️
<T> void printIfTypeMatch(Object item){
    if(item instanceof T){ // 👈 IDE 会提示错误，illegal generic type for instanceof
        System.out.println(item);
    }
}
```

```kotlin
🏝️
fun <T> printIfTypeMatch(item: Any){
    if(item is T){ // 👈 IDE 会提示错误，Cannot check for instance of erased type: T
        println(item)
    }
}
```



这个问题，在 Java 中的解决方案通常是额外传递一个 `Class<T>` 类型的参数，然后通过 `Class#isInstance` 方法来检查：

```java
☕️                             👇
<T> void check(Object item, Class<T> type) {
    if (type.isInstance(item)) {
               👆
        System.out.println(item);
    }
}
```



Kotlin 中同样可以这么解决，不过还有一个更方便的做法：使用关键字 `reified` 配合 `inline` 来简化代码：

```kotlin
🏝️ 👇         👇
inline fun <reified T> printIfTypeMatch(item: Any) {
    if (item is T) { // 👈 这里就不会在提示错误了
        println(item)
    }
}
```

这具体是怎么回事呢？等到后续章节讲到 `inlline` 的时候会详细说明，这里就不过多延伸了。





Kotlin 的泛型大部分都只是把 Java 泛型的东西换了种写法。如果你看到这里还是觉得很懵逼，可以先去把 Java 泛型好好复习一下，然后在回过头来看 Kotlin 的泛型，自然就懂了。