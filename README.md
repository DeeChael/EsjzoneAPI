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
```kotlin
// 登录方法 1 - Cookie 缓存
val client: EsjzoneClient = EsjzoneClientBuilder.of()
        .key("这里放置你的wsKey")
        .token("这里放置你的wsToken")
        .build()
// 登录方法 2 - 账户 + 密码
val client: EsjzoneClient = EsjzoneLoginer.of()
        .login("123456@deechael.net", "password123")
```
获取用户信息
```kotlin
val uid: Int = 12345
val user: User = client.getUserInfo(uid)
```
获取所有的分类，并获取分类下所有的小说
```java
val categories: List<Category> = client.getCategories()
for (category in categories) {
        val novels: List<Novel> = category.listNovels()
}
```
获取小说的介绍、章节
```kotlin
val novel: Novel = ...
val description: NovelDescription = novel.description
for (line in description.descriptionLine) {
    if (line is TextDescriptionLine) {
        println(line.text)
    } else if (line is ImageDescriptionLine) {
        println(line.src)
    }   
}
val chapters: List<Chapter> = novel.listChapters()
```
获取小说章节的内容
```kotlin
val chapter: Chapter = ...
for (content in chapter.contents) {
    if (content is TextChapterContent) {
        println(content.text)
    } else if (content is ImageChapterContent) {
        println(content.url)
    }
}
```