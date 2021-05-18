package com.blocksure.dao;

import com.blocksure.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Amjad
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Message findBySha256(String sha256);

    void deleteByPlain(String message);

}
