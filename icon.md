# 频道图标提供功能说明

## 概述

频道图标提供功能允许应用为频道自动生成或覆盖图标，解决M3U直播源中频道图标缺失或质量不佳的问题。

## 频道图标对应机制

### 1. M3U解析过程
在M3U文件解析时（`M3uIptvParser.kt`），系统会提取以下信息：
- `tvg-logo="URL"` - M3U中定义的频道图标URL
- `tvg-name="频道名"` - 用于EPG匹配的频道名称
- 频道名称本身

### 2. 频道图标优先级规则
系统按以下优先级决定使用哪个图标：

```kotlin
val logo = if (settingsVM.iptvChannelLogoOverride || channel.logo.isNullOrBlank()) {
    // 使用频道图标提供的模板
    settingsVM.iptvChannelLogoProvider
        .replace("{name}", channel.epgName)
        .replace("{name|lowercase}", channel.epgName.lowercase())
        .replace("{name|uppercase}", channel.epgName.uppercase())
} else {
    // 使用M3U中定义的图标
    channel.logo
}
```

**优先级顺序：**
1. 如果开启了"频道图标覆盖"选项 → 使用图标提供模板
2. 如果M3U中没有定义图标（`tvg-logo`为空） → 使用图标提供模板  
3. 否则使用M3U中的`tvg-logo`定义的图标

### 3. 图标提供模板机制
默认模板：`https://live.fanmingming.com/tv/{name|uppercase}.png`

**变量替换规则：**
- `{name}` → 频道的EPG名称（原始大小写）
- `{name|lowercase}` → 频道EPG名称转小写
- `{name|uppercase}` → 频道EPG名称转大写

**示例：**
- 频道EPG名称：`cctv1`
- 模板：`https://live.fanmingming.com/tv/{name|uppercase}.png`
- 最终URL：`https://live.fanmingming.com/tv/CCTV1.png`

### 4. 配置选项
- **频道图标提供**：设置图标URL模板
- **频道图标覆盖**：是否强制使用模板覆盖M3U中的图标

## 实现细节

### 核心文件
- `Constants.kt` - 定义默认图标提供模板
- `M3uIptvParser.kt` - 解析M3U文件中的图标信息
- `Channel.kt` - 频道实体，包含logo字段
- `ChannelsChannelItem.kt` - 频道图标显示逻辑
- `Configs.kt` - 配置管理

### 配置存储
- `iptvChannelLogoProvider` - 图标提供模板URL
- `iptvChannelLogoOverride` - 是否覆盖M3U中的图标

## 使用场景

1. **补充缺失图标**：为没有`tvg-logo`的频道自动生成图标
2. **统一图标风格**：使用统一的图标源替换各种不同质量的图标
3. **自定义图标源**：可以配置自己的图标服务器

## 设计优势

这样设计的好处是：
- 可以为没有图标的频道自动生成统一的图标URL
- 可以覆盖M3U中质量不好的图标
- 支持灵活的模板变量替换
- 用户可以自定义图标提供源
