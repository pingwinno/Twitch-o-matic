package net.streamarchive.presentation.management.api;

import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.exceptions.NotFoundException;
import net.streamarchive.infrastructure.models.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/settings")
public class SettingsApi {

    @Autowired
    private SettingsProvider settingsProperties;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Settings getSettings() {
        if (settingsProperties.isInitialized()) {
            return settingsProperties.getSettings();
        } else {
            throw new NotFoundException("File is empty. Please, save settings first");
        }
    }

    @PostMapping
    public void saveSettings(@RequestBody Settings settings) throws IOException {
        settingsProperties.saveSettings(settings);
    }
}
