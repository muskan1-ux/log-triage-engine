package com.logtriage.engine.config;

import com.logtriage.engine.service.LogTriageService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class LangChain4jConfig {

    @Bean
    public ChatModel chatModel(
            @Value("${langchain4j.open-ai.api-key}") String apiKey,
            @Value("${langchain4j.open-ai.model-name:gpt-4o-mini}") String modelName,
            @Value("${langchain4j.open-ai.timeout-seconds:4}") long timeoutSeconds) {

        return OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .temperature(0.1)
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .maxRetries(0)
                .logRequests(false)
                .logResponses(false)
                .build();
    }

    @Bean
    public LogTriageService logTriageService(ChatModel chatModel) {
        return AiServices.builder(LogTriageService.class)
                .chatModel(chatModel)
                .build();
    }
}
