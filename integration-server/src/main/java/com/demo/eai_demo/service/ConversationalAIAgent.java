package com.demo.eai_demo.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Project Name: eai_demo
 * File Name: ai
 * Created by: DELL
 * Created on: 11/25/2024
 * Description:
 * <p>
 * ai is a part of the eai_demo project.
 */

@Component
public class ConversationalAIAgent {
    private final ChatLanguageModel chatLanguageModel;

    @Autowired
    public ConversationalAIAgent(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    public String chat(String message) {
        System.out.println("Generating response for message: " + message);
        return chatLanguageModel.generate(message);  // Check if this generates the response correctly
    }

}

