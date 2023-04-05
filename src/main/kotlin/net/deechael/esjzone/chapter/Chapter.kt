package net.deechael.esjzone.chapter

import net.deechael.esjzone.EsjzoneClient
import net.deechael.esjzone.novel.Novel
import okhttp3.internal.toImmutableList
import us.codecraft.xsoup.Xsoup

class Chapter(private val client: EsjzoneClient, val novel: Novel, val id: String, val name: String) {

    private var contents: List<ChapterContent>? = null
        get() {
            if (field == null)
                field = listOf()
            if (field!!.isEmpty()) {
                val document = this.client.service.getChapterDetail(this.novel.id, this.id).execute().body()!!
                val chapterContents = mutableListOf<ChapterContent>()
                for (rawContent in Xsoup.select(document, "/html/body/div[3]/section/div/div[1]/div[3]")
                    .elements[0].allElements) {
                    if (rawContent.getElementsByTag("br").size > 0) {
                        chapterContents.add(ChapterBreakLine())
                    } else if (rawContent.getElementsByTag("img").size > 0) {
                        chapterContents.add(ChapterImage(rawContent.getElementsByTag("img")[0].attr("src")))
                    } else {
                        chapterContents.add(ChapterText(rawContent.text()))
                    }
                }
                field = chapterContents.toImmutableList()
            }
            return field
        }

}

interface ChapterContent {

}

class ChapterText(content: String) : ChapterContent {

    private val content: String

    init {
        this.content = content
    }

    override fun toString(): String {
        return this.content
    }

}

class ChapterBreakLine : ChapterContent {

    override fun toString(): String {
        return "\n"
    }

}

class ChapterImage(url: String) : ChapterContent {

    private val url: String

    init {
        this.url = url
    }

    override fun toString(): String {
        return this.url
    }

}