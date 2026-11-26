package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = {
		"jwt.secret=26joMqqcMgRsT5DZkUcaNoIf74NcdKOFWpOiy6GYje9fMd0wbzbFoSXV5WD6RToS",
		"jwt.access-token-expiration=900000",
		"jwt.refresh-token-expiration=604800000"
})
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}
}