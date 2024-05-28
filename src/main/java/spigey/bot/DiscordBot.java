package spigey.bot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.ParseException;
import spigey.bot.system.util;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import spigey.bot.system.CommandHandler;
import spigey.bot.system.env;

import javax.security.auth.login.LoginException;
import java.io.IOException;

import static spigey.bot.system.util.debug;
import static spigey.bot.system.util.log;

public class DiscordBot extends ListenerAdapter {
    static CommandHandler commandHandler;

    public static String prefix = "!";

    public static void main(String[] args) {
        JDABuilder.createDefault(env.TOKEN)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new DiscordBot())
                .build();
        log("  _____  _                       _   ____        _   \n |  __ \\(_)                     | | |  _ \\      | |  \n | |  | |_ ___  ___ ___  _ __ __| | | |_) | ___ | |_ \n | |  | | / __|/ __/ _ \\| '__/ _` | |  _ < / _ \\| __|\n | |__| | \\__ \\ (_| (_) | | | (_| | | |_) | (_) | |_ \n |_____/|_|___/\\___\\___/|_|  \\__,_| |____/ \\___/ \\__|\n                                                     ");
        commandHandler = new CommandHandler();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        commandHandler.onMessageReceived(event);
    }
}
