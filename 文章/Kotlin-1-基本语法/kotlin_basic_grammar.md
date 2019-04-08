# Kotlin 基础语法 

Kotlin 是一门 Google 极为推崇的语言，目前已经成为了 Android 的官方开发语言，这也是一门值得 Android 工程师学习的语言。同为 JVM 系列语言，Kotlin 和 Java 有不少相似之处，学习曲线并没有大家想象的那么陡峭。

> 下列内容都基于 Kotlin 1.3 最新版本。

## 基本数据类型

Kotlin 中的基本数据类型主要有：数值(numbers)、字符(characters)、布尔值(booleans)和字符串(string)。

> 通常情况下，数据类型会当作 JVM 上的基本数据类型来存储。当类型可空(nullable)或者引入了泛型时，数值类型会自动装箱（就跟 Java 里面的 int 变成 Integer 一样）。

### 数值类型

数值类型包括了 Int、Float、Double、Long、Short、Byte 这几种类型，写起来和 Java 并没有太大区别。

- `123`、`123L`、`0x0F`、`0b00001011` 分别用十进制、十六进制和二进制表示一个 Int 或 Long
- `123.5`、`123.5e10`、`123F` 来表示浮点数
- `1234_5678_9012_3456L` 也可以在数字中用下划线来提高可读性

在 Kotlin 中，数值类型**没有隐式自动转换**(Automatic Conversions)，他们都提供了一组名为 `toXXX()` 的转换方法，不同类型的数值之间必须显式转换。

```kotlin
val a: Int = 1 
val b: Long = a // 编译错误! Type mismatch: inferred type is Int but Long was expected
val b: Long = a.toLong() // 显式转换
```

### 字符类型

Kotlin 中的字符类型不能直接当作数字来用，不能与数字进行比较或者赋值，需要通过 `Chat.toInt()` 或 `Int.toChar()` 转换才行。

### 布尔值

布尔值分别为 `true` 和 `false`，可以使用逻辑运算符 `&&`、`||` 和 `!`。

### 字符串

Kotlin 中可以用双引号 `"` 声明普通的字符串，也可以用三个双引号 `"""` 来声明一个多行的字符串。

另外 Kotlin 还支持字符串模板语法，用 `$` 向字符串中注入变量值。如果需要表达式的值，可以用 `${...}`。 

```kotlin
val s = "Hello, world!\n"

val text = """
    Hello, world!
    Hello, world!
    Hello, world!
"""

val name = "bruce"
val greet = "Hello, $name! The length of name is ${name.length}"
```

## 变量


Kotlin 中使用 `val` 关键字来声明**只读**变量(Read-only local variables)，使用 `var` 关键字来声明变量，格式为`var/val 变量名: 变量类型`。变量类型可以手动指定，也可以由自动推断(inferred type)得出。

```kotlin
val a: Int = 1 // 显式指定类型
val b = 2      // 自动推断类型
val c: Int     // 没有初始化，需要指定类型
c = 3

var d = 4      // 数值可变的变量
d = 444
```

## 函数

Kotlin 中使用 `fun` 关键字来声明一个函数，函数的返回值类型在函数签名末尾指定。如果函数体只有一个表达式，返回值类型也可以自动推断。如果方法没有返回值，那么返回值类型就为 `Unit`，等同于 Java 中的 `void`，也可以省略不写出。

```kotlin
// 普通的带返回值的函数
fun sum(a: Int, b: Int): Int {
    return a + b
}

// 简化只有一个表达式的函数
fun sum(a: Int, b: Int) = a + b

// 返回 Unit 的函数
fun printSum(a: Int, b: Int): Unit {
    println("sum of $a and $b is ${a + b}")
}

// 省略 Unit 返回值的函数
fun printSum(a: Int, b: Int) {
    println("sum of $a and $b is ${a + b}")
}
```

## 控制流程

### If 语句

If 语句基本用法和 Java 一致。

```kotlin
var max: Int
if (a > b) {
    max = a
} else {
    max = b
}
```

此外，If 语句还支持作为表达值返回，等效于 Java 中的三目运算符(Conditional Operator `?  :`)。

```kotlin
val max: Int = if(a > b) a else b 
```

### When 语句

`when` 可是视作 Kotlin 中的 switch 语句。每一条分支的值，可以是常量或表达式。

```kotlin
when (x) {
    1 -> print("x == 1")
    2 -> print("x == 2")
    6 / 2 -> print("x == 3")
    else -> print("none of the above")
}
```

`when` 语句的分支还支持 `in` 和 `is` 操作符，分别用来判断范围和判断类型，可以与上面的常量、表达式混合使用。

```kotlin
when (x) {
    in 1..10 -> print("x is in the range")
    !in 10..20 -> print("x is outside the range")
    is Int -> print("x is Int")
    else -> print("none of the above")
}
```

此外，`when` 语句还可以用来替代 `if-else-if` 的多层判断。

```kotlin
when {
    x.isOdd() -> print("x is odd")
    x.isEven() -> print("x is even")
    else -> print("x is funny")
}
```

综上，如果 `when` 有参数传入，则与依次与各分支的值比较是否相等；如果没有参数传入，则直接判断各分支的布尔值是否为 true。

### For 循环

`for` 语句本质上是在遍历一个对象的迭代器，只要一个对象能提供迭代器，就能用 `for` 循环，比如常用的列表集合、字符串和范围对象(`range`) 。


基本写法：

```kotlin
for (item in collection) {
    print(item)
}

// range
for (i in 1..3) {
    println(i)
}
for (i in 6 downTo 0 step 2) {
    println(i)
}
```

> 示例中的 `1..3` `downTo` `step` 都是创建 Range 对象的方法，会在后续章节中介绍。

### While 循环

`while` 和 `do .. while` 语句与 Java 没有什么差别。

```kotlin
while (x > 0) {
    x--
}

do {
    val y = retrieveData()
} while (y != null)
```

> `continue` 和 `break` 语句在 for / while 循环中都能正常使用

## 异常处理

Kotlin 中的异常处理和 Java 类似，抛出异常用 `throw` 表达式，捕获异常用 `try-catch-finally` 语句。

```kotlin
throw Exception("Hi There!")

try {
    // 会产生异常的代码
}
catch (e: Exception) {
    // 处理异常
}
finally {
    // 可选的收尾操作
}
```

> `Exception("hello")` 是 Kotlin 中创建对象的写法，不需要 Java 中的 `new` 关键字，具体用法会在后续章节中介绍。

在 Kotlin 中，`try-catch` 语句可以作为表达式，输出返回值。

```kotlin
val a: Int? = try { parseInt(input) } catch (e: NumberFormatException) { null }
```

## 总结

Kotlin 作为一门 JVM 语言，在语法上和 Java 较为类似，类比两者的差异，则能更快更好地掌握 Kotlin 基本写法。

## 参考

- [Reference \- Kotlin Programming Language](https://kotlinlang.org/docs/reference/)




