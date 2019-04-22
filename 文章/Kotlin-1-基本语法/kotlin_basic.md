# Kotlin 基础

Kotlin 是一门 Google 极为推崇的语言，目前已经成为了 Android 的官方开发语言，这也是一门值得 Android 工程师学习的语言。同为 JVM 系列语言，Kotlin 和 Java 有不少相似之处，学习曲线并没有大家想象的那么陡峭。下面从一个简短的例子说起，介绍一下 Kotlin 的基础语法。

这一天，老王代码写着写着肚子就饿了，随手拿起手机就打开了点餐机，准备点几份吃的。毕竟填饱肚子，才有力气好好搬砖嘛。老王先点了一盘最爱的小炒肉，又来了一份宫保鸡丁。这个点餐机能实时显示已选的菜品和价格，还是挺好用的呢。老王看到今天点餐机竟然有一个「山珍海味」的选项，就决定尝尝看。结果选了之后被告知厨子不会做这个菜，真是可恶😠，心想大概是写点餐机的程序猿又写了一个 bug 吧。

![preview](https://ws1.sinaimg.cn/large/006tNc79gy1g2ay3mth3wg30bu0l4au2.gif)

> 点餐机项目地址 > https://github.com/bruce3x/KotlinDemo

项目主要就两个文件：`FoodOrderService` 和 `MainActivity`。

其中 FoodOrderService 中有三个主要方法：

- select() 选择食物
- getOrderDetail() 获取订单详情
- reset() 重置订单

而 MainActivity 中主要是 UI 组件初始化和绑定事件的相关代码。

整个项目包含了 Java 和 Kotlin 两个语言的版本，分别在 `java_version` 和 `kotlin_version` module 下，可以去对应的 module 里查阅代码。

下面就先从 Java 版本的代码开始吧。

## 启用 Kotlin

要学习 Kotlin，首先要了解如何将原来的 Java 代码转换成 Kotlin。

首先要在项目中添加 Kotlin 相关依赖，在项目根目录 `build.gradle` 文件中添加 Kotlin 的 gradle 插件。

> 代码参考 [添加 gradle 插件](https://github.com/bruce3x/KotlinDemo/blob/v1.0/build.gradle#L3)

```groovy
buildscript {
    ext.kotlin_version = '1.3.30'
    repositories {
        google()
        jcenter()
        
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.3.2'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}
```

然后在 app module 下的 `build.gradle` 文件中启用 Kotlin 插件，并且添加 Kotlin 标准库的依赖。

> 代码参考 [启用 Kotlin 插件](https://github.com/bruce3x/KotlinDemo/blob/v1.0/kotlin_version/build.gradle#L2)、[标准库依赖](https://github.com/bruce3x/KotlinDemo/blob/v1.0/kotlin_version/build.gradle#L30)

```groovy
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android' // 启用 kotlin 插件

// ...

dependencies {
    // ...
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
}
```

接下来就是将 Java 代码转换成 Kotlin 代码了，最简单的方式自然是用 Kotlin 重新写一遍😅。不过 Android Studio 给我们提供了一个工具，可以自动将 Java 代码转换成 Kotlin 代码。首先打开一个要转换的 Java 文件，然后使用 `MainMenu -> Code -> Convert Java File to Kotlin File` 操作（图片中的最后一项）即可。

![006tNc79gy1g2488tz1wvj308f0h1guf.jpg](https://ws3.sinaimg.cn/large/006tNc79gy1g2488tz1wvj308f0h1guf.jpg)

## Kotlin 基础语法

### 基本类型

Kotlin 中的基本数据类型主要有：数值(numbers)、字符(characters)、布尔值(booleans)和字符串(string)。

### 数值类型

数值类型包括了 Int、Float、Double、Long、Short、Byte 这几种类型，写起来和 Java 并没有太大区别。

- `123`、`123L`、`0x0F`、`0b00001011` 分别用十进制、十六进制和二进制表示一个 Int 或 Long
- `123.5`、`123.5e10`、`123F` 来表示浮点数
- `1234_5678_9012_3456L` 也可以在数字中用下划线来提高可读性

在 Kotlin 中，数值类型**没有隐式自动转换**(Automatic Conversions)，他们都提供了一组名为 `toXXX()` 的转换方法，不同类型的数值之间必须显式转换。

> Kotlin 代码参考

```kotlin
val a: Int = 1 
val b: Long = a // 编译错误! Type mismatch: inferred type is Int but Long was expected
val b: Long = a.toLong() // 显式转换
```

#### 字符类型

Kotlin 中的字符类型不能直接当作数字来用，不能与数字进行比较或者赋值，需要通过 `Chat.toInt()` 或 `Int.toChar()` 转换才行。

#### 布尔值

布尔值分别为 `true` 和 `false`，可以使用逻辑运算符 `&&`、`||` 和 `!`。

#### 字符串

Kotlin 中可以用双引号 `"` 声明普通的字符串，也可以用三个双引号 `"""` 来声明一个多行的字符串。

在 Java 中拼接字符串，通常用 `+` 手动拼接，或者用 `StringBuilder`类和 `String#format(String, Object...)`方法。而 Kotlin 支持了字符串模板语法，用 `$` 向字符串中注入变量值。如果需要表达式的值，可以用 `${...}`。 

> Java 代码参考 [字符串模版](https://github.com/bruce3x/KotlinDemo/blob/v1.0/java_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.java#L60)

```java
Food item = // ...;
String line = String.format(Locale.getDefault(), "#%d  菜名：%s      价格：￥%.2f", index, item.name, item.price);
```

> Kotlin 代码参考 [字符串模板](https://github.com/bruce3x/KotlinDemo/blob/v1.0/kotlin_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.kt#L42)

```kotlin
val item: Food = // ...
val line = "#$index   菜名：${item.name}      价格：￥${item.price}"
```

### 变量

Kotlin 中使用 `val` 关键字来声明**只读**变量(Read-only local variables)，使用 `var` 关键字来声明变量，格式为`var/val 变量名: 变量类型`。变量类型可以手动指定，也可以由自动推断(inferred type)得出。

> Java 代码参考

```java
boolean isVip = false;
List<String> lines = new ArrayList<>();
float amount;
amount = 100F;

float total = 0F;
total = 1F
```

> Kotlin 代码参考 

```kotlin
val isVip: Boolean = false          // 显式指定类型
val lines = ArrayList<String>()     // 自动推断类型
val amount: Float                   // 没有初始化，需要指定类型
amount = 100F

var total = 0F                      // 数值可变的变量
total = 1F
```

> 这里创建 `ArrayList` 实例的写法，在后面“类”那一小节中会讲到。

### 函数

Kotlin 中使用 `fun` 关键字来声明一个函数，函数的返回值类型在函数签名末尾指定。如果函数体只有一个表达式，返回值类型也可以自动推断。如果方法没有返回值，那么返回值类型就为 `Unit`，等同于 Java 中的 `void`，也可以省略不写出。

> Java 代码参考 [无返回值的方法](https://github.com/bruce3x/KotlinDemo/blob/v1.0/java_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.java#L33)、[带返回值的方法](https://github.com/bruce3x/KotlinDemo/blob/v1.0/java_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.java#L52)

```java
// 没有返回值
public void select(String food) {

}

// 有返回值
public String getOrderDetail() {
    return "";
}
```

> Kotlin 代码参考 [无返回值的函数](https://github.com/bruce3x/KotlinDemo/blob/v1.0/kotlin_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.kt#L22)、[带返回值的函数](https://github.com/bruce3x/KotlinDemo/blob/v1.0/kotlin_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.kt#L34) 

```kotlin
// 没有返回值
fun select(food: String) {

}

// 有返回值
fun getOrderDetail(): String {
    return ""
}

// 自动推断返回值类型
fun getOrderDetail() = "detail"
```

### 控制流程

#### If 语句

If 语句基本用法和 Java 一致。

> Java 代码参考 [if-else 语句](https://github.com/bruce3x/KotlinDemo/blob/v1.0/java_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.java#L67)

```java
float amount;
if (isVip) {
    amount = 0.88F * total;
} else {
    amount = total;
}
```

> Kotlin 代码参考 [if-else 语句](https://github.com/bruce3x/KotlinDemo/blob/v1.0/kotlin_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.kt#L49)

```kotlin
val amount: Float
if (isVip) {
    amount = 0.88F * total
} else {
    amount = total
}
```

此外，If 语句还支持作为表达值返回，等效于 Java 中的三目运算符(Conditional Operator `?  :`)。

> Java 代码参考 三目运算符

```java
float amount = isVip ? 0.88F * total : total;
```

> Kotlin 代码参考 if-else 返回值

```kotlin
val amount: Float = if (isVip) 0.88F * total else total
```

#### When 语句

`when` 可是视作 Kotlin 中的 switch 语句。每一条分支的值，可以是常量或表达式。

> Java 代码参考 [switch 语句](https://github.com/bruce3x/KotlinDemo/blob/v1.0/java_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.java#L34)

```java
public void select(String food) throws FoodUnavailableException {
    switch (food) {
        case FOOD_A:
            foods.add(new Food(FOOD_A, 15F));
            break;
        case FOOD_B:
            foods.add(new Food(FOOD_B, 16.5F));
            break;
        case FOOD_C:
            foods.add(new Food(FOOD_C, 20.0F));
            break;
        default:
            throw new FoodUnavailableException(food);
    }
}
```

> Kotlin 代码参考 [when 语句](https://github.com/bruce3x/KotlinDemo/blob/v1.0/kotlin_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.kt#L22)

```kotlin
@Throws(FoodUnavailableException::class)
fun select(food: String) {
    when (food) {
        FOOD_A -> foods.add(Food(FOOD_A, 15f))
        FOOD_B -> foods.add(Food(FOOD_B, 16.5f))
        FOOD_C -> foods.add(Food(FOOD_C, 20.0f))
        else -> throw FoodUnavailableException(food)
    }
}
```

另外 Kotlin 的 `when` 语句也可以不传入参数，这样子每条分支的值必须是**布尔值**。比如这样：

> Kotlin 代码参考 无参数的 when 语句

```kotlin
val number = 233
when{
    number < 5 -> println("small number")
    number > 5 -> println("large number")
    else -> println("number is 5")
}
```

#### For 循环

`for` 语句本质上是在遍历一个对象的迭代器，只要一个对象能提供迭代器，就能用 `for` 循环，比如常用的列表集合、字符串等。

> Java 代码参考  [for-loop 语句](https://github.com/bruce3x/KotlinDemo/blob/v1.0/java_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.java#L59)

```java
List<Food> items = new ArrayList<>();
for (Food item : items) {
    // 一些操作 
}
```

> Kotlin 代码参考  [for-loop 语句](https://github.com/bruce3x/KotlinDemo/blob/v1.0/kotlin_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.kt#L41)

```kotlin
val items = ArrayList<Food>()
for (item in items) {
    // 一些操作
}
```

#### While 循环

`while` 和 `do .. while` 语句与 Java 没有什么差别。

> Kotlin 代码参考 while 循环

```kotlin
while (x > 0) {
    x--
}

do {
    val y = retrieveData()
} while (y != null)
```

> `continue` 和 `break` 语句在 for / while 循环中都能正常使用

### 异常处理

Kotlin 中的异常处理和 Java 类似，抛出异常用 `throw` 表达式，捕获异常用 `try-catch-finally` 语句。不过声明异常的写法略有不同，Java 中是在方法签名后面加上 `throws` 语句，而 Kotlin 是给函数加上 `@Throws()` 注解。

> Java 代码参考 [声明异常](https://github.com/bruce3x/KotlinDemo/blob/v1.0/java_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.java#L33)、[抛异常](https://github.com/bruce3x/KotlinDemo/blob/v1.0/java_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.java#L45)、[捕获异常](https://github.com/bruce3x/KotlinDemo/blob/v1.0/java_version/src/main/java/com/bruce3x/kotlin/demo/MainActivity.java#L46)

```java
// 声明异常
public void select(String food) throws FoodUnavailableException {
}

// 抛出异常
throw new FoodUnavailableException("不会做的菜");

// 捕获异常
FoodOrderService service = new FoodOrderService(true);
String food = ((RadioButton) selected).getText().toString();

try {
    // 会产生异常的代码
    service.select(food);
} catch (FoodOrderService.FoodUnavailableException e) {
    // 处理异常
    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
} finally {
    // 可选的收尾操作
    tvDetail.setText(service.getOrderDetail());
}
```

> Kotlin 代码参考 [声明异常](https://github.com/bruce3x/KotlinDemo/blob/v1.0/kotlin_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.kt#L21)、[抛异常](https://github.com/bruce3x/KotlinDemo/blob/v1.0/kotlin_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.kt#L27)、[捕获异常](https://github.com/bruce3x/KotlinDemo/blob/v1.0/kotlin_version/src/main/java/com/bruce3x/kotlin/demo/MainActivity.kt#L45)

```kotlin
// 声明异常
@Throws(FoodUnavailableException::class)
fun select(food: String) {
}

// 抛出异常
throw FoodUnavailableException("不会做的菜")

// 捕获异常
val service = FoodOrderService(true)
val food = selected.text.toString()
try {
    // 会产生异常的代码
    service.select(food)
} catch (e: FoodOrderService.FoodUnavailableException) {
    // 处理异常
    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
} finally {
    // 可选的收尾操作
    tvDetail.text = service.getOrderDetail()
}
```

另外，在 Kotlin 中 `try-catch` 语句可以作为表达式，输出返回值。

```kotlin
val a: Int? = try { parseInt(input) } catch (e: NumberFormatException) { null }
```

### 类

在 Kotlin 中通过 `class` 关键字声明一个类。

> Java 代码参考 [声明 class](https://github.com/bruce3x/KotlinDemo/blob/v1.0/java_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.java#L12)

```java
public class FoodOrderService {
    private boolean isVip;
    public FoodOrderService(boolean isVip) {
        this.isVip = isVip;
    }
}
```

> Kotlin 代码参考 [声明 class](https://github.com/bruce3x/KotlinDemo/blob/v1.0/kotlin_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.kt#L11)

```kotlin
class FoodOrderService(private val isVip: Boolean) {
}
```

Kotlin 中创建一个类的实例，不需要 `new` 关键字。

> Kotlin 代码参考 [class 实例化](https://github.com/bruce3x/KotlinDemo/blob/v1.0/kotlin_version/src/main/java/com/bruce3x/kotlin/demo/MainActivity.kt#L14)

```kotlin
val service = FoodOrderService(true)
```

另外在 Kotlin 代码中用到了**伴生对象**(Companion Objects)，它内部声明的成员变量和函数可以理解为 Java 类的静态变量和静态方法。具体细节会在后续文章中讲解。

> Kotlin 代码参考 [伴生对象](https://github.com/bruce3x/KotlinDemo/blob/v1.0/kotlin_version/src/main/java/com/bruce3x/kotlin/demo/FoodOrderService.kt#L72)

```kotlin
companion object {
    const val FOOD_A = "宫保鸡丁"
    const val FOOD_B = "鱼香肉丝"
    const val FOOD_C = "小炒肉"
}
```

## 总结

Kotlin 作为一门 JVM 语言，在语法上和 Java 较为类似，类比两者的差异，则能更快更好地掌握 Kotlin 基本写法。