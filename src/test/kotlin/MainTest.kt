import com.google.gson.JsonParser
import net.deechael.esjzone.EsjzoneClient
import net.deechael.esjzone.EsjzoneClientProxiedBuilder
import net.deechael.esjzone.novel.Novel
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.io.FileReader
import java.net.Proxy

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainTest {

    private lateinit var client: EsjzoneClient

    @BeforeAll
    fun createClient() {
        val data = JsonParser.parseReader(
            FileReader(
                File(
                    "./.local/testAccount.json"
                )
            )
        ).asJsonObject
        this.client = EsjzoneClientProxiedBuilder
            .of(Proxy.Type.HTTP, "127.0.0.1", 7890)
            .login(data["email"].asString, data["password"].asString)
    }

    @Test
    fun test() {
        val novel = Novel(this.client, "1678715616", "")
        for (comment in novel.listComments()) {
            println("${comment.senderId} - ${comment.content}")
        }
        // novel.comment("支持一下！")
        // val comment = MeComment(this.client, novel, null, "1098400", 89573, "")
        // comment.delete()
    }
}