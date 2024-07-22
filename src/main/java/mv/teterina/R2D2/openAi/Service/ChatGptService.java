package mv.teterina.R2D2.openAi.Service;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import mv.teterina.R2D2.openAi.api.ChatCompletionRequest;
import mv.teterina.R2D2.openAi.api.History.ChatGptHistory;
import mv.teterina.R2D2.openAi.api.Message;
import mv.teterina.R2D2.openAi.api.OpenAiClient;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatGptService {

    private final OpenAiClient openAiClient;
    private final ChatGptHistory chatGptHistory;

    @Nonnull
    public String getResponseChatUser(Long userId, String userText){

        chatGptHistory.createHistoryIfNotFound(userId);
        var history = chatGptHistory.addMessageToHistory(
                userId,
                Message.builder()
                    .content(userText)
                    .role("user")
                    .build()
                );
        var request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(history.chatMessages())
                .build();
        var response1 = openAiClient.createChatCompletion(request);
        var messageToUserFromGpt = response1.choices().get(0).message();
        chatGptHistory.addMessageToHistory(userId, messageToUserFromGpt);
    return messageToUserFromGpt.content();
    }
}
