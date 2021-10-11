package com.karankumar.bookproject.mock;

import java.util.HashMap;
import java.util.Map;

public class MockEmailTemplate {

    public static Map<String, Object> getTestTemplate(String recipient){
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", recipient);
        templateModel.put("senderName", "mockSenderName");
        templateModel.put("text", "This is test message");
        return templateModel;
    }
}
