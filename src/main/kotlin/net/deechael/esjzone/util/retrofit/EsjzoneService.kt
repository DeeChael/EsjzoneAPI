package net.deechael.esjzone.util.retrofit

import net.deechael.esjzone.comment.Comment
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @GET("forum")
    fun getCategories(): Call<Document>

    @GET("forum/{categoryId}/")
    fun getCategoryNovels(@Path("categoryId") categoryId: String): Call<Document>

    @GET("tags{typeId}{sortId}/")
    fun getNovelsByTag(@Path("typeId") typeId: Int, @Path("sortId") sortId: Int): Call<Document>

    @GET("tags{typeId}{sortId}/{tag}/")
    fun getNovelsByTag(@Path("typeId") typeId: Int, @Path("sortId") sortId: Int, @Path("tag") tag: String): Call<Document>

    @POST("inc/forum_reply.php")
    // fun createComment(@Part("content") content: String, @Part("data") data: String = "books")
    fun createComment(@Body body: RequestBody): Call<ResponseBody> // FIXME not works

    @POST("inc/forum_reply.php")
    // fun replyComment(@Part("content") content: String, @Part("reply") reply: Comment, @Part("forum_id") forumId: String = "0", @Part("data") data: String = "books")
    fun replyComment(@Body body: RequestBody): Call<ResponseBody> // FIXME not works

    @POST("inc/forum_del.php")
    // fun deleteComment(@Part("rid") commentId: String, @Part("type") type: String = "book", @Part("data") data: String = "books")
    fun deleteComment(@Body body: RequestBody): Call<ResponseBody> // FIXME not works

    @POST("{path}")
    // fun getAuthToken(@Path("path") path: String, @Part("plxf") plxf: String = "getAuthToken") // Invoke this before every POST request
    fun getAuthToken(@Path("path") path: String, @Body body: RequestBody): Call<ResponseBody> // Invoke this before every POST request

}