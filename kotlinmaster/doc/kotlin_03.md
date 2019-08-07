## Kotlin 里那些「更方便的」

在上期课程当中，我们学习了 Kotlin 的那些与 Java 写法不同且需要注意的知识点。本节我们讲一讲 Kotlin 中那些还可以这么方便的写法。

### 构造器

上期我们学习了 Kotlin 的 constructor 的写法：

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
           👇          👇
class User constructor(name: String) {
  👇
  var name: String = name
}
```

你会发现在上面的例子当中，constructor 构造函数写在了类的头部，在类名之后。原本在构造函数中赋值的 `name` 也被直接放在了属性初始化的时候进行赋值。这就是「主构造器 primary constructor」，在 Kotlin 中一个类只允许有一个主构造器以及一个或者多个次构造函数。

这时你会奇怪，「构造函数被放到了类的头部，那构造函数中的参数 `name` 该如何使用呢？」其实可以在以下两个地方使用：

- 可以在属性的初始化中使用：

  ```kotlin
  🏝️
  class User constructor(name: String) {
    var name: String = name
  }
  ```

- 另一个是在 `init` 代码块中：

  ```kotlin
  🏝️
  class User constructor(name: String) {
    var name: String
    👇
    init {
      this.name = name
    }
  }
  ```

上面的例子甚至还可以这么写：

```kotlin
🏝️
class User constructor(var name: String) {
                       👆
}
```

是不是很方便，原本一个有构造函数以及需要赋值属性的类需要写很多行，现在一行就能搞定！但你也会疑问，那么 `name` 这个属性我还能不能在这个类的其他地方使用呢？别急，在上面的代码中的 primary constructor 的 `name` 参数的左边多了一个 `var` 可变的修饰，这时 Kotlin 会自定帮你创建一个与参数同名的属性，并把这个参数的值作为属性的初始化，这样你就可以在类中使用 `name` 属性啦。当然，你也可以使用 `val` 只读的修饰符。

以上讲了主构造器的使用与简化，结合上期所讲的次构造函数，我们思考下，如果一个类有多个构造函数，应该把哪个写成 primary 的呢？其实很简单，只要把最基本、最通用的那个写成 primary 的就好了，primary constructor 会参与任何一个 constructor 创建对象的初始化过程，所以如果有 `init` 代码块也会在使用次构造函数之前执行。

在没有使用 primary constructor 简化之前，写多个构造函数的代码：

```kotlin
🏝️
class User {
  var name: String
  var id: String

  constructor(name: String, id: String) {
    this.name = name
    this.id = id
  }
  
  constructor(person: Person) {
    this.name = person.name
    this.id = person.id
  }
}
```

在选择将第一个构造函数作为  primary constructor 时，你可以这样写：

```kotlin
🏝️
class User(person: Person) {
  var name: String
  var id: String
  
  init {
    name = person.name
    id = person.id
  }

  constructor(name: String, id: String): this(person = Person(name, id)) {
  }
}
```

学完今天对构造函数的简化之后，你甚至可以这样写：

```kotlin
🏝️
class User(var name: String, var id: String) {
  constructor(person: Person) : this(person.name, person.id) {
  }
}
```

### 函数简化

#### 使用 `=` 连接返回值

在之前的课程中，我们已经学习了 Kotlin 中函数的写法：

```kotlin
🏝️
fun sayHi(name: String) {
    println("Hi " + name)
}
```

这是一个非常简单的函数，获取到参数 `name` 值并输出，函数中只有一行代码，其实还可以这么写：

```kotlin
🏝️
                       👇
fun sayHi(name: String) = println("Hi " + name)
```

上面的代码中可以看出，我们把大括号去掉并在函数体前面使用 `=` 符号连接，是不是非常的整洁，这时你会有疑问「这是函数没有返回值的情况，那如果函数有返回值呢？」：

```kotlin
🏝️
fun area(width: Int, height: Int): Int {
  return width * height
}
```

当函数返回单个表达式时，我们同样可以省略花括号并使用 `=` 符号连接：

```kotlin
🏝️
fun area(width: Int, height: Int): Int = width * height
```

之前我们学习过，Kotlin 有个「类型推断」的特性，这里函数的返回类型就可以隐藏了：

```kotlin
🏝️
                                👇
fun area(width: Int, height: Int) = width * height
```

是不是非常的方便，一个函数可以写的如此简洁。

#### 参数默认值

在 Kotlin 中，函数参数可以有默认值，当省略相对应的参数时 Kotlin 会自动使用默认值，在 Java 中可以使用对函数的重载，生成不同参数类型的相同函数：

```java
☕️
public void sayHi(String name) {
  System.out.println("Hi " + name);
}

public void sayHi() {
  sayHi("world");
}
```

但在 Kotlin 中你可以这么写：

```kotlin
🏝️
                         👇
fun sayHi(name: String = "world") = println("Hi " + name)
```

上面两段不同语言的代码，都可以在使用 `sayHi` 函数时选择性的填参数或者不填，在 Kotlin 中：

```kotlin
🏝️
sayHi("kaixue.io") // 打印 Hi kaixue.io
sayHi() // 打印 Hi world
```

这时你也许会有疑问「那如果子类中覆盖父类中有默认值的方法时是怎样的？」，这时必须子类中必须省略默认参数值：

```kotlin
🏝️
open class A {
    fun sayHi(name: String = "world") = println("Hi " + name)
}

class B : A() {
                        👇
    override fun sayHi(name: String) { …… }  // 不能有默认值
}
```

上面就是函数默认参数值的概念，再来思考一个问题「如果一个有默认值的参数在一个无默认值参数的前面，那么该怎样使用上默认值进行调用呢？」：

```kotlin
🏝️
fun sayHi(name: String = "world", age: String) {
  ...
}
```

别急，我们来学习下一个知识点就能解决这个疑惑。

#### 命名参数

当一个函数有非常多的参数或者有默认参数时：

```kotlin
🏝️
                            👇          👇                        👇              
fun sayHi(name: String = "leavesC", age: Int, isStudent: Boolean = true, isFat: Boolean = true, isTall: Boolean = true) {
👆                       👆
  ...
}
```

上面的函数中有些参数有默认值有些没有，最后还有连续 3 个布尔类型参数，此时我们调用这个函数会是这样：

```kotlin
🏝️
sayHi("world", 21, false, true, false)
```

当看到后面一长串的布尔值时，我是崩溃的，好在 Kotlin 为我们提供了命名参数，使用命名参数我们可以这样写：

```kotlin
🏝️
        👇         👇        👇                👇             👇
sayHi(name = "wo", age = 21, isStudent = false, isFat = true, isTall = false)
```

可以发现，在调用函数时，我们显示的指定了每个参数的名称，让人一目了然。回到上面讲参数默认值最后提到的那个问题「如果一个有默认值的参数在一个无默认值参数的前面，那么该怎样使用上默认值进行调用呢？」，以此处的 `sayHi` 函数为例，我们可以不用提供所有的参数：

```kotlin
🏝️
sayHi(age = 21)
      👆
```

只需要在函数调用时，指定好需要的参数的名称就可以了。

#### 本地函数（嵌套函数）

在 Kotlin 中提供了一种简洁的方案减少重复的代码：嵌套函数，即一个函数在另一个函数的内部。首先来看下这段代码：

```kotlin
🏝️
fun login(user: String, password: String, illegalStr: String) {
            👇 
  if (user.isEmpty()) {
    👇                            👇 
    throw IllegalArgumentException(illegalStr)
  }
                👇 
  if (password.isEmpty()) {
    👇                            👇 
    throw IllegalArgumentException(illegalStr)
  }
}
```

上面代码中，重复的逻辑很少，你又不想将这段逻辑作为一个单独的函数做到面面俱到，但又有些重复的逻辑实在难受，这时你可以使用局部函数，在 `login` 函数内部添加验证逻辑的函数：

```kotlin
🏝️
fun login(user: String, password: String, illegalStr: String) {
       👇                     👇
  fun validate(value: String, illegalStr: String) {
    if (value.isEmpty()) {
      throw IllegalArgumentException(illegalStr)
    }
  }
  👇
  validate(user, illegalStr)
  👇
  validate(password, illegalStr)
}
```

上面这段代码看起来似乎简洁多了，将共同的验证逻辑放进了局部函数  `validate` 中，但是发现 `illegalStr` 参数被我们一路传进了局部函数  `validate` 中，其实完全没有这个必要，因为局部函数可以访问它所在函数的所有参数与变量，我们稍加改进：

```kotlin
🏝️
fun login(user: String, password: String, illegalStr: String) {
               👇
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

这样是不是就简洁很多了。

### 字符串

#### 字符串模板

对于字符串的使用我们已不陌生，前面的课程也讲到了基本类型，Java 对于字符串的使用可以使用 + 符号拼接，Kotlin 同样可以：

```kotlin
🏝️
val name = "world"
println("Hi " + name)
```

但 Kotlin 可以更加的简洁：

```kotlin
🏝️
val name = "world"
           👇
println("Hi $name")
```

这就是字符串模板表达式，Kotlin 允许在一段字符串中使用变量进行计算并把结果合并到字符串中进行顺序拼接。也就是以 `$` 符号开头引用一个变量，或者使用花括号添加任意表达式：

```kotlin
🏝️
val name = "world"
              👇
println("Hi ${name.length + 1}")
```

如果你想在字符串使用表示字面值的 $ 字符，可以这样写：

```kotlin
🏝️
val price = "${'$'}9.99"
```

#### raw string (原生字符串)

在 Kotlin 中与 Java 相同，也支持转义字符：

```kotlin
🏝️
                 👇
val name = "world!\n"
println("Hi $name")
```

上面代码中使用了转义字符 `\n` 进行换行操作。其实，Kotlin 中还支持原生字符串，在原始字符串中可以包含换行以及任意文本，转义字符串也不会生效，也就是使用三个引号 `"""` 括起来：

```kotlin
🏝️
            👇
 val text = """
    for (c in "foo")
        print(c)\n
"""
👆
println(text)
```

上面的代码会将 `text` 中的内容原样输出，并带有换行与空格，其中末尾的 `\n` 也不会生效而会直接输出：

```
    for (c in "foo")
        print(c)\n
```

### 数组和集合

#### 数组与集合的操作符

在之前的课程中，我们学习过 Kotlin 定义了一套新的数组和集合的类型，它的目的就在于提供一套方便的操作方法用这一套方法，你可以对数组和集合做各种便捷的整体化操作。

定义如下数组与集合，并对他们进行操作实例：

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

- `filter`：遍历每个元素并进行过滤操作，如果 lambda 表达式中条件成立则留下改元素，最终过滤生成新的集合或数组

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
  intArray.flatMap {
    listOf(it+1) // 遍历数组，且该闭包需要一个集合返回值，并将每个值加 1，最终返回新的数组
  }.forEach {
    println(it) // 新的数组进行 forEach 遍历输出：2 3
  }
  
  strList.flatMap { str ->
    listOf(str + "d") // 遍历数组，且该闭包需要一个集合返回值，并将每个元素与 d 拼接，最终返回新的数组
  }.forEach { str ->
    println(str) // 新的集合进行 forEach 遍历输出：ad bd cd
  }
  ```

- `first`：返回数组或集合的第一个与给定条件匹配的值元素，不传参数则返回第一个元素

  ```kotlin
  🏝️
  intArray.first {
    it > 0 // 返回数组中第一个大于 0 的元素
  }
  
  strList.first { 
    it.contains("a") // 返回集合中第一个包含字母 a 的元素
  }
  ```

以上列出了一些常用的集合操作符，还有许多操作符等待你去发现学习，这里就不一一列举，正因为有了这些 Kotlin 为我们准备的操作符才使得我们在开发中对集合的操作更加的方便简洁。

#### `Range` 和 `Sequence`

在 Kotlin 中还额外提供了 `Range` 和 `Sequence` 这两种新的类型。

在 Java 语言中并没有 `Range` 的概念，`Range` 也就是区间，区间的写法如下：

```kotlin
🏝️
                      👇
val range: IntRange = 0..1000 
```

上面示例代码表示一个闭区间 [0,1000]。Kotlin 中没有纯开区间的定义，但有半开区间的定义：

```kotlin
🏝️
                         👇
val range: IntRange = 0 until 1000 
```

上面示例代码表示的是 [0,1000) 的半开区间，也就是 [0,999]。你还可以对区间进行迭代操作：

```kotlin
🏝️
val range = 0..1000
      👇
for (i in range) {
  print("$i, ")
}
```

上方代码中 `for` 循环会对 `rang` 区间进行遍历操作，最终输出结果为：

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

上方示例中的 `4 downTo 1` 表示一个从 4 到 1 的闭区间 [4,1]。

同样的，在 Java 中也没有 Sequence 序列概念，在上面我们已经学习了 Kotlin 集合操作符的概念并且可以链式的进行调用，但是在某些情况下 list 的迭代器并不是最好的方式，从而就有了另一种方式：序列。在 Kotlin 中可以通过 `asSequence()` 将一个集合转化成一个序列：

```kotlin
🏝️
val list = listOf(1, 2, 3, 4, 5, 6)
                      👇
val sequence = list.asSequence()
```

那么序列到底是什么呢，序列又被称为「惰性集合操作」，序列中的元素求值都是惰性的，可以更加高效的对数据集进行链式操作，而不像普通集合那样每次数据操作都开辟新的控件存储中间结果。序列操作分为两大类：

- 中间操作

  序列的中间操作始终都是惰性的，一次中间操作返回的都是一个序列，运行如下代码会发现什么也没有输出，这意味着序列的 `map` 与 `filter` 操作被延迟了，他们只有在获取结果的时候才会输出：

  ```kotlin
  🏝️
  val list = listOf(1, 2, 3, 4, 5, 6)
  val result = list.asSequence()
      .map{ println("Map"); it * 2 } // map 返回 Sequence<T> ，所以是中间操作
      .filter { println("Filter");it % 3  == 0 } // filter 返回 Sequence<T> ，所以是中间操作
  ```

- 末端操作

  序列的末端操作会执行原来中间操作的所有延迟计算，一次末端操作返回的是一个结果，返回的结果可以是集合、数字、或者从其他对象集合变换得到任意对象，代码如下：

  ```kotlin
  🏝️
  val list = listOf(1, 2, 3, 4, 5, 6)
  val result = list.asSequence()
      .map{ println("Map"); it * 2 }
      .filter { println("Filter");it % 3  == 0 }
  println(result.first())
  ```

  最终运行结果如下：

  ```
  Map
  Filter
  Map
  Filter
  Map
  Filter
  6
  ```

  你会发现，`map` 与 `filter` 甚至都没有完全执行完就输出了结果的第一个值，这也意味着当序列找到第一个值的那一刻，就会停止。

`Sequence` 就像是一种「广度优先遍历」版本的集合类。这种数据类型可以在数据量比较大或者数据量为知的时候提供方便的流式处理方案。还有许多用法需要等待你自己去探索。

### 条件控制

#### `if/else` 和 `when`

在 Kotlin 中，也进行了许多的优化改变，首先看下在 Java 中的 `if/else` 写法：

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

但是，Kotlin 中 `if` 可以是一个表达式，它会返回一个值，上面代码你就可以这样写：

```kotlin
🏝️
         👇
val max = if (a > b) a else b
```

是不是非常的直观方便，因此 Kotlin 中不需要三元运算符（条件 ? 然后 : 否则）。当然 `if/else` 中分支里可以是代码块，代码块的最后一行会作为返回值：

```kotlin
🏝️
val max = if (a > b) {
  print("max:a")
  a
} else {
  print("max:b")
  b
}
```

以上就是 Kotlin 中 `if/else` 的用法，这时你会问「那有没有 `switch` 呢？」，其实在 Kotlin 中用 `when` 替换了 Java 中的 `switch` 操作符：

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

`when` 表达式会将它的参数与每个条件进行比较，直到遇到合适的分支，否则会走默认的 `else` 分支。与 Kotlin 中的 `if` 相同也可以被当做表达式使用，符合条件的分支的最后一行表达式的值就作为返回值，如果 `when` 被作为表达式使用的话，那必须要有 `else` 分支，除非编译器能够检测出已经将所有的情况覆盖。

这时你会疑问「好像没有 Java 中的 `break` 返回语句啊？」，其实 Kotlin 中不需要那么麻烦，当一个条件满足的时候会执行当前代码块并自动返回，而不会执行之后的其他分支。如果多个分支都是相同的代码块的话，可以将多个分支条件放在一起，用 `,` 符号隔开：

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
  else -> print("不相同相同")
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
  else -> print("else")
}
```

也可以使用 `is` 进行特定类型的检测：

```kotlin
🏝️
                 👇
fun hasPrefix(x: Any) = when(x) {
  👇
  is String -> x.startsWith("prefix")
  else -> false
}
```

或者可以用来替代 `if/else if` 链，即不提供参数，每一个分支条件都可以是一个简单的布尔表达式，当找到第一个分支为真时则执行该分支：

```kotlin
🏝️
when {
  👇
  str.contains("a") -> print("字符串 str 包含 a")
  👇
  str.length == 3 -> print("字符串 str 的长度为3")
  else -> print("else")
}
```

#### `for`

在 Kotlin 中，`for` 循环与 Java 的 `for` 相比也进行了许多优化，Kotlin 中 `for` 循环可以对任何提供迭代器（iterator）的对象进行遍历：

```kotlin
🏝️
         👇
for (item in collection) {
  print(item)
}
```

或者使用常见的数字区间迭代，区间的知识我们已在上面的内容学过，代码如下：

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

`indices` 方法会返回该数组或者集合的有效索引的区间，或者使用库函数 `withIndex` ：

```kotlin
🏝️
          👇                    👇
for ((index, value) in array.withIndex()) {
    println("index: $index , value: $value")
}
```

`withIndex` 方法会返回一个包含索引值与元素值的迭代器对象。

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

可以看出 Kotlin 异常处理与 Java 的异常处理基本相同，但也有几个不同点：

- Kotlin 中 `try` 可以是一个表达式，允许代码块的最后一行作为返回值：

  ```kotlin
  🏝️
             👇          👇
  val a: Int? = try { parseInt(input) } catch (e: NumberFormatException) { null }
  ```

- 并且 Kotlin 中的异常都是不受检的异常，什么意思呢？

  - 受检查的异常：必须在方法上定义并且需要处理的异常，比如 Java 中的 IoException
  - 不受检查的异常：不是必须进行处理的，比如 NullPointerException
  - Kotlin 中异常的这种设计，是想要尝试修补 Java 上没有达到的理论效果

#### `?:` 和 `?.`

Kotlin 的空安全我们在之前的课程中已经学过，其实还有两个常用的复合符号可以让你的代码更加简洁，那就是 `?:` 和 `?.` 。

当我们有一个可空的对象，如果想要这个对象不空时使用这个对象，为空时使用另一个非空值时，我们一般可以这么写：

```kotlin
🏝️
val length = if (str != null) str.length else -1
```

上方示例中 `str` 是一个可空的字符串对象，我们想要获取它的长度时先判断是否为空，不为空则使用 `str.length` ，为空则使用 `-1` 。其实不必写的这么麻烦，可以通过 Elvis 操作符 `?:` 完成：

```kotlin
🏝️
val length = str?.length ?: -1
```

上方代码示例中的空安全调用 `?.` 我们已经在之前的课程中学过，如果 `str` 为空则 `str?.length` 的结果为 `null`，而如果 `?:` 左侧表达式为空，则返回其右侧表达式的值 `-1`，否则返回左侧表达式的值。 Elvis 操作符右侧还可以是一个表达式，例如检查函数参数：

```kotlin
🏝️
fun validate(user: User) {
  val id = user.id ?: return
  val name = user.name ?: throw IllegalArgumentException("name is null")
}
```

#### `==` 和 `===`

我们知道在 Java 中相等性比较可以使用 `==` 如果是比较的 `int` 类型则表示数值是否相等，如果用在 `String` 字符串上则表示引用地址是否相等，而 `String` 字符串的内容比较使用的是 `equals()` ：

```java
☕️
String str1 = "123", str2 = "123";
System.out.println(str1.equals(str2)); // 判断内容是否相等 输出 true
System.out.println(str1 == str2); // 判断引用地址是否相等  输出 false
```

而在 Kotlin 中 `==` 得作用类似于 Java 中的 `equals()`，用于判断内容是否相等，只不过多了判空的操作：

```kotlin
🏝️
    👇
str1 == str2
//等价于：
   👇             👇       👇
str1?.equals(str2) ?: (str2 === null)
```

如果 `str1` 与 `str2` 都是可空的，那 `==` 比较内容是会进行判空操作。这时你会疑问「上方示例中，`===` 又是什么意思呢？又如何判断引用地址是否相等呢？」其实 `===` 就是对两个对象的地址是否相等的判断。

### 思考题

1. 下面这段代码的 `value` 为 `null` 时，结果是多少？

   ```kotlin
   fun double(value: Int?): Int = value ?: 0 * 2
   ```

2. 将下方给定的集合中的每个元素乘 3 形成行的集合并输出，要求使用今天刚学的操作符？

   ```kotlin
   val list = listOf(1, 2, 3)
   ```

### 小结

今天的内容就是这些：Kotlin 里那些「更方便的」。除了开头的 primary constructor，其它东西都很简单，不过还是建议你把文章看完，然后把练习题做一下，这会对你的记忆很有帮助。











