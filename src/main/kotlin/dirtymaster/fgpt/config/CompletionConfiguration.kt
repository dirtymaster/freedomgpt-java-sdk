package dirtymaster.fgpt.config

import java.math.BigDecimal

class CompletionConfiguration {
    val maxTokens: Int = Int.MAX_VALUE
    val temperature: BigDecimal = BigDecimal.valueOf(0.7)
    val topK: Int? = null
    val topP: BigDecimal? = null
}
