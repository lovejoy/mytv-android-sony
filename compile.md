# MyTV Android 编译指南

本文档详细说明了如何从源码编译 MyTV Android 应用，包括环境配置、依赖安装和编译流程。

注意原开发者自己弄了media3 player支持av3a的格式 https://github.com/yaoxieyoulei/media/tree/av3a_1.4.1 
但是我用这个编译不成功，所以还是用的官方media3的库，这个格式搜了下应该是8k视频之类用的更好音质的音频格式，但是我这没有，所以先不折腾了
## 📋 目录

- [系统要求](#系统要求)
- [基础环境安装](#基础环境安装)
- [项目结构](#项目结构)
- [编译步骤](#编译步骤)
- [编译配置](#编译配置)
- [常见问题](#常见问题)
- [高级配置](#高级配置)

## 💻 系统要求

### 支持的操作系统
- **macOS**: 10.14+ (推荐 11.0+)
- **Linux**: Ubuntu 18.04+, CentOS 7+
- **Windows**: Windows 10+ (需要 WSL2 或原生支持)

### 硬件要求
- **内存**: 最少 8GB RAM (推荐 16GB+)
- **存储**: 可用空间 20GB+
- **CPU**: 64位处理器

## 🛠️ 基础环境安装

### 1. Java Development Kit (JDK)

项目需要 **JDK 17** 或更高版本。

#### macOS 安装
```bash
# 使用 Homebrew 安装
brew install openjdk@17

# 设置环境变量
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v17)' >> ~/.zshrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.zshrc
source ~/.zshrc
```

#### Linux (Ubuntu) 安装
```bash
# 安装 OpenJDK 17
sudo apt update
sudo apt install openjdk-17-jdk

# 设置环境变量
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

#### Windows 安装
1. 下载 [OpenJDK 17](https://adoptium.net/)
2. 安装并设置环境变量 `JAVA_HOME`
3. 将 `%JAVA_HOME%\bin` 添加到 `PATH`

#### 验证安装
```bash
java -version
javac -version
```

### 2. Android SDK

#### 方法 1: 使用 Android Studio (推荐)

1. **下载安装 Android Studio**
   - 访问 [Android Studio 官网](https://developer.android.com/studio)
   - 下载对应平台的安装包
   - 运行安装程序并完成初始化

2. **SDK 配置**
   ```
   启动 Android Studio
   → Tools → SDK Manager
   → 安装所需的 SDK 平台和工具
   ```

   **必需安装项**：
   - Android SDK Platform 21 (API Level 21)
   - Android SDK Platform 35 (API Level 35)  
   - Android SDK Build-Tools 35.0.0
   - Android SDK Platform-Tools
   - Android SDK Tools

#### 方法 2: 使用命令行工具

##### macOS 安装
```bash
# 使用 Homebrew 安装
brew install --cask android-commandlinetools

# 安装必需的 SDK 组件
sdkmanager --install "platforms;android-21" \
                     "platforms;android-35" \
                     "build-tools;35.0.0" \
                     "platform-tools"

# 接受许可协议
yes | sdkmanager --licenses
```

##### Linux 安装
```bash
# 下载命令行工具
wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
unzip commandlinetools-linux-9477386_latest.zip
mkdir -p ~/android-sdk/cmdline-tools
mv cmdline-tools ~/android-sdk/cmdline-tools/latest

# 设置环境变量
echo 'export ANDROID_HOME=~/android-sdk' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/platform-tools' >> ~/.bashrc
source ~/.bashrc

# 安装 SDK 组件
sdkmanager --install "platforms;android-21" \
                     "platforms;android-35" \
                     "build-tools;35.0.0" \
                     "platform-tools"
```

#### 验证 SDK 安装
```bash
# 检查 SDK 路径
echo $ANDROID_HOME

# 列出已安装的包
sdkmanager --list_installed

# 检查 ADB
adb version
```


## 🏗️ 项目结构

MyTV Android 采用多模块架构：

```
mytv-android/
├── allinone/              # 完整版本模块
├── core/                  # 核心功能模块
│   ├── data/             # 数据层
│   ├── designsystem/     # 设计系统
│   └── util/             # 工具类
├── ijkplayer-java/       # IJK播放器
├── mobile/               # 手机版模块
├── tv/                   # TV版模块 (主要)
├── build.gradle.kts      # 根项目构建脚本
├── settings.gradle.kts   # 项目设置
├── gradle.properties     # Gradle配置
└── local.properties      # 本地配置(需要创建)
```

### 编译产物说明

- **tv**: Android TV 版本 (主要版本)
- **mobile**: 手机版本
- **allinone**: 包含所有功能的完整版本

## 🚀 编译步骤

### 1. 获取源码

```bash
# 克隆仓库
git clone https://github.com/yaoxieyoulei/mytv-android.git
cd mytv-android

# 查看分支
git branch -a

# 切换到开发分支 (如果需要)
git checkout feature/ui
```

### 2. 创建本地配置文件

创建 `local.properties` 文件：

```bash
# 创建配置文件
touch local.properties
```

编辑内容（根据你的实际路径修改）：

```properties
## Android SDK 路径配置
# macOS (Android Studio)
sdk.dir=/Users/你的用户名/Library/Android/sdk

# macOS (Homebrew)
# sdk.dir=/opt/homebrew/share/android-commandlinetools

# Linux
# sdk.dir=/home/你的用户名/android-sdk

# Windows
# sdk.dir=C\:\\Users\\你的用户名\\AppData\\Local\\Android\\Sdk

## 签名配置 (可选，用于 Release 版本)
storeFile=debug.keystore
storePassword=android
keyAlias=androiddebugkey
keyPassword=android
```

### 3. 生成调试签名文件

```bash
# 生成调试用的 keystore
keytool -genkey -v -keystore debug.keystore \
        -keyalg RSA -keysize 2048 -validity 10000 \
        -alias androiddebugkey \
        -storepass android -keypass android \
        -dname "CN=Android Debug,O=Android,C=US"
```

### 4. 编译项目

#### 清理项目
```bash
./gradlew clean
```

#### 编译 Debug 版本
```bash
# 编译 TV 版本 (推荐)
./gradlew :tv:assembleDebug

# 编译 Mobile 版本
./gradlew :mobile:assembleDebug

# 编译 AllinOne 版本
./gradlew :allinone:assembleDebug
```

#### 编译 Release 版本
```bash
# TV 版本 Release
./gradlew :tv:assembleOriginalRelease -x lint

# Mobile 版本 Release  
./gradlew :mobile:assembleOriginalRelease

# AllinOne 版本 Release
./gradlew :allinone:assembleOriginalRelease
```

#### 编译所有版本
```bash
./gradlew assemble
```

### 5. 查找编译产物

编译完成后，APK 文件位置：

```
# TV 版本
tv/build/outputs/apk/debug/mytv-android-tv-{version}-all-sdk21-debug.apk
tv/build/outputs/apk/original/release/mytv-android-tv-{version}-all-sdk21-original.apk

# Mobile 版本
mobile/build/outputs/apk/debug/mytv-android-mobile-{version}-all-sdk21-debug.apk
mobile/build/outputs/apk/original/release/mytv-android-mobile-{version}-all-sdk21-original.apk

# AllinOne 版本
allinone/build/outputs/apk/debug/mytv-android-allinone-{version}-all-sdk21-debug.apk
allinone/build/outputs/apk/original/release/mytv-android-allinone-{version}-all-sdk21-original.apk
```

## ⚙️ 编译配置

### Gradle 配置

#### 版本信息
```kotlin
// gradle/libs.versions.toml
[versions]
minSdk = "21"           # 最低支持 Android 5.0
targetSdk = "35"        # 目标 Android 14  
compileSdk = "35"       # 编译 Android 14
versionName = "3.3.9"   # 应用版本号
```

#### 支持的 CPU 架构
```kotlin
// 项目支持的 ABI
android {
    defaultConfig {
        ndk {
            abiFilters 'armeabi-v7a', 'arm64-v8a'
        }
    }
}
```

#### 构建变体 (Build Variants)

**Flavors**:
- `original`: 原版功能
- `disguised`: 伪装版本

**Build Types**:
- `debug`: 调试版本
- `release`: 发布版本

### 输出文件命名

APK 文件命名格式：
```
mytv-android-{module}-{version}-{abi}-sdk{minSdk}-{flavor}.apk

示例:
mytv-android-tv-3.3.9-all-sdk21-original.apk
```

## ❗ 常见问题

### 1. SDK 路径错误

**错误信息**:
```
SDK location not found. Define location with an ANDROID_HOME environment variable or by setting the sdk.dir path in your project's local.properties file.
```

**解决方案**:
```bash
# 检查 SDK 路径
echo $ANDROID_HOME

# 创建/修改 local.properties
echo "sdk.dir=/path/to/your/android-sdk" > local.properties
```

### 2. 内存不足

**错误信息**:
```
Java heap space
```

**解决方案**:
```bash
# 增加 Gradle 内存限制
echo "org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=512m" >> gradle.properties
```

### 3. 签名文件缺失

**错误信息**:
```
Keystore file not found
```

**解决方案**:
```bash
# 生成调试签名文件
keytool -genkey -v -keystore debug.keystore \
        -keyalg RSA -keysize 2048 -validity 10000 \
        -alias androiddebugkey \
        -storepass android -keypass android \
        -dname "CN=Android Debug,O=Android,C=US"

# 更新 local.properties
echo "storeFile=debug.keystore" >> local.properties
echo "storePassword=android" >> local.properties
echo "keyAlias=androiddebugkey" >> local.properties
echo "keyPassword=android" >> local.properties
```

### 4. 网络问题

**错误信息**:
```
Could not resolve dependencies
```

**解决方案**:
```bash
# 使用国内镜像 (gradle.properties)
systemProp.http.proxyHost=mirrors.cloud.tencent.com
systemProp.http.proxyPort=80
systemProp.https.proxyHost=mirrors.cloud.tencent.com  
systemProp.https.proxyPort=80

# 或者使用阿里云镜像
# 在 build.gradle.kts 中添加:
repositories {
    maven { url = uri("https://maven.aliyun.com/repository/google") }
    maven { url = uri("https://maven.aliyun.com/repository/central") }
    google()
    mavenCentral()
}
```

### 5. 编译错误

**错误信息**:
```
Compilation error
```

**解决方案**:
```bash
# 清理并重新构建
./gradlew clean
./gradlew build --refresh-dependencies

# 检查 Java 版本
java -version  # 应该是 17+

# 检查 Gradle 版本
./gradlew --version
```

## 🔧 高级配置

### 1. 加速编译

#### 启用并行编译
```properties
# gradle.properties
org.gradle.parallel=true
org.gradle.daemon=true
org.gradle.configureondemand=true
org.gradle.caching=true
```

#### 使用 Gradle Build Cache
```bash
# 启用构建缓存
./gradlew build --build-cache
```

### 2. 自定义构建

#### 修改应用信息
```kotlin
// tv/build.gradle.kts
android {
    defaultConfig {
        applicationId = "your.package.name"
        versionCode = 100
        versionName = "1.0.0"
    }
}
```

#### 自定义签名
```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("your-keystore.jks")
            storePassword = "your-store-password"
            keyAlias = "your-key-alias"
            keyPassword = "your-key-password"
        }
    }
}
```

### 3. 持续集成

#### GitHub Actions 示例
```yaml
# .github/workflows/build.yml
name: Build APK
on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
    
    - name: Setup Android SDK
      uses: android-actions/setup-android@v3
    
    - name: Create local.properties
      run: echo "sdk.dir=$ANDROID_HOME" > local.properties
    
    - name: Build APK
      run: ./gradlew :tv:assembleOriginalRelease -x lint

    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: tv/build/outputs/apk/original/release/*.apk
```

## 📱 安装与调试

### ADB 安装
```bash
# 安装 APK 到设备
adb install tv/build/outputs/apk/debug/mytv-android-tv-*.apk

# 卸载应用
adb uninstall top.yogiczy.mytv.tv

# 查看日志
adb logcat | grep MyTV
```

### 无线调试
```bash
# 启用无线调试 (Android 11+)
adb pair ip:port
adb connect ip:port
```

## 📄 脚本化编译

创建便捷的编译脚本：

### build.sh (Linux/macOS)
```bash
#!/bin/bash

echo "🚀 开始编译 MyTV Android..."

# 检查环境
if [ ! -f "local.properties" ]; then
    echo "⚠️  local.properties 不存在，正在创建..."
    echo "sdk.dir=$ANDROID_HOME" > local.properties
fi

# 清理项目
echo "🧹 清理项目..."
./gradlew clean

# 编译 TV 版本
echo "📱 编译 TV 版本..."
./gradlew :tv:assembleOriginalRelease

# 检查编译结果
if [ $? -eq 0 ]; then
    echo "✅ 编译成功！"
    echo "📁 APK 位置: tv/build/outputs/apk/original/release/"
    ls -la tv/build/outputs/apk/original/release/*.apk
else
    echo "❌ 编译失败！"
    exit 1
fi
```

### build.bat (Windows)
```batch
@echo off
echo 🚀 开始编译 MyTV Android...

REM 检查环境
if not exist local.properties (
    echo ⚠️ local.properties 不存在，正在创建...
    echo sdk.dir=%ANDROID_HOME% > local.properties
)

REM 清理项目
echo 🧹 清理项目...
gradlew.bat clean

REM 编译 TV 版本
echo 📱 编译 TV 版本...
gradlew.bat :tv:assembleOriginalRelease

if %errorlevel% == 0 (
    echo ✅ 编译成功！
    echo 📁 APK 位置: tv\build\outputs\apk\original\release\
    dir tv\build\outputs\apk\original\release\*.apk
) else (
    echo ❌ 编译失败！
    exit /b 1
)
```

---

## 🎯 总结

MyTV Android 的编译过程包括：

1. **环境准备**: JDK 17+, Android SDK (API 21 & 35), Git
2. **项目配置**: 创建 `local.properties`, 配置 SDK 路径
3. **编译命令**: 使用 Gradle 编译对应模块
4. **问题排查**: 解决常见的环境和配置问题

编译完成后，你将得到可安装的 APK 文件，支持 Android 5.0+ 的设备。

*最后更新：2025-08-08*  
*适用版本：MyTV Android v3.3.9*