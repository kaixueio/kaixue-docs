# Kotlin 基础语法（一）

Google 在 I/O 2019 上，宣布 Kotlin 成为 Android 的第一开发语言，对于开发者来讲意味着，将来更多的官方示例会首选 Kotlin，Google 对 Kotlin 在开发、构建等各个方面的支持也会更优先。

在这个大环境下，很多公司的移动开发岗也已经把 Kotlin 作为面试的考察点之一，甚至作为简历筛选的必要条件，学会并掌握 Kotlin 成了 Android 开发者的当务之急。

「Kotlin 好难学啊」、「Kotlin 真的有那么好吗」相信屏幕前的你也有这些疑问。鉴于 Kotlin 在国内还不算普及，而学习一门新语言有不少成本，本系列文章，将会帮助国内开发者学习并掌握 Kotlin。

## 搭建 Kotlin 开发环境

新版的 Android Studio 对 Kotlin 非常友好，可以很方便地新建一个 Kotlin 项目，如图所示。

![](http://ww4.sinaimg.cn/large/006tNc79gy1g42sd40ajkj318o0rwmzs.jpg)

这里的语言选择 Kotlin 以便让 IDE 自动帮我们生成支持 Kotlin 的 `build.gradle` 文件。

重点看 `project root`  和 `app` 目录下的 `build.gradle` 文件和原来 Java 的有什么不同，这里把两个文件不同的地方写在了一起。

```groovy
// project root dir build.gradle start
ext.kotlin_version = '1.3.31'
dependencies {
    classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
}
// project root dir build.gradle end

// app dir build.gradle start
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions' // 这个之后再讲，现在用不到

dependencies {
    // 因为 minSdkVersion 是 21 所以后缀加了 jdk7
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
}
// app dir build.gradle end
```

如果是现有的项目要支持 Kotlin ，只需要在这两个目录的 `build.gradle` 对应加上上面这些就可以了。

这样配置好之后 Android Studio 额外支持 Kotlin，也就是说，原来支持的 Java 现在还是支持的。 

以上就是配置开发环境的所有内容，怎么样，很简单吧，Kotlin 语言设计的初衷就是让开发 Android 变得简单（话外音：哪个语言不是呢）。

#### Kotlin 一瞥

学习的最初阶段是模仿，因此我们先快速认识下 IDE 帮我们创建好的 MainActivity.kt 这个文件。

![image-20190617155342369](http://ww3.sinaimg.cn/large/006tNc79gy1g447fyl29dj30ro0dwdhw.jpg)

乍一看，和 Java 里的 `Activity` 长得还挺像的，也有 `package`、 `import`、 `class` 这些关键字，还有熟悉的生命周期函数 `onCreate`，不过看起来比相同的 Java 代码量要少很多。

Kotlin 的文件名是以 `.kt` 结尾的，而 Java 是以 `.java` 结尾。

值得注意的一点是，没看到代码里有分号，事实上 Kotlin 中是**不需要分号**的。

是不是有点跃跃欲试了？我们自己来搞一个吧。

在同样新建 Java Class 的入口发现一个叫 Kotlin File/Class 的选项，这就是我们新建 Kotlin 文件的入口。

![](http://ww3.sinaimg.cn/large/006tNc79gy1g42sk3roflj314a08s40r.jpg)

这里我们先不理其他选项（放心，以后都会掌握的），选择新建 Class，叫 Sample.kt。

![](http://ww2.sinaimg.cn/large/006tNc79gy1g42slcl158j30j80akta9.jpg)

创建完成后的 Sample.kt 内容如下

```kotlin
package org.kotlinmaster

class Sample {

}
```

好了，这样我们就有一个自己写的类了。「Kotlin 也没想象得这么难嘛」，的确如此，就像张无忌先学九阳神功再练乾坤大挪移那样，有 Java 基础还怕学不会 Kotlin ？

再回到项目中，除了 MainActivity.kt 之外，IDE 还帮我们创建一个叫 ExampleUnitTest.kt 的类。

![image-20190617162010177](http://ww1.sinaimg.cn/large/006tNc79gy1g4487iekfcj312c0i0goa.jpg)

Java 里面我们也用过单元测试 JUnit，这里我们先跑一下试试。

![image-20190617162536155](http://ww2.sinaimg.cn/large/006tNc79gy1g448d5owbkj30pe0o4gpu.jpg)

```shell
Process finished with exit code 0
```

随着命令行打印结果，我们完成了 Kotlin 的一次非常棒的旅程。

这就算正式上手 Kotlin 了，小结下：

- 一个 Android 项目要支持 Kotlin，需要在 `project root`  和 `app` 目录下的 `build.gradle` 中加上对应的依赖。
- Kotlin 文件以 `.kt` 结尾，加入一个 Kotlin 文件和加入一个 Java 文件一样简单。
- Kotlin 单元测试和 Java 单元测试一样，上手即用。

到这里，准备工作就全部完成了，让我们一起开始学习基础语法吧。

---

## 变量

### 变量的声明与赋值

Kotlin 里声明一个变量要这么写：

```kotlin
var count: Int
```

但如果真这么写，会报错：

![image-20190617165855987](http://ww2.sinaimg.cn/large/006tNc79gy1g449btvvy2j30fm05kwes.jpg)

这个提示是在说，属性需要在声明的同时初始化，除非你把它声明成抽象的。

> Java 里的 field 在 Kotlin 里叫 Property，不过它们确实不一样，Kotlin 的 Property 功能会多些。

哎，变量还能抽象的？嗯，这是 Kotlin 的功能，不过这里先不理它，后面会讲到。

为什么要初始化？因为 Kotlin 的变量没有默认值的，这点不像 Java，Java 的 field 有默认值，比如引用类型的默认 null，`int` 类型的默认 0，但这些 Kotlin 是没有的。不过其实，Java 也只是 field 有默认值，local variable 也是没有默认值的。

> Kotlin 把默认值的检查设计得更加严格了，开发者需要负责对变量进行初始化，这样设计是为了明确一个变量的意义。

如果只声明变量而不给变量初始化，就像普罗米修斯创造了第一批人类的肉体，但是却没有雅典娜赋予他们的灵魂。

好，那我们就给它注入一个灵魂，不是，给它一个默认值。

```kotlin
var count: Int = 0
```

一个 `Int` 类型可以给它初始化为 0，那如果是 Android 的 `View` 类型呢？

```kotlin
var view: View
```

哎呀，不知道填啥，干脆给它一个 null 吧。

![image-20190617170655503](http://ww1.sinaimg.cn/large/006tNc79gy1g449k53pmwj30ie05a74p.jpg)

啊又不行，告诉我需要赋一个非空的值给它才行，怎么办？

那就给它一个默认值呗。

![image-20190617170851107](http://ww1.sinaimg.cn/large/006tNc79gy1g449m6oz57j30pi09qta7.jpg)

哎不对啊， `findViewById()` 现在还不能用的，需要写在 `onCreate()` 里面。那那那，怎么办？

好麻烦，完全没有搭建环境时的「就这个 feel 倍儿爽」的感觉了。Kotlin 你好，Kotlin 再见。

> 其实这都是 Kotlin 的空安全设计相关的内容。很多人尝试上手 Kotlin 之后快速放弃，就是因为搞不明白它的空安全设计，导致代码各种拒绝编译，最终光速放弃。所以咱先别猴急，我先来给你讲一下 Kotlin 的空安全。

### Kotlin 的空安全

简单来说就是通过 IDE 的提示来避免调用 null 对象，从而避免 NullPointerException。其实 androidx 就有的，用一个注解就可以标记变量是否可能为空，然后 IDE 会帮助检测和提示：

![image-20190601205535431](http://ww2.sinaimg.cn/large/006tNc79gy1g449qaiknfj30wq0620te.jpg)

而到了 Kotlin 这里，就有了语言级别的默认支持，而且从警告变成了报错（编译失败）：

![image-20190601201609731](http://ww3.sinaimg.cn/large/006tNc79gy1g449r3lqm1j30ni04kwf1.jpg)

在 Kotlin 里面，所有的变量都默认是不允许为空的，如果你给它赋值 null，就会报错，像上面那样。

> 这种有点强硬的要求，其实是很合理的：既然你声明了一个变量，就是要使用它对吧？那你把它赋值为 null 干嘛？要尽量让它有可用的值啊。Java 在这方面很宽松，我们成了习惯，但 Kotlin 更强的限制其实在你熟悉了之后，是会减少很多运行时的问题的。

另外，这个 View 其实在实际场景中我们是不会让它为空的对吧？这种「我很确定我用的时候绝对不为空，但第一时间我没法给它赋值」的场景，Kotlin 给了我们一个选项：`lateinit`：

```kotlin
lateinit var view: View
```

这个 `lateinit` 的意思很简单：我没法第一时间就初始化，但我肯定会在使用它之前完成初始化的。

它的作用也很直接：让 IDE 不要对这个变量检查初始化和报错。换句话说，加了这个关键字，这个变量的初始化就全靠你自己了，编译器不帮你了。编译器说，好，既然你诚心诚意地保证了，我就大发慈悲地相信你，不在声明的时候检查了，之后如果有什么问题，后果自负。

![âå¥½è®¨åçæè§âçå¾çæç´¢ç»æ](http://ww3.sinaimg.cn/large/006tNc79gy1g44di2yzezj30hs0hsgmq.jpg)

不过，还是有些场景，变量的值真的无法保证空与否，比如你要从服务器取一个 JSON 数据，并把它解析成一个 `User` 对象：

![image-20190604173433383](http://ww4.sinaimg.cn/large/006tNc79gy1g449vf0i82j30sm0480sv.jpg)

这个时候，空值就是有意义的。对于这些可以为空值的变量，你可以在类型右边加一个 `?` 号，解除它的非空限制：

![image-20190604173503812](http://ww1.sinaimg.cn/large/006tNc79gy1g449w2j9z9j30oi03kt8u.jpg)

也就是，加了问号之后，一个 Kotlin 变量就像 Java 变量一样没有非空的限制，自由自在了。你除了在初始化的时候可以给他设置为空值，在代码里的任何地方也都可以。不过，可空类型的变量会有新的问题：

由于对空引用的调用会导致空指针异常，所以 Kotlin 在可空变量直接调用的时候 IDE 会报错：

![image-20190601213017496](http://ww2.sinaimg.cn/large/006tNc79gy1g44am69gsmj314e050wfc.jpg)

「可能为空」的变量，Kotlin 不允许用。那怎么办？用之前检查一下吧：

![image-20190601213438929](http://ww3.sinaimg.cn/large/006tNc79gy1g44anzrjp3j31ao066jsi.jpg)

哎？这怎么还报错？这个报错的意思是即使你检查了非空也不能保证下面调用的时候就是非空（多线程情况下，其他线程可能把它再改成空的）。

不过 Kotlin 里其实不是这么玩的，而是用 `?`：

```kotlin
view?.setBackgroundColor(Color.RED)
```

它的意思是说，如果 view 不为空，就执行 `?.` 后面的逻辑。

也就是说，如果你要在 Kotlin 里使用一个可能为空的变量，代码大概是这样的：

```kotlin
var view: View? = null

...

view?.setBackgroundColor(Color.RED)
```

这个写法同样会对变量做一次非空确认之后再调用方法，不过这是 Kotlin 的写法，并且它可以做到线程安全，所以 `?.` 的写法又叫做  **safe call**。

另外还有一种双感叹号的用法：

```kotlin
view!!.setBackgroundColor(Color.RED)
```

意思是告诉编译器，我保证这里的 view 一定是非空的，那么编译器也就不会帮你检查了，这种「肯定不会为空」的断言式的调用叫做 **non-null asserted call**。一旦这么用了，那就是风险自担的强行调用，就像唐僧念紧箍咒逼走孙悟空一样，没有了孙悟空的保护，你唐僧只能自求多福了。

![âåå§ ç´§ç®å å­æç©º ä¸æç½éª¨ç²¾âçå¾çæç´¢ç»æ](http://ww4.sinaimg.cn/large/006tNc79gy1g44djk8tyxj3085064aa1.jpg)

以上就是 Kotlin 的变量及空安全设计。它的声明跟 Java 虽然完全不一样，但是学习起来完全没难度的，因为只是一个风格而已。但很多人在上手的时候都被变量声明搞懵，原因就是 Kotlin 的空安全设计所导致的这些报错：

- 变量需要手动初始化，所以不初始化的话报错；
- 变量默认非空，所以初始化赋值 null 的话报错；
    - 之后再次赋值为 null 也会报错；
- 变量用 `?` 设置为可空的时候，使用的时候因为「可能为空」又报错。

关于空安全，最重要的是记住一点：所谓「可空不可空」，关注的全都是使用的时候，即「这个变量在使用时是否可能为空」。

另外，Kotlin 的空安全设计是完全兼容 Java 的，换句话说，Java 里面的 @NonNull 和 @Nullable 注解，在 Kotlin 里调用时同样会触发编译器的空安全检查。

![image-20190617194307505](http://ww4.sinaimg.cn/large/006tNc79gy1g44e2o21h3j30e005yjrp.jpg)

![image-20190617194247150](http://ww1.sinaimg.cn/large/006tNc79gy1g44e2bg9xgj30x603qq3c.jpg)

### 再谈变量声明与赋值

理解了空安全设计之后，我们再来看看变量声明的一些变种用法。之所以叫变种，是因为对编译器来讲，最终转换成字节码都是一样的，区别只是我们写代码的时候格式不一样而已。

![ççåï¼æä¸ä¿¡ï¼](http://ww3.sinaimg.cn/large/006tNc79gy1g44dff0yqrg308r062dxs.gif)

好，我证明给你看。

![image-20190617195523390](http://ww2.sinaimg.cn/large/006tNc79gy1g44efff9cxj30ci03omxb.jpg)

哇，原来还可以省略类型啊（准确来讲是连同 `:` 和类型一起省略了）。

这种「省略类型」的写法叫**类型推断**，其实 Java 里也有，比如：

```java
// 右边省略了 <> 里面的 String
List<String> names = new ArrayList<>();
```

类型推断最直接的好处就是可以帮我们简化代码。

> 省略返回类型不等于动态类型，Kotlin 依然是静态类型的。

Kotlin 和 Java 都是 JVM 语言，最终都会编译成字节码。

这里可以使用 Android Studio 提供的查看 Kotlin 字节码的工具：

![image-20190617194808959](http://ww3.sinaimg.cn/large/006tNc79gy1g44e7wxzzvj30w00qu4cy.jpg)

转换之后

![image-20190617195607981](http://ww2.sinaimg.cn/large/006tNc79gy1g44eg7ku8bj313k0jw0vv.jpg)

字节码看不懂啊，没关系，我们点击 **Decompile** 按钮再反编译处理下。

![image-20190617195636558](http://ww1.sinaimg.cn/large/006tNc79gy1g44egq71xlj30iq0iamzc.jpg)

反编译出来了一个 Java 文件，我们看到一个 Sample 的类，里面果然是声明了 word1 和 word2。

这下你相信了吧，在之后的讲解中，只要是提到某个 Kotlin 语法的变种写法，都可以通过这个转换工具反编译成 Java 文件来查看。

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

