# Kotlin 的泛型

> 本期作者：
>
> 视频：扔物线（朱凯）
>
> 文章：[Bruce（郑啸天）](https://github.com/bruce3x)

大家好，我是扔物线朱凯。你在看的是码上开学项目的 Kotlin 高级部分的第 1 篇：Kotlin 的泛型。首当其冲的当然还是香香的视频香香的我啦：

<div class="aspect-ratio">
    <iframe src="https://player.bilibili.com/player.html?aid=66340216&page=1&high_quality=1" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true"> </iframe>
</div>

*如果你看不到上面的哔哩哔哩视频，可以点击 [这里](https://youtu.be/rgslIQi65b8) 去 YouTube 看。* 

> 以下内容来自文章作者[Bruce](https://github.com/bruce3x)。

## 从 Kotlin 的 in 和 out 说起

这期是码上开学 Kotlin 系列的独立技术点部分的第一期，我们来聊一聊泛型。

提到 Kotlin 的泛型，通常离不开 `in ` 和 `out` 关键字，但泛型这门武功需要些基本功才能修炼，否则容易走火入魔，待笔者慢慢道来。

下面这段 Java 代码在日常开发中应该很常见了：

```java
☕️
List<TextView> textViews = new ArrayList<TextView>();
```

其中 `List<TextView>` 表示这是一个泛型类型为 `TextView` 的 `List`。

那到底什么是泛型呢？我们先来讲讲泛型的由来。

现在的程序开发大都是面向对象的，平时会用到各种类型的对象，一组对象通常需要用集合来存储它们，因而就有了一些集合类，比如 `List`、`Map` 等。

这些集合类里面都是装的具体类型的对象，如果每个类型都去实现诸如 `TextViewList`、`ActivityList` 这样的具体的类型，显然是不可能的。

因此就诞生了「泛型」，它的意思是把具体的类型泛化，编码的时候用符号来指代类型，在使用的时候，再确定它的类型。

前面那个例子，`List<TextView>` 就是泛型类型声明。

既然泛型是跟类型相关的，那么是不是也能适用类型的多态呢？

先看一个常见的使用场景：

```java
☕️
TextView textView = new Button(context);
// 👆 这是多态

List<Button> buttons = new ArrayList<Button>();
List<TextView> textViews = buttons;
// 👆 多态用在这里会报错 incompatible types: List<Button> cannot be converted to List<TextView>
```

我们知道 `Button` 是继承自 `TextView` 的，根据 Java 多态的特性，第一处赋值是正确的。

但是到了 `List<TextView>` 的时候 IDE 就报错了，这是因为 Java 的泛型本身具有「不可变性 Invariance」，Java 里面认为 `List<TextView>` 和 `List<Button>` 类型并不一致，也就是说，子类的泛型（`List<Button>`）不属于泛型（`List<TextView>`）的子类。

>  Java 的泛型类型会在编译时发生**类型擦除**，为了保证类型安全，不允许这样赋值。至于什么是类型擦除，这里就不展开了。
>
>  你可以试一下，在 Java 里用数组做类似的事情，是不会报错的，这是因为数组并没有在编译时擦除类型：
>
>  ```java
>  ☕️
>  TextView[] textViews = new TextView[10];
>  ```

但是在实际使用中，我们的确会有这种类似的需求，需要实现上面这种赋值。

 Java 提供了「泛型通配符」 `? extends` 和 `? super` 来解决这个问题。

## Java 中的 `? extends`

在 Java 里面是这么解决的：

```java
☕️
List<Button> buttons = new ArrayList<Button>();
      👇
List<? extends TextView> textViews = buttons;
```

这个 `? extends` 叫做「上界通配符」，可以使 Java 泛型具有「协变性 Covariance」，协变就是允许上面的赋值是合法的。

> 在继承关系树中，子类继承自父类，可以认为父类在上，子类在下。`extends` 限制了泛型类型的父类型，所以叫上界。

它有两层意思：

- 其中 `?` 是个通配符，表示这个 `List` 的泛型类型是一个**未知类型**。
- `extends` 限制了这个未知类型的上界，也就是泛型类型必须满足这个 `extends` 的限制条件，这里和定义 `class` 的 `extends` 关键字有点不一样：
    - 它的范围不仅是所有直接和间接子类，还包括上界定义的父类本身，也就是 `TextView`。
    - 它还有 `implements` 的意思，即这里的上界也可以是 `interface`。

这里 `Button` 是 `TextView` 的子类，满足了泛型类型的限制条件，因而能够成功赋值。

根据刚才的描述，下面几种情况都是可以的：

```java
☕️
List<? extends TextView> textViews = new ArrayList<TextView>(); // 👈 本身
List<? extends TextView> textViews = new ArrayList<Button>(); // 👈 直接子类
List<? extends TextView> textViews = new ArrayList<RadioButton>(); // 👈 间接子类
```

一般集合类都包含了 `get` 和 `add` 两种操作，比如 Java 中的 `List`，它的具体定义如下：

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
List<? extends TextView> textViews = new ArrayList<Button>();
TextView textView = textViews.get(0); // 👈 get 可以
textViews.add(textView);
//             👆 add 会报错，no suitable method found for add(TextView)
```

前面说到 `List<? extends TextView>` 的泛型类型是个未知类型 `?`，编译器也不确定它是啥类型，只是有个限制条件。

由于它满足 `? extends TextView` 的限制条件，所以 `get` 出来的对象，肯定是 `TextView` 的子类型，根据多态的特性，能够赋值给 `TextView`，啰嗦一句，赋值给 `View` 也是没问题的。

到了 `add` 操作的时候，我们可以这么理解：

- `List<? extends TextView>` 由于类型未知，它可能是 `List<Button>`，也可能是 `List<TextView>`。
- 对于前者，显然我们要添加 TextView 是不可以的。
- 实际情况是编译器无法确定到底属于哪一种，无法继续执行下去，就报错了。

那我干脆不要 `extends TextView` ，只用通配符 `?` 呢？

这样使用 `List<?>` 其实是 `List<? extends Object>` 的缩写。

```java
☕️
List<Button> buttons = new ArrayList<>();

List<?> list = buttons;
Object obj = list.get(0);

list.add(obj); // 👈 这里还是会报错
```

和前面的例子一样，编译器没法确定 `?` 的类型，所以这里就只能 `get` 到 `Object` 对象。

同时编译器为了保证类型安全，也不能向 `List<?>` 中添加任何类型的对象，理由同上。

由于 `add` 的这个限制，使用了 `? extends` 泛型通配符的 `List`，只能够向外提供数据被消费，从这个角度来讲，向外提供数据的一方称为「生产者 Producer」。对应的还有一个概念叫「消费者 Consumer」，对应  Java 里面另一个泛型通配符 `? super`。

## Java 中的 `? super`

先看一下它的写法：

```java
☕️
     👇
List<? super Button> buttons = new ArrayList<TextView>();
```

这个 `? super` 叫做「下界通配符」，可以使 Java 泛型具有「逆变性 Contravariance」。

> 与上界通配符对应，这里 super 限制了通配符 ? 的子类型，所以称之为下界。

它也有两层意思：

- 通配符 `?` 表示 `List` 的泛型类型是一个**未知类型**。
- `super` 限制了这个未知类型的下界，也就是泛型类型必须满足这个 `super` 的限制条件。
    - `super` 我们在类的方法里面经常用到，这里的范围不仅包括 `Button` 的直接和间接父类，也包括下界 `Button` 本身。
    - `super` 同样支持 `interface`。

上面的例子中，`TextView` 是 `Button` 的父类型 ，也就能够满足 `super` 的限制条件，就可以成功赋值了。

根据刚才的描述，下面几种情况都是可以的：

```java
☕️
List<? super Button> buttons = new ArrayList<Button>(); // 👈 本身
List<? super Button> buttons = new ArrayList<TextView>(); // 👈 直接父类
List<? super Button> buttons = new ArrayList<Object>(); // 👈 间接父类
```

对于使用了下界通配符的 `List`，我们再看看它的 `get` 和 `add` 操作：

```java
☕️
List<? super Button> buttons = new ArrayList<TextView>();
Object object = buttons.get(0); // 👈 get 出来的是 Object 类型
Button button = ...
buttons.add(button); // 👈 add 操作是可以的
```

解释下，首先 `?` 表示未知类型，编译器是不确定它的类型的。

虽然不知道它的具体类型，不过在 Java 里任何对象都是 `Object` 的子类，所以这里能把它赋值给 `Object`。

`Button` 对象一定是这个未知类型的子类型，根据多态的特性，这里通过 `add` 添加 `Button` 对象是合法的。

使用下界通配符 `? super` 的泛型 `List`，只能读取到 `Object` 对象，一般没有什么实际的使用场景，通常也只拿它来添加数据，也就是消费已有的 `List<? super Button>`，往里面添加 `Button`，因此这种泛型类型声明称之为「消费者 Consumer」。

---

小结下，Java 的泛型本身是不支持协变和逆变的。

- 可以使用泛型通配符 `? extends` 来使泛型支持协变，但是「只能读取不能修改」，这里的修改仅指对泛型集合添加元素，如果是 `remove(int index)` 以及 `clear` 当然是可以的。
- 可以使用泛型通配符 `? super` 来使泛型支持逆变，但是「只能修改不能读取」，这里说的不能读取是指不能按照泛型类型读取，你如果按照 `Object` 读出来再强转当然也是可以的。

根据前面的说法，这被称为 PECS 法则：「*Producer-Extends, Consumer-Super*」。

理解了 Java 的泛型之后，再理解 Kotlin 中的泛型，有如练完九阳神功再练乾坤大挪移，就比较容易了。

## 说回 Kotlin 中的 `out` 和 `in`

和 Java 泛型一样，Kolin 中的泛型本身也是不可变的。

- 使用关键字 `out` 来支持协变，等同于 Java 中的上界通配符 `? extends`。
- 使用关键字 `in` 来支持逆变，等同于 Java 中的下界通配符 `? super`。

```kotlin
🏝️
var textViews: List<out TextView>
var textViews: List<in TextView>
```

换了个写法，但作用是完全一样的。`out` 表示，我这个变量或者参数只用来输出，不用来输入，你只能读我不能写我；`in` 就反过来，表示它只用来输入，不用来输出，你只能写我不能读我。

你看，我们 Android 工程师学不会 `out` 和 `in`，其实并不是因为这两个关键字多难，而是因为我们应该先学学 Java 的泛型。是吧？

说了这么多 `List`，其实泛型在非集合类的使用也非常广泛，就以「生产者-消费者」为例子：

```kotlin
🏝️
class Producer<T> {
    fun produce(): T {
        ...
    }
}

val producer: Producer<out TextView> = Producer<Button>()
val textView: TextView = producer.produce() // 👈 相当于 'List' 的 `get`
```

再来看看消费者：

```kotlin
🏝️            
class Consumer<T> {
    fun consume(t: T) {
        ...
    }
}

val consumer: Consumer<in Button> = Consumer<TextView>()
consumer.consume(Button(context)) // 👈 相当于 'List' 的 'add'
```

## 声明处的 `out` 和 `in`

在前面的例子中，在声明 `Producer` 的时候已经确定了泛型 `T` 只会作为输出来用，但是每次都需要在使用的时候加上 `out TextView` 来支持协变，写起来很麻烦。

Kotlin 提供了另外一种写法：可以在声明类的时候，给泛型符号加上 `out` 关键字，表明泛型参数 `T` 只会用来输出，在使用的时候就不用额外加 `out` 了。

```kotlin
🏝️             👇
class Producer<out T> {
    fun produce(): T {
        ...
    }
}

val producer: Producer<TextView> = Producer<Button>() // 👈 这里不写 out 也不会报错
val producer: Producer<out TextView> = Producer<Button>() // 👈 out 可以但没必要
```

与 `out` 一样，可以在声明类的时候，给泛型参数加上 `in` 关键字，来表明这个泛型参数 `T` 只用来输入。

```kotlin
🏝️            👇
class Consumer<in T> {
    fun consume(t: T) {
        ...
    }
}

val consumer: Consumer<Button> = Consumer<TextView>() // 👈 这里不写 in 也不会报错
val consumer: Consumer<in Button> = Consumer<TextView>() // 👈 in 可以但没必要
```

## `*` 号

前面讲到了 Java 中单个 `?` 号也能作为泛型通配符使用，相当于 `? extends Object`。
它在 Kotlin 中有等效的写法：`*` 号，相当于 `out Any`。

```kotlin
🏝️            👇
var list: List<*>
```

和 Java 不同的地方是，如果你的类型定义里已经有了 `out` 或者 `in`，那这个限制在变量声明时也依然在，不会被 `*` 号去掉。

比如你的类型定义里是 `out T : Number` 的，那它加上 `<*>` 之后的效果就不是 `out Any`，而是 `out Number`。

## `where` 关键字

Java 中声明类或接口的时候，可以使用 `extends` 来设置边界，将泛型类型参数限制为某个类型的子集：

```java
☕️                
//                👇  T 的类型必须是 Animal 的子类型
class Monster<T extends Animal>{
}
```

注意这个和前面讲的声明变量时的泛型类型声明是不同的东西，这里并没有 `?`。

同时这个边界是可以设置多个，用 `&` 符号连接：

```java
☕️
//                            👇  T 的类型必须同时是 Animal 和 Food 的子类型
class Monster<T extends Animal & Food>{ 
}
```

Kotlin 只是把 `extends` 换成了 `:` 冒号。

```kotlin
🏝️             👇
class Monster<T : Animal>
```

设置多个边界可以使用 `where` 关键字：

```kotlin
🏝️                👇
class Monster<T> where T : Animal, T : Food
```

有人在看文档的时候觉得这个 `where` 是个新东西，其实虽然 Java 里没有 `where` ，但它并没有带来新功能，只是把一个老功能换了个新写法。

不过笔者觉得 Kotlin 里 `where` 这样的写法可读性更符合英文里的语法，尤其是如果 `Monster` 本身还有继承的时候：

```kotlin
🏝️
class Monster<T> : MonsterParent<T>
    where T : Animal
```

## `reified` 关键字

由于 Java 中的泛型存在类型擦除的情况，任何在运行时需要知道泛型确切类型信息的操作都没法用了。

比如你不能检查一个对象是否为泛型类型 `T` 的实例：

```java
☕️
<T> void printIfTypeMatch(Object item) {
    if (item instanceof T) { // 👈 IDE 会提示错误，illegal generic type for instanceof
        System.out.println(item);
    }
}
```

Kotlin 里同样也不行：

```kotlin
🏝️
fun <T> printIfTypeMatch(item: Any) {
    if (item is T) { // 👈 IDE 会提示错误，Cannot check for instance of erased type: T
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

Kotlin 中同样可以这么解决，不过还有一个更方便的做法：使用关键字 `reified` 配合 `inline` 来解决：

```kotlin
🏝️ 👇         👇
inline fun <reified T> printIfTypeMatch(item: Any) {
    if (item is T) { // 👈 这里就不会在提示错误了
        println(item)
    }
}
```

这具体是怎么回事呢？等到后续章节讲到 `inline` 的时候会详细说明，这里就不过多延伸了。

还记得第二篇文章中，提到了两个 Kotlin 泛型与 Java 泛型不一致的地方，这里作一下解答。

1. Java 里的数组是支持协变的，而 Kotlin 中的数组 `Array` 不支持协变。

   这是因为在 Kotlin 中数组是用 `Array` 类来表示的，这个 `Array` 类使用泛型就和集合类一样，所以不支持协变。

2. Java 中的 `List` 接口不支持协变，而 Kotlin 中的 `List` 接口支持协变。

   Java 中的 `List` 不支持协变，原因在上文已经讲过了，需要使用泛型通配符来解决。 

   在 Kotlin 中，实际上 `MutableList` 接口才相当于 Java 的 `List`。Kotlin 中的 `List` 接口实现了只读操作，没有写操作，所以不会有类型安全上的问题，自然可以支持协变。

## 练习题

1. 实现一个 `fill` 函数，传入一个 `Array` 和一个对象，将对象填充到 `Array` 中，要求 `Array` 参数的泛型支持逆变（假设 `Array` size 为 1）。
2. 实现一个 `copy` 函数，传入两个 `Array` 参数，将一个 `Array` 中的元素复制到另外个 `Array` 中，要求 `Array` 参数的泛型分别支持协变和逆变。（提示：Kotlin 中的 `for` 循环如果要用索引，需要使用 `Array.indices`）


## 作者介绍

### 视频作者

#### 扔物线（朱凯）

- 码上开学创始人、项目管理人、内容模块规划者和视频内容作者。
- <a href="https://developers.google.com/experts/people/kai-zhu" target="_blank">Android GDE</a>（ Google 认证 Android 开发专家），前 Flipboard Android 工程师。 
- GitHub 全球 Java 排名第 92 位，在 <a href="https://github.com/rengwuxian" target="_blank">GitHub</a> 上有 6.6k followers 和 9.9k stars。
- 个人的 Android 开源库 <a href="https://github.com/rengwuxian/MaterialEditText/" target="_blank">MaterialEditText</a> 被全球多个项目引用，其中包括在全球拥有 5 亿用户的新闻阅读软件 Flipboard 。
- 曾多次在 Google Developer Group Beijing 线下分享会中担任 Android 部分的讲师。
- 个人技术文章《<a href="https://gank.io/post/560e15be2dca930e00da1083" target="_blank">给 Android 开发者的 RxJava 详解</a>》发布后，在国内多个公司和团队内部被转发分享和作为团队技术会议的主要资料来源，以及逆向传播到了美国一些如 Google 、 Uber 等公司的部分华人团队。
- 创办的 Android 高级进阶教学网站 [HenCoder](https://hencoder.com) 在全球华人 Android 开发社区享有相当的影响力。
- 之后创办 Android 高级开发教学课程 [HenCoder Plus](https://plus.hencoder.com) ，学员遍布全球，有来自阿里、头条、华为、腾讯等知名一线互联网公司，也有来自中国台湾、日本、美国等地区的资深软件工程师。

### 文章作者

#### Bruce（郑啸天）

[Bruce（郑啸天）](https://github.com/bruce3x)，即刻 Android 工程师。2018 年加入即刻，参与了即刻多个版本的迭代。多年 Android 开发经验，现在负责即刻客户端中台基础建设。