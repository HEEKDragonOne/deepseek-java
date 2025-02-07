package com.tothefor.deepseekjava.client;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author DragonOne
 * @since 2025/2/7 10:28
 */

@Component
@ConfigurationProperties(prefix = "ai.deepseek")
public class DeepSeekClient {
    private static Logger log = LoggerFactory.getLogger(DeepSeekClient.class);


    @Value("${ai.deepseek.baseUrl}")
    private String baseUrl;

    @Value("${ai.deepseek.model}")
    private String model;

    @Value("${ai.deepseek.timeout}")
    private Integer timeout;

    public DeepSeekClient() {
    }

    public DeepSeekClient(String baseUrl, String model, Integer timeout) {
        this.baseUrl = baseUrl;
        this.model = model;
        this.timeout = timeout;
    }

    public List<JSONObject> MESSAGES = new ArrayList<>();

    public String reply(String input) {
        List<JSONObject> msgArr = new ArrayList<>();
        JSONObject msg = new JSONObject();
        msg.put("role", "user");
        msg.put("content", input);

        msgArr.addAll(MESSAGES);
        msgArr.add(msg);
        MESSAGES.add(msg);
        log.info("current query message : {}", msg);

        JSONObject data = new JSONObject();
        data.put("messages", msgArr);
        JSONObject formatObj = new JSONObject();
        formatObj.put("type", "json_object");
        data.put("response_format", formatObj);
        data.put("model", model);

        HttpResponse execute = HttpRequest.post(baseUrl)
                .header(Header.CONTENT_TYPE, "application/json")
                .body(data.toString())
                .timeout(1000 * timeout)
                .execute();


        String body = execute.body();
        body = body.trim();
        body = "[" + body + "]";
        JSONArray parseArray = JSON.parseArray(body);

        StringBuffer sb = new StringBuffer("");
        for (Object item : parseArray) {
            JSONObject obj = (JSONObject) item;
            JSONObject message = obj.getJSONObject("message");
            if (Objects.nonNull(message)) {
                MESSAGES.add(message);
                String content = message.getString("content");
                sb.append(content);
            } else {
                log.error("response={}", item);
            }
        }

        return sb.toString();
    }

}
