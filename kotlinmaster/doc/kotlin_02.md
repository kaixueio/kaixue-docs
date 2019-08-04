## Kotlin 里那些「不是那么写的」

Kotlin 是一门和 Java 很类似的面向对象语言，很多概念都是相通的，这为我们 Andriod 开发由 Java 转向 Kotlin 降低了一些门槛。但还有一些不同的写法需要我们注意，这篇文章我们就来看看 Kotlin 里那些「不是那么写的」地方。

### Constructor

让我们来看看两段分别用 Java 和 Kotlin 写的代码：

``` java
☕️
public class User {
  int id;
  String name;
  public User(int newId, String newName) {
    id = newId;
    name = newName;
  }
}
```

``` kotlin
🏝️
class User {
  val id: Int
  val name: String
  constructor(newId: Int, newName: String) {
    id = newId
    name = newName
  }
}
```

我们可以发现构造器的写法主要有两点不同：

- Java 中构造器和类同名，Kotlin 中使用 constructor 表示。
- Kotlin 构造器没有 public 修饰，因为默认可见性就是公开的，关于可见性修饰符这里我们先不展开，后面会讲到。

Kotlin 除了这种和 Java 类似的构造器之外还引入了 「主构造器 primary constructor」，可以让你的代码更加直观和简洁：

``` kotlin
class User(val id: Int, val name: String) {}
```

而和 Java 比较类似的构造器在 Kotlin 中称之为 「次构造器 secondary constructor」，接下来我们分别看看这两种构造器。

#### 主构造器

Kotlin 中每个类只能有一个主构造器，属于类头部的一部分，位于类名之后：

``` kotlin
class User constructor(name: String) {}
```

如果需要限制构造器的可见性或者给构造器添加注解直接放在 constructor 前面：

``` kotlin
class User private @Inject constructor(name: String) {}
```

当不需要修饰时可以省略掉关键字 constructor：

``` kotlin
class User(name: String) {}
```

有人可能会问，「如果我想在构造器中执行初始化操作该怎么做呢？」为此 Kotlin 提供了初始化代码块来负责这部分任务：

``` kotlin
🏝️
init {
  ...
}
```

初始化代码块由 init 关键字和一对大括号构成，这其实不是 Kotlin 创造的新概念，对应的 Java 在类中也有类似的功能，也可以用来完成初始化操作：

``` java
☕️
{
  ...
}
```

一个 Kotlin 类中可以有多个初始化代码块，它们的执行顺序和创建的顺序是一致的：

``` kotlin
class User {
  init {
    println("First init block.")
  }
  init {
    println("Second init block.")
  }
}
```

当创建上面这个类的实例时输出如下：

```
First init block.
Second init block.
```

当类中存在属性初始化代码时，执行的优先级和初始化代码块是同级的：

``` kotlin
class User {
  val firstProperty = "First property.".also { println(it) }
  init {
    println("First init block.")
  }
  val secondProperty = "Second property.".also { println(it) }
  init {
    println("Second init block.")
  }
}
```

当创建上面这个类的实例时输出如下：

```
First property.
First init block.
Second property.
Second init block.
```

由此可见属性初始化代码和初始化代码块处于同一个执行上下文，另一个证据是这两块代码都可以访问主构造器的参数：

``` kotlin
class User(name: String) {
  val length: Int = name.length
  init {
    println("My name is $name")
  }
}
```

Kotlin 还有一种简洁的写法用于将主构造器中的参数声明为属性，并用参数值初始化属性：

``` kotlin
class User(val name: String, val age: Int) {}
```

这种写法等价于：

``` kotlin
class User(name: String, age: Int) {
  val name: String = name
  val age: Int = age
}
```

对应于 Java 中的写法：

``` kotlin
☕️
public class User {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

相比之下，Kotlin 的写法确实简化了很多。

#### 次构造器

Kotlin 中的次构造器和 Java 类似写在类中，通过 constructor 表示，并且关键字不可以省略：

``` kotlin
class User {
  private val name: String
  constructor(name: String) {
    this.name = name
    println("My name is $name.")
  }
}
```

写次构造器时，如果存在主构造器，需要通过代理的方式引用到主构造器：

``` kotlin
class User(val name: String) {
  private var age: Int = 0
  constructor(name: String, age: Int) : this(name) {
    this.age = age
  }
}
```

上面这段代码属于直接代理主构造器，也可以通过间接的方式代理主构造器：

``` kotlin
class User(val name: String) {
	var age: Int = 0
  var gender: String = "male"
  constructor(name: String, age: Int) : this(name) {
    this.age = age
  }
  constructor(name: String, age: Int, gender: String) : this(name, age) {
    this.gender = gender
  }
}
```

这段代码里，第二个次构造器就是通过代理第一个次构造器间接代理了主构造器。在代理构造器时，通过 this 关键字引用别的构造器，通过参数定位具体的构造器。

被代理构造器的初始化代码总是先执行，而初始化代码块属于主构造器的一部分，所以初始化代码块在所有的次构造器代码之前执行：

``` kotlin
class User constructor(name: String) {
  init {
    println("Init block.")
  }
  constructor(name: String, age: Int) : this(name) {
    println("Secondary constructor.")
  }
}
```

创建上面这个类的实例时会输出：

```
Init block.
Secondary constructor.
```

即使没有声明主构造器，初始化代码块也先于次构造器执行，这时次构造器相当于代理了一个空的主构造器：

``` kotlin
class User {
  init {
    println("Init block.")
  }
  constructor(name: String) {
    println("Secondary Constructor.")
  }
}
```

创建这个类的实例时输出：

```
Init block.
Secondary Constructor.
```

上面这段代码等价于：

``` kotlin
class User constructor() {
  init {
    println("Init block.")
  }
  constructor(name: String) : this() {
    println("Secondary Constructor.")
  }
}
```

### `final`

在 Java 中如果我们想声明一个只读字段或者变量，直接在类型前加上 final 关键字：

``` java
☕️
final int final1 = 1;

void method(final String final2) {
  System.out.println(final2);
  final Date final3 = new Date();
  System.out.println(final3);
}
```

对应的，在 Kotlin 中上面这段代码这样写：

``` kotlin
🏝️
val fina1 = 1

fun method(final2: String) {
    println(final2)
    val final3 = Date()
    println(final3)
}
```

可以看到不同点主要有：

- final 变成 val，由于类型推断，类型可以省略不写，写法上简短了一些。
- Kotlin 方法参数默认是 val 类型，所以参数前不需要写 val 关键字。

上一篇文章讲过 `var` 是 variable 的缩写，而这里的 `val` 是 value 的缩写，从命名上也能看出只读的含义。

相比 Java 中通过额外添加 final 前缀来表示只读，Kotlin 的只读声明简化了很多，只是把 var 最后一个字母改成 l，大大增加了开发者使用只读类型的频率。虽然只是减少了一个单词，但考虑到变量声明的频率，总体效果还是很可观的。这种优化使得在该加只读限制的地方加上限制，减少了出现错误的概率，从而提高代码质量。

#### `val`自定义 getter

不过 `val` 和 `final` 还是有一点区别的，虽然 `val` 修饰的变量不能二次赋值，但可以通过自定义变量的 getter 方法来让变量每次被访问时返回动态计算的值：

``` kotlin
val size: Int
		get() {
      return items.size
    }
```

前面说到类的属性类型可以通过初始化代码进行类型推断，除此之外也可以通过 getter 方法的返回值推断，而且 Kotlin 中可以通过 `=` 直接连接函数表达式，所以上面这段代码可以简化为：

``` kotlin
val size get() = items.size
```

不过这个属于特殊用法，一般情况下 `val` 还是对应于 Java 中的 `final` 使用的。一个可能的应用是用于简化一些没有参数的属性类方法调用：

``` kotlin
fun isEmpty(): Boolean {
  return items.size == 0
}
```

可以简化为：

``` kotlin
val isEmpty: Boolean
		get() {
      return items.size == 0
    }
```

上面的方法和属性的调用方式分别如下：

``` kotlin
any.isEmpty()
any.isEmpty
```

少了一对括号，可以让代码简洁一些。

### `static` property / function

#### Kotlin 中的静态变量和方法

在 Java 中我们定义类级别的属性或者方法是通过在类中定义静态属性或者方法：

``` java
class A {
  public static int property = 1;
  public static void method() {
    println("A.method()")
  }
}
```

这样我们就可以不需要创建该类的实例，直接通过类名调用静态变量或者方法：

``` java
int variable = A.property;
A.method();
```

那么在 Kotlin 中如果我们想实现类似的功能该怎么做呢，在类中我们可以创建一个叫作 `companion object` 的东西，将我们想定义的静态属性和方法放在其中：

``` kotlin
class A {
  companion object {
    val property: Int = 1
    fun method() {
      println("A.method()")
    }
  }
}
```

这样我们在调用的时候可以像 Java 的静态变量和方法一样去调用：

``` kotlin
val variable = A.property
A.method()
```

这个 `companion object`  是什么意思呢，为什么要使用这种方式定义类级别的变量或者方法，我们先来看看什么是 object。

#### `object`

##### `object` 创建单例类

`object` 字面意思是对象，与 Java 中通过 `new` 创建一个类的对象不同，Kotlin 中可以通过 `object` 直接创建一个对象实例：

``` kotlin
object A {
  val number: Int = 1
  fun method() {
    println("A.method()")
  }
}
```

和类的定义类似，不过 `class` 关键字替换成 `object` ，调用方式如下：

``` kotlin
val result = A.number + 1
A.method()
```

看着是不是很像 Java 中的单例，`object` 一个很重要的用途就是声明一个单例类。

我们来看看 Java 中要实现上面这个单例类需要怎么做：

``` java
public class A {

    private static A sInstance;

    public static A getInstance() {
        if (sInstance == null) {
            sInstance = new A();
        }
        return sInstance;
    }

    private A() {
    }

    public int number = 1;

    public void method() {
        System.out.println("A.method()");
    }
}
```

调用的时候：

``` java
int result = A.getInstance().number + 1;
A.getInstance().method()
```

可以看到 Java 中为了实现单例类写了大量的模版代码，在没有 Kotlin 的时候大家写多了也就习惯了，直到遇见了 Kotlin 才发现单例类原来可以这么简单，而且调用也比较简洁，不需要 getInstance() 方法。

通过 `object` 创建的对象也可以继承别的类或者接口，和创建一个类是一样的：

``` kotlin
open class A {
    open fun method() {
        println("A.method()")
    }
}

interface B {
    fun interfaceMethod()
}

object C : A(), B {

    override fun method() {
        println("C.method()")
    }

    override fun interfaceMethod() {
        println("C.interfaceMethod()")
    }
}
```

##### `object` 创建匿名类

有时我们需要改变类方法的实现，但因为改动很少不想创建该类的子类，Java 中是通过创建匿名内部类来实现这个目的：

``` java
ViewPager.SimpleOnPageChangeListener listener = new ViewPager.SimpleOnPageChangeListener() {
	@Override
	public void onPageSelected(int position) {
		// override
	}
};
```

Kotlin 中通过 `object` 创建一个继承该类的对象和对象表达式来实现：

``` kotlin
val listener = object: ViewPager.SimpleOnPageChangeListener() {
    override fun onPageSelected(position: Int) {
        // override
    }
}
```

和 Java 创建匿名类的方式很相似，你可以简单理解为通过 object 创建一个匿名内部类。这里需要注意的是，`=` 后的语句不能单独存在，因为对象表达式是指将对象赋值给一个变量或者作为参数传递给方法，如果没有 `=` 以及前面的变量，这段代码就不能称为对象表达式，就会报错：

``` kotlin
		 👇 // compile error: Name expected
object: ViewPager.SimpleOnPageChangeListener() {
    override fun onPageSelected(position: Int) {
        // override
    }
}
```

`object` 后编译器提示这里需要一个对象的名字，因为编译器以为你想创建一个继承 `ViewPager.SimpleOnPageChangeListener` 的对象，而创建一个单独的对象必须指定名字，否则无法引用该对象。

##### `companion object`

前面我们说到访问对象中的变量或者方法时直接通过类名引用，就像 Java 的静态变量和方法一样，不同的是不需要在每个变量和方法前面用 `static` 修饰，因为 `object` 创建的对象内所有变量和方法默认都是静态的，没得选。如果我们只想让类中的一部分方法和变量是静态的该怎么做呢：

``` kotlin
class A {
  object B {
    var c: Int = 0
  }
}
```

如上，我们可以在类中创建一个对象，这样我们把需要静态的变量或方法放在内部对象 B 中，外部可以通过如下的方式调用该静态变量：

``` kotlin
A.B.c
```

类中嵌套的对象可以用 `companion` 修饰：

``` kotlin
class A {
  companion object B {
    var c: Int = 0
  }
}
```

`companion` 可以理解为伴随、伴生，表示修饰的对象和外部类绑定，这样的好处是调用的时候可以省掉对象名：

``` kotlin
A.c
```

当有 `companion` 修饰时，对象的名字也可以省略掉：

``` kotlin
class A {
  companion object {
    var c: Int = 0
  }
}
```

这就是我们这节最开始讲到的，和 Java 静态变量或方法的等价写法：`companion object`。

#### top-level property / function 声明

不同于 Java 所有的方法和变量都必须写在类中，Kotlin 可以在类外写变量和方法，这个称之为「top-level property/function」即顶层声明。

``` kotlin
package com.hencoder.plus

fun topLevelFuncion() {
}
```

这样写的属性和函数，不属于任何 `class`，而是直接属于 `package`，它和静态变量、静态方法一样是全局的，但用起来更方便：你在其它地方用的时候，就连类名都不用写：

``` kotlin
import com.hencoder.plus.topLevelFunction

topLevelFunction()
```

写在顶级的方法或者变量有个好处，在 Android Studio 中写代码时，IDE 很容易根据你写的方法前几个字母自动联想出相应的方法，提高了写代码的效率，而且可以防止项目中的重复代码。

顶级函数不写在类中可能有一个问题：如果在不同文件中声明命名相同的函数，使用的时候会不会混淆？我们来看一个例子：

* 在 `org.kotlinmaster.library` 包下有一个方法 method：

  ``` kotlin
  package org.kotlinmaster.library1
  
  fun method() {
    println("library1 method()")
  }
  ```

* 在 `org.kotlinmaster.library2` 包下也有一个同名方法：

  ``` kotlin
  package org.kotlinmaster.library2
  
  fun method() {
    println("library2 method()")
  }
  ```

我们看看在使用的时候如果同时调用这两个同名方法：

```kotlin
import org.kotlinmaster.library1.method

fun test() {
  method()
  org.kotlinmaster.library2.method()
}
```

可以看到当出现两个同名顶级函数时，会通过加上包前缀来区分，这也印证了顶级函数是属于包的特性。

#### 对比

那在实际使用中，我们该使用哪一种呢？`companion object`？`object` ？还是 `top-level` ？

其实一般来说，如果你想写工具类，那直接创建一个文件，里面全都写成 `top-level` functions 就行了；但 `companion object` 和 `object` 是可以有父类和接口的，所以你利用这点可以对你的全局函数进行一些功能扩展和延伸。

所以简单的判断原则是：能写在 `top-level` 就写在 `top-level`，但如果需要继承别的类或者实现接口，就用 `companion object` 或者直接用 `object`。

### 常量

首先我们来看看 Java 中怎么声明一个常量：

``` java
public class Sample {
  public static final int CONST_NUMBER = 1;
}
```

然后我们看看 Kotlin 中怎么声明一个变量：

``` kotlin
class Sample {
  companion object {
    const val CONST_NUMBER = 1
  }
}
```

可以发现不同点有：

- 必须声明在类的伴随对象内，因为常量是静态的。
- 通过 Kotlin 新增的 `const` 关键字修饰。

除此之外还有一个区别是 Kotlin 中只有基本类型和 String 类型可以声明成常量，原因是 Kotlin 中的常量指的是 compile-time constant 编译时常量。它的意思是「编译器在编译的时候就知道这个东西在每个调用处的实际值」，因此可以在编译时直接把这个值硬编码到代码里使用的地方。

而非基础或者 String 类型可以通过调用对象的方法改变对象内部的值，这样这个变量就不是常量了，我们来看一个 Java 的例子，比如一个 User 类：

``` java
public class User {
  public User(int id, String name) {
    this.id = id;
    this.name = name;
  }
  
  int id;
  String name;
}
```

我们在使用的地方声明一个 `static final` 的 User，它是不能二次赋值的：

``` java
static final User user = new User(123, "rengwuxian");
```

但是可以通过访问这个类的成员变量改变它的值：

``` java
user.name = "zhukai";
```

所以相比 Java 里声明的常量，Kotlin 的常量限制更严格，更加符合常量的定义。

### 数组和集合

#### 数组

声明一个 String 数组，Java 中的写法：

``` java
String[] strs = {"a", "b", "c"};
```

Kotlin 中的写法:

``` kotlin
val strs: Array<String> = arrayOf("a", "b", "c")
```

可以看到 Kotlin 中的数组是一个拥有泛型的类，创建方法也是泛型方法，和集合数据类型一样。

Kotlin 中获取或者设置数组数据和 Java 一样可以使用方括号加下标的方式索引：

``` kotlin
println(strs[0])
strs[1] = "B"
```

初次之外，Kotlin 的 Array 类型还可以使用 `get()` 和 `set()` 方法取值和赋值：

``` kotlin
println(strs.get(0))
strs.set(1, "B")
```

第二种方式有点像集合类型，这正是 Kotlin 中将数组泛型化的原因：使对数组的操作像集合一样功能更强大，由于泛型化，Kotlin 可以通过扩展方法的方式给数组增加很多有用的工具方法：

- `contains()`
- `first()`
- `find()`

这样数组的实用性就大大增加了。

Kotlin 的数组编译成字节码时使用的仍然是 Java 的数组，但在语言层面是泛型实现，这样会失去协变 (covariance) 特性，就是在 Kotlin 中字类数组对象不能赋值给父类的数组：

``` kotlin
val strs: Array<String> = arrayOf("a", "b", "c")
val anys: Array<Any> = strs // ❌
```

而这在 Java 中是可以的：

``` java
String[] strs = {"a", "b", "c"};
Object[] objs = strs; // ✅
```

关于协变的问题，这里就先不展开，后面讲泛型的时候会提到。

#### 集合





