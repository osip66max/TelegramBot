package com.example.ling_bot.appconfig;

import com.example.ling_bot.LingTelegramBot;
import com.example.ling_bot.botapi.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import javax.sql.DataSource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    private DefaultBotOptions.ProxyType proxyType;
    private String proxyHost;
    private int proxyPort;

    private String dataDriverClassName;
    private String dataURL;
    private String dataUserName;
    private String dataPassword;

    @Bean
    public LingTelegramBot lingTelegramBot(TelegramFacade telegramFacade) {
        DefaultBotOptions options = new DefaultBotOptions();

        options.setProxyHost(proxyHost);
        options.setProxyPort(proxyPort);
        options.setProxyType(proxyType);

        LingTelegramBot lingTelegramBot = new LingTelegramBot(options, telegramFacade);
        lingTelegramBot.setBotUserName(botUserName);
        lingTelegramBot.setBotToken(botToken);
        lingTelegramBot.setWebHookPath(webHookPath);

        return lingTelegramBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(dataDriverClassName);
        dataSource.setUrl(dataURL);
        dataSource.setUsername(dataUserName);
        dataSource.setPassword(dataPassword);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }
}
