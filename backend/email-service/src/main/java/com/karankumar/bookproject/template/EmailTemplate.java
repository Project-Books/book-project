/*
The book project lets a user keep track of different books they would like to read, are currently
reading, have read or did not finish.
Copyright (C) 2021  Karan Kumar
This program is free software: you can redistribute it and/or modify it under the terms of the
GNU General Public License as published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program.
If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.template;

import com.karankumar.bookproject.ExcludeFromJacocoGeneratedReport;
import com.karankumar.bookproject.constant.EmailConstant;

import java.util.HashMap;
import java.util.Map;

@ExcludeFromJacocoGeneratedReport
public class EmailTemplate {
  private EmailTemplate() {}

  public static Map<String, Object> getAccountCreatedEmailTemplate(String username) {
    Map<String, Object> templateModel = getTemplate(username);
    templateModel.put("text", EmailConstant.ACCOUNT_CREATED_MESSAGE);
    return templateModel;
  }

  public static Map<String, Object> getAccountDeletedEmailTemplate(String username) {
    Map<String, Object> templateModel = getTemplate(username);
    templateModel.put("text", EmailConstant.ACCOUNT_DELETED_MESSAGE);
    return templateModel;
  }

  public static Map<String, Object> getChangePasswordEmailTemplate(String username) {
    Map<String, Object> templateModel = getTemplate(username);
    templateModel.put("text", EmailConstant.ACCOUNT_PASSWORD_CHANGED_MESSAGE);
    return templateModel;
  }

  private static Map<String, Object> getTemplate(String username) {
    Map<String, Object> templateModel = new HashMap<>();
    templateModel.put("recipientName", username);
    templateModel.put("senderName", EmailConstant.KARANKUMAR);
    return templateModel;
  }
}
