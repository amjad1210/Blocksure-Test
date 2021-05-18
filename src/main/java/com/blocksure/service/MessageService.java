package com.blocksure.service;

import com.blocksure.dao.MessageRepository;
import com.blocksure.entity.Message;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Amjad
 */
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<String> getAllMessages() {
        return messageRepository.findAll()
                .stream()
                .map(Message::getPlain)
                .collect(Collectors.toList());
    }

    public String convertPlain(String value) {
        var sha256 = DigestUtils.sha256Hex(value);
        var message = messageRepository.findBySha256(sha256);
        if (message == null) {
            messageRepository.save(new Message(value, sha256));
        }

        return sha256;
    }

    public String convertSha256(String value) {
        var message = messageRepository.findBySha256(value);
        if (message == null) {
            return null;
        }

        return message.getPlain();
    }

    @Transactional
    public void deleteMessage(String value) {
        messageRepository.deleteByPlain(value);
    }

}
