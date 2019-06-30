package net.streamarchive;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
class StatusServiceTest {
    @Autowired
    MongoTemplate mongoTemplate;



    @Test
    void findAll() {
        System.out.println(mongoTemplate.collectionExists("olyashaa"));

    }
}