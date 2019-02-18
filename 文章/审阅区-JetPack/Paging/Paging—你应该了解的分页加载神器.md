# Paging—你应该了解的分页加载神器

> 自从去年 (2018 年) 谷歌爸爸推出 JetPack 后，在 Android 圈掀起欣然大波，各类技术文章与开源框架层出不穷，而作为 Jetpack 中最接近业务场景的一员（没错，就是我自己认为的），Paging 也深受关注。那么今天，我们来初步了解一下，Paging到底是何方神圣，它可以干什么，怎么去使用它。
>



## 一则小故事

小明的公司研发了一款购物 App “天狗”，界面非常好看，内容也很不错，作为 Android 端负责人的小明非常开心，因为奖金这个月跑不了啦。但是有一天老板找了小明谈话： 

老板：小明啊，Android 端这次做的非常好，功能实现的都很不错。

小明：谢谢老板，这都是我应该做的 (小明窃喜中 )。

老板：但是呢，在列表这里我感觉可以再优化优化，你看啊，在其他 App 上他们的列表为什么一直上拉加载一点都不觉得卡顿，完全没有停顿的感觉，但是我们的 App 上拉几条就要有个 “加载中” 的提示，可不可以把这个提示去掉，做跟他们一样的效果啊？

小明：这个是上拉时候的加载提示，是分页用的，没有这个提示，给用户的感觉就会有卡顿的感觉的。

老板：我不管，这个别人可以做，我们也可以的。 

小明：这....

苦恼的小明于是只能求助于谷歌爸爸，在搜索栏输入关键字：**Android 列表 分页加载 顺滑 ** 然后回车 

而谷歌爸爸给了一页的 **Paging** 。。。。(嗯，内容纯属虚构，搜索结果也是虚构的 ٩(๑❛ᴗ❛๑)۶) 



## Paging是何方神圣？

接触一个新事物，我们总是想先直观的看到它到底是什么样子，那么废话不多说，二营长，上效果图！

![Paging](https://github.com/CoolCsf/pic/blob/master/Paging.gif)

要说 ```Paging``` 是什么东东，首先就要说到 App 分页加载的设计上，一般来说，App 的分页加载有两种情况：

- 当你滑动到底部时，才会触发分页加载的逻辑，展示一个``` footer```  (就是一般我们说的"正在加载中"的提示)，然后从数据源 (网络/ 数据库)获取下一页的数据加载到列表中
- 当你滑动到一定的条数时，就会触发分页加载的逻辑，而不需要等到底部才进行分页加载的逻辑，这样可以使得整个分页加载让用户无感知，就像是直接加载全部数据一样，体验更加的如丝~顺滑

而 ```Paging``` 就是基于第二种方案封装的一组神器，通过 UI 与数据的解耦，让你更方便的实现你那如丝般的分页加载。

那么为什么说 ```Paging``` 是一组呢？因为它不单单只有一部分... （呸，废话！） 它可以分成两部分，分别是 UI 层和数据层，接下来，我们就分别通过 UI 层和数据层，以及他们的组合对 ```Paging``` 进行用法解剖，让它不再神秘！



### Paging 基本组成

`` Paging ``  在设计之初，就采用了很好的设计，隔离了数据层和 `` UI`` 层，并通过中间层进行连接，达到数据和`` UI`` 统一的效果，具体的基本骨架如下图（来自灵魂画手的我(:≡））：

![1550214159543](https://github.com/CoolCsf/pic/blob/master/Pgaing-flow.png)

这张图很好的表达了`` Paging`` 的基本架构，也是本文讲述 `` Paging``  的基本架构，本文也通过逐层通过对数据层，UI 层以及组合层进行讲解，达到对 ``Paging`` 的基本剖析。



## 使用 Paging 

#### 添加依赖

在使用的 ``Module`` 中添加依赖：

```groovy
implementation "android.arch.paging:runtime:2.1.0"
```



### 数据层

> 要展示一个列表必须要有数据，这个数据一般我们是从网络服务器中获取得到或者从本地数据库获取然后再进行分页的逻辑。在 Paging 中，获取数据被独立封装起来，这就是我们今天数据部分的主角— ```DataSource ``` 。良心的谷歌爸爸帮我们考虑了集中获取数据的场景，并拓展了```DataSource``` ，让开发者自由地根据不同场景选择不同的 ```DataSource```，他们分别是 ``PageKeyedDataSource`` 、``ItemKeyedDataSource``、``PositionalDataSource`` ，接下来，我们就分别来分析这三种不同的 ``DataSource`` 的用法。
>



#### PageKeyDataSource

官方介绍：

```English
Incremental data loader for page-keyed content, where requests return keys for next/previous pages.

Implement a DataSource using PageKeyedDataSource if you need to use data from page N - 1 to load page N. This is common, for example, in network APIs that include a next/previous link or key with each page load.
```



对官方介绍的不正经翻译：***如果你加载数据源的时候，需要根据上下页的键值作为key去请求的话，那么，``PageKeyedDataSource``  你值得拥有。***



> 举个栗子：***你下一页要请求的参数中，需要用当前页数去请求，那么，``` PageKeyedDataSource ``` 将完美适用这个场景。***
>



道理说再多也不如 See the fuck code ：

```kotlin
class ArticlePageKeyDataSource : PageKeyedDataSource<Int, Article>() {
    private val initPage = 1
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Article>) {
        //调用时机：当列表第一次获取数据时调用
        // 参数讲解：
        // params: params封装了requestedLoadSize和placeholdersEnabled，这两个参数都是从PagedList.Config中设置的，后面会提到，这里用不到，不做赘述
        // callback：callback是UI数据更新的一个回调，当我们成功获取到数据之后，需要调用callback来通知UI更新
        getFeed(initPage, params.requestedLoadSize).subscribe({
            //参数讲解：
            //it：更新UI的List，类型必须是List<Article>
            //第二个参数： 调用loadBefore时所需要的key,在这里我们不需要加载上一页，所以给的是null
            //第三个参数： 调用loadAfter时所需要的key，在这里我们需要加载下一页的参数，而我们需要的是加载的页数，那么就是初始页面+1
            callback.onResult(it, null, initPage + 1)
        }, {
            Log.e("loadInitial", it.message)
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        //调用时机：触发加载下一页的时候调用
        // 参数讲解：
        // params: 可以从中获取到由loadInitial或上一次loadAfter所传递的key
        // callback：callback是UI数据更新的一个回调，当我们成功获取到数据之后，需要调用callback来通知UI更新
        getFeed(initPage, params.requestedLoadSize).subscribe({
             //参数讲解：
            //it：更新UI的List，类型必须是List<Article>
            //第二个参数： 调用下一个loadAfter时所需要的key，这里用上个页面传下来的页数+1，这样就可以保证页数的连续增加了。
            callback.onResult(it, initPage + 1)
        }, {
            Log.e("loadAfter", it.message)
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        // 调用时机：触发加载上一页的时候调用
        // 调用方式与 loadAfter 一致，当前不需要，可忽略。
    }

    private fun getFeed(pageIndex: Int, loadSize: Int): Observable<List<Article>> {
        return AppController.restApi.fetchFeed(
            AppController.query, AppController.apiKey, pageIndex.toLong(), loadSize
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.articles }
    }
}
```



#### ItemKeyDataSource

官方介绍：

```English
Incremental data loader for paging keyed content, where loaded content uses previously loaded items as input to future loads.

Implement a DataSource using ItemKeyedDataSource if you need to use data from item N - 1 to load item N. This is common, for example, in sorted database queries where attributes of the item such just before the next query define how to execute it.
```



对官方介绍的不正经翻译：***如果你加载数据源的时候，需要根据之前页面最后一个 item 的 key 作为参数的话，那么 ``` ItemKeyDataSource ``` 是个不错的选择***



> 举个栗子：***你下一页要请求的参数中，需要用到之前 item 的 id 作为请求参数***



see the fuck code :

```kotlin

class ArticleItemKeyDataSource : ItemKeyedDataSource<Int, Article>() {
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Article>) {
        //调用时机：当列表第一次获取数据时调用
        // 可以从param 中获取到设置的initialKey
        // callback：callback是UI数据更新的一个回调，当我们成功获取到数据之后，需要调用callback来通知UI更新
        // 这里由于场景问题不需要用到key，你可以根据自己的场景进行使用
        Log.d("loadBefore", "id:${params.requestedInitialKey}")
        getFeed().subscribe({
            callback.onResult(it)
        }, {
            Log.e("loadInitial", it.message)
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Article>) {
        // 调用时机：触发加载下一页的时候调用
        // 参数讲解：
        // params: 可以从中获取到由loadInitial或上一次loadAfter所传递的key
        // callback：callback是UI数据更新的一个回调，当我们成功获取到数据之后，需要调用callback来通知UI更新
        // 这里由于场景问题不需要用到key，你可以根据自己的场景进行使用
        Log.d("loadBefore", "id:${params.key}")
        getFeed().subscribe({
            callback.onResult(it)
        }, {
            Log.e("loadInitial", it.message)
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Article>) {
        // 调用时机：触发加载上一页的时候调用
        // 调用方式与 loadAfter 一致，当前不需要，可忽略。
    }

    override fun getKey(item: Article): Int {
        // 调用时机，当有触发 load 的时候，
        // 比如 loadInitial 或者 loadAfter 或者 loadBefore 的时候，所需要的 key 的时候都会调用此方法。
        return item.id.toInt()
    }

    private fun getFeed(): Observable<List<Article>> {
        return AppController.restApi.fetchArticle(
            AppController.query, AppController.apiKey
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.articles }
    }
}
```



#### PositionalDataSource

官方介绍：

```English
Position-based data loader for a fixed-size, countable data set, supporting fixed-size loads at arbitrary page positions.

Extend PositionalDataSource if you can load pages of a requested size at arbitrary positions, and provide a fixed item count. If your data source can't support loading arbitrary requested page sizes (e.g. when network page size constraints are only known at runtime), use either PageKeyedDataSource or ItemKeyedDataSource instead.

Note that unless placeholders are disabled PositionalDataSource requires counting the size of the data set. This allows pages to be tiled in at arbitrary, non-contiguous locations based upon what the user observes in a PagedList. If placeholders are disabled, initialize with the two parameter onResult(List, int).
```



对官方介绍的不正经翻译：***可从任意位置进行加载插入，可以在任意位置加载请求大小的页面，需要提供固定的项目计数，但是如果你的数据源不支持加载任意请求的页面大小（例如，仅在运行时知道网络页面大小约束时），则请用 ``PageKeyedDataSource`` 或``ItemKeyedDataSource``***



> 由于笔者较少使用 ``PositionalDataSource``，因此这里引用官方文档的示例（代码为 ``Java``），为了统一文章的代码，这里改成 ``kotlin``：



See the fuck code ：

```kotlin
class ItemDataSource : PositionalDataSource<Source>() {
    private fun computeCount(): Int {
        // 计算数量
    }

    private fun loadRangeInternal(startPosition: Int, loadCount: Int): List<Source> {
        // 获取 item 的代码
    }

    override fun loadInitial(
        params: PositionalDataSource.LoadInitialParams,
        callback: PositionalDataSource.LoadInitialCallback<Source>
    ) {
        val totalCount = computeCount()
        val position = PositionalDataSource.computeInitialLoadPosition(params, totalCount)
        val loadSize = PositionalDataSource.computeInitialLoadSize(params, position, totalCount)
        callback.onResult(loadRangeInternal(position, loadSize), position, totalCount)
    }

    override fun loadRange(
        params: PositionalDataSource.LoadRangeParams,
        callback: PositionalDataSource.LoadRangeCallback<Source>
    ) {
        callback.onResult(loadRangeInternal(params.startPosition, params.loadSize))
    }
}
```



### UI 层

> UI 层主要是负责展示 Data 层所提供的数据，这其中包括了列表属性的配置（页面个数，是否加载 PlaceHolders，初始加载的数量等等），还有页面展示与 RecyclerView 绑定的 Adapter 。



#### PagedList.Config

`` PagedList.Config `` 用于配置列表的属性，比如我们常用的有加载的个数，是否加载 PlaceHolders，初始加载的数量等等，具体配置代码如下：

```Kotlin
val pagedListConfig = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)                         //配置分页加载的数量
            .setEnablePlaceholders(ENABLE_PLACEHOLDERS)     //配置是否启动PlaceHolders
            .setInitialLoadSizeHint(PAGE_SIZE)              //初始化加载的数量
			.build()
```



#### PagedListAdapter

``PagedListAdapter`` 也是 `` RecyclerView`` 可以使用的一个 ``Adapter`` 与我们常用的 `` RecyclerView.Adapter`` 类似，``PagedListAdapter`` 也提供 ``onCreateViewHolder`` 和 ``onBindViewHolder`` 方法，使用方法并没有什么改变，只是增加了一个Diff 机制，会自动判断数据是否已经加载过了，如果已经加载过了，则不会重复增加。

代码如下 ：

```kotlin
class QuickPagingAdapter : PagedListAdapter<Article, BaseViewHolder>(diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
    	// 创建 viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        // 进行 viewHolder 的相关操作
    }
    companion object {
        private val diffCallBack = object :
            DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                // 判断之前的加载的对象id是否存在
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                // 判断之前的加载的对象是否存在
                return oldItem == newItem
            }

        }
    }
    inner class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {}
}

```



### UI 层与数据层的组合

> 当我们已经完成了 UI层 和 数据层，接下来，我们就要将他们串联起来，进行整合，让它们不再孤立。



#### Data.Factory

``DataFactory`` 是谷歌爸爸封装的一个可以提供 ``DataSource ``  的工厂类，在这个`` Factory ``里，你可以返回所有`` DataSource ``  的子类，使用如下：

``` kotlin
class SourceFactory<Key, Source> : DataSource.Factory<Key, Source>() {
        override fun create(): DataSource<Key, Source> {
            // 在这里可以返回以上数据层所有的 DataSource，比如 PageKeyedDataSource,ItemKeyedDataSource,PositionalDataSource
            // 需要注意的是：PositionalDataSource 只需要一个泛型 T，即对应着Source，而返回值需要两个泛型，则Key为Integer
            
        }

    }
```



#### 单数据源

单数据源可分为两种

- 只有网络请求，一般在``DataSource``  通过 `` Retrofit`` 或者 直接 ``OkHttp``  请求数据后，将数据通过`` CallBack`` 回调给 ``Paging`` ，上面所举的所有`` DataSource`` 例子均是以网络请求作为例子。

- 只有数据库，``Paging`` 推荐的数据库为官方的 ``Room`` 数据库，`` Room``  会很方便的生成一个`` DataSource.Factory`` 可以很完美的和`` Paging `` 兼容，不需要重新写 `` DataSource`` 和 `` DataSource.Factory``  例如一个获取学生数据的列子：

  ```kotlin
  @Dao
  interface StudentDao {
      @Query("SELECT * FROM Student ORDER BY name COLLATE NOCASE ASC")
      fun getAllStudent(): DataSource.Factory<Int, Student>
  }
  ```
  > 更多例子可以参考我们``Room`` 的相关教程(此处应有 Room 教程的连接)



#### 多数据源 

有时候我们会遇到这样的需求：

> 一开始展示列表的时候，从数据库中获取缓存数据，如果拿不到数据的话， 就从网络中获取，然后更新数据库。如果不用 ``Paging`` 的话，一般我们的做法，都是先获取数据库，然后自行判断是否获取得到，然后如果数据为空，则回去数据库，然后自行更新数据库缓存，但是使用了 ``Paging`` ，这部分逻辑 ``Paging`` 提供了一个回调，让你可以很方便的实现，并将他封装起来，跟业务隔离开来。

在 ``Paging`` 中，帮助我们实现这个需求的是一个叫 `` BoundaryCallback`` 的东西：

```kotlin
 class MBoundaryCallBack(
 ) : PagedList.BoundaryCallback<Concert>() {
     override fun onZeroItemsLoaded() {
         // 回调时机：当从本地数据库查询到的数据为0时;
         // 这里执行网络数据的获取和插入数据库的操作
     }
 
     override fun onItemAtEndLoaded(itemAtEnd: Concert) {
         // 回调时机：当从本地数据库查询到的数据为最后一条时;
         // 这里执行网络数据的获取和插入数据库的操作
     }
 }
```

这样我们就创建了一个回调帮助我们实现了上面的场景。而如何将这个 ``BoundaryCallBack`` 接入到我们的的流程当中呢？根据上文我们说到，当我们使用`` Room`` 创建一个数据库请求的时候，会返回一个`` DataSource.Factory``  为了方便观察，我们一般都会将这个 ``DataSource.Factory`` 转成一个 ``LiveData``  （如果你还不知道`` LiveData`` 是啥，没关系，请看这篇文章。ps：此处应有 ``LiveData``的文章）

```kotlin
// 利用上面单数据源数据库的例子：
val boundaryCallBack = MBoundaryCallBack()
// 这里就获得了一个LiveData<PageList<Student>> ，我们可以拿到这个 LiveData 去监听数据的变化
getAllStudent().toLiveData(PAGE_SIZE,boundaryCallBack) 
```



#### LivePagedListBuilder

上面我们说到`` LiveData `` ，还有另外一种实现也可以构造出 ``LiveData`` 就是利用 `` LivePagedListBuilder``  ，根据官方的介绍，这个是一个可以构造一个用 ``LiveData``  包含了 ``PagedList``  的一个 ``Builder`` 。他可以包含一个``PageList.Config``  让你可以配置更多的属性，当然，直接 `` toLiveData`` 也可以配置 `` PageList.Config``  具体使用哪种方法，主要是看习惯和场景的需要。

```kotlin 
// 创建一个线程池， executorNumber 是指定线程池的个数
val executor = Executors.newFixedThreadPool(executorNumber)
val liveData = LivePagedListBuilder(dataSourceFactory, pagedListConfig)
				.setFetchExecutor(executor).build()
```



#### 组合 

OK，我们现在已经将 ``Paging``  的所有准备工作做好了，是时候把他们组合串联起来了。

我们在一个`` Activity ``  中进行组合（为了减少其他不相关的知识点，方便阐述 ``Paging`` ，这里在Activity组装，但是更加推荐将部分代码放到 ``ViewModel`` 里面 。ps: 这里应该有`` ViewModel`` 的相关连接）核心代码已经做了注释：

```kotlin
class TestActivity : AppCompatActivity() {
    // 声明一个LiveData,并且延迟初始化
	private lateinit var mLiveData: LiveData<PagedList<Article>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       	rv_test.layoutManager = LinearLayoutManager(this)
        // 生成一个adapter，这个adapter 继承实现了PageListAdapter
        val adapter =  ListAdapter()
        // 创建一个线程池
        val executor = Executors.newFixedThreadPool(executorNumber)
        // 生成一个PageListConfig
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(enablePlaceholders)
            .setInitialLoadSizeHint(initialLoadSizeHint)
            .setPageSize(pageSize).build()
        // 通过LivePagedListBuilder的方式生成一个LiveData
      	mLiveData= LivePagedListBuilder(dataSourceFactory,pagedListConfig)
        				.setFetchExecutor(executor).build()
        // 利用这个LiveData 监听数据的变化
        mLiveData.observe(
            this,
            Observer<PagedList<Article>> {
                //当数据发生变化时，会走这个回调，通过submitList将数据重新设置到Adapter里
                adapter.submitList(it) 
            })
        rv_test.adapter = adapter
    }
}

```



## 总结

先上一张官方讲解 ``Paging`` 整体流程的神图，这张动图在很多讲解 `` Paging`` 的文章都会有，因为他很好的表达了整个`` Paging``  的工作流程，让我们可以更方便，更简洁明了的搞清楚 `` Paging``  各部分的关系 : 

![img](https://upload-images.jianshu.io/upload_images/7293029-27facf0a399c66b8.gif?imageMogr2/auto-orient/strip%7CimageView2/2/w/800/format/webp)  

下面，我通过上面所有的知识点，来梳理一下这张图的含义：

***首先，数据先从``DataSource``  处通过网络/数据库的方式获取  =>  然后生成一个`` DataSource.Factory``  被``PagedList`` 接受，`` PagedList`` 通过各种参数的配置，如 `` PagedListConfig``， =>  然后生成一个`` LiveData``  ，在 ``LiveData``  里面进行数据的监听，当有数据发生变化的时候，会回调给 `` LiveData`` 然后通过`` adapter.submitList``  将数据提供给UI层，这时候，数据层的工作已经圆满完成   = > 这时候数据进入 UI 层的 ``Adapter``，首先调用 UI 层的 DiffUtil 进行数据的 Diff 工作，然后将可以更新的数据返回给`` Adapter `` 的 `` onBindViewHolder`` 做数据的渲染工作，=>  当渲染完成后，即已经进行了列表的更改***

以上，则是 `` Paging `` 完整工作流程的一个拆解，我们可以看到，`` Paging ``  在设计的时候，就已经充分的提现了数据与 UI 的强解耦，各管各的，这样使得我们在做单元测试也好，在做数据的调整也罢，不会牵一发而动全身，而这，正是一个好的架构一个重要的特性。

好了，关于`` Paging``  你应该了解的 **基本 **东西差不多就这么多，但是 ``Paging`` 要真正用好，你要了解的并不止这么多，期待我们下次的关于 ``Paging``  话题的升级版的文章吧~ Thanks !