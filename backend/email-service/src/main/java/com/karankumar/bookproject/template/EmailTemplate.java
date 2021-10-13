package com.karankumar.bookproject.template;

import com.karankumar.bookproject.constant.EmailConstant;

import java.util.HashMap;
import java.util.Map;

public class EmailTemplate {
    private EmailTemplate() {
    }

    public static Map<String,Object> getAccountCreatedEmailTemplate(String username){
        Map<String, Object> templateModel = getTemplate(username);
        templateModel.put("text", EmailConstant.ACCOUNT_CREATED_MESSAGE);
        return templateModel;
    }

    public static Map<String,Object> getAccountDeletedEmailTemplate(String username){
        Map<String, Object> templateModel = getTemplate(username);
        templateModel.put("text", EmailConstant.ACCOUNT_DELETED_MESSAGE);
        return templateModel;
    }

    public static  Map<String,Object> getChangePasswordEmailTemplate(String username){
        Map<String, Object> templateModel = getTemplate(username);
        templateModel.put("text", EmailConstant.ACCOUNT_PASSWORD_CHANGED_MESSAGE);
        return templateModel;
    }

    private static Map<String, Object> getTemplate(String username){
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", username);
        templateModel.put("senderName", EmailConstant.KARANKUMAR);
        return templateModel;
    }
}
