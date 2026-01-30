package com.db.bankingapi.configurations;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZipkinConfiguration {
    @Bean
    public SpanHandler customSpanHandler() {
        return new SpanHandler() {
            @Override
            public boolean end(TraceContext context, MutableSpan span, Cause cause) {
                span.tag("custom.tag", "custom-value");
                return true;
            }
        };
    }
}
