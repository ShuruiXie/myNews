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

/**
 * OpenAI服务实现类
 * 负责与OpenAI API进行交互，处理AI文本摘要功能
 */
@Service
@Slf4j
public class OpenAIServiceImpl implements AIService {

    @Autowired
    public OpenAIConfig config;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public OpenAIServiceImpl() {
        this.webClient = WebClient.builder()
            .codecs(configurer -> configurer.defaultCodecs()
                .maxInMemorySize(16 * 1024 * 1024))
            .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 对文本内容进行摘要
     * @param content 需要摘要的文本内容
     * @return 摘要结果
     * @throws ValidationException 当content为空时抛出
     * @throws BusinessException 当API调用失败时抛出
     */
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

    /**
     * 构建OpenAI API请求体
     * @param content 用户输入的文本内容
     * @return 符合OpenAI API格式的请求体
     */
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

    /**
     * 发送HTTP请求到OpenAI API
     * @param apiUrl API端点URL
     * @param requestBody 请求体
     * @return API响应内容
     */
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

    /**
     * 处理API错误响应
     * @param clientResponse HTTP响应对象
     * @return 包装成Mono的异常对象
     */
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

    /**
     * 解析API响应内容
     * @param response API原始响应字符串
     * @return 提取的摘要内容
     * @throws Exception 当响应格式不正确时抛出
     */
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

    /**
     * 统一异常处理
     * @param e 捕获的异常
     * @throws BusinessException 包装后的业务异常
     */
    private void handleException(Exception e) {
        log.error("调用 OpenAI API 失败: {}", e.getMessage());
        if (e instanceof BusinessException) {
            throw (BusinessException) e;
        }
        throw new BusinessException(StatusCode.INTERNAL_SERVER_ERROR, 
            "AI分析失败: " + e.getMessage());
    }
} 