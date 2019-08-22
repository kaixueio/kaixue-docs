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

- 可见性修饰符我们之前已经讲过，它修饰普通函数和修饰构造器的用法一样：

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

在 Kotlin 中，这种只有一行代码的函数，还可以这么写：

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

我们知道 Kotlin 中当函数没有返回值时，它的返回类型为 `Unit`。那么这里的函数 `sayHi` 的返回类型就是 `Unit`，只不过被我们省略了。所以，我们同样可以省略大括号并使用 `=` 符号进行连接：

```kotlin
🏝️
                       👇
fun sayHi(name: String) = println("Hi " + name)
```

以上就是关于 `=` 连接返回值的概念，当函数中只有一行代码的时候，不管返回类型是什么，都可以省略大括号，并使用 `=` 符号进行连接。

对于 Java 中的方法重载，我们都不陌生，那 Kolin 中是否有更方便的重载方式呢？接下来我们看看 Kotlin 中的「参数默认值」。

#### 参数默认值

我们都知道，Java 允许在一个类中定义多个名称相同的方法，但是参数的类型或个数必须不同，这就是方法的重载：

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

这就等价于上面  Java 写的重载函数，当调用 `sayHi` 函数时，可以选择性的填参数或者不填：

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

在调用函数时，我们显式地指定了参数 `age` 的名称，这就是「命名参数」。Kotlin 中的每一个函数都具备命名参数的特性。

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

当看到后面一长串的布尔值时，很难分清楚每个参数的用处，可读性很差。还好有了命名参数，我们就可以这样写：

```kotlin
🏝️
sayHi(name = "wo", age = 21, isStudent = false, isFat = true, isTall = false)
```

另外，与命名参数相对的一个概念被称为「位置参数」，也就是按位置顺序进行参数填写。

当一个函数被调用时，如果混用位置函数与命名参数，那么所有的位置参数都应该放在第一个命名参数之前。我们来看下面这个例子：

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

这里我们将共同的验证逻辑放进了嵌套函数 `validate` 中，并且 `login` 函数之外的其他地方是访问不到这个嵌套函数的。

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

上面 `login` 函数中的验证逻辑，其实还有一种更简单的方式：

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

但是参数比较多的话，可读性会变差，写起来也比较麻烦。好在 Kotlin 还为我们提供了一种更加方便的方法：

```kotlin
🏝️
val name = "world"
           👇
println("Hi $name") // 👈 输出： Hi world
```

我们在字符串中以 `$` 符号开头引用了一个变量，最终运行结果会将该变量的值填入到字符串中，这就是「字符串模板」。

如果填的变量换成表达式时：

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

其实就跟四则运算的括号一样，提高语法上的优先级，而单个变量的场景可以省略 `{}`。这里最终运行结果会将 `{}` 中表达式的运算结果填入到字符串当中。

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

字符串模板的用法对于我们 Android 工程师来说，一点都不陌生。首先，Gradle 所用的 Groovy 语言本来就已经有了这种支持：

```groovy
def name = "world"
println "Hi ${name}"
```

而且在 Android 资源文件里，定义字符串也有类似用法：

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

这就是「原生字符串」：

- 转义字符在这里不起作用
- 最后输出时，与写的内容完全一致，包括实际的换行
- `$` 符号引入变量也会生效

输出结果如下：

```
      Hi world!
    my name is kotlin.\n
```

原生字符串还可以通过 `trimMargin()` 函数去除每行前面的空格：

```kotlin
🏝️
val text = """
//   👇 相当于每一行都是从这里开始输出，但前面必须是空格
      |Hi world!
    |my name is kotlin.
""".trimMargin()
println(text)
```

这里的 `trimMargin()` 函数有以下几个注意点：

- `|` 符号为默认的边界前缀，前面只能有空格，否则不会生效
- 输出时除了 `|` 符号前面的空格会被删除， `|` 符号本身也会删除
- 还能使用其他字符，比如 `trimMargin("/")`，上方的代码使用的是参数默认值方式

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

- `map`：遍历每个元素并与给定表达式进行运算，转换形成新的集合

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

在开发中，我们会经常用到上面的数组与集合，下面我们看看另外一种常见数据处理类型： `Range`。

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
// 也就是说 sequence 首先取出一个元素进行 map 操作后，紧跟着进行 filter 操作

// 我们再来看看下面这段 list 运算顺序
// {1, 2, 3, 4} -> {2, 4, 6, 8} -> 逐一是否能被 3 整除
// 可以发现集合操作是先将所有元素进行 map 操作，再将新的集合中元素逐一进行 filter 操作

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

所以 `sequence` 序列的遍历顺序就像是「广度优先遍历」。

并且运算上面的 `sequence` 代码会发现什么也没有输出，这意味着序列的 `map` 与 `filter` 操作不是立即执行的。这样懒加载的实现有什么好处呢？

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

讲了 Kotlin 中这么多方便的用法，接下来我们再看看最基础的条件控制有没有什么简单写法吧。

#### `if/else` 和 `when`

相比 Java 的条件控制，Kotlin 中对条件控制进行了许多的优化及改进，首先看下在 Java 中的 `if/else` 写法：

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

不过，Kotlin 中 `if` 还可以作为一个表达式，返回一个值：

```kotlin
🏝️
       👇
val max = if (a > b) a else b
```

这里使用 `=` 符号连接了 `max` 变量与 `if` 判断，这就表示 `if/else` 作为一个表达式，当 `a>b` 时返回结果为 `a` 的值，否则返回 `b` 的值。因此 Kotlin 中不需要三元运算符（条件 ? 然后 : 否则）。

另外 `if/else` 中的分支也可以是代码块，代码块的最后一行会作为返回结果：

```kotlin
🏝️
val max = if (a > b) {
    print("max:a")
    a // 👈 返回 a 的值
} else {
    print("max:b")
    b // 👈 返回 b 的值
}
```

以上就是 Kotlin 中 `if/else` 的用法，那有没有 `switch` 语句呢？其实在 Kotlin 中用 `when` 代表了 Java 中的 `switch` 语句：

```kotlin
🏝️
👇
when (x) {
   👇
    1 -> print("1")
    2 -> print("2")
   👇
    else -> {
        print("else")
    }
}
```

这里的 `when` 将它的参数与每个条件进行比较，直到遇到合适的分支，否则会走默认的 `else` 分支，并且分支的代码块在执行完之后就会结束 `when` 的执行。

`when` 也可以作为表达式进行使用，符合条件的分支中的最后一行的结果作为返回值。但需要注意的是，这时就必须要有 `else` 分支，使得表达式无论怎样都会有返回结果，除非编译器能够检测出已经将所有的情况覆盖。

如果多个分支都是相同的代码块的话，可以将多个分支条件放在一起，用 `,` 符号隔开：

```kotlin
🏝️
when (x) {
    👇
    1, 2 -> print("x == 1 or x == 2")
    else -> print("else")
}
```

在 `when` 中我们也可以使用任意表达式作为分支的判断条件：

```kotlin
🏝️
when (x) {
    👇
    parseInt(str) -> print("字符串 str 的值与 Int 值 x 相同")
    else -> print("没有相同值")
}
```

也可以使用 `in` 检测是否在一个区间或者集合中：

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

或者使用 `is` 进行特定类型的检测：

```kotlin
🏝️
val isString = when(x) {
    👇
    is String -> true
    else -> false
}
```

还可以用来替代 `if/else if` 链，也就是不提供参数，每一个分支条件都可以是一个布尔表达式，当找到第一个分支为真时就会执行该分支：

```kotlin
🏝️
when {
   👇
    str.contains("a") -> print("字符串 str 包含 a")
   👇
    str.length == 3 -> print("字符串 str 的长度为 3")
}
```

至此，相信你对 `if/else` 与 `when` 相关的知识了解得差不多了，下面再来看看 `for` 循环控制的使用方法。

#### `for`

在 Kotlin 中，`for` 循环操作与 Java 的 `for` 相比也进行了许多优化，Kotlin 中 `for` 循环可以对任何提供迭代器（iterator）的对象进行遍历：

```kotlin
🏝️
          👇
for (item in collection) {
    print(item)
}
```

这里的的 `in` 操作符与 `for` 循环结合，不断的从 `collection` 中取出子元素赋值给 `item` 变量，从而实现对该对象的遍历读取操作。

还可以对常见的数字区间进行迭代，代码如下：

```kotlin
🏝️
          👇
for (i in 1..10) {
    println(i)
}
for (i in 10 downTo 1 step 2) {
    println(i)
}
```

如果想要通过索引的方式遍历一个数组或者集合，可以这样写：

```kotlin
🏝️
                👇
for (i in array.indices) {
    println(array[i])
}
```

`indices` 函数会返回该数组或者集合的有效索引的区间，并逐一赋值到 `i` 中进行遍历。

还可以使用 `withIndex` 函数，同时获取索引值与元素值：

```kotlin
🏝️
          👇                    👇
for ((index, value) in array.withIndex()) {
    println("index: $index , value: $value")
}
```

这里的 `withIndex` 函数会返回一个包含索引值与元素值的迭代器对象，其中 `(index, value)` 是 Kotlin 中的解构声明，该部分我们会在之后的文章进行讲解，这里我们只需要明白 `index` 与 ` value` 分别用来承载单个元素的索引值与元素值即可。

#### `try-catch`

关于 `try-catch` 我们都不陌生，在平时开发中难免都会遇到异常需要处理，那么在 Kotlin 中是怎样处理的呢，先来看下 Kotlin 捕获异常的代码：

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

可以发现 Kotlin 异常处理与 Java 的异常处理基本相同，但在使用上有几个不同点：

- Kotlin 中 `try` 也可以是一个表达式，允许代码块的最后一行作为返回值：

  ```kotlin
  🏝️
             👇          👇                                                👇
  val a: Int? = try { parseInt(input) } catch (e: NumberFormatException) { null }
  ```

- Kotlin 中的异常都是不受检查的异常，什么意思呢？

  - 受检查的异常：必须在函数上定义并且需要处理的异常，比如 Java 中的 `IOException`
  - 不受检查的异常：不是必须进行处理的，比如 `NullPointerException`

#### `?:` 和 `?.`

我们在之前的文章中已经讲过 Kotlin 的空安全，其实还有另外一个常用的复合符号可以让你在判空时更加方便，那就是 Elvis 操作符 `?:` 。

我们先来看下这段代码：

```kotlin
🏝️
val str: String? = "Hello"
val length = if (str != null) str.length else -1
```

这里的 `str` 是一个可空的字符串对象，我们想要获取它的长度时先判断是否为空，不为空则获取 `str.length` ，为空则返回 `-1` 。

其实还有一种更简单的方法来写判空操作，即通过 Elvis 操作符 `?:` 再结合空安全调用 `?.` 实现：

```kotlin
🏝️
val str: String? = "Hello"
                        👇
val length = str?.length ?: -1
```

这里的 `str?.length` 使用了空安全调用 `?.` ，表示 `str` 为空时结果为 `null`，非空时获取 `length`。其中 `?:` 表示若左侧表达式为空，则返回其右侧表达式的值 `-1`，否则返回左侧表达式的值。

 Elvis 操作符右侧还可以是一个表达式，例如：

```kotlin
🏝️
fun validate(user: User) {
    val id = user.id ?: return
    val name = user.name ?: throw IllegalArgumentException("name is null")
}
```

这里的 `return` 与 `throw` 都是在左侧表达式为空时执行。

看到这里，想必你对 Kotlin 的空安全有了更深入的了解了。下面我们再看看 Kotlin 的相等比较符。

#### `==` 和 `===`

我们知道在 Java 中相等性比较可以使用 `==` ，如果是比较的 `int` 类型则判断数值是否相等，如果用在 `String` 字符串上则表示引用地址是否相等， `String` 字符串的内容比较则使用的是 `equals()` ：

```java
☕️
String str1 = "123", str2 = "123";
System.out.println(str1.equals(str2)); // 判断内容是否相等 输出 true
System.out.println(str1 == str2); // 判断引用地址是否相等  输出 false
```

那 Kotlin 中的相等性比较是怎样的呢？首先我们来看看所有类的基类 `Any` 中 `equals` 函数的定义：

```kotlin
🏝️
public open operator fun equals(other: Any?): Boolean
```

其中 `open operator` 表示重载操作符，这里先不展开，后面会讲到。

这里的 `equals` 表示重载了 `==` 操作符，也就是说 Kotlin 中 `==` 等同于 `equals` 函数，且每个类需要自己实现 `equals` 函数，否则默认比较的是内存地址。诸如 `String`、`Date` 等类都对 `equals` 方法进行了重写，比较的是对象的值。

因此，Kotlin 中对基本数据类型或者 `String` 类型都使用 `==` 进行值的比较，例如：

```kotlin
🏝️
val str1 = "123"
val str2 = "123"
println(str1 == str2) // 输出结果为 true
```

那该如何对基本类型或者重写了 `equals` 函数的类进行内存地址的比较呢？Kotlin 还给我们提供了一个比较内存地址的操作符 `===` ，例如：

```kotlin
🏝️
val str1= "字符串"
val str2 = str1
val str3 = str1
print(str2 === str3)
```

这里比较的就是字符串的内存地址， 输出结果为：`true`。

我们再来看另一段示例代码：

```kotlin
🏝️
val str1: String? = "123"
val str2: String? = "abc"
str1 == str2

//等价于：

str1?.equals(str2) ?: (str2 === null)
```

其中 `str1` 与 `str2` 都是可空的 `String` 类型，`str1 == str2` 的执行过程首先是判断 `str1` 是否为空，不为空则调用 `equals` 比较值是否与 `str2` 相等，如果 `str1` 为空则执行右侧表达式 `str2 === null`，这时 `str2` 为空则表达式为 `true`，否则表达式结果为 `false`。

以上就是 Kotlin 中相等性比较，我们做个总结：

- `==` ：如果作用于基本数据类型，则比较其中的值是否相等。如果比较的是类的对象，则比较内存地址是否相等，除非该类重写了 `equals` 函数，例如 `String`、`Date` 等。
- `===` ：比较的是引用的内存地址是否相等。

### 练习题

好了，以上就是关于 Kotlin 里那些「更方便的」，给你留两道练习题吧：

1. 请按照以下要求实现一个 `Student` 类：

   - 要求有一个包含 `name` 与 `age` 这两个参数的主构造器，并将该主构造器中的参数取出作为类的属性
   - 在 `init` 代码块中打印 `I am a student.`
   - 要求有一个包含  `name` 与 `age` 以及 `school` 这三个参数的次构造器，并在其中输出学校信息
   - 再写一个 `show` 函数，将 `name` 与 `age` 属性通过字符串模板输出

   最后使用次构造器创建 `Student` 对象，并调用 `show` 函数。

2. 请编写程序，找出数组 {21,30,11,44,78}  中能够被 3 整除的值，并输出，要求使用今天所讲的数组操作符。