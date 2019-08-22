## Kotlin 里那些「更方便的」

在上期内容当中，我们知道了 Kotlin 的那些与 Java 写法不同的地方。这一期我们再进阶一点，讲一讲 Kotlin 中那些「更方便的」用法。这些知识点在不知道之前，你也可以正常写 Kotlin，但是在熟悉之后能够让你写得更爽。

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

其实 Kotlin 中还有更简单的方法来写构造函数：

```kotlin
🏝️
               👇       
class User constructor(name: String) {
    //                  👇 这里与构造器中的 name 是同一个
    var name: String = name
}
```

这里有几处不同点：

- `constructor` 构造器移动到了类名之后
- 构造器中的参数 `name` ，被用在类的属性 `name` 中进行赋值操作

这个写法我们叫「主构造器 primary constructor」。与之相对的在第二篇中，我们写在类中的构造函数被称为「次构造器」。在 Kotlin 中一个类最多只有 1 个主构造器（可以没有），以及 0 到 N 个次构造器。

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

其中 `init` 代码块是紧跟在主构造器之后执行的。由于主构造器本身没有代码体，因此这里的 `init` 代码块就相当于主构造器的代码体。

另外，如果类中有主构造器，那么其他的次构造器都需要通过 `this` 关键字调用主构造器，可以直接调用或者通过别的次构造器间接调用。如果不调用 IDE 就会报错：

```kotlin
🏝️
class User constructor(var name: String) {
    constructor(name: String, id: Int) {
    // 👆这样写会报错，Primary constructor call expected
    }
}
```

上面的代码没有通过 `this` 关键字调用主构造器，就会提示需要调用主构造器。

为什么次构造器一定要调用主构造器呢？其实，主构造器不仅仅是写在类名之后这么简单，一旦在类中声明了主构造器，就包含两点：

- 必须性：创建类的对象时，不管使用哪个构造器，都需要主构造器的参与
- 第一性：在类的初始化过程中，首先执行的是主构造器

这也就是称之为主构造器的原因。

当一个类中同时有主构造器与次构造器的时候，应该这样写：

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

在使用次构造器创建对象时，`init` 代码块是先于次构造器执行的。我们可以做个比喻，如果把主构造器看成身体的头部，那么 `init` 代码块就是颈部，次构造器就相当于身体其余部分。

细心的你也许会发现这里又出现了 `:` 符号，它还在其他场合出现过，例如：

- 变量的声明：`var id: Int`
- 类的继承：`class MainActivity : AppCompatActivity() {}`
- 接口的实现：`class User : Impl {}`
- 匿名类的创建：`object: ViewPager.SimpleOnPageChangeListener() {}`

可以发现 `:` 符号在 Kotlin 中表示了一种依赖关系，在这里表示依赖于主构造器。

一般情况下，主构造器中的 `constructor` 关键字可以省略：

```kotlin
🏝️
class User(name: String) {
    var name: String = name
}
```

但有些场景，`constructor` 是不可以省略的，例如在主构造器上使用可见性修饰符或者注解：

- 可见性修饰符我们之前已经讲过，它修饰普通函数与修饰构造器的用法一样：

  ```kotlin
  🏝️
  class User private constructor(name: String) {
  //           👆 主构造器被修饰为私有的，外部就无法调用该构造器
  }
  ```

- 关于注解的知识点，我们之后会讲，这里就不展开了

看到这里，构造器的写法已经非常简单了，那么还会不会更简单呢？下面我们来看看，如何在主构造器中声明属性。

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

如果在主构造器的参数左边加上 `var` 或者 `val`，就等价于在类中创建了相同名称的属性（property），并且初始值就是主构造器中该参数的值。

以上讲了所有关于主构造器相关的知识，让我们再总结一下类的初始化写法：

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

- 将主构造器中的 `name` 与 `id` 取出，作为类的属性使用：

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

- 最后再添加一个参数为 `person` 的次构造器：

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

  当一个类有多个构造函数时，只需要把最基本、最通用的那个写成主构造器就好啦。这里我们选择将参数为 `name` 与 `id` 的构造函数作为主构造器。

到这里，整个类的初始化就完成了，并且该类的初始化顺序也与刚才从上到下的创建顺序相同。

构造器部分的知识就先讲到这里，其实普通函数也有许多更简单的写法，下面让我们来看看普通函数的那些「更方便的」。

### 函数简化

#### 使用 `=` 连接返回值

我们已经熟悉了 Kotlin 中函数的写法：

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

这里我们把大括号去掉，并在函数体前面使用 `=` 符号连接函数名，这就是使用 `=` 连接返回值。

我们之前讲过，Kotlin 有「类型推断」的特性，那么这里函数的返回类型还可以隐藏掉：

```kotlin
🏝️
                                👇
fun area(width: Int, height: Int) = width * height
```

不过，在实际开发中，还是推荐显式的将返回类型写出来，这样可以提升代码阅读效率。

以上是函数有返回值时的情况，那如果没有返回值时可不可以使用 `=` 进行连接呢？例如下面这段代码：

```kotlin
🏝️
fun sayHi(name: String) {
    println("Hi " + name)
}
```

我们知道 Kotlin 中当函数没有返回值时，它的返回类型为 `Unit`。那么这里的函数 `sayHi` 的返回类型就是 `Unit`，只不过被我们省略了。所以，这里我们同样可以省略大括号并使用 `=` 符号进行连接：

```kotlin
🏝️
                       👇
fun sayHi(name: String) = println("Hi " + name)
```

以上就是关于 `=` 连接返回值的概念，当函数中只有一行代码的时候，不管返回类型是什么，都可以省略大括号，并使用 `=` 符号进行连接。

对于 Java 中的方法重载，我们都不陌生，那 Kolin 中是否有更方便的重载方式呢？接下来我们看看 Kotlin 中的「参数默认值」。

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

这就等价于上面  Java 写的重载方法，当调用 `sayHi` 函数时，可以选择性的填参数或者不填：

```kotlin
🏝️
sayHi("kaixue.io") // 打印 Hi kaixue.io
sayHi() // 打印 Hi world
```

既然与重载函数的效果相同，那 Kotlin 中的参数默认值有什么好处呢？仅仅只是少写了一些代码吗？

其实在 Java 中，每个重载方法的内部实现可以各不相同，这就无法保证重载方法内部设计上的一致性，而 Kotlin 的参数默认值，就解决了这个问题。

再来看下面这段代码，这里有默认值的参数在无默认值参数的前面：

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

那应该怎样使用默认值进行调用时，这点 Kotlin 在设计上也考虑到了，我们可以使用命名参数来解决这个问题。

#### 命名参数

对于上面这个问题，我们可以这么写：

```kotlin
🏝️
fun sayHi(name: String = "world", age: Int) {
    ...
}
      👇   
sayHi(age = 21)
```

在调用函数时，显式地指定了参数 `age` 的名称，这就是「命名参数」。Kotlin 中的每一个函数都具备命名参数的特性。

如果一个函数有非常多的参数时：

```kotlin
🏝️ 
fun sayHi(name: String = "world", age: Int, isStudent: Boolean = true, isFat: Boolean = true, isTall: Boolean = true) {
    ...
}
```

一般会这么调用这个函数：

```kotlin
🏝️
sayHi("world", 21, false, true, false)
```

当看到后面一长串的布尔值时，很难分清楚每个参数的用处，可读性很差。还好有了命名参数，我们就可以写成这样：

```kotlin
🏝️
sayHi(name = "wo", age = 21, isStudent = false, isFat = true, isTall = false)
```

另外，与命名参数相对的一个概念被称为「位置参数」，也就是按位置顺序进行参数填写。

当一个函数被调用时，如果混用位置参数与命名参数，那么所有的位置参数都应该放在第一个命名参数之前。我们来看下面这个例子：

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

在 Kotlin 中还提供了一种简洁的方案用来减少重复的代码：嵌套函数，简单来讲就是一个函数可以在另一个函数的内部声明。首先来看下这段代码：

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

函数中检查参数这个部分有些冗余，我们又不想将这段逻辑作为一个单独的函数对外暴露，但这些重复逻辑看着又实在难受。这时可以使用嵌套函数，在 `login` 函数内部声明一个函数：

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

这里的 `illegalStr` 是通过参数的方式传进嵌套函数中的，其实完全没有这个必要，因为嵌套函数中可以访问在它外部的所有变量或常量，例如类中的属性、当前函数中的参数与变量等。我们稍加改进：

```kotlin
🏝️
fun login(user: String, password: String, illegalStr: String) {
    fun validate(value: String) {
        if (value.isEmpty()) {
                                              👇
            throw IllegalArgumentException(illegalStr)
        }
    }
    validate(user)
    validate(password)
}
```

这里省去了嵌套函数中的 `illegalStr` 参数，在该函数内直接使用外层函数 `login` 的参数 `illegalStr`。

上面 `login` 函数中的验证逻辑，其实还有另一种更简单的方式：

```kotlin
🏝️
fun login(user: String, password: String, illegalStr: String) {
    require(user.isNotEmpty()) { illegalStr }
    require(password.isNotEmpty()) { illegalStr }
}
```

其中用到的 lambda 表达式以及 Kotlin 提供给我们的 `require` 函数，这里先不做展开，之后的文章会介绍。

### 字符串

讲完了普通函数的简化写法，Kotlin 中字符串也有很多方便写法。

#### 字符串模板

在  Java 中，字符串与变量之间是使用 `+` 符号进行拼接的，Kotlin 中也是如此：

```kotlin
🏝️
val name = "world"
println("Hi " + name)
```

但是当变量比较多的时候，可读性会变差，写起来也比较麻烦。好在 Kotlin 还为我们提供了一种更加方便的方法：

```kotlin
🏝️
val name = "world"
           👇
println("Hi $name") // 👈 输出： Hi world
```

我们在字符串中以 `$` 符号开头引用了一个变量，最终运行结果会将该变量的值填入到字符串中，这就是「字符串模板」。

如果将这里填的变量换成表达式时：

```kotlin
🏝️
val name = "world"
println("Hi $name.length") // 👈 输出： Hi world.length
```

我们想要输出的是变量 `name` 的长度，但结果显然不符合预期。

这时可以使用 `$` 符号加上 `{}` 将任意表达式括起来：

```kotlin
🏝️
val name = "world"
            👇          👇
println("Hi ${name.length}") 
```

其实就跟四则运算的括号一样，提高语法上的优先级，而单个变量的场景可以省略 `{}`。最终运行结果会将 `{}` 中表达式的运算结果填入到字符串当中。

字符串模板还支持转义字符，比如使用转义字符 `\n` 进行换行操作或者 `\$` 输出 `$` 字符的字面值：

```kotlin
🏝️
                  👇 
val name = "world!\n"
println("Hi $name")

             👇
val price = "\$9.99"
println(price) // 输出：$9.99
```

字符串模板的用法对于我们 Android 工程师来说，一点都不陌生。首先，Gradle 所用的 Groovy 语言就已经有了这种支持：

```groovy
def name = "world"
println "Hi ${name}"
```

而且在 Android 的资源文件里，定义字符串也有类似用法：

```xml
<string name="hi">Hi %s</string> 
```

在 Java 代码中的使用方法：

```java
☕️
getString(R.id.hi, "world");
```

#### raw string (原生字符串)

Kotlin 中还允许使用一对 `"""` 将字符串括起来：

```kotlin
🏝️
val name = "world"
val myName = "kotlin"
           👇
val text = """
      Hi $name!
    my name is $myName.\n
"""
println(text)
```

这里有几个注意点：

- 转义字符在这里是不起作用的
- 最后输出的内容与写的内容完全一致，包括实际的换行
- `$` 符号引入变量也会生效

这就是「原生字符串」。输出结果如下：

```
      Hi world!
    my name is kotlin.\n
```

原生字符串还可以通过 `trimMargin()` 函数去除每行前面的空格：

```kotlin
🏝️
val text = """
     👇 
      |Hi world!
    |my name is kotlin.
""".trimMargin()
println(text)
```

这里的 `trimMargin()` 函数有以下几个注意点：

- `|` 符号为默认的边界前缀，前面只能有空格，否则不会生效
- 输出时除了 `|` 符号前面的空格会被删除， `|` 符号本身也不会显示
- 边界前缀还能使用其他字符，比如 `trimMargin("/")`，上方的代码使用的是参数默认值的调用方式

输出结果如下：

```
Hi world!
my name is kotlin.
```

至此，我们了解了 Kotlin 中字符串的方便写法，下面我们来看看数组与集合有哪些更方便的操作。

### 数组和集合

#### 数组与集合的操作符

在之前的文章中，我们已经知道了数组和集合的基本概念，其实 Kotlin 中，还为我们提供了许多使数组与集合操作起来更加方便的函数，下面让我们来看看吧。

首先声明了如下数组与集合：

```kotlin
🏝️
val intArray = intArrayOf(1, 2, 3) // Int 类型数组
val strList = listOf("a", "b", "c") // List<String> 集合
```

接下来，对它们的操作函数进行讲解：

- `forEach`：遍历每一个元素

  ```kotlin
  🏝️
  //              👇 lambda 表达式，i 表示传进来的 Int 类型参数
  intArray.forEach { i ->
      print(i + " ") // 输出：1 2 3 
  }
  ```

- `filter`：对每个元素进行过滤操作，如果 lambda 表达式中的条件成立则留下该元素，否则剔除，最终生成新的集合

  ```kotlin
  🏝️
  // [1, 2, 3]
        ⬇️
  //  {2, 3}
  
  intArray.filter { i ->
      i != 1 // 👈 过滤掉数组中等于 1 的元素
  }
  ```

- `map`：遍历每个元素并与给定表达式进行运算，最终形成新的集合

  ```kotlin
  🏝️
  //  [1, 2, 3]
         ⬇️
  //  {2, 3, 4}
  
  intArray.map { i ->
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


这里是以数组 `intArray` 为例，集合 `strList` 也同样有这些操作函数。Kotlin 中还有许多类似的操作函数需要你去发现与学习，这里就不一一列举了。

在开发中，数组与集合的操作符会被经常用到，下面我们再看看另外一种常用数据处理类型： `Range`。

#### `Range`

在 Java 语言中并没有 `Range` 的概念，Kotlin 中的 `Range` 表示区间的意思，也就是范围。区间的常见写法如下：

```kotlin
🏝️
              👇      👇
val range: IntRange = 0..1000 
```

这里的 `0..1000` 就表示从 0 到 1000 的范围，包括 1000，数学上称之为闭区间 [0,1000]。除了这里的 `IntRange` ，还有 `CharRange` 以及 `LongRange`。

既然有闭区间，那有没有开区间呢？其实 Kotlin 中没有纯开区间的定义，不过有半开区间的定义：

```kotlin
🏝️
                         👇
val range: IntRange = 0 until 1000 
```

这里的 `0 until 1000` 表示从 0 到 1000，但不包括 1000，这就是半开区间 [0,1000) 。

另外，还可以对区间进行遍历操作：

```kotlin
🏝️
val range = 0..1000
//     👇 默认步长为 1，输出 0, 1, 2, 3, 4, 5, 6, 7....1000,
for (i in range) {
    print("$i, ")
}
```

这里的 `in` 关键字可以与 `for` 循环结合使用，表示取出从 0 到 1000 的整数。关于 `for` 循环控制的使用，这里先不展开，在本期文章的后面会做具体讲解。

除了使用默认的步长 1，还可以通过 `step` 设置步长：

```kotlin
🏝️
val range = 0..1000
//                👇 步长为 2，输出 0, 2, 4, 6, 8, 10,....1000,
for (i in range step 2) {
    print("$i, ")
}
```

以上是递增区间，Kotlin 还提供了递减区间 `downTo` :

```kotlin
🏝️
//            👇 输出 4, 3, 2, 1, 
for (i in 4 downTo 1) {
    print("$i, ")
}
```

其中 `4 downTo 1` 就表示递减的闭区间 [4,1]。这里的 `downTo` 以及上面的 `step` 都叫做中缀表达式，这里先不展开，之后的文章会做介绍。

上面我们了解到了 `Range` 就像是一个自动化的数数工具，而下面要讲的 `Sequence` 就像是一种「广度优先遍历」版本的集合类。

#### `Sequence`

在上一期中我们已经熟悉了 `Sequence` 的基本概念，这次让我们更加深入的了解 `Sequence` 序列的使用方式。

序列 `Sequence` 又被称为「惰性集合操作」，关于什么是惰性，我们先来看看下面的例子：

```kotlin
🏝️
val sequence = sequenceOf(1, 2, 3, 4)
val result = sequence
    .map{ i ->
        println("Map $i")
        i * 2 
    }.filter { i ->
        println("Filter $i")
        i % 3  == 0 
    } 

// 上面这段代码运行时不会立即执行，不过它的运算顺序如下：
// 1 -> 2 -> 判断 2 是否能被 3 整除
// 2 -> 4 -> 判断 4 是否能被 3 整除
// ...
// 也就是说 sequence 首先取出一个元素进行 map 操作，紧跟着进行 filter 操作

// 我们再来看看下面这段代码中 list 的运算顺序
// {1, 2, 3, 4} -> {2, 4, 6, 8} -> 逐一判断是否能被 3 整除
// 可以发现 list 集合操作是先将所有元素进行 map 操作，再将新的集合中元素逐一进行 filter 操作

val list = listOf(1, 2, 3, 4)
val result = list
    .map{ i ->
        println("Map $i")
        i * 2 
    }.filter { i ->
        println("Filter $i")
        i % 3  == 0 
    } 
```

所以 `sequence` 序列的遍历顺序就像是「广度优先遍历」。并且运算上面的 `sequence` 代码会发现什么也没有输出，这意味着序列的 `map` 与 `filter` 操作不是立即执行的。这样懒加载的实现有什么好处呢？

在 Kotlin 中， `Iterable` 每调用一次函数就会生成一个新的 `Iterable`，下一个函数再基于新的 `Iterable` 执行，每次函数调用产生的临时 `Iterable` 会导致额外的内存消耗，而 `Sequence` 就避免了这样的问题。

我们再通过 `first` 函数取出第一个元素试试：

```kotlin
🏝️
val sequence = sequenceOf(1, 2, 3, 4)
val result = sequence
    .map{ i ->
        println("Map $i")
        i * 2 
    }.filter { i ->
        println("Filter $i")
        i % 3  == 0 
    } 
//               👇 取出运算结果中的第一个元素
println(result.first())
```

最终运行结果如下：

```
Map 1
Filter 2
Map 2
Filter 4
Map 3
Filter 6
6
```

可以看出，`map` 与 `filter` 甚至都没有完全执行结束就停止了，当序列找到第一个满足 `it % 3  == 0` 的元素时就会停止并输出，这样就很好的省去后面不必要的计算。

以上我们对 `Sequence` 的懒加载与遍历函数执行顺序有了进一步的了解。这种数据类型可以在数据量比较大或者数据量未知的时候，可以为我们提供方便的流式处理方案。

### 条件控制

相比 Java 的条件控制，Kotlin 中对条件控制进行了许多的优化及改进，下面让我们来看看吧。

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

在 Kotlin 中，也可以这么写：

```kotlin
🏝️
var max: Int
if (a > b) {
    max = a
} else {
    max = b
}
```

不过，Kotlin 中 `if` 语句还可以作为一个表达式赋值给变量：

```kotlin
🏝️
       👇
val max = if (a > b) a else b
```

这里使用 `=` 符号连接了 `max` 变量与 `if` 语句，当 `a > b` 时 `max` 等 `a`，否则 `max` 等于 `b`。

Kotlin 中弃用了三元运算符（条件 ? 然后 : 否则），不过我们可以使用 `if/else` 来替换它。

上面的 `if/else` 的分支中是一个变量，另外还可以是一个代码块，代码块的最后一行会作为结果返回：

```kotlin
🏝️
val max = if (a > b) {
    println("max:a") // todo  ln 统一
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

在 Kotlin 中虽然没有 `switch` 语句，但可以使用 `when` 语句进行判断：

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

- Java 中使用的是 `switch` 关键字，Kotlin 中使用的是 `when` 关键字
- Java 中使用 `case 1:` 判断是否相等，Kotlin 中是使用 `1 ->` 进行判断
- Java 中有 `break` 关键字，表示执行结束，而 Kotlin 没有该关键字，但某个分支执行完就会结束 `when` 的执行
- Java 中的默认分支使用的是 `default` 关键字，Kotlin 中使用的是 `else`

与 `if/else` 一样，`when` 也可以作为表达式进行使用，分支中最后一行的结果作为返回值。需要注意的是，这时就必须要有 `else` 分支，使得无论怎样都会有结果返回，除非已经列出了所有情况。

在 Java 中，当多种情况执行同一份代码时：

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
     👇
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

而 Kotlin 中是这么写的：

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

另外，Kotlin 的 `in` 后面的变量可以是任何实现了 `iterator` 迭代器接口的对象。

在 Java 中，我们一般还可以这么写 `for` 循环：

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
  user.sayHi() // 👈 正常调用，IDE 不会报错，但运行时会报错
  ```

  为什么这里不会报错呢？因为 Kotlin 中的异常是不会被检查的，只有在运行时才会出错。

- Kotlin 中 `try-catch` 语句也可以是一个表达式，允许代码块的最后一行作为返回值：

  ```kotlin
  🏝️
             👇       
  val a: Int? = try { parseInt(input) } catch (e: NumberFormatException) { null }
  ```


#### `?.` 和 `?:`

我们在之前的文章中已经讲过 Kotlin 的空安全，其实还有另外一个常用的复合符号可以让你在判空时更加方便，那就是 Elvis 操作符 `?:` 。

我们知道空安全调用 `?.`，在对象非空时会执行后面的调用，对象为空时就会返回 `null`。如果这时将该表达式赋值给赋值给一个不可空的变量：

```kotlin
🏝️
val str: String? = "Hello"
var length: Int = str?.length
//                👆 ，IDE 报错，Type mismatch. Required:Int. Found:Int?
```

这里想要获取到可空变量 `str` 的长度，并赋值给不可空变量 `length`，IDE 就会提示类型不匹配。该怎么办呢？

这时就可以使用 Kotlin 中的 Elvis 操作符 `?:` 来解决：

```kotlin
🏝️
val str: String? = "Hello"
                             👇
val length: Int = str?.length ?: -1
```

这里的 `?:` 就表示如果左侧表达式 `str?.length ` 结果为空，则返回右侧的值 `-1`。

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

看到这里，想必你对 Kotlin 的空安全有了更深入的了解了。下面我们再看看 Kotlin 的相等比较符。

#### `==` 和 `===`

我们知道在 Java 中，`==` 如果是比较的基本数据则判断值是否相等，如果用在 `String` 字符串上则表示引用地址是否相等， `String` 字符串的内容比较则使用的是 `equals()` ：

```java
☕️
String str1 = "123", str2 = "123";
System.out.println(str1.equals(str2)); // 判断内容是否相等 输出 true
System.out.println(str1 == str2); // 判断引用地址是否相等  输出 false
```

Kotlin 中也有两种相等比较方式：

- `==` ：可以对基本数据类型以及 `String` 等进行内容比较，相当于 Java 中的 `equals`
- `===` ：对引用的内存地址进行比较，相当于 Java 中的 `==`

其中 `equals` 换成了 `==`，这样可以使我们的代码更加简洁。

下面再来看看代码示例：

```kotlin
🏝️
val str1 = "123"
val str2 = "123"
println(str1 == str2) // 输出：true

val str1= "字符串"
val str2 = str1
val str3 = str1
print(str2 === str3) //输出：true
```

其实 Kotlin 中的 `equals` 函数是 `==` 的操作符重载，关于操作符重载，这里先不做展开，之后的文章会讲到。

### 练习题

1. 请按照以下要求实现一个 `Student` 类：

   - 写出三个构造函数，其中一个必须是主构造器
   - 主构造器中的参数作为属性
   - 写一个普通函数 `show`，要求通过字符串模板输出类中的属性

   最后使用次构造器创建 `Student` 对象，并调用 `show` 函数。

2. 编写程序，使用今天所讲的操作符，找出数组 [21,40,11,33,78] 中能够被 3 整除的所有元素，并输出。