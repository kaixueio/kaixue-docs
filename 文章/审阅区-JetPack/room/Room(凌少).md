# Jetpack -- Room

某天，风和日丽，你和你媳妇儿正在收拾家里的杂物，突然听到你媳妇儿嘟囔了一句，「之前要用找半天找不到，这会不用了都出来了。东西越来越多，得抽时间列个物品清单了，下回找起来方便」。

不管你有没有女朋友，最后一句话你听进去了。作为一名 Android developer，想着你这一身技能不能无用武之地，刚好 Android 自带 SQLite，记录这点东西不在话下。前段时间听朋友吹官方的 Room 数据库管理组件，正好也借这机会熟悉熟悉。

（算了编不下去了，那就开始吧...）

## Room 介绍

看到这篇文章，说明你对 SQLite 有一定的了解并且在招寻一种更简单的方法来操作 SQLite。如果不知道 SQLite 也没关系，等你了解了你还会回来的，因为纯原生的 SQLite 操作是一件很费神且极容易出错的事，这也是 Android ORM 框架（可以简单理解为封装了底层数据库操作的框架）出现的原因之一——简化 SQLite 操作，提升开发效率。当然这也是 Room 出现的原因之一。

Android 在 2017 年 5 月份开始推出官方的架构组件，在 2018 年 I/O 大会上正式更名为 Jetpack，Room 正是其中的数据持久化方案。Room 在 SQLite 上层封装了一个对象映射的抽象层，通过对象的方法操作可以方便的 SQLite 而不需要去写一些繁琐易出错的操作语句。那么 Room 既然和一些三方 ORM 框架一样为 SQLite 操作提供了便利，那为什么我们选择 Room 而不是其他框架呢？总得来说有以下几点：

1. Room 更容易上手。由于 Room 是在 SQLite 上封装了一层抽象层，在操作时对 SQLite 的侵入性并不大，只要对 SQLite 的语句熟悉，通过对 Room 中注解的学习，可以很快的上手开发，而不需要去学习一些专有的 API。

2. Room 具有编译时校验的特性。Room 在编译时会对 SQLite 语句进行验证，一旦出现语法或者逻辑错误会在编译时提出，不会等到 APP 运行时才能发现奔溃了，这样一来大大提高了效率。

3. Room 支持数据库迁移。数据库迁移也算是一大令人头疼的问题了，当需要对数据库进行字段更新时，一般都需要手动将旧版数据库的内容迁移到新版数据库再删除旧数据库文件，而 Room 内置迁移功能，只需要几行代码就可以完成数据库的迭代。

4. Room 是官方推出的方案。当然这并不是说官方推出的就一定是最好的，但是自官方推出架构组件以来，这套开发模式逐渐成型，目的就是提高开发效率，提升 APP 质量。Room 与 Jetpack 中其他组件如 LiveData、Paging 等配合使用能最大程度体现官方架构方案的优势。

选择 Room 作为 ORM 方案，大家肯定会考虑性能问题。由于 APP 需要存储的数据量以及机器配置都不相同，性能的优劣并不能决定最好的方案。可以放心的是，官方团队并是单纯的重新造轮子，而是借鉴了一些流行库中比较好的做法优化 Room，性能方面不会是掣肘 Room 发展的原因。当然网上也有很多对各个 ORM 方案的性能对比，可以自行查阅，Room 的表现并不逊色于其他方案。

（说了这么多，终于决定用 Room 了，我们继续...）

Room 的主要功能是通过注解实现的，所以只要学会了 Room 的注解，再加上 SQLite 的技能点，上手开发不是问题。Room 中最主要的三部分对应到注解分别是`@Database`、`@Entity`、`@Dao`，这三部分代表了最基本的数据库增删改查操作：

* **Database**：使用 `@Database` 注解，相当于 SQLite 中的数据库文件，是连接数据的主要入口。

* **Entity**：使用 `@Entity` 注解，相当于 SQLite 中的数据表。

* **Dao**：使用 `@Dao` 注解，是 Room 提供的 data-access-object，在 Room 中我们通过 **Dao** 访问数据表并进行操作。

它们三者的关系可以用官网的一张架构图来表示：

![Room 架构图][room-architecture-diagram.png]

可以看到，在 App 中通过  **Database** 作为数据库入口获取该数据库中可操作的 **Dao**，再通过 **Dao** 以 **Entity** 的格式读写数据表进而操作数据库中的字段。有了这三部分，我们就可以通过 Room 实现对 SQLite 的基本操作了。

**注意事项**

> 截至本文完成时，Room 最新版本为 *2.1.0-alpha04*，本着就稳不就新的原则，生产环境中请使用稳定版本 *1.1.1*。从 *2.0.0-beta01* 开始，Room 依赖于 AndroidX 进行开发，也就是说，如果项目中仍然使用的是 Support 库，为了避免冲突，那还是老实用稳定版 *1.1.1*。当然，AndroidX 于 2018.9.21 发布了首个可用于生产环境的稳定版本，已经尝试了 AndroidX 的项目也没得选，最低 *2.0.0-beta01* 起步，不过可以大胆体验最新 alpha 版本的功能。基本可以看出， AndroidX 是大势所趋，后续稳定版 *2.** 都需要 AndroidX 支持。

## Room 使用

还记得开篇提的需求么，现在我们就用 Room 来实现基本功能。打开 Android Studio 创建名为 `RoomSample` 的项目，项目创建好了准备工作也就完成了。

> 由于目前 Room 的稳定版本是 *1.1.1*，所以没有特殊说明，本文的功能都是基于此版本的示例。

### 导入依赖

首先要做的当然是导入依赖，由于 Room 的包存放在 Google 的 Maven 仓库，所以要先导入 Google 的 Maven 仓库地址。打开项目中的 `gradle-wrapper.properties` 文件，最后一行 `distributionUrl=https\://services.gradle.org/distributions/gradle-4.10.1-all.zip` 即表示了当前项目所用的 Gradle 版本，如果 Gradle 版本低于 *4.1*，则需要在项目级的 `build.gradle` 文件（即 `build.gradle(Project: RoomSample)`）中做如下配置：

```Gradle
buildscript {
    ...

    repositories{
        jcenter()
        maven {
            url 'https://maven.google.com/'
            name 'Google'
            }
    }

    ...
}
```

如果 Gradle 版本是 *4.1* 及以上，在同样的地方进行配置，但是更为简单（强烈建议升级 Gradle 版本！本文也将以最新版 Gradle 为项目配置）：

```Gradle
buildscript {
    ...

    repositories{
        jcenter()
        google()
    }

    ...
}
```

配置好 Google Maven 仓库就可以开始导入 Room 的依赖的，在 Module 级别的 `build.gradle` 文件（即 `build.gradle(Module: app)`）中做如下配置：

```Gradle
...

dependencies {
    ...
    implementation 'android.arch.persistence.room:runtime:1.1.1'
    annotationProcessor 'android.arch.persistence.room:compiler:1.1.1'
}
```

### 定义数据表

一切准备就绪，我们来分析下需求。「列个物品清单，方便查找」，所以我们需要记录下物品的相关信息以及存放地点。需求明确了，按照 SQLite 的方式，同样需要设计数据表，这里我们需要两张表，一张表记录物品信息，另一张记录存放地点，并且以外链的形式将两张表进行关联以方便查询。那么在 Room 中具体该怎么实现呢？还记得 Room 中最重要的三部分么，**Entity** 代表了数据表，我们先使用 `@Entity` 注解来定义地点表 `PlaceEntity`：

```Java
@Entity
public class PlaceEntity {
    @PrimaryKey
    public long id;
    public String description;
}
```

使用 `@Entity` 注解的类定义了一张数据表中的字段结构，其中：

* 默认实体类的类名即为表名。

* 实体类的属性（Property）即为数据表中的字段，每个属性代表一个字段，默认属性名即为字段名。要求每个属性需要对外开放访问权限，即使用 `public` 修饰符进行修饰或者使用 `private` 修饰并创建相应的 getter/setter 方法。

* 与 SQLite 语句创建表不同的是，Room 每个 **Entity** 必须定义至少一个主键，使用 `@PrimaryKey` 进行注解，即示例中 `id` 为这张表的主键。

因此，上例等价于 SQLite 语句就是：

```SQLite
CREATE TABLE PlaceEntity(
    id INTEGER PRIMARY KEY,
    description TEXT);
```

再来个复杂点的，我们继续实现物品信息表 `products`：

```Java
@Entity(tableName = "products",
        foreignKeys = {@ForeignKey(entity = PlaceEntity.class,
                parentColumns = "id",
                childColumns = "place_id")},
        indices = {@Index(value = "place_id")})
public class ProductEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public double price;
    @ColumnInfo(name = "purchase_date")
    public Date purchaseDate;
    @ColumnInfo(name = "place_id")
    public long placeId;
}
```

在这次的定义中：

* 默认类名 `ProductEntity` 即为表名，我们使用 `@Entity` 的 `tableName` 属性自定义表名为 `products`。

* 使用 `@Entity` 的 `foreignKeys` 属性定义了外键，它的值是一个 `@ForeignKey` 数组，即可以定义多个外键。`entity` 指需要建立连接的另一个 **Entity**，我们可以称之为**父 Entity**；显然 `parentColumns` 指 **父 Entity** 中对应的字段，`childColumns` 指 **子 Entity** 即当前 **Entity** 中的字段，`parentColumns` 和 `childColumns` 都是数组，但是两者数组大小必须一致，对应字段的顺序也必须一致。

* 当然，Room 同样建议在设置了外键时将 `childColumns` 在**子 Entity** 中设置索引，使用 `@Entity` 的 `indices` 属性。默认索引名为 `index_tableName_columnName`，也可以通过 `@Index` 注解的 `name` 属性设置：

```Java
indices = {@Index(name = "index_place_id",
                value = "place_id", 
                unique = true /*设置是否为唯一索引，默认为 false*/)})
```

* SQLite 支持自增主键，Room 也不例外，将 `@PrimaryKey` 的 `autoGenerate` 属性设置为 `true` 则表示该主键为自增主键。

* 默认该类的属性属性名即为字段名，同样也可以使用 `@ColumnInfo` 注解指定 `name` 为字段名。

上例等价于 SQLite 语句：

```SQLite
CREATE TABLE products(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    price REAL NOT NULL,
    purchase_date INTEGER,
    place_id INTEGER NOT NULL,
    FOREIGN KEY(place_id) REFERENCES place(id));

CREATE INDEX index_products_place_id ON products(place_id);
```

如果你眼尖的话，应该发现了一个问题，`ProductEntity` 中的 `purchaseDate` 是 `Date` 类型，为什么说等价于 SQLite 语句中的 `purchase_date INTEGER` 呢？ `Date` 当然是不可能等价 `INTEGER` 的，这是 Room 中的 `TypeConverter` 在帮忙。`TypeConverter` 可以将包装类型转换成基本数据类型存入 SQLite，读取的时候再将其还原成包装类型。只要定义好转换规则，使用也很简单。先创建一个 Converter：

```Java
public class DateConverter {

    @TypeConverter
    public Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
```

使用的时候只需要添加 `@TypeConverters`（注意不是 `@TypeConverter`）添加到需要的 **Entity** 即可：

```Java
@Entity(tableName = "products",
        foreignKeys = {@ForeignKey(entity = PlaceEntity.class,
                parentColumns = "id",
                childColumns = "place_id")},
        indices = {@Index(value = "place_id")})
@TypeConverters({DateConverter.class})
public class ProductEntity {
    ...
}
```

有了 `TypeConverter` 的转换，`Date` 和 `INTEGER` 就等价了，后续使用 **Dao** 查询的时候可以将 `Date` 直接作为参数（见 **Dao** 的使用）。

以上基本上就是定义一张表的基本操作，但实际开发中并不会刚好实体类的每个属性都需要存入数据库，这时候可以使用 `@Ignore` 注解表示 Room 在执行逻辑时会忽略该属性，当然 `@Ignore` 注解不仅可以注解属性，还可以注解普通方法及构造方法。

```Java
@Entity(tableName = "products",
        foreignKeys = {@ForeignKey(entity = PlaceEntity.class,
                parentColumns = "id",
                childColumns = "place_id")},
        indices = {@Index(value = "place_id")})
public class ProductEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public double price;
    @ColumnInfo(name = "purchase_date")
    public Date purchaseDate;
    @ColumnInfo(name = "place_id")
    public long placeId;

    @Ignore
    public ProductEntity(String name, double price, Date purchaseDate, long placeId) {
        this.name = name;
        this.purchaseDate = purchaseDate;
        this.placeId = placeId;
        this.price = price;
    }
}
```

需要注意的是，如果直接用上例进行编译的话，你马上就能体会到 Room 编译时校验的厉害，AS 会中断编译并提示你：

```
Entities and Pojos must have a usable public constructor. You can have an empty constructor or a constructor whose parameters match the fields (by name and type).
```

由于使用 `@Ignore` 注解了实体类中唯一的构造方法，Room 在工作时忽略了这个方法，而 Room 要求 **Entity** 必须有一个可用的 `public constructor`，所以直接在编译时就报错。这个问题的解决方案就是加上一个构造方法：

```Java
...
public class ProductEntity {
    
    ...

    public ProductEntity() {
    }
}
```

编译时校验这一强大的功能能帮我们提前发现错误，避免因为排查细微的遗漏而浪费大量时间。

还有一个很实用的注解 `@Embedded` 可以辅助定义数据表，顾名思义，这个注解用来实现类似嵌套的功能。试想如果 `products` 和 `place` 不拆分成两张表而是作为一张表存储，但是数据来自 `ProductEntity` 和 `PlaceEntity` 两个实体类，这时可以新增一个实体类并借助 `@Embedded` 注解快速实现一个 `Entity`：

```Java
@Entity
public class ProductWithPlace {
    @Embedded
    public ProductEntity product;
    @Embedded
    public PlaceEntity place;
}
```

看起来就像是将 `ProductEntity` 和 `PlaceEntity` 作为 `ProductWithPlace` 的属性，使用时也像平常一样调用 `productWithPlace.product`，但是在 SQLite 端表现不一样，并不是往表里存了两个对象引用，而是将两个实体类的属性拆分组合后存入数据表。由于使用一张表存储，不需要建立外键，所以 `ProductEntity` 中的 `placeId` 属性也不需要了，最终得到的表含有以下字段：`id`，`name`，`price`，`purchase_date`，`id`，`description`。这时发现有两个 `id` 字段，这显然是行不通的，可以使用 `@Embedded` 的 `prefix` 属性增加前缀：

```Java
@Entity
public class ProductWithPlace {
    @Embedded(prefix = "product_")
    public ProductEntity product;
    @Embedded(prefix = "place_")
    public PlaceEntity place;
}
```

现在表中的字段变成了：`product_id`，`product_name`，`product_price`，`product_purchase_date`，`place_id`，`place_description`

### 定义数据操作

SQLite 的操作无非是增删改查，Room 提供了 Dao 层对数据操作进行封装，使用的是 `@Dao` 注解。`@Dao` 可以注解一个接口（Interface）或者抽象类，二者选一即可。Room 提供了多个注解完成这些操作。

#### @Insert

使用 `@Insert` 注解 **Dao** 中的方法可以将参数存入数据表中：

```Java
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertRecord(ProductEntity product);

    @Insert
    public long[] insertRecord(ProductEntity... products);
}
```

`@Insert` 使用 `onConflict` 属性支持发生冲突时的处理，其注解的方法可以是无法返回类型也可以根据参数返回对应类型——单个参数返回的 `long` 类型表示新插入数据的 `rowId`，多个参数则返回 `long[]` 或 `List<Long>`。

#### @Update

用法和 `@Insert` 一样，不同的是返回类型：

```Java
public interface ProductDao {
    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateRecord(ProductEntity product);

    @Update
    public int updateRecord(ProductEntity... products);
}
```

`@Update` 可以是无返回类型，也可以返回 `int` 表示成功更新的记录数。

#### @Delete

`@Delete` 注解的方法可以无返回类型，也可以返回 `int` 表示成功删除的记录数。

```Java
public interface ProductDao {
    @Delete
    public void deleteRecord(ProductEntity product);

    @Delete
    public int deleteRecord(ProductEntity... products);
}
```

`@Insert`、`@Update`、`@Delete` 这三个注解比较简但，正是如此，可以看到多个 **Dao** 会存在多个 `insert*`、`update*`、`delete*` 方法，不同的只是参数，这里可以用泛型和继承功能来优化：

```Java
public interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertRecord(T record);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public int updateRecord(T record);

    @Delete
    public int deleteRecord(T record);
}

@Dao
public interface ProductDao extends BaseDao<ProductEntity> {
    // 其他操作
}
```

这样一来，就可省略很多样板代码了，Room 会自动生成相应的 Dao 实现。

#### @Query

最基本的 Query 与 SQLite 一致，通过 SQLite 语句执行查询操作，但 Room 简化了查询返回的结果处理：

```Java
@Dao
public interface ProductDao extends BaseDao<ProductEntity> {
    @Query("select * from products")
    public List<ProductEntity> getProducts();
}
```

可以看到，直接调用 `productDao.getProducts()` 就可以直接拿到 Room 处理好的结果。根据条件查询也很简单：

```Java
@Dao
public interface ProductDao extends BaseDao<ProductEntity> {
    
    ...

    @Query("select * from products where id = :productId")
    public ProductEntity getProductById(int productId);

    // 还记得由 TypeConverter 转换的 Date 类型么？
    // 查询购买日期早于 date 的物品
    @Query("select * from products where purchase_date < :date")
    public List<ProductEntity> getProductBefore(Date date);
}
```

使用时就像调用普通的带参方法一样就能取到处理好的结果。有时候可能一张表字段比较多，但是只想取出一部分字段的值，Room 也支持按需查询，只需要创建对应的实体类接收数据就行：

```Java
// 简略版的实体类
@TypeConverters(DateConverter.class)
public class Product {
    public int id;
    @ColumnInfo(name = "name")
    public String productName;
    @ColumnInfo(name = "price")
    public double productPrice;
    @ColumnInfo(name = "purchase_date")
    public Date purchaseDate;
}

// 只查询需要的数据
@Dao
public interface ProductDao extends BaseDao<ProductEntity> {
    
    ...

    @Query("select id, name, price, purchase from products")
    public List<ProductEntity> getProductBefore();
    
    // JOIN 子句与 SQLite 的用法也是一样的
    @Query("select products.id, name, price, purchase_date, description from products join place on products.place_id = place.id")
    public List<ProductWithPlace> getProductsWithPlace();
}
```

以上，就是最基本的增删改查的用法了。再来一些更便捷的操作吧。

#### @Transaction

Transaction 与 SQLite 的定义也是一样的，Room 同样用最注解的方式实现 Transaction 的创建。Room 的每一次 `@Insert`、`@Update`、`@Delete` 操作都是一次 Transaction 的创建提交，我们也可以使用 `@Transaction` 注解将多个 `@Query` 操作放到一个 Transaction 提交：

```Java
// Dao 也可以以抽象类的方式实现
@Dao
public abstract ProductDao {
    @Transaction
    public void clearAndInsertProduct(ProductEntity product) {
        deleteAllProducts();
        insertProduct(product);
    }

    @Insert
    abstract void insertProduct(ProductEntity product);

    @Query("delete from products")
    abstract void deleteAllProducts();
}
```

默认情况下 `@Query` 操作没有放到 Transaction 中，但是如果 `@Query` 执行了 insert、update、delete 等操作时 Room 也会自动将其放到一个 Transaction 中。需要注意两种情况下需要使用 `@Transaction`：

1. 查询语句得到的结果过大。由于 Android 会将结果缓冲到一个 `CursorWindow` 中，而且 `CursorWindow` 有大小限制，一旦查询结果超过单个 `CursorWindow` 的限制，如果在交换 `CursorWindow` 时数据库发生更改，那么最终得到的数据也就不是我们想要的了，所以这种情况下将一次 `@Query` 操作放到 Transaction 中。

2. 如果查询结果的实体类使用了 `@Relation` 注解其中的属性，由于被 `@Relation` 注解的属性与其他属性在 Room 中是分开执行查询操作的，所以需要在单一的 Transaction 中取到一个整体结果。

#### @Relation

上面提到了 `@Relation`，那么这个注解又解决了什么问题呢？简单来讲，这个注解可以很方便的处理一对多关系的数据查询。

在上面的例子中，假设我们想知道指定的某个地点存放了哪些物品，通常需要根据该地点的 `id` 查询 `products` 表中对应的记录，如果需要列出每个地点存放物品的概览会更复杂：

```Java
@Query(select * from place)
public List<PlaceEntity> getPlaces();

@Query(select * from products where place_id = :placeId)
public List<ProductEntity> getProductsByPlace(long placeId);
```

我们需要先查询出所有可存放物品的地点，再进行遍历并根据地点 `id` 查询相应的物品，这操作看起来都觉得有点麻烦。`@Relation` 正好是解决这类问题的利器，处理一对多关系的数据时，它会根据这个「一」自动查询出对应的「多」：

```Java
public class ProductsForPlace {
    @Embedded
    public PlaceEntity place;
    @Relation(parentColumn = "id", entityColumn = "place_id")
    public List<ProductEntity> products;
}
```

现在查询只需要一个简单的方法就行了：

```Java
@Transaction // 记得加上 @Transaction 哦
@Query("select * from place")
public List<ProductsForPlace> getPlacesWithProducts();
```

`@Relation` 虽然好用，但是也有限制：

1. `@Relation` 只能用于 Pojo 中，不能在 **Entity** 中使用，但是可通过继承 **Entity** 在其 Pojo 子类中使用。

2. `@Relation` 只能用于注解 `List`、`Set` 集合对象。想想也是，`@Relation` 就是方便找出那个「多」的数据，自然要指向集合。

3. `@Relation` 注解的属性不能作为构造方法的参数，而且必须使用 `public` 修饰或者有 `public` 修饰的 setter 方法。

### 处理数据库操作

到此为止，已经准备好了 **Entity** 和 **Dao** 两部分，Room 的大门基本为你打开了，最后我们需要用 **Database** 来实现真正的持久化存储，将数据一条条存入 Android 设备。

使用 `@Database` 注解的同时，数据库类是一个抽象类，而且需要继承 `RoomDatabase`，并且将前面定义好的 **Entity** 和 **Dao** 关联到 **Database** 中：

```Java
@Database(entities = {ProductEntity.class, PlaceEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    public abstract ProductDao productDao();

    public abstract PlaceDao placeDao();

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context);
                }
            }
        }
        return sInstance;
    }

    private static AppDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "res.db")
                .build();
    }
}
```

首先是 **Entity**，通过 `@Database` 的 `entities` 属性设置。`entities` 是一个 Class 数组，需要将之前定义好的 **Entity** 添加进去，代表需要在 Database 中建立相应的数据表：`entities = {ProductEntity.class, PlaceEntity.class}`。

`version` 属性表示数据库的版本号，当后续数据库迭代时需要对应的更新版本号。

`@Database` 还有一个 Boolean 型的属性 `exportSchema`，表示是否导出数据库的架构，默认为 true。保持默认值的话还需要设置导出路径，打开 Module 级别的 `build.gradle` 文件（即 `build.gradle(Module: app)`）进行配置：

```Gradle
android {
    ...
    defaultConfig {
        ...
        // 设置数据库架构版本导出地址
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = ["room.schemaLocation": "$projectDir/schemas".toString()]
            }
        }
    }
}
```

设置完编译后会在 AS 左侧的 `Project` 视图转换为 Project，在 `RoomSample/app` 路径下会发现 `schemas` 文件夹，里面就是各个版本的数据库架构信息，以 Json 的形式保存：

```Json
{
  "formatVersion": 1,
  "database": {
    "version": 1,
    "identityHash": "ef78510192648c2b98e59914237103e1",
    "entities": [
      {
        "tableName": "products",
        "createSql": "CREATE TABLE IF NOT EXISTS `${TABLE_NAME}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `price` REAL NOT NULL, `purchase_date` INTEGER, `place_id` INTEGER NOT NULL, FOREIGN KEY(`place_id`) REFERENCES `place`(`id`) ON UPDATE NO ACTION ON DELETE NO ACTION )",
        "fields": [
          {
            "fieldPath": "id",
            "columnName": "id",
            "affinity": "INTEGER",
            "notNull": true
          },
          ...
        ],
        "primaryKey": {
          "columnNames": [
            "id"
          ],
          "autoGenerate": true
        },
        "indices": [
          {
            "name": "index_products_place_id",
            "unique": false,
            "columnNames": [
              "place_id"
            ],
            "createSql": "CREATE  INDEX `index_products_place_id` ON `${TABLE_NAME}` (`place_id`)"
          }
        ],
        "foreignKeys": [
          {
            "table": "place",
            "onDelete": "NO ACTION",
            "onUpdate": "NO ACTION",
            "columns": [
              "place_id"
            ],
            "referencedColumns": [
              "id"
            ]
          }
        ]
      },
      ...
    ],
    "setupQueries": [
      "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)",
      "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"ef78510192648c2b98e59914237103e1\")"
    ]
  }
}
```

可以看到，我们之前设置的数据库的信息都被保存下来了，这样的好处是可以看到数据库迭代历史，方便维护。当然，如果不需要的话可以设置 `exportSchema = false`：

```Java
@Database(entities = {ProductEntity.class, PlaceEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    ...
}
```

如果既不设置 `exportSchema = false` 又不在 `build.gradle` 文件中配置导出地址，编译时 Room 会给你报出警告信息。

配置好后，我们可以通过 `Room` 工具类获取到 `RoomDatabase` 实例：

```Java
Room.databaseBuilder(context, AppDatabase.class, "res.db")
                .build();
```

`Room` 工具类封装了实例化 `RoomDatabase` 的方法，`databaseBuilder()` 方法可以建立数据库，还有一个 `inMemoryDatabaseBuilder()` 方法可以建立临时数据库，即只存在于内存中，不会生成实际的数据库文件，两个方法唯一的区别是 `inMemoryDatabaseBuilder()` 没有第三个设置数据库文件名的参数：`"res.db"`。此外还可以通过 `RoomDatabase.Callback` 回调监听数据库的 `onCreate()` 和 `onOpen()` 事件：

```Java
Room.databaseBuilder(context, AppDatabase.class, "res.db")
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        // 当数据库及数据表创建完成时回调，只在第一次创建数据库是回调
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        // 当数据库 open 时回调
                    }
                })
                .build();
```

可以看到，`APPDatabase` 中还有几个关于 **Dao** 的抽象方法，没错只需要简单的添加对应 **Dao** 的抽象方法，Room 会自动生成对应的 **Dao** 实现。调用的时候只需要获取实例调用 **Dao** 的方法即可：

```Java
AppDatabase db = AppDatabase.getInstance(context);

db.productDao().insertRecord(product);

db.placeDao().insertRecord(place);
```

当然，这些操作都需要在异步进行，否则会阻塞 UI 线程的。

有了以上对 Room 的了解，再回过头来看「列个物品清单，方便查找」这个需求是不是就很简单了~如果你还想进一步知道 Room 为你做了什么，AS 左侧的 `Project` 窗口切换成 Android 视图，打开 `generatedJava` 文件夹，你会发现 Room 在里面帮你处理了各种本来需要手动处理的 SQLite 语句。

等等，好像忘了件事，如果经历一番辛苦完成了物品清单的记录，最后你媳妇儿希望记录更多信息，意味着数据表需要改动了。当然不可能让她把之前的数据都清掉等你完善了再添加一遍，我们来看看 Room 是怎样完成数据库迭代的吧。

### Room 的迭代及数据迁移

Room 提供了 `Migration` 类帮助我们顺利的完成数据库的迭代及数据迁移，操作也很简单，在 `AppDatabase` 类中将版本号增加并新建 `Migration` 变量：

```Java
@Database(entities = {ProductEntity.class, PlaceEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    ...

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 从版本 1 升级到版本 2 数据库需要进行的操作
        }
    };
}
```

在获取 `RoomDatabase` 实例时进行设置：

```Java
Room.databaseBuilder(context, AppDatabase.class, "res.db")
                .addMigrations(MIGRATION_1_2)
                .build();
```

基本设置就是如此了，当新版的 APP 打开时，Room 会调用内部实现的 `SQLiteOpenHelper.onUpgrade` 方法并执行 `Migration` 操作进行数据库迭代。迭代迁移操作必须保证同时新增版本号、提供 `Migration`，二者缺一都会导致 APP 奔溃。

举个例子来看一下迭代操作，比如希望新增物品的分类，那么需要在 `products` 表中新增 `cat` 字段，将数据库版本升级为2：

```Java
@Database(entities = {ProductEntity.class, PlaceEntity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    ...

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.exeSQL("ALTER TABLE products ADD COLUMN cat TEXT");
        }
    };

    ...
    Room.databaseBuilder(context, AppDatabase.class, "res.db")
                .addMigrations(MIGRATION_1_2)
                .build();
    ...
}
```

说白了其实就是将 SQLite 语句写到了 `Migration` 中，Room 在打开数据库之前先执行这些升级迁移的语句。如果是多个版本迭代的话，比如从版本 1 升级到版本 4，那么需要提供每个版本之间的 `Migration`，并且都添加到 `databaseBuilder` 中：

```Java
static final Migration MIGRATION_1_2 = ...

static final Migration MIGRATION_2_3 = ...

static final Migration MIGRATION_3_4 = ...

    ...
    Room.databaseBuilder(context, AppDatabase.class, "res.db")
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                .build();
    ...
```

需要注意的是 `Migration.migrate` 中的操作 Room 不会在编译时进行校验，所以需要确保迭代的 SQLite 语句的正确性。

## 总结

以上就是 Room *1.1.1* 版本的主要内容了，可以看到，只要会 SQLite，学习 Room 根本不需要太多时间，当然 Room 也在不断更新，想要使用更丰富的功能还是需要不断的学习，在最新的 *alpha* 版本中，Room 还新增了对 `View`、`FTS3`、`FTS4` 等的支持，并且完善了 Room 与 RXJava 的结合使用，感兴趣的话自行前往学习哟~

以上，希望本篇上手指南能帮助到你~