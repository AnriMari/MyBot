package mv.teterina.R2D2.openAi.Voice;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import java.io.*;
import java.net.URI;
import java.net.URL;

@Slf4j
@Service
public class TelegramService {

    private final DefaultAbsSender telegramSender;

    private final String botToken;

    public TelegramService(@Lazy DefaultAbsSender telegramSender, @Value("${bot.token}") String botToken) {
        this.telegramSender = telegramSender;
        this.botToken = botToken;
    }

    @SneakyThrows
    public java.io.File getFile(String fileId){
         File file   = telegramSender.execute(GetFile.builder()
                .fileId(fileId)
                .build());
        var urlToDownload = file.getFileUrl(botToken);
        return getByteFromUrl(urlToDownload);
    }

    @SneakyThrows
    private java.io.File getByteFromUrl(String urlToDownload) {
        URL url = new URI(urlToDownload).toURL();
        var fileTemplate = java.io.File.createTempFile("telegram", ".ogg");

        try (InputStream inputStream = url.openStream();
             FileOutputStream fileOutputStream = new FileOutputStream(fileTemplate)) {
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (IOException e) {
            log.error("Ошибка загрузки файла.", e);
            throw new RuntimeException("Ошибка загрузки файла.", e);
        }
        return fileTemplate;
    }
}
