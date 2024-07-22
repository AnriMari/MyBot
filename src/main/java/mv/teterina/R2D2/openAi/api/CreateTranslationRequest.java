package mv.teterina.R2D2.openAi.api;

import lombok.Builder;
import java.io.File;

@Builder
public record CreateTranslationRequest(File audioFile, String model) {}
