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

