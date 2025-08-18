<div align="center">
    <h1>电视直播<sup>TV</sup></h1>
<div align="center">

<p align="right">
  <a href="README.md">🇨🇳 中文</a> | <a href="README_EN.md">🇺🇸 English</a>
</p>

![GitHub Repo stars](https://img.shields.io/github/stars/mytv-android/mytv-android)
![GitHub all releases](https://img.shields.io/github/downloads/mytv-android/mytv-android/total)
[![Android Sdk Require](https://img.shields.io/badge/Android-5.0%2B-informational?logo=android)](https://apilevels.com/#:~:text=Jetpack%20Compose%20requires%20a%20minSdk%20of%2021%20or%20higher)
[![GitHub](https://img.shields.io/github/license/mytv-android/mytv-android)](https://github.com/mytv-android/mytv-android)

</div>
    <p>基于天光云影3.3.9和mytv-android最后的开源版本，主要是针对sony的遥控器（按键比较多的那种）使用进行了魔改，包名和版本号也都改了，别的品牌的遥控器应该也行</p>

<!-- <img src="./screenshots/Screenshot_dashboard.png" width="96%"/> -->
<br/>
<!-- <img src="./screenshots/Screenshot_channels.png" width="48%"/>
<img src="./screenshots/Screenshot_search.png" width="48%"/> -->
</div>

## 修改点

- Sony TV深度适配

   - 遥控器按键全面支持：
      - 频道上下键 (CHANNEL_UP/CHANNEL_DOWN)
      - 数字键支持 (KEYCODE_NUMBER)
      - 彩色功能键支持 (红绿黄蓝键)
      - 音轨切换键 (KEYCODE_MEDIA_AUDIO_TRACK)
      - 字幕切换键 (KEYCODE_CAPTIONS)
      - 节目指南键 (KEYCODE_GUIDE)
      - 信息显示键 (KEYCODE_INFO)
      - MENU键切换QuickOp界面
      - F2键Dashboard和直播页面切换
      - 回看按键 (KEYCODE_LAST_CHANNEL)
   - USB遥控器支持 - 扩展遥控器兼容性
   - 按键逻辑优化 - 符合Sony遥控器使用习惯
- 全新EPG界面：
  - EpgGuideScreen节目指南主界面
  - 频道列表和节目网格显示
  - 日期选择和时间段显示
  - 当前节目快速访问
- 其他:
  - 播放器内核切换弹窗功能
  - 配置默认值调整 - 关闭了频道分组切换和自动更新功能
  - 自适应图标删了，因为sony上就是矩形的
## 遥控器使用

### 操作方式

> 原有代码支持的遥控器操作方式

- 频道切换：使用上下方向键，或者数字键切换频道；屏幕上下滑动；
- 频道选择：OK键；单击屏幕；
- 线路切换：使用左右方向键；屏幕左右滑动；
- 设置页面：按下菜单、帮助键，长按OK键；双击、长按屏幕；

### 支持的遥控器按键

新增支持多种遥控器类型和按键映射，按键都会被应用拦截，避免与电视系统产生冲突，但是有些按键有最高优先级拦截不到没办法处理，比如帮助按键怎么都拦截不到。

#### 频道切换按键

##### 标准Android TV遥控器
| 功能 | 按键名称 | KeyCode | 数值 | 说明 |
|------|----------|---------|------|------|
| 频道+ | `KEYCODE_CHANNEL_UP` | 166 | 标准TV遥控器频道上键 |
| 频道- | `KEYCODE_CHANNEL_DOWN` | 167 | 标准TV遥控器频道下键 |

##### USB遥控器/外接遥控器
| 功能 | 按键名称 | KeyCode | 数值 | 说明 |
|------|----------|---------|------|------|
| 频道+ | `KEYCODE_MEDIA_PREVIOUS` | 88 | 媒体上一个按键 |
| 频道- | `KEYCODE_MEDIA_NEXT` | 87 | 媒体下一个按键 |
| 频道+ | `KEYCODE_PAGE_UP` | 92 | 翻页向上键 |
| 频道- | `KEYCODE_PAGE_DOWN` | 93 | 翻页向下键 |
| 频道+ | `KEYCODE_PLUS` | 81 | 加号键 |
| 频道- | `KEYCODE_MINUS` | 69 | 减号键 |
| 频道+ | `KEYCODE_NUMPAD_ADD` | 157 | 数字键盘加号 |
| 频道- | `KEYCODE_NUMPAD_SUBTRACT` | 156 | 数字键盘减号 |


#### 数字键支持
| 按键 | KeyCode名称 | KeyCode | 数值 | 功能说明 |
|------|-------------|---------|------|----------|
| 0 | `KEYCODE_0` | 7 | 数字0键，频道直选 |
| 1 | `KEYCODE_1` | 8 | 数字1键，频道直选 |
| 2 | `KEYCODE_2` | 9 | 数字2键，频道直选 |
| 3 | `KEYCODE_3` | 10 | 数字3键，频道直选 |
| 4 | `KEYCODE_4` | 11 | 数字4键，频道直选 |
| 5 | `KEYCODE_5` | 12 | 数字5键，频道直选 |
| 6 | `KEYCODE_6` | 13 | 数字6键，频道直选 |
| 7 | `KEYCODE_7` | 14 | 数字7键，频道直选 |
| 8 | `KEYCODE_8` | 15 | 数字8键，频道直选 |
| 9 | `KEYCODE_9` | 16 | 数字9键，频道直选 |

> **重要说明**：所有数字键（0-9）已被应用完全拦截，**不会触发电视系统的频道切换**，仅用于应用内的频道直选功能。

#### 功能按键支持
| 功能     | 按键名称                   | KeyCode | 数值                       | 功能说明 |
|--------|------------------------|---------|--------------------------|----------|
| EPG节目单切换 | `KEYCODE_LAST_CHANNEL` | 229 | 回看键，全局EPG页面和直播页面之间切换     |
| 节目指南   | `KEYCODE_GUIDE`        | 229 | 当前频道的节目指南|
| 播放器信息显示 | `KEYCODE_INFO` | 165 | 信息键，显示/隐藏播放器详细信息 |
| 音轨切换 | `KEYCODE_MEDIA_AUDIO_TRACK` | 222 | 音轨键，显示/隐藏音轨选择界面       |
| 字幕切换 | `KEYCODE_CAPTIONS` | 175 | 字幕键，显示/隐藏字幕选择界面       |
| Dashboard主页切换 | `KEYCODE_F2` | 132 | F2键，Dashboard主页和直播页面之间切换 |

> **功能说明**：
> - **回看键**：第一次按进入EPG节目单页面，再次按返回直播页面
> - **信息键**：第一次按显示播放器详细信息，再次按隐藏播放器信息
> - **音轨键**：第一次按显示音轨选择界面，再次按关闭音轨界面
> - **字幕键**：第一次按显示字幕选择界面，再次按关闭字幕界面
> - **F2键**：第一次按进入Dashboard主页，再次按返回直播页面


#### 颜色按键支持
| 功能 | 按键名称 | KeyCode | 数值 | 功能说明 |
|----|----------|---------|------|----------|
| 线路切换 | `KEYCODE_PROG_RED` | 183 | 红色功能键，显示/隐藏线路选择界面 |
| 音轨切换 | `KEYCODE_PROG_GREEN` | 184 | 绿色功能键，显示/隐藏音轨选择界面 |
| 字幕切换 | `KEYCODE_PROG_YELLOW` | 185 | 黄色功能键，显示/隐藏字幕选择界面 |
| Dashboard主页 | `KEYCODE_PROG_BLUE` | 186 | 蓝色功能键，Dashboard主页和直播页面之间切换 |

> **功能说明**：
> - **红色键**：第一次按显示线路选择界面，再次按关闭线路界面
> - **绿色键**：第一次按显示音轨选择界面，再次按关闭音轨界面  
> - **黄色键**：第一次按显示字幕选择界面，再次按关闭字幕界面
> - **蓝色键**：第一次按进入Dashboard主页，再次按返回直播页面


#### 调试和扩展支持

如果你的遥控器使用了上述列表中没有包含的按键编码，可以通过以下方式获取具体的KeyCode：

1. **启用ADB调试**：
   ```bash
   adb connect <电视IP地址>
   adb logcat | grep mytv"
   ```

2. **按下遥控器按键**，查看日志输出：
   ```
   Unknown key intercepted: keyCode=XXX, scanCode=XXX, displayLabel='X'
   ```


#### 按键拦截机制

应用使用 `dispatchKeyEvent()` 方法在系统级别拦截按键事件：
- **完全拦截**：频道切换类按键返回 `true`，阻止事件传递给电视系统
- **应用优先**：数字键先由应用处理，然后阻止系统处理
- **兼容性保证**：未识别的按键会记录日志但不会影响正常功能

### 触摸键位对应

- 方向键：屏幕上下左右滑动
- OK键：点击屏幕
- 长按OK键：长按屏幕
- 菜单、帮助键：双击屏幕

### 自定义设置

- 访问以下网址：`http://<设备IP>:10591`

## 下载

可以通过右侧release进行下载或拉取代码到本地进行编译

## 说明

- 仅支持Android5及以上
- 只在自家电视上测过，其他电视稳定性未知


### 以下说明是原项目的，本项目有问题提issue吧，但是我只能处理遥控器按键的问题

## 信息获取

交流（此群一般不作为反馈bug，提供建议之处，相关内容请发布至issue）和测试版信息发布，都请关注此频道以获取最新改进，以及更新投票计划。

<div align="center">
    <img src="./img/QRCode.png" width="48%"/>
</div>

## 星标历史

<a href="https://www.star-history.com/#mytv-android/mytv-android&Date">
 <picture>
   <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=mytv-android/mytv-android&type=Date&theme=dark" />
   <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=mytv-android/mytv-android&type=Date" />
   <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=mytv-android/mytv-android&type=Date" />
 </picture>
</a>

## 著作权、许可证声明和致谢

- 本软件基于天光云影（https://github.com/yaoxieyoulei/mytv-android/tree/feature/ui ）进行迭代，在此感谢作者 yaoxieyoulei 的无私奉献。天光云影使用的MIT许可证请参见[天光云影许可证](./LICENSE_ORIGIN)，当你复制软件代码时，请保留此许可证和原作者版权声明。

- 本软件使用的GNU许可证请参见[本项目许可证](./LICENSE)，你可以自由地分发和衍生本软件。但当你基于本软件代码进行分发和演绎时，你不能修改许可证；你需要公开修改后的源代码；你也需要保留本软件的相关声明。

- 本软件还使用了BV（https://github.com/aaa1115910/bv ）的部分代码，在此特感谢aaa1115910。[许可证](./LICENSE_PART1)。