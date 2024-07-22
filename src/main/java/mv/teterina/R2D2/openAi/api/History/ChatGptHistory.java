package mv.teterina.R2D2.openAi.api.History;

import lombok.AllArgsConstructor;
import mv.teterina.R2D2.openAi.api.Message;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class ChatGptHistory {

    private final Map<Long, ChatHistory> chatHistoryMap = new ConcurrentHashMap<>();

    public Optional<ChatHistory> getUserHistory(Long userId){
        return Optional.ofNullable(chatHistoryMap.get(userId));
    }

    public void createHistory(Long userId){
        chatHistoryMap.put(userId, new ChatHistory(new ArrayList<>()));

    }

    public void clearHistory(Long userId) {
        chatHistoryMap.remove(userId);
    }

    public ChatHistory addMessageToHistory(Long userId,
                                    Message message){
        var chatHistory = chatHistoryMap.get(userId);
        if (chatHistory == null) {
            throw new IllegalStateException("History not founded");
        }
        chatHistory.chatMessages().add(message);
        return chatHistory;
    }

    public void createHistoryIfNotFound(Long userId) {
        if(!chatHistoryMap.containsKey(userId)) {
            createHistory(userId);
        }
    }
}
