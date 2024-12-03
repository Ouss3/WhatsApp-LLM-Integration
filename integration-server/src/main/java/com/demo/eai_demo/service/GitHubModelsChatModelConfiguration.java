package com.demo.eai_demo.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.github.GitHubModelsChatModel;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Project Name: eai_demo
 * File Name: Assistant
 * Created by: DELL
 * Created on: 11/25/2024
 * Description:
 * <p>
 * Assistant is a part of the eai_demo project.
 */

@Configuration
@Profile("github")
public class GitHubModelsChatModelConfiguration {

    @Value("${GITHUB_TOKEN}")
    private String gitHubToken;

    @Bean
   public ChatLanguageModel gitHubModelsChatLanguageModel() {
        return GitHubModelsChatModel.builder()
                .gitHubToken(gitHubToken)
                .modelName("gpt-4o-mini")
                .logRequestsAndResponses(true)
                .build();
    }
}
