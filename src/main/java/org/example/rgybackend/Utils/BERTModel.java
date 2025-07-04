package org.example.rgybackend.Utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BERTModel {
    @Autowired
    private RestTemplate restTemplate;

    public ModelResponse checkEmotion(String text) {
        String url = "http://localhost:8000/api/check/emotion";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", text);
        ModelResponse response = restTemplate.postForObject(url, requestBody, ModelResponse.class);
        if(response == null) {
            throw new RuntimeException("调用API失败");
        }
        return response;
    }

    public ModelResponse checkCrisis(String text) {
        String url = "http://localhost:8000/api/check/crisis";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", text);
        ModelResponse response = restTemplate.postForObject(url, requestBody, ModelResponse.class);
        if(response == null) {
            throw new RuntimeException("调用API失败");
        }
        return response;
    }

}
