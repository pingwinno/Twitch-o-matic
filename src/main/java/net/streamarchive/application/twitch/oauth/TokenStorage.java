package net.streamarchive.application.twitch.oauth;

import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.exceptions.TwitchTokenProcessingException;
import net.streamarchive.infrastructure.models.TwitchAuthToken;
import net.streamarchive.util.UrlFormatter;

import java.io.*;

import static net.streamarchive.util.UrlFormatter.*;

@Slf4j
public class TokenStorage {
    private final static String TOKEN_FILE = "token.ser";

    public static void saveToken(TwitchAuthToken twitchAuthToken, String path) {
        log.debug("Saving token...");
        log.trace("Token path: {}",format(path, TOKEN_FILE));
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(format(path, TOKEN_FILE)))) {
            objectOutputStream.writeObject(twitchAuthToken);
            log.debug("Token saved");
        } catch (IOException e) {
            log.error("Can't save token file. Token will be lost after server reboot");
            throw new TwitchTokenProcessingException(e);
        }
    }

    public static TwitchAuthToken loadToken(String path) {
        log.debug("Loading token...");
        log.debug("Token path: {}", format(path, TOKEN_FILE));
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(format(path, TOKEN_FILE)))) {
            return (TwitchAuthToken) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Can't load token file. Authorization required");
        }
        return null;
    }
}
