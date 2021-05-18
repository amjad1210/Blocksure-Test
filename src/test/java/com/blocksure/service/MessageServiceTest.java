package com.blocksure.service;

import com.blocksure.dao.MessageRepository;
import com.blocksure.entity.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    private MessageService messageService;

    @BeforeEach
    public void setup() {
        messageService = new MessageService(messageRepository);
    }

    @Test
    void getAllMessages() {
        var helloMessage = new Message("hello", "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824");
        var hello3Message = new Message("hello3", "47ea70cf08872bdb4afad3432b01d963ac7d165f6b575cd72ef47498f4459a90");
        List<Message> messageList = new ArrayList<>();
        messageList.add(helloMessage);
        messageList.add(hello3Message);

        when(messageRepository.findAll()).thenReturn(messageList);

        List<String> fetchedMessages = messageService.getAllMessages();
        assertThat(fetchedMessages.size()).isGreaterThan(0);
    }

    @Test
    void convertPlain() {
        var helloMessage = new Message("hello", "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824");

        when(messageRepository.findBySha256(eq(helloMessage.getSha256()))).thenReturn(helloMessage);

        assertThat(messageService.convertPlain(helloMessage.getPlain())).isEqualTo("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824");
    }

    @Test
    void convertSha256() {
        //valid
        var helloMessage = new Message("hello", "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824");

        when(messageRepository.findBySha256(eq(helloMessage.getSha256()))).thenReturn(helloMessage);

        //valid
        assertThat(messageService.convertSha256(helloMessage.getSha256())).isEqualTo("hello");
        //invalid
        assertThat(messageService.convertSha256("bad")).isNull();
    }

}