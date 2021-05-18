package com.blocksure.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author Amjad
 */
@Entity
@Table(name = "message",
        indexes = {
                @Index(columnList = "plain", name = "idx_plain"),
                @Index(columnList = "sha256", name = "idx_sha256")
        })
@Data
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String plain;

    private String sha256;

    public Message(String plain, String sha256) {
        this.plain = plain;
        this.sha256 = sha256;
    }

}
