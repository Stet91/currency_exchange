package com.fundcount.currency;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EntityScan("com.fundcount.currency.domain")
@ComponentScan("com.fundcount.currency.serviceImpl")
public class TestConfig {
}