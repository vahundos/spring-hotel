package com.vahundos.spring.hotel.web.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:hsqldb/data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestPropertySource(locations = "classpath:test.properties")
// https://github.com/spring-projects/spring-framework/issues/19930
@interface NestedSql {
}
