[TOC]


> 如果你正在使用 java8 或者更高的版本，那么对 Lambda 一定就比较熟悉了，Kotlin 中的 Lambda 跟 java8 相比，除了表示格式跟略有不同之外，很多东西是相似的的，就不用多介绍了。所以本文假设读者还没有使用过 Lambda。

# 1. 什么是 Lambda？


在官方文档的表述中， Lambda 是`函数字面值（Function literals）`，英语比较好的同学可以仔细体会一下 `literal` 这个单词的意思 :-D。

也可以这样理解：**Lambda 是一个函数表达式，是匿名的函数，它是用来表示`函数类型`的实例的一种方式**。

> 注意：Lambda 可以理解为匿名的函数，但这里并没有直接使用『匿名函数』（Anonymous Functions）这个名词，因为在 Kotlin 中，『匿名函数』一词已经有一个另外的定义，所以需要稍加区分。


我们先直接来看一个 lambda 表达式的示例：
```
{ x: Int, y: Int -> println("x + y = ${x + y}") }
```

它是一个比较简单的的 Lambda，但是如果要比较彻底地理解它，我们需要先从『**函数类型**』开始说起。

# 2. 从函数类型类型说起

## 2.1 为什么需要函数类型？

Kotlin 中的函数是一等公民，是不需要依附于类就能单独存在的。
函数也能够像基础类型、数组、以及类或接口一样，也是可以作为表达式进行赋值操作、存储或传递的，函数能够作为值赋值给一个变量、也能够作为另一个函数的参数或者返回值。

关于这一点，我们可以举一个例子 —— 先声明一个函数 max：

```kotlin
fun max(a: Int, b: Int): Int {
    return if (a > b) a else b
}
```

操作符`::`可以用于获取一个已声明的函数的引用，所以我们可以这样操作：

```kotlin
val maxCopy = ::max
// ↓↓ 然后就可以这么操作了：
maxCopy(1, 2) // 值为 2
maxCopy.invoke(1, 2) // 值为 2
    
```
上面这段代码演示了『函数能够作为值赋值给一个变量』：我们声明了一个叫 maxCopy 的变量，然后通过引用，把 max 函数的值赋给了 maxCopy，于是 maxCopy 就有了跟 max 一样的函数值，于是我们就可以像调用 max 函数一样调用 maxCopy 函数了。

Kotlin 的类型推导特性使得我们可以在写上面的代码时，能够省略 maxCopy 的类型，但我们都知道，一个完整的声明其实是需要指明变量的类型的。

那 maxCopy 是什么类型的变量呢？显然是个函数。

而上面的变量声明，如果写的完整一些，应该是这样的：

```kotlin
val maxCopy: (Int, Int) -> Int = ::max
```

这其中的『(Int, Int) -> Int』，表示『表示接收两个 Int 类型参数，返回 Int 类型』，它就是 maxCopy 的类型 —— 函数类型。

## 2.2 函数类型的定义

Kotlin 使用形如函数『(参数类型列表) -> 返回类型』这样的格式来定义函数类型。
即一个圆括号括起来的参数类型列表，一个箭头`->`符号，再加上一个返回类型。参数类型列表可以为空，如 `() -> A`。Unit 返回类型不可省略。

详细一些的规则如下，来自[这个链接](https://hltj.gitbooks.io/kotlin-reference-chinese/content/txt/lambdas.html)：

> * 函数类型表示法可以选择性地包含函数的参数名：`(x: Int, y: Int) -> Point`。 这些名称可用于表明参数的含义。
> * 如需将函数类型指定为可空，请使用圆括号：`((Int, Int) -> Int)?`。
> * 函数类型可以使用圆括号进行接合：`(Int) -> ((Int) -> Unit)`。
> * 箭头表示法是右结合的，`(Int) -> (Int) -> Unit` 与前述示例等价，但不等于 `((Int) -> (Int)) -> Unit`。

（另外类型列表前可以有一个接收者类型、有一种特殊的『挂起函数』类型表示有一个『suspend』修饰符 —— 这些在之后的文章中再进行展开）。


我们可以先不用考虑特殊情况，比如，如下代码中的 min 函数的类型是跟前面的 max 函数一样的，都可以表示为 `(Int ,Int) -> Int`，而 `isEqual` 函数的类型则是不同的 `(Int ,Int) -> Boolean` 。

```
fun min(a: Int, b: Int): Int {
    return if (a <= b) a else b
}

fun isEqual(a: Int, b: Int): Boolean {
    return a == b
}

```

现在，我们已经理解『函数类型』的含义了，它跟『基础类型』、『数组』以及『类』类似，都是用来表述某种数据格式的类型，那如何表示『函数类型』的具体实例呢？

我们需要用到『高阶函数』来演示这几种实例的表示方式。『高阶函数』也是函数类型的主要应用场景之一，所以我们先来了解一下它。

# 3. 高阶函数

**高阶函数是将函数用作参数或返回值的函数。**

> 是的，你之前在java中遇到的函数，都是低级（划掉）低阶函数 😏。

举个例子:

```kotlin
fun h1(useFirstFun: Boolean,
 num: Int, f1: (Int) -> Int, f2: (Int) -> Int): Int {
    return if (useFirstFun) f1.invoke(num) else f2.invoke(num)
}

```
比如，这个函数 h1，它接收4个参数：一个 Boolean 类型，一个 Int 值，以及两个`(Int) -> Int`的函数类型 f1、f2，它的作用是根据 Boolean 参数来判断是使用 f1 或者 f2 来对 Int 值进行操作来进行返回 —— 虽然看起来没什么卵用，但它的的确确是一个『高阶函数』。

要使用这个函数，有一个关键点就是：要把函数类型的实例作为参数传递给它。


# 4. 如何表示函数类型的实例？

开篇已经讲到，Lambda 就是用来表示`函数类型`的实例的一种方式。

除此之外，还有其他几种方式，一起列出来：

* 1. 使用 `::` 获取已声明函数的引用
* 2. 使用匿名函数
* 3. 使用实现函数类型接口的自定义类的实例
* 4. Lambda

接下来我们就来逐一看看。

## 4.1 使用 `::` 获取已声明函数的引用

跟上文使用的 `::max` 类似，这个就不多说了，直接看代码，其中的 `h1` 函数沿用自前文的『高阶函数』：

```kotlin
fun self(n : Int): Int {
    return n
}
fun dobule(n : Int) = n * 2

h1(false, 666, ::self, ::dobule)

```

## 4.2 匿名函数

开篇提到了 Lambda 是匿名的函数，但是却不是『匿名函数』，就是因为『匿名函数』这个词，已经有了另一个具体的含义。

匿名函数跟常规函数看起来非常像，唯一的区别就是它省略了函数名称。

```kotlin
fun(x: Int, y: Int): Int {
    return x + y
}

fun(x: Int) {}

fun(x: Int, y: Int): Int = x + y
```

比如上面这几种都是合法的匿名函数，但单独存在的匿名函数一般是没什么意义的，它也是函数类型的具体实例，应用场景之一就是作为高阶函数的参数，像这样：

```kotlin
h1(false, 666, ::self, fun (x: Int) = x * 3)

```

## 4.3 使用实现函数类型接口的自定义类的实例

『函数类型』跟『函数表达式』（或者叫做『函数实现』）的关系，很像是『接口』跟『实现类』的关系，只是省略了接口的名称跟抽象方法名称。

而实际上，不只是像，『函数类型』是的确可以跟接口一样，被其他类实现的，实现类中的具体方法名使用『invoke』，像这样：

```kotlin
// 类 HalfMaker 实现了函数类型 (Int) -> Int
class HalfMaker : (Int) -> Int {
    override fun invoke(a: Int): Int {
        return a / 2
    }
}
```
这个类的实例，也可以作为函数类型的实例进行使用：

```kotlin
h1(false, 666, ::self, HalfMaker()) // 类实例 HalfMaker() 可以作为函数类型实例使用
```

# 5. Lambda 

好，终于到我们期待已久的 『Lambda』出场了 🤩。

如之前所说，Lambda 是一个函数表达式，也即是一种函数类型的具体实例表示。

在实际的开发场景中，Lambda 一般也是应用最广泛的。

跟前面等价的代码，如果使用 Lambda 来写，是这样的：

```kotlin

h1(false, 666, { x: Int -> x }, { x: Int -> x * 2 })

h1(false, 666, { it }, { it * 2 })
```

其中 `{ x: Int -> x }` 以及 `{ x: Int -> x * 2 }` 就是 Lambda。

## 5.1 Lambda 的语法

Lambda 作为函数类型实例，必然需要有正确地表示函数相关元素的能力，包括参数列表、返回类型、函数体等。

Lambda 的语法规则是这样的：

* lambda 表达式总是括在花括号中
* 完整语法形式的参数声明放在花括号内，多参数的话用逗号隔开
* 函数体放在 `->` 之后
* 如果需要返回值，那么函数体的最后一个表达式会被视为返回值

以如下几条规则能够让 Lambda 的表示更加简洁：

* 如果参数类型能被推导出来，那么可以省略的类型标注
* 如果 lambda 表达式的参数未使用，那么可以用下划线取代其名称
* 如果 Lambda 只有一个参数并且编译器能识别出类型，那么可以不用声明这个参数并忽略 ->。 该参数会隐式声明为 it 。
* 如果函数的最后一个参数接受函数，那么作为相应参数传入的 lambda 表达式可以放在圆括号之外
* 如果该 lambda 表达式是调用时唯一的参数，那么圆括号可以完全省略。

所以上面的代码可以写的更加简洁：

```kotlin

h1(false, 666, { x: Int -> x }, { x: Int -> x * 2 })

// 省略参数的类型标注
h1(false, 666, { x -> x }, { x -> x * 2 })

// 省略单个的参数，并用 it 代替
h1(false, 666, { it }, { it * 2 })

```

## 5.2 为什么要使用 Lambda？

简洁。

相对于 Java 来说 ，Kotlin 更加的简洁优雅。而 Lambda 表达式，正式这种简洁优雅的重要组成部分。

我们可以再从一段代码的演化中来感受一下这种简洁。

先看一下 java 代码：

```java
view.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        System.out.println("Click");
    }
});
```

如果直接翻译成 Kotlin 代码的是，是这样：

```Kotlin 
view.setOnClickListener(object : View.OnClickListener {
    override fun onClick(v: View?) {
        println("Click")
    }
})
```

由于 `SAM转换`机制的存在，这段代码中的匿名类可以改成为 Lambda ：

> `SAM 转换（Single Abstract Method Conversions）`是指：对于Java 中**只有单个非默认抽象方法接口**，在Kotlin中可以直接函数类型表示 —— 现在只需知道这条即可，如果想了解更多可以参考：<https://www.jianshu.com/p/6386a36c1728>

```Kotlin
view.setOnClickListener({ v: View ->
    println("Click")
})
```

Lambda 中唯一的参数可以省略：
```Kotlin
view.setOnClickListener({
    println("Click")
})

```

如果该 lambda 表达式是调用时唯一的参数，那么圆括号可以完全省略，这段代码就可以变得更加简洁：
```Kotlin
view.setOnClickListener {
    println("Click")
}
```

↑ 简洁就是美，就是我们应该不懈追求的东西。


## 5.3 Lambda 的局限

Lambda 如果需要返回值，那么函数体的最后一个表达式会被视为返回值；此外，Lambda 也可以用使用[`限定的返回`](https://hltj.gitbooks.io/kotlin-reference-chinese/content/txt/returns.html#%E6%A0%87%E7%AD%BE%E5%A4%84%E8%BF%94%E5%9B%9E)来处理返回值。

Lambda 表达式并不能明确指定它作为函数时的函数返回类型。大多数情况下这没有问题，因为类型可以被自动推导出来。但如果的确需要指明返回类型的话，可以使用『匿名函数』来代替。


# 6. 总结

本文讲到了 Lambda、函数类型、匿名函数、高阶函数等，都比较浅显，但是希望没有让看到这里的你感到虚度。


