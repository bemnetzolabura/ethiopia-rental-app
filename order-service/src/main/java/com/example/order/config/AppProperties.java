package com.example.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Rabbitmq rabbitmq = new Rabbitmq();
    private final Jwt jwt = new Jwt();

    public Rabbitmq getRabbitmq() { return rabbitmq; }
    public Jwt getJwt() { return jwt; }

    public static class Rabbitmq {
        private String exchange;
        public String getExchange() { return exchange; }
        public void setExchange(String exchange) { this.exchange = exchange; }
    }

    public static class Jwt {
        private String secret;
        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
    }
}
