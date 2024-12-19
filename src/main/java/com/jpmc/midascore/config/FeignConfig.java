package com.jpmc.midascore.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    public class CustomErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            return switch (response.status()) {
                case 400 -> {
                    log.error("Bad Request: {}", response.request().url());
                    yield new BadRequestException("Bad Request");
                }
                case 404 -> {
                    log.error("Not Found: {}", response.request().url());
                    yield new BadRequestException("Not Found");
                }
                default -> {
                    log.error("Generic Error: {}", response.request().url());
                    yield new Exception("Generic Error");
                }
            };
        }
    }
}