package com.test.task.novisign;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.liquibase.enabled=false",
        "spring.main.web-application-type=reactive"
})
class ApplicationTests {

    @Test
    void contextLoads() {
    }

}
