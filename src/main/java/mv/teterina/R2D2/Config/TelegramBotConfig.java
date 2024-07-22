package mv.teterina.R2D2.Config;

import lombok.SneakyThrows;
import mv.teterina.R2D2.TelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfig {

    @Bean
    @SneakyThrows
    public TelegramBotsApi telegramBotsApi(TelegramBot bot){
        var telegramBotApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotApi.registerBot(bot);
        return telegramBotApi;
    }
}
