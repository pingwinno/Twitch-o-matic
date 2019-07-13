package net.streamarchive.presentation.management.api;

import net.streamarchive.infrastructure.SettingsProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StreamsApiTest {
    SettingsProperties settingsProperties = new SettingsProperties();

    @Test
    public void isStreamerExist() {
        assertTrue(settingsProperties.isUserExist("olyashaa"));
    }
}