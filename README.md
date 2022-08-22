# CraftGUI - Frame of Minecraft GUI
![GitHub all releases](https://img.shields.io/github/downloads/Fireflyest/CraftGUI/total?style=flat-square)
![GitHub release (latest by date)](https://img.shields.io/github/downloads/Fireflyest/CraftGUI/latest/total?style=flat-square)
![Spiget Rating](https://img.shields.io/spiget/rating/CraftGUI?style=flat-square)
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

![guide](https://wx3.sinaimg.cn/mw2000/007vuuAIgy1h5c0h57eofj30qe0gq44d.jpg)

这里列出一些常用界面类型，当然你可以自己挖掘其他用法
#### 单目标界面
只有一页，而且界面的内容不由使用者的变化而变化。
通常界面的归属是某个插件，例如用来显示插件的信息，每个玩家打开看到都是一样的内容。

该类型下使用导航调用的指令`viewGuide.openView(player, "view-key",  "target-key")`中的target的作用就不是用来区分界面的使用者。
这种界面一般只有固定的几个`target`。

大致结构如下
```
ViewGuide {

  "view-key" → View {
    "target-key" → { ViewPage ↔ ViewPage ↔ ViewPage ↔ ViewPage ↔ ... }
  }
  
  "view-key" → View {
    "target-key" → ViewPage
    "target-key" → ViewPage
  }
  
  more view...
  
}
```
#### 多目标界面
对于不同的玩家打开容器，可以使用参数`target`来获取对应玩家的专属页面。
这个界面的`target`数量取决于使用容器的玩家数量，并非固定的

一般来说，使用一个`Map<String, ViewPage>`来存储一个界面的首页，供玩家打开，其中数据的键为`target`。

大致结构如下
```
ViewGuide {

  "view-key" → View {
  
    "target-key" → { ViewPage ↔ ViewPage ↔ ... }
    "target-key" → { ViewPage ↔ ViewPage ↔ ViewPage ↔ ViewPage ↔ ... }
    "target-key" → { ViewPage ↔ ViewPage ↔ ViewPage ↔ ... }
    
    more page...
  }
  
  "view-key" → View {
    "target-key" → ViewPage
    "target-key" → ViewPage
    "target-key" → ViewPage
    
    more page...
  }
  
  more view...
  
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

在新建物品时设置额外参数
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
在plugin.yml文件下添加依赖，让GUI插件优先于你的插件加载。
```yml
depend: [ProtocolLib,CraftGUI]
```
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
      ...
}
```

### 自定义界面
新建一个类并实现`View`接口，可以参考如下代码
```java 
public class CustomView implements View<CustomPage> {

    // 插件名称用于在监听事件时判断点击的是否本插件的界面
    protected final String pluginName;
    // 存储各个target的页面
    protected final Map<String, CustomPage> pageMap = new HashMap<>();

    public CustomView(String pluginName) {
        this.pluginName = pluginName;
    }

    /**
     * 玩家打开该界面时显示的页面
     * @param target 页面标签
     * @return 页面
     */
    @Override
    public CustomPage getFirstPage(String target){
        if (!pageMap.containsKey(target)){
            pageMap.put(target, new CustomPage(pluginName, target, 0, 27));
        }
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }
}
```
创建完界面后，创建相应的页面。可以新建一个类实现`ViewPage`接口，当然也可以继承本插件提供的`TemplatePage`，代码如下
```java 
public class CustomPage extends TemplatePage {

    public CustomPage(String pluginName, String target, int page, int size) {
        super(pluginName, target, page, size);
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        crashMap.clear();
        crashMap.putAll(itemMap);

        // 这里是需要异步加载的按钮
        for (int i = 0; i < 9; i++) {
            ItemStack item = new ViewItemBuilder(Material.STONE)
                    .name(String.format("Button%s", i))
                    .build();
            crashMap.put(i, item);
        }
        return crashMap;
    }

    @Override
    public ViewPage getNext() {
        // 自动创建下一页
        if(next == null && page < 7){
            next = new CustomPage(pluginName, target, page+1, size);
            next.setPre(this);
        }
        return next;
    }

    @Override
    public void refreshPage() {
        // 关闭按钮
        ItemStack closeButton = new ViewItemBuilder(Material.REDSTONE)
                .name("§c关闭")
                .lore("§f关闭界面")
                .action(ViewItem.ACTION_CLOSE)
                .build();
        itemMap.put(26, closeButton);
    }

}
```
这样就完成了一个简单的自定义页面。
### 添加界面
自定义界面后，需要将界面添加到导航里面，才能让玩家打开。

首先为自定义界面起个名字，例如`myplugin.custom`。即使用插件的名称和界面名称组成，避免和其他插件的界面重名。
```java 
public static final String CUSTOM_VIEW = "myplugin.custom";
```
设置了名称后，使用导航添加界面。
建议界面需要带有一些标记，在事件监听的时候能够判别该事件是否他人的界面。
例如我传入插件的名称作为容器的标题。
```java 
guide.addView(CUSTOM_VIEW, new CustomView("[CraftGUI]"));
```
需要玩家打开界面时，调用
```java 
guide.openView(player, CUSTOM_VIEW, "target");
```
### 事件
插件提供三种界面操作，分别是界面点击`ViewClickEvent`、界面物品放置`ViewPlaceEvent`
和数字键按钮操作`ViewHotbarEvent`。

监听代码参考如下
```java 
@EventHandler
public void onViewClick(ViewClickEvent event) {
    // 判断是否本插件相关的事件
    if(!event.getView().getTitle().contains("[CraftGUI]")) return;
    // 是否点击到物品，一般来说是有物品
    ItemStack item = event.getCurrentItem();
    if(item == null) return;
    // 获取点击的玩家
    Player player = (Player)event.getWhoClicked();
    // 获取行为和值
    int action = ItemUtils.getItemAction(item);
    String value = ItemUtils.getItemValue(item);
    // 根据行为做反应，如果是页面跳转，请取消刷新，防止不必要算力消耗
    switch (action){
        case ViewItem.ACTION_CLOSE: // 关闭页面
            event.setRefresh(event.isShiftClick());
            player.closeInventory();
            break;
        case ViewItem.ACTION_PAGE: // 页面跳转
            event.setRefresh(event.isShiftClick());
            if ("pre".equals(value)){
                guide.prePage(player);
            } else if ("next".equals(value)) {
                guide.nextPage(player);
            } else {
                guide.jump(player, NumberUtils.toInt(value));
            }
            break;
        case ViewItem.ACTION_BACK: // 返回上一个界面
            event.setRefresh(event.isShiftClick());
            guide.back(player);
            break;
        case ViewItem.ACTION_OPEN: // 打开一个界面
            event.setRefresh(event.isShiftClick());
            guide.openView(player, CraftGUI.SIMPLE_VIEW, value);
            break;
        case ViewItem.ACTION_PLAYER_COMMAND: // 玩家指令
            if (value != null) player.performCommand(value);
            break;
        case ViewItem.ACTION_CONSOLE_COMMAND: // 控制台指令
            if (value == null) break;
            String command = value.replace("%player%", player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
            break;
        case ViewItem.ACTION_PLUGIN:
            // do something
            break;
        case ViewItem.ACTION_UNKNOWN:
        case ViewItem.ACTION_EDIT:
        case ViewItem.ACTION_NONE:
        default:
    }
}
```
## 维护人员
[Fireflyest](https://github.com/Fireflyest) QQ: 746969484
## 使用情况
![bstats](https://bstats.org/signatures/bukkit/CraftGUI.svg)
