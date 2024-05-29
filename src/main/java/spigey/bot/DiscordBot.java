package spigey.bot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import spigey.bot.system.db;
import spigey.bot.system.util;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import spigey.bot.system.CommandHandler;
import spigey.bot.system.env;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static spigey.bot.system.util.*;
import static spigey.bot.system.util.msg;

public class DiscordBot extends ListenerAdapter {
    static CommandHandler commandHandler;

    public static String prefix;

    public static void main(String[] args) throws IOException, ParseException {
        JSONObject config = (JSONObject) new JSONParser().parse(new FileReader("src/main/java/spigey/bot/config.json"));
        db.setDefaultValue((String) config.get("DEFAULT_VALUE"));
        prefix = (String) config.get("PREFIX");
        JDABuilder.createDefault(env.TOKEN)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new DiscordBot())
                .build();
        log("  _____  _                       _   ____        _   \n |  __ \\(_)                     | | |  _ \\      | |  \n | |  | |_ ___  ___ ___  _ __ __| | | |_) | ___ | |_ \n | |  | | / __|/ __/ _ \\| '__/ _` | |  _ < / _ \\| __|\n | |__| | \\__ \\ (_| (_) | | | (_| | | |_) | (_) | |_ \n |_____/|_|___/\\___\\___/|_|  \\__,_| |____/ \\___/ \\__|\n                                                     ");
        commandHandler = new CommandHandler();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        try {
            commandHandler.onMessageReceived(event);
        } catch (Exception e) {
            util.init(event, this);
            error("An error has occurred while executing Command:\n" + e + "\nMessage: " + event.getMessage().getContentRaw(), false);
            msg("An error occurred while executing Command: ```" + (e.toString().length() > 1000 ? e.toString().substring(0, 1000) + "..." : e) + "```");
        }
    }
}
