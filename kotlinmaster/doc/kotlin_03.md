## Kotlin 里那些「更方便的」

在上期内容当中，我们介绍了 Kotlin 的那些与 Java 写法不同的地方。这一期我们再进阶一点，讲一讲 Kotlin 中那些「更方便的」用法。这些知识点在不知道之前，你也可以正常写 Kotlin，但是在熟悉之后会让你写得更爽。

### 构造器

#### 主构造器

我们之前已经了解了 Kotlin 中 constructor 的写法：

```kotlin
🏝️
class User {
    var name: String
    constructor(name: String) {
        this.name = name
    }
}
```

其实 Kotlin 中还有更简单的方法来写构造器：

```kotlin
🏝️
               👇       
class User constructor(name: String) {
    //                  👇 这里与构造器中的 name 是同一个
    var name: String = name
}
```

这里有几处不同点：

- `constructor` 构造器移到了类名之后
- 类的属性 `name` 可以引用构造器中的参数 `name`

这个写法叫「主构造器 primary constructor」。与之相对的在第二篇中，写在类中的构造器被称为「次构造器」。在 Kotlin 中一个类最多只能有 1 个主构造器（也可以没有），而次构造器是没有个数限制。

主构造器中的参数除了可以在类的属性中使用，还可以在 `init` 代码块中使用：

```kotlin
🏝️
class User constructor(name: String) {
    var name: String
    init {
        this.name = name
    }
}
```

其中 `init` 代码块是紧跟在主构造器之后执行的，这是因为主构造器本身没有代码体，`init` 代码块就充当了主构造器代码体的功能。

另外，如果类中有主构造器，那么其他的次构造器都需要通过 `this` 关键字调用主构造器，可以直接调用或者通过别的次构造器间接调用。如果不调用 IDE 就会报错：

```kotlin
🏝️
class User constructor(var name: String) {
    constructor(name: String, id: Int) {
    // 👆这样写会报错，Primary constructor call expected
    }
}
```

为什么当类中有主构造器的时候就强制要求次构造器调用主构造器呢？

我们从主构造器的特性出发，一旦在类中声明了主构造器，就包含两点：

- 必须性：创建类的对象时，不管使用哪个构造器，都需要主构造器的参与
- 第一性：在类的初始化过程中，首先执行的就是主构造器

这也就是主构造器的命名由来。

当一个类中同时有主构造器与次构造器的时候，需要这样写：

```kotlin
🏝️
class User constructor(var name: String) {
                                   // 👇  👇 直接调用主构造器
    constructor(name: String, id: Int) : this(name) {
    }
                                                // 👇 通过上一个次构造器，间接调用主构造器
    constructor(name: String, id: Int, age: Int) : this(name, id) {
    }
}
```

在使用次构造器创建对象时，`init` 代码块是先于次构造器执行的。如果把主构造器看成身体的头部，那么 `init` 代码块就是颈部，次构造器就相当于身体其余部分。

细心的你也许会发现这里又出现了 `:` 符号，它还在其他场合出现过，例如：

- 变量的声明：`var id: Int`
- 类的继承：`class MainActivity : AppCompatActivity() {}`
- 接口的实现：`class User : Impl {}`
- 匿名类的创建：`object: ViewPager.SimpleOnPageChangeListener() {}`
- 函数的返回值：`fun sum(a: Int, b: Int): Int`

可以看出 `:` 符号在 Kotlin 中非常高频出现，它其实表示了一种依赖关系，在这里表示依赖于主构造器。

通常情况下，主构造器中的 `constructor` 关键字可以省略：

```kotlin
🏝️
class User(name: String) {
    var name: String = name
}
```

但有些场景，`constructor` 是不可以省略的，例如在主构造器上使用「可见性修饰符」或者「注解」：

- 可见性修饰符我们之前已经讲过，它修饰普通函数与修饰构造器的用法是一样的，这里不再详述：

  ```kotlin
  🏝️
  class User private constructor(name: String) {
  //           👆 主构造器被修饰为私有的，外部就无法调用该构造器
  }
  ```

- 关于注解的知识点，我们之后会讲，这里就不展开了

既然主构造器可以简化类的初始化过程，那我们就帮人帮到底，送佛送到西，用主构造器把属性的初始化也一并给简化了。

#### 主构造器里声明属性

之前我们讲了主构造器中的参数可以在属性中进行赋值，其实还可以在主构造器中直接声明属性：

```kotlin
🏝️
           👇
class User(var name: String) {
}
// 等价于：
class User(name: String) {
  var name: String = name
}
```

如果在主构造器的参数声明时加上 `var` 或者 `val`，就等价于在类中创建了该名称的属性（property），并且初始值就是主构造器中该参数的值。

以上讲了所有关于主构造器相关的知识，让我们总结一下类的初始化写法：

- 首先创建一个 `User` 类：

  ```kotlin
  🏝️
  class User {
  }
  ```

- 添加一个参数为 `name` 与 `id` 的主构造器：

  ```kotlin
  🏝️
  class User(name: String, id: String) {
  }
  ```

- 将主构造器中的 `name` 与 `id` 声明为类的属性：

  ```kotlin
  🏝️
  class User(val name: String, val id: String) {
  }
  ```

- 然后在 `init` 代码块中添加一些初始化逻辑：

  ```kotlin
  🏝️
  class User(val name: String, val id: String) {
      init {
          ...
      }
  }
  ```

- 最后再添加其他次构造器：

  ```kotlin
  🏝️
  class User(val name: String, val id: String) {
      init {
          ...
      }
      
      constructor(person: Person) : this(person.name, person.id) {
      }
  }
  ```

  当一个类有多个构造器时，只需要把最基本、最通用的那个写成主构造器就行了。这里我们选择将参数为 `name` 与 `id` 的构造器作为主构造器。

到这里，整个类的初始化就完成了，类的初始化顺序就和上面的步骤一样。

除了构造器，普通函数也是有很多简化写法的。

### 函数简化

#### 使用 `=` 连接返回值

我们已经知道了 Kotlin 中函数的写法：

```kotlin
🏝️
fun area(width: Int, height: Int): Int {
    return width * height
}
```

其实，这种只有一行代码的函数，还可以这么写：

```kotlin
🏝️
                                      👇
fun area(width: Int, height: Int): Int = width * height
```

`{}` 和 `return ` 没有了，使用 `=` 符号连接返回值。

我们之前讲过，Kotlin 有「类型推断」的特性，那么这里函数的返回类型还可以隐藏掉：

```kotlin
🏝️
//                               👇省略了返回类型
fun area(width: Int, height: Int) = width * height
```

不过，在实际开发中，还是推荐显式地将返回类型写出来，增加代码可读性。

以上是函数有返回值时的情况，对于没有返回值的情况，可以理解为返回值是 `Unit`：

```kotlin
🏝️
fun sayHi(name: String) {
    println("Hi " + name)
}
```

因此也可以简化成下面这样：

```kotlin
🏝️
                       👇
fun sayHi(name: String) = println("Hi " + name)
```

简化完函数体，我们再来看看前面的参数部分。

对于 Java 中的方法重载，我们都不陌生，那 Kolin 中是否有更方便的重载方式呢？接下来我们看看 Kotlin 中的「参数默认值」的用法。

#### 参数默认值

Java 中，允许在一个类中定义多个名称相同的方法，但是参数的类型或个数必须不同，这就是方法的重载：

```java
☕️
public void sayHi(String name) {
    System.out.println("Hi " + name);
}

public void sayHi() {
    sayHi("world"); 
}
```

在 Kotlin 中，也可以使用这样的方式进行函数的重载，不过还有一种更简单的方式，那就是「参数默认值」：

```kotlin
🏝️
                           👇
fun sayHi(name: String = "world") = println("Hi " + name)
```

这里的 `world` 是参数 `name` 的默认值，当调用该函数时不传参数，就会使用该默认值。

这就等价于上面 Java 写的重载方法，当调用 `sayHi` 函数时，参数是可选的：

```kotlin
🏝️
sayHi("kaixue.io")
sayHi() // 使用了默认值 "world"
```

既然与重载函数的效果相同，那 Kotlin 中的参数默认值有什么好处呢？仅仅只是少写了一些代码吗？

其实在 Java 中，每个重载方法的内部实现可以各不相同，这就无法保证重载方法内部设计上的一致性，而 Kotlin 的参数默认值就解决了这个问题。

不过参数默认值在调用时也不是完全可以放飞自我的。

来看下面这段代码，这里函数中有默认值的参数在无默认值参数的前面：

```kotlin
🏝️
fun sayHi(name: String = "world", age: Int) {
    ...
}

sayHi(10)
//    👆 这时想使用默认值进行调用，IDE 会报以下两个错误
// The integer literal does not conform to the expected type String
// No value passed for parameter 'age'
```

这个错误就是告诉你参数不匹配，说明我们的「打开方式」不对，其实 Kotlin 里是通过「命名参数」来解决这个问题的。

#### 命名参数

具体用法如下：

```kotlin
🏝️
fun sayHi(name: String = "world", age: Int) {
    ...
}
      👇   
sayHi(age = 21)
```

在调用函数时，显式地指定了参数 `age` 的名称，这就是「命名参数」。Kotlin 中的每一个函数参数都可以作为命名参数。

再来看一个有非常多参数的函数的例子：

```kotlin
🏝️ 
fun sayHi(name: String = "world", age: Int, isStudent: Boolean = true, isFat: Boolean = true, isTall: Boolean = true) {
    ...
}
```

当函数中有非常多的参数时，调用该函数就会写成这样：

```kotlin
🏝️
sayHi("world", 21, false, true, false)
```

当看到后面一长串的布尔值时，我们很难分清楚每个参数的用处，可读性很差。通过命名参数，我们就可以这么写：

```kotlin
🏝️
sayHi(name = "wo", age = 21, isStudent = false, isFat = true, isTall = false)
```

与命名参数相对的一个概念被称为「位置参数」，也就是按位置顺序进行参数填写。

当一个函数被调用时，如果混用位置参数与命名参数，那么所有的位置参数都应该放在第一个命名参数之前：

```kotlin
🏝️
fun sayHi(name: String = "world", age: Int) {
    ...
}

sayHi(name = "wo", 21) // 👈 IDE 会报错，Mixing named and positioned arguments is not allowed
sayHi("wo", age = 21) // 👈 这是正确的写法
```

讲完了命名参数，我们再看看 Kotlin 中的另一种常见函数：嵌套函数。

#### 本地函数（嵌套函数）

首先来看下这段代码，这是一个简单的登录的函数：

```kotlin
🏝️
fun login(user: String, password: String, illegalStr: String) {
    // 验证 user 是否为空
    if (user.isEmpty()) {
        throw IllegalArgumentException(illegalStr)
    }
    // 验证 password 是否为空
    if (password.isEmpty()) {
        throw IllegalArgumentException(illegalStr)
    }
}
```

该函数中，检查参数这个部分有些冗余，我们又不想将这段逻辑作为一个单独的函数对外暴露。这时可以使用嵌套函数，在 `login` 函数内部声明一个函数：

```kotlin
🏝️
fun login(user: String, password: String, illegalStr: String) {
           👇 
    fun validate(value: String, illegalStr: String) {
      if (value.isEmpty()) {
          throw IllegalArgumentException(illegalStr)
      }
    }
   👇
    validate(user, illegalStr)
    validate(password, illegalStr)
}
```

这里我们将共同的验证逻辑放进了嵌套函数 `validate` 中，并且 `login` 函数之外的其他地方无法访问这个嵌套函数。

这里的 `illegalStr` 是通过参数的方式传进嵌套函数中的，其实完全没有这个必要，因为嵌套函数中可以访问在它外部的所有变量或常量，例如类中的属性、当前函数中的参数与变量等。

我们稍加改进：

```kotlin
🏝️
fun login(user: String, password: String, illegalStr: String) {
    fun validate(value: String) {
        if (value.isEmpty()) {
                                              👇
            throw IllegalArgumentException(illegalStr)
        }
    }
    ...
}
```

这里省去了嵌套函数中的 `illegalStr` 参数，在该嵌套函数内直接使用外层函数 `login` 的参数 `illegalStr`。

上面 `login` 函数中的验证逻辑，其实还有另一种更简单的方式：

```kotlin
🏝️
fun login(user: String, password: String, illegalStr: String) {
    require(user.isNotEmpty()) { illegalStr }
    require(password.isNotEmpty()) { illegalStr }
}
```

其中用到了 lambda 表达式以及 Kotlin 内置的 `require` 函数，这里先不做展开，之后的文章会介绍。

### 字符串

讲完了普通函数的简化写法，Kotlin 中字符串也有很多方便写法。

#### 字符串模板

在 Java 中，字符串与变量之间是使用 `+` 符号进行拼接的，Kotlin 中也是如此：

```kotlin
🏝️
val name = "world"
println("Hi " + name)
```

但是当变量比较多的时候，可读性会变差，写起来也比较麻烦。

Java 给出的解决方案是 `String.format`：

```java
☕️
System.out.print(String.format("Hi %s", name));
```

Kotlin 为我们提供了一种更加方便的写法：

```kotlin
🏝️
val name = "world"
//         👇 用 '$' 符号加参数的方式
println("Hi $name")
```

这种方式就是把 `name` 从后置改为前置，简化代码的同时增加了字符串的可读性。

除了变量，`$` 后还可以跟表达式，但表达式是一个整体，所以我们要用 `{}` 给它包起来：

```kotlin
🏝️
val name = "world"
println("Hi ${name.length}") 
```

其实就跟四则运算的括号一样，提高语法上的优先级，而单个变量的场景可以省略 `{}`。

字符串模板还支持转义字符，比如使用转义字符 `\n` 进行换行操作：

```kotlin
🏝️
val name = "world!\n"
println("Hi $name") // 👈 会多打一个空行
```

字符串模板的用法对于我们 Android 工程师来说，其实一点都不陌生。

首先，Gradle 所用的 Groovy 语言就已经有了这种支持：

```groovy
def name = "world"
println "Hi ${name}"
```

在 Android 的资源文件里，定义字符串也有类似用法：

```xml
<string name="hi">Hi %s</string> 
```

```java
☕️
getString(R.id.hi, "world");
```

#### raw string (原生字符串)

有时候我们不希望写过多的转义字符，这种情况 Kotlin 通过「原生字符串」来实现。

用法就是使用一对 `"""` 将字符串括起来：

```kotlin
🏝️
val name = "world"
val myName = "kotlin"
           👇
val text = """
      Hi $name!
    My name is $myName.\n
"""
println(text)
```

这里有几个注意点：

- `\n` 并不会被转义
- 最后输出的内容与写的内容完全一致，包括实际的换行
- `$` 符号引用变量仍然生效

这就是「原生字符串」。输出结果如下：

```
      Hi world!
    My name is kotlin.\n
```

但对齐方式看起来不太优雅，原生字符串还可以通过 `trimMargin()` 函数去除每行前面的空格：

```kotlin
🏝️
val text = """
     👇 
      |Hi world!
    |My name is kotlin.
""".trimMargin()
println(text)
```

输出结果如下：

```
Hi world!
My name is kotlin.
```

这里的 `trimMargin()` 函数有以下几个注意点：

- `|` 符号为默认的边界前缀，前面只能有空格，否则不会生效
- 输出时 `|` 符号以及它前面的空格都会被删除
- 边界前缀还可以使用其他字符，比如 `trimMargin("/")`，只不过上方的代码使用的是参数默认值的调用方式

字符串的部分就先到这里，下面来看看数组与集合有哪些更方便的操作。

### 数组和集合

#### 数组与集合的操作符

在之前的文章中，我们已经知道了数组和集合的基本概念，其实 Kotlin 中，还为我们提供了许多使数组与集合操作起来更加方便的函数。

首先声明如下 `IntArray` 和 `List`：

```kotlin
🏝️
val intArray = intArrayOf(1, 2, 3)
val strList = listOf("a", "b", "c")
```

接下来，对它们的操作函数进行讲解：

- `forEach`：遍历每一个元素

  ```kotlin
  🏝️
  //              👇 lambda 表达式，i 表示数组的每个元素
  intArray.forEach { i ->
      print(i + " ")
  }
  // 输出：1 2 3 
  ```

  除了「lambda」表达式，这里也用到了「闭包」的概念，这又是另一个话题了，这里先不展开。

- `filter`：对每个元素进行过滤操作，如果 lambda 表达式中的条件成立则留下该元素，否则剔除，最终生成新的集合

  ```kotlin
  🏝️
  // [1, 2, 3]
        ⬇️
  //  {2, 3}
  
  //            👇 注意，这里变成了 List
  val newList: List = intArray.filter { i ->
      i != 1 // 👈 过滤掉数组中等于 1 的元素
  }
  ```

- `map`：遍历每个元素并执行给定表达式，最终形成新的集合

  ```kotlin
  🏝️
  //  [1, 2, 3]
         ⬇️
  //  {2, 3, 4}
  
  val newList: List = intArray.map { i ->
      i + 1 // 👈 每个元素加 1
  }
  ```

- `flatMap`：遍历每个元素，并为每个元素创建新的集合，最后合并到一个集合中

  ```kotlin
  🏝️
  //          [1, 2, 3]
                 ⬇️
  // {"2", "a" , "3", "a", "4", "a"}
  
  intArray.flatMap { i ->
      listOf("${i + 1}", "a") // 👈 生成新集合
  }
  ```

关于为什么数组的 `filter` 之后变成 `List`，就留作思考题吧~

这里是以数组 `intArray` 为例，集合 `strList` 也同样有这些操作函数。Kotlin 中还有许多类似的操作函数，这里就不一一列举了。

除了数组和集合，Kotlin 中还有另一种常用的数据类型： `Range`。

#### `Range`

在 Java 语言中并没有 `Range` 的概念，Kotlin 中的 `Range` 表示区间的意思，也就是范围。区间的常见写法如下：

```kotlin
🏝️
              👇      👇
val range: IntRange = 0..1000 
```

这里的 `0..1000` 就表示从 0 到 1000 的范围，**包括 1000**，数学上称为闭区间 [0, 1000]。除了这里的 `IntRange` ，还有 `CharRange` 以及 `LongRange`。

Kotlin 中没有纯的开区间的定义，不过有半开区间的定义：

```kotlin
🏝️
                         👇
val range: IntRange = 0 until 1000 
```

这里的 `0 until 1000` 表示从 0 到 1000，但**不包括 1000**，这就是半开区间 [0, 1000) 。

`Range` 这个东西，天生就是用来遍历的：

```kotlin
🏝️
val range = 0..1000
//     👇 默认步长为 1，输出：0, 1, 2, 3, 4, 5, 6, 7....1000,
for (i in range) {
    print("$i, ")
}
```

这里的 `in` 关键字可以与 `for` 循环结合使用，表示挨个遍历 `range` 中的值。关于 `for` 循环控制的使用，在本期文章的后面会做具体讲解。

除了使用默认的步长 1，还可以通过 `step` 设置步长：

```kotlin
🏝️
val range = 0..1000
//               👇 步长为 2，输出：0, 2, 4, 6, 8, 10,....1000,
for (i in range step 2) {
    print("$i, ")
}
```

以上是递增区间，Kotlin 还提供了递减区间 `downTo` ，不过递减没有半开区间的用法:

```kotlin
🏝️
//            👇 输出：4, 3, 2, 1, 
for (i in 4 downTo 1) {
    print("$i, ")
}
```

其中 `4 downTo 1` 就表示递减的闭区间 [4, 1]。这里的 `downTo` 以及上面的 `step` 都叫做「中缀表达式」，之后的文章会做介绍。

#### `Sequence`

在上一期中我们已经熟悉了 `Sequence` 的基本概念，这次让我们更加深入地了解 `Sequence` 序列的使用方式。

序列 `Sequence` 又被称为「惰性集合操作」，关于什么是惰性，我们通过下面的例子来理解：

```kotlin
🏝️
val sequence = sequenceOf(1, 2, 3, 4)
val result: List = sequence
    .map { i ->
        println("Map $i")
        i * 2 
    }
    .filter { i ->
        println("Filter $i")
        i % 3  == 0 
    }
👇
println(result.first()) // 👈 只取集合的第一个元素
```

- 惰性的概念首先就是说在「👇」标注之前的代码运行时不会立即执行，它只是定义了一个执行流程，只有 `result` 被使用到的时候才会执行

- 当「👇」的 `println` 执行时数据处理流程是这样的：

    - 取出元素 1 -> map 为 2 -> filter 判断 2 是否能被 3 整除
    - 取出元素 2 -> map 为 4 -> filter 判断 4 是否能被 3 整除
    - ...

    惰性指当出现满足条件的第一个元素的时候，`Sequence` 就不会执行后面的元素遍历了，即跳过了 `4` 的遍历。

而 `List` 是没有惰性的特性的：

```kotlin
🏝️
val list = listOf(1, 2, 3, 4)
val result: List = list
    .map { i ->
        println("Map $i")
        i * 2 
    }
    .filter { i ->
        println("Filter $i")
        i % 3  == 0 
    }
👇
println(result.first()) // 👈 只取集合的第一个元素
```

包括两点：

- 声明之后立即执行
- 数据处理流程如下：
    - {1, 2, 3, 4} -> {2, 4, 6, 8}
    - 遍历判断是否能被 3 整除

`Sequence` 这种类似懒加载的实现有下面这些优点：

- 一旦满足遍历退出的条件，就可以省略后续不必要的遍历过程。
- 像 `List` 这种实现 `Iterable` 接口的集合类，每调用一次函数就会生成一个新的 `Iterable`，下一个函数再基于新的 `Iterable` 执行，每次函数调用产生的临时 `Iterable` 会导致额外的内存消耗，而 `Sequence` 在整个流程中只有一个。

因此，`Sequence` 这种数据类型可以在数据量比较大或者数据量未知的时候，作为流式处理的解决方案。

### 条件控制

相比 Java 的条件控制，Kotlin 中对条件控制进行了许多的优化及改进。

#### `if/else`

首先来看下 Java 中的 `if/else` 写法：

```Java
☕️
int max;
if (a > b) {
    max = a;
} else {
    max = b;
}
```

在 Kotlin 中，这么写当然也可以，不过，Kotlin 中 `if` 语句还可以作为一个表达式赋值给变量：

```kotlin
🏝️
       👇
val max = if (a > b) a else b
```

另外，Kotlin 中弃用了三元运算符（条件 ? 然后 : 否则），不过我们可以使用 `if/else` 来代替它。

上面的 `if/else` 的分支中是一个变量，其实还可以是一个代码块，代码块的最后一行会作为结果返回：

```kotlin
🏝️
val max = if (a > b) {
    println("max:a")
    a // 👈 返回 a
} else {
    println("max:b")
    b // 👈 返回 b
}
```

#### `when`

在 Java 中，用 `switch` 语句来判断一个变量与一系列值中某个值是否相等：

```java
☕️
switch (x) {
    case 1: {
        System.out.println("1");
        break;
    }
    case 2: {
        System.out.println("2");
        break;
    }
    default: {
        System.out.println("default");
    }
}
```

在 Kotlin 中变成了 `when`：

```kotlin
🏝️
👇
when (x) {
   👇
    1 -> { println("1") }
    2 -> { println("2") }
   👇
    else -> { println("else") }
}
```

这里与 Java 相比的不同点有：

- 省略了 `case` 和 `break`，前者比较好理解，后者的意思是 Kotlin 自动为每个分支加上了 `break` 的功能，防止我们像 Java 那样写错
- Java 中的默认分支使用的是 `default` 关键字，Kotlin 中使用的是 `else`

与 `if/else` 一样，`when` 也可以作为表达式进行使用，分支中最后一行的结果作为返回值。需要注意的是，这时就必须要有 `else` 分支，使得无论怎样都会有结果返回，除非已经列出了所有情况：

```kotlin
🏝️
val value: Int = when (x) {
    1 -> { x + 1 }
    2 -> { x * 2 }
    else -> { x + 5 }
}
```

在 Java 中，当多种情况执行同一份代码时，可以这么写：

```kotlin
☕️
switch (x) {
    case 1:
    case 2: {
        System.out.println("x == 1 or x == 2");
        break;
    }
    default: {
        System.out.println("default");
    }
}
```

而 Kotlin 中多种情况执行同一份代码时，可以将多个分支条件放在一起，用 `,` 符号隔开，表示这些情况都会执行后面的代码：

```kotlin
🏝️
when (x) {
    👇
    1, 2 -> print("x == 1 or x == 2")
    else -> print("else")
}
```

在 `when` 语句中，我们还可以使用表达式作为分支的判断条件：

- 使用 `in` 检测是否在一个区间或者集合中：

  ```kotlin
  🏝️
  when (x) {
     👇
      in 1..10 -> print("x 在区间 1..10 中")
     👇
      in listOf(1,2) -> print("x 在集合中")
     👇 // not in
      !in 10..20 -> print("x 不在区间 10..20 中")
      else -> print("不在任何区间上")
  }
  ```

- 或者使用 `is` 进行特定类型的检测：

  ```kotlin
  🏝️
  val isString = when(x) {
      👇
      is String -> true
      else -> false
  }
  ```

- 还可以省略 `when` 后面的参数，每一个分支条件都可以是一个布尔表达式：

  ```kotlin
  🏝️
  when {
     👇
      str1.contains("a") -> print("字符串 str1 包含 a")
     👇
      str2.length == 3 -> print("字符串 str2 的长度为 3")
  }
  ```

当分支的判断条件为表达式时，哪一个条件先为 `true` 就执行哪个分支的代码块。

#### `for`

我们知道 Java 对一个集合或数组可以这样遍历：

```kotlin
☕️
int[] array = {1, 2, 3, 4};
for (int item : array) {
    ...
}
```

而 Kotlin 中 对数组的遍历是这么写的：

```kotlin
🏝️
val array = intArrayOf(1, 2, 3, 4)
          👇
for (item in array) {
    ...
}
```

这里与 Java 有几处不同：

- 在 Kotlin 中，表示单个元素的 `item` ，不用显式的声明类型
- Kotlin 使用的是 `in` 关键字，表示 `item` 是 `array` 里面的一个元素

另外，Kotlin 的 `in` 后面的变量可以是任何实现 `Iterable` 接口的对象。

在 Java 中，我们还可以这么写 `for` 循环：

```kotlin
☕️
for (int i = 0; i <= 10; i++) {
    // 遍历从 0 到 10
}
```

但 Kotlin 中没有这样的写法，那应该怎样实现一个 0 到 10 的遍历呢？

其实使用上面讲过的区间就可以实现啦，代码如下：

```kotlin
🏝️
for (i in 0..10) {
    println(i)
}
```

#### `try-catch`

关于 `try-catch` 我们都不陌生，在平时开发中难免都会遇到异常需要处理，那么在 Kotlin 中是怎样处理的呢，先来看下 Kotlin 中捕获异常的代码：

```kotlin
🏝️
try {
    ...
}
catch (e: Exception) {
    ...
}
finally {
    ...
}
```

可以发现 Kotlin 异常处理与 Java 的异常处理基本相同，但也有几个不同点：

- 我们知道在 Java 中，调用一个抛出异常的方法时，我们需要对异常进行处理，否则就会报错：

  ```java
  ☕️
  public class User{
      void sayHi() throws IOException {
      }
      
      void test() {
          sayHi();
          // 👆 IDE 报错，Unhandled exception: java.io.IOException
      }
  }
  ```

  但在 Kotlin 中，调用上方 `User` 类的 `sayHi` 方法时：

  ```kotlin
  🏝️
  val user = User()
  user.sayHi() // 👈 正常调用，IDE 不会报错，但运行时会出错
  ```

  为什么这里不会报错呢？因为 Kotlin 中的异常是不会被检查的，只有在运行时如果 `sayHi` 抛出异常，才会出错。

- Kotlin 中 `try-catch` 语句也可以是一个表达式，允许代码块的最后一行作为返回值：

  ```kotlin
  🏝️
             👇       
  val a: Int? = try { parseInt(input) } catch (e: NumberFormatException) { null }
  ```


#### `?.` 和 `?:`

我们在之前的文章中已经讲过 Kotlin 的空安全，其实还有另外一个常用的复合符号可以让你在判空时更加方便，那就是 Elvis 操作符 `?:` 。

我们知道空安全调用 `?.`，在对象非空时会执行后面的调用，对象为空时就会返回 `null`。如果这时将该表达式赋值给一个不可空的变量：

```kotlin
🏝️
val str: String? = "Hello"
var length: Int = str?.length
//                👆 ，IDE 报错，Type mismatch. Required:Int. Found:Int?
```

报错的原因就是 `str` 为 null 时我们没有值可以返回给 `length`

这时就可以使用 Kotlin 中的 Elvis 操作符 `?:` 来兜底：

```kotlin
🏝️
val str: String? = "Hello"
                             👇
val length: Int = str?.length ?: -1
```

它的意思是如果左侧表达式 `str?.length ` 结果为空，则返回右侧的值 `-1`。

Elvis 操作符还有另外一种常见用法，如下：

```kotlin
🏝️
fun validate(user: User) {
    val id = user.id ?: return // 👈 验证 user.id 是否为空，为空时 return 
}

// 等同于

fun validate(user: User) {
    if (user.id == null) {
        return
    }
    val id = user.id
}
```

看到这里，想必你对 Kotlin 的空安全有了更深入的了解了，下面我们再看看 Kotlin 的相等比较符。

#### `==` 和 `===`

我们知道在 Java 中，`==` 比较的如果是基本数据类型则判断值是否相等，如果比较的是 `String` 则表示引用地址是否相等， `String` 字符串的内容比较使用的是 `equals()` ：

```java
☕️
String str1 = "123", str2 = "123";
System.out.println(str1.equals(str2));
System.out.println(str1 == str2); 
```

Kotlin 中也有两种相等比较方式：

- `==` ：可以对基本数据类型以及 `String` 等类型进行内容比较，相当于 Java 中的 `equals`
- `===` ：对引用的内存地址进行比较，相当于 Java 中的 `==`

可以发现，Java 中的 `equals` ，在 Kotlin 中与之相对应的是 `==`，这样可以使我们的代码更加简洁。

下面再来看看代码示例：

```kotlin
🏝️
val str1 = "123"
val str2 = "123"
println(str1 == str2) // 👈 内容相等，输出：true

val str1= "字符串"
val str2 = str1
val str3 = str1
print(str2 === str3) // 👈 引用地址相等，输出：true
```

其实 Kotlin 中的 `equals` 函数是 `==` 的操作符重载，关于操作符重载，这里先不讲，之后的文章会讲到。

### 练习题

1. 请按照以下要求实现一个 `Student` 类：

   - 写出三个构造器，其中一个必须是主构造器
   - 主构造器中的参数作为属性
   - 写一个普通函数 `show`，要求通过字符串模板输出类中的属性

2. 编写程序，使用今天所讲的操作符，找出集合 {21, 40, 11, 33, 78} 中能够被 3 整除的所有元素，并输出。