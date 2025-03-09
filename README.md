# MyNews 新闻管理系统

## 项目简介

这是一个基于Spring Boot的新闻管理系统,提供新闻的增删改查功能以及用户管理功能。系统采用RESTful API设计,使用MySQL存储数据,Redis缓存,实现了完整的异常处理、参数校验和性能优化。

## 技术栈

- Spring Boot 3.2.3
- MyBatis 3.0.4
- MySQL 8.0
- Redis
- Maven
- Lombok
- Java 17

## 系统架构

## 项目结构
```
src/main/java/com/example/mynews/
├── controller // 控制器层,处理HTTP请求
│ ├── NewsController // 新闻相关接口
│ └── UserController // 用户相关接口
├── service // 业务逻辑层
│ ├── NewsService // 新闻服务接口
│ └── UserService // 用户服务接口
├── mapper // 数据访问层
│ ├── NewsMapper // 新闻数据库操作
│ └── UsersMapper // 用户数据库操作
├── pojo // 数据对象
│ ├── dto // 数据传输对象
│ ├── vo // 视图对象
│ └── entity // 实体类
├── config // 配置类
│ ├── RedisConfig // Redis配置
│ └── WebConfig // Web配置
├── exception // 异常处理
├── util // 工具类
└── response // 响应封装
```

## API文档

### 新闻管理接口

#### 1. 获取新闻详情
- 请求方式: GET
- 请求路径: `/api/news/{id}`
- 参数说明:
  - id: 新闻ID(必填,大于0的整数)
- 返回示例:
```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        "id": 1,
        "title": "示例新闻",
        "contentTxt": "新闻详细内容",
        "simpleTxt": "新闻简要内容",
        "source": "新闻来源",
        "date": "2024-03-20 12:00:00"
    }
}
```

#### 2. 根据标题查询新闻
- 请求方式: GET
- 请求路径: `/api/news/title/{title}`
- 参数说明:
  - title: 新闻标题(必填)
- 返回格式同上

#### 3. 添加新闻
- 请求方式: POST
- 请求路径: `/api/news/insert`
- 请求体:
```json
{
    "title": "新闻标题",
    "contentTxt": "新闻内容",
    "source": "新闻来源",
    "date": "2024-03-20 12:00:00"
}
```
- 返回格式同上

#### 4. 添加简要新闻
- 请求方式: POST
- 请求路径: `/api/news/insertSimple`
- 请求体:
```json
{
    "title": "新闻标题",
    "simpleTxt": "新闻简要内容"
}
```

### 用户管理接口

#### 1. 用户登录
- 请求方式: POST
- 请求路径: `/api/users/login`
- 请求体:
```json
{
    "account": 123456,
    "password": "password"
}
```

#### 2. 用户注册
- 请求方式: POST
- 请求路径: `/api/users/reg`
- 请求体:
```json
{
    "account": 123456,
    "password": "password",
    "username": "用户名"
}
```

#### 3. 修改用户信息
- 请求方式: POST
- 请求路径: `/api/users/updateUser`
- 请求体:
```json
{
    "account": 123456,
    "username": "新用户名",
    "password": "新密码"
}
```

## Redis 缓存配置说明

### 1. 配置参数
Redis服务器配置位于`application.yml`：
```yaml
spring:
  redis:
    host: localhost           # Redis服务器地址
    port: 6379               # Redis服务器端口
    database: 0              # 使用的数据库索引，默认是0
    password:                # Redis服务器密码，如果没有设置可以为空
    timeout: 10000          # 连接超时时间（毫秒）
    lettuce:                # 使用lettuce连接池
      pool:
        max-active: 8       # 连接池最大连接数
        max-wait: -1        # 连接池最大阻塞等待时间（使用负值表示没有限制）
        max-idle: 8         # 连接池中的最大空闲连接
        min-idle: 0         # 连接池中的最小空闲连接
```

### 2. 缓存操作工具类
项目提供了`RedisUtil`工具类，封装了常用的Redis操作：

- 设置缓存：`set(String key, String value, long timeout, TimeUnit unit)`
- 获取缓存：`get(String key)`
- 删除缓存：`delete(String key)`
- 判断key是否存在：`hasKey(String key)`
- 设置过期时间：`expire(String key, long timeout, TimeUnit unit)`

### 3. 健康检查
系统通过`RedisHealthIndicator`提供Redis服务的健康检查功能：
- 访问路径：`/actuator/health`
- 检查内容：Redis连接状态
- 响应示例：
```json
{
    "status": "UP",
    "components": {
        "redis": {
            "status": "UP",
            "details": {
                "redis": "Redis is running"
            }
        }
    }
}
```

### 4. 异常处理
Redis操作异常码定义：
- 4000: Redis服务异常
- 4001: Redis连接失败
- 4002: Redis操作失败

### 5. 使用示例
```java
@Autowired
private RedisUtil redisUtil;

// 存储数据（5分钟过期）
redisUtil.set("user:token", tokenValue, 5, TimeUnit.MINUTES);

// 获取数据
String token = redisUtil.get("user:token");

// 删除数据
redisUtil.delete("user:token");
```

### 6. 注意事项
1. 确保Redis服务器已启动并可访问
2. 合理设置过期时间，避免缓存积压
3. 使用合适的key命名规范，建议使用冒号分隔不同层级
4. 注意捕获和处理Redis异常
5. 定期监控Redis健康状态

### 7. 缓存策略
1. 热点数据缓存：
   - 频繁访问的新闻列表
   - 用户会话信息
   - 系统配置信息

2. 防重复提交：
   - 表单提交标记
   - 接口调用计数

3. URL去重：
   - 爬虫已访问的URL存储
   - 设置7天过期时间

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 请求参数错误 |
| 401 | 未经授权 |
| 403 | 访问被拒绝 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
| 1000 | 参数错误 |
| 2000 | 用户不存在 |
| 2001 | 用户名已存在 |
| 2002 | 密码错误 |
| 3000 | 新闻不存在 |
| 3001 | 新闻已存在 |

## 本地开发

1. 克隆项目
```bash
git clone <project-url>
```

2. 配置数据库
- 创建MySQL数据库mynews
- 修改application.yml中的数据库连接信息

3. 运行项目
```bash
mvn spring-boot:run
```

## 注意事项

1. 所有接口返回统一格式的JSON数据
2. 请求参数校验失败会返回400错误码
3. 用户密码传输时建议进行加密
4. API访问需要进行登录认证(除登录注册接口外)

## 待优化项

1. 添加新闻分页查询功能
2. 实现新闻的删除和更新接口
3. 添加新闻的缓存机制
4. 完善单元测试
5. 添加新闻的审核流程
6. 优化异常处理机制
7. 增加接口访问频率限制
8. 添加数据库索引优化查询性能

## 贡献指南

1. Fork 本仓库
2. 创建新的分支
3. 提交代码
4. 创建 Pull Request

## 许可证

[MIT License](LICENSE)

