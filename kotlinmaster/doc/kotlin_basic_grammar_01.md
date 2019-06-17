# Kotlin 基础语法（一）

Google 在 I/O 2019 上，宣布 Kotlin 成为 Android 的第一开发语言，这对于使用 Java 的开发者影响重大。意味着，将来很多的官方示例都会首选 Kotlin，同时，对 Kotlin 在开发、构建等各个方面的支持也会更优先。很多公司的移动开发岗已经把掌握 Kotlin 作为面试的考察点之一，甚至作为简历筛选的必要条件，学会并掌握 Kotlin 成了当下 Android 开发者的当务之急。「不会 Kotlin，Android 开发没人要了」、「Kotlin 真的有那么好吗」类似这些问题困扰着国内的 Android 开发者。鉴于 Kotlin 在国内还不算普及，学习一门新语言有不少成本，本系列文章，将会帮助国内开发者学习并掌握 Kotlin，是的，它非常棒。

## 搭建 Kotlin 开发环境

新版的 Android Studio 已经对 Kotlin 非常友好了，可以很方便地新建一个 Kotlin 项目，如图所示。

![](http://ww4.sinaimg.cn/large/006tNc79gy1g42sd40ajkj318o0rwmzs.jpg)

这里的语言选择 Kotlin 以便让 IDE 自动帮我们生成支持 Kotlin 的 `build.gradle` 文件。

重点看 `project root`  和 `app` 目录下的 `build.gradle` 文件和原来 Java 的有什么不同，这里把两个文件不同的地方写在了一起。

```groovy
// 项目根目录下 build.gradle
ext.kotlin_version = '1.3.31'
dependencies {
    classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
}

// app 目录下 build.gradle
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions' // 这个之后再讲，现在用不到

dependencies {
    // 因为 minSdkVersion 是 21 所以后缀加了 jdk7
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
}
```

如果是现有的项目要支持 Kotlin ，只需要在这两个目录的 `build.gradle` 对应加上上面这些就可以了。

这样配置好是额外支持 Kotlin，也就是说，原来支持的 Java 现在还是支持的。 

以上就是配置开发环境的所有内容，怎么样，很简单吧，Kotlin 语言设计的初衷就是让开发 Android 变得简单（话外音：谁不是呢）。

####Kotlin 一瞥

学习的开始阶段是模仿，在开始基础语法之前，先快速认识 IDE 帮我们创建好的 `MainActivity.kt` 这个文件。

![image-20190617155342369](http://ww3.sinaimg.cn/large/006tNc79gy1g447fyl29dj30ro0dwdhw.jpg)

和 Java 里的 `Activity` 长得还挺像的，也有 `package` `import` `class` 和貌似重载了的 `onCreate` 方法。

值得注意的一点是，没看到代码里有分号，事实上 Kotlin 中是**不需要分号**的。

Kotlin 文件是以 `.kt` 结尾的。我们先不在这个类里加代码，而是照葫芦画瓢新建一个。 

新建一个包 basic_grammar_01，包下新建一个 Kotlin Class 叫 `Sample.kt` 。

![](http://ww4.sinaimg.cn/large/006tNc79gy1g42sn90cj9j30bi02cjrg.jpg)

![](http://ww3.sinaimg.cn/large/006tNc79gy1g42sk3roflj314a08s40r.jpg)

![](http://ww2.sinaimg.cn/large/006tNc79gy1g42slcl158j30j80akta9.jpg)

我们看到，可以创建的不同的 Kotlin Kind  对应在 IDE 里的图标是不同的，这个在 Java 里也是一样的。

这里的各种 Kind 我们之后都会掌握，不用着急。

![image-20190616185118546](http://ww2.sinaimg.cn/large/006tNc79gy1g436ygeds7j30d40ao0tv.jpg)

Dict 是 Java Class ， 其他都是 Kotlin Class 。

准备工作完成，开始学习基础语法的旅程吧。

---

## 变量

### 声明与赋值

Kotlin 里面声明变量要用到关键字 `var` ，就是 variable 的缩写。

这里先给出一个正确的写法

![](http://ww4.sinaimg.cn/large/006tNc79gy1g42t0wy02lj30li08sgma.jpg)

对比 Java 主要有以下不同

- 类型和变量名位置互换
- 中间使用 `:` 分隔
- `var` 关键字开头
- 必须初始化

前面 3 点是 Kotlin 的格式，没什么理解门槛，最后一个，为什么要初始化？

因为 Kotlin 的变量没有默认值的，这点不像 Java，Java 的 field 有默认值，引用类型的默认 `null`，`int` 类型的默认 0，这些 Kotlin 没有。不过其实，Java 也只是 field 有默认值，local variable 也是没有默认值的。

Kotlin 把默认值的检查设计得更加严格了，认为开发者需要负责对变量进行初始化，这样设计的好处是明确一个变量的意义。

如果只声明变量而不给变量初始化，就像普罗米修斯创造了第一批人类的肉体，但是却没有雅典娜赋予他们的灵魂。

如果不进行初始化会怎么样呢？看看👇这么写

![](http://ww3.sinaimg.cn/large/006tNc79gy1g42t237dvoj30lq08qaax.jpg)

我们先把具体的错误放一边。

下面的**黑色粗体**文字是学习任何新的编程语言事半功倍的利器，我们在学习 Java 的时候也经常用到。

- **看懂 IDE 的报错（以下都简称报错）对于初学者来讲非常重要，我会对当前需要知道的知识点进行讲解，对暂时用不到的知识点咱们放到后面再讲解。**
- **IDE 是非常智能的，通常来讲会提供修复错误的解决方案（虽然有时候会有问题），理解 IDE 的解决方案，也是帮助我们更好地掌握知识的途径。**
- **为了跟国际接轨，尽量先给出英文原文，然后再进行中文解释。**

让我们回到上面这个错误，这个报错的意思是

> 属性需要在声明的同时初始化，除非你把它声明成抽象的。

哎，变量还能抽象的？嗯，这是 Kotlin 的功能，不过这里先不理它，后面会讲到。

IDE 告诉我们，这里声明的 `var` 叫属性 Property，这个概念相当于 Java 里的字段 field，但 Kotlin 里的 field 是另一个概念，之后再讲。

我们可以看到 IDE 给我们的修复提示

![](http://ww1.sinaimg.cn/large/006tNc79gy1g42tq5m3q6j30g60bgq4e.jpg)

apply 第一个修复提示后我们就在声明的同时进行初始化了

```kotlin
var count: Int = 0
```

在某些实际场景中我们在用的时候是不会让变量没有值的，但声明的时候确实还不知道该给它初始化什么默认值。

这种「我很确定我用的时候绝对不为空，但第一时间我没法给它赋值」的场景，Kotlin 给了我们一个选项：`lateinit` 。顾名思义，就是说我先告诉编译器我保证在使用它之前对它初始化，你不用担心它没有值的情况。

编译器说，好，既然你诚心诚意地保证了，我就大发慈悲地相信你，不在声明的时候检查了，之后如果有什么问题，后果自负（大声喊出好讨厌的感觉~~~）。

![image-20190616183757620](http://ww4.sinaimg.cn/large/006tNc79gy1g436kkfpecj30pu0b0abp.jpg)

可以看到 `view` 声明时使用了 `lateinit`，在 `onCreate` 中进行了初始化。

好，既然 `lateinit` 这么有用，快点带我装哔带我飞。

![image-20190616112010831](http://ww3.sinaimg.cn/large/006tNc79gy1g42tx200gcj30ms08o756.jpg)

帅不过 3 秒，又报错了（这里还是 IDE 的报错，下同），意思是

> Kotlin 里的 `Int` 仍然是 `基本类型` 的属性，关于基本类型后面再讲。不过换成其他类型比如 `String` 是没问题的，前面的 `View` 也没问题。

![image-20190616112307135](http://ww1.sinaimg.cn/large/006tNc79gy1g42u04vs8gj30gs06kwer.jpg)

那是不是每次都要添加变量类型呢，太麻烦了，Kotlin 还支持类型**自动推断 inferred** ，像这样：

```kotlin
var count = 1 // 这里根据右侧的 1 自动推断出 count 是 'Int' 类型
```

这是在编译期间完成的，这也是 Kotlin 作为静态类型语言的体现。

Java 里也有类型推断，比如👇

```java
// 右边省略了 <> 里面的 String
List<String> names = new ArrayList<>();
```

类型推断最直接的好处就是可以帮我们简化代码。

终于声明好一个变量了，我现在要对它进行重新赋值了。

`var` 声明的变量是可以被重新赋值的。

![image-20190616185918076](http://ww4.sinaimg.cn/large/006tNc79gy1g4376rlpwoj30cc090aai.jpg)

上面出现了 `fun` 关键字，我们在 `MainActivity` 中也看到过，这里简单提一下这个叫函数，`{}` 里面的是函数体，先不管它，我们在里面对 `count` 进行了重新赋值。

### 空安全设计

我们回忆一下 Java 里对变量赋值为空的场景

![image-20190616200434083](http://ww1.sinaimg.cn/large/006tNc79gy1g4392oq6fej30ji0dmgn0.jpg)

这里我们使用了 `@NonNull` 和 `@Nullable` 的注解来让 IDE 提示我们避免调用 `null` 对象，从而避免 NullPointerException。

下面的用法 IDE 就会给出警告。

![image-20190616200723042](http://ww4.sinaimg.cn/large/006tNc79gy1g4395m4bhnj30rg0augmv.jpg)

而到了 Kotlin 这里，就有了语言级别的默认支持，而且从警告变成了报错（编译失败）。

![image-20190616205212423](http://ww1.sinaimg.cn/large/006tNc79gy1g43ag96vt7j30ji0c8dgz.jpg)

在 Kotlin 里面，所有的变量都默认是不允许为空的，如果你给它赋值 null，就会报错，初始化的时候和后面再次赋值都不行，像👆那样。

这种有点强硬的要求，其实是很合理的：既然你声明了一个变量，就是要使用它对吧？那你把它赋值为 null 干嘛？要尽量让它有可用的值啊。

Java 在这方面很宽松，我们成了习惯，但 Kotlin 更强的限制其实在你熟悉了之后，是会减少很多运行时的问题的。

但我们都知道一个道理，做人留一线日后好想见。

有时候对于一个变量，确实存在可能为空值的场景，因为我们无法用一个默认值去解决。Kotlin 也为这种情况提供了语言层面的支持。

比如你要从服务器取一个 JSON 数据，并把它解析成一个 `Person` 对象。

![image-20190616210319947](http://ww2.sinaimg.cn/large/006tNc79gy1g43arv22ggj30s408g75o.jpg)

这个时候，空值就是有意义的。对于这些可以为空值的变量，你可以在类型右边加一个 `?` 号，解除它的非空限制，就像 IDE 的修复提示那样。

![image-20190616210502922](http://ww4.sinaimg.cn/large/006tNc79gy1g43atouiw9j30dk05mmx5.jpg)

加了 `?` 之后，一个 Kotlin 变量就像没有 `@NonNull` 注解的  Java 变量一样没有非空的限制，自由自在了。你除了在初始化的时候可以给它设置为空值，在代码里的任何地方也都可以。

不过，可空类型的变量会有新的问题：

由于对空引用的调用会导致空指针异常，所以 Kotlin 在可空变量直接调用的时候 IDE 会报错
![image-20190616210901577](http://ww2.sinaimg.cn/large/006tNc79gy1g43axqy9lvj30xc03smxq.jpg)

「可能为空」的变量，Kotlin 不允许用。那怎么办？用之前检查一下吧
![image-20190616211641446](http://ww2.sinaimg.cn/large/006tNc79gy1g43b5pxd10j313w04saau.jpg)

哎？这怎么还报错？这个报错的意思是你检查了非空也不能保证下面调用的时候就是非空（因为多线程情况下，其他线程可能把它再改成空的）。不过 Kotlin 里其实不是这么玩的，而是用 `?` ：
![image-20190616213711992](http://ww3.sinaimg.cn/large/006tNc79gy1g43br298o4j30hq01yjrh.jpg)
这个写法同样会对变量做一次非空确认之后再调用方法，不过这是 Kotlin 的写法，并且它可以做到线程安全，所以英文叫 safe call。

也就是说，如果你要在 Kotlin 里使用一个可能为空的变量，代码大概是这样的：

```kotlin
var view: View? = null
view?.setBackgroundColor(Color.RED)
```

除了 `?.` 这种 safe call 还有一种用 `!!.` 的叫 non-null asserted call (见上文调用可空变量时的 IDE 报错)。

既然有了 `?.` ，那为什么还要有 `!!.` 呢？有种既生瑜何生亮的感觉。

- `?.` safe call 表示「如果就」规则，如果不为空就执行 `?.` 后面的逻辑，运行期不会出现问题。
- `!!.` non-null asserted call 「肯定不空」的断言，相当于忽略可空提示的风险自担的强行调用，这种强行调用编译是通过了，但运行期可能会出现和断言不一致的情况（异常），断言更多是开发者希望某个时刻如果出现问题能够暴露出来，而不是像 safe call 那样隐蔽掉，另一个场景是参数的传递，这个后面再讲。

以上就是 Kotlin 的空安全设计，并且是兼容 Java 的。

这里的兼容主要是指 Java 中的 @Nullable 和 @NonNull 在 Kotlin 中会被当成可空和非空来对待，看👇这个例子。

```java
package org.kotlinmaster.basic_grammar_01;

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
// 因为初始化 initialize 是不调用 setter 的
// 如果接着写
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
    // 这里简单提一下 var 的可见性，默认是 public 的
    // 真的吗我不信.gif
    var word: String? = null
    // 那我们手动加一下
    public var word1: String = "i dont believe this var is public"
    // 这时候 IDE 马上给你提示了
    // Redundant visibility modifier
    // 告诉你 'public' 是多余的，我们 public 着呢
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

至此，变量部分告一段落了，当然本文无法涵盖所有知识点，希望同学们通过文章学会在实际的练习中掌握学习和解决问题的能力。知识是无穷尽的，方法论才是王道。

---

## 函数

前面在讲变量的时候我们看到，对变量的操作需要放在函数里。

Kotlin 中的函数和 Java 中的方法可以大体上理解成一个概念，其中的区别我们不用在意。

### 声明

Kotlin 中的函数使用 `fun` 关键字声明

```kotlin
fun incrementCount(): Unit {
}

// 上面的写法 IDE 会提示 Redundant 'Unit' return type
// 意思是返回值如果是 'Unit' 可以省略
// 类比 Java 中的 'void' 关键字
fun incrementCount() {
}

// 但是其他类型的返回值就不能省略，并且要加 return
fun incrementCount(): Int {
	return 2
}

// 来看看参数怎么写
fun incrementCount(count: Int): Int {
	return count + 1
}

// 参数和返回值都允许可空
fun incrementCount(count: Int?): Int? {
    // 下面这句的报错非常有意思了
    // Operator call conrresponds to a dot-qualified call 'count.plus(1)' which is not allowed on a nullable receiver 'count'.
	count + 1 ❌
    // 来解释下，就是说 '+' 在 kotlin 里是 operator call，关于 operator 的概念我们先有个印象
    // 知道下面这两句是等价的就行
    1 + 1
    1.plus(1)
    // 现在理解为什么 count + 1 会报错了吧，因为和下面是等价的
	count.plus(1) ❌
    count?. + 1 ❌ // 没有这种写法
    count!! + 1 // 等于把空判断交给了运行时，也不推荐
    return count?.plus(1) // 使用 safe call 的方式
}
```

来总结下 Kotlin 声明时和 Java 的不同点

- 返回类型和方法名位置互换
- 中间用 `: ` 分隔
- 参数的格式也和 Java 不一样
- 参数和返回类型也有「可空与不可空」
- 没有返回类型时，返回 `Unit` ，可以省略不写

我们再来看看刚才的例子

```kotlin
fun incrementCount(count: Int?): Int? {
    // 这里还有几个需要注意的地方
    // '?.' 和 '!!.' 这两种 call 本质上是函数调用，所以一定有返回值，返回值可以是 Unit
    // '?.' 根据如果就的规则，当 count 为空时，返回什么呢？这里其实返回的是 null ，对应函数的返回值 'Int?'
    return count?.plus(1)
}
```

### 构造函数

前面讲的 Kotlin 的 getter / setter 就是读写函数，现在我们还没讲到类，但是类的构造函数也是函数。

讲到这里，同学们可能又要提出问题了，那为什么这两种函数没有 `fun` 关键字声明呢？而且读写函数也没有声明返回值啊。

其实，这两种函数属于特殊的函数，读写函数总是和属性一起出现，构造函数总是和类一起出现。

我们只要知道，函数的 `{}` 内部的规则都是一致的就好。

## 类型

我们学习变量和函数的时候大部分用的都是 `Int` ，接下来我们正式介绍下 Kotlin 中的基本类型

