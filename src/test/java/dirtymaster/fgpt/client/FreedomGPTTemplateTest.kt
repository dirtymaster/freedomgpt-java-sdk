package dirtymaster.fgpt.client

import dirtymaster.fgpt.model.Model
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

internal class FreedomGPTTemplateTest {
    private val apiKey = String(Files.readAllBytes(Paths.get("src/test/resources/key.txt")))

    @Test
    fun testCompletion() {
        val template = FreedomGPTTemplate(Model.claudeMinus3Period5MinusSonnet, apiKey)
        val hello = template.getCompletion("hello")
        Assertions.assertFalse(hello.isEmpty())
    }
}