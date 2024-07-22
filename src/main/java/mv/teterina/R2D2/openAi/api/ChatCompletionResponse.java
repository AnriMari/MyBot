package mv.teterina.R2D2.openAi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.List;

@Builder
public record ChatCompletionResponse(@JsonProperty("choices") List<Choice> choices) { }
