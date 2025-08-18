# 贡献指南

感谢您对 Structure Boot 项目的关注！我们欢迎所有形式的贡献，包括但不限于：

- 🐛 Bug 报告
- 💡 功能建议
- 📝 文档改进
- 🔧 代码贡献

## 📋 贡献前准备

### 1. 环境要求

- JDK 8+
- Maven 3.6+
- Git 2.0+
- IDE (推荐 IntelliJ IDEA 或 Eclipse)

### 2. 项目结构

```
structure-boot/
├── doc/                           # 文档目录
├── structure-common/              # 公共组件
├── structure-dependencies/        # 依赖管理
├── structure-*-starter/           # 各种启动器
├── structure-example/             # 示例项目
├── scripts/                       # 脚本文件
├── README.md                      # 项目说明
├── LICENSE                        # 开源协议
└── pom.xml                       # 父 POM
```

### 3. 开发规范

#### 代码规范

- 遵循 Java 编码规范
- 使用 4 个空格进行缩进
- 类名使用 PascalCase 命名
- 方法名和变量名使用 camelCase 命名
- 常量使用 UPPER_SNAKE_CASE 命名

#### 注释规范

- 类和方法必须有 JavaDoc 注释
- 复杂逻辑需要添加行内注释
- 注释使用中文，便于理解

#### 提交规范

提交信息格式：`<type>(<scope>): <subject>`

**Type 类型：**

- `feat`: 新功能
- `fix`: 修复 Bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

**Scope 范围：**

- `web`: Web 相关
- `mybatis`: MyBatis 相关
- `redis`: Redis 相关
- `minio`: MinIO 相关
- `log`: 日志相关
- `rpc`: RPC 相关

**示例：**

```
feat(web): 添加统一异常处理功能
fix(mybatis): 修复批量插入时的空指针异常
docs: 更新 README 文档
```

## 🚀 贡献步骤

### 1. Fork 项目

1. 访问 [Structure Boot GitHub 页面](https://github.com/your-username/structure-boot)
2. 点击右上角的 "Fork" 按钮
3. 选择要 Fork 到的目标账户

### 2. 克隆项目

```bash
# 克隆您 Fork 的项目
git clone https://github.com/your-username/structure-boot.git

# 添加上游仓库
cd structure-boot
git remote add upstream https://github.com/original-username/structure-boot.git
```

### 3. 创建分支

```bash
# 创建并切换到新分支
git checkout -b feature/AmazingFeature

# 或者创建 Bug 修复分支
git checkout -b fix/bug-description
```

### 4. 开发代码

1. 在本地进行代码开发
2. 确保代码符合项目规范
3. 添加必要的测试用例
4. 更新相关文档

### 5. 提交代码

```bash
# 添加修改的文件
git add .

# 提交代码
git commit -m "feat(web): 添加统一异常处理功能"

# 推送到远程分支
git push origin feature/AmazingFeature
```

### 6. 创建 Pull Request

1. 访问您 Fork 的项目页面
2. 点击 "Compare & pull request" 按钮
3. 填写 PR 标题和描述
4. 提交 PR

## 📝 Pull Request 规范

### PR 标题

使用与提交信息相同的格式：`<type>(<scope>): <subject>`

### PR 描述

请包含以下内容：

```markdown
## 变更说明

简要描述此次变更的内容和原因。

## 变更类型

- [ ] Bug 修复
- [ ] 新功能
- [ ] 文档更新
- [ ] 代码重构
- [ ] 性能优化
- [ ] 其他

## 测试说明

描述如何测试此次变更，包括测试用例和测试环境。

## 相关 Issue

关联相关的 Issue，如：Closes #123

## 检查清单

- [ ] 代码符合项目规范
- [ ] 添加了必要的测试用例
- [ ] 更新了相关文档
- [ ] 所有测试通过
- [ ] 代码审查通过
```

### PR 审查

- 每个 PR 需要至少一个维护者审查
- 审查通过后才会合并到主分支
- 如果审查不通过，请根据反馈进行修改

## 🐛 Bug 报告

### 报告格式

请在 GitHub Issues 中使用以下模板：

```markdown
## Bug 描述

详细描述 Bug 的表现和影响。

## 复现步骤

1. 步骤 1
2. 步骤 2
3. 步骤 3

## 期望行为

描述期望的正确行为。

## 实际行为

描述实际发生的错误行为。

## 环境信息

- 操作系统：
- Java 版本：
- Spring Boot 版本：
- Structure Boot 版本：
- 其他相关版本：

## 错误日志

如果有错误日志，请提供完整的错误信息。

## 其他信息

任何其他相关的信息或截图。
```

## 💡 功能建议

### 建议格式

```markdown
## 功能描述

详细描述您希望添加的功能。

## 使用场景

描述该功能的使用场景和解决的问题。

## 实现建议

如果有实现思路，请提供相关的建议。

## 优先级

- [ ] 高优先级
- [ ] 中优先级
- [ ] 低优先级
```

## 📚 文档贡献

### 文档类型

- API 文档
- 使用教程
- 最佳实践
- 常见问题
- 示例代码

### 文档规范

- 使用 Markdown 格式
- 中文文档使用中文标点符号
- 代码示例要完整可运行
- 图片和截图要清晰

## 🧪 测试贡献

### 测试类型

- 单元测试
- 集成测试
- 性能测试
- 兼容性测试

### 测试规范

- 测试覆盖率不低于 80%
- 测试用例要覆盖各种边界情况
- 测试代码要清晰易懂
- 测试结果要稳定可靠

## 🔧 构建和测试

### 本地构建

```bash
# 清理并编译
mvn clean compile

# 运行测试
mvn test

# 打包
mvn package

# 安装到本地仓库
mvn install
```

### 运行示例

```bash
# 运行基础示例
cd structure-example/structure-boot-example
mvn spring-boot:run

# 运行 Redis 示例
cd structure-example/structure-redis-example
mvn spring-boot:run
```

## 📞 获取帮助

### 交流渠道

- **GitHub Issues**: 用于 Bug 报告和功能建议
- **GitHub Discussions**: 用于技术讨论和问题咨询
- **邮件**: your-email@example.com

### 常见问题

1. **如何开始贡献？**

   - 从简单的文档改进开始
   - 修复已知的 Bug
   - 添加测试用例

2. **代码审查不通过怎么办？**

   - 仔细阅读审查意见
   - 根据反馈进行修改
   - 如有疑问，在 PR 中讨论

3. **如何保持分支同步？**
   ```bash
   git fetch upstream
   git checkout main
   git merge upstream/main
   git push origin main
   ```

## 🏆 贡献者名单

感谢以下贡献者的支持：

- [贡献者 1](https://github.com/contributor1)
- [贡献者 2](https://github.com/contributor2)
- [贡献者 3](https://github.com/contributor3)

## 📄 开源协议

本项目采用 [Apache License 2.0](LICENSE) 开源协议。贡献者提交的代码将自动采用相同的协议。

## 🙏 致谢

感谢所有为 Structure Boot 项目做出贡献的开发者！

---

**让我们一起让 Structure Boot 变得更好！** 🚀
