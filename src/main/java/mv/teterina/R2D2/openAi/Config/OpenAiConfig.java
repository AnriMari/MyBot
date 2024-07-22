package mv.teterina.R2D2.openAi.Config;

import mv.teterina.R2D2.openAi.api.OpenAiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

    @Bean
    public OpenAiClient openAiClient(
            @Value("${bot.tokenAi}") String tokenAi,
            RestTemplateBuilder restTemplateBuilder ){
        return new OpenAiClient(tokenAi,restTemplateBuilder.build());
    }
}
