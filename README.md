<div align="center">
    <h1>天光云影</h1>
<div align="center">

![GitHub Repo stars](https://img.shields.io/github/stars/yaoxieyoulei/mytv-android)
![GitHub all releases](https://img.shields.io/github/downloads/yaoxieyoulei/mytv-android/total)
[![Android Sdk Require](https://img.shields.io/badge/Android-5.0%2B-informational?logo=android)](https://apilevels.com/#:~:text=Jetpack%20Compose%20requires%20a%20minSdk%20of%2021%20or%20higher)
[![GitHub](https://img.shields.io/github/license/yaoxieyoulei/mytv-android)](https://github.com/yaoxieyoulei/mytv-android)

</div>
    <p>使用Android原生开发的视频播放软件</p>
</div>

## 使用

### 操作方式

> 遥控器操作方式与主流视频播放软件类似；

- 频道切换：使用上下方向键，或者数字键切换频道；屏幕上下滑动；
- 频道选择：OK键；单击屏幕；
- 线路切换：使用左右方向键；屏幕左右滑动；
- 设置页面：按下菜单、帮助键，长按OK键；双击、长按屏幕；

### 支持的遥控器按键

增加支持多种遥控器类型和按键映射，所有按键都会被应用拦截，避免与电视系统产生冲突。

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

##### 特殊遥控器按键（Sony等品牌）
| 功能 | 按键名称 | KeyCode | 数值 | 说明 |
|------|----------|---------|------|------|
| 频道+ | `PROG_RED` | 166 | 红色功能键（部分Sony TV） |
| 频道- | `PROG_GREEN` | 167 | 绿色功能键（部分Sony TV） |

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

#### 调试和扩展支持

如果你的遥控器使用了上述列表中没有包含的按键编码，可以通过以下方式获取具体的KeyCode：

1. **启用ADB调试**：
   ```bash
   adb connect <电视IP地址>
   adb logcat | grep "MainActivity"
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

- 访问以下网址：`http://<设备IP>:10481`

## 下载

可以通过右侧release进行下载或拉取代码到本地进行编译

## 说明

- 仅支持Android5及以上
- 网络环境必须支持IPV6（默认订阅源）
- 只在自家电视上测过，其他电视稳定性未知

## 更新日志

[更新日志](./CHANGELOG.md)

## 声明

此项目（天光云影）是个人为了兴趣而开发, 仅用于学习和测试。 所用API皆从官方网站收集, 不提供任何破解内容。

## 技术交流

Telegram: https://t.me/mytv_android

## 赞赏

<img src="./screenshots/mm_reward_qrcode.png" width="48%"/>

## 致谢

- [my-tv](https://github.com/lizongying/my-tv)
- [参考设计稿](https://github.com/lizongying/my-tv/issues/594)
- [live](https://github.com/fanmingming/live)
- 等等
