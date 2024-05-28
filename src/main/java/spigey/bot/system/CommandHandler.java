package spigey.bot.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.simple.parser.ParseException;
import spigey.bot.DiscordBot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static spigey.bot.system.util.*;

public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandHandler() {
        registerCommands();
    }

    void registerCommands() {
        try {
            String path = "spigey/bot/Commands";
            File[] files = new File(getClass().getClassLoader().getResource(path).getFile()).listFiles((dir, name) -> name.endsWith(".class"));
            if (files == null) {
                return;
            }

            for (File file : files) {
                String className = file.getName().replace(".class", "");
                Class<?> cls = Class.forName("spigey.bot.Commands." + className);
                if (Command.class.isAssignableFrom(cls)) {
                    Command command = (Command) cls.getDeclaredConstructor().newInstance();
                    String commandName = className.replace("Command", "").toLowerCase();
                    commands.put(commandName, command);
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    private void doTheActualShit(MessageReceivedEvent event) {
        String[] split = event.getMessage().getContentRaw().split(" ");
        if (!split[0].startsWith(DiscordBot.prefix)) return;
        String commandName = split[0].substring(1).toLowerCase();
        Command command = commands.get(commandName);

        if (command != null) {
            String[] args = new String[split.length - 1];
            System.arraycopy(split, 1, args, 0, args.length);
            try {
                command.execute(event, args);
            } catch (Exception e) {
                util.init(event, this);
                error("An error has occurred while executing " + command.getClass().getSimpleName() + ":\n" + e + "\nMessage: " + content(), false);
                msg("An error occurred while executing " + command.getClass().getSimpleName() + ": ```" + (e.toString().length() > 1000 ? e.toString().substring(0, 1000) + "..." : e) + "```");
            }
        }
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        doTheActualShit(event);
    }
}
