package net.streamarchive.infrastructure.handlers.misc;

import lombok.extern.slf4j.Slf4j;
import net.streamarchive.application.RecoveryRecordHandler;
import net.streamarchive.application.twitch.handler.TwitchOAuthHandler;
import net.streamarchive.domain.TelegramServerPool;
import net.streamarchive.infrastructure.exceptions.TwitchTokenProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StartupApplicationListener implements
        ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    RecoveryRecordHandler recoveryRecordHandler;
    @Autowired
    TwitchOAuthHandler twitchOAuthHandler;



    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            twitchOAuthHandler.run();
            recoveryRecordHandler.run();
        }catch (TwitchTokenProcessingException e){
            log.error("Can't start automatic recovery", e);
        }
    }


}
