package net.deechael.esjzone.novel

import net.deechael.esjzone.EsjzoneClient
import net.deechael.esjzone.chapter.Chapter
import okhttp3.internal.toImmutableList
import us.codecraft.xsoup.Xsoup

class Novel(private val client: EsjzoneClient, val id: String, val name: String) {

    var cover: String? = null
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
                field = NovelDescription(rawList.toImmutableList())
            }
            return field
        }

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

}

class NovelDescription(val descriptionLines: List<DescriptionLine>) {

}

interface DescriptionLine {

}

class TextDescriptionLine(val text: String, val strong: Boolean): DescriptionLine {

}

class ImageDescriptionLine(val src: String): DescriptionLine {

}