# Kotlin 的泛型



今天我们来聊一聊泛型。

下面这段 Java 代码在日常开发中应该很常见了：

```java
☕️
List<TextView> textViews = new ArrayList<TextView>();
```

其中 `List<TextView>` 表示这是一个泛型类型为 TextView 的列表。

可以想一下，为什么我们会需要泛型呢？

现在的程序开发都是面向对象的，平时会用到很多各种类型的对象，对象多了肯定需要用某种类型的容器来装它们，所以就有了一些容器类，比如 List、Map、Set 等。

但是这些容器类里面都是装的具体类型的对象，我们几乎不可能去实现无数个类似于 `TextViewList`、`ActivityList` 这样的具体类型的容器类。

所以就有了泛型这个东西，把具体的类型泛化，编码的时候用符号指代类型，在具体使用的时候，才会确定它的类型。

前面那个例子，`List<TextView>`指的就是一个装着对象类型为 TextView 的列表容器。



一般容器类都包含了读写两种操作，比如 Java 中列表类，它的具体定义长这个样子：

```java
public interface List<E> extends Collection<E>{
    E get(int index);
    boolean add(E e);
  
    // ... 其他方法
}

```

`get()` 方法通过 index 获取到类型为 E 的对象，`add()` 方法可以向列表中添加一个类型为 E 的对象。



我们再看看另外一个例子：

```java
☕️
// Context context = ...
Button button = new Button(context);
TextView textView = button;
// 👆 这里能正确赋值

List<Button> buttons = new ArrayList<Button>();
List<TextView> textViews = buttons;
// 👆 IDE 会提示错误 incompatible types: List<Button> cannot be converted to List<TextView>
```

我们知道 Button 是继承自 TextView 的，根据 Java 多态的特性，把 button 赋值给 textView 自然是没什么问题的。

既然根据多态，Button 对象能作为 TextView 对象来用，那一个 Button 的列表应该也能赋值给 TextView 的列表吧。为什么编译器就给报错呢？

这是因为 Java 的泛型本身具有不可变性（invariance），也就是说 Java 里面认为 `List<TextView>` 和 `List<Button>` 类型并不一致。

>  Java 的泛型类型会在编译时发生**类型擦除**，为了保证类型安全，不允许这样赋值。至于什么是类型擦除，这里就不展开了。

但是在实际使用中，我们的确会有这种类似的需求，要把 Button 列表赋值给 TextView 列表。

 Java 提供了「泛型通配符」 `? extends` 和 `? super` 来解决棒我们解决这个问题。



### Java 中的 `? extends`

前面那个问题在 Java 里面是这么解决的：

```java
☕️
List<Button> buttons = new ArrayList<Button>();
      👇
List<? extends TextView> textViews = buttons;
```

这个 `? extends` 叫做「上界通配符」，可以使 Java 泛型具有协变性（covariance）。

> 在继承关系树中，子类继承自父类，可以认为父类在上，子类在下。extends 限制了类型的父类型，所以叫上界。

其中 `? `是个通配符，所以这个列表的泛型类型其实是一个**未知类型**。同时又用 `extends`限制了这个未知类型的上界，也就是泛型类型必须满足这个 extends 的限制条件。

这里 Button 是 TextView 的子类，满足了泛型类型的限制条件，所以能够成功赋值。

当然，这里不只是 List<Button> 可以赋值，还有几种情况也是可以的：

```java
☕️
List<? extends TextView> textViews = new ArrayList<TextView>();
List<? extends TextView> textViews = new ArrayList<Button>();
List<? extends TextView> textViews = new ArrayList<RadioButton>(); // 👈 RadioButton 是 Button 的子类
```

TextView 的所有子类型都满足 `? extends TextView`的条件，TextView 类型自身也算满足这个限制条件。



前面集合的基本操作有读和写，来看下使用了上界通配符的列表读写有没有什么问题：


```java
☕️
List<Button> buttons = new ArrayList<>();
// ... 执行一些添加元素的操作

List<? extends TextView> textViews = buttons;

TextView textView = textViews.get(0); // 👈 能正常读取到 TextView 类型的对象
View view = textViews.get(0);

textViews.add(textView);
//             👆 IDE 报错，no suitable method found for add(TextView)
```

前面说到列表的泛型类型是个未知类型 `?`，编译器也不确定它是啥类型。

不过由于它满足 `? extends TextView` 的限制条件，所以使用 `get()` 方法读出来的对象，肯定是 TextView 的子类型，根据多态的特性，能够赋值给 TextView 或者 View 等。

也是因为这样，编译器无法判断通过 `add()` 方法添加进去的 TextView 对象，到底是不是属于这个未知类型的，所以干脆直接报错，不让你编译通过，保证运行时类型安全。



那你可能会问，能不能直接只通配符 `?`呢？

答案是可以。这样使用 `List<?>` 相当于是 `List<? extends Object>`。

```java
☕️
List<Button> buttons = new ArrayList<>();

List<?> list = buttons;
Object obj = list.get(0);

list.add(obj); // 👈 这里会报错
```

和前面的例子一样，编译器没法确定 `?`的类型，所以这里就只能读到 Object 对象啦；同时编译器为了保证类型安全，也不能向列表中插入任何类型的对象。



当你遇到「只需要读取，不需要写入」的场景，就可以用 `? extends` 来使 Java 泛型支持协变，以此来扩大变量或者方法参数的接受范围。



使用了 `? extends` 泛型通配符的列表，只能够作为生产者向外提供数据。

这就像张全蛋在富土康造手机，工厂只生产手机卖出，不会买入手机；同时肯定会有对应的消费者只买入手机，不会去生产手机。

Java 里面还有一个泛型通配符能够使列表容器只写入不读取，它就是 `? super`。



### Java 中的 `? super`

下面先看一下它的写法：

```java
☕️
List<TextView> textViews = new ArrayList<TextView>();
     👇
List<? super Button> buttons = textViews;
```

这个 `? super` 叫做「下界通配符」，可以使 Java 泛型具有逆变性（contravariance）。

> 与上界通配符类似，这里 super 限制了通配符 ? 的子类型，所以称之为下界。

通配符 `?` 表示列表的泛型类型是一个未知类型，同时又用 super 限制了这个未知类型的上界，也就是泛型类型必须满足这个 super 的限制条件。

上面的例子中，TextView 是 Button 的父类型 ，也就能够满足 super 的限制条件，这里就可以成功赋值了。



这里不仅 TextView 类型的列表能正常赋值，还有其他一些情况：

```java
☕️
List<? super Button> buttons = new ArrayList<Button>();
List<? super Button> buttons = new ArrayList<TextView>();
List<? super Button> buttons = new ArrayList<Object>();
```

只要是 Button 的父类型就可以，也就是说从 Button 到 Object 之间继承关系上的各种类型，在这里就是可以的。



那么使用了下界通配符的列表，它的读写操作会有问题吗：

```java
☕️
List<TextView> textViews = new ArrayList<TextView>();
// ... 执行一些添加元素的操作

List<? super Button> buttons = textViews;

Object object = buttons.get(0); // 👈 这里读取到的是 Object 类型的对象

// Button button = ...
buttons.add(button); // 👈 这里可以向列表中添加 Button 对象
```

首先 `?` 表示未知类型，编译器是不确定它的类型的。

这个未知类型满足 super 的限制条件，通过 `get()` 方法读出来的对象，肯定是 Button 的父类型。虽然不知道它的具体类型，不过在 Java 里任何对象都是 Object 类型，所以这里能把它赋值给 Object。

Button 对象一定是这个未知类型的子类型，根据多态的特性，这里通过 `add()` 方法添加 Button 对象肯定是没问题的了。

使用下界通配符的列表，只能读取到 Object 对象，一般没有什么意义，通常也就只拿它来写入数据。



当你遇到「只想写数据，不需要读取使用」的场景，就可以用 `? super` 来使 Java 泛型支持逆变，可以扩大变量或参数的接收范围。



Java 的泛型本身是不支持协变和逆变的。可以使用泛型通配符 `? extends` 来使泛型支持协变，但是「只能读不能写」；可以使用泛型通配符 `? super` 来使泛型支持逆变，但是「不能读只能写」。这也被成为 PECS 法则：*Producer-Extends, Consumer-Super*。



Java 的泛型就讲到这里了，下面来看看在 Kotlin 中是如何使用泛型的。



### Kotlin 中的 `out` 和 `in`

和 Java 泛型一样，Kolin 中的泛型本身是不可变的。

需要使用关键字 `out` 来支持协变，等同于 Java 中的上界通配符 `? extends`。

```kotlin
🏝️
class Producer<T>{
    fun produce(): T{
        // ...
    }
}

val producer: Producer<TextView> = Producer<TextView>()
val producer: Producer<TextView> = Producer<Button>() // 👈 编译器报错，类型不匹配
val producer: Producer<out TextView> = Producer<Button>()
//                     👆 可以用 out 来支持协变
```



也可以使用关键字 `in` 来支持逆变，等同于 Java 中的下界通配符 `? super`。

```kotlin
🏝️            
class Consumer<T> {
    fun consume(t: T) {
        // ...
    }
}

val consumer :Consumer<Button> = Consumer<Button>()
val consumer :Consumer<Button> = Consumer<TextView>() // 👈 编译器报错，类型不匹配
val consumer :Consumer<in Button> = Consumer<TextView>()
//                     👆 可以用 in 来支持逆变
```

就和字面意思一样，`out` 表示这个变量或者方法参数只用来输出，不用来输入，也就是「只能读不能写」。

而 `in` 则相反，表示这个变量或者方法参数只能用来输入，不用来输出，也就是「不能读只能写」。



### 声明处的 `out` 和 `in`


每次使用的时候都需要关心泛型的型变类型，写起来很麻烦。

在 Kotlin 中提供了另外种写法，可以在定义类或者接口的时候，就指明泛型参数的型变类型，可以简化使用处的代码。

```kotlin
🏝️             👇
class Producer<out T>{
    fun produce(): T{
        // ...
    }
}

val producer: Producer<TextView> = Producer<TextView>()
val producer: Producer<TextView> = Producer<Button>() // 👈 这里不会报错了
val producer: Producer<out TextView> = Producer<Button>() // 👈 out 可以但没必要
```

在声明类的地方，指定泛型为协变类型，也就是只用来输出。在使用的地方，编译器就不会报错啦。



与 `out` 一样，可以直接在声明类的时候给泛型加上 `in`，来表面这个泛型类型只用来输入。

```kotlin
🏝️            👇
class Consumer<in T> {
    fun consume(t: T) {
        // ...
    }
}

val consumer :Consumer<Button> = Consumer<Button>()
val consumer :Consumer<Button> = Consumer<TextView>() // 👈 可以直接用 Button 的父类型
```



### `*` 号

前面讲到了 Java 中单个 `?` 号也能作为泛型通配符使用，相当于 `? extends Object`。
这个在 Kotlin 中有等效的写法：`*` 号，相当于 `out Any`。

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

由于 Java 中的泛型存在类型擦除的情况，任何在运行时需要知道泛型确切类型信息的操作都没法用了。

比如你不能检查一个对象是否为泛型类型 `T` 的实例：

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

这具体是怎么回事呢？等到后续章节讲到 `inline` 的时候会详细说明，这里就不过多延伸了。



我们来看看之前的文章提到的，Kotlin 泛型与 Java 泛型不一致的地方，主要有两点：

1. Java 里的数组是支持逆变的，而 Kotlin 中的数组 Array 不支持逆变

在 Kotlin 中数组是用 Array 类来表示的，这个 Array 使用泛型就和集合类一样，不支持逆变。

2. Java 中的 List 接口不支持逆变，而 Kotlin 中的 List 接口支持逆变

Java 的 List 不支持逆变，原因在上文已经讲过啦，需要使用泛型通配符来结果。 而在 Kotlin 中，实际上 MutableList 接口才等于 Java 的 List，Kotlin 中的 List 接口实现了只读操作，不会有类型安全上的问题，所以就支持的逆变。



### 思考题

