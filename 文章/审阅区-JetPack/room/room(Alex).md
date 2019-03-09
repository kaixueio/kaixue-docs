**引言**

Jetpack的数据库框架Room可以简单的理解为Retrofit的数据库版本, 利用注解对Android的原生数据库操作进行抽象, 简化了CRUD (Create, Retrieve,Update, Delete)操作的复杂性.

在Room之前, 已经有包括GreenDao, ORMLite, XUtils等等数据库操作框架出现并赢得了广大用户的好评, 毕竟android原生的数据库操作要写的重复代码很多,生产效率低且易出错.以上框架大同小异, 通过链式方法(或者称之为"操作符")调用来实现SQL的各种操作.相比原生好一些,但由于对SQL进行了框架级的封装(或者称之为"映射"),就导致了三个问题
1. 框架的这些"操作符"是否完全覆盖了SQL语句操作?
2. 开发者对这些没有统一规范的"操作符"的学习成本
3. 越是复杂的SQL语句,其链式"操作符"调用越是复杂
在实际开发中,一些复杂的调用,最后要么写了冗长不好维护的链式方法调用,要么直接简单粗暴使用框架预留的SQLiteOpenHelper(我接触的框架都返回了SQLiteOpenHelper,这是作者的兜底方案)通过SQL语句实现了.

另外,随着响应时编程(RxJava等)的兴起,越来越多的开发者尝到了这项技术的甜头,纷纷黑转粉并提升了工作效率.然而以上几个框架对响应式的支持参差不齐.

**说了这么多,是时候让我们今天的主角----Room上场了.**

有痛点就有需求,Room独辟蹊径并更进一步,利用注解的方式将SQL语句封装到数据库访问对象(data access objects, or DAOs)中,将数据库操作的复杂度和学习成本又降低了一个层次:

![](https://oscimg.oschina.net/oscnet/f652884451e27964d2106cc589f9238b704.jpg)

对开发而言,一个DAO只是集成了SQL信息的接口类,举个例子:
数据对象
```
@Entity(
    tableName = "cargoRecord"
)
data class CargoRecord(
    @ColumnInfo(name = "name") var name: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}
```
DAO对象:
```
@Dao
interface CargoRecordDao {
	@Query("SELECT * FROM cargoRecord ORDER BY orderTime desc")
 	fun getAllCargoRecords(): LiveData<List<CargoRecord>>
}
```
使用
```
AppDatabase.INSTANCE.cargoRecordDao().getAllCargoRecords()
```
基本上没有学习成本,只要有Java知识和SQL知识就够了,再也不需要去学习各种数据库框架的各种"操作符",或者使用SQLiteOpenHelper操作数据库了.

**ROOM的优势**

ROOM的优势有如下几点
1. 抽象DAO接口,封装Android原生的SQL操作.避免让人反感的样板代码(boilerplate code);避免其他数据库框架中需要学习和使用的各种操作符

2. 数据库操作问题更早发现和解决.我们知道,软件文件的解决成本和发现时间是成正相关的(注意不是正比),早发现问题和解决会极大提高生产力:

	- Room为开发者保留了最原始的SQL语句,对于负责的SQL操作,开发者可以在Sqlite编辑器中执行通过后,稍作修改就可以放到注解中执行,避免来回修改测试
	- Room在编译期会进行SQL语句正确性检查,同样地也降低了测试成本

3. 官方支持LiveData和RxJava, 监听数据库数据变化, 实现数据自动更新
4. 线程约束, 防止主线程的数据库操作导致卡顿(可以关闭,但是不建议)

**ROOM的使用**

0. 基于ROOM的小项目

本章计划使用一个简单的发货App来进行ROOM使用的讲解,数据结构的设计图如下

![](https://oscimg.oschina.net/oscnet/5a2c9f6cca3e0b0de0f03ba2e925615d9b9.jpg)

CargoRecord表是发货记录,包括记录id,发货人信息,货物信息,订单时间,金额和备注等信息

Cargo表和Persion表都是两个字段:id和name.两个表的id都是CargoRecord的[外键](http://www.w3school.com.cn/sql/sql_foreignkey.asp "外键")主要为了防止破坏表之间连接的动作以及非法数据插入外键列，因为外键必须是它指向的那个表中的值之一。

1. ROOM的集成

在主Project的build.gradle中增加版本号
```
buildscript {
    ext {
        room_version = '2.1.0-alpha04'
	........
    }
	........
}
```
在需要Room的App中增加依赖
```
dependencies {
    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
}
```

ROOM最新版本参考https://developer.android.com/jetpack/androidx/releases/room

2. ROOM的数据库操作
- 2.1 RoomDatabase初始化
使用ROOM需要继承RoomDatabase来实现一个数据库连接对象,后面所有的DAO操作都基于这个对象来获取DAO.需要在这个类中以注解的形式给出当前数据库的所有表,数据库版本:
```
@Database(
    entities = [//声明需要建表的实体类
        CargoRecord::class,
        Cargo::class,
        Cushion::class,
        Manufacture::class,
        Person::class],
    version = 1,//数据库版本
    exportSchema = false //是否导出建表用到的SQL语句(schema,后面介绍)
)
```
对于Android App的数据库访问,大多数情况下我们只要使用一个数据库连接就行了,所以这个RoomDatabase可以做成一个单例,并进行懒加载初始化即可:
```
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private const val TAG = "AppDatabase"
        val INSTANCE: AppDatabase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            buildDatabase(App.context)
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)//配置升级代码,详见下文
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        Log.d(TAG, "onCreate")//数据库文件创建的时候调用
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        Log.d(TAG, "onOpen")//数据库文件每次打开的时候调用
                    }
                })
                .build()
        }
    }
}
```
往AppDatabase中获取DAO对象:
```
    abstract fun cargoRecordDao(): CargoRecordDao
    abstract fun cargoDao(): CargoDao
    abstract fun personDao(): PersonDao
```
- 2.2 表定义

和其他数据库操作框架一样,Room的表定义也是在Entity中通过注解实现,代码如下

```
@Entity(
    tableName = "cargoRecord",//表名
    foreignKeys = [//外键
        ForeignKey(
            entity = Person::class,//外键所在的表对应的实体类
            parentColumns = ["id"],//外键在关联表中的字段,本例对应persion表的id字段
            childColumns = ["deliverPersonId"]//外键在本表中的字段
        ),
        ForeignKey
            entity = Cargo::class,
            parentColumns = ["id"],
            childColumns = ["cargoId"]
        )],
    indices = [//[指定索引](https://www.w3cschool.cn/sql/cuj91oz2.html "索引")
        Index("deliverPersonId"),
        Index("cargoId")
    ]
)
data class CargoRecord(
    @ColumnInfo(name = "deliverPersonId") var deliverPersonId: Int,//定义字段
    @ColumnInfo(name = "cargoId") var cargoId: Int,
    @ColumnInfo(name = "orderTime") var orderTime: Date,
    @ColumnInfo(name = "deliverTime") var deliverTime: Date,
    @ColumnInfo(name = "money") var money: Double,
    @ColumnInfo(name = "note") var note: String
) {
    @PrimaryKey(autoGenerate = true)/自增ID
    @ColumnInfo(name = "id")
    var id: Long = 0
}
```

- 2.2 插入操作

分为插入单个实体和多个实体两种方式, 注意每次数据库操作都是一次[事务](https://www.w3cschool.cn/mysql_developer/mysql_developer-i9km2j87.html "事务")
```
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(cargoRecord: CargoRecord)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(cargoRecords: List<CargoRecord>)

```
其中OnConflictStrategy标志了在插入值时遇到重复id值该如何操作:
|值   | 作用  |
| ------------ | ------------ |
| REPLACE  | 更新旧数据 |
| ROLLBACK  | 废弃,请使用ABORT |
| ABORT  | 放弃这次数据库事务操作  |
| FAIL  |   废弃,请使用ABORT |
| IGNORE  |  跳过这个重复值 |

- 2.3 查询操作

最简单的查询
```
    @Query("SELECT * FROM cargoRecord")
    fun getAllCargoRecords():List<CargoRecord>
```
按照降序查询
```
    @Query("SELECT * FROM cargoRecord ORDER BY orderTime desc")
    fun getAllCargoRecords():List<CargoRecord>
```
条件查询
```
   @Query("SELECT * FROM cargoRecord WHERE orderTime=:orderTime")
    fun getCargoRecordByOrderTime(orderTime: Date): List<CargoRecord>
```
多表查询

在实际软件开发中,为了降低数据库的冗余,提高数据库的维护性,有时候我们会对数据库进行分表操作. 一条数据可能从多个表中获取,这种需求对于Room来说也是小菜一碟.
先定义一个多表查询的数据类:
```
data class CargoRecordFull(
    @ColumnInfo(name = "personName") var personName: Int,//persoName在persion表
    @ColumnInfo(name = "orderTime") var orderTime: Date,//订单时间在cargo_record表,下同
    @ColumnInfo(name = "deliverTime") var deliverTime: Date,
    @ColumnInfo(name = "money") var money: Double,
    @ColumnInfo(name = "note") var note: String
) {
    override fun toString(): String {
        return "CargoRecordFull(personName=$personName, orderTime=$orderTime, deliverTime=$deliverTime, money=$money, note='$note)"
    }
}
```
多表查询
```
    @Query("SELECT cargoRecord.*, person.name AS personName FROM cargoRecord,person WHERE cargoRecord.deliverPersonId = person.id AND orderTime >= :orderTime ORDER BY orderTime desc")
    fun getCargoRecordSince(orderTime: Date): List<CargoRecordFull>
```

可以看到,由于嵌入了SQL语句,所以各种操作是非常灵活和方便的.

- 2.4 更新操作
更新数据库中与给定对象ID相同的条目
```
@Update
fun updateRecord(CargoRecord: CargoRecord): Int
```
使用SQL更新数据
```
 @Query("UPDATE cargoRecord SET note = :newNote WHERE orderTime = :orderTime")
 fun updateNoteByRecordTime(newNote: String, orderTime: Date): Int
```
- 2.5 删除操作

删除数据库中与给定对象ID相同的条目
```
@Delete
fun deleteByRecord(record: CargoRecord): Int
```
删除含有指定值的数据([LIKE操作符](http://www.runoob.com/sqlite/sqlite-like-clause.html "LIKE操作符"))
```
@Query("DELETE FROM cargoRecord where note like :note")
fun deleteByNote(note: String): Int
```
删除表中所有数据
```
@Query("DELETE FROM cargoRecord")
fun deleteAll(): Int
```
- 2.6 数据库的升级
ROOM下的数据库升级也很简单,只需要在创建RoomDatabase对象的时候增加配置代码
```
.addMigrations(MIGRATION_1_2, MIGRATION_2_3)
```
代码配置如下:
```
val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        Log.d("Migration", "Migration 1->2")
        database.execSQL("ALTER TABLE cargo ADD COLUMN costPrice REAL")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        Log.d("Migration", "Migration 2->3")
    }
}
```
在调用代码创建RoomDatabase对象的时候.如果/data/data/{package}/database中的旧数据库文件版本为1,当前RoomDatabase注解中的数据库版本为2,则就回调到MIGRATION_1_2的migrate回调;同理如果旧数据库版本为2,当前版本为3就回调MIGRATION_2_3中的migrate.如果旧版本为1, 当前版本为3,则这两个回调都会调用.

3. ROOM和响应式编程
Room原生支持两种响应时编程方式
- [Jetpack的LiveData](https://developer.android.com/topic/libraries/architecture/livedata "Jetpack的LiveData")

- [RxJava2的Flowable,Single和Maybe](https://medium.com/androiddevelopers/room-rxjava-acb0cd4f3757 "RxJava2的Flowable,Single和Maybe")

对于LiveData,主要的应用还是查询,如下面的查询
```
    @Query("SELECT * FROM cargoRecord ORDER BY orderTime desc")
    fun getAllCargoRecords(): LiveData<List<CargoRecord>>
```
使用:
```
data.getCargoRecords(Date(1550373836000)).observe(viewLifecycleOwner, Observer { result ->
     // TODO("show data from database in UI")
})
```
这样的好处是,在其他地方更新了数据库中的CargoRecord后,Observer会回调,并将新的result传给UI,以便刷新,此种场景下,可以和EventBus或广播 Say GoodBye了.

注意:我们看到,在observe的时候传入了viewLifecycleOwner,所以实际上在**数据发生变化**和**订阅所在页面的onResume事件**都到来的时候才会真正进行数据的更新回调.如果数据更改和展示在两个页面,则多次的数据更改其实不会立刻引起展示页面的刷新,只有真的返回到数据页面,才会进行刷新,拿到的是最新的数据库结果.

对于喜爱RxJava2的同学,Room也做了支持,具体用法和普通RxJava差不多:
```
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(cargoRecord: CargoRecord): Maybe<Long>

    @Query("SELECT * FROM cargoRecord ORDER BY orderTime desc")
    fun getAllCargoRecords(): Flowable<List<CargoRecord>>

    @Update
    fun updateRecord(CargoRecord: CargoRecord): Maybe<Integer>

    @Query("DELETE FROM cargoRecord")
    fun deleteAll(): Completable
```

4. 实际使用过程中遇到的一些问题
在创建数据库的时候用到了exportSchema字段,表示是否导出RoomDatabase在创建表的时候用到的SQL语句,这个值默认ture,但是如果要真正拿到schema文件,还需要在当前项目build.gradle的android{}下增加room.schemaLocation:

Kotlin:

```
 kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas".toString())
            }
        }
```
Java
```
 javaCompileOptions {
            annotationProcessorOptions {
                arguments = ["room.schemaLocation": "$projectDir/schemas".toString()]
            }
        }
```

**Demo仓库地址**

https://github.com/sfshine/CargoRecord

**参考**

	http://kevinwu.cn/p/717ed5d8/#Update
	https://codelabs.developers.google.com/codelabs/android-persistence/#2
	https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#2
	https://developer.android.com/training/data-storage/room/accessing-data
	https://blog.csdn.net/Alexwll/article/details/83033460
	https://developer.android.com/jetpack/androidx/releases/room
	http://www.w3school.com.cn/sql/sql_update.asp
	https://medium.com/androiddevelopers/room-rxjava-acb0cd4f3757
