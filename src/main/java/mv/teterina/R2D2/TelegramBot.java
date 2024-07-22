package mv.teterina.R2D2;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mv.teterina.R2D2.model.User;
import mv.teterina.R2D2.model.UserRepository;
import mv.teterina.R2D2.openAi.Service.ChatGptService;
import mv.teterina.R2D2.openAi.Voice.TelegramService;
import mv.teterina.R2D2.openAi.Voice.TranscribeVoiceToTextService;
import mv.teterina.R2D2.openAi.api.History.ChatGptHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;
    private final ChatGptHistory chatGptHistory;
    private final ChatGptService chatGptService;
    private final TelegramService telegramService;
    private final TranscribeVoiceToTextService transcribeVoiceToTextService;
    static final String HELP_TEXT = """
             Этот бот поддерживает работу с OpenAi \s 
             \
            и может отвечать на ваши вопросы, \s
            \
            для этого нажмите команду /start, после чего начнется общение с ботом.""";

    @SneakyThrows
    public TelegramBot(
            @Value("${bot.token}") String botToken,
            ChatGptHistory chatGptHistory,
            ChatGptService chatGptService,
            TelegramService telegramService,
            TranscribeVoiceToTextService transcribeVoiceToTextService) {

        super(new DefaultBotOptions(), botToken);
        this.chatGptHistory = chatGptHistory;
        this.chatGptService = chatGptService;
        this.telegramService = telegramService;
        this.transcribeVoiceToTextService = transcribeVoiceToTextService;

        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Начните работу с ботом"));
        listOfCommands.add(new BotCommand("/help", "Помощь при работе с ботом"));
        listOfCommands.add(new BotCommand("/clear", "Очистить контекст"));
        this.execute(new SetMyCommands(listOfCommands,new BotCommandScopeDefault(), null));
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String name = update.getMessage().getFrom().getFirstName();
            if (messageText.equals("/start")){
                registerUser(update.getMessage());
                SendMessage replaceText = new SendMessage();
                replaceText.setChatId(chatId);
                replaceText.setText("Привет, " + name + "!" + "\n"  +
                         "Как я могу вам помочь?");
                executeMessage(replaceText);
            } else {
                if (messageText.equals("/help")){
                    SendMessage helpText = new SendMessage();
                    helpText.setChatId(chatId);
                    helpText.setText(HELP_TEXT);
                    executeMessage(helpText);
                } else {
                    if (messageText.equals("/clear")){
                        chatGptHistory.clearHistory(chatId);
                        SendMessage clearText = new SendMessage();
                        clearText.setChatId(chatId);
                        clearText.setText("История ваших сообщений была очищена");
                        executeMessage(clearText);
                        } else {
                                var gptText = chatGptService.getResponseChatUser(chatId, messageText);
                                SendMessage sendMessage = new SendMessage(chatId.toString(), gptText);
                                sendApiMethod(sendMessage);
                    }
                }
            }
        } else {
            sendVoiceMessageToGpt(update);
        }
    }

    @SneakyThrows
    public void sendVoiceMessageToGpt (Update update){
        if (update.hasMessage() && update.getMessage().hasVoice()){
            var voice = update.getMessage().getVoice();
            Long chatId = update.getMessage().getChatId();
            var fileId = voice.getFileId();
            var file = telegramService.getFile(fileId);
            var text = transcribeVoiceToTextService.transcribe(file);
            var gptText = chatGptService.getResponseChatUser(chatId, text);
            SendMessage sendMessage = new SendMessage(chatId.toString(), gptText);
            sendApiMethod(sendMessage);
    }}

    public void registerUser(Message message){
        if (userRepository.findById(message.getChatId()).isEmpty()) {
            var chatId = message.getChatId();
            var chat = message.getChat();

            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setTimeRegister(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
        }
    }
    @SneakyThrows
    private void executeMessage(SendMessage message){
            execute(message);}

    @Override
    public String getBotUsername() {
        return "R2D2";
    }
}
