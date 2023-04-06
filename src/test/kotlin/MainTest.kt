import com.google.gson.JsonParser
import net.deechael.esjzone.EsjzoneClient
import net.deechael.esjzone.EsjzoneClientProxiedBuilder
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
    fun listNovelsTest() {
        for (novel in this.client.listNovels()) {
            println(novel.name)
        }
    }

}