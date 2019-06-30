package net.streamarchive.presentation.twitch.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TwitchApiHandler.class)
class TwitchApiHandlerTest {


    @Autowired
    private MockMvc mockMvc;


    @Test
    void getSubscriptionQuery() throws Exception {
        this.mockMvc.perform(get("/handler/olyashaa")).andDo(print()).andExpect(status().isOk());
    }
}