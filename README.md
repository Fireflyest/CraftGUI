# CraftGUI - Frame of Minecraft GUI
##### 以游戏自带的容器作为可操作的用户界面，简化界面的开发流程
在原生的服务器中，使用GUI界面操作可以减少玩家需要输入的指令数量，让玩家无需为太多指令需要记忆而发愁。
使用容器界面，同时使用容器内的物品作为按钮，监听容器的点击事件，便实现了容器界面。让玩家更多更GUI打交道而不是使用指令。

由于服务器时常容易出现不稳定的情况，在特定的条件下，玩家有可能将容器内的按钮取下，所以仅仅取消容器点击事件来限制玩家取出按钮是完全不够的。
所以本插件使用前置插件[ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/)用于发送虚拟的物品以显示按钮。

## 目录
* 插件内容
  * [界面](#界面)
  * [物品按钮](#物品按钮)
* 使用方法
  * [获取导航](#获取导航)
  * [自定义界面](#自定义界面)
  * [添加界面](#添加界面)
  * [事件](#事件)
* 维护人员
* 使用情况

## 插件内容
### 界面
在添加界面时，要考虑到界面的用途以及界面的归属问题，比如该界面是属于插件的还是玩家的。
同时，还要考虑翻页的问题，页面的页码是否有限制。

本插件的页面实现简单来说就是通过一个导航`ViewGuide`控制玩家打开界面、切换界面和翻页。
每一个界面`View`可以包含多数的页面`ViewPage`双向链表，其中每个页面都能通过`viewPage.getNext()`来获取下一页或`viewPage.getPre()`获取上一页。

这里列出一些常用界面类型，当然你可以自己挖掘其他用法
#### 单目标界面
只有一页，而且界面的内容不由使用者的变化而变化。
通常界面的归属是某个插件，例如用来显示插件的信息，每个玩家打开看到都是一样的内容。

该类型下使用导航调用的指令`viewGuide.openView(player, viewName,  target)`中的target的作用就不是用来区分界面的使用者。
这种界面一般只有固定的几个`target`。

大致结构如下
```
ViewGuide {

  view_name → View {
    target → { ViewPage ↔ ViewPage ↔ ViewPage ↔ ViewPage ↔ ... }
  }
  
  view_name → View {
    target → ViewPage
    target → ViewPage
  }
  
}
```
#### 多目标界面
对于不同的玩家打开容器，可以使用参数`target`来获取对应玩家的专属页面。
这个界面的`target`数量取决于使用容器的玩家数量，并非固定的

一般来说，使用一个`Map<String, ViewPage>`来存储一个界面的所有页面，其中数据的键为`target`。

大致结构如下
```
ViewGuide {

  view_name → View {
  
    target → { ViewPage ↔ ViewPage ↔ ViewPage ↔ ViewPage ↔ ... }
    target → { ViewPage ↔ ViewPage ↔ ViewPage ↔ ViewPage ↔ ... }
    target → { ViewPage ↔ ViewPage ↔ ViewPage ↔ ViewPage ↔ ... }
    ...
  }
  
  view_name → View {
    target → ViewPage
    target → ViewPage
    target → ViewPage
    ...
  }
  
}
```
#### 应用示例
[GlobalMarket](https://github.com/Fireflyest/GlobalMarket)
插件的市场主界面就是单目标多页界面，每个玩家看到的都是同一个市场数据，界面归属是插件本体。

邮箱界面则是多目标单页界面，每个玩家都是一个目标，有自己专属的邮箱页面，
在玩家打开时，需要使用玩家的名称作为`target`作为参数找到玩家的邮箱页面。

此外，数据键`target`也不仅只使用玩家名称，交易界面使用商品的ID来作为数据键，使每个商品有专属的交易界面。
### 物品按钮
如果玩家打开一个界面，发现里面空空如也，那么他肯定有点诧异。

容器为了交互，就免不了在里面添加一些按钮。
为了更加方便地构建一个物品添加到界面内，插件提供了`ViewItemBuilder`类。
```java 
ItemStack button = new ViewItemBuilder(Material.DIAMOND)
                .name("§c按钮")
                .lore("§f点我送钻石！")
                .build();
```
当用户与界面中的物品交互时，插件需要获取玩家交互的物品，以及该物品对应的行为。
可以使用本插件提供的`ItemUtils`获取物品的相关数据。

在新建物品时设置参数
```java 
ItemStack button = new ViewItemBuilder(Material.DIAMOND)
                .name("§c按钮")
                .lore("§f点我送钻石！")
                .action(ViewItem.ACTION_CONSOLE_COMMAND)
                .value("give %player% diamond 1")
                .build();
```
在监听到玩家点击事件后通过`ItemUtils.getItemAction(item)`获取按钮执行行为类型。
并使用`ItemUtils.getItemValue(item)`获取行为的值，比如指令。
获取了物品的数据后，按照行为的类型执行相应的任务。

## 使用方法
### 获取导航
在插件的`onEnable()`方法内添加导航获取代码`this.setupGuide();`。
在类内添加初始化方法如下
```java 
private static ViewGuide guide;

public void setupGuide() {
      RegisteredServiceProvider<ViewGuide> rsp = Bukkit.getServer().getServicesManager().getRegistration(ViewGuide.class);
      if (rsp == null) {
          this.getLogger().warning("CraftGUI not found!");
          return;
      }
      guide = rsp.getProvider();
}
```

### 自定义界面
自定义界面可以用两种方法实现，自己定义类实现接口或者直接继承本插件提供的`SimpleView`和`SimplePage`。

todo
### 添加界面
自定义界面后，需要将界面添加到导航里面，才能让玩家打开。

首先为自定义界面起个名字，例如`craftgui.simple`。即使用插件的名称和界面名称组成，避免和其他插件的界面重名。
```java 
public static final String SIMPLE_VIEW = "craftgui.simple";
```
设置了名称后，使用导航添加界面。
建议界面需要带有一些标记，在事件监听的时候能够判别该事件是否他人的界面。
例如我传入插件的名称作为容器的标题。
```java 
guide.addView(SIMPLE_VIEW, new SimpleView("[CraftGUI]"));
```
需要玩家打开界面时，调用
```java 
guide.openView(player, SIMPLE_VIEW, "target");
```
### 事件
插件提供三种界面操作，分别是界面点击`ViewClickEvent`、界面物品放置`ViewPlaceEvent`
和数字键按钮操作`ViewHotbarEvent`（数字按钮类型在1.17以下表现得不完美）。

todo
## 维护人员
[@Fireflyest](https://github.com/Fireflyest) QQ: 746969484
## 使用情况

todo

