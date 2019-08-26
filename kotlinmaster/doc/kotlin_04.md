# Kotlin 的泛型

这一期文章来讲讲泛型。



泛型是我们在日常开发中很常见的东西，比如我们常常会拿集合类来装一些 View。

```java
☕️
List<TextView> textViews = new ArrayList<TextView>();
//      👆           指定泛型类型             👆
```

```kotlin
🏝️
var textViews: MutableList<TextView> = ArrayList<TextView>()
//                            👆    指定泛型类型      👆
```

在 Java 中，创建新集合的时候会根据变量类型来推断出泛型的类型，可以简化一点代码；Kotlin 有更强大的类型推断系统，同样可以简化代码：

```java
☕️
List<TextView> textViews = new ArrayList<>();
//                                      👆 省略泛型类型
```

```kotlin
🏝️
var textViews: MutableList<TextView> = ArrayList();
//                                             👆 Kotlin 甚至可以省略<>
```



Kotlin 的泛型基本上继承了 Java 泛型的特性，想要学好 Kotlin 的泛型，那就先来回顾一下泛型在 Java 中的使用吧。



在 Java 中使用泛型的时候，我们经常会遇到这种场景：不能把子类的 `List` 对象赋值给父类 `List` 对象的引用。

```java
☕️
List<TextView> textViews = new ArrayList<Button>();
//                       👆 incompatible types: 
//                          ArrayList<Button> cannot be converted to List<TextView>
```

这是因为 Java 的泛型本身具有不可变性(invariance)。具体原因是 Java 的泛型类型会在编译时发生**类型擦除**，为了保证类型安全，不允许这样赋值。至于什么是类型擦除，这里就不展开了，网上有很多文章来讲解这个东西。不过 Java 提供了泛型通配符 `? extends` 和 `? super` 来解决这问题。



### Java 中的 `? extends`

Java 提供了上界通配符 `? extends` 来使泛型具有协变性(covariance)，也就可以允许子类的泛型类型对象赋值到父类的泛型类型引用上去。也就是这样：

```java
☕️
     👇
List<? extends TextView> textViews;
textViews = new ArrayList<Button>();
```

这样定义的 `textViews` 变量的泛型类型是一个 TextView 或其子类型的的**未知类型**。由于我们不知道什么样的对象符合这个未知类型，也就没法向 List 对象中写入新元素，只能从中读取到 TextView 类型的对象。相当于作为生产者(Producer)只提供数据读取。

> 但它并不是不可变的(immutable)，因为 `clear()` 方法与类型无关，还是可以调用的，只是不能调用 `add()` 这种与类型相关的写入方法。

比如，有一个方法接收类型参数为 TextView 的 List，遍历打印它们的文字内容：

```java
☕️
public void printTexts(List<TextView> textViews) {
    for (TextView textView: textViews) {
        System.out.println(textView.getText());
    }
}
```

按照 Java 的规矩，如果传入一个参数类型为 Button 的 List 对象，肯定会报错的：

```java
☕️
List<Button> buttons = new ArrayList<>();
printTexts(buttons); 
// 👆 IDE 会提示错误，incompatible types: List<Button> cannot be converted to List<TextView>

```

如果在方法参数上加上 `? extends`，就不会报错啦：

```java
☕️
                             👇
public void printTexts(List<? extends TextView> textViews) {
    for (TextView textView: textViews) {
        System.out.println(textView.getText());
    }
}
```



当你遇到「只需要读取，不需要写入」的场景，就可以用 `? extends` 来使 Java 泛型支持协变，以此来扩大变量或者参数的接收范围。



### Java 中的 `? super`

Java 中还提供了下界通配符 `? super` 来使泛型具有逆变性(contravariance)，也就可以允许父类的泛型类型对象赋值到子类的泛型类型引用上去。比如这样：

```java
☕️
     👇
List<? super Button> textViews;
textViews = new ArrayList<TextView>();
```

这样定义的 `textViews` 变量的泛型类型是一个 Button 或其父类型的**未知类型**。我们不知道它具体的父类型是什么，只能从中读取到 Object 类型，不过可以写入任何 `Button` 类型的对象。基本上就是作为消费者(Consumer)接收数据写入。

比如又一个方法，接收一个 TextView 类型的 List，然后把自己持有的一个 TextView 对象给它添加进去：

```java
☕️
public void addTextView(List<TextView> textViews) {
    textViews.add(textView);
}
```

这样在使用的时候，如果我传过来一个 View 类型的 List，编译器就报错了：

```java
☕️
List<View> views = new ArrayList<View>();
addTextView(views);
// 👆 IDE 会提示错误，incompatible types: List<View> cannot be converted to List<TextView>
```

但是我们其实是期望它不报错的，我们只是想让这个 TextView 插到一个 View 的 List 里面。为了解除这种限制，我们可以在方法参数上加上 `? super`，就不会报错啦：

```java
☕️
                              👇
public void addTextView(List<? super TextView> textViews) {
    textViews.add(textView);
}
```



当你遇到「只想修改，不需要读取使用」的场景，就可以用 `? super` 来使 Java 泛型支持逆变，以此来反向扩大变量或参数的接收范围。



### 小结

Java 的泛型本身是不支持协变和逆变的。可以使用泛型通配符 `? extends` 来使泛型支持协变，但是「只能读不能写」；可以使用泛型通配符 `? super` 来使泛型支持逆变，但是「不能读只能写」。这也被成为 PECS 法则：*Producer-Extends, Consumer-Super*。



### Kotlin 中的 `out` 和 `in`

Kolin 中的泛型也是不支持协变和逆变的，需要使用关键字 `out` 和 `in`，等同于 Java 中的 `? extends` 和 `? super`。

```kotlin
🏝️
//                  👇 相当于 Java 中的 <? extends TextView>
var textViews: List<out TextView>
var textViews: List<in TextView>
//                  👆 相当于 Java 中的 <? super TextView>
```

和字面意思一样，`out` 表示这个变量或者参数只用来输出，不用来输入，也就是「只能读不能写」；`in` 则相反，表示这个变量或者参数只能用来输入，不用来输出，也就是「不能读只能写」。




### 声明处的 `out` 和 `in`

Kotlin 中的 `out` 和 `in` 关键字，不仅能用在变量和参数的声明上，还能直接用在类或接口的类型参数上：

```kotlin
🏝️                 👇
interface Producer<out T>{
    fun produce(): T
}
                   👇
interface Consumer<in T>{
    fun consume(product: T)
}
```



这种写法直接表明泛型 `T`只是用来输出或者只用来输入的，避免了在每个用到泛型类型的地方都需要加上 `out` 或 `in`，一劳永逸了。



### `*` 号

前面讲到了 Java 中的两个泛型的通配符，其实单个 `?` 号也能作为泛型通配符使用，相当于 `? extends Object`。

```java
☕️  👇
List<?> list;
```

在 Kotlin 中有等效的写法：`*` 号，相当于 `out Any`。

```kotlin
🏝️            👇
var list: List<*>
```





### `where` 关键字

Java 中声明类或接口的时候，可以使用 `extends` 来设置边界，将泛型类型参数限制为某个类型的子集：

```java
☕️                
//                👇  T 的类型必须是 Animal 的子类型
class Monster<T extends Animal>{
}
```

同时这个边界是可以设置多个，用 `&` 符号连接：

```java
☕️
//                            👇  T 的类型必须同时是 Animal 和 Food 的子类型
class Monster<T extends Animal & Food>{ 
}
```



Kotlin 只是把 `extends` 换成了 `:`冒号。

```kotlin
🏝️            👇
class Monster<T: Animal>
```

设置多个边界可以使用 `where` 关键字：

```kotlin
🏝️                👇
class Monster<T> where T: Animal, T: Food
```



### `reified` 关键字

由于 Java 中的泛型存在类型擦除的情况，任何在运行时需要知道泛型确切类型信息的操作都没法用了。比如你不能检查一个对象是否为泛型类型 `T` 的实例：

```java
☕️
<T> void printIfTypeMatch(Object item){
    if(item instanceof T){ // 👈 IDE 会提示错误，illegal generic type for instanceof
        System.out.println(item);
    }
}
```

```kotlin
🏝️
fun <T> printIfTypeMatch(item: Any){
    if(item is T){ // 👈 IDE 会提示错误，Cannot check for instance of erased type: T
        println(item)
    }
}
```



这个问题，在 Java 中的解决方案通常是额外传递一个 `Class<T>` 类型的参数，然后通过 `Class#isInstance` 方法来检查：

```java
☕️                             👇
<T> void check(Object item, Class<T> type) {
    if (type.isInstance(item)) {
               👆
        System.out.println(item);
    }
}
```



Kotlin 中同样可以这么解决，不过还有一个更方便的做法：使用关键字 `reified` 配合 `inline` 来简化代码：

```kotlin
🏝️ 👇         👇
inline fun <reified T> printIfTypeMatch(item: Any) {
    if (item is T) { // 👈 这里就不会在提示错误了
        println(item)
    }
}
```

这具体是怎么回事呢？等到后续章节讲到 `inlline` 的时候会详细说明，这里就不过多延伸了。





Kotlin 的泛型大部分都只是把 Java 泛型的东西换了种写法。如果你看到这里还是觉得很懵逼，可以先去把 Java 泛型好好复习一下，然后在回过头来看 Kotlin 的泛型，自然就懂了。