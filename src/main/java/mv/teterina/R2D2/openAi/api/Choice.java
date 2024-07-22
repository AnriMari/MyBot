package mv.teterina.R2D2.openAi.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record Choice(@JsonProperty("message") Message message) { }
