package com.nerdtranslator.translateapibridge.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nerdtranslator.translateapibridge.service.LanguagesService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LanguagesServiceImpl implements LanguagesService {
    private static final Logger log = LoggerFactory.getLogger(LanguagesServiceImpl.class);
    @Override
    public Map<String, String> getSupportedLanguages() {
        String filePath = "data/TranslationApiLanguages.json";
        Map<String, String> languageMap;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            JsonNode jsonNode = objectMapper.readTree(inputStream);
            languageMap = getLanguageMap(jsonNode);
        } catch (IOException e) {
            log.error("An error occurred in LanguagesServiceImpl", e);
            throw new RuntimeException("LanguagesServiceImpl" + e.getMessage());
        }
        return languageMap;
    }

    private static Map<String, String> getLanguageMap(JsonNode jsonNode) {
        Map<String, String> languageMap = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            languageMap.put(entry.getKey(), entry.getValue().asText());
        }

        return languageMap;
    }
}
