package com.nerdtranslator.lingueeapibridge.service.impl;

import com.google.cloud.translate.v3.*;
import com.nerdtranslator.lingueeapibridge.service.CredentialsProviderFactory;
import com.nerdtranslator.lingueeapibridge.service.TranslationApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TranslationApiServiceImpl implements TranslationApiService {
    private final CredentialsProviderFactory credentialsProviderFactory;

    @Override
    public List<String> getTranslationsFromApi(String originalText, String originalLanguage, String targetLanguage) {
        try (TranslationServiceClient client = TranslationServiceClient.create(
                TranslationServiceSettings
                        .newBuilder()
                        .setCredentialsProvider(credentialsProviderFactory.getCredentialsProvider())
                        .build())) {
            TranslateTextRequest request = TranslateTextRequest.newBuilder()
                    .setMimeType("text/plain")
                    .setTargetLanguageCode(targetLanguage)
                    .addContents(originalText)
                    .build();

            TranslateTextResponse response = client.translateText(request);
            return response.getTranslationsList()
                    .stream()
                    .map(Translation::getTranslatedText)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String getSingleTranslationFromApi(String originalText, String originalLanguage, String targetLanguage) {
        try (TranslationServiceClient client = TranslationServiceClient.create(
                TranslationServiceSettings
                        .newBuilder()
                        .setCredentialsProvider(credentialsProviderFactory.getCredentialsProvider())
                        .build())) {
            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
                            .setMimeType("text/plain")
                            .setTargetLanguageCode(targetLanguage)
                            .addContents(originalText)
                            .build();

            TranslateTextResponse response = client.translateText(request);
            return response.getTranslations(1).getTranslatedText();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
