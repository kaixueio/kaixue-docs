## Kotlin 里那些「更方便的」

在上期课程当中，我们知道了 Kotlin 的那些与 Java 写法不同且需要注意的知识点。这一期我们再进阶一点，讲一讲 Kotlin 中那些「更方便的」。这些知识点在不知道之前，你也可以正常写 Kotlin，但是在熟悉之后能够让你写得更爽。

### 构造器

#### 主构造函数

上期我们了解了 Kotlin 中 constructor 的写法：

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
                       👇
    var name: String = name
}
```

这里有几处不同点：

- `constructor` 构造函数写在了类的头部，放在类名之后
- 构造函数中的参数 `name` ，被用在类的属性 `name` 中进行赋值操作

这就是「主构造器 primary constructor」。上一期中已经讲过关于 constructor 的概念，而那些写在类中的构造函数被称为「次构造函数」。在 Kotlin 中一个类只允许有一个主构造器，以及一个或者多个次构造函数。

主构造器中的参数除了可以在属性中使用，还可以在类的 `init` 代码块中使用：

```kotlin
🏝️
class User constructor(name: String) {
    var name: String
    init {
                     👇
        this.name = name
    }
}
```

其中 `init` 代码块的执行时机是紧随在主构造器之后的，可以在其中写一些初始化逻辑，这就相当于主构造器的代码块。

另外，如果类中有一个主构造器，那么其他次构造函数都需要通过 `this` 关键字委托给主构造器，可以直接委托或者通过别的次构造函数间接委托。如果不委托会怎样呢：

```kotlin
🏝️
class User constructor(var name: String) {
    constructor(name: String, id: Int) {
    // 👆这样写会报错，Primary constructor call expected
    }
}
```

这里的没有通过 `this` 关键字进行委托，就会报错提示需要调用主构造器。为什么非得需要委托呢？因为对象的初始化过程必须要有主构造器的参与，如果次构造函数没有委托给主构造器，在初始化过程中是缺少参数的，也就是非法的。

次构造函数的正确的写法是这样的：

```kotlin
🏝️
class User constructor(var name: String) {
                                         👇
    constructor(name: String, id: Int) : this(name) {
    }
}
```

可以发现，我们通过 `this` 关键字将次构造函数中的 `name` 作为参数调用了主构造器。如果这时候类中还有 `init` 初始化代码块的话，由于初始化代码块实际也是主构造器的一部分，所以初始化代码块是在次构造函数之前执行的。

除此之外，主构造器也可以有注解或者可见性修饰符，同样是放在 `constructor` 前面，如果不需要注解或可见性还可以省略 `constructor` 关键字：

```kotlin
🏝️
         👇
class User(name: String) {
    var name: String = name
}
```

看到这里，我们会发现一开始那段五六行的 `User` 类，已经简化到还有三行。那么，还有没有更简单的写法呢？下面我们来看看，如何在主构造器中声明属性，把上面这段三行的代码简化到只剩一行！

#### 主构造器里声明属性

上一小节我们了解了主构造函数的概念。其实在 Kotlin 中，还支持将主构造函数中的参数作为该类的属性：

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

如果在主构造器的参数左边加上 `var` 或者 `val`，就等价于在类中创建了相同名称的属性（property），并且初始值就是构造函数中该参数的值。

以上讲了所有主构造器相关的知识，我们来做个总结吧。如果有一个 `User` 类：

```kotlin
🏝️
class User {
}
```

首先我们需要一个有 `name` 与 `id` 为参数的构造函数，我们可以直接将该构造函数作为主构造器：

```kotlin
🏝️
class User(name: String, id: String) {
    init {
        ...
    }
}
```

其中 `init` 代码块可以有选择性的使用。这时候需要将构造函数中的 `name` 与 `id` 取出，作为类的属性使用，可以通过主构造器进行声明：

```kotlin
🏝️
class User(var name: String, var id: String) {
    init {
        ...
    }
}
```

如果需要再加入一个参数为 `person` 的构造函数的话，那么这时应该把两个构造函数中的哪个写成 primary 的呢？其实很简单，只要把最基本、最通用的那个写成主构造器就好啦，并且 primary constructor 会参与任何一个 constructor 创建对象的初始化过程。

这里我们选择把有 `name` 与 `id` 为参数的构造函数作为主构造器，有 `person` 为参数的构造函数作为次构造函数，可以写成这样：

```kotlin
🏝️
class User(var name: String, var id: String) {
    constructor(person: Person) : this(person.name, person.id) {
    }
}
```

这时一个有着两个构造函数，以及多个属性的 `User` 类仅仅几行就写完啦，是不是非常方便。以上就是对主构造器所有概念的回顾，并且最终的调用执行顺序也与刚才我们书写的顺序相同。

至此，我们对构造函数中方便的写法有了一定了解，其实普通函数也有许多更简单的写法，下面让我们来看看普通函数的那些「更方便的」。

### 函数简化

#### 使用 `=` 连接返回值

在之前的课程中，我们已经了解了 Kotlin 中函数的写法：

```kotlin
🏝️
fun sayHi(name: String) {
    println("Hi " + name)
}
```

这是一个非常简单的函数，获取到参数 `name` 值并输出，函数中只有一行代码，还可以这么写：

```kotlin
🏝️
                       👇
fun sayHi(name: String) = println("Hi " + name)
```

这里我们把大括号去掉，并在函数体前面使用 `=` 符号连接函数名。在之前文章中，我们讲过 Kotlin 的 `Unit` 返回类型，那么这里的 `sayHi` 函数的返回类型就是 `Unit`，只不过被我们省略了。

那再来看看返回其他类型的函数，该怎么使用 `=` 符号进行连接：

```kotlin
🏝️
fun area(width: Int, height: Int): Int {
    return width * height
}
```

当函数返回单个表达式时，我们同样可以省略大括号并使用 `=` 符号进行连接：

```kotlin
🏝️
fun area(width: Int, height: Int): Int = width * height
```

我们知道，Kotlin 有「类型推断」的特性，那么这里函数的返回类型还可以隐藏掉：

```kotlin
🏝️
                                👇
fun area(width: Int, height: Int) = width * height
```

不过，在实际开发中，还是推荐显式的将返回类型写出来，这样可以使代码阅读效率大大提高。

以上就是关于 `=` 连接返回值的概念，当函数中只有一行代码的时候，不管返回类型是什么，都可以省略大括号，并使用 `=` 符号进行连接。

当我们想对一个函数重载时，在 Java 中是新写一个同名但参数不同的函数，那 Kolin 中是否有更方便的方法呢？接下来我们看看 Kotlin 中的「参数默认值」。

#### 参数默认值

我们知道，在 Java 中一个类中有两个或两个以上函数名相同但参数不同的函数时，被叫做函数的重载：

```java
☕️
public void sayHi(String name) {
    System.out.println("Hi " + name);
}

public void sayHi() {
    sayHi("world"); 
}
```

在 Kotlin 中，也可以使用这样的方式进行函数的重载，不过还有更简单的方法，那就是参数默认值：

```kotlin
🏝️
                           👇
fun sayHi(name: String = "world") = println("Hi " + name)
```

这里的 `world` 是参数 `name` 的默认值，当调用时不传参数，则会使用该默认参数值。

这就等价于上面 Java 写的重载函数，当调用 `sayHi` 函数时，可以选择性的填参数或者不填：

```kotlin
🏝️
sayHi("kaixue.io") // 打印 Hi kaixue.io
sayHi() // 打印 Hi world
```

我们知道在 Java 中，每个重载函数的内部实现可以各不相同，这就无法保证在调用每个重载函数时，都能起到相同的效果。而 Kotlin 的参数默认值，就可以保证相同函数不同调用方式的效果一致性。

以上就是函数默认参数值的概念，再来思考一个问题「如果一个有默认值的参数在一个无默认值参数的前面，那么该怎样使用上默认值进行调用呢？」：

```kotlin
🏝️
fun sayHi(name: String = "world", age: Int) {
    ...
}
```

Kotlin 也为我们充分考虑到了这一点，下面我们来看看命名参数的使用。

#### 命名参数

当一个函数有非常多的参数或者多个参数有默认值时：

```kotlin
🏝️
                           👇          👇                        👇              
fun sayHi(name: String = "world", age: Int, isStudent: Boolean = true, isFat: Boolean = true, isTall: Boolean = true) {
    ...
}
```

上面的函数中有些参数有默认值有些没有，最后还有连续 3 个布尔类型参数，此时我们调用这个函数会是这样：

```kotlin
🏝️
sayHi("world", 21, false, true, false)
```

当看到后面一长串的布尔值时，我是崩溃的，好在 Kotlin 为我们提供了命名参数，使用命名参数我们可以这样调用：

```kotlin
🏝️
       👇          👇           👇              👇             👇
sayHi(name = "wo", age = 21, isStudent = false, isFat = true, isTall = false)
```

可以发现，在调用函数时，我们显式的指定了每个参数的名称，这就是命名参数。Kotlin 中的每一个函数都可以具备命名参数的特性，不过在 Kotlin 中调用 Java 的函数时是不支持使用命名参数的。

与命名参数相对应的被称为位置参数，也就是按位置顺序进行参数填写。当一个函数被调用时，混用位置函数与命名参数，所有的位置参数都应该放在第一个命名参数之前，例如：

```kotlin
🏝️
            👇 
sayHi("wo", age = 21) // 位置参数 name 在命名参数 age 之前，这样是正确的
      👇
sayHi(name = "wo", 21) // 命名参数 name 后面又出现了位置参数，这是不允许的
```

再回到之前遗留的问题「如果一个有默认值的参数在一个无默认值参数的前面，那么该怎样使用上默认值进行调用呢？」：

```kotlin
🏝️
fun sayHi(name: String = "world", age: Int) {
    ...
}
//    👇   
sayHi(age = 21)
```

只需要对 `age` 使用命名参数，参数 `name` 就会使用默认参数啦。

接下来，我们再看看 Kotlin 中的另一种常见函数：嵌套函数。

#### 本地函数（嵌套函数）

在 Kotlin 中提供了一种简洁的方案减少重复的代码：嵌套函数，简单来讲就是一个函数可以在另一个函数的内部声明。首先来看下这段代码：

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

可以发现上面代码中，有少量重复的判断逻辑，你又不想将这段逻辑作为一个单独的函数，使类做的面面俱到，但这些重复逻辑看着又实在难受，这时你可以使用局部函数，在 `login` 函数内部继续声明验证逻辑的函数：

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

上面这段代码看起来似乎简洁多了，将共同的验证逻辑放进了局部函数  `validate` 中。

这时又发现 `illegalStr` 参数是被传进局部函数中的，其实完全没有这个必要，因为嵌套函数可以访问在它外部的所有变量，包括类中的属性、当前函数中的参数与变量等。我们稍加改进：

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

可以看到，这里省去了嵌套函数中的 `illegalStr` 参数，在该函数内直接使用外层函数 `login` 的参数 `illegalStr`，这样是不是就简洁很多了。

> 上面 `login` 函数中的验证逻辑，其实还有一种更简单的方式：
>
> ```kotlin
> 🏝️
> fun login(user: String, password: String, illegalStr: String) {
>     require(user.isNotEmpty()) { illegalStr }
>     require(password.isNotEmpty()) { illegalStr }
> }
> ```
>
> 这里的 `require` 函数，接收一个布尔运算表达式与一个 lambda 表达式，如果布尔运算结果为 `false` ，就会抛出一个 `IllegalArgumentException`，异常的内容就是刚传入的 lambda 表达式的内容。
>
> 在 Kotlin 中，还提供了许多类似 `require` 这样方便的函数，我们会在之后的课程中继续讲到。

到此，我们知道了函数的 `= ` 连接符、参数默认值、命名参数、嵌套参数，这些知识点都可以将平时写的函数进行更方便的简化使用。再回过头来看这段代码：

```kotlin
🏝️
fun sayHi(name: String = "world") = println("Hi " + name)
```

如果函数要求多加几个参数，并在 `println` 中输出，会写成这样：

```kotlin
🏝️
fun sayHi(name: String = "world", myName: String) = println("Hi " + name + "，my name is " + myName)
```

可以看到 `println` 中写了一长串用 `+` 号拼接的字符串，那在 Kotlin 中有没有什么办法进行简化呢，下面我们来看看字符串的那些「更方便的」。

### 字符串

#### 字符串模板

对于字符串的使用我们已不陌生，在 Java 中，字符串与变量可以使用 `+` 符号进行拼接，Kotlin 也同样可以：

```kotlin
🏝️
val name = "world"
println("Hi " + name)
```

如果需要拼接的变量越多，那使用 `+` 连接的操作也越多，就会变成这样：

```kotlin
🏝️
val name = "world"
val myName = "kotlin"
println("Hi " + name + "，my name is " + myName)
```

上方代码示例中的 `println` 中写了一长串用 `+` 号拼接的字符串，看着难受写起来也更难受，好在 Kotlin 提供了更加方便的对字符串中加入变量的处理办法：

```kotlin
🏝️
val name = "world"
val myName = "kotlin"
           👇               👇
println("Hi $name，my name is $myName") // 输出： Hi world，my name is kotlin
```

这就是字符串模板表达式，Kotlin 允许在字符串中以 `$` 符号开头引用一个变量，最终会将变量结果合并到字符串中。或者使用花括号添加任意表达式：

```kotlin
🏝️
val name = "world"
            👇
println("Hi ${name.length + 1}") // 输出：Hi 6
```

如果你想在字符串使用表示字面值的 $ 字符，可以这样写：

```kotlin
🏝️
val price = "${'$'}9.99"
println(price) // 输出：$9.99
```

同样也支持转义字符，比如换行操作：

```kotlin
🏝️
                 👇
val name = "world!\n"
println("Hi $name")
```

上面代码中使用了转义字符 `\n` 进行换行操作。但如果有多个换行操作的话，会这样写成这样：

```kotlin
 🏝️
val name = "world"
val myName = "kotlin"
println("Hi $name!\n my name is $myName.\n")
```

虽然也可以达到想要的效果，但看着显得生硬，好在 Kotlin 中还有原生字符串，可以使这类字符串写起来更简单，下面我们来看看该怎么使用吧。

#### raw string (原生字符串)

Kotlin 中允许使用三个引号 `"""` 括起来的原生字符串，在原生字符串中可以包含换行以及任意文本：

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

上面的代码会将 `text` 中的内容原样输出，并带有换行与空格，其中末尾的 `\n` 转义字符也不会生效而会直接输出：

```
      Hi world!
    my name is kotlin.\n
```

还可以通过 `trimMargin()` 函数去除前面的空格：

```kotlin
🏝️
val text = """
      |Hi world!
    |my name is kotlin.
""".trimMargin()
println(text)
```

其中的 `|` 符号为默认的边界前缀，表示 `|` 之前这一行的空格可以去除。也可以选择其他字符做边界前缀，比如 `trimMargin("/")`。

到此我们了解了 Kotlin 中对字符串更加方便的操作，原来写一串字符拼接还可以如此流畅。

### 数组和集合

#### 数组与集合的操作符

在之前的课程中，我们了解了数组和集合的概念，其实 Kotlin 中还为我们提供了许多对数组与集合的操作更加方便的函数，下面让我们来看看这些便捷的操作符吧。

首先定义了如下数组与集合，并对他们进行一些便捷操作函数的讲解：

```kotlin
🏝️
val intArray = intArrayOf(1, 2) // Int 类型数组
val strList = listOf("a", "b", "c") // List<String> 集合
```

- `forEach`：遍历每一个元素

  ```kotlin
  🏝️
  intArray.forEach { i ->
      println(i) // 输出每一个元素值 1 2
  }
  
  strList.forEach { str ->
      println(str) // 输出每一个元素值 a b c
  }
  ```

- `filter`：遍历每个元素并进行过滤操作，如果 lambda 表达式中条件成立则留下该元素，最终过滤生成新的集合或数组

  ```kotlin
  🏝️
  intArray.filter { i ->
      i != 1 // 当遍历到的元素不等于 1 时成立并保留，也就是过滤掉数组中等于 1 的元素
  }.forEach { i ->
      println(i) // 输出过滤后的每一个元素 2
  }
  
  strList.filter { str ->
      str.contains("a") // 过滤掉集合元素中不包含 a 的元素
  }.forEach { str ->
      println(str) // 输出过滤后的每一个元素 a
  }
  ```

- `map`：遍历每个元素并与给定函数进行转换形成新的集合或数组

  ```kotlin
  🏝️
  intArray.map { i ->
      i+1 // 遍历数组，并将每个值加 1，返回新的数组
  }.forEach { i ->
      println(i) // 新的数组进行 forEach 遍历输出：2 3
  }
  
  strList.map { str ->
      str + "d" // 遍历集合，并将每个元素与 d 拼接，返回新的集合
  }.forEach { str ->
      println(str) // 新的集合进行 forEach 遍历输出：ad bd cd
  }
  ```

- `flatMap`：遍历每个元素并生成集合，最终将所有集合放到一个集合中，也就是平铺每个元素，可用在多维数组或者集合上

  ```kotlin
  🏝️
  intArray.flatMap { i ->
      listOf(i+1) // 遍历数组，且该闭包需要一个集合返回值，并将每个值加 1，最终返回新的数组
  }.forEach {
      println(i) // 新的数组进行 forEach 遍历输出：2 3
  }
  
  strList.flatMap { str ->
      listOf(str + "d") // 遍历集合，且该闭包需要一个集合返回值，并将每个元素与 d 拼接，最终返回新的集合
  }.forEach { str ->
      println(str) // 新的集合进行 forEach 遍历输出：ad bd cd
  }
  ```

- `first`：返回数组或集合的第一个与给定条件匹配的值元素，不传参数则返回第一个元素

  ```kotlin
  🏝️
  intArray.first { i ->
      i > 0 // 返回数组中第一个大于 0 的元素
  }
  
  strList.first { str ->
      str.contains("a") // 返回集合中第一个包含字母 a 的元素
  }
  ```

以上列出了一些 Kotlin 提供给我们的常用集合操作函数，还有许多操作函数等待你去发现学习，这里就不一一列举。正因为有了这些 Kotlin 为我们准备的操作函数，才使得我们在开发中对数组与集合的操作更加的方便快捷。

#### `Range` 和 `Sequence`

在 Kotlin 中还额外提供了 `Range` 和 `Sequence` 这两种新的类型。

在 Java 语言中并没有 `Range` 的概念，`Range` 也就是区间的意思，区间的写法如下：

```kotlin
🏝️
                       👇
val range: IntRange = 0..1000 
```

这里的 `0..1000` 就表示一个闭区间 [0,1000]，既然有闭区间那有没有开区间呢？其实 Kotlin 中没有纯开区间的定义，不过有半开区间的定义：

```kotlin
🏝️
                         👇
val range: IntRange = 0 until 1000 
```

这里的 `0 until 1000` 表示的是 [0,1000) 的半开区间，也就是 [0,999]。你还可以对区间进行迭代操作：

```kotlin
🏝️
val range = 0..1000
      👇
for (i in range) {
    print("$i, ")
}
```

这里的 `in` 关键字可以与 `for` 循环结合使用，不断的取出 `range` 区间上的下一个值，并赋值给 `i` 变量，这就实现了对 `rang` 区间的遍历操作。关于 `for` 循环控制的使用，在本期文章的后面有做具体讲解。

最终输出结果为：

```
0, 1, 2, 3, 4, 5, 6, 7....1000,
```

除此之外，还可以在遍历的时候设置步长：

```kotlin
🏝️
                 👇
for (i in range step 2) {
    print("$i, ")
}
```

上方代码中通过关键字 `step` 设置了增量为 2 的步长，最终输出结果为：

```
0, 2, 4, 6, 8, 10,....1000,
```

以上是递增增区间，Kotlin 还提供了递减区间 `downTo` :

```kotlin
🏝️
              👇
for (i in 4 downTo 1) print(i)
```

这里的 `4 downTo 1` 表示一个从 4 到 1 的闭区间 [4,1]。

上面我们了解了 `Range` 自动化的数数工具，而 `Sequence` 就像是一种「广度优先遍历」版本的集合类。在上一期中我们已经熟悉了 `Sequence` 的基本概念，这次让我们更加深入的了解 `Sequence` 。

序列 `Sequence` 又被称为「惰性集合操作」，序列中的元素求值都是惰性的，这样可以更加高效的对数据集进行链式操作，而不像普通集合那样每次数据操作都开辟新的空间存储中间结果。序列操作分为两大类：

- 中间操作

  序列的中间操作是惰性的，一次中间操作返回的都是一个序列，运行如下代码会发现什么也没有输出，这意味着序列的 `map` 与 `filter` 操作不是立即执行的，他们只有在获取结果的时候才会执行：

  ```kotlin
  🏝️
  val list = listOf(1, 2, 3, 4, 5, 6)
  val result = list.asSequence()
      .map{ println("Map $it"); it * 2 } // map 返回 Sequence<T> ，所以是中间操作
      .filter { println("Filter $it");it % 3  == 0 } // filter 返回 Sequence<T> 
  ```

  这样懒加载的实现有什么好处呢？我们知道在 Kotlin 中， `Iterable` 每调用一次函数就会生成一个新的 `Iterable`，下一个函数再基于新的 `Iterable` 执行，每次函数调用产生的临时 `Iterable` 会导致额外的内存消耗，而 `Sequence` 避免了这样的问题。

- 末端操作

  序列的末端操作会执行原来中间操作的所有延迟计算，一次末端操作返回的是一个结果，返回的结果可以是集合、数字、或者从其他对象集合变换得到任意对象，代码如下：

  ```kotlin
  🏝️
  val list = listOf(1, 2, 3, 4, 5, 6)
  val result = list.asSequence()
      .map{ println("Map $it"); it * 2 }
      .filter { println("Filter $it");it % 3  == 0 }
  println(result.first()) // result.first() 获取 result 中第一个元素
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

  你会发现，`map` 与 `filter` 甚至都没有完全执行完就停止了，这也意味着当序列找到第一个满足 `it % 3  == 0` 的元素时就会停止。这样就很好的省去后面不必要的计算。

以上我们对 `Sequence` 的懒加载与遍历函数执行顺序有了进一步的了解。这种数据类型可以在数据量比较大或者数据量未知的时候提供方便的流式处理方案。

讲了 Kotlin 中这么多方便的写法，接下来我们再看看最基础的条件控制相关的写法吧。

### 条件控制

#### `if/else` 和 `when`

相比 Java 的条件控制，Kotlin 中的条件控制进行了许多的优化改进，首先看下在 Java 中的 `if/else` 写法：

```Java
☕️
int max;
if (a > b) {
    max = a;
} else {
    max = b;
}
```

在 Kotlin 中你也可以这么写：

```kotlin
🏝️
var max: Int
if (a > b) {
    max = a
} else {
    max = b
}
```

但是，Kotlin 中 `if` 可以作为一个表达式，返回一个值：

```kotlin
🏝️
       👇
val max = if (a > b) a else b
```

可以发现 `=` 符号连接了 `max` 变量与 `if` 判断，这就表示 `if/else` 作为一个表达式，当 `a>b` 时返回结果为 `a` 的值，反正返回 `b` 的值。因此 Kotlin 中不需要三元运算符（条件 ? 然后 : 否则）。

当然 `if/else` 中分支里也可以是代码块，代码块的最后一行会作为返回值：

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

以上就是 Kotlin 中 `if/else` 的用法，那 Kotlin 中有没有 `switch` 语句呢？其实在 Kotlin 中用 `when` 代表了 Java 中的 `switch` 语句：

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

这里的 `when` 将它的参数与每个条件进行比较，直到遇到合适的分支，否则会走默认的 `else` 分支。

`when` 也可以作为表达式使用，符合条件的分支中的最后一行的结果会被作为返回值，但需要注意的是，这时就必须要有 `else` 分支，使得表达式无论怎样都会有返回结果，除非编译器能够检测出已经将所有的情况覆盖。

这时你会疑问「在刚刚的代码中，好像没有 Java 中的 `break` 返回语句啊？」，其实 Kotlin 中不需要那么麻烦，当一个条件满足的时候会执行当前代码块并自动返回，而不会执行之后的其他分支。如果多个分支都是相同的代码块的话，可以将多个分支条件放在一起，用 `,` 符号隔开：

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

还可以用来替代 `if/else if` 链，即不提供参数，每一个分支条件都可以是一个简单的布尔表达式，当找到第一个分支为真时则执行该分支：

```kotlin
🏝️
when {
   👇
    str.contains("a") -> print("字符串 str 包含 a")
   👇
    str.length == 3 -> print("字符串 str 的长度为 3")
}
```

到此，我相信你对 `if/else` 条件控制相关的知识了解得差不多了，下面再来看看 `for` 循环控制有什么更简单的使用方法。

#### `for`

在 Kotlin 中，`for` 循环操作与 Java 的 `for` 相比也进行了许多优化，Kotlin 中 `for` 循环可以对任何提供迭代器（iterator）的对象进行遍历：

```kotlin
🏝️
          👇
for (item in collection) {
    print(item)
}
```

这里的的 `in` 操作符与 `for` 循环结合，不断的从 `collection` 中取出子元素赋值给 `item` 变量，从而实现对该对象的遍历读取操作。或者使用常见的数字区间迭代，区间的知识我们已在上面的内容讲过，代码如下：

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

如果你想要通过索引的方式遍历一个数组或者集合，可以这样写：

```kotlin
🏝️
                👇
for (i in array.indices) {
    println(array[i])
}
```

`indices` 函数会返回该数组或者集合的有效索引的区间，或者使用库函数 `withIndex` ：

```kotlin
🏝️
          👇                    👇
for ((index, value) in array.withIndex()) {
    println("index: $index , value: $value")
}
```

这里的 `withIndex` 函数会返回一个包含索引值与元素值的迭代器对象，其中 `(index, value)` 是 Kotlin 中的解构声明，该部分我们会在之后的文章进行讲解，这里我们只需要明白 `index` 与 ` value` 用来分别承载单个元素的索引值与元素值即可。

#### `try-catch`

`try-catch` 我们都不陌生，在编程开发中难免都会遇到异常需要进行处理，那么在 Kotlin 中是怎样处理的呢，先来看下 Kotlin 捕获异常的代码：

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

可以发现 Kotlin 异常处理与 Java 的异常处理基本相同，但在使用上也有几个不同点：

- Kotlin 中 `try` 也可以是一个表达式，允许代码块的最后一行作为返回值：

  ```kotlin
  🏝️
             👇          👇                                                👇
  val a: Int? = try { parseInt(input) } catch (e: NumberFormatException) { null }
  ```

- Kotlin 中的异常都是不受检的异常，什么意思呢？

  - 受检查的异常：必须在函数上定义并且需要处理的异常，比如 Java 中的 `IOException`
  - 不受检查的异常：不是必须进行处理的，比如 `NullPointerException`
  - Kotlin 中异常的这种设计，是想要尝试修补 Java 上没有达到的理论效果

#### `?:` 和 `?.`

Kotlin 的空安全我们在之前的文章中已经讲过，其实还有一个常用的复合符号可以让你的代码更加简洁，那就是 Elvis 操作符 `?:` 。

当我们有一个可空的对象，想要这个对象不空时使用这个对象，为空时使用另一个非空值时，我们一般可以这么写：

```kotlin
🏝️
val str: String? = "Hello"
val length = if (str != null) str.length else -1
```

这里的 `str` 是一个可空的字符串对象，我们想要获取它的长度时先判断是否为空，不为空则获取 `str.length` ，为空则使用 `-1` 。

其实不必写的这么麻烦，我们可以通过 Elvis 操作符 `?:` 再结合空安全调用 `?.` 实现：

```kotlin
🏝️
val str: String? = "Hello"
                        👇
val length = str?.length ?: -1
```

这里的 `str?.length` 使用了空安全调用 `?.` ，表示 `str` 为空时结果为 `null`，非空时获取 `length`。这里的 `?:` 表示若左侧表达式为空，则返回其右侧表达式的值 `-1`，否则返回左侧表达式的值。

 Elvis 操作符右侧还可以是一个表达式，例如：

```kotlin
🏝️
fun validate(user: User) {
    val id = user.id ?: return
    val name = user.name ?: throw IllegalArgumentException("name is null")
}
```

这里的 `return` 与 `throw` 都是在左侧表达式为空时执行。

看到这里，想必你对 Kotlin 的空安全有了更深入的了解吧。下面我们看看 Kotlin 的相等比较符。

#### `==` 和 `===`

我们知道在 Java 中相等性比较可以使用 `==` ，如果是比较的 `int` 类型则表示数值是否相等，如果用在 `String` 字符串上则表示引用地址是否相等， `String` 字符串的内容比较使用的是 `equals()` ：

```java
☕️
String str1 = "123", str2 = "123";
System.out.println(str1.equals(str2)); // 判断内容是否相等 输出 true
System.out.println(str1 == str2); // 判断引用地址是否相等  输出 false
```

而 Kotlin 中有三种比较方式：

- `==` ：如果是基本数据类型的变量，则会比较其中的值是否相等，包括 `String` 类型。如果是引用类型的变量则会比较对象的地址。
- `===` ：比较的是引用的内存地址。
- `equals` ：不可以比较基本类型变量。如果作用的类没有对 `equals` 方法重写，则会比较所指向的内存地址。诸如 `String`、`Date` 等类对 `equals` 方法进行了重写，比较的则是对象的内容。

我们来看一段示例代码：

```kotlin
🏝️
str1 == str2
//等价于：
                  👇       👇
str1?.equals(str2) ?: (str2 === null)
```

如果 `str1` 与 `str2` 都是可空的 `String` 类型，如果 `str1` 不为空则调用 `equals` 比较值是否相同，如果为空则执行右侧表达式 `str2 === null`。

### 练习题

好了，以上就是关于 Kotlin 里那些「更方便的」，给你留两道思考题吧：

1. 下面这段代码，当使用 `double(null)` 调用时，结果是多少？

   ```kotlin
   fun double(value: Int?): Int = value ?: 0 * 2
   ```

2. 将下方给定的集合中的每个元素乘 3 形成行的集合并输出，要求使用今天所讲的集合操作符？

   ```kotlin
   val list = listOf(1, 2, 3)
   ```