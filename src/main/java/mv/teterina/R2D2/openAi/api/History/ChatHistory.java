package mv.teterina.R2D2.openAi.api.History;

import lombok.Builder;
import mv.teterina.R2D2.openAi.api.Message;
import java.util.List;

@Builder
public record ChatHistory(List<Message> chatMessages) {
}
