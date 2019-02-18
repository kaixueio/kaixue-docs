> 自盘谷开天辟地后，上古大神Andy Rubin开宗立派创立Android门，历经数十载，宗门日渐壮大，门下弟子遍布整个九州大陆。坊间流传，其门下弟子最善以身着格子衬衣，牛仔裤，背黑色双肩包装扮行走江湖，那其中的头顶光滑锃亮者更是其中的佼佼强者，修为绝顶。

### 本门藏经阁-AndroidX 
藏经阁，历来为各大门派收藏本门绝学之所在。
本门亦不例外，这AndroidX 中尽数收录了宗门无上武功心法秘笈，其中多数源自宗门上古宝典支持库([Support Library Packages](https://developer.android.com/topic/libraries/support-library/))，经过历代宗主不断参悟，优化改进，现已形成一套完整修炼体系，为门下弟子修行所用。后世若是有门人参透更高武学，亦将收录于此，造福后世弟子。

好吧，接下来我要好好说话了，编不下去了......

<img src="http://pn3wv0m0r.bkt.clouddn.com/blog/image/androidx/img1.jpeg" width=200 height=200 />

**来自官方的解释**
> AndroidX是对原始Android Support库([Support Library Packages](https://developer.android.com/topic/libraries/support-library/))的重大改进。是Android团队用于在Jetpack中开发，测试，打包，发布和发布库的开源项目 。

PS：[**Support库**](https://developer.android.com/topic/libraries/support-library/)如果刚入门的同学不知为何物的话这边简单介绍一下。
support库和Android操作系统是分开提供的，Support库提供了一系列的没有内置在Android框架内的功能，包括向后兼容版本的新功能，框架中未包含的实用 UI 元素等如V4（`android.support.v4`），V7（`android.support.v7`）包。

看到这里是不是还是觉得一头雾水，AndroidX 到底是什么XX啊，Jetpack！怎么又冒出个新名词。别急，下面会展开细说（关于Jetpack，[码上开学](https://kaixue.io/)将会展开对其进行详细的介绍，感兴趣的同学可以在后面的文章学习到）。看完相信众位师兄弟们定能修为有所精进，距离绝顶境界又进一步.

### 藏经阁中都有什么XX
AndroidX中的所有软件包都以字符串`androidx`开头。原始Support库包也已映射到相应的`androidx.*`包中。
如原始Support库中的`com.android.support:appcompat-v7`包映射到AndroidX中为`androidx.appcompat:appcompat:1.0.0` 。包名变成以`androidx`开头。
下表仅列部分原始支持库迁移到AndroidX到映射表，大概了解一下就可以了，只要知道之前使用到支持库中的库AndroidX都支持，如何使用下面会具体介绍，感兴趣的同学可以去官网查看完整[映射表](https://developer.android.com/jetpack/androidx/migrate)

| 原始Support库      |    AndroidX | 
| :--------: | :--------:| 
| com.android.support:support-compat	  | androidx.core:core:1.0.0+| 
|com.android.support:appcompat-v7	|androidx.appcompat:appcompat:1.0.0+|
|com.android.support:design	|com.google.android.material:material:1.0.0+|
|com.android.support:multidex|androidx.multidex:multidex:2.0.0+|
|com.android.support.test.espresso:espresso-accessibility|androidx.test.espresso:espresso-accessibility:3.1.0|
|com.android.support.test:runner|androidx.test:runner:1.1.0|
|com.android.support:cardview-v7|androidx.cardview:cardview:1.0.0|
|com.android.support:recyclerview-v7|androidx.recyclerview:recyclerview:1.0.0|
|com.android.support:viewpager|androidx.viewpager:viewpager:1.0.0|
|android.arch.lifecycle:livedata|androidx.lifecycle:lifecycle-livedata:2.0.0-rc01|
|com.android.support:support-fragment|androidx.fragment:fragment:1.0.0+|

除了对原始Support库的映射和改进，后续新的Support库的开发都将在AndroidX库中进行，包括原始Support库都维护和新引入的Jetpack组件。AndroidX库中的包大致可以分为以下三大类：
  1. 测试组件
  > 包含AndroidX中所有测试相关组件，如果你是个爱写单元测试的宝宝，那在原始Support库时代就一定用过相关的测试组件，没用过的也没关系，以后的项目中如果需要覆盖单元测试甚至自动化测试，就一定会用到测试相关的组件。
  > AndroidX中所有测试相关的组件都在`androidx.test.*`包下，如：
  `androidx.test.runner`,`androidx.test.espresso`
 
  2. 架构组件
  >  即Jetpack相关组件。
  3. 其他AndroidX组件

### AndroidX 解决了什么问题
前面说到AndroidX是对原始Support库重大改进。那么谷歌爸爸为什么在已经有了迭代了很多版本的Support库的情况下又整出一个新的AndroidX呢？
直白一点来说就是现有的Support库越来越难用了：
1. 随着版本的迭代，需要做更多的兼容，Support库包的体积越来越臃肿，版本维护成本越来越高。
2. 库的功能不单一，每个库均是针对特定范围的Android平台和功能，如使用`android.support.v7.*`包，可能你的App只会用到其中的几个库，但是你需要将整个V7包都引入，这势必会增加你的App体积和方法数等。
3. 基于第二点原因带来的更新体验更加难受，如果Support库中更新了某个功能的新特性或修复了bug需要升级，又或是你的targetsdk需要变更，你的所有`android.supprot.*`相关的依赖全部都需要更新，牵一发动全身这无疑是开发总最让人脑阔疼的事了。



那么AndroidX是如何解决以上问题的呢？

AndroidX中的所有组件都是单独维护和更新的。这样做的好处就是所有组件功能更加单一，职责分明，更加易于维护和更新。我们在使用时仅需引入我们需要的组件，而且当某个组件发布新的特性或者修复bug，只需更新对应的组件即可，其他组件不受影响。

### AndroidX 如何使用
目前Android 28.0.0稳定版使用的还是`android.support.*`,创建新的项目默认使用的还是Support库。所以想体验AndroidX的同学可以尝试将自己的项目迁移至AndroidX。
>PS:最新的Support库版本是28.0.0稳定版（2018年9月21日更新），这将是Support更新的最后一个版本，这也是谷歌爸爸预留给开发者们迁移到AndroidX的时间，后续所有更新都将在AndroidX中进行。

[来自官方的说明:](https://developer.android.google.cn/topic/libraries/support-library/revisions#28-0-0-alpha1)

>This is the stable release of Support Library 28.0.0 and is suitable for use in production. This will be the last feature release under the android.support packaging, and developers are encouraged to migrate to AndroidX.

OK,既然官方推荐而且Support库也不再更新了，没啥好说的了，盘它！

#### 准备工作
**1.Android Studio 升级到3.2.0 Canary 14以上版本**

 <img src="http://pn3wv0m0r.bkt.clouddn.com/blog/image/androidx/img2.png" width=500 height=300 />



**2.gradle版本升到3.2.0以上**
```
classpath 'com.android.tools.build:gradle:3.2.1'
```

**3.compileSdkVersion升到28以上**
```
 compileSdkVersion 28
 defaultConfig {
      targetSdkVersion 28
  }
```
**4.gradle.properties文件配置**
如果你是在一个新的项目中需要使用AndroidX，`.gradle.properties`文件中这样配置：
```
android.enableJetifier=true
android.useAndroidX=true
```
如果你想在一个现有的项目中体验AndroidX，但是之前使用的库不迁移的话，`.gradle.properties`文件中这样配置：
```
android.enableJetifier=false
android.useAndroidX=true
```

#### 一键迁移
Android Studio 3.2.0 Canary 14及以上版本提供了方便快捷的一键迁移到AndroidX的功能。在菜单上选择**Refactor->Migrate to AndroidX**即可完成迁移。

![Alt text](http://pn3wv0m0r.bkt.clouddn.com/blog/image/androidx/img3.png)

**注意:**
如果准备工作第三点中你的`compileSdkVersion`没有升到28及以上点击“Migrate to AndroidX”会提示：

![Alt text](http://pn3wv0m0r.bkt.clouddn.com/blog/image/androidx/img4.png)

### 写在最后
关于AndroidX的来龙去脉以及如何使用就介绍到这里啦！最后再总结一下：
 原始Support库谷歌爸爸最终只更新到28.0.0版本，后续将不再更新，所有的新功能开发都将在AndroidX中进行。仅凭这一点我们就需要赶紧学起来了，虽然技术的更迭节奏让我们眼花缭乱，明明刚出的还没学完又出新花样，但正是技术的不断更迭才带来技术的不断进步，我们在学习新技术的同时也是让自己变得更好的过程，从我们踏进这个门，选择这条路的时候开始，就已经做好了终身学习的准备了。世界在变，我们需要做的就是享受变化，拥抱变化！（猝不及防来了一波鸡汤）。

电光火石间，宗门重地藏经阁金顶三道天雷降下，将这夜映的如白昼一般，顿时间狂风扫落叶，乌云压金顶！突然，一道金光夺窗而出，直奔天雷御剑而去。得见此景弟子的们都在大喊“快看！快看！藏经阁飞出一个头顶冒金光的人，往天雷飞去了”。而此时站在山顶的宗主则微微一笑，他知道这是门下又一弟子习得藏经阁中上乘功法至绝顶境界了，那一头的金光正是绝顶境界！

