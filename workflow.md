# GitHub Actions 工作流文档

本项目配置了完整的CI/CD工作流，包含自动化构建、质量检查和发布流程。

## 📋 工作流概览

项目包含3个主要的GitHub Actions工作流，位于`.github/workflows/`目录：

| 工作流 | 文件名 | 触发条件 | 主要功能 |
|--------|--------|----------|----------|
| 主构建流水线 | `build.yml` | 推送到主要分支、PR | 多模块矩阵构建、测试 |
| 质量检查 | `pr-check.yml` | Pull Request | 代码质量检查、测试报告 |
| 发布流程 | `release.yml` | 标签推送、手动触发 | 自动发布APK到GitHub Releases |

## 🔧 工作流详细说明

### 1. 主构建流水线 (`build.yml`)

**触发条件**：
- 推送到 `main`、`develop`、`feature/*` 分支
- Pull Request 到 `main`、`develop` 分支
- 手动触发 (`workflow_dispatch`)

**构建矩阵**：
- **模块**: `tv`, `mobile`, `allinone`
- **构建类型**: `debug`, `release`
- **总计**: 6个并行构建任务

**主要步骤**：
1. **环境准备**：JDK 17 + Android SDK
2. **依赖缓存**：Gradle缓存优化构建速度
3. **签名配置**：自动生成调试签名
4. **并行构建**：
   - Debug版本：`./gradlew :MODULE:assembleDebug`
   - Release版本：`./gradlew :MODULE:assembleOriginalRelease`
5. **产物上传**：APK文件保留30-90天
6. **质量检查**：单元测试、Lint检查

### 2. PR质量检查 (`pr-check.yml`)

**触发条件**：
- PR打开、同步、重新打开时触发
- 目标分支：`main`, `develop`

**检查项目**：
- **Kotlin静态分析**：Detekt代码检查
- **Android Lint**：官方代码质量检查
- **单元测试**：`testDebugUnitTest`
- **测试覆盖率**：Jacoco报告生成
- **依赖检查**：依赖更新和安全检查

**智能报告**：
- 自动在PR中评论代码质量报告
- 显示Lint问题数量和测试结果
- 上传详细报告到工作流产物

**构建验证**：
- 矩阵构建验证各模块编译正常
- APK大小监控和报告
- 依赖冲突检测

### 3. 自动发布流程 (`release.yml`)

**触发方式**：
- **自动触发**：推送 `v*` 格式的标签（如 `v1.0.0`）
- **手动触发**：通过GitHub界面手动启动，输入版本号

**发布步骤**：
1. **签名配置**：
   - 优先使用Release签名（从GitHub Secrets获取）
   - 回退到调试签名（确保始终能构建成功）

2. **全量构建**：
   ```bash
   ./gradlew :tv:assembleOriginalRelease -x lint
   ./gradlew :mobile:assembleOriginalRelease -x lint  
   ./gradlew :allinone:assembleOriginalRelease -x lint
   ```

3. **文件重命名**：
   - `mytv-android-tv-{VERSION}-*`
   - `mytv-android-mobile-{VERSION}-*`
   - `mytv-android-allinone-{VERSION}-*`

4. **自动生成变更日志**：
   - 基于Git提交历史自动生成
   - 对比上一个标签的变更内容

5. **GitHub Release创建**：
   - 自动创建Release页面
   - 上传所有APK文件
   - 包含中文安装说明和系统要求

## 🛡️ 安全和配置

### GitHub Secrets配置

为了支持正式发布，需要在仓库设置中配置以下Secrets：

| Secret名称 | 用途 | 是否必需 |
|------------|------|----------|
| `KEYSTORE_BASE64` | Release签名文件(base64编码) | 可选 |
| `KEYSTORE_PASSWORD` | 签名文件密码 | 可选 |
| `KEY_ALIAS` | 签名密钥别名 | 可选 |
| `KEY_PASSWORD` | 签名密钥密码 | 可选 |

> **注意**：如果未配置Release签名，系统会自动使用调试签名确保构建成功。

### 构建优化

**缓存策略**：
- Gradle依赖缓存：`~/.gradle/caches`
- Gradle Wrapper缓存：`~/.gradle/wrapper`
- 缓存键基于`gradle.properties`和构建脚本内容

**性能优化**：
- 跳过Lint检查加速Release构建（`-x lint`）
- 并行构建不同模块
- 智能依赖解析和缓存

## 📱 支持的构建产物

### TV模块（主要版本）
- **目标平台**：Android TV、机顶盒
- **特性**：遥控器按键支持、全屏体验
- **文件大小**：约19MB

### Mobile模块
- **目标平台**：手机、平板
- **特性**：触摸优化、便携体验

### AllinOne模块
- **目标平台**：通用版本
- **特性**：包含所有功能的完整版本

### 技术规格
- **最低版本**：Android 5.0 (API Level 21)
- **目标版本**：Android 14 (API Level 35)
- **支持架构**：armeabi-v7a, arm64-v8a
- **编译工具**：JDK 17, Gradle 8.9

## 🎯 使用指南

### 开发流程
1. **创建功能分支**：`git checkout -b feature/new-feature`
2. **推送代码**：触发自动构建检查
3. **创建PR**：触发质量检查和构建验证
4. **合并后**：自动触发主构建流水线

### 发布流程
1. **创建标签**：`git tag v1.0.0 && git push origin v1.0.0`
2. **自动构建**：工作流自动构建所有版本
3. **自动发布**：APK自动上传到GitHub Releases
4. **下载使用**：用户可从Releases页面下载

### 手动发布
1. 进入GitHub项目的Actions页面
2. 选择"Release MyTV Android"工作流
3. 点击"Run workflow"按钮
4. 输入版本号（如：v1.0.0）
5. 点击运行

## 📊 监控和维护

### 构建状态监控
- 所有工作流支持实时状态查看
- 构建失败会自动发送通知
- 详细日志便于问题排查

### 产物管理
- Debug APK保留30天
- Release APK保留90天
- GitHub Release永久保存
- 支持手动下载和分发

### 持续优化
- 定期更新依赖版本
- 优化构建速度和缓存策略
- 根据用户反馈调整发布流程

---

*最后更新：2025-08-08*  
*适用版本：MyTV Android v3.3.9+*