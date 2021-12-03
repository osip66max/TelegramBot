package com.example.ling_bot.utils;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emojis {
    SPARKLES(EmojiParser.parseToUnicode(":sparkles:")),
    SCROLL(EmojiParser.parseToUnicode(":scroll:")),
    MAGE(EmojiParser.parseToUnicode(":mage:")),
    LIKE(EmojiParser.parseToUnicode(":+1:")),
    HMM(EmojiParser.parseToUnicode(":thinking:")),
    SOS(EmojiParser.parseToUnicode(":sos:")),
    BACKWARD(EmojiParser.parseToUnicode(":arrow_backward:")),
    TV(EmojiParser.parseToUnicode(":tv:")),
    OLDER(EmojiParser.parseToUnicode(":older_man:")),
    STAR(EmojiParser.parseToUnicode(":star:")),
    PRAY(EmojiParser.parseToUnicode(":pray:"));

    private final String emojiName;

    @Override
    public String toString() {
        return emojiName;
    }
}
