> 因为我们面向的用户是 Android 开发工程师，所以预设了读者有基本的 Java 基础。
>
> 在开始时，我们只会介绍怎样用 Kotlin 的「语法」写出 Kotlin 的代码，不会过多的介绍 Kotlin 的语法糖及简化代码的常用手段。
>
> 同时假如有部分 Kotlin 的语法，你认为在「开学」针对已经有 Java 基础的读者来说，是可以被省略的，那么就应该被省略。



第一部分和第二部分为 Kotlin 基础语法

[TOC]





# 第一篇：Kotlin 基础语法（一）

## 知识体系（仅作为写文章时的参考）：

### 变量（field、local variable、parameter）

- 声明时，与 Java 格式的不同
  - 类型和变量名位置互换
  - 中间冒号分隔
  - var 关键字打头
  - 属性 property（相当于 Java 的字段 field）没有默认值，所以必须在声明时初始化
    - 使用 `lateinit` 可以关闭属性的初始化检查
  - 通过类型推断，可以省略返回类型
    - 但是省略返回类型不等于动态类型，Kotlin 依然是静态类型的
    - 可以用 `List<String> names = new ArrayList<>()` 做例子：右边省略了 `<>` 里面的 `String`，但它依然是一个 `ArrayList<String>` 实例
- 空安全
  - 默认是不可空
    - 不可空变量不能赋值 `null`
    - 初始化的时候以及后面的再次赋值都不行
  - 类型右边加 `?` 号可以声明可空变量
    - 可空变量在调用的时候需要解决「可能为空」的问题
      - 可以用 `?.` 来做「如果就」规则
      - 可以用 `!!.` 来做「肯定不空」断言
        - 相当于忽略可空提示的、风险自担的强行调用
  - 「平台类型」：和 Java 交互时的空安全
- getter / setter
  - 读写属性的时候，实际上是调用 `getX() / setX()` 方法
  - 可以重写 setter / getter
    - 作用一：设置钩子，添加监听代码
    - 作用二：修改读写规则
    - 通过重写 getter，可以让属性的值变成动态的
  - Kotlin 调用 Java 的 setter/getter
  - 反过来 Java 调用 Kotlin 的 setter/getter
- `var` 和 `val`
  
  `var` 就是 Java 中的变量，而除了 `var` 之外，Kotlin 还增加了一个 `val`： `val` 叫做「只读变量」，是 Java 中没有的
  
  - `val` 只能在初始化时赋值一次，之后再也不能赋值。比较像 Java 中的 `final` 修饰的变量。
  - 但 `val` 可以通过自定义 getter 来让自己的值动态变化，这是跟 Java 的 `final` 的不同。

### 函数

- 声明时，和 Java 的格式不同
  - 返回类型和方法名位置对换
  - 中间用冒号分隔
  - 参数的格式记得也和 Java 不一样哟
  - 参数也有「可空与不可空」
  - 没有返回类型时，写返回 `Unit`
    - `Unit` 返回类型可以省略

- 构造函数
  - 构造函数的名字是 `constructor`
  - `new SomeClass()` 变为 `SomeClass()`

### 类型

* 基本数据类型

  * `int` `float` `Integer` `Float` 一众统一变成了 `Int` `Float` 一众

  * 区别：全都变成了普通的类

    * 全都是对象
    
    * 多了一大堆工具方法，如 `Int.toFloat()`
      
    * 限制：没有自动转型
    
      ```kotlin
      var height: Float = 2f // ✔️
      var width: Float = 1 // ❌
      
      var offset: Int = 1
      var offsetX: Float = offset.toFloat() // ✔️
      var offsetY: Float = offset // ❌
      ```
    
  * Android 工程师的疑问：没了 `int`，会不会有什么暂时想不到的麻烦？

* 类型的声明

  * extends 和 implements 变成 `:` 号

  * 默认是 public

  * annotation、enum 的声明

    ```kotlin
    annotation class Xxx
    enum class Xxx
    ```

  * `internal`

  * `override`、`open` 和 `final`

  * 接口和 Java7/8 的区别

  * `Any` 和 `Object`

  * 类型的判断和强转

  * class literal: `::class`, `.javaClass`, `::class.java`

### 条件控制

* `if/else`
* `when`
* `try-catch`
  * 非强制受检

* `?:` 操作符
* `==` 和 `===`

* 循环
* 区间

### 两个转换工具

* Java 转换成 Kotlin 的工具

  * 转换后需要手动修改的地方：早晚都要写纯 Kotlin 代码，但在初始阶段，快速迁移很重要，而快速迁移的关键就是「发现和解决自动转换工具带来的问题」。

* 反编译 Kotlin bytecode 的工具

  *  查看 Kotlin "去糖" 后对应的 Java 代码：没事看两眼，方便理解 Kotlin。
  

## 文章结构要求：

> 如果有不同意见请先来找我讨论，不要直接自我修改文章结构哟。——扔物线







# part II



## 字符串

- 字符串模版
- 多行字符串（待定）





## 可见性修饰符

* internal （module 内可见，可配合 Android Framework的 `@hide` 注解或自己写的 SDK？）
* private （文件内可见）





## 构造器（次级构造）







## 容器

* 数组
* 集合
* 可变集合/不可变集合
* 遍历 （for 循环）





## 静态函数和属性

* 静态函数的声明
  * package function
  * object
  * companion object
* 编译期常量





## 特殊的对象声明

* 声明单例对象

  > **请避免完全没意义的「 Kotlin 版 DCL 单例」**

* 匿名内部类对象



































# part III



## 构造器

* 主构造
* init 代码块
* 构造属性





## 函数简化

- 使用 `=` 连接返回值
- 参数默认值
- 命名参数
- 本地函数（嵌套函数）





## 容器

* 遍历 `forEach{}`
* 区间（包括 `step` ）





### 容器的操作符

- `filter` ,`map` ,`flatMap`  等
- `asSequence` 与性能





## Java 与 Kotlin 互相操作

* 在 Java 中调用 Kotlin 的静态函数

* 与 Java 互相调用的相关注解
  * `@JvmOverLoads`
  * `@JvmStatic`
  * `@JvmName`

* 其他

















# part IV

## 数据类

* `copy()`
* `componentN()`

## 解构

（未用到的参数可以使用 `_` 表示，待定）



## 扩展函数和属性

* 声明扩展
* 扩展函数和成员函数调用优先级
* 扩展函数原理
* 防止滥用



## lambda 及简化





## 标签

 内部类引用外部类对象
 循环中指定对象
 lambda 中引用指定对象





## `inline`

* 内联函数
  * `inline` ，（`noinline` 待定）
* 非局部返回，（`crossinline` 待定）
* 内联类





# part V

## 泛型

* `in/out` （`where` 待定）
* 泛型边界约束

* `reified`



## 函数类型

* 高阶函数
* typealias





## `operator`

## `infix`











# part VI

## 抽象属性（`abastract var/val`）

## 委托

* 类委托
* 属性委托
* `by lazy`
  * 线程安全
* Kotlin 是如何实现的（待定）



## 标准函数

* 标准函数
  * `repeat`
  * `takeIf`/`takeUnless`
  * `TODO`
  
* 作用域函数
  * 相同和区别
  * 如何选择/使用实际

## 密封类

* 密封类和枚举之间的联系





## Kotlin 的反射

* KClass
* 属性引用

[参考？](https://www.baeldung.com/kotlin-reflection)



## Kotlin 的注解

* 注解使用处目标（指的是 `file`,`filed`,`set/get` 等）











# part Ⅶ

## 协程













# part VIII

## Kotlin 已知缺陷和改进方案

安装包体积大小，及优化

编译时间增加，及优化

语法糖滥用，（及 Lint）

`kotlinx.android.synthetic` 的缺点，（及替代方案）





## 以下待定

Gradle Kotlin DSL （待定）

Anko (待定)

Kotlin with Jetpack（待定）

