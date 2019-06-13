# Kotlin 基础语法（一）

> Google 在 I/O 2019 上，宣布 Kotlin 作为 Android 的第一开发语言，这对于 Android 开发者的重要性不言而喻。 Kotlin 是一门开源的、静态类型的语言，天然支持面向对象和函数式编程。它借鉴了很多其他语言的设计思想。为了帮助 Android 开发者更好地掌握这门语言，从本篇开始将会带着大家来学习。

## 搭建 Kotlin 开发环境

先新建一个 Kotlin 工程。

- 准备好 Android Studio 开发环境，当前最新版本是 3.5 beta 4。

- 新建 project，其中 Language 选择 Kotlin 来自动生成支持 Kotlin 的 `build.gradle` 文件。

重点看 `project root`  和 `app` 目录下的 `build.gradle` 文件和 Java 有什么不同：

```groovy
// project root dir
ext.kotlin_version = '1.3.31'
dependencies {
    classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
}

// app dir
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions' // 这个之后再讲，现在用不到

dependencies {
    // 因为 minSdkVersion 是 21 所以后缀加了 jdk7
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
}
```

任何语言的学习都离不开基础语法，就像学外语要先认识单词才能造句。

不过在开始动手之前，有必要先认识下 IDE 已经创建好的 `MainActivity.kt` 这个类，可以看到，Kotlin 文件是以 .kt 结尾的。我们并不在这个类里写任何代码，而是会参照着新写一个。 

```kotlin
// 包的声明和 Java 类似，但是有一些小区别
// 目录与包的结构无需匹配：源代码可以在文件系统的任意位置
package org.kotlinmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// 关于 class 后面再讲
class MainActivity : AppCompatActivity() {
	// ...
}
```

这里值得注意的一点是，Kotlin 中是**不需要分号**的！只是硬要加上的话也不会报错。

新建一个 Kotlin File 叫 Sample.kt 。

```kotlin
package org.kotlinmaster.basic_grammar_01
```

好了，开始学习基础语法的旅程吧。

---

## 变量

### 声明与赋值

声明变量用到关键字 `var` ，就是 variable 的缩写。

```kotlin
var count: Int = 1
```

和 Java 有以下不同

- 类型和变量名位置互换
- 中间使用 `:` 分隔
- `var` 关键字开头
- 必须有默认值

如果这么写:

```kotlin
var count: Int ❌
```

报错 `Property must be initialized` ，也就是说这里声明的 `var` 叫 Property ，相当于 Java 里的 field ，但 Kotlin 里的 field 是另一个概念，之后再讲。

根据修改建议 `Add Initializer` 会变成如下：

```kotlin
var count: Int = 0
```

那么问题来了，我就是现在不想赋值行不行？

可以使用 `lateinit` 关键字，顾名思义，就是说我先声明好我晚些时候会初始化它，你不用担心它没有值的情况。

```kotlin
lateinit count: Int ❌
```

报错 `'lateinit' modifier is not allowd on primitive properties` ，这就是说虽然 Kotlin 里的 `Int` 是大写，但仍然是`基本类型`的属性，关于基本类型后面再讲。不过换成非基本类型比如 `String` 是没问题的。

```kotlin
lateinit countStr: String
```

当然 Kotlin 以防万一还提供了 `::countStr.isInitialized` 来判断变量到底有没有被初始化过，实际的开发中应该尽可能避免用到，简单了解下即可。

那是不是每次都要添加变量类型呢，当然不是，Kotlin 还支持类型**自动推断 inferred** ，像这样：

```kotlin
var count = 1
```

这是在编译期间完成的，所以 Kotlin 是一门静态类型语言。

Java 里也有类型推断：

```java
// 右边省略了 <> 里面的 String
List<String> names = new ArrayList<>();
```

如果使用 `lateinit` 关键字就必须要加变量类型。

`var` 声明的变量是可以被重新赋值的。

```kotlin
var count: Int = 1
// reassign
count = 2
```

报错 `Expecting a top level declaration` 。

说明对变量的操作需要放到顶层声明中，Kotlin 这里的顶层声明指的就是`函数`，但是函数怎么写我们还不知道，*没关系 再继续努力 没关系*，怎么唱起周杰伦的《三年二班》了，回到正题，先假定已经有一个函数了。

```kotlin
// fun 是函数的关键字，这里先跳过
fun incrementCount() {
    var count: Int = 1
    // 在函数里操作变量就不会报错了
    // += 的语法和 Java 一样，是不是很熟悉
    count += 1
}
```

### 空安全

刚才声明的变量，如果赋值为空会出现什么情况？

```kotlin
var ballCount1: Int = null ❌ // 初始化报错

var ballCount2: Int = 1
ballCount2 = null ❌ // 赋值也报错
```

报错 `Null can not be a value of a non-null type Int` 。

修复建议是 `Change type of 'ballCount' to type 'Int?'`

当某个变量的值可以为 `null` 的时候，必须在声明处的类型后添加 `?` 来标识可为空。

来复习下前面学过的知识吧。

```kotlin
var ballCount1: Int? = null
ballCount1 = 1

lateinit var ballCount2: Int? ❌ // lateinit 修饰符只能用来修饰非空类型，不然既要延迟初始化又不保证非空，编译器说你玩儿我的吧

var ballCount3 = null // 编译通过，这是唯一可以不显式指定类型的 case
ballCount3 = 3 ❌ // 因为之前没有指定类型，所以这行报错了

var ballCount4 = 4 // 自动推断，此时其实是 'Int' 而不是 'Int?'
```

你可能又要问了，为什么要这样设计？请看下面这个例子：

```kotlin
var word: String = "hello"
word.length

var nullableWord: String? = null
// 在 Java 里调用一个空值的话编译期间不会报错，而是在运行时抛出 NullPointerException
// 但是在 Kotlin 里面编译的时候就会报错，提前告诉你
nullableWord.length ❌
```

报错 `Only safe (?.) or non-null asserted (!!) calls are allowed on a nullable receiver of type String? `

这里有几个概念：

- `?.` safe call 「如果就」规则，如果不为空就执行 `?.` 后面的逻辑。
- `!!` non-null asserted call 「肯定不空」的断言，相当于忽略可空提示的风险自担的强行调用。
- `receiver` 这里就是 call 前面的变量。

```kotlin
var nullableWord1: String? = null
nullableWord1?.length // 运行期不会报错
nullableWord1!!.length // 运行期仍然会报错！

var nullableWord2: String? = "hello"
nullableWord2?.length // 运行期不会报错
nullableWord2!!.length // 运行期不会报错
```

这就是 Kotlin 的空安全特性，并且是兼容 Java 的。

新建一个 Java 类

```java
package org.kotlinmaster.basic_grammar_01;

// 这里用 import org.jetbrains.annotations.Nullable; 也可以
import androidx.annotation.Nullable;

public class Dict {
    @Nullable
    public static String word = "hello";
}
```

在 Kotlin 里调用同样会报错：

```kotlin
Dict.word.length ❌
Dict.word?.length
```

### getter / setter

其实在讲变量声明和赋值的时候我们已经在用它的 getter / setter 了。

```kotlin
var word: String = "hello"
word = "hey" // 属性的写操作
println(word) // 属性的读操作
```

读写属性的时候，实际上是调用 `getX() / setX()` 方法，可以重写 getter / setter ，像这样：

```kotlin
var word: String = "hello"
    get() {
        return "world"
    }
println(word)
// 这时会打印出什么呢？答案是 world
```

```kotlin
var word: String = "hello"
    set(value) {
        // field 是个啥？下面会讲
        field = value + " Mike"
    }
println(word)
// 这时会打印出什么呢？答案是 hello
// 因为初始化的动作是 assign ，是不调用 setter 函数的
// 如果再这样写
word = "hello"
println(word)
// 那么打印出来的就是 hello Mike
```

上面重写的 `setter` 中用到了 `field` 标识符，这个叫**幕后字段 Backing Fields**，只能用在属性的访问器内，并且不能直接声明，说白了就是只能直接拿来用，还有个概念叫**幕后属性 Backing Properties**，是不是绕晕了？没事，我们现在不需要知道，我们只要知道属性是需要声明的就行了。

来总结下为什么有时候需要重写 getter / setter

- 作用一：对相关方法进行 hook
- 作用二：修改读写规则
- 通过重写 getter ，可以让属性的值变成动态的

讲到这里，我们又要来玩玩 Java 和 Kotlin 对于 getter /setter 的互操作了

```java
// java
public class Dict {
    private String dict;
    public String getDict() {
        return dict;
    }
    public void setDict(String dict) {
        this.dict = dict;
    }
}
```

```kotlin
// kotlin
// setter 简化成了赋值
// 这里涉及到类的实例化，我们先有个印象，没有 'new' 关键字
Dict().word = "hello"
// getter 省略了 get 前缀
println(Dict().word)
```

那么反过来呢

```kotlin
// kotlin
class KotlinDict {
    var word: String? = null
}
```

```java
// java
KotlinDict kotlinDict = new KotlinDict();
// 自动生成了 getter / setter
kotlinDict.getWord();
kotlinDict.setWord("hello");
```

### var 和 val

前面讲了变量声明用 `var` ，除此之外还可以用 `val `，它是 value 的缩写。

`var` 就是 Java 中的变量，而 `val ` 是 Kotlin 增加的一种变量类型，叫**只读变量 Read-only variables** 。

- `val` 只能在初始化时赋值一次，之后再也不能赋值。类比 Java 中的 `final` 修饰的变量。
- 但 `val` 可以通过自定义 getter 来让自己的值动态变化，这是跟 Java 的 `final` 的不同。

```kotlin
var word1: String = "hello"
    get() {
        return "hello1"
    }
    set(value) {
        field = value + " world"
    }

// 如果重写 setter 会报错 a 'val' property cannot have a setter
val word2: String = "hello"
    get() {
        return "hello2"
    }
```

