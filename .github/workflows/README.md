# Maven Central 发布配置说明

## GitHub Secrets 配置

在 GitHub 仓库的 Settings > Secrets and variables > Actions 中配置以下 secrets：

### 必需的环境变量：
- `OSSRH_USERNAME`: Sonatype OSSRH 用户名
- `OSSRH_PASSWORD`: Sonatype OSSRH 密码
- `GPG_PRIVATE_KEY`: GPG 私钥（ASCII-armored 格式）
- `GPG_PASSPHRASE`: GPG 私钥密码

### 获取 GPG 私钥：
```bash
gpg --list-secret-keys
gpg --export-secret-keys --armor YOUR_KEY_ID > private.key
cat private.key | pbcopy  # 复制到剪贴板，粘贴到 GitHub Secret
```

## 本地发布使用 release.sh

```bash
# 设置环境变量
export OSSRH_USERNAME=your_username
export OSSRH_PASSWORD=your_password
export GPG_PASSPHRASE=your_gpg_passphrase

# 运行发布脚本
./scripts/release.sh 1.2.4
```

## GitHub Actions 自动发布

1. 创建新的 GitHub Release，触发自动发布
2. 或者手动触发工作流：
   - 进入 Actions > Release to Maven Central
   - 点击 "Run workflow"
   - 输入版本号（如：1.2.4）

## Maven 配置验证

确保以下配置正确：
- `structure-dependencies/pom.xml` 中的 `distributionManagement` 已配置
- `release` profile 中的 `nexus-staging-maven-plugin` 已配置
- GPG 签名插件已配置

## 发布顺序

发布将按以下顺序进行：
1. structure-dependencies（依赖管理）
2. structure-common（公共工具类）
3. structure-boot-parent（父项目）