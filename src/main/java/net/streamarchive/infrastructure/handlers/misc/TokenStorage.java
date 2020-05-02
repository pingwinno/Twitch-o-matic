package net.streamarchive.infrastructure.handlers.misc;

import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.exceptions.TwitchTokenProcessingException;
import net.streamarchive.infrastructure.models.TwitchAuthToken;

import java.io.*;

@Slf4j
public class TokenStorage {
    private final static String TOKEN_FILE = "token.ser";

    public static void saveToken(TwitchAuthToken twitchAuthToken, String path) {
        log.debug("Saving token...");
        log.trace("Token path: {}{}", path, TOKEN_FILE);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(path + TOKEN_FILE))) {
            objectOutputStream.writeObject(twitchAuthToken);
            log.debug("Token saved");
        } catch (IOException e) {
            log.error("Can't save token file. Token will be lost after server reboot");
            throw new TwitchTokenProcessingException(e);
        }
    }

    public static TwitchAuthToken loadToken(String path) {
        log.debug("Loading token...");
        log.debug("Token path: {}{}", path, TOKEN_FILE);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path + TOKEN_FILE))) {
            return (TwitchAuthToken) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Can't load token file. Authorization required");
        }
        return null;
    }
}
