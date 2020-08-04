package com.karankumar.bookproject.tags;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Tag("integration-test")
@DataJpaTest
@ActiveProfiles("test")
public @interface DataJpaIntegrationTest {
}