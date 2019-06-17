package net.streamarchive;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest
class StatusServiceTest {
    @Autowired
    MongoTemplate mongoTemplate;



    @Test
    void findAll() {
        System.out.println(mongoTemplate.collectionExists("olyashaa"));

    }
}