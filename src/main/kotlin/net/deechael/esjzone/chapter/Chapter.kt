package net.deechael.esjzone.chapter

import net.deechael.esjzone.EsjzoneClient
import net.deechael.esjzone.novel.Novel
import okhttp3.internal.toImmutableList
import us.codecraft.xsoup.Xsoup

/**
 * 小说章节
 *
 * @param client Esjzone客户端
 * @param novel 章节所属小说
 * @param id 章节id
 * @param name 章节名称
 */
class Chapter(private val client: EsjzoneClient, val novel: Novel, val id: String, val name: String) {

    var contents: List<ChapterContent>? = null
        private set
        get() {
            if (field == null)
                field = listOf()
            if (field!!.isEmpty()) {
                val document = this.client.service.getChapterDetail(this.novel.id, this.id).execute().body()!!
                val chapterContents = mutableListOf<ChapterContent>()
                for (rawContent in Xsoup.select(document, "/html/body/div[3]/section/div/div[1]/div[3]")
                    .elements[0].allElements) {
                    if (rawContent.getElementsByTag("br").size > 0) {
                        chapterContents.add(BreakLineChapterContent())
                    } else if (rawContent.getElementsByTag("img").size > 0) {
                        chapterContents.add(ImageChapterContent(rawContent.getElementsByTag("img")[0].attr("src")))
                    } else {
                        chapterContents.add(TextChapterContent(rawContent.text()))
                    }
                }
                field = chapterContents.toImmutableList()
            }
            return field
        }

}

interface ChapterContent {

}

class TextChapterContent(val text: String) : ChapterContent {

    override fun toString(): String {
        return this.text
    }

}

class BreakLineChapterContent : ChapterContent {

    override fun toString(): String {
        return "\n"
    }

}

class ImageChapterContent(val url: String) : ChapterContent {

    override fun toString(): String {
        return this.url
    }

}