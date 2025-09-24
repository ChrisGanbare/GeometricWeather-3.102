# GeometricWeather 简化版说明

## 概述

本项目是 [GeometricWeather](https://github.com/WangDaYeeeeee/GeometricWeather) 的简化版本，专为 Android 开发新手设计，目的是提供一个可快速上手、最小可运行的版本。

## 精简原则

1. **移除第三方闭源 SDK**：
   - 删除了高德地图 (AMap) 定位服务
   - 删除了百度地图 (Baidu) 定位服务
   - 删除了 Bugly 崩溃报告服务
   - 只保留 Android 原生定位服务和基于 IP 的百度定位服务

2. **简化构建配置**：
   - 移除了复杂的 productFlavors，只保留一个 basic flavor
   - 使用占位符填充所有必需的 BuildConfig 字段，避免因缺少密钥导致同步失败
   - 保持项目结构不变，便于新手直接在 Android Studio 中同步和运行

3. **保留核心功能**：
   - 保留了天气显示、预报等核心功能
   - 保留了基本的 UI 和设置功能
   - 保留了必要的开源库依赖

## 主要改动

### 1. 构建配置 (app/build.gradle)
- 移除了 amap、bugly、noamap、nobugly、proprietary 等 flavor
- 简化了 sourceSets 配置，只保留 main 和 opensource
- 移除了对专有 SDK 的依赖
- 移除了 GreenDao 数据库依赖
- 移除了部分 JitPack 依赖

### 2. 配置文件 (gradle.properties)
- 使用占位符替换所有 API 密钥，避免同步错误

### 3. 代码改动
- 修改 LocationHelper，移除对专有定位服务的引用
- 更新数组资源，移除对专有定位服务的引用
- 移除了对 BubbleSeekBar 的引用，改用标准 SeekBar
- 移除了部分 Hilt 注解的 Worker 类

### 4. 文件删除
- 删除了 amap、bugly、noamap、nobugly、proprietary 等目录

## 如何使用

1. 克隆或下载本项目
2. 在 Android Studio 中打开项目
3. 等待 Gradle 同步完成
4. 连接 Android 设备或启动模拟器
5. 点击 Run 按钮编译并运行应用

## 环境要求

要成功构建和运行此项目，您需要：

1. Android Studio (推荐 Electric Eel 或更新版本)
2. Android SDK (在 Android Studio 安装时会自动安装)
3. JDK 8 或更高版本 (Android Studio 会自带)

## 构建验证

在项目根目录下执行以下命令来验证构建：

```bash
# 清理项目
./gradlew clean

# 编译 debug 版本
./gradlew assembleBasicDebug
```

如果构建成功，APK 文件将生成在 `app/build/outputs/apk/basic/debug/` 目录下。

## 常见问题

### SDK 位置未找到错误
如果遇到 "SDK location not found" 错误，请确保：
1. 已安装 Android Studio 和 Android SDK
2. 设置 ANDROID_HOME 环境变量指向 SDK 路径
3. 或在项目根目录创建 local.properties 文件，并添加：
   ```
   sdk.dir=/path/to/your/android/sdk
   ```

### API 密钥占位符
所有必需的 API 密钥已在 gradle.properties 中使用占位符设置，不会影响基本功能的运行和测试。

### 构建失败
如果构建失败，请尝试以下解决方案：
1. 确保已安装 Android SDK 32 (Android 12L)
2. 确保已安装必要的构建工具
3. 清理项目并重新构建：`./gradlew clean && ./gradlew assembleBasicDebug`

## 注意事项

- 此简化版仅用于学习和入门目的
- 生产环境建议使用完整版本并配置合适的定位服务
- 如需使用专有定位服务，请参考原项目配置
- 由于移除了数据库依赖，部分功能可能受限

## 许可证

本项目遵循原 GeometricWeather 项目的许可证。