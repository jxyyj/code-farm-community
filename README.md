# Code Farm Community

## 项目简介
Code Farm Community 是一个基于 Spring Boot 4.0.3 和 Vue 3 的全栈社区项目，提供了完整的用户认证、权限管理、用户管理、角色管理、权限管理等功能，以及美观的前端界面。

## 技术栈

### 后端技术
- **Spring Boot 4.0.3** - 应用框架
- **Spring Security 4.0.3** - 安全框架
- **MyBatis-Plus 3.5.16** - ORM 框架
- **MySQL 8.4.8** - 数据库
- **JWT 0.13.0** - 身份认证
- **Druid 1.2.28** - 数据库连接池
- **AOP 3.5.11** - 面向切面编程
- **Log4j2 2.25.3** - 日志框架

### 前端技术
- **Vue 3** - 前端框架
- **Vite** - 构建工具
- **Pinia** - 状态管理
- **Vue Router** - 路由管理
- **Element Plus** - UI 组件库
- **Axios** - HTTP 客户端

### API 文档
- **SpringDoc OpenAPI 3.0.2** - API 文档生成
- **Knife4j 4.5.0** - API 文档增强

### 开发工具
- **Maven 3.6+** - 后端依赖管理
- **npm 10+** - 前端依赖管理
- **Lombok** - 代码简化

## 快速开始

### 环境要求
- JDK 25+
- Maven 3.9.11
- MySQL 8.4.8
- Node.js 25+
- npm 10+

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/jxyyj/code-farm-community.git
   cd code-farm-community
   ```

2. **配置数据库**
   - 创建数据库 `code_farm_community`
   - 修改 `application.yml` 中的数据库配置

3. **运行后端项目**
   ```bash
   # 安装依赖
   mvn clean install
   
   # 运行项目
   mvn spring-boot:run
   ```

4. **运行前端项目**
   ```bash
   # 进入前端目录
   cd frontend
   
   # 安装依赖
   npm install
   
   # 运行项目
   npm run dev
   ```

## 项目结构

```
code-farm-community/
├── src/                 # 后端代码
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
├── frontend/            # 前端代码
│   ├── public/          # 静态资源
│   ├── src/
│   │   ├── assets/      # 资源文件
│   │   ├── components/  # 组件
│   │   ├── router/      # 路由
│   │   ├── services/    # API 服务
│   │   ├── stores/      # 状态管理
│   │   ├── views/       # 页面
│   │   ├── App.vue      # 根组件
│   │   └── main.js      # 入口文件
│   ├── index.html       # HTML 模板
│   ├── package.json     # 前端依赖
│   └── vite.config.js   # Vite 配置
├── pom.xml             # Maven 配置
└── README.md           # 项目说明
```

## 核心功能

### 后端功能
1. **用户认证**
   - JWT 令牌生成与验证
   - Spring Security 权限控制

2. **用户管理**
   - 用户列表查询
   - 用户新增、编辑、删除
   - 用户角色分配

3. **角色管理**
   - 角色列表查询
   - 角色新增、编辑、删除
   - 角色权限分配

4. **权限管理**
   - 权限列表查询
   - 权限新增、编辑、删除

5. **统计功能**
   - 用户总数统计
   - 角色总数统计
   - 权限总数统计

6. **密码管理**
   - 密码修改功能

### 前端功能
1. **登录与注册**
   - 登录界面
   - 注册界面
   - 表单验证

2. **管理控制台**
   - 仪表盘
   - 用户管理
   - 角色管理
   - 权限管理

3. **个人中心**
   - 个人信息查看与修改
   - 密码修改

4. **响应式设计**
   - 适配不同屏幕尺寸
   - 现代化 UI 设计

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

### 前端 API 代理配置
在 `frontend/vite.config.js` 中配置：

```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true,
    rewrite: (path) => path.replace(/^\/api/, '')
  }
}
```

## API 文档访问

项目启动后，可以通过以下地址访问 API 文档：

- **SpringDoc OpenAPI**：`http://localhost:8080/v3/api-docs`
- **Swagger UI**：`http://127.0.0.1:8080/swagger-ui/index.html`

## 前端访问

前端项目启动后，可以通过以下地址访问：

- **前端应用**：`http://localhost:5173`

## 许可证

本项目采用 MIT 许可证。详见 LICENSE 文件。
