package com.example.mynews.Service.impl;

import com.example.mynews.config.OpenAIConfig;
import com.example.mynews.Service.AIService;
import com.example.mynews.exception.BusinessException;
import com.example.mynews.response.StatusCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.web.reactive.function.client.ClientResponse;
import com.example.mynews.exception.ValidationException;

@Service
@Slf4j
public class OpenAIServiceImpl implements AIService {

    @Autowired
    private OpenAIConfig config;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public OpenAIServiceImpl() {
        this.webClient = WebClient.builder()
            .codecs(configurer -> configurer.defaultCodecs()
                .maxInMemorySize(16 * 1024 * 1024))
            .build();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String summarizeContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new ValidationException(StatusCode.PARAM_ERROR, "内容不能为空");
        }

        try {
            Map<String, Object> requestBody = buildRequestBody(content);
            String apiUrl = config.getApiUrl() + "/v1/chat/completions";
            
            log.debug("OpenAI API 请求: URL={}, Body={}", 
                apiUrl, objectMapper.writeValueAsString(requestBody));

            String response = sendRequest(apiUrl, requestBody);
            return parseResponse(response);
        } catch (Exception e) {
            handleException(e);
            return null; // 不会执行到这里，因为handleException会抛出异常
        }
    }

    private Map<String, Object> buildRequestBody(String content) {
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
            "role", "system",
            "content", "你是一个专业的新闻编辑，请对文章进行简要的总结，包括主要观点和关键信息。要求：简洁、准确、客观。"
        ));
        messages.add(Map.of("role", "user", "content", content));

        return Map.of(
            "model", config.getModel(),
            "messages", messages,
            "temperature", config.getTemperature(),
            "max_tokens", config.getMaxTokens(),
            "stream", false
        );
    }

    private String sendRequest(String apiUrl, Map<String, Object> requestBody) {
        return webClient.post()
            .uri(apiUrl)
            .header("Authorization", "Bearer " + config.getApiKey())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                clientResponse -> handleErrorResponse(clientResponse))
            .bodyToMono(String.class)
            .block();
    }

    private Mono<? extends Throwable> handleErrorResponse(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
            .flatMap(errorBody -> {
                log.error("OpenAI API 调用失败: status={}, body={}", 
                    clientResponse.statusCode(), errorBody);
                return Mono.error(new BusinessException(
                    StatusCode.INTERNAL_SERVER_ERROR, 
                    "AI服务调用失败: " + clientResponse.statusCode()));
            });
    }

    private String parseResponse(String response) throws Exception {
        JsonNode responseNode = objectMapper.readTree(response);
        if (!responseNode.has("choices") || !responseNode.get("choices").isArray() 
            || responseNode.get("choices").size() == 0) {
            throw new BusinessException(StatusCode.INTERNAL_SERVER_ERROR, 
                "AI服务返回数据格式错误");
        }

        return responseNode.path("choices").get(0)
            .path("message").path("content").asText();
    }

    private void handleException(Exception e) {
        log.error("调用 OpenAI API 失败: {}", e.getMessage());
        if (e instanceof BusinessException) {
            throw (BusinessException) e;
        }
        throw new BusinessException(StatusCode.INTERNAL_SERVER_ERROR, 
            "AI分析失败: " + e.getMessage());
    }
} 