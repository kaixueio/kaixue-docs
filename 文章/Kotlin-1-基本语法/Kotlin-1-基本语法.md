## 原文链接：

https://gitee.com/CnPeng_1/CnPengKotlin/blob/master/%E7%A0%81%E4%B8%8A%E5%BC%80%E5%AD%A6-Kotlin.md

## 同步时间：

2019-02-18 16:08



以下是正文：

-----------------------------------------





在[《Kotlin开篇》](https://kaixue.io/kotlin-overview/)中，我们对 Kotlin 有了一个大致的了解—— Kotlin 是一个基于JVM的新的编程语言，由 JetBrains 开发。与 Java 相比，Kotlin 的语法更简洁、更具表达性，而且提供了更多的特性，比如，高阶函数、操作符重载、字符串模板。它与 Java 高度可互操作，可以同时用在一个项目中。

在学习 Kotlin 基础部分的时候，我们会与 Java 做相应的对比，有了对比你会更加喜欢 Kotlin !

接下来，我们就从头开始，细致的了解一下这门语言。

## 一、Kotlin编译环境搭建

### 1、编译环境介绍

所谓编译环境，就是运行我们编写完的 Kotlin 代码的一个工具。Kotlin 支持多种编译器 ——

先来一张 Kotlin 官网中对支持编译器的截图：

![](https://images.gitee.com/uploads/images/2019/0129/115313_ceaece5e_930142.png)


在上图中，我们可以得知，编译 Kotlin 时可以使用 [Intellij IDEA](https://www.jetbrains.com/idea/)、[AndroidStudio](https://developer.android.google.cn/studio/index.html)、[Eclipse](http://www.eclipse.org/downloads/)、[Complier ](https://github.com/JetBrains/kotlin/releases/tag/v1.1.2-2)。

对于上面四种编译途径，各自有如下特点：

* Intellij IDEA 中已经集成了 Kotlin 编译环境
* AndroidStudio 3.X 也集成了 Kotlin 编译环境
* Eclipse 需要安装对应的 kotlin 插件才可以
* Complier 是纯命令行编译环境

四种编译器对应的下载页面分别为：

编译器|下载地址
---|---
 Intellij IDEA    | [https://www.jetbrains.com/idea/](https://www.jetbrains.com/idea/)
AndroidStudio   | [https://developer.android.google.cn/studio/index.html](https://developer.android.google.cn/studio/index.html)
Eclipse|[http://www.eclipse.org/downloads/](http://www.eclipse.org/downloads/)
Complier|[https://github.com/JetBrains/kotlin/releases/tag/v1.1.2-2](https://github.com/JetBrains/kotlin/releases/tag/v1.1.2-2)

### 2、搭建编译环境

我们在这里主要介绍 AndroidStudio 和 Intellij IDEA.

#### (1)、AndroidStudio 编译环境的搭建

> 本节介绍的是如何在 AndroidStudio 中新建一个基于 Kotlin 的 Android 项目。如果你想编译和运行后面章节中的 Kotlin 代码以查看运行结果，请在 Intellij IDEA 中尝试，安装步骤将在下一节中介绍。

AndroidStudio 的编译需要JDK ，在下载 AndroidStudio 的时候可以选择带 JDK 版本的（或者自己单独下载 JDK）

安装步骤省略。

下面的图例会引导你一步步的新建一个基于 Kotlin 的 Android 项目。

![打开AndroidStudio并创建一个新的AndroidStudio项目 ](https://images.gitee.com/uploads/images/2019/0128/084036_64014519_930142.png)

![输入项目名称、选择项目路径、勾选kotlin支持](https://images.gitee.com/uploads/images/2019/0128/084036_5d8a7306_930142.png)

![选择设备](https://images.gitee.com/uploads/images/2019/0128/084036_604ede3f_930142.png)

![创建空页面.png](https://images.gitee.com/uploads/images/2019/0128/084036_774f5a8d_930142.png)

![输入页面名称](https://images.gitee.com/uploads/images/2019/0128/084036_ca8c887b_930142.png)

![创建完成，展示自动生成的kotlin代码](https://images.gitee.com/uploads/images/2019/0128/084037_8814b69f_930142.png)

到此，一个基于 Kotlin 的 Android 项目已经创建完成。


#### (2)、Intellij IDEA 编译环境搭建

> 如果你想编译和运行后面章节中的 Kotlin 示例代码，推荐安装 IDEA 。文中后续的代码都是基于 IDEA 编写和运行的

下载及安装的过程省略。

需要说明的是，IEDA分为 Ultimate 旗舰版 和 Community 社区版，前者主要针对 Web 和 企业级 开发，是收费的，试用期一个月；社区版是免费开源的，所以，学习阶段可以直接下载社区版的。

![初次开启](https://images.gitee.com/uploads/images/2019/0129/115313_c6aa9540_930142.png)

上图中编号的含义分别如下：
* 1、创建一个新的项目
* 2、导入已有项目
* 3、打开本地项目
* 4、从版本控制软件中拉取

我们选择 1 新建一个项目，然后按照下图操作：

![新建kotlin项目](https://images.gitee.com/uploads/images/2019/0128/084032_8ae7ef46_930142.png)

![创建项目名称并选择路径](https://images.gitee.com/uploads/images/2019/0128/084033_a872ed2c_930142.png)


![正在初始化](https://images.gitee.com/uploads/images/2019/0128/084032_17fbf560_930142.png)

![新建kotlin文件](https://images.gitee.com/uploads/images/2019/0128/084032_f9e29990_930142.png)

![为文件命名](https://images.gitee.com/uploads/images/2019/0128/084033_fae49625_930142.png)

![编辑内容](https://images.gitee.com/uploads/images/2019/0128/084034_59383b0e_930142.png)

![运行程序](https://images.gitee.com/uploads/images/2019/0128/084033_38eb4ced_930142.png)

![大功告成](https://images.gitee.com/uploads/images/2019/0128/084034_2a88d435_930142.png)

好了，基于 IDEA 的编译环境构建完成了。

### 3、本章小结：

本章中我们介绍了 Kotlin 支持的编译环境，以及基于 IDEA 和 AndroidStudio 的编译环境的基本搭建步骤，并运行了第一个 Kotlin 程序 —— 打印了 ”HelloWorld“。

如果你没有看懂我们输入的那一段代码是啥意思，不要着急，我们会慢慢带你了解的。

在 Kotlin 基础部分的后续章节中我们将基于 IDEA 编写和运行相关的代码。


## 二、函数基础

函数 (funcation) 也叫 方法，可以类比数学里的各种公式。我们想获取某个结果时，直接调用对应的公式即可。我们在调用公式时传递的数据被称为 **参数**；而公式的运行结果，就是函数的 **返回值**。

本章节将介绍函数的声明格式、函数的返回值类型。其中会牵涉到 `数据类型` 和 `for循环`，如果对这两个东西不了解也不用着急，后面会有对应介绍。

### 1、函数声明的基本格式

#### (1)、Kotlin 函数的基本格式

在上一篇文章中，我们在创建 Intellij IDEA 编译环境时已经写过 HelloWorld 代码，具体代码为：

```java
fun main(args: Array<String>) {
    println("HelloWorld")
}
```

那么，接下来我们来解析一下这个 main 函数（也可以称为 main 方法）的格式，具体如下：

字段|含义
---|---
fun | 用来声明一个函数，表示它后面的内容是一个函数，
main | 函数的具体名称。名称自定义，你想叫啥就叫啥
args | 该函数的参数。如果某个函数有多个参数，使用逗号间隔
：| 声明参数类型的声明符。参数和类型之间必须用：链接
Array<String>|参数类型，这里表示 args 的类型是 String 类型的 Array 数组
{ } | 用来包裹函数的主体内容
println ("HelloWorld") | 这是函数主体，你想让函数实现什么功能就写出对应的代码即可

以上就是 函数声明的基本格式，以后我们在声明函数的时候也需要遵守上面的格式。


### 2、函数的返回值类型

我们在定义一个函数之后，有时候是需要通获取运行结果并使用，这些运行结果会有对应的数据类型（具体下一节会有介绍），这就是函数的返回值类型。

#### (1）、函数无返回值

在kotlin中，如果某个函数不需要返回数据，那么这个函数的类型就是 **Unit**。也就是说，我们前面说的 main 函数的完整写法应该是：

```
fun main(args: Array<String>): Unit {
    println("HelloWorld")
}
```

但是，通常情况下， 如果某个函数没有返回值，那么 **Unit** 可以省略。

#### (2)、函数有返回值

##### A、显示声明返回值类型

如果某个函数有返回值，那么就需要在函数声明中声明其返回值类型，示例代码如下：

```java
fun sum(a: Int, b: Int): Int {
    return a + b
}
```

在上面的代码中，我们定义了一个 求和的函数，接收两个 Int 类型的参数，返回值是两个参数的和，由于得到的 和 是 Int 类型的数据，所以该函数的返回值类型就是Int，这个返回值类型就需要在函数中声明，否则会报错，报错状态如下：

![返回值类型不匹配](https://images.gitee.com/uploads/images/2019/0129/115312_d58dcaf1_930142.png)

##### B、隐式声明返回值类型

对于上面 显示声明返回值类型 中的示例代码，还有一种简写方式，如下：

```
fun sum(a: Int, b: Int) = a + b
```

在上面的示例代码中，直接将表达式作为函数体，然后 kotlin 会自动根据 a+b 的类型来确定 sum 函数的返回值类型，这也就是 **类型推断**(也可以称为：类型推导)。

函数调用示例：

```
fun main(args: Array<String>) {
    println(sum(5, 6))
}

fun sum(a: Int, b: Int) = a + b
```
运行上面代码之后我们就会得到 5+6 的和。

### 3、参数长度可变的函数

所谓参数长度可变的函数，就是说，在调用这个函数的时候我们可以根据实际需要传入参数，参数的个数由我们的具体需要决定，可能是1个，也可能是2 个或3个等等。

参数长度可变的函数在声明的时候需要使用关键字 **vararg**，示例如下：

```
//可变参数关键词：vararg。下面这个函数接收可变参数，参数的具体类型为Int
fun varList(vararg vars: Int) { 
 	 //遍历输入的参数
    for (v in vars) {            
        println(v)
    }
}
```

完整实例代码及运行结果：

```
fun main(args: Array<String>) {

    varList(1)
    println("上面调用时传入一个参数，下面调用时传入多个参数")
    varList(1, 2, 3, 4)
}

fun varList(vararg vars: Int) {
    for (v in vars) {
        println(v)
    }
}
```

![运行结果](https://images.gitee.com/uploads/images/2019/0129/115312_b740bd6b_930142.png)

### 4、与Java函数的对比

在对比之前，我们先了解一下 main() 函数。

main() 函数是程序的入口，我们把编写的程序编译运行之后，编译器会先找 main 函数，如果有 main 函数，就会执行其中的内容；如果没有 main 函数，那么就只会编译，无法运行。

前面我们已经了解了 Kotlin 中 函数的声明格式，那么我们在对比看一下 Java 中的函数。

```java
public static void main(String[] args) {
	System.out.println("打印");
}
```

瞧，同样是打印一个 HelloWorld，Java 中编写的内容明显的看着比 Kotlin 中的要多，那么，在这个函数中，两者的区别具体是怎样的呢？

* Java 语句末尾必须有分号`;`表示语句到此结束；Kotlin中不需要这个语句分号
* Java 函数中的 public 表示可见属性为公开的；而 Kotlin 中默认函数就是 public 的，所以，可以省略
* Java 中函数的返回值类型定义在 函数名之前；而 Kotlin 中返回值类型定义在函数末尾，遵循的格式是 `可见类型 函数名(参数) 返回值`——这样在语义层面更容易阅读。
* Java 中函数的参数是先声明类型，然后声明参数名称；Kotlin 中先声明名称，再声明类型。



### 5、Kotlin中的注释

注释就是对代码片段的说明，通常用来描述代码片段的作用以及使用该代码片段时的注意事项。

Kotlin 支持 `单行注释`和`多行注释`。（此处，与 Java 注释相同）

类型|含义
---|---
// |单行注释。用来描述内容前面。后面的内容不能换行，如果产生了换行，必须在新的一行前面添加新的//
/*  */ |多行注释。描述的内容被包裹在其中，描述内容可以换行
/** */ |文档注释。通常用来描述类和方法。

示例代码：

```java

/**
 * main函数是程序的入口
 * @param args 接收一个String数组作为参数
 */
fun main(args: Array<String>) {
    //这是一个单行注释,不支持换行
    //单行注释不支持换行，换行时必须在新的一行前面添加//
    println("HelloWorld")

    /*
    * 这是多行注释，内容被包裹在中间
    * 支持换行
    */
    println("HelloKotlin")
}
```

### 6、小结

本章主要介绍了 Kotlin 中函数的声明格式，包括有返回值的声明格式，无返回值的声明格式。并且与 Java 中的声明格式做了简要对比。

重点理解 隐式声明返回值类型。

其他函数的高阶用法，在后续的 函数 部分会有详细讲解。


## 三、变量

声明变量的目的是方便我们传递和调用数据。


### 1、变量的声明

声明变量时需要遵循： `修饰符 变量名:类型 = 值` 的格式。

修饰符有 `var`(对应单词 variables) 和 `val`(对应单词 value) 两种。

被 `var` 修饰的变量可以多次赋值，被 `val` 修饰的变量只能赋值一次。


举一个例子：

```
fun sum(a: Int) {
    var value1:Int = a
    value1 = a + 1
    
    val value2:Int = 5
    //下面这句代码会报错，被 val 修饰的变量只能赋值一次 
    //value2 = 6

    print(value1+value2)
}
```

上述函数中，

我们声明了一个名称 sum 的函数，它接收一个 Int 类型的参数：a。在该函数的函数体(被大括号包裹的内容称为函数体)中我们用 `var value1:Int = a` 声明了变量 value1，用 `val value2:Int = 5` 声明了变量 value2 , 然后将打印 value1+value2 的和出来。

由于 value1 被 `var` 修饰，所以，我们通过 `value1=a+1` 为它再次赋值时不会报错。

但是，value2 被 `val` 修饰，我们通过 `value2 = 6` 为它再次赋值时会报错。


### 2、const val

`const val` 是一个比较特殊的概念。

它主要有三个特点：

* 位于 .kt 文件顶层(就是类的外部) 或者 被 `object` 修饰的单例类 内部，或者被 `companion` 修饰的伴生对象内部
* 其值只能是 `String` 或 其他基本数据类型
* 没有自定义的 getter 方法

（关于 单例、伴生对象、数据类型、getter 方法等内容后续会有介绍，此处只需要先记住 `const val`的特点即可。）

如：

```
//顶层的 const val
const val str = "哈哈哈哈"

//报错：Const val has type User,only primitives or String are allowed
//const val user: User = User("李四")

class User(val uName: String) {
    //伴生对象内部的 const val
    companion object {
        const val age = 15
    }

}

object School {
    //单例类中的 const val
    const val schoolName = "张三"
}
```

### 3、命名规则

Kotlin 的命名规范与 Java 一致。

* 以字母、数字、下划线组成
* 定义变量名时首字母小写，定义类名时首字母大小。
* 遵从驼峰规则（单词首字母大写，其他小写）

其他更为细致的规则不再赘述


### 4、与Java中的对比

在 Java 中定义一个同样功能的 sum 函数：

```java
public void sum(int a) {
	int value1 = a;
        value1 = a + 1
	
        final int value2 = 5;
	    
	System.out.println(""+(value1+value2));
}
```

* Java 中声明变量量时，遵循的格式为：`修饰符 类型 变量名=值`
* Java 中变量不需要修饰符，kotlin 中则需要使用 var 或 val 修饰。
* 被 val 修饰的变量等同于Java 中被 final 修饰的变量。


### 4、小结

本章介绍了变量的特点，以及如何声明变量。

其实，变量又可以分为 `局部变量` 和 `全局变量`。定义在函数内部的称为 局部变量；定义在类中的称为 全局变量（类的概念在后续章节会介绍）。所以，本章节中的例子实际是局部变量。


## 四、基本数据类型

数据类型，就是数据的分类。

比如 1、2、3 它们都是整数，所以，它们的数据类型就是整型——Int。

Kotlin 中的基本数据类型包括：数值类型，字符类型，字符串类型，布尔类型。

### 1、数值类型

Kotlin 中数值类型可以细分为：Byte、Short、Int、Long、Float、Double。它们都用来表示数值，区别在于表示的数值范围和数值精度。具体如下：

类型|含义|位宽|取值范围(最小值~最大值)
---|---|---|---
Byte| 整数| 8|-128 ~ 127
Short|整数|16|-32768 ~ 32767
Int |整数|32|-2147483648 ~ 2147483647
Long|整数|64|-9223372036854775808 ~ 9223372036854775807
Float|单精度小数|32|1.4E-45  ~  3.4028235E38
Double|双精度小数|64|4.9E-324 ~ 1.7976931348623157E308

#### (1)、数值的表示方式
  * Long 类型数据通常会在末尾加 L 或 l 后缀，如 123L，123l，**建议用L**

  * Float 类型数据通常会在末尾加 F 或 f 后缀，如 12.3F，12.3f，  如果一个小数 **不加后缀 F 或 f , 则默认为是 Double类型**

 * kotlin中的数值可以用二进制、十进制、十六进制表示，但 **不支持八进制！！**。所以 十进制的 3 可以用如下三种方式表示：
   * `二进制: 0b00000011`
   * `十进制: 3` 
   * `十六进制: 0x3`


#### (2)、用下划线增加大数据的易读性

先来看这么一个数据， 1234500000，这是 十二亿三千四百五十万， 我们看到这么一个数据的时候，基本都会去挨个的从左到右的数，个十百千万十万百万千万亿，然后才知道这个数据到底是多少。

对于例子中这种值比较大的数据，我们读起来很费劲，但是，**kotlin 1.1 版本之后我们可以使用 下划线 _ 来链接较大的数值** ，我们可以每隔三位或者四位加一个下划线，这样，我们在读数据的时候就能一目了然了。
        
示例如下：

```java
val oneMillion = 1_000_000
val creditCardNumber = 1234_5678_9012_3456L
val socialSecurityNumber = 999_99_9999L
val hexBytes = 0xFF_EC_DE_5E
val bytes = 0b11010010_01101001_10010100_10010010
```
#### (3)、数值类型之间的转换

##### A、显示类型转换

先来看一段代码：

![将Byte 值直接赋值给 Int类型](https://images.gitee.com/uploads/images/2019/0129/115312_ec092ba1_930142.png)

在上图中，我们看到，当我们将 Byte 类型的 a 赋值给 Int 类型的 b 时程序爆红了，那么我们该怎么解决呢？很简单：a.toInt( )，参考下图：

![Byte 转为 Int型](https://images.gitee.com/uploads/images/2019/0129/115312_7237ee0c_930142.png)

那么，如果我将 Int 数据赋值给 Byte 呢？

![Int 数据赋值给 Byte](https://images.gitee.com/uploads/images/2019/0129/115313_42d1d1db_930142.png)

瞧，还是报错，那么怎么解决呢？a.toByte() 

![Int 转为Byte](https://images.gitee.com/uploads/images/2019/0129/115313_5c671ed8_930142.png)

通过上面的示例我们可以得出如下结论：

**当不同数据类型的变量之间进行赋值时，必须进行类型转换。**

所以，kotlin给我们提供了如下转换方法：

方法名|作用
---|---
 toByte()|将数据转为Byte类型
 toShort()| 将数据转为Short
 toInt()|将数据转为Int
 toLong()|将数据转为 Long
 toFloat()| 将数据转为Float
 toDouble()| 将数据转为Double 
 toChar()| 将数据转为Char
 toString()|将数据转为String

> 注意：如果将表示范围更大的数据转换为范围小的数据类型，可能会丢失精度。

##### B、隐式类型转换和类型推断

在上面的代码中，我们在显示声明了 b 的类型，所以在给他赋值时，如果类型不一致则必须进行显示类型转换，但是，如果我们没有给 b 声明具体的类型呢？

![未声明 b 的类型](https://images.gitee.com/uploads/images/2019/0129/115313_120e500d_930142.png)

瞧，没有报错，这是类型推断的作用。这种情况下，b 的类型就是 a 的类型。

另外，在下面的代码中，同时包含了隐式类型转换和类型推断。

```java
// Long + Int => Long。
// 低精度的数据与高精度的数据运算时，默认会把参与运算的低精度数据转换为高精度数据。
val result = 1L + 3   
```
在上面这个示例中，1L 表示一个 Long 类型的数据，3 表示一个 Int 类型的数据，当低精度数据和高精度数据一起参与运算时，编译器会将低精度先转换为高精度数据，然后再参与运算，这就是隐式类型转换。而且，虽然我们没有主动声明 result 的类型，但是编译器会根据后面的值动态判断出它的类型为 Long。

通过上面两个示例，相信你已经知道什么是类型推断和隐式类型转换了，可以概括为如下：

* **类型推断 ： 在声明变量时，不需要主动声明其类型，而是让编译器根据它的值自动推断类型的一种形式。**
* **隐式类型转换 ： 不同精度类型的数据共同参与运算时，编译器主动将低精度数据转为高精度数据的操作。**

类型推断也叫类型推导。


### 2、字符类型

Koltin中用 **单引号** `‘ ’` 括起来的单个内容被称为字符。

kotlin中字符用 **Char** 类型表示。


**注意：**
**与Java不同，在 kotlin 中 Char 类型不能直接当作数字参与算术运算！！** 看下图：

![字符不能直接参与与数值的算术运算](https://images.gitee.com/uploads/images/2019/0129/115313_4e5017cc_930142.png)

在上图中，我们让 数值 3 和 字符 ‘1’ 直接进行算术运算，在 + 下面爆红了！！！因为，Char 不能直接参与算术运算。而在 Java 中 每个 Char 都有一个对应的数值，可以直接参与算术运算。

在 kotlin 中，如果我们想让 Char 参与算术运算，需要先做类型转换，示例代码如下：

```java
fun main(args: Array<String>) {
    // '1'.toInt() 得到 49，
    val b = 3 + '1'.toInt()
    print(b)
}
```

运行结果：

![将 char 转换类型之后参与运算的结果](https://images.gitee.com/uploads/images/2019/0129/115313_1ad92738_930142.png)


### 3、布尔类型

#### (1)、布尔类型概念

布尔类型的关键字为 **Boolean** ，它只有两个值: `true` 和 `false`。 

布尔类型可以类比为判断题的答案，要么对——true，要么不对——false.

```java
fun max(){
    val a = 5
    val b = 6

    //显示声明 flag1 的类型
    val flag1:Boolean = a > b
    //隐式声明 flag2 的类型——类型推导
    val flag2 = a < b
}
```

上述示例代码中，我们定义了两个布尔类型的变量。一个使用了显式声明类型的方式，一个使用了隐式声明类型。它们的值由比较运算符决定。

比较运算符：

运算符|含义
---|---
\> | 判断符号前面的数据是否大于后面，大于返回 true，否则返回 false
< | 判断符号前面的数据是否小于后面，小于返回 true，否则返回 false
== | 判断符号前面的数据是否与后面的相等，相等返回 true, 否则返回 false
<= | 判断符号前面的数据是否小于或者等于后面的数据，满足则返回 true，否则返回 false
\>= | 判断符号前面的数据是否大于或者等于后面的数据，满足则返回 true，否则返回 false

#### (2)、布尔运算符和布尔函数

布尔运算符和布尔函数用来对布尔值进行运算。

##### A: 布尔预算符

布尔运算符（也叫逻辑运算符）有:

布尔运算符|含义
---|---
!  | 逻辑非，取反
ll | 短路逻辑或。有一个值为true，则返回true。若前一个值为true, 则不用再判断后一个值 
&& | 短路逻辑与。有一个值为false，则返回false。若前一个值为false，则不用再判断后一个值 


如：

```java
fun isXxTrue(){
    // && 两个值都为true才返回true。
    val result1=true && true
    //&& 前一个值为false，所以，表达式可以直接返回false。不用再计算后面的 4 是否大于 5
    val result2=false && 4>5

    //|| 两个值都为false才返回false。
    val result5= false || false
    //|| 如果前一个值为true，必然返回true，此时不用判断后一个值
    val result3= true || 5>4
    val result4= false || true
    
    //! 就是取反
    val result6 = !true
    val result7 = !false
}
```

上述示例代码中，各个 result 对应的值分别为：

```
true
false

true
true
false

false
true
```

##### B: 布尔函数

Kotlin 中没有提供 Java 语言中的 `&`(逻辑与) 和 `|`(逻辑或)。但为我们提供了 `and()`、`or()`函数，通过这两个函数，我们可以实现 `&` 和 `|` 的效果。而且还提供了 `not()`，该函数等价于 `！`(逻辑非)

示例：

```java

fun main(args: Array<String>) {
    val flag = false
    //and()等价于 & ，isFirstMaxThanSecond 将被执行
    val result1 = flag and (isFirstMaxThanSecond(3, 5))
    val result2 = flag && isFirstMaxThanSecond(3, 5)

    println("result1的值为：$result1")
    println("result2的值为：$result2")

    val flag2 = true
    //or()等价于 | ，isFirstMaxThanSecond 将被执行
    val result3 = flag2 or (isFirstMaxThanSecond(3, 5))
    val result4 = flag2 || (isFirstMaxThanSecond(3, 5))

    println("result3的值为：$result3")
    println("result4的值为：$result4")
}

fun isFirstMaxThanSecond(a: Int, b: Int): Boolean {
    println("Kotlin中没有 & 、| ，而是使用 and()/or() 替代")
    return a > b
}
```

运行结果：

```java
Kotlin中没有 & 、| ，而是使用 and()/or() 替代
result1的值为：false
result2的值为：false

Kotlin中没有 & 、| ，而是使用 and()/or() 替代
result3的值为：true
result4的值为：true
```


### 4、字符串

#### (1)、字符串的类型
kotlin 中字符串用 **String** 类型表示。

用 一对双引号 `“ ”` ，或者 一对的三个引号 `“”“   ”“”`括起来的内容就是字符串。

那么，`“ ”` 和 `“”“  ”“”` 有什么区别呢？他们其实分别代表了两种字符串类型：**转义型字符串 和 原样字符串（也可以叫非转义字符串）**。

* 用 `“ ”` 括起来的内容称为 转义字符串。也就是说其中的内容支持转义字符
* 用 `“”“  ”“”` 括起来的内容称为 原样字符串。其中的内容不支持转义字符。输入啥样，输出就啥样。

> 补充：转义字符
> 
> 通俗的说就是把 特殊符号 和 字符 组合起来表达另一种含义的字符组合形式。就像汉语中的 `妈` 和 `的`，本来很普通的两个字，组合起来就表达了另外一种意思。
> 
> 在 Kotlin 语言中，组成转义字符的特殊符号有 `\`、`&`、`%` 等，字符就是我们在本章第二节中介绍的内容。举个转义的例子：`\n` 就表示换行
> 
> 更多内容请参考维基百科 [转义字符](https://zh.wikipedia.org/wiki/%E8%BD%AC%E4%B9%89%E5%AD%97%E7%AC%A6)

##### A、原样字符串(非转义字符串)
原样字符串也可以叫做非转义字符串，其特点是：

不支持转义字符；在打印原样字符串时，你输入的字符串是什么样的，它打印出来就是什么样的！

看示例代码：

```
fun main(args: Array<String>) {
	 //这里使用 “”“  ”“” 创建了一个原生字符串
    val str = """        
        床前明月光，
        疑是地上霜。
        举头望明月,
        低头思故乡。
    """
    println(str)
}
```
运行结果：

![原样字符串](https://images.gitee.com/uploads/images/2019/0129/115313_becf1660_930142.png)

瞧，我们在创建字符串时使用了键盘上的回车键创建了换行，打印的时候这些格式被保留了！这就是 原样字符串。

##### B、转义字符串
转义字符串就是支持转义字符的字符串。

还是 李白的《静夜思》，依旧是在输入字符串的时候使用键盘上的回车键创建换行，如下：

```
fun main(args: Array<String>) {
	//使用换行时，系统自动用 + 将字符串链接了
    val str2 = "" +  
            "床前明月光，" +    
            "疑是地上霜，" +
            "举头望明月，" +
            "低头思故乡。"

    println(str2)
}
```
查看运行结果：

![转义字符串](https://images.gitee.com/uploads/images/2019/0129/115313_aee09c3e_930142.png)

你看，虽然我们在创建字符串的使用手动敲击了键盘的回车，从外表看起来也像是换行了，但是打印出来其实是在一行的！！

那么，如果我们想用 转义字符串打印出 原始字符串的样子该怎么办呢？——**用转义字符啊！**，代码如下：

```
fun main(args: Array<String>) {
    val str3 = " 床前明月光，\n 疑是地上霜，\n 举头望明月，\n 低头思故乡。"
    println(str3)
}
```
运行结果：

![image.png](https://images.gitee.com/uploads/images/2019/0129/115313_6fa2a2c0_930142.png)

好了，到这里我想你就应该明白为什么它叫 转义字符串了吧？——**支持转义字符，通过转义字符可以实现特殊的效果**

但是，你也可能会想，原样字符串不支持转义字符么？**是的，原样字符串不支持转义字符**，不信你就看下面的代码：

```
fun main(args: Array<String>) {
 	 //原样字符串不支持转义字符。输入啥样输出啥样
    val str = """
        床前明月光，\n
        疑是地上霜。\n
        举头望明月, \n
        低头思故乡。

    """
    println(str)     
}
```
运行结果：

![](https://images.gitee.com/uploads/images/2019/0129/115313_1de80b22_930142.png)

你看，换行符的转义符 \n 直接被打印出来了。。。


#### (2)、获取字符串元素

字符串是由一个个的字符元素组成的，如果我们需要获取某个字符串中的元素有两种方式：

##### A：使用索引运算符访问

索引其实就是单个字符在整个字符串中的位置，但这个位置是从 0 开始计数的。从左向右，第一个字符的索引为0 ，第二个为1，依次类推。

使用索引运算符获取元素的格式为： `字符串[i] `

示例代码：

```
fun main(args: Array<String>) {
    val str = "123456789"
    //使用索引运算符获取字符串中的元素
    val a = str[1] 
    println(a)
}
```

上述代码中，`str[1]`表示取出字符串中索引为1的字符，由于字符串中元素的索引是从0开始计数的，所以，1 索引对应的值实际为2.

##### B：用 for 循环迭代字符串

在 kotlin 中字符串也支持 for 循环，通过 for 循环我们也可以获取字符元素.

kotlin 中 for 循环的基本格式为 ：

```
for (a in str){
    //TODO sth   
}
```

> 注意：如果你不了解 for循环 是啥也不要着急，此处先记住这个使用格式。在《流程语句》部分还会对for循环做详细介绍。

使用 for 循环迭代字符串的示例代码：

```
fun main(args: Array<String>) {
    val str = "123456"

    //遍历 字符串中的内容 并打印出来
    for (a in str) {
        println(a)
    }
}
```
运行结果：
![for 循环遍历字符串中的元素](https://images.gitee.com/uploads/images/2019/0129/115313_e4ce9706_930142.png) 

#### (3)、字符串模板

##### A、字符串模板的标准写法

字符串模板，也叫占位符，这个占位符的值为实际传入的数据。

Kotlin中字符串模板 以 `$` 开头，后面跟一个 `{ }` ，`{ }` 中的内容可以是一个变量、方法或者一个运算式。

示例代码：

```
fun main(args: Array<String>) {
    inputSth("abc")
}

fun inputSth(content: String) {
	//字符串模板的使用
    println("传入方法的数据是：${content}")    
}

```
运行结果：
![字符串模板的使用](https://images.gitee.com/uploads/images/2019/0129/115314_bbec1a6b_930142.png)

##### B、字符串模板的简写方式

我们再来看下面的代码：

```
fun main(args: Array<String>) {
    inputSth("abc")
}

fun inputSth(content: String) {
    println("传入方法的数据是：$content，它的长度是 ${content.length}") 
}
```

细心的你可能已经发现了，这次的代码和上次的明显有区别，`$content`省略了大括号， 而后面的 `${content.length}` 并没有省略大括号，这是因为：

* **如果字符串模板中包裹的内容是单一的变量可以省略大括号{ }**
* **如果字符串模板中包裹的是表达式，则不能省略大括号**

上面代码的运行结果：
![](https://images.gitee.com/uploads/images/2019/0129/115314_8ecfd592_930142.png)

##### C: 字符串模板中如何打印美元符号

Kotlin 中使用 $ 作为字符串模板关键字，但它同时也是美元符号，那么， 如果我们需要字符串中使用美元符号时该怎么做么？

* 方式1：`${‘＄’} `

`${}` 还是字符串占位格式，其中包含的`＇＄＇`表示 ＄ 符号

```
val str="人民币转换后的美元金额为${'$'}$rmbNum"
```

* 方式2：`＼$`

\ 表示转义，`＼＄`转义后得到 ＄

```
//＼＄ 表示转义获取$符号，$rmbNum 为字符串占位符
val str="人民币转换后的美元金额为\$ $rmbNum"
```

#### (4)、字符串比较

##### A、== 和 equals(xx)

kotlin 中比较字符串有两种方式: `==` 和 `.equals()`

这两种方式都能比较字符串是否相同。看代码：

```
fun main(args: Array<String>) {
    var str1 = "a"
    var str2 = "a"
    //使用 == 判断字符串是否一致  
    println(str1 == str2)  
    // 使用 equals 判断字符串是否一致        
    println(str1.equals(str2))  
}
```
运行结果：

![](https://images.gitee.com/uploads/images/2019/0129/115314_4ca09ea9_930142.png)

通过上面的代码可知 ：

kotlin 中 `==` 具有 与 `.equals(XX)` 相同的功能。

其实， `==` 本身就是通过 `.equals(xx)` 实现的。equals()函数源码描述如下：

```java
* Note that the `==` operator in Kotlin code is translated into 
* a call to [equals] when objects on both sides of the operator
* are not null.
*/
public open operator fun equals(other: Any?): Boolean
```

##### B、equals(xx) 和 equals(xx , Boolean)

`equals( xx )`有一个重载方法 `equals(xxx , Boolean)` ，后面的Boolean表示 **是否忽略大小写** ，true 忽略，false不忽略。`equals(xx)` 内部将 Boolean 赋值为 false。看代码：

```
fun main(args: Array<String>) {
    var str1 = "a"
    var str2 = "A"
    println(str1.equals(str2))
    println(str1.equals(str2,true))
}
```
运行结果：

![](https://images.gitee.com/uploads/images/2019/0129/115314_aee61e0d_930142.png)


### 5、与Java对比

对于数值类型，Kotlin 与 Java 差异不大，区别主要是类型之间的转换：Kotlin使用的是方法转换，Java使用的是强制类型转换。

字符类型的差异在于，Kotlin 中字符不能直接参与整数运算，必须先调用 toInt() 等函数转换为数值类型才可以参与运算；但 Java 中的字符可以直接参与运算。


布尔类型基本一致。

对于字符串类型，它们的差异有点大。

* Kotlin 中区分了转义字符串和非转义字符串; Java 中只有一种转义字符串，不支持非转义字符串。

```java
//这是Java版示例，你可以再回忆一下用Kotlin怎么实现的
public static void main(String[] args) {
		String str="床前明月光，\n疑是地上霜。\n举头望明月，\n低头思故乡。";
	    System.out.printf(str);
	}
```

* Kotlin 中的字符模板比 Java 中的要简便很多，Java中根据接收数据的类型不同使用的模板也不一样，比如 %s, %d 等，Kotlin中完全不需要关心这些类型。

```java
//这是Java版示例，你可以再回忆一下用Kotlin怎么实现的
public static void main(String[] args) {
	   String str="123456";
	   System.out.printf("str的值为：%s",str);
}
```

* Kotlin 中获取字符串的字符时可以通过索引下标获取，也可以通过 for 循环获取。Java 中获取单个字符时需要使用 charAt(), 遍历全部字符时需要 for循环 配合 charAt() 获取。

```java
//这是Java版示例，你可以再回忆一下用Kotlin怎么实现的。
public static void main(String[] args) {
		String str="123456";
	    //Java中获取单个字符时，必须
	    for (int i=0;i<str.length();i++) {
	    	 System.out.println(str.charAt(i));
	    }
}
```
* 比较字符串是否一致时，Kotlin 可以使用 == 和 equals(), Java 中仅能使用 equals()

```java
//这是Java版示例，你可以再回忆一下用Kotlin怎么实现的。
public static void main(String[] args) {
		String str1="123";
		String str2="123";
		
		boolean flag= str1.equals(str2);
	    System.out.println(flag);
	}
```


### 6、小结

本章主要介绍了 Kotlin 中的基本数据类型，并与 Java 中的基本数据类型做了简单的对比。

类型推导(类型推断)在实际开发中用的还是比较多；Java 中没有这种定义，在使用变量时必须显示声明类型。

更多关于数据类型的知识，在后续的《类型系统》中会有相应介绍。


## 五、流程语句

### 1、if 表达式

在 Kotlin 中，if 既可以作为普通的判断语句使用，也可以作为表达式使用。
**当 if 作为表达式使用时，本身就会有返回值，其效果等同于 java 中的三元运算。**

### (1)、if/else

我们来看使用 if 获取两个值中较大值的代码，如果按照 java 中的模式，我们应该这么写：

```java
fun getMaxVal1(a: Int, b: Int) {
    var max: Int
    if (a > b) {
        max = a
    } else {
        max = b
    }
}
```

但是，我们已经知道了，在 kotlin 中，if 作为表达式时有返回值，效果等同于 java 中的三元运算，所以，我们获取两个值中较大值的代码就可以这么写：

```java
fun getMaxVal2(a: Int, b: Int) {
    //这种方式等同于 java 中的 三元运算
    var max = if (a > b) a else b   
}
```
这样是不是感觉很清新？

>注意：
>
>* 作为表达式使用时，其性质等同于 java 中的三元运算，此时**必须要有 else  字段**

另外，作为表达式使用时，if 和 else 分支后面不仅能跟普通的值，也可以跟代码块。如：

```java
 //作为表达式使用时，我们跟了一段代码块，
 val max = if (a > b) {  
        print("Choose a") 
        a
    } else {
        print("Choose b") 
        b
    }
```
向上面的这段代码中，我们将 if  作为表达式使用，并且在 if 和 else 分支后面跟了一段代码块，这样，我们不但能将 较大的值赋值给 max , 还能在同时做其他的操作 -- 这里是打印了一句话。 

### (2)、if/else if

if/else if 用来做多分支的判断，可以使用多个 else if .

示例如下：

```java
fun main(args: Array<String>) {
    val a = 5
    val b = 6

    val max= if (a>b){
        a
    }else if (a==b){
        0
    }else{
        b
    }

    println("max的值为：$max")
}
```

### (3)、对比Java中的if/else

Kotlin 中的 if/else、if/else if 与 Java 中的最大区别就是：**Kotlin 中的是表达式，可以有返回值；Java 中的仅是语句，没有返回值。**

除了上述区别，其他使用形式是一致的。

### 2、when 表达式

在做多分支判断是，除了用 if/else if ，还可以使用 when 表达式。

when 类似于 Java 中的 switch ，但是功能比 switch 更为强大。


#### (1)、主要特点：

kotlin 中 ，when 的主要特点如下：

- **when 既可以当做表达式使用，也可以当做语句使用。**
  - 当做语句使用时，效果等同于 java 中的 switch 
  - 当做表达式使用时，会有返回值，符合条件的分支的值就是整个表达式的值。
  - 当做表达式使用时, 必须要有 else 分支**
  - 当做表达式使用时，when 的各个分支不仅可以是常量，也可以是表达式。

* 多个分支有相同的处理方式时，**可以把多个分支条件放在一起，用逗号分隔**。（类似于 java 中 switch 语句的穿透）

#### (2)、用法示例

好了，我们已经知道了 when 的主要特点，那么接下来就看下 when 的具体用法：

##### 示例1、when 的基本使用格式

```java
fun main(arg: Array<String>) {
    whenFunc1(6)
}

fun whenFunc1(a: Int) {  
    when (a) {
        1 -> println("传入的值是1")
        2 -> println("传入的值是2")
        else -> {
            println("传入的值既不是1 也不是2")
        }
    }
}
```

上述示例代码的含义为：当 a==1 时，执行一个打印语句；当 a==2 时，执行一个打印语句，如果是其他值，则执行 else 后面的打印语句。

`1 -> println("传入的值是1")`中，1 是条件，-> 是连接符，-> 后面的println()是满足条件时要执行的语句。当 -> 的语句只有一行时，可以省略`{}`；当有多个语句时，不能省略，不省略{}时的格式应该为 else 语句的形式。 

##### 示例2、多分支共用一种处理（分支穿透）

```java
fun whenFunc2(a: Int) {
    when (a) {
        //分支穿透
        1, 2 -> println("传入的是1 或者是2")  
        else -> {
            println("传入的既不是1 也不是2")
        }
    }
}
```

上述代码中：

如果 a==1 或者 a==2 , 就执行 println("传入的是1 或者是2") 。这种方式类似于 Java 中 Switch穿透。两个条件都执行了同一个语句。


##### 示例3、以表达式(而不只是常量)作为分支条件

在前面两个示例中，我们的条件都是常量，但 Kotlin 的 when 语句中，可以使用 表达式作为条件。

```java
fun whenFunc3(a: Int) {
    when (a) {
        //以表达式作为分支，实际是以表达式的值作为分支条件
        sum(3, 3) -> print("a 的值是 ${sum(3, 3)}")    
        else -> print("我哪里知道分支值是多少")
    }
}
```
在上面的代码中，我们 使用 Integer.sum(a,b) 方法作为 when 的分支条件，实际就是以 sum 的值作为分支条件。

##### 示例4、将When 作为 if..else if 使用

**A : 检测某个值是否在区间或者集合中**

- 判断某个值是否在 XXX 中的关键字 是 `in`
- 判断某个值是否不在 XXX 中的关键字 是 `!in`

```java
//声明一个 包含 1--100之间数值的区间，默认是闭区间
var nums=1..100    

fun whenFunc4(a: Int) {
    when (a) {
        in 1..5 -> println("$a 在 1..5 的区间之内")
        !in 10..15 -> println("$a 不在 10..15 的区间之内")
        else -> println("$a 在 10..15 的区间之内")
    }
}
```
上面的代码中，我们演示了判断一个值是否在区间之内，当然也可以判断值是否在某个集合中，关于集合的内容后面会有讲解。

区间 就是指一个数据范围，在高中数学中有相应介绍。

  - 区间分为开区间 、闭区间 、半开区间 。
  - 开区间的表示方式为 `( a , b )` , 表示该范围内的数据 自 a 开始 到 b 结束，但不包含  a 和 b
  - 闭区间的表示方式为 `[ a , b ]` , 表示该范围内的数据 自 a 开始到 b 结束，包含 a 和 b
  - 半开区间有两种方式：`( a , b ]`和 `[ a ,b )` 。前者表示不包含 a 但是 包含 b , 后者表示 包含  a 但不包含 b  

**B : 检测某个值是否是某种类型**

- 检测某个值是某种类型的关键字是 `is`
- 检测某个值不是某种类型的关键字是 `!is`

```java
fun whenFunc5(a: Int?) {
    when (a) {
        is Int -> println("$a 是 Int 类型的数据")
        else -> println("$a 不是 Int 类型的数据")
    }
}
```


##### 示例5 ：when 后面也可以不跟参数

如果不提供参数，所有的分支条件都是简单的布尔表达式，而当一个分支的条件为真时则执行该分支:

```java
fun whenFunc6(a: Int) {
	//此处未跟参数，所以分支条件必须是 简单的 boolean 表达式
    when {  
        a < 6 -> println("$a 小于6")
        a == 6 -> println("传入的值是6")
        else -> println("$a 大于6")
    }
}
```

##### 示例6：when 作为表达式使用

前面我们说过，when 可以作为表达式使用——表达式的一个特点就是有返回值，示例如下：

```java
fun main(args: Array<String>) {
    val a = 5
    val b = 6

    val max = when {
        a > b -> a
        else -> b
    }

    println("两者中的较大值为：$max")
}
```

#### (3)、对比Java中的switch

下面列出一个 Java 中标准 switch 语句示例：

```java
public static void main(String[] args) {
		int age = 10;
		String desc = "";
		switch (age) {
			case  10:
				desc = "儿童";
				break;
			case 15:
			case 17:
				desc = "少年";
				break;
			default:
				desc = "成年";
				break;
		}
	}
```

Java 的 switch 语句中，case 后面跟的条件必须是常量。Kotlin 的 when 中条件可以是表达式。

Java 的 switch 语句中，条件末尾需要使用 `break` 标记语句的结束。Kotlin 的 when 中条件后面不需要特殊标记。

Java 的 switch 语句中，多个分支执行同样的操作时，也需要列出每一个分支，然后省略条件之间的break，从而形成穿透。Kotlin 的 when 中可以将多个条件罗列在一行，然后用逗号间隔。

Java 的 switch 语句中，没有返回值。Kotlin 的 when 中可以有返回值。

Java 的 switch 语句中，默认分支使用 default 表示。Kotlin 的 when 中默认语句使用 else 表示。

### 3、 For 循环

For循环通常用来遍历获取集合或者数组中的元素。所谓遍历就是挨个获取数组或集合中的每一个元素。

前面在介绍 获取字符串的元素 时，使用了 `for/in`, 它是 for 循环的一种。(字符串内部其实是维系了一个字符类型的数组)。除了 `for/in`, 还有`forEach`

#### (1)、数组

##### A: 创建数组

在介绍 for循环时，我们会用到数组，所以，在开始介绍 for 循环之前，我们先简单了解一下数组以及如何创建数组。

数组是相同类型数据的一个”小团体“。比如，一个班级中的全部学生就可以使用数组表示，数组中的元素就是每一个学生。班级中的学生都有固定的学号，这个学号就是数组元素的索引。

Kotlin 中，数组是一个类，类名为 `Array`。

创建数组的基本格式为：`可变修饰符 数组名:Array<元素类型> = arrayOf(元素1,元素2)`

如：

```java
val students:Array<String> = arrayOf("张三","李四","王五")
```

上述代码中，students 为数组名称；冒号后面的 Array 关键字表示是一个数组；而 Array 后面的 <String> 表示该数组的元素类型为 String。 **arrayOf() 函数就是用来创建数组的核心函数。** 函数的参数就是数组的元素，多个元素之间使用逗号间隔。

在之前我们经介绍过，Kotlin 有类型推断的功能，所以，我们在创建数组的时候，也可以不用显式声明类型：

```java
val students = arrayOf("张三","李四","王五")
```

##### B：访问数组元素

在 创建数组 一节中我们已经知道，数组是有索引的，所以，访问数组元素时可以直接使用下标的形式。如：

```java
fun main(args: Array<String>) {
    val students = arrayOf("张三","李四","王五")
    //以下标的形式访问数组元素
    val name=students[0]

    println(name)
}
```

> 更多关于数组的知识，请参考后续的 《集合和数组》部分。


#### (2）、for/in 

for/in 的基本格式如下: 

```
for (item in xxArrary) {
	// 循环体代码块
}
```

在上述格式中，如果被 {} 包裹的循环体只有一行，那么可以省略 {}, 如：

```
for (item in xxArrary) 
	print(item)

```

##### 示例1：遍历获取数组中的数据

```
fun forFunc1() {
	 //创建int类型的数组
    var nums = arrayOf(1, 2, 3, 4, 5, 6, 7)  
    for (num in nums) { 
        println(num)
    }
}
```

##### 示例2：遍历数组中的索引

```
fun forFunc2() {
    var nums = arrayOf(1, 2, 3, 4, 5)
    //nums.indices 获取数组的全部索引组成的区间
    for (index in nums.indices) {   
        println(nums[index])   
    }
}
```

在 Array 类中有一个 `indices` 属性 ，该属性返回的是 数组的索引区间。`indices` 源码如下：

```
/**
 * Returns the range of valid indices for the array.
 */
public val <T> Array<out T>.indices: IntRange
    get() = IntRange(0, lastIndex)
```
所以，遍历索引的本质就是区间的遍历。

##### 示例3、同时遍历数据和索引

格式为：`for((变量名A, 变量名B) in 数组名.withIndex())`

`变量名A`用来接收数组元素；`变量名B` 用来接收元素索引。

`数组名.withIndex()`返回的是一个 IndexingIterator 实例，该实例中包括两个属性：数组元素的索引，数组元素的值。

```
fun forFunc3() {
    var nums = arrayOf(1, 2, 3, 4, 5)
    for ((index, num) in nums.withIndex()) {
        println("索引 $index 对应的数据是 $num")
    }
}
```

#### (3)、forEach

kotlin中遍历的时候，我们也可以使用 `forEach ( ){  }`, 需要注意的是：
**使用 forEach()时, 被遍历到的数据 使用关键字 it 表示。**

示例代码如下：

```
fun forFunc4() {
    var nums = arrayOf(1, 2, 3, 4, 5)
    nums.forEach {    
    	 //forEach 遍历到的数据使用固定的 it 代表
        println("nums 中的数据包含 $it")
    }
}
```
在上面的代码中，我们使用 forEach 遍历了数组中的数据，在  ` println("nums 中的数据包含 $it")` 中，it 就代码每次遍历到的具体数据。

#### (4)、对比 Java 中的 for 循环

在下面的示例代码中，我们分别使用了 Java 和 Kotlin 实现了同样的需要——遍历并打印数组中的元素。

你觉的哪种更清爽更便捷呢？

```java
public static void main(String[] args) {
		int[] nums= {1,2,3,4,5}; 
		
		for(int i=0;i<nums.length;i++) {
			System.out.println(nums[i]);
		}
		
		for(int num:nums) {
			System.out.println();
		}
	}
```

```java
fun main(args: Array<String>) {
    val nums = intArrayOf(1, 2, 3, 4, 5)

    for (num in nums) {
        println(num)
    }

    nums.forEach {
        println(it)
    }
}
```



### 4、while 循环

Kotlin 中的 `while` 和 `do...while` 与java中的并没有区别，使用的方式是一致的。

#### (1)、while

while 的基本格式为：

```
while(条件语句){
	//代码块
}
```

其含义是：

当条件语句为 true 的时候，执行 { } 中的代码块。当条件语句为 false 时，不再执行 { } 中的内容。

如果条件语句一直为 true，那么程序就会一直执行下去，从而形成死循环 —— 所以，为了避免死循环的出现，必须保证条件语句可以为false.

示例：

```
fun whileFunc1() {
    var a = 10
    while (a > 0) {
        println("当前a 的值是 $a")
        a--
    }
}
```

上述示例中，

`a--` 叫做自减运算符，是算数运算符的一种，等价于`a = a-1`.
除了自减运算符还有自增运算符：`a++`, 等价于 `a = a+1`

而且，还有 `--a` 和 `++a`。那么，`--a` 和 `a--`、`++a` 和 `a++` 的区别在哪里呢？运行下面的代码看一看吧。(示例中仅给出了 --a 和 a-- 代码，++a 和 a++ 的代码请自己尝试实现一下看看吧)

```java

fun main(args: Array<String>) {

    //不参与其他运算时的 --a 和 a--
    whileFunc1()
    println("")
    whileFunc2()
    println("")

    //参与其他运算时的 --a 和 a--
    whileFunc3()
    println("")
    whileFunc4()
}

fun whileFunc1() {
    var a=10
    while (a>0){
        print("a的值为$a ")
        a--
    }
}

fun whileFunc2(){
    var a=10
    while (a>0){
        print("a的值为$a ")
        --a
    }
}

fun whileFunc3() {
    var a = 10
    var b = 0
    while (a > 0) {
        b = --a
        print("b的值为$b ")
    }
}

fun whileFunc4() {
    var a = 10
    var b = 0
    while (a > 0) {
        b = a--
        print("b的值为$b ")
    }
}
```

#### (2)、do/while

do/while 的基本格式为：

```
do{
	//代码块
}while(条件语句)
```

其含义为:

先执行 do {} 中的代码块，然后再判断 while 后面的条件语句是否为 true 。如果为 true 就继续执行 do {} 中的代码块，否则，终止程序。


示例：

```
fun whileFunc2() {
    var a = 10
    do {
        println("当前 a 的值是 $a")
        a--
    } while (a > 0)
}
```

#### (3)、while 与 do/while 的区别

do/while 中由于是先执行代码，所以，不论条件是否为 true ，都会执行一次。

而 while 是先判断条件，当条件为 false 时，将不会执行代码。

### 5、循环控制

所谓循环控制，就是在满足某个条件之后，我们提前终止或者跳过某一次循环的操作。

Kotlin 中循环控制的逻辑与 Java 一致。

循环控制的关键词有：

* continue 跳过本次循环
* break 中断循环，但是会继续执行循环后面的语句
* return 中断循环，并且不再执行循环后面的语句

#### (1)、break

break 表示中断循环。

假设某个函数中包含一个循环，循环后面还有其他语句，当循环使用 break 中断之后，循环后面的其他语句还可以继续执行。

示例：

```java
fun main(args: Array<String>) {
    breakFunc()
    breakFunc2()
}

fun breakFunc() {
    var a = 10
    while (a > 0) {
        a--

        if (a == 5) {
            break
        }

        print("a的值为$a ")
    }
    println("break中断循环，该循环后面的语句会继续执行")
}

fun breakFunc2() {
    var a = 10
    while (a > 0) {
        if (a == 5) {
            break
        }

        a--
        print("a的值为$a ")
    }
    println("break中断循环，该循环后面的语句会继续执行")
}
```
输出结果为：

```java
a的值为9 a的值为8 a的值为7 a的值为6 break中断循环，该循环后面的语句会继续执行
a的值为9 a的值为8 a的值为7 a的值为6 a的值为5 break中断循环，该循环后面的语句会继续执行
```


#### (2)、continue

continue 表示跳过本次循环，继续执行后面的循环.

```java
fun main(args: Array<String>) {
    continueFunc1()
    println()
    continueFunc2()
}

fun continueFunc2() {
    var a = 10
    while (a > 0) {
        if (a == 5) {
            continue
        }

        a--
        print("a的值为$a ")
    }
}

fun continueFunc1() {
    var a = 10
    while (a > 0) {
        a--
        if (a == 5) {
            continue
        }
        print("a的值为$a ")
    }
}
```

运行结果为：

```java
a的值为9 a的值为8 a的值为7 a的值为6 a的值为4 a的值为3 a的值为2 a的值为1 a的值为0 
a的值为9 a的值为8 a的值为7 a的值为6 a的值为5 
```

看到上面的执行结果，你是不是对 `continueFunc2()` 的输出结果有点不理解？为啥这个函数没有输入后面的 4，3，2，1 呢？

其实，在 `continueFunc2()` 中，当 a==5 时，就跳过了本次循环，后面的 `a--` 和 `print()` 将不会被执行。继续执行循环时发现 a 还是5 ，然后就继续跳过；再次循环时还是5，然后再跳过...就这样，形成了一个 循环——跳过——循环 的死循环。 所以 ——**改变数据的操作需要在 continue 判断条件之前。**


#### (3)、return

return 表示中断循环，并且跳出当前函数。

假设某个函数中包含一个循环，循环后面还有其他语句，当循环使用 return 中断之后，循环后面的其他语句不会被执行。

```java
fun main(args: Array<String>) {
    returnFunc1()
    println()
    returnFunc2()
}

fun returnFunc2() {
    var a = 10
    while (a > 0) {
        if (a == 5) {
            return
        }

        a--
        print("a的值为$a ")
    }
    println("return中断循环-退出函数，循环后面的语句不会被执行")
}

fun returnFunc1() {
    var a = 10
    while (a > 0) {
        a--
        if (a == 5) {
            return
        }

        print("a的值为$a ")
    }
    println("return中断循环-退出函数，循环后面的语句不会被执行")
}
```

运行结果：

```java
a的值为9 a的值为8 a的值为7 a的值为6 
a的值为9 a的值为8 a的值为7 a的值为6 a的值为5 
```


## 六、类

> 本章仅介绍类的基础知识，先带你快速认识类的基本特点和使用方式。更详细的内容请参考后续的 《类，对象和接口》章节。

类，是对同一种事物的抽象描述。比如： 学生类，就是指还在学校读书学习的一群人。

类的属性，是指类具有的特点。比如：学生类中，所有学生都必然有学校、年级、班级、年龄、性别，这些就是类的属性。

类的实例，是同一种事物中的个体。比如：张三是一个学生，那么，张三就是学生类的实例。（实例 也叫 对象 ）

类的构造，是指编程语言中创建类实例的特殊函数。就像幼儿园新生入学报到，报到成功后才能成为学生，类的构造函数就是报到事件。

类的继承，就是指类之间的从属关系。比如：大学生、中学生 和 小学生 都属于学生，那么我们就可以说，大学生类、中学生类、小学生类 继承自 学生类；学生类 是 大、中、小学生类的父类，大、中、小学生类 是 学生类的子类。 

### 1、类和构造

#### (1)、类的声明

Kotlin 中使用关键字 `class` 声明类 。

声明一个类时，通常需要指定 类名、类头以及类体。

* 类名，就是类的名字
* 类头，指 构造函数所接受的参数 或 类的继承关系
* 类体，指 类后面被 { } 包裹的内容。比如：属性、函数等 

格式为：

```java
class 类名 (参数：参数类型){
	//类体内容。
}
```


如：

```java
class Students(schoolName: String) {
    fun printXXX() {
        println("声明一个类，这是包裹在类中的函数")
    }
}
```

上述代码中，

* class 关键字用来声明一个类。
* Students 是类名。
* 类名后面的 `(schoolName: String)` 表示构造函数所需的参数, 属于类头
* 类中的 printXXX() 就是类体。

此外，**类头 与 类体 是可选的 ; 如果一个类没有类体，可以省略后面的 { }。**

比如：

```java
class Empty(){
    
}
```

因为上面的 Empty 类没有类头，也没有类体，所以可以简化为如下：

```java
class Empty
```
#### (2)、构造和实例

与 Java 等语言不同，Kotlin 中类的构造有主构造和次构造之分。（构造函数 简称 构造）

Kotlin 的类可以有一个主构造函数 以及 一个或多个次构造函数。

##### A: 主构造

先放一个类：

```java
class Students(schoolName: String) {
   //...
}
```

上面的示例中，我们定义了一个 Students 类，类名后面跟了一个包裹内容的小括号——`(schoolName: String)`，这个小括号的作用就是声明造函数，并且是主构造函数。也就是说——

**跟在类名后面和类一起声明的构造函数 称为 主构造函数。**

其实，在上面的示例中我们省略了主构造关键字 `constructor`，如果不省略，应该是下面这个样子：

```java
class Students constructor(schoolName: String) {
   //...
}
```

上面示例中，**主构造函数没有任何注解或者可⻅性修饰符，所以可以省略`constructor `关键字**。

##### B: 主构造的 init 代码块

在 Kotlin 中，主构造函数是没法添加代码块内容的，仅能定义接收的参数。

如果我们需要在调用主构造的同时做一些初始化的操作，可以放在 `init{}` 代码块中。

init 代码块可以直接访问主构造函数的参数。

格式：

```java
init {
	//初始化代码
}
```

示例：

```java
class Students(uName: String) {
    init {
        println("姓名:$uName")
    }
}
```


##### C: 次构造函数

除了在声明类的同时声明的主构造函数，我们还可以在类内容声明一个或多个次构造函数。

次构造函数也是使用 `constructor` 关键字修饰，其格式如下：

```java
constructor(参数1:参数类型, 参数2:参数类型):this(参数:参数类型){
	//函数体
}
```

上述格式中：

* 虽然我们称其为构造函数，但要注意不需要 `fun` 关键字
* construcotr 后面小括号中的参数可以有一个或多个
* 末尾的 `:this(参数:参数类型)` 表示构造函数委托，参数个数由被委托方决定。
* 如果类有主构造函数，那么每个次构造函数都需要委托给主构造函数，可以直接委托或者通过别的次构造 函数间接委托。

>委托就是把自己不想做的事情交给别人去做。
>关于委托的更多内容，可以参考后续的 《属性委托》

示例：

```java
class Students(uName: String) {
    
    //次构造函数直接委托给主构造。后面的 this 表示主构造 
    constructor(name: String, age: Int) : this(name) {
        // ...
    }

    //次构造函数间接委托给主构造。后面的 this 表示前一个次构造
    constructor(name: String, age: Int, schoolName: String) : this(name, age) {
        // ...
    }
}

class Student2 {
    //没有主构造时，次构造不需要委托
    constructor(name: String, age: Int) {
        //...
    }
}
```

##### D: 创建类的实例

与 Java 不同，在 Kotlin 中创建类实例时，不需要 new ，直接调用构造函数并传递参数即可。

格式：`val 实例名 = 类名(实参1，实参2)`

上述格式中，

* 可以根据实际需求动态决定是 var 还是 val 
* 实参的具体个数由构造函数决定
* 实参就是指传递给函数的具体数据；相对应的是形参，表示声明函数时的参数名。

示例:

```java
class Students(uName: String) {

    init {
        println("姓名:$uName")
    }

    //次构造函数直接委托给主构造
    constructor(name: String, age: Int) : this(name) {
        println("年龄:$age")
    }

    //次构造函数间接委托给主构造
    constructor(name: String, age: Int, schoolName: String) : this(name, age) {
        println("学校名称：$schoolName")
    }
}

fun main(args: Array<String>) {
    //使用主构造创建实例
    val student1 = Students("张三")
    println()

	//使用次构造函数创建实例
    val student2 = Students("张三",23)
    println()

	//使用次构造函数创建实例
    val student3 = Students("张三",23,"山东大学")
    println()
}
```

运行结果：

```java
姓名:张三

姓名:张三
年龄:23

姓名:张三
年龄:23
学校名称：山东大学
```

上述示例代码展示了构造函数的定义、构造函数的委托、实例的创建。

虽然，在两个次构造函数中我们没有对姓名进行打印，但它们都通过委托的形式触发了主构造函数的 init 代码块，这样，init 代码块中打印姓名的操作就被触发了。

### 2、类的属性

Kotlin 中为类定义属性时有两种方式：类内部定义，在主构造函数中定义

属性可以为 var , 也可以为 val。

#### (1)、类内部定义属性

```java
class Students(uName: String) {

    var mName = uName
    var mAge: Int = 0
    var mSchoolName = ""

  	 ...
}
```

上述示例中：

我们为 Students 定义了三个变量：姓名——mName，年龄——mAge，学校名称——mSchoolName，并分别为它们赋值。这三个定义在类内部的变量就是类的属性。

在类中定义的变量被称为成员属性 或 全局变量，整个类中的函数都可以调用它们。

与 全局变量 对应的是 局部变量，它们定义在某个函数内部，只在函数内部有效。


#### (2)、主构造中定义属性

方式很简单，只需要在主构造参数的前面加上一个 var 或 val , 这样就表示这个参数是该类的一个属性

```java
class Students(val uName: String) {

    var mAge: Int = 0
    var mSchoolName = ""
    ...
}
```

上面这种方式也是表示 Students 有三个属性，分别为：uName, mAge, mSchoolName。

接下来，我们就调用一下这些属性吧：

```java
class Students(val uName: String) {

    var mAge: Int = 0
    var mSchoolName = ""

    init {
        println("姓名:$uName")
    }

    constructor(name: String, age: Int) : this(name) {
        mAge = age
        println("年龄:$age")
    }

    constructor(name: String, age: Int, schoolName: String) : this(name, age) {
        mSchoolName = schoolName
        println("学校名称：$schoolName")
    }
}

fun main(args: Array<String>) {
    val student = Students("张三", 23, "山东大学")
    println("该学生的姓名为：${student.uName}, 年龄为：${student.mAge}, 来自于：${student.mSchoolName}")
}
```
运行结果：

```java
姓名:张三
年龄:23
学校名称：山东大学
该学生的姓名为：张三, 年龄为：23, 来自于：山东大学
```



## 七、异常处理

异常，就是导致程序无法正常运行或者崩溃的特殊情况。比如，你今天早上没赶上公交车，导致上班迟到，这就是一个异常。

Kotlin 中，异常的顶级类是 `Throwable`。它有两个子类：`Error（错误）`、`Exception（异常）`

* Error（错误） 通常是非程序因素导致的，比如 从U盘向电脑复制内容时，突然拔掉了U盘，就会产生一个 IOError。
* Exception（异常） 是指代码因素导致的程序无法运行的情况。比如 索引越界、空指针等

实际应用中，我们需要处理的是 Exception —— 如果不处理，程序就会崩溃。而 Error 通常不会导致程序崩溃。

### 1、异常

异常的种类有很多，但它们都是 Exception 类的子类。Exception 及其子类的主要属性和方法有：

函数/属性|含义
---|---
message | 异常的详细描述字符串。
stackTrace | 异常的跟踪栈信息。
printStackTrace() | 打印异常以及异常所在的栈信息
printStackTrace(PrintStream s) | 将异常以及异常所在栈信息输入到指定的流中。

在对异常有了基本了解之后，我们手动创建一个异常：

```java
fun main(args: Array<String>) {
    val result = getConsult(5, 0)
}

fun getConsult(a: Int, b: Int): Int {
    return a / b
}
```

当我们运行上面这段代码后，在控制台会得到如下输出：

```java
Exception in thread "main" java.lang.ArithmeticException: / by zero
	at TempKt.getConsult(Temp.kt:22)
	at TempKt.main(Temp.kt:17)

Process finished with exit code 1
```

上面这段错误信息告诉我们：

在名称为 ”main“ 的线程中，我们得到了一个 `ArithmeticException` 异常。该异常产生的原因是：`/ by zero`, 该异常产生的位置是 Temp.kt 文件中的第 22 行。

### 2、捕获异常

#### (1)、捕获并处理异常
在上一节中，我们制造了一个异常，程序无法正常运行了。那么，当我们碰见异常的时候该怎么办呢 ？—— 当然是捕获这个异常信息，然后处理掉。

##### Ａ: 捕获异常的格式

Kotlin 中捕获并处理异常的格式为：

```java
try{
	//可能产生异常的代码块
}catch(e : 异常类型 ){
	//对捕获的异常做处理
}finally{
	//不论是否产生异常，必然都会执行的代码内容
}
```

try 后面的大括号中包裹可能会产生异常的代码片段。

catch 用来捕获具体的异常信息，并在其后的 { } 中做出相应的处理。可以有多个 catch 分支。

finally 表示不论是否产生异常，必然都会执行的代码内容。通常用来做一些释放资源的操作。

catch 和 finally 是可以省略的。但不能同时省略它们两个，也就是说，要么有 catch, 要么有 finally , 要么同时都有。

##### B: 处理异常

```java
fun main(args: Array<String>) {
    val result = getConsult(5, 0)
    val result2 = getConsult(6, 2)
}

fun getConsult(a: Int, b: Int): Int {
    var result = -1
    try {
        result = a / b
    } catch (e: Exception) {
        result = 0
        //将异常信息和异常所在的栈信息打印到控制台
        e.printStackTrace()
    } finally {
        println("finally语句不论是否有异常都会执行,result的值为：$result")
    }
    return result
}
```

运行结果：

```java
java.lang.ArithmeticException: / by zero
	at TempKt.getConsult(Temp.kt:25)
	at TempKt.main(Temp.kt:17)
finally语句不论是否有异常都会执行,result的值为：0

finally语句不论是否有异常都会执行,result的值为：3
```

上述示例中，我们在 catch 中捕获了异常，并调用 `e.printStackTrace()`把错误信息打印出来了。而且我们也确实看到 finally 中的语句不论是否有异常都会执行。

我们捕获异常之后，如果不调用 `e.printStackTrace()` ，那么控制台中是不会打印错误信息的。

##### C: 多 catch 的注意事项

有多个 catch 分支时，最好将子类 Exception 放在前面 ，父类 Exception 需要放在后面。

因为，我们使用多个 catch 的目的就是为了更精确的匹配异常从而给出个性化的处理，如果把父类异常放在前面，那么匹配到父类异常之后，将不会再去匹配子类异常。

所以，正确示例如下：

```java
fun getConsult(a: Int, b: Int): Int {
    var result = -1
    try {
        result = a / b
    } catch (e: ArithmeticException) {
        result = 0
        //将异常信息和异常所在的栈信息打印到控制台
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
        println("多catch时，父类Exception需要放在后面的分支")
    } finally {
        println("finally语句不论是否有异常都会执行,result的值为：$result")
    }
    return result
}
```


#### (2)、try 表达式

Kotlin 中 try 是表达式，是有返回值的。

* 如果没有产生异常并且没有finally，那么，try 代码块中最后一条语句的返回值就是 try 表达式的返回值。

* 如果产生了异常并且没有 finally ，那么匹配到的 catch分支 中最后一条语句的返回值就是 try 表达式的返回值。
* 如果有 finally ，不论是否有异常，那么 finally 中最后一条语句的返回值就是 try 表达式的值。（所以，使用 try 表达式的返回值时 慎用 finally）

##### 示例1：

```java
fun main(args: Array<String>) {
    val result = getConsult(5, 0)
    val result2 = getConsult(6, 2)

    println("result的值为 $result")
    println("result2的值为 $result2")
}

fun getConsult(a: Int, b: Int): Int {
    val result =try {
         a / b
    } catch (e: ArithmeticException) {
        0
    }
    return result
}
```

输出结果为：

```java
result的值为 0
result2的值为 3
```

##### 示例2：

```java
fun main(args: Array<String>) {
    val result = getConsult(5, 0)
    val result2 = getConsult(6, 2)

    println("result的值为 $result")
    println("result2的值为 $result2")
}

fun getConsult(a: Int, b: Int): Int {
    val result =try {
         a / b
    } catch (e: ArithmeticException) {
        0
        5
    }
    return result
}
```

输出结果：

```java
result的值为 5
result2的值为 3
```

上述示例代码中：

catch 分支中有个两个值表达式：0、5。当产生异常触发 catch 分支时，以最后一个表达式的值作为返回值，所以，result 的值为 5。

### 3、抛出异常

在定义函数时，如果我们知道这个函数可能会产生异常，但是我们又不想处理这个异常，那么就可以用 `throw` 将可能产生的异常抛出，然后由该函数的调用方做处理。

如果调用方不对可能产生的异常进行捕获并处理，那么异常产生时，程序就会崩溃无法运行。

其格式为：`throw 异常实例`

##### 示例1：调用方捕获并处理异常

```java
fun main(args: Array<String>) {
	 //调用方捕获并处理异常
    val result = try {
        getConsult(5, 0)
    } catch (e :Exception) {
        0
    }
    
    val result2 = getConsult(6, 2)

    println("result的值为 $result")
    println("result2的值为 $result2")
}

fun getConsult(a: Int, b: Int): Int {
    if (b != 0) {
        return a / b
    } else {
		  //抛出异常。throw 后面跟的是 异常实例（也叫 异常对象）
        throw ArithmeticException("除数不能为0")
    }
}
```

输出结果：

```
result的值为 0
result2的值为 3
```

##### 示例2：调用方未处理异常

```java
fun main(args: Array<String>) {
    val result = getConsult(5, 0)
    val result2 = getConsult(6, 2)

    println("result的值为 $result")
    println("result2的值为 $result2")
}

fun getConsult(a: Int, b: Int): Int {
    if (b != 0) {
        return a / b
    } else {
        throw ArithmeticException("除数不能为0")
    }
}
```

输出信息：

```java
Exception in thread "main" java.lang.ArithmeticException: 除数不能为0
	at TempKt.getConsult(Temp.kt:30)
	at TempKt.main(Temp.kt:19)
```

上述示例中，调用方没有捕获并处理异常，所以产生了崩溃，控制台中显示了导致崩溃的异常信息。


### 4、小结

Kotlin 中的异常处理与 Java 中的基本一致，但也有如下区别：

* Java 中的异常又分为：编译器异常、运行期异常。Kotlin 统一归为 运行期异常。
* Java 中抛出异常使用关键字 throws。Kotlin 中抛出异常使用关键字 throw。
* Java 中函数通过throws抛出异常时，需要在函数参数后面声明。Kotlin中则不需要声明。

Java 中抛出异常的处理如下：

```java
public class HelloWorld {
	public static void main(String[] args) {
		int result =-1;
		
		//调用方处理异常
		try {
			result = getConsult(5,0);
		}catch(Exception e){
			result = 0;
		}
		
		System.out.println("result的结果为:"+result);
	}
	
	//通过 throws 抛出异常 
	public static int getConsult(int a,int b) throws ArithmeticException{
		return	a/b;
	}
}
```

## 八、附录

### 1、参考资料

* [Kotlin 官方文档](https://kotlinlang.org/docs/reference/)
* 《 Kotlin 实战 》
* [《 Kotlin视频教程|黑马程序员 》](http://yun.itheima.com/course/266.html)
* 《 Kotlin从零到精通Android开发 》

### 2、其他相关资料

* [《 Kotlin视频教程|黑马程序员》笔记](https://gitee.com/CnPeng_1/LearningNotes/blob/master/03_Kotlin/02_%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/2%E3%80%81%E4%BC%A0%E6%99%BAKotlin%E5%9F%BA%E7%A1%80--%E5%BC%A0%E6%B3%BD%E5%8D%8E.md)
* [《 Kotlin从零到精通Android开发——欧阳燊（shen）》笔记](https://gitee.com/CnPeng_1/LearningNotes/blob/master/03_Kotlin/03_Kotlin%E5%BC%80%E5%8F%91%E5%AE%89%E5%8D%93/2%E3%80%81Kotlin%E4%BB%8E%E9%9B%B6%E5%88%B0%E7%B2%BE%E9%80%9AAndroid%E5%BC%80%E5%8F%91.md)



> 本篇文章到此结束，我们主要介绍了 Kotlin 的基本语法及相关知识，如需了解更多内容，请查看后续章节。谢谢！
>
> 如果文中有错误的地方，请务必联系我：QQ 893612134 , 微信 P893612134


