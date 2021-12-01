package com.example.ling_bot.botapi.handlers.askdestiny;

import com.example.ling_bot.botapi.BotState;
import com.example.ling_bot.botapi.InputMessageHandler;
import com.example.ling_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AskDestinyHandler implements InputMessageHandler {
    private final ReplyMessageService messagesService;

    public AskDestinyHandler(ReplyMessageService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_DESTINY;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        String localeTag = inputMsg.getFrom().getLanguageCode();
        String chatId = String.valueOf(inputMsg.getChatId());

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, localeTag, "reply.askDestiny");
        replyToUser.setReplyMarkup(getInlineMessageButtons(localeTag));

        return replyToUser;
    }

    private InlineKeyboardMarkup getInlineMessageButtons(String localeTag) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonYes = new InlineKeyboardButton();
        InlineKeyboardButton buttonNo = new InlineKeyboardButton();
        InlineKeyboardButton buttonIWillThink = new InlineKeyboardButton();
        InlineKeyboardButton buttonIDontKnow = new InlineKeyboardButton();

        buttonYes.setText(messagesService.getReplyText(localeTag, "reply.yes"));
        buttonNo.setText(messagesService.getReplyText(localeTag, "reply.no"));
        buttonIWillThink.setText(messagesService.getReplyText(localeTag, "reply.iWillThink"));
        buttonIDontKnow.setText(messagesService.getReplyText(localeTag, "reply.iDontKnow"));

        buttonYes.setCallbackData("buttonYes");
        buttonNo.setCallbackData("buttonNo");
        buttonIWillThink.setCallbackData("buttonIWillThink");
        buttonIDontKnow.setCallbackData("-");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonYes);
        keyboardButtonsRow1.add(buttonNo);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonIWillThink);
        keyboardButtonsRow2.add(buttonIDontKnow);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
