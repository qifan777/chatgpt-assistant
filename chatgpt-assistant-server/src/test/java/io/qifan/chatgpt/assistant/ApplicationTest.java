package io.qifan.chatgpt.assistant;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
public class ApplicationTest {
    private final MongoTemplate mongoTemplate;

    public ApplicationTest(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Test
    void findById() {
//        mongoTemplate.findOne(Query.query(Criteria.where("c")))
    }
}
