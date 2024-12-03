package com.demo.eai_demo.service;

/**
 * Project Name: eai_demo
 * File Name: tes
 * Created by: DELL
 * Created on: 11/26/2024
 * Description:
 * <p>
 * tes is a part of the eai_demo project.
 */

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppService {

    @Autowired
    private ProducerTemplate producerTemplate;

    public void sendWhatsAppMessage(String toNumber, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", toNumber);
        body.put("type", "text");

        Map<String, String> text = new HashMap<>();
        text.put("body", message);
        body.put("text", text);

        // Send the body to the "direct:sendWhatsAppMessage" Camel endpoint
        producerTemplate.sendBody("direct:sendWhatsAppMessage", body);
    }
}

