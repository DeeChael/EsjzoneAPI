# EsjzoneAPI
通过jsoup解析网页内容制作的esjzone api

（吐槽一句Esjzone前后端不分离）

## 开发计划
- 优化 EsjzoneClient的结构
- 将网页版Esjzone大部分功能实现
- - 列出评论
  - 发送评论
  - 回复评论
  - 添加/删除收藏
  - 内容相关的部分添加更多的支持（比如 **加粗** *斜体* ~~删除线~~ 等）

## 项目依赖
Okhttp3\
Retrofit2\
Jsoup\
Xsoup\
Gson

## 快速入门
```java
// 登录方法 1 - Cookie 缓存
EsjzoneClient client = EsjzoneClientBuilder.of()
        .key("这里放置你的wsKey")
        .token("这里放置你的wsToken")
        .build();
// 登录方法 2 - 账户 + 密码
EsjzoneClient client = EsjzoneLoginer.of()
        .login("123456@deechael.net", "password123");
```
获取用户信息
```java
int uid = 12345
User user = client.getUserInfo(uid);
```