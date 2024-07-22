package mv.teterina.R2D2.openAi.api;

import lombok.Builder;
import java.util.List;

@Builder
public record ChatCompletionRequest(
        String model,
        List<Message> messages) { }
