package spigey.bot.system;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.json.simple.parser.ParseException;
import spigey.bot.DiscordBot;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import static spigey.bot.DiscordBot.prefix;
import static spigey.bot.system.util.*;

public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();
    private final Map<String, String> aliasToCommandMap = new HashMap<>();

    public CommandHandler() {
        registerCommands();
    }

    private void registerCommands() {
        try {
            debug("Registering commands", false);
            String path = "spigey/bot/Commands";
            File[] files = new File(getClass().getClassLoader().getResource(path).getFile()).listFiles((dir, name) -> name.endsWith(".class"));
            if (files == null) return;

            for (File file : files) {
                String className = "spigey.bot.Commands." + file.getName().replace(".class", "");
                Class<?> cls = Class.forName(className);
                if (Command.class.isAssignableFrom(cls)) {
                    Command command = (Command) cls.getDeclaredConstructor().newInstance();
                    String commandName = className.substring(className.lastIndexOf('.') + 1).replace("Command", "").toLowerCase();
                    debug("Registered command " + className, false);
                    commands.put(commandName, command);

                    // Register aliases
                    try {
                        Field aliasesField = cls.getDeclaredField("ALIASES");
                        aliasesField.setAccessible(true);
                        String[] aliases = (String[]) aliasesField.get(null);
                        for (String alias : aliases) {
                            debug("Registered alias " + alias + " for command " + className, false);
                            aliasToCommandMap.put(alias.toLowerCase(), commandName);
                        }
                    } catch (NoSuchFieldException e) {
                    }
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void doTheActualShit(MessageReceivedEvent event) throws IOException {
        String[] split = event.getMessage().getContentRaw().split(" ");
        if (!split[0].startsWith(prefix)) return;

        String commandName = split[0].substring(1).toLowerCase();
        String resolvedCommandName = aliasToCommandMap.getOrDefault(commandName, commandName);
        Command command = commands.get(resolvedCommandName);

        if (command != null) {
            String[] args = new String[split.length - 1];
            System.arraycopy(split, 1, args, 0, args.length);
            try {
                command.execute(event, args);
            } catch (Exception e) {
                init(event, this);
                StringBuilder err = new StringBuilder(e + "\n   ");
                for(int i = 0; i < e.getStackTrace().length - 1; i++){
                    err.append(e.getStackTrace()[i]).append("\n   ");
                }
                err.append(e.getStackTrace()[e.getStackTrace().length - 1]);
                error("An error has occurred while executing " + command.getClass().getSimpleName() + ":\n" + e + "\nMessage: " + event.getMessage().getContentRaw(), false);
                msg("An error occurred while executing " + command.getClass().getSimpleName() + ": ```" + (err.toString().length() > 1000 ? err.substring(0, 1000) + "..." : err.toString()) + "```\nThis error has been automatically reported.");
                TextChannel channel = event.getJDA().getGuildById("1211627879243448340").getTextChannelById("1245302943951880303");
                MessageEmbed embed = new EmbedBuilder()
                        .setTitle("Error Report")
                        .setDescription(String.format("Message: ```%s```\nAuthor Username: `%s`\nAuthor ID: `%s`",event.getMessage().getContentRaw(), event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getId()))
                        .setColor(EmbedColor.RED)
                        .build();
                Path temp = Files.createTempFile("error", ".txt");
                Files.writeString(temp, err);
                channel.sendMessage("<@" + event.getJDA().retrieveApplicationInfo().complete().getOwner().getId() + ">").addEmbeds(embed).addFiles(FileUpload.fromData(err.toString().getBytes(StandardCharsets.UTF_8), "error_report.txt")).queue();
            }
        }
    }

    public void onMessageReceived(MessageReceivedEvent event) throws Exception {
        doTheActualShit(event);
    }
}
