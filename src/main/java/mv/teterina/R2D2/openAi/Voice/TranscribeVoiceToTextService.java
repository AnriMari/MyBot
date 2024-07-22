package mv.teterina.R2D2.openAi.Voice;

import lombok.AllArgsConstructor;
import mv.teterina.R2D2.openAi.api.CreateTranslationRequest;
import mv.teterina.R2D2.openAi.api.OpenAiClient;
import org.springframework.stereotype.Service;
import java.io.File;

@AllArgsConstructor
@Service
public class TranscribeVoiceToTextService {

    private final OpenAiClient openAiClient;

    public String transcribe(File audioFile) {
        var response = openAiClient.createTranslation(CreateTranslationRequest.builder()
                .audioFile(audioFile)
                .model("whisper-1")
                .build());
        return response.text();
    }
}
