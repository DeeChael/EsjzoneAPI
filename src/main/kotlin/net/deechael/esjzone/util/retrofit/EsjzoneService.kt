package net.deechael.esjzone.util.retrofit

import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EsjzoneService {

    @GET("my/profile")
    fun getUserProfile(@Query("uid") uid: Int): Call<Document>

    @GET("my/book")
    fun getUserManagedBooks(@Query("uid") uid: Int): Call<Document>

    @GET("my/post")
    fun getUserPosts(@Query("uid") uid: Int): Call<Document>

    @GET("my/favorite")
    fun getUserFavorites(@Query("uid") uid: Int): Call<Document>

    @GET("my/profile")
    fun getMyProfile(): Call<Document>

    @GET("my/book")
    fun getMyManagedBooks(): Call<Document>

    @GET("my/post")
    fun getMyPosts(): Call<Document>

    @GET("my/favorite")
    fun getMyFavorites(): Call<Document>

    @GET("my/reply")
    fun getMyReplies(): Call<Document>

    @GET("my/message")
    fun getMyMessages(): Call<Document>

    @GET("my/view")
    fun getMyViewedHistories(): Call<Document>

    @GET("my/record")
    fun getMyExperienceRecords(): Call<Document>

    @GET("my/fixed")
    fun getMyIssues(): Call<Document>

    @GET("my/ticket")
    fun getMyTickets(): Call<Document>

    @GET("my/sys")
    fun getMySystemMessages(): Call<Document>

    @GET("detail/{bookId}.html")
    fun getNovelDetail(@Path("bookId") bookId: String): Call<Document>

    @GET("forum/{bookId}/{chapterId}.html")
    fun getChapterDetail(@Path("bookId") bookId: String, @Path("chapterId") chapterId: String): Call<Document>

}