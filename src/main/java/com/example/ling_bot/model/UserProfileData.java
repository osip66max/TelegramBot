package com.example.ling_bot.model;

import com.example.ling_bot.service.ReplyMessageService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "userprofiledata")
public class UserProfileData implements Serializable {

    @Id
    String id;
    String name;
    String gender;
    String color;
    String movie;
    String song;
    int age;
    int number;
    @Column(name = "chatid")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserProfileData that = (UserProfileData) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
