package dirtymaster.fgpt.client;

import dirtymaster.fgpt.model.Model;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;

class FreedomGPTTemplateTest {
    private final String apiKey;

    FreedomGPTTemplateTest() throws IOException {
        this.apiKey = new String(Files.readAllBytes(Paths.get("src/test/resources/key.txt")));
    }

    @Test
    void testCompletion() {
        FreedomGPTTemplate template = new FreedomGPTTemplate(Model.CLAUDE_3_5_SONNET, apiKey);
        String hello = template.getCompletion("hello");
        assertFalse(hello.isEmpty());
    }
}