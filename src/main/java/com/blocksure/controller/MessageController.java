package com.blocksure.controller;

import com.blocksure.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Amjad
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/all")
    public List<String> getAll() {
        return messageService.getAllMessages();
    }

    @PostMapping("/plain")
    public String convertPlain(@RequestParam String value) {
        return messageService.convertPlain(value);
    }

    @GetMapping("/sha256")
    public ResponseEntity<String> convertSha256(@RequestParam String value) {
        var plain = messageService.convertSha256(value);
        if (plain == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(plain);
    }

    @DeleteMapping("/delete")
    public void deleteMessage(@RequestParam String value) {
        messageService.deleteMessage(value);
    }

}
