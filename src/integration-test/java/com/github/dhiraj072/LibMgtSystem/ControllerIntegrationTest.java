package com.github.dhiraj072.LibMgtSystem;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * All the configuration we need to run test our REST API in controllers
 * This instantiates the standard application context, but uses IntegrationTest profile
 * which will disable spring security (application-integrationtest.properties) making
 * our life easier for making rest api calls in tests. Though unlike {@link H2IntegrationTest}
 * this will not roll back any database transactions as that is not very straightforward to
 * do with a {@link WebEnvironment#RANDOM_PORT} set in {@link SpringBootTest}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ActiveProfiles("IntegrationTest")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public @interface ControllerIntegrationTest {

}
