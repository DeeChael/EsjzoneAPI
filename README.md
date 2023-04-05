# EsjzoneAPI
通过jsoup解析网页内容制作的esjzone api

（吐槽一句Esjzone前后端不分离）

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