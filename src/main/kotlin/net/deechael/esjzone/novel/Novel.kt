package net.deechael.esjzone.novel

import net.deechael.esjzone.EsjzoneClient
import net.deechael.esjzone.chapter.Chapter
import net.deechael.esjzone.comment.Comment
import net.deechael.esjzone.tag.Tag
import okhttp3.internal.toImmutableList
import us.codecraft.xsoup.Xsoup

/**
 * 小说
 *
 * @param client Esjzone客户端
 * @param id 小说id
 * @param name 小说名称
 */
class Novel(private val client: EsjzoneClient, val id: String, val name: String, cover: String? = null) {

    var cover: String? = null
        private set
        get() {
            if (field == null) {
                val desc = this.description
            }
            return field
        }

    var tags: List<Tag>? = null
        private set
        get() {
            if (field == null) {
                val desc = this.description
            }
            return field
        }

    var views: Int? = null
        private set
        get() {
            if (field == null) {
                val desc = this.description
            }
            return field
        }

    var favorites: Int? = null
        private set
        get() {
            if (field == null) {
                val desc = this.description
            }
            return field
        }

    var words: Int? = null
        private set
        get() {
            if (field == null) {
                val desc = this.description
            }
            return field
        }

    var description: NovelDescription? = null
        private set
        get() {
            if (field == null) {
                val document = this.client.service.getNovelDetail(this.id).execute().body()!!
                val elements = Xsoup.select(document, "/html/body/div[3]/section/div/div[1]/div[2]/div/div/div/p").elements
                val rawList = mutableListOf<DescriptionLine>()
                for (element in elements) {
                    val strongs = element.getElementsByTag("strong")
                    if (strongs.size > 0) {
                        rawList.add(TextDescriptionLine(strongs[0].text(), true))
                        continue
                    }
                    val imgs = element.getElementsByTag("imgs")
                    if (imgs.size > 0) {
                        rawList.add(ImageDescriptionLine(imgs[0].attr("src")))
                        continue
                    }
                    rawList.add(TextDescriptionLine(element.text(), false))
                }
                var cover = Xsoup.select(document, "/html/body/div[3]/section/div/div[1]/div[1]/div[1]/div[1]/a/img/@src").list().getOrNull(0)
                if (cover == null)
                    cover = ""
                this.cover = cover
                if (this.tags == null) {
                    val tags = mutableListOf<Tag>()
                    for (tag in Xsoup.select(document, "/html/body/div[3]/section/div/div[2]/section/a/text()").list()) {
                        tags.add(Tag(this.client, tag))
                    }
                    this.tags = tags.toImmutableList()
                }
                this.views = Xsoup.select(document, "/html/body/div[3]/section/div/div[1]/div[1]/div[2]/span/label[1]/span").get().toInt()
                this.favorites = Xsoup.select(document, "/html/body/div[3]/section/div/div[1]/div[1]/div[2]/span/label[2]/span").get().toInt()
                this.words = Xsoup.select(document, "/html/body/div[3]/section/div/div[1]/div[1]/div[2]/span/label[3]/span").get().replace(",", "").toInt()
                field = NovelDescription(rawList.toImmutableList())
            }
            return field
        }

    init {
        this.cover = cover
    }

    /**
     * 列出该小说的所有章节
     * @return 该小说的所有章节
     */
    fun listChapters(): List<Chapter> {
        val chapters = mutableListOf<Chapter>()
        val document = this.client.service.getNovelDetail(this.id).execute().body()!!
        val rawDetailedChapters = Xsoup.select(document, "/html/body/div/section/div/div/div/div/div/div/div/detail/a").elements
        val rawChapters = Xsoup.select(document, "/html/body/div/section/div/div/div/div/div/div/div/a").elements
        for (rawDetailedChapter in rawDetailedChapters) {
            val rawUrl = rawDetailedChapter.attr("href")
            chapters.add(Chapter(this.client, this, rawUrl.substring("https://www.esjzone.cc/forum/${this.id}/".length, rawUrl.length - 4), rawDetailedChapter.attr("data-title")))
        }
        for (rawChapter in rawChapters) {
            val rawUrl = rawChapter.attr("href")
            chapters.add(Chapter(this.client, this, rawUrl.substring("https://www.esjzone.cc/forum/${this.id}/".length, rawUrl.length - 4), rawChapter.attr("data-title")))
        }
        return chapters.toImmutableList()
    }

    fun comment(content: String) {
        this.client.service.getAuthToken("detail/${this.id}.html")
        this.client.service.createComment(content)
    }

    fun listComments(): List<Comment> {
        val comments = mutableListOf<Comment>()
        val document = this.client.service.getNovelDetail(this.id).execute().body()!!
        for (commentElement in Xsoup.select(document, "/html/body/div[3]/section/div/div[1]/section[39]/div[1]").elements) {
            val id = commentElement.attr("id").substring(8)
            val senderId = commentElement.getElementById("comment-author-ava")!!.getElementsByTag("a")[0].attr("href").substring(16).toInt()
            comments.add(Comment(this.client, this, null, id, senderId, commentElement.getElementById("comment-text ")!!.text()))
        }
        return comments.toImmutableList()
    }

}

class NovelDescription(val descriptionLines: List<DescriptionLine>) {

}

interface DescriptionLine {

}

class TextDescriptionLine(val text: String, val strong: Boolean): DescriptionLine {

}

class ImageDescriptionLine(val src: String): DescriptionLine {

}