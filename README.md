# HyperGMS

HyperOS 控制中心快捷管理 Google 基础服务的工具，通过磁贴一键查看状态、跳转设置，无需进入层层菜单。

## 功能

- **磁贴状态显示** — 磁贴亮起表示 GMS 已启用，灰暗表示已关闭
- **一键跳转** — 点击磁贴直接打开 Google 基础服务设置页（HyperOS 3：安全中心 → GMS 设置）
- **零界面** — 无桌面图标，纯磁贴交互，简洁无打扰

## 支持

| 系统 | 版本 |
|------|------|
| HyperOS 1/2/3 | ✅ 完整支持 |
| MIUI | ✅ 降级到账号同步页/降级到账号同步页 |

## 技术规格

| 项 | 值 |
|----|-----|
| minSdk | 34 (Android 14) |
| targetSdk | 37 (Android 16) |
| 语言 | Kotlin |
| 架构 | TileService（非 Active 模式） |
| 包大小 | ~75 KB |

## 构建

```bash
./gradlew assembleRelease
```

输出：`app/build/outputs/apk/release/app-release.apk`

签名需自行配置 `signingConfigs`（见 `app/build.gradle.kts`）。

## 使用

1. 安装 APK
2. 下拉控制中心 → 编辑 → 找到「GMS」磁贴 → 添加到面板
3. 磁贴亮 = GMS 开启，磁贴暗 = GMS 关闭
4. 点击磁贴跳转到 GMS 设置页面

## License

MIT
