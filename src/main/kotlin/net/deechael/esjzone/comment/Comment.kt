package net.deechael.esjzone.comment

import net.deechael.esjzone.EsjzoneClient
import net.deechael.esjzone.chapter.Chapter
import net.deechael.esjzone.novel.Novel
import net.deechael.esjzone.user.User
import net.deechael.esjzone.util.retrofit.BodyBuilder

open class Comment(internal val client: EsjzoneClient, val novel: Novel, val chapter: Chapter?, val id: String, val senderId: Int, val content: String, val replayContent: String? = null) {

    private var sender: User? = null

    fun reply(content: String) {
        if (this.chapter == null) {
            this.client.getAuthToken("detail/${novel.id}.html")
            this.client.service.replyComment(BodyBuilder.of()
                .param("content", content)
                .param("reply", "${this.id}-${this.senderId}")
                .param("forum_id", "0")
                .param("data", "books")
                .build())
        } else {
            this.client.getAuthToken("forum/${novel.id}/${chapter.id}.html")
            this.client.service.replyComment(BodyBuilder.of()
                .param("content", content)
                .param("reply", "${this.id}-${this.senderId}")
                .param("forum_id", this.chapter.id)
                .param("data", "forum")
                .build())
        }
    }

    fun getSender(): User {
        if (this.sender == null)
            this.sender = this.client.getUserInfo(this.senderId)
        return this.sender!!
    }

}

class MeComment(
    client: EsjzoneClient,
    novel: Novel,
    chapter: Chapter?,
    id: String,
    senderId: Int,
    content: String,
    replayContent: String? = null
) : Comment(client, novel, chapter, id, senderId, content, replayContent) {

    fun delete() {
        if (this.chapter == null) {
            this.client.getAuthToken("detail/${novel.id}.html")
            this.client.service.deleteComment(BodyBuilder.of()
                .param("rid", this.id)
                .param("type", "book")
                .param("data", "books")
                .build())
        } else {
            this.client.getAuthToken("forum/${novel.id}/${chapter.id}.html")
            this.client.service.deleteComment(BodyBuilder.of()
                .param("rid", this.id)
                .param("type", "forum")
                .param("data", "forum")
                .build())
        }
    }

}