package com.blocksure.controller;

import com.blocksure.entity.Message;
import com.blocksure.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    private MockMvc mvc;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(messageController)
                .build();
    }

    @Test
    void getAll() throws Exception {
        var helloMessage = new Message("hello", "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824");
        var hello3Message = new Message("hello3", "47ea70cf08872bdb4afad3432b01d963ac7d165f6b575cd72ef47498f4459a90");
        List<Message> messageList = new ArrayList<>();
        messageList.add(helloMessage);
        messageList.add(hello3Message);

        // given
        when(messageService.getAllMessages()).thenReturn(messageList.stream()
                .map(Message::getPlain)
                .collect(Collectors.toList()));

        mvc.perform(get("/message/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void convertPlain() throws Exception {
        var helloMessage = new Message("hello", "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824");

        // given
        when(messageService.convertPlain(helloMessage.getPlain())).thenReturn(helloMessage.getSha256());

        //when
        var result = mvc.perform(post("/message/plain?value=" + helloMessage.getPlain())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(helloMessage.getSha256());
    }

    @Test
    void convertSha256() throws Exception {
        var helloMessage = new Message("hello", "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824");

        // given
        when(messageService.convertSha256(helloMessage.getSha256())).thenReturn(helloMessage.getPlain());
        when(messageService.convertSha256("bad")).thenReturn(null);

        //valid
        var result1 = mvc.perform(get("/message/sha256?value=" + helloMessage.getSha256())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result1.getResponse().getContentAsString()).isEqualTo(helloMessage.getPlain());

        //invalid
        mvc.perform(get("/message/sha256?value=bad")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}