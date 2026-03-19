# Code Farm Community

## 项目简介
Code Farm Community 是一个基于 Spring Boot 4.0.3 的社区项目，提供了完整的用户认证、权限管理、API 文档等功能。

## 技术栈

### 核心技术
- **Spring Boot 4.0.3** - 应用框架
- **Spring Security 4.0.3** - 安全框架
- **MyBatis-Plus 3.5.16** - ORM 框架
- **MySQL 8.4.8** - 数据库
- **JWT 0.13.0** - 身份认证
- **Druid 1.2.28** - 数据库连接池
- **AOP 3.5.11** - 面向切面编程
- **Log4j2 2.25.3** - 日志框架

### API 文档
- **SpringDoc OpenAPI 3.0.2** - API 文档生成
- **Knife4j 4.5.0** - API 文档增强

### 开发工具
- **Maven 3.6+** - 依赖管理
- **Lombok** - 代码简化

## 快速开始

### 环境要求
- JDK 25+
- Maven 3.9.11
- MySQL 8.4.8

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd code-farm-community
   ```

2. **配置数据库**
   - 创建数据库 `code_farm_community`
   - 修改 `application.yml` 中的数据库配置

3. **安装依赖**
   ```bash
   mvn clean install
   ```

4. **运行项目**
   ```bash
   mvn spring-boot:run
   ```

## 项目结构

```
code-farm-community/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/yyj/codefarmcommunity/
│   │   │       ├── controller/     # 控制器
│   │   │       ├── service/        # 服务层
│   │   │       ├── mapper/         # 数据访问层
│   │   │       ├── entity/         # 实体类
│   │   │       ├── config/         # 配置类
│   │   │       ├── security/       # 安全相关
│   │   │       └── CodeFarmCommunityApplication.java  # 应用入口
│   │   └── resources/
│   │       ├── application.yml     # 应用配置
│   │       └── static/             # 静态资源
│   └── test/                       # 测试代码
├── pom.xml                         # Maven 配置
└── README.md                       # 项目说明
```

## 核心功能

1. **用户认证**
   - JWT 令牌生成与验证
   - Spring Security 权限控制

2. **API 文档**
   - 自动生成 RESTful API 文档
   - 可视化 API 调试界面

3. **数据库操作**
   - MyBatis-Plus 增强 ORM
   - Druid 连接池优化

4. **日志管理**
   - Log4j2 日志框架
   - 统一日志格式

5. **面向切面编程**
   - AOP 实现横切关注点

## 配置说明

### 数据库配置
在 `application.yml` 中配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/code_farm_community?useSSL=false&serverTimezone=UTC
    username: ****
    password: ****
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## 依赖说明

### 核心依赖
- `spring-boot-starter-security` - 安全框架
- `spring-boot-starter-webmvc` - Web 框架
- `mybatis-plus-spring-boot4-starter` - ORM 框架
- `mysql-connector-j` - MySQL 驱动
- `jjwt` - JWT 库
- `druid-spring-boot-starter` - 数据库连接池
- `spring-boot-starter-aop` - 面向切面编程
- `spring-boot-starter-log4j2` - 日志框架

### 文档依赖
- `springdoc-openapi-starter-webmvc-ui` - API 文档生成
- `knife4j-openapi3-jakarta-spring-boot-starter` - API 文档增强

## API 文档访问

项目启动后，可以通过以下地址访问 API 文档：

- **SpringDoc OpenAPI**：`http://localhost:8080/v3/api-docs`
- **Knife4j UI**：`http://localhost:8080/doc.html`

## 许可证

本项目采用 MIT 许可证。详见 LICENSE 文件。
