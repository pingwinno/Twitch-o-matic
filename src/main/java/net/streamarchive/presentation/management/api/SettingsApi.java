package net.streamarchive.presentation.management.api;

import net.streamarchive.infrastructure.SettingsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/settings")
public class SettingsApi {
    @Autowired
    SettingsProperties settingsProperties;
}
