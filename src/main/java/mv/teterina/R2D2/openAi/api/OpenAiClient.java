package mv.teterina.R2D2.openAi.api;

import lombok.AllArgsConstructor;
import mv.teterina.R2D2.openAi.Voice.TranscriptionResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
public class OpenAiClient {

    private final String tokenAi;
    private final RestTemplate restTemplate;

    public ChatCompletionResponse createChatCompletion(ChatCompletionRequest request){
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + tokenAi);
        httpHeaders.set("Content-Type", "application/json");

        HttpEntity<ChatCompletionRequest> httpEntity = new HttpEntity<>(request, httpHeaders);

        ResponseEntity<ChatCompletionResponse> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, httpEntity, ChatCompletionResponse.class);
        return responseEntity.getBody();
    }

    public TranscriptionResponse createTranslation(CreateTranslationRequest request){
            String url = "https://api.openai.com/v1/audio/translations";

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + tokenAi);
            httpHeaders.set("Content-Type", "multipart/form-data");

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(request.audioFile()));
            body.add("model", request.model());
            var httpEntity = new HttpEntity<>(body, httpHeaders);

            ResponseEntity<TranscriptionResponse> responseEntity = restTemplate.exchange(
                    url, HttpMethod.POST, httpEntity, TranscriptionResponse.class);
            return responseEntity.getBody();
    }
}
