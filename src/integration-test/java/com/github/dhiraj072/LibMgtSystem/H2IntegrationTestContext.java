package com.github.dhiraj072.LibMgtSystem;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Profile("IntegrationTest")
@SpringBootApplication
@EnableTransactionManagement
public class H2IntegrationTestContext {

}
