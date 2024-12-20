package dirtymaster.fgpt.config;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CompletionConfiguration {
    private Integer maxTokens;// = Integer.MAX_VALUE;
    private BigDecimal temperature = BigDecimal.valueOf(0.7);
    private Integer topK;
    private BigDecimal topP;
}
