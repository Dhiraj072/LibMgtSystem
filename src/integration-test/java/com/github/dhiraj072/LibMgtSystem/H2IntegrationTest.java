package com.github.dhiraj072.LibMgtSystem;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.transaction.Transactional;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

/**
 * This encapsulates every configuration we need to run our integration tests.
 * This is transactional and any database calls made in the tests annotated with
 * this class will be rolled back
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest(classes = { H2IntegrationTestContext.class })
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@Rollback
@ActiveProfiles("IntegrationTest")
public @interface H2IntegrationTest {

}
