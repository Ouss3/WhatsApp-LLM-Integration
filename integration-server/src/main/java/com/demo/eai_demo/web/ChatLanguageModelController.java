package com.demo.eai_demo.web;


import com.demo.eai_demo.service.ConversationalAIAgent;
import com.demo.eai_demo.service.WhatsAppService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.model.chat.ChatLanguageModel;

import lombok.*;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project Name: eai_demo
 * File Name: controler
 * Created by: DELL
 * Created on: 11/25/2024
 * Description:
 * <p>
 * controler is a part of the eai_demo project.
 */

@RestController
public class ChatLanguageModelController {

    ChatLanguageModel chatLanguageModel;
    @Autowired
    ConversationalAIAgent conversationalAIAgent;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    WhatsAppService whatsAppService;
    @Autowired
    private ProducerTemplate producerTemplate;


    private final Map<Pattern, Consumer<MessageContext>> handlers = Map.of(
            // Add User pattern (case-insensitive, spaces optional)
            Pattern.compile("(?i)^name:\\s*(.+);\\s*(.+),\\s*email:\\s*(.+)$"), this::handleAddUser,
            // Fetch User pattern (case-insensitive, spaces optional)
            Pattern.compile("(?i)^fetch user:\\s*email:\\s*(.+)$"), this::handleFetchUser,
            // Delete User pattern (case-insensitive, spaces optional)
            Pattern.compile("(?i)^delete user:\\s*(\\d+)$"), this::handleDeleteUser,
            // Fetch All Users pattern
            Pattern.compile("(?i)^fetch all users$"), this::handleFetchAllUsers,
            // Modify User by ID pattern
            Pattern.compile("(?i)^modify user id:\\s*(\\d+),\\s*name:\\s*(.+),\\s*email:\\s*(.+),\\s*age:\\s*(\\d+)$"), this::handleModifyUserById,
           // Help pattern
            Pattern.compile("(?i)^help\\?$"), this::handleHelp
    );

    ChatLanguageModelController(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;

    }

    @GetMapping("/chat")
    public String model(@RequestParam(value = "message", defaultValue = "Hello") String message) {
        return chatLanguageModel.generate(message);
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> verifyWebhook(@RequestParam("hub.mode") String mode,
                                                @RequestParam("hub.challenge") String challenge,
                                                @RequestParam("hub.verify_token") String verifyToken) {
        // Check if the verify token matches
        if ("demo".equals(verifyToken)) {
            // Respond with the challenge to confirm the webhook URL
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token");
        }
    }



    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            JsonNode messages = jsonNode.at("/entry/0/changes/0/value/messages");
            if (messages.isArray() && messages.size() > 0) {
                String receivedText = messages.get(0).at("/text/body").asText();
                String fromNumber = messages.get(0).at("/from").asText();

                if (receivedText.toLowerCase().contains("chat:")) {
                    // Trigger the AI agent
                    String aiResponse = conversationalAIAgent.chat(receivedText.replaceFirst("(?i)chat:", "").trim());
                    this.whatsAppService.sendWhatsAppMessage(fromNumber, aiResponse);
                    return ResponseEntity.ok("AI response sent");
                }
                for (Map.Entry<Pattern, Consumer<MessageContext>> entry : handlers.entrySet()) {
                    Matcher matcher = entry.getKey().matcher(receivedText);
                    if (matcher.matches()) {
                        // Trigger the corresponding handler
                        entry.getValue().accept(new MessageContext(receivedText, fromNumber, matcher));
                        return ResponseEntity.ok("Database action executed");
                    }
                }

                this.whatsAppService.sendWhatsAppMessage( fromNumber,conversationalAIAgent.chat(receivedText));
                return ResponseEntity.ok("Message sent");
            }
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
        }
        return null;
    }


    // Handler: Add User
    private void handleAddUser(MessageContext context) {
        String firstName = context.getMatcher().group(1).trim();
        String lastName = context.getMatcher().group(2).trim();
        String email = context.getMatcher().group(3).trim();

        // Create a map to hold the user data
        Map<String, Object> user = new HashMap<>();
        user.put("nom", firstName);
        user.put("prenom", lastName);
        user.put("email", email);
        user.put("password", "");
        user.put("token", "");
        user.put("roles", List.of("USER"));
        producerTemplate.sendBody("direct:saveClient", user);


        // Notify sender
        whatsAppService.sendWhatsAppMessage(context.getFromNumber(), "User " + firstName + " " + lastName + " has been added successfully.");
    }
    private void handleFetchUser(MessageContext context) {
        String email = context.getMatcher().group(1).trim();

        // Fetch user from the Client service using Apache Camel
        String userInfo = producerTemplate.requestBodyAndHeader("direct:getClientById", null, "email", email, String.class);

        // Notify sender
        whatsAppService.sendWhatsAppMessage(context.getFromNumber(), "Fetched user info: " + userInfo);
    }

    private void handleDeleteUser(MessageContext context) {
        int id = Integer.parseInt(context.getMatcher().group(1).trim());

        // Delete user from the Client service using Apache Camel
        producerTemplate.sendBodyAndHeader("direct:deleteClient", null, "id", id);

        // Notify sender
        whatsAppService.sendWhatsAppMessage(context.getFromNumber(), "User with ID " + id + " has been deleted.");
    }

    // Handler: Fetch All Users
    private void handleFetchAllUsers(MessageContext context) {
        // Fetch all users from the Client service using Apache Camel
        String userInfo = producerTemplate.requestBody("direct:getAllClients", null, String.class);
        String response = conversationalAIAgent.chat("no details only the reponse for my demande use icons and good representation ,t's a whatsapp msg i want to send for my user for the follwing  Fetched user info:  " + userInfo);

        // Notify sender
        whatsAppService.sendWhatsAppMessage(context.getFromNumber(), response);
    }

    // Handler: Modify User by ID
    private void handleModifyUserById(MessageContext context) {
        int id = Integer.parseInt(context.getMatcher().group(1).trim());
        String name = context.getMatcher().group(2).trim();
        String email = context.getMatcher().group(3).trim();
        int age = Integer.parseInt(context.getMatcher().group(4).trim());

        // Create a map to hold the user data
        Map<String, Object> user = Map.of(
                "id", id,
                "name", name,
                "email", email,
                "age", age
        );

        // Send the user data to the Client service using Apache Camel
        producerTemplate.sendBodyAndHeader("direct:updateClient", user, "id", id);

        // Notify sender
        whatsAppService.sendWhatsAppMessage(context.getFromNumber(), "User with ID " + id + " has been modified successfully.");
    }

    private void handleHelp(MessageContext context) {
        String helpMessage = """
                🌟 *Here are the commands you can use:*
                        
                ➕ *Add a user:* 
                _"name: <name>; <last name>, email: <email>"_
                        
                🔍 Fetch a user by email: 
                _"fetch user:  <email>"_
                        
                ❌ *Delete a user by email:* 
                _"delete user:  <ID>"_
                        
                📋 *Fetch all users:* 
                _"fetch all users"_
                        
                ✏️ *Modify a user by ID:* 
                _"modify user id: <id>, name: <name>, email: <email>, age: <age>"_
                        
                🤖 *To interact with the AI agent, start your message with "chat:".* For example:
                _"chat: What is the weather like today?"_
                """;



        //reply with the help message
        String response = conversationalAIAgent.chat("can you add icons to this reponse and good representation it's a help msg i will send via whatsapp give only the reponse no details :  " + helpMessage);

        // Send the help message to the sender
        whatsAppService.sendWhatsAppMessage(context.getFromNumber(), helpMessage);
    }
    @AllArgsConstructor@Getter@ToString
    private static class MessageContext {
        private final String message;
        private final String fromNumber;
        private final Matcher matcher;


    }




}


