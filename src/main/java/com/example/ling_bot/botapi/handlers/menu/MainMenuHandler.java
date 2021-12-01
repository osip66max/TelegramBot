package com.example.ling_bot.botapi.handlers.menu;

import com.example.ling_bot.botapi.BotState;
import com.example.ling_bot.botapi.InputMessageHandler;
import com.example.ling_bot.service.MainMenuService;
import com.example.ling_bot.service.ReplyMessageService;
import com.example.ling_bot.utils.Emojis;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MainMenuHandler implements InputMessageHandler {
    private final ReplyMessageService messagesService;
    private final MainMenuService mainMenuService;

    public MainMenuHandler(ReplyMessageService messagesService, MainMenuService mainMenuService) {
        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
    }
    @Override
    public SendMessage handle(Message message) {
        String localeTag = message.getFrom().getLanguageCode();
        return mainMenuService.getMainMenuMessage(String.valueOf(message.getChatId()), localeTag,
                messagesService.getReplyText(localeTag, "reply.showMainMenu", Emojis.MAGE));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }
}
