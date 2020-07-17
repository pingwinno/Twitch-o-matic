package net.streamarchive.presentation.management.api;

import net.streamarchive.domain.service.LogDownloadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(LogApi.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class LogApiTest {

    @MockBean
    private LogDownloadService service;
    private Resource inputStream;
    private final static String LOGS = "some logs";
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        inputStream = new ByteArrayResource(LOGS.getBytes());
    }

    @Test
    @DisplayName("Log get test")
    public void shouldReturnLogWhenCallGetWithUUID() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(service.getLogFile(uuid)).thenReturn(inputStream);
        this.mockMvc.perform(get("/api/v1/log/{uuid}", uuid)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(LOGS)).andDo(document("logs/{methodName}",
                pathParameters(parameterWithName("uuid").description("stream uuid"))));
    }
}