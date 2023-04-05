package net.deechael.esjzone.category

import net.deechael.esjzone.EsjzoneClient
import net.deechael.esjzone.novel.Novel
import okhttp3.internal.toImmutableList
import us.codecraft.xsoup.Xsoup

class Category(val client: EsjzoneClient, val id: String, val name: String) {

    fun listNovels(): List<Novel> {
        val novels = mutableListOf<Novel>()
        val document = this.client.service.getCategoryNovels(this.id).execute().body()!!
        for (element in Xsoup.select(document, "/html/body/div[3]/section/div/div/div/table/tbody/tr/td/a").elements) {
            val rawUrl = element.attr("href")
            novels.add(Novel(this.client, rawUrl.substring("/forum/${this.id}/".length, rawUrl.length - 1), element.text()))
        }
        return novels.toImmutableList()
    }

}