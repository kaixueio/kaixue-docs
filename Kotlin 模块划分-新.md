> 因为我们面向的用户是 Android 开发工程师，所以预设了读者有良好的 Java 基础。
>
> 在开始时，我们只会介绍怎样用 Kotlin 的「语法」写出 Kotlin 的代码，不会过多的介绍 Kotlin 的语法糖及简化代码的常用手段。
>
> 同时假如有部分 Kotlin 的语法，你认为在「开学」针对已经有 Java 基础的读者来说，是可以被省略的，那么就应该被省略。



第一部分和第二部分为 Kotlin 基础语法

[TOC]





# part I 

## 函数，变量及类的声明

1. Kotlin 怎样写
2. 和 Java 有什么不同

> 通常只要能够指出有什么不同的话，读者就已经能够理解了。（如果有必要的话，用 tips 的形式来更仔细介绍不同之处，tips 即可以是帮助记忆的，也可以是解释 Kotlin 为什么要这样做的）



* 函数——怎样在 Kotlin 中声明一个函数

  > 函数的简化至多提及到 Unit 作为返回值类型可以省略

* 变量——在 Kotlin 中声明一个变量

  > 至多提及到可推断类型时，类型可以省略

  * val/var

  * 基本数据类型及对象的声明 (new 关键字)
  * 类型推断

* 类——怎样在 Kotlin 中声明类

  * 不同的 Type 的声明
  * 内部 Type 声明
  * 继承及实现
  * 接口和 Java7/8 的区别



## 空安全设计

* 可空类型和不可空类型
* 调用可空类型
* 非空断言
* `lateinit` 
* 平台类型
* "可空基本数据类型" 和 "不可空数据类型"





## 类型

* 类型的判断以及强转
* 获取 Class 对象
  * ::class，.javaClass，::class.java 
* `Any` 和 `Unit` （不提 `NoThing` ）





## setter/getter

* Kotlin 调用 Java 的 setter/getter
* Kotlin 书写 setter/getter
* 反过来 Java 调用 Kotlin 的 setter/getter



## 条件控制

* `if/else`
* `when`
* `try-catch`
  * 非强制受检

* `?:` 操作符
* `==` 和 `===`

* 循环
  * 区间



## 部分关键字

* `override` 
* `open/final`
* 待定



## 两个转换工具

*  Java 转换成  Kotlin 的工具

  * 转换后需要手动修改的地方（尽可能多。这样才会显得我们的教程有用，而不是用工具就行了？）

* 反编译 Kotlin bytecode 工具

  *  查看 Kotlin "去糖" 后对应的 Java 代码
  
  





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
