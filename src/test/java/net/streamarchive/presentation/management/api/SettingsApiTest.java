package net.streamarchive.presentation.management.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.streamarchive.infrastructure.SettingsProvider;
import net.streamarchive.infrastructure.exceptions.WrongParamsException;
import net.streamarchive.infrastructure.models.Settings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(SettingsApi.class)
@AutoConfigureMockMvc(addFilters = false)
@Slf4j
@AutoConfigureRestDocs
class SettingsApiTest {

    private static final String CONSTRAINTS = "constraints";
    private static Settings settings = new Settings();
    private static String jsonSettings;
    private static ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SettingsProvider settingsProvider;
    private ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(Settings.class);

    @BeforeAll
    static void init() throws IOException {
        settings.setClientID("swejniobnvernv");
        settings.setUser("evij");
        settings.setUserPass("dfvojeoi");
        settings.setCallbackAddress("http://localhost");
        settings.setDbUsername("jerfe");
        settings.setDbPassword("iaoejfvioe");
        settings.setRemoteDBAddress("http://db.com");
        settings.setClientSecret("swejniobnvernv");
        jsonSettings = mapper.writeValueAsString(settings);
    }

    @Test
    @DisplayName("Settings get test")
    public void shouldReturnSettingsWhenCallGet() throws Exception {
        when(settingsProvider.isInitialized()).thenReturn(true);
        when(settingsProvider.getSettings()).thenReturn(settings);
        log.warn(constraintDescriptions.descriptionsForProperty("user").toString());
        this.mockMvc.perform(get("/api/v1/settings")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().json(jsonSettings)).andDo(document("settings/{methodName}"
                , responseFields(
                        fieldWithPath("user").description("recorder login").attributes(key(CONSTRAINTS)
                                .value(constraintDescriptions.descriptionsForProperty("user"))),
                        fieldWithPath("userPass").description("recorder password").attributes(key(CONSTRAINTS)
                                .value(constraintDescriptions.descriptionsForProperty("userPass"))),
                        fieldWithPath("callbackAddress").description("Callback address for twitch webhook api (needed for stream events")
                                .attributes(key(CONSTRAINTS).value(constraintDescriptions.descriptionsForProperty("callbackAddress"))),
                        fieldWithPath("remoteDBAddress").description("Streamarchive db server address. Needed for publishing streams to streamarchive portal").attributes(key(CONSTRAINTS)
                                .value(constraintDescriptions.descriptionsForProperty("remoteDBAddress"))),
                        fieldWithPath("dbUsername").description("Streamarchive db server username").attributes(key(CONSTRAINTS)
                                .value(constraintDescriptions.descriptionsForProperty("dbUsername"))),
                        fieldWithPath("dbPassword").description("Streamarchive db server password").attributes(key(CONSTRAINTS)
                                .value(constraintDescriptions.descriptionsForProperty("dbPassword"))),
                        fieldWithPath("clientID").description("Twitch API client id (more on https://dev.twitch.tv/docs/v5")
                                .attributes(key(CONSTRAINTS).value(constraintDescriptions.descriptionsForProperty("clientID"))),
                        fieldWithPath("clientSecret").description("Twitch API client secret (more on https://dev.twitch.tv/docs/v5")
                                .attributes(key(CONSTRAINTS).value(constraintDescriptions.descriptionsForProperty("clientSecret"))))));

    }

    @Test
    @DisplayName("Empty settings get test")
    public void shouldReturn404WhenCallGetOnEmptySettings() throws Exception {
        when(settingsProvider.isInitialized()).thenReturn(false);
        this.mockMvc.perform(get("/api/v1/settings")).andDo(print()).andExpect(status().isNotFound())
                .andDo(document("settings/{methodName}"));
    }

    @Test
    @DisplayName("Save settings")
    public void shouldReturn200WhenSaveSettings() throws Exception {
        doNothing().when(settingsProvider).saveSettings(settings);
        this.mockMvc.perform(post("/api/v1/settings").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8").content(jsonSettings)).andDo(print()).andExpect(status().isOk())
                .andDo(document("settings/{methodName}"
                        , requestFields(
                                fieldWithPath("user").description("recorder login").attributes(key(CONSTRAINTS)
                                        .value(constraintDescriptions.descriptionsForProperty("user"))),
                                fieldWithPath("userPass").description("recorder password").attributes(key(CONSTRAINTS)
                                        .value(constraintDescriptions.descriptionsForProperty("userPass"))),
                                fieldWithPath("callbackAddress").description("Callback address for twitch webhook api (needed for stream events")
                                        .attributes(key(CONSTRAINTS).value(constraintDescriptions.descriptionsForProperty("callbackAddress"))),
                                fieldWithPath("remoteDBAddress").description("Streamarchive db server address. Needed for publishing streams to streamarchive portal").attributes(key(CONSTRAINTS)
                                        .value(constraintDescriptions.descriptionsForProperty("remoteDBAddress"))),
                                fieldWithPath("dbUsername").description("Streamarchive db server username").attributes(key(CONSTRAINTS)
                                        .value(constraintDescriptions.descriptionsForProperty("dbUsername"))),
                                fieldWithPath("dbPassword").description("Streamarchive db server password").attributes(key(CONSTRAINTS)
                                        .value(constraintDescriptions.descriptionsForProperty("dbPassword"))),
                                fieldWithPath("clientID").description("Twitch API client id (more on https://dev.twitch.tv/docs/v5")
                                        .attributes(key(CONSTRAINTS).value(constraintDescriptions.descriptionsForProperty("clientID"))),
                                fieldWithPath("clientSecret").description("Twitch API client secret (more on https://dev.twitch.tv/docs/v5")
                                        .attributes(key(CONSTRAINTS).value(constraintDescriptions.descriptionsForProperty("clientSecret"))))));
    }

    @Test
    @DisplayName("Save wrong settings")
    public void shouldReturn400WhenSaveWrongSettings() throws Exception {
        doThrow(WrongParamsException.class).when(settingsProvider).saveSettings(settings);
        this.mockMvc.perform(post("/api/v1/settings").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF8").content(jsonSettings)).andDo(print()).andExpect(status().isBadRequest())
                .andDo(document("settings/{methodName}"));
    }
}