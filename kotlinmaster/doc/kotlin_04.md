# Kotlin 的泛型

今天我们来聊一聊泛型。

下面这段 Java 代码在日常开发中应该很常见了：

```java
☕️
List<TextView> textViews = new ArrayList<TextView>();
```

其中 `List<TextView>` 表示这是一个泛型类型为 TextView 的列表。

这里首先要讲一下「泛型」的概念，因为如果不理解泛型，那么无论是 Java 还是 Kotlin 里对泛型的使用，都无从谈起了。

那什么是泛型呢？我们先来看看为什么要有这么个东西。

现在的程序开发都是面向对象的，平时会用到各种类型的对象，对象多了肯定需要用某种类型的容器来装它们，因而就有了一些容器类，比如 List、Map、Set 等。

但是这些容器类里面都是装的具体类型的对象，如果每个类型都去实现一个诸如 `TextViewList`、`ActivityList` 这样的具体类型的容器类，显然是不可能的。

所以就有了「泛型」，它的意思是把具体的类型泛化，编码的时候用符号指代类型，在具体使用的时候，才会确定它的类型。

前面那个例子，`List<TextView>` 指的就是一个装着对象类型为 TextView 的列表容器。



我们再看看另外一个常见的使用场景：

```java
☕️
Button button = new Button(context);
TextView textView = button;
// 👆 这是多态

List<Button> buttons = new ArrayList<Button>();
List<TextView> textViews = buttons;
// 👆 多态用在这里会报错 incompatible types: List<Button> cannot be converted to List<TextView>
```

我们知道 Button 是继承自 TextView 的，根据 Java 多态的特性，把 button 赋值给 textView 自然是没什么问题的。

那么一个 Button 的列表应该也能赋值给 TextView 的列表吧。为什么编译器就给报错呢？

这是因为 Java 的泛型本身具有不可变性（invariance），也就是说 Java 里面认为 `List<TextView>` 和 `List<Button>` 类型并不一致。

>  Java 的泛型类型会在编译时发生**类型擦除**，为了保证类型安全，不允许这样赋值。至于什么是类型擦除，这里就不展开了。

但是在实际使用中，我们的确会有这种类似的需求，需要把 Button 列表赋值给 TextView 列表。

 Java 提供了「泛型通配符」 `? extends` 和 `? super` 来解决这个问题。

### Java 中的 `? extends`

在 Java 里面是这么解决的：

```java
☕️
List<Button> buttons = new ArrayList<Button>();
      👇
List<? extends TextView> textViews = buttons;
```

这个 `? extends` 叫做「上界通配符」，可以使 Java 泛型具有协变性（covariance），协变性就是允许上面的赋值是合法的。

> 在继承关系树中，子类继承自父类，可以认为父类在上，子类在下。`extends` 限制了泛型类型的父类型，所以叫上界。

它有两层意思：

- 其中 `? ` 是个通配符，表示这个列表的泛型类型是一个**未知类型**。
- `extends` 限制了这个未知类型的上界，也就是泛型类型必须满足这个 extends 的限制条件，这里和定义 class 的 `extends` 关键字有点不一样：
    - 它的范围不仅是所有直接和间接子类，还包括上界定义的父类，也就是 TextView。
    - 它还有 `implements` 的意思，也就是说这里的上界 TextView 可以替换为接口。

这里 Button 是 TextView 的子类，满足了泛型类型的限制条件，所以能够成功赋值。

根据刚才的描述，下面几种情况都是可以的：

```java
☕️
List<? extends TextView> textViews = new ArrayList<TextView>();
List<? extends TextView> textViews = new ArrayList<Button>();
List<? extends TextView> textViews = new ArrayList<RadioButton>(); // 👈 RadioButton 是 Button 的子类
```

一般容器类都包含了 `get` 和 `add` 两种操作，比如 Java 中的 `List`，它的具体定义如下：

```java
☕️
public interface List<E> extends Collection<E>{
    E get(int index);
    boolean add(E e);
    ...
}

```

上面的代码中，`E` 就是表示泛型类型的符号（用其他字母甚至单词都可以）。

我们看看在使用了上界通配符之后，`List` 的使用上有没有什么问题：


```java
☕️
List<Button> buttons = ...;
List<? extends TextView> textViews = buttons;

TextView textView = textViews.get(0); // 👈 get 可以
textViews.add(textView);
//             👆 add 会报错，no suitable method found for add(TextView)
```

前面说到列表的泛型类型是个未知类型 `?`，编译器也不确定它是啥类型。

不过由于它满足 `? extends TextView` 的限制条件，所以 `get` 出来的对象，肯定是 TextView 的子类型，根据多态的特性，能够赋值给 TextView，当然也可以赋值给 View。

到了 `add` 操作的时候，我们可以这么理解：

- `List<? extends TextView>` 可能表示 `List<Button>`，也可能表示 `List<TextView>`。
- 对于前者，显然我们要添加 TextView 是不可以的。
- 实际情况是编译器无法确定，所以无法继续执行下去，就报错了。

那我干脆不要 `extends TextView` ，只用通配符 `?` 呢？

这样使用 `List<?>` 相当于是 `List<? extends Object>`。

```java
☕️
List<Button> buttons = new ArrayList<>();

List<?> list = buttons;
Object obj = list.get(0);

list.add(obj); // 👈 这里还是会报错
```

和前面的例子一样，编译器没法确定 `?` 的类型，所以这里就只能 `get` 到 Object 对象。

同时编译器为了保证类型安全，也不能向列表中插入任何类型的对象。

由于这个限制，使用了 `? extends` 泛型通配符的列表，只能够被消费，向外提供数据。



一个实际的场景是消费者买苹果手机，ta 只能买回来用（get），没办法造一台新手机（add）。

那么有消费者就有富土康和张全蛋们这些生产者，类似的 Java 里面还有一个泛型通配符能够使列表容器可以 `add` 但没法 `get`，它就是 `? super`。

### Java 中的 `? super`

先看一下它的写法：

```java
☕️
List<TextView> textViews = new ArrayList<TextView>();
     👇
List<? super Button> buttons = textViews;
```

这个 `? super` 叫做「下界通配符」，可以使 Java 泛型具有逆变性（contravariance）。

> 与上界通配符对应，这里 super 限制了通配符 ? 的子类型，所以称之为下界。

它也有两层意思：

- 通配符 `?` 表示列表的泛型类型是一个未知类型。
- `super` 限制了这个未知类型的上界，也就是泛型类型必须满足这个 super 的限制条件。
    - super 我们在类的方法里面经常用到，这里的范围不仅包括 Button 的父类，也包括下届 Button。
    - super 同样支持 implements，也就是说接口之间的继承也适用。

上面的例子中，TextView 是 Button 的父类型 ，也就能够满足 super 的限制条件，这里就可以成功赋值了。

根据刚才的描述，下面几种情况都是可以的：

```java
☕️
List<? super Button> buttons = new ArrayList<Button>();
List<? super Button> buttons = new ArrayList<TextView>();
List<? super Button> buttons = new ArrayList<Object>();
```

对于使用了下界通配符的列表，我们再看看它的 `get` 和 `add` 操作：

```java
☕️
List<TextView> textViews = ...
List<? super Button> buttons = textViews;
Object object = buttons.get(0); // 👈 get 出来的是 Object 类型
Button button = ...
buttons.add(button); // 👈 add 操作是可以的
```

首先 `?` 表示未知类型，编译器是不确定它的类型的：

- `get` 出来必须有一个类型，不能是一个不确定的范围。
- 虽然不知道它的具体类型，不过在 Java 里任何对象都是 Object 的子类，所以这里能把它赋值给 Object。

Button 对象一定是这个未知类型的子类型，根据多态的特性，这里通过 `add()` 方法添加 Button 对象是合法的。

使用下界通配符的列表，只能读取到 Object 对象，一般没有什么实际的使用场景，通常也只拿它来添加数据。



小结下，Java 的泛型本身是不支持协变和逆变的。

- 可以使用泛型通配符 `? extends` 来使泛型支持协变，但是「只能读不能添加」。
- 可以使用泛型通配符 `? super` 来使泛型支持逆变，但是「不能读只能添加」。

这也被成为 PECS 法则：*Producer-Extends, Consumer-Super*。

这里说的 Producer 和 Consumer 是指声明泛型通配符所代表的变量。

理解了 Java 的泛型之后，再理解 Kotlin 中的泛型，有如练完九阳神功再练乾坤大挪移，就比较容易了。

### Kotlin 中的 `out` 和 `in`

和 Java 泛型一样，Kolin 中的泛型本身也是不可变的。

需要使用关键字 `out` 来支持协变，等同于 Java 中的上界通配符 `? extends`。

```kotlin
🏝️
class Producer<T>{
    fun produce(): T {
        ...
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

val consumer: Consumer<Button> = Consumer<Button>()
val consumer: Consumer<Button> = Consumer<TextView>() // 👈 编译器报错，类型不匹配
val consumer: Consumer<in Button> = Consumer<TextView>()
//                     👆 可以用 in 来支持逆变
```

就和字面意思一样，`out` 表示这个变量或者方法参数只用来输出，不用来输入，也就是「只能读不能写」。

而 `in` 则相反，表示这个变量或者方法参数只能用来输入，不用来输出，也就是「不能读只能写」。



### 声明处的 `out` 和 `in`

在前面的例子中，在声明 `Producer` 的时候已经确定了泛型 `T` 只会作为输出来用，但是每次都需要在使用的时候加上 `out TextView` 来支持协变，写起来很麻烦。

Kotlin 提供了另外一种写法：可以在声明类的时候，给泛型参数加上 `out` 关键字，表明泛型参数 `T` 只会用来输出，在使用的时候就不用额外加 `out` 了。

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

与 `out` 一样，可以在声明类的时候，给泛型参数加上 `in` 关键字，来表明这个泛型参数 `T` 只用来输入。

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
它在 Kotlin 中有等效的写法：`*` 号，相当于 `out Any`。

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



Kotlin 只是把 `extends` 换成了 `:` 冒号。

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



还记得第二篇文章中，提到了两个 Kotlin 泛型与 Java 泛型不一致的地方。

1. Java 里的数组是支持逆变的，而 Kotlin 中的数组 Array 不支持逆变。

   在 Kotlin 中数组是用 Array 类来表示的，这个 Array 类使用泛型就和集合类一样，所以不支持逆变。

   

2. Java 中的 List 接口不支持逆变，而 Kotlin 中的 List 接口支持逆变。

   Java 中的 List 不支持逆变，原因在上文已经讲过了，需要使用泛型通配符来解决。 

   在 Kotlin 中，实际上 MutableList 接口才等于 Java 的 List。Kotlin 中的 List 接口实现了只读操作，没有写操作，所以不会有类型安全上的问题，自然可以支持逆变。



### 练习题

1. 实现一个 `fill` 函数，传入一个数组和一个对象，将对象填充到数组中，要求数组参数的泛型支持逆变。
2. 实现一个 `copy` 函数，传入两个数组参数，将一个数组中的元素复制到另外个数组中，要求数组参数的泛型支持协变和逆变。