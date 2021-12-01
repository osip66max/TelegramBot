package com.example.ling_bot.model;

import com.example.ling_bot.service.ReplyMessageService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileData implements Serializable {

    String id;
    String name;
    String gender;
    String color;
    String movie;
    String song;
    int age;
    int number;
    String chatId;

    public String toString(String localeTag, ReplyMessageService messageService) {
        return String.format(messageService.getReplyText(localeTag, "reply.name") + " %s%n" +
                messageService.getReplyText(localeTag, "reply.age") + " %d%n" +
                messageService.getReplyText(localeTag, "reply.gender") + " %s%n" +
                messageService.getReplyText(localeTag, "reply.number") + " %d%n" +
                messageService.getReplyText(localeTag, "reply.color") + " %s%n" +
                messageService.getReplyText(localeTag, "reply.movie") + " %s%n" +
                messageService.getReplyText(localeTag, "reply.song") + " %s%n",
                getName(), getAge(), getGender(), getNumber(), getColor(), getMovie(), getSong());
    }
}
