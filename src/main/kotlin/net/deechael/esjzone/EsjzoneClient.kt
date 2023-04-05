package net.deechael.esjzone

import net.deechael.esjzone.category.Category
import net.deechael.esjzone.util.retrofit.DocumentFactory
import net.deechael.esjzone.util.retrofit.EsjzoneService
import net.deechael.esjzone.user.User
import okhttp3.*
import okhttp3.internal.toImmutableList
import retrofit2.Retrofit
import us.codecraft.xsoup.Xsoup
import java.net.InetSocketAddress
import java.net.Proxy

class EsjzoneClient internal constructor(wsKey: String, wsToken: String, proxy: Proxy? = null) {

    val httpClient: OkHttpClient
    val retrofit: Retrofit
    val service: EsjzoneService

    init {
        val builder = OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return listOf(
                        Cookie.Builder().domain("www.esjzone.cc").name("ws_key").value(wsKey)
                            .build(),
                        Cookie.Builder().domain("www.esjzone.cc").name("ws_token").value(wsToken)
                            .build()
                    )
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                }
            })
        if (proxy != null)
            builder.proxy(proxy)
        this.httpClient = builder.build()
        this.retrofit = Retrofit.Builder()
            .baseUrl("https://www.esjzone.cc/")
            .client(this.httpClient)
            .addConverterFactory(DocumentFactory())
            .build()
        this.service = this.retrofit.create(EsjzoneService::class.java)
    }

    fun getUserInfo(uid: Int): User {
        return User(this, this.service.getUserProfile(uid).execute().body()!!)
    }

    fun getCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        val document = this.service.getCategories().execute().body()!!
        for (element in Xsoup.select(document, "/html/body/div[3]/section/div/div[1]/div[1]/table/tbody/tr/td/a").elements) {
            val rawUrl = element.attr("href")
            categories.add(Category(this, rawUrl.substring(7, rawUrl.length - 1), element.text()))
        }
        return categories.toImmutableList()
    }

}

class EsjzoneClientProxiedBuilder private constructor(private val proxy: Proxy) {

    fun cookie(wsKey: String, wsToken: String): EsjzoneClient {
        return EsjzoneClientBuilder.of().proxy(this.proxy).key(wsKey).token(wsToken).build()
    }

    fun login(email: String, password: String): EsjzoneClient {
        return EsjzoneLoginer.of().proxy(this.proxy).login(email, password)
    }

    companion object {

        fun of(proxy: Proxy): EsjzoneClientProxiedBuilder {
            return EsjzoneClientProxiedBuilder(proxy)
        }

        fun of(type: Proxy.Type, host: String, port: Int): EsjzoneClientProxiedBuilder {
            return of(Proxy(type, InetSocketAddress(host, port)))
        }

    }

}

class EsjzoneClientBuilder private constructor() {

    private var proxy: Proxy? = null
    private lateinit var wsKey: String
    private lateinit var wsToken: String

    fun proxy(proxy: Proxy?): EsjzoneClientBuilder {
        this.proxy = proxy
        return this
    }

    fun key(wsKey: String): EsjzoneClientBuilder {
        this.wsKey = wsKey
        return this
    }

    fun token(wsToken: String): EsjzoneClientBuilder {
        this.wsToken = wsToken
        return this
    }

    fun build(): EsjzoneClient {
        return EsjzoneClient(this.wsKey, this.wsToken, this.proxy)
    }

    companion object {

        fun of(): EsjzoneClientBuilder {
            return EsjzoneClientBuilder()
        }

    }

}

class EsjzoneLoginer private constructor() {


    private var proxy: Proxy? = null

    fun proxy(proxy: Proxy?): EsjzoneLoginer {
        this.proxy = proxy
        return this
    }

    fun login(email: String, password: String): EsjzoneClient {
        var builder = OkHttpClient.Builder()
        if (this.proxy != null)
            builder.proxy(this.proxy)
        var httpClient = builder.build()
        val loginRequest = requestBuilder().url("https://www.esjzone.cc/my/login")
            .post(
                MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("plxf", "getAuthToken")
                    .build()
            ).build()
        httpClient.newCall(loginRequest).execute()
        var wsKey: String = ""
        var wsToken: String = ""
        builder = OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return listOf()
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    for (cookie in cookies) {
                        if (cookie.name == "ws_key") {
                            wsKey = cookie.value
                        } else if (cookie.name == "ws_token") {
                            wsToken = cookie.value
                        }
                    }
                }
            })
        if (this.proxy != null)
            builder.proxy(this.proxy)
        httpClient = builder.build()
        val memLoginRequest = requestBuilder().url("https://www.esjzone.cc/inc/mem_login.php")
            .post(
                MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .addFormDataPart("pwd", password)
                    .addFormDataPart("remember_me", "on")
                    .build()
            ).build()
        httpClient.newCall(memLoginRequest).execute()
        return EsjzoneClientBuilder.of()
            .key(wsKey)
            .token(wsToken)
            .proxy(this.proxy)
            .build()
    }

    companion object {

        fun of(): EsjzoneLoginer {
            return EsjzoneLoginer()
        }

    }

}

internal fun requestBuilder(): Request.Builder {
    return Request.Builder()
        .header(
            "accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
        )
        .header("accept-language", "en-US,en;q=0.9,zh-CN;q=0.8,zh-Hans;q=0.7,zh;q=0.6")
        .header(
            "user-agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36"
        )
}