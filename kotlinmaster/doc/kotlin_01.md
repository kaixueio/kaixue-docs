# Kotlin 的变量、函数和类型

> 本期作者：
>
> 视频：扔物线（朱凯）
>
> 文章：hamber（罗琼）

大家好，我是扔物线，我唠叨两句就滚。绝不耽误你们看下面的视频。

欢迎大家来到码上开学 Kotlin 系列上手教程。大家久等了，其实我也早就被你们催得不想活了，奈何我事情太多啊。比如我要旅游吧？我要陪老婆吧？我要陪孩子吧？我要打孩子吧？我要打老婆吧？而且大家知道，我现在开了在线的 Android 进阶课程，我得花大量时间精力在课程上面吧？不然客户爸爸一个差评那我就要被团队砍死啊。

不过不管怎么样，码上开学终于开始生产了，而且我们是备了一些存货的哟。废话不多说，视频伺候！

<div class="aspect-ratio">
    <iframe src="https://player.bilibili.com/player.html?aid=64353734&cid=111733749&page=1&high_quality=1" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true"> </iframe>
</div>

*如果你看不到上面的哔哩哔哩视频，可以点击 [这里](https://youtu.be/z1OeXDRGMOg) 去 YouTube 看。*

> 以下内容来自文章作者 hamber。

在 Google I/O 2019 上，Google 宣布 Kotlin 成为 Android 的第一开发语言。这对于开发者来讲意味着，将来所有的官方示例会首选 Kotlin，并且 Google 对 Kotlin 在开发、构建等各个方面的支持也会更优先。

在这个大环境下，Kotlin 已经作为很多公司的移动开发岗面试的考察点之一，甚至作为 HR 简历筛选的必要条件。因此，学会并掌握 Kotlin 成了 Android 开发者的当务之急。

「Kotlin 真的有那么好吗」「到底要不要学 Kotlin」这样的问题很快就要过时了，码上开学这个项目的目的并不在于向各位安利 Kotlin，而在于怎样让希望学习 Kotlin 的人最快速地上手。

我们的目的非常明确：这是一份给 Android 工程师的 Kotlin 上手指南。其中：

- 这是一份上手指南，而不是技术文档。所以：
  - 我们会带着你一步步地、有节奏地学习，让你轻松愉快地学会 Kotlin；
  - 但这里不会有完整的 API 清单。如果你想查看 API，可以去看 Kotlin 官方文档。
- 这虽然只是一份「上手」指南，我们也不会刻意展示过于深入的内容，但所有你需要了解的技术细节，一个都不会少。
- 我们针对的是 Android 工程师，因此所有的视频和文章讲解以及示例代码，全都会以 Android 开发场景为基础，所用的开发环境也是 Android Studio。如果这份指南能顺便让一些其他领域的 Java 开发者获益当然更好，但仅仅是顺便:joy:。
- 讲解中呈现的代码段部分，我会以「👆」「👇」「👈」「👉」形式标注代码中需要关注的地方，并以「…」省略了读者暂时不需要关心的代码。
- Android 开发者使用 Android Studio 作为开发的 IDE，以下所有的 IDE 都是指 Android Studio。

## 为项目添加 Kotlin 语言的支持

学习 Kotlin 的第一步就是要为项目添加 Kotlin 语言的支持，这非常简单。

### 新建支持 Kotlin 的 Android 项目

如果你要新建一个支持 Kotlin 的 Android 项目，只需要如下操作：

- File -> New -> New Project …
- Choose your project -> Phone and Tablet -> Empty Activity
- Configure your project  -> Language 选择 「Kotlin」

别的都和创建一个普通的 Android 项目一样，创建出的项目就会是基于 Kotlin 的了。

所谓「基于 Kotlin」，意思有两点：

1. IDE 帮你自动创建出的 `MainActivity` 是用 Kotlin 写的：

   ```kotlin
   package org.kotlinmaster
   
   import android.os.Bundle
   import androidx.appcompat.app.AppCompatActivity
   
   class MainActivity : AppCompatActivity() {
       ...
   }
   ```
   
   > 扫一眼就好，不用读代码，我们后面都会讲。
   
2. 项目中的 2 个 `bulid.gradle` 文件比 Java 的 Android 项目多了几行代码（以「👇」标注），它们的作用是添加 Kotlin 的依赖：

   - 项目根目录下的 `build.gradle`：

     ```groovy
     buildscript {
         👇
         ext.kotlin_version = '1.3.41'
         repositories {
             ...
         }
         dependencies {
             classpath 'com.android.tools.build:gradle:3.5.0-beta05'
             👇
             classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
         }
     }
     ```

   - app 目录下的 `build.gradle`：

     ```groovy
     apply plugin: 'com.android.application'
     👇
     apply plugin: 'kotlin-android'
     ...
     
     android {
         ...
     }
     
     dependencies {
         implementation fileTree(dir: 'libs', include: ['*.jar'])
         👇
         implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
         ...
     }
     
     ```

也就是说，如果你是要创建一个新项目，记得把语言选择为 Kotlin，项目创建完成后你就可以用 Kotlin 来写它了。

### 给现有项目添加 Kotlin 支持

如果是现有的项目要支持 Kotlin，只需要像上面这样操作，把两个 `build.gradle`  中标注的代码对应贴到你的项目里就可以了。

笔者建议刚开始学习的时候还是新建一个基于 Kotlin 的项目，按照上面的步骤练习一下。

### 初识 MainActivity.kt

前面我们提到，如果新建的项目是基于 Kotlin 的，IDE 会帮我们创建好 `MainActivity`，它其实是有一个 `.kt` 的文件后缀名（打开的时候可以看到）。

> Kotlin 文件都是以 `.kt` 结尾的，就像 Java 文件是以 `.java` 结尾。

我们看看这个 `MainActivity.kt` 里到底有些什么：

```kotlin
package org.kotlinmaster
  👆
import android.os.Bundle
  👆
import androidx.appcompat.app.AppCompatActivity
                  👇
class MainActivity : AppCompatActivity() {
  👆
       👇    👇                            👇     👇
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
```

乍一看，「👆」标注的 `package` `import` `class` 这些 Java 里的东西，Kotlin 也有；但是也有一些以「👇」标注的在 Java 里是没见过的。

为了暂时避开这些干扰，我们自己新建一个文件。

- 在新建 Java Class 的入口下面可以看见一个叫 「Kotlin File/Class」 的选项，这就是我们新建 Kotlin 文件的入口
- New Kotlin File/Class
    - Name: Sample
    - Kind: Class

创建完成后的 `Sample.kt`：

```kotlin
package org.kotlinmaster

class Sample {}
```

这个类仅包含 `package` 和 `class` 两个关键字，我们暂时先看成和 Java 差不多（其实真的就是差不多）的概念，这样就都是我们熟悉的东西了。

接下来，让我们开始学习基础语法吧。

---

## 变量

### 变量的声明与赋值

> 这里讲一个 Java 和 Kotlin 命名由来的小插曲。
>
> 我们知道 Java 就是著名的爪哇岛，爪哇岛盛产咖啡，据说就是一群研究出 Java 语言的牛人们在为它命名时由于闻到香浓的咖啡味，遂决定采用此名称。
>
> Kotlin 来源于芬兰湾中的 Kotlin 岛。
>
> 因此，我们在代码段的开头以「☕️」来表示 Java 代码段，「🏝️」来表示 Kotlin 代码段。

我们回忆下 Java 里声明一个 View 类型的变量的写法：

```java
☕️
View v;
```

Kotlin 里声明一个变量的格式是这样的：

```kotlin
🏝️
var v: View
```

这里有几处不同：

- 有一个 `var` 关键字
- 类型和变量名位置互换了
- 中间是用冒号分隔的
- 结尾没有分号（对，Kotlin 里面不需要分号）

看上去只是语法格式有些不同，但如果真这么写，IDE 会报错：

```kotlin
🏝️
class Sample {
    var v: View
    // 👆这样写 IDE 会报如下错误
    // Property must be initialized or be abstract
}
```

这个提示是在说，属性需要在声明的同时初始化，除非你把它声明成抽象的。

- 那什么是属性呢？这里我们可以简单类比 Java 的 field 来理解 Kotlin 的 Property，虽然它们其实有些不一样，Kotlin 的 Property 功能会多些。

- 变量居然还能声明成抽象的？嗯，这是 Kotlin 的功能，不过这里先不理它，后面会讲到。

属性为什么要求初始化呢？因为 Kotlin 的变量是没有默认值的，这点不像 Java，Java 的 field 有默认值：

```java
☕️
String name; // 👈默认值是 null
int count; // 👈默认值是 0
```

但这些 Kotlin 是没有的。不过其实，Java 也只是 field 有默认值，局部变量也是没有默认值的，如果不给它初始值也会报错：

```java
☕️
void run() {
    int count;
    count++; 
    // 👆IDE 报错，Variable 'count' might not have been initialized
}
```

既然这样，那我们就给它一个默认值 null 吧，遗憾的是你会发现仍然报错。

```kotlin
🏝️
class Sample {
    var v: View = null
    // 👆这样写 IDE 仍然会报错，Null can not be a value of a non-null type View
}
```

又不行，IDE 告诉我需要赋一个非空的值给它才行，怎么办？Java 的那套不管用了。

其实这都是 Kotlin 的空安全设计相关的内容。很多人尝试上手 Kotlin 之后快速放弃，就是因为搞不明白它的空安全设计，导致代码各种拒绝编译，最终只能放弃。所以咱先别急，我先来给你讲一下 Kotlin 的空安全设计。

### Kotlin 的空安全设计

简单来说就是通过 IDE 的提示来避免调用 null 对象，从而避免 NullPointerException。其实在 androidx 里就有支持的，用一个注解就可以标记变量是否可能为空，然后 IDE 会帮助检测和提示，我们来看下面这段 Java 代码：

```java
☕️
@NonNull
View view = null;
// 👆IDE 会提示警告，'null' is assigned to a variable that is annotated with @NotNull
```

而到了 Kotlin 这里，就有了语言级别的默认支持，而且提示的级别从 warning 变成了 error（拒绝编译）：

```kotlin
🏝️
var view: View = null
// 👆IDE 会提示错误，Null can not be a value of a non-null type View
```

在 Kotlin 里面，所有的变量默认都是不允许为空的，如果你给它赋值 null，就会报错，像上面那样。

这种有点强硬的要求，其实是很合理的：既然你声明了一个变量，就是要使用它对吧？那你把它赋值为 null 干嘛？要尽量让它有可用的值啊。Java 在这方面很宽松，我们成了习惯，但 Kotlin 更强的限制其实在你熟悉了之后，是会减少很多运行时的问题的。

不过，还是有些场景，变量的值真的无法保证空与否，比如你要从服务器取一个 JSON 数据，并把它解析成一个 User 对象：

```kotlin
🏝️
class User {
    var name: String = null // 👈这样写会报错，但该变量无法保证空与否
}
```

这个时候，空值就是有意义的。对于这些可以为空值的变量，你可以在类型右边加一个 `?` 号，解除它的非空限制：

```kotlin
🏝️
class User {
    var name: String? = null
}
```

加了问号之后，一个 Kotlin 变量就像 Java 变量一样没有非空的限制，自由自在了。

你除了在初始化的时候可以给它赋值为空值，在代码里的任何地方也都可以：

```kotlin
🏝️
var name: String? = "Mike"
...
name = null // 👈原来不是空值，赋值为空值
```

这种类型之后加 `?` 的写法，在 Kotlin 里叫**可空类型**。

不过，当我们使用了可空类型的变量后，会有新的问题：

由于对空引用的调用会导致空指针异常，所以 Kotlin 在可空变量直接调用的时候 IDE 会报错：

```kotlin
🏝️
var view: View? = null
view.setBackgroundColor(Color.RED)
// 👆这样写会报错，Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type View?
```

「可能为空」的变量，Kotlin 不允许用。那怎么办？我们尝试用之前检查一下，但似乎 IDE 不接受这种做法：

```kotlin
🏝️
if (view != null) {
    view.setBackgroundColor(Color.RED)
    // 👆这样写会报错，Smart cast to 'View' is impossible, because 'view' is a mutable property that could have been changed by this time
} 
```

这个报错的意思是即使你检查了非空也不能保证下面调用的时候就是非空，因为在多线程情况下，其他线程可能把它再改成空的。

那么 Kotlin 里是这么解决这个问题的呢？它用的不是 `.` 而是 `?.`：

```kotlin
🏝️
view?.setBackgroundColor(Color.RED)
```

这个写法同样会对变量做一次非空确认之后再调用方法，这是 Kotlin 的写法，并且它可以做到线程安全，因此这种写法叫做「**safe call**」。

另外还有一种双感叹号的用法：

```kotlin
🏝️
view!!.setBackgroundColor(Color.RED)
```

意思是告诉编译器，我保证这里的 view 一定是非空的，编译器你不要帮我做检查了，有什么后果我自己承担。这种「肯定不会为空」的断言式的调用叫做 「**non-null asserted call**」。一旦用了非空断言，实际上和 Java 就没什么两样了，但也就享受不到 Kotlin 的空安全设计带来的好处（在编译时做检查，而不是运行时抛异常）了。

以上就是 Kotlin 的空安全设计。

理解了它之后再来看变量声明，跟 Java 虽然完全不一样，只是写法上不同而已。

很多人在上手的时候都被变量声明搞懵，原因就是 Kotlin 的空安全设计所导致的这些报错：

- 变量需要手动初始化，如果不初始化的话会报错；
- 变量默认非空，所以初始化赋值 null 的话报错，之后再次赋值为 null 也会报错；
- 变量用 `?` 设置为可空的时候，使用的时候因为「可能为空」又报错。

明白了空安全设计的原理后，就很容易能够解决上面的问题了。

关于空安全，最重要的是记住一点：所谓「可空不可空」，关注的全都是使用的时候，即「这个变量在使用时是否可能为空」。

另外，Kotlin 的这种空安全设计在与 Java 的互相调用上是完全兼容的，这里的兼容指：

- Java 里面的 @Nullable 注解，在 Kotlin 里调用时同样需要使用 `?.`。

    ```java
    ☕️
    @Nullable
    String name;
    ```

    ```kotlin
    🏝️
    name?.length
    ```

- Java 里面的 @Nullable 和 @NonNull 注解，在转换成 Kotlin 后对应的就是可空变量和不可空变量，至于怎么将 Java 代码转换为 Kotlin，Android Studio 给我们提供了很方便的工具（但并不完美），后面会讲。

    ```java
    ☕️
    @Nullable
    String name;
    @NonNull
    String value = "hello";
    ```

    ```kotlin
    🏝️
    var name: String? = null
    var value: String = "hello"
    ```

空安全我们讲了这么多，但是有些时候我们声明一个变量是不会让它为空的，比如 view，其实在实际场景中我们希望它一直是非空的，可空并没有业务上的实际意义，使用 `?.` 影响代码可读性。

但如果你在 `MainActivity`  里这么写：

```kotlin
🏝️
class MainActivity : AppCompatActivity() {
    👇
    var view: View = findViewById(R.id.tvContent)
}
```

虽然编译器不会报错，但程序一旦运行起来就 crash 了，原因是 findViewById() 是在 onCreate 之后才能调用。

那怎么办呢？其实我们很想告诉编译器「我很确定我用的时候绝对不为空，但第一时间我没法给它赋值」。

Kotlin 给我们提供了一个选项：延迟初始化。

### 延迟初始化

具体是这么写的：

```kotlin
🏝️
lateinit var view: View
```

这个 `lateinit` 的意思是：告诉编译器我没法第一时间就初始化，但我肯定会在使用它之前完成初始化的。

它的作用就是让 IDE 不要对这个变量检查初始化和报错。换句话说，加了这个 `lateinit` 关键字，这个变量的初始化就全靠你自己了，编译器不帮你检查了。

然后我们就可以在 onCreate 中进行初始化了：

```kotlin
🏝️
👇
lateinit var view: View
override fun onCreate(...) {
    ...
    👇
    view = findViewById(R.id.tvContent)
}
```

### 类型推断

Kotlin 有个很方便的地方是，如果你在声明的时候就赋值，那不写变量类型也行：

```kotlin
🏝️
var name: String = "Mike"
👇
var name = "Mike"
```

这个特性叫做「类型推断」，它跟动态类型是不一样的，我们不能像使用 Groovy 或者 JavaScript 那样使用在 Kotlin 里这么写：

```kotlin
🏝️
var name = "Mike"
name = 1
// 👆会报错，The integer literal does not conform to the expected type String
```

```groovy
// Groovy
def a = "haha"
a = 1
// 👆这种先赋值字符串再赋值数字的方式在 Groovy 里是可以的
```

「动态类型」是指变量的类型在运行时可以改变；而「类型推断」是你在代码里不用写变量类型，编译器在编译的时候会帮你补上。因此，Kotlin 是一门静态语言。

除了变量赋值这个场景，类型推断的其他场景我们之后也会遇到。

### val 和 var

声明变量的方式也不止 var 一种，我们还可以使用 val：

```kotlin
🏝️
val size = 18
```

val 是 Kotlin 在 Java 的「变量」类型之外，又增加的一种变量类型：只读变量。它只能赋值一次，不能修改。而 var 是一种可读可写变量。

> var 是 variable 的缩写，val 是 value 的缩写。

val 和 Java 中的 final 类似：

```java
☕️
final int size = 18;
```

不过其实它们还是有些不一样的，这个我们之后再讲。总之直接进行重新赋值是不行的。

### 可见性

看到这里，我们似乎都没有在 Kotlin 里看到类似 Java 里的 public、protected、private 这些表示变量可见性的修饰符，因为在 Kotlin 里变量默认就是 **public** 的，而对于其他可见性修饰符，我们之后会讲，这里先不用关心。

至此，我相信你对变量这部分已经了解得差不多了，可以根据前面的例子动手尝试尝试。

## 函数

Kotlin 除了变量声明外，函数的声明方式也和 Java 的方法不一样。Java 的方法（method）在 Kotlin 里叫函数（function），其实没啥区别，或者说其中的区别我们可以忽略掉。对任何编程语言来讲，变量就是用来存储数据，而函数就是用来处理数据。

### 函数的声明

我们先来看看 Java 里的方法是怎么写的：

```java
☕️
Food cook(String name) {
    ...
}
```

而到了 Kotlin，函数的声明是这样：

```kotlin
🏝️
👇                      👇
fun cook(name: String): Food {
    ...
}
```

- 以 fun 关键字开头
- 返回值写在了函数和参数后面

那如果没有返回值该怎么办？Java 里是返回 void：

```java
☕️
void main() {
   ...
}
```

Kotlin 里是返回 Unit，并且可以省略：

```kotlin
🏝️
            👇
fun main(): Unit {}
// Unit 返回类型可以省略
fun main() {}
```

函数参数也可以有可空的控制，根据前面说的空安全设计，在传递时需要注意：

```kotlin
🏝️
// 👇可空变量传给不可空参数，报错
var myName : String? = "rengwuxian"
fun cook(name: String) : Food {}
cook(myName)
  
// 👇可空变量传给可空参数，正常运行
var myName : String? = "rengwuxian"
fun cook(name: String?) : Food {}
cook(myName)

// 👇不可空变量传给不可空参数，正常运行
var myName : String = "rengwuxian"
fun cook(name: String) : Food {}
cook(myName)
```

### 可见性

函数如果不加可见性修饰符的话，默认的可见范围和变量一样也是 public 的，但有一种情况例外，这里简单提一下，就是遇到了 `override` 关键字的时候，下面会讲到。

### 属性的 getter/setter 函数

我们知道，在 Java 里面的 field 经常会带有 getter/setter 函数：

```java
☕️
public class User {
    String name;
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
```

它们的作用就是可以自定义函数内部实现来达到「钩子」的效果，比如下面这种：

```java
☕️
public class User {
    String name;
    public String getName() {
        return this.name + " nb";
    }
    public void setName(String name) {
        this.name = "Cute " + name;
    }
}
```

在 Kotlin 里，这种 getter / setter 是怎么运作的呢？

```kotlin
🏝️
class User {
    var name = "Mike"
    fun run() {
        name = "Mary"
        // 👆的写法实际上是👇这么调用的
        // setName("Mary")
        // 建议自己试试，IDE 的代码补全功能会在你打出 setn 的时候直接提示 name 而不是 setName
        
        println(name)
        // 👆的写法实际上是👇这么调用的
        // print(getName())
        // IDE 的代码补全功能会在你打出 getn 的时候直接提示 name 而不是 getName
    }
}
```

那么我们如何来操作前面提到的「钩子」呢？看下面这段代码：

```kotlin
🏝️
class User {
    var name = "Mike"
        👇
        get() {
            return field + " nb"
        }
        👇   👇 
        set(value) {
            field = "Cute " + value
        }
}
```

格式上和 Java 有一些区别：

- getter / setter 函数有了专门的关键字 get 和 set
- getter / setter 函数位于 var 所声明的变量下面
- setter 函数参数是 value

除此之外还多了一个叫 field 的东西。这个东西叫做「**Backing Field**」，中文翻译是**幕后字段**或**后备字段**（马云背后的女人😝）。具体来说，你的这个代码：

```kotlin
🏝️
class Kotlin {
  var name = "kaixue.io"
}
```

在编译后的字节码大致等价于这样的 Java 代码：

```java
☕️
public final class Kotlin {
   @NotNull
   private String name = "kaixue.io";

   @NotNull
   public final String getName() {
      return this.name;
   }

   public final void setName(@NotNull String name) {
      this.name = name;
   }
}
```

上面的那个 `String name` 就是 Kotlin 帮我们自动创建的一个 Java field。这个 field 对编码的人不可见，但会自动应用于 getter 和 setter，因此它被命名为「Backing Field」（backing 的意思是在背后进行支持，例如你闯了大祸，我动用能量来保住你的人头，我就是在 back you）。

所以，虽然 Kotlin 的这个 `field` 本质上确实是一个 Java 中的 field，但对于 Kotlin 的语法来讲，它和 Java 里面的 field 完全不是一个概念。在 Kotlin 里，它相当于每一个 var 内部的一个变量。

我们前面讲过 val 是只读变量，只读的意思就是说 val 声明的变量不能进行重新赋值，也就是说不能调用 setter 函数，因此，val 声明的变量是不能重写 setter 函数的，但它可以重写 getter 函数：

```kotlin
🏝️
val name = "Mike"
    get() {
        return field + " nb"
    }
```

val 所声明的只读变量，在取值的时候仍然可能被修改，这也是和 Java 里的 final 的不同之处。

关于「钩子」的作用，除了修改取值和赋值，也可以加一些自己的逻辑，就像我们在 Activity 的生命周期函数里做的事情一样。

## 类型

讲完了变量和函数，接下来我们可以系统性地学习下 Kotlin 里的类型。

### 基本类型

在 Kotlin 中，所有东西都是对象，Kotlin 中使用的基本类型有：数字、字符、布尔值、数组与字符串。

```kotlin
🏝️
var number: Int = 1 // 👈还有 Double Float Long Short Byte 都类似
var c: Char = 'c'
var b: Boolean = true
var array: IntArray = intArrayOf(1, 2) // 👈类似的还有 FloatArray DoubleArray CharArray 等，intArrayOf 是 Kotlin 的 built-in 函数
var str: String = "string"
```

这里有两个地方和 Java 不太一样：

- Kotlin 里的 Int 和 Java 里的 int 以及 Integer 不同，主要是在装箱方面不同。

    Java 里的 int 是 unbox 的，而 Integer 是 box 的：

    ```java
    ☕️
    int a = 1;
    Integer b = 2; // 👈会被自动装箱 autoboxing
    ```

    Kotlin 里，Int 是否装箱根据场合来定：

    ```kotlin
    🏝️
    var a: Int = 1 // unbox
    var b: Int? = 2 // box
    var list: List<Int> = listOf(1, 2) // box
    ```
    Kotlin 在语言层面简化了 Java 中的 int 和 Integer，但是我们对是否装箱的场景还是要有一个概念，因为这个牵涉到程序运行时的性能开销。

    因此在日常的使用中，对于 Int 这样的基本类型，尽量用不可空变量。

- Java 中的数组和 Kotlin 中的数组的写法也有区别：

    ```java
    ☕️
    int[] array = new int[] {1, 2};
    ```

    而在 Kotlin 里，上面的写法是这样的：

    ```kotlin
    🏝️
    var array: IntArray = intArrayOf(1, 2)
    // 👆这种也是 unbox 的
    ```

简单来说，原先在 Java 里的基本类型，类比到 Kotlin 里面，条件满足如下之一就不装箱：

- 不可空类型。

- 使用 IntArray、FloatArray 等。

### 类和对象

现在可以来看看我们的老朋友 `MainActivity` 了，重新认识下它：

```kotlin
🏝️
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
    }
}
```

我们可以对比 Java 的代码来看有哪些不同：

```java
☕️
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ...
    }
}
```

- 首先是类的可见性，Java 中的 public 在 Kotlin 中可以省略，Kotlin 的类默认是 public 的。

- 类的继承的写法，Java 里用的是 `extends`，而在 Kotlin 里使用 `:`，但其实 `:` 不仅可以表示继承，还可以表示 Java 中的 `implement`。

    举个例子，假设我们有一个 interface 叫 Impl：

    ```kotlin
    🏝️
    interface Impl {}
    ```

    > Kotlin 里定义一个 interface 和 Java 没什么区别。

    ```java
    ☕️
    public class Main2Activity extends AppCompatActivity implements Impl { }
    ```

    ```kotlin
    🏝️
    class MainActivity : AppCompatActivity(), Impl {}
    ```

- 构造方法的写法不同。

    - Java 里省略了默认的构造函数：

    - ```java
        ☕️
        public class MainActivity extends AppCompatActivity {
            // 👇默认构造函数
            public MainActivity() {
            }
        }
        ```

    - Kotlin 里我们注意到  AppCompatActivity 后面的 `()`，这其实也是一种省略的写法，等价于：

    - ```kotlin
        🏝️                   
        class MainActivity constructor() : AppCompatActivity() {
                                👆
        }
        ```

        不过其实更像 Java 的写法是这样的：

        ```kotlin
        🏝️
        // 👇注意这里 AppCompatActivity 后面没有 '()'
        class MainActivity : AppCompatActivity {
            constructor() {
            }
        }
        ```

        Kotlin 把构造函数单独用了一个 `constructor` 关键字来和其他的 `fun` 做区分。
    
- override 的不同

    - Java 里面 `@Override` 是注解的形式。
    - Kotlin 里的 `override` 变成了关键字。
    - Kotlin 省略了 `protected` 关键字，也就是说，Kotlin 里的 `override` 函数的可见性是继承自父类的。

除了以上这些明显的不同之外，还有一些不同点从上面的代码里看不出来，但当你写一个类去继承 `MainActivity` 时就会发现：

- Kotlin 里的 MainActivity 无法继承：

    ```kotlin
    🏝️
    // 👇写法会报错，This type is final, so it cannot be inherited from
    class NewActivity: MainActivity() {
    }
    ```

    原因是 Kotlin 里的类默认是 final 的，而 Java 里只有加了 `final ` 关键字的类才是 final 的。

    那么有什么办法解除 final 限制么？我们可以使用 `open` 来做这件事：

    ```kotlin
    🏝️
    open class MainActivity : AppCompatActivity() {}
    ```

    这样一来，我们就可以继承了。

    ```kotlin
    🏝️
    class NewActivity: MainActivity() {}
    ```

    但是要注意，此时 NewActivity 仍然是 final 的，也就是说，`open` 没有父类到子类的遗传性。

    而刚才说到的 `override` 是有遗传性的：

    ```kotlin
    🏝️
    class NewActivity : MainActivity() {
        // 👇onCreate 仍然是 override 的
        override fun onCreate(savedInstanceState: Bundle?) {
            ...
        }
    }
    ```

    如果要关闭 `override` 的遗传性，只需要这样即可：

    ```kotlin
    🏝️
    open class MainActivity : AppCompatActivity() {
        // 👇加了 final 关键字，作用和 Java 里面一样，关闭了 override 的遗传性
        final override fun onCreate(savedInstanceState: Bundle?) {
            ...
        }
    }
    ```

- Kotlin 里除了新增了 `open` 关键字之外，也有和 Java 一样的 `abstract` 关键字，这俩关键字的区别就是 `abstract` 关键字修饰的类无法直接实例化，并且通常来说会和 `abstract` 修饰的函数一起出现，当然，也可以没有这个 `abstract` 函数。

    ```kotlin
    🏝️
    abstract class MainActivity : AppCompatActivity() {
        abstract fun test()
    }
    ```

    但是子类如果要实例化，还是需要实现这个 abstract 函数的：

    ```kotlin
    🏝️
    class NewActivity : MainActivity() {
        override fun test() {}
    }
    ```

当我们声明好一个类之后，我们就可以实例化它了，实例化在 Java 中使用 `new` 关键字：

```java
☕️
void main() {
    Activity activity = new NewActivity(); 
}
```

而在 Kotlin 中，实例化一个对象更加简单，没有 `new` 关键字：

```kotlin
🏝️
fun main() {
    var activity: Activity = NewActivity()
}
```

通过 `MainActivity` 的学习，我们知道了 Java 和 Kotlin 中关于类的声明主要关注以下几个方面：

- 类的可见性和开放性
- 构造方法
- 继承
- override 函数

### 类型的判断和强转

刚才讲的实例化的例子中，我们实际上是把子类对象赋值给父类的变量，这个概念在 Java 里叫多态，Kotlin 也有这个特性，但在实际工作中我们很可能会遇到需要使用子类才有的函数。

比如我们先在子类中定义一个函数：

```kotlin
🏝️
class NewActivity : MainActivity() {
    fun action() {}
}
```

那么接下来这么写是无法调用该函数的：

```kotlin
🏝️
fun main() {
    var activity: Activity = NewActivity()
    // 👆activity 是无法调用 NewActivity 的 action 方法的
}
```

在 Java 里，需要先使用 `instanceof` 关键字判断类型，再通过强转来调用：

```java
☕️
void main() {
    Activity activity = new NewActivity();
    if (activity instanceof NewActivity) {
        ((NewActivity) activity).action();
    }
}
```

Kotlin 里同样有类似解决方案，使用 `is` 关键字进行「类型判断」，并且因为编译器能够进行类型推断，可以帮助我们省略强转的写法：

```kotlin
🏝️
fun main() {
    var activity: Activity = NewActivity()
    if (activity is NewActivity) {
        // 👇的强转由于类型推断被省略了
        activity.action()
    }
}
```

那么能不能不进行类型判断，直接进行强转调用呢？可以使用 `as` 关键字：

```kotlin
🏝️
fun main() {
    var activity: Activity = NewActivity()
    (activity as NewActivity).action()
}
```

这种写法如果强转类型操作是正确的当然没问题，但如果强转成一个错误的类型，程序就会抛出一个异常。

我们更希望能进行安全的强转，可以更优雅地处理强转出错的情况。

这一点，Kotlin 在设计上自然也考虑到了，我们可以使用 `as?` 来解决：

```kotlin
🏝️
fun main() {
    var activity: Activity = NewActivity()
    // 👇'(activity as? NewActivity)' 之后是一个可空类型的对象，所以，需要使用 '?.' 来调用
    (activity as? NewActivity)?.action()
}
```

它的意思就是说如果强转成功就执行之后的调用，如果强转不成功就不执行。

---

好了，关于 Kotlin 的变量、函数和类型的内容就讲到这里，给你留 2 道思考题吧：

1. 子类重写父类的 `override` 函数，能否修改它的可见性？

2. 以下的写法有什么区别？

    ```kotlin
    🏝️
    activity as? NewActivity
    activity as NewActivity?
    activity as? NewActivity?
    ```

## 练习题

1. 使用 Android Studio 创建一个基于 Kotlin 的新项目（Empty Activity），添加一个新的属性（类型是非空的 `View`），在 onCreate 函数中初始化它。
2. 声明一个参数为 `View?` 类型的方法，传入刚才的 `View` 类型属性，并在该方法中打印出该 `View?` 的 id。

### 作者介绍

#### 视频作者

##### 扔物线（朱凯）

- 码上开学创始人、项目管理人、内容模块规划者和视频内容作者。
- <a href="https://developers.google.com/experts/people/kai-zhu" target="_blank">Android GDE</a>（ Google 认证 Android 开发专家），前 Flipboard Android 工程师。 
- GitHub 全球 Java 排名第 92 位，在 <a href="https://github.com/rengwuxian" target="_blank">GitHub</a> 上有 6.6k followers 和 9.9k stars。
- 个人的 Android 开源库 <a href="https://github.com/rengwuxian/MaterialEditText/" target="_blank">MaterialEditText</a> 被全球多个项目引用，其中包括在全球拥有 5 亿用户的新闻阅读软件 Flipboard 。
- 曾多次在 Google Developer Group Beijing 线下分享会中担任 Android 部分的讲师。
- 个人技术文章《<a href="https://gank.io/post/560e15be2dca930e00da1083" target="_blank">给 Android 开发者的 RxJava 详解</a>》发布后，在国内多个公司和团队内部被转发分享和作为团队技术会议的主要资料来源，以及逆向传播到了美国一些如 Google 、 Uber 等公司的部分华人团队。
- 创办的 Android 高级进阶教学网站 [HenCoder](https://hencoder.com) 在全球华人 Android 开发社区享有相当的影响力。
- 之后创办 Android 高级开发教学课程 [HenCoder Plus](https://plus.hencoder.com) ，学员遍布全球，有来自阿里、头条、华为、腾讯等知名一线互联网公司，也有来自中国台湾、日本、美国等地区的资深软件工程师。

#### 文章作者

##### hamber（罗琼）

即刻 Android 负责人。2015 年加入即刻，主导了即刻 Android App 2.0 到 6.0 版本的架构设计和产品迭代。多年 Android 开发经验，曾就职于美团点评、华为，对客户端用户体验和性能优化有丰富的实践经验。2019 Droidcon Shanghai Speaker。