package spigey.bot.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static spigey.bot.DiscordBot.prefix;
import static spigey.bot.system.util.debug;

public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();
    private final Map<String, String> aliasToCommandMap = new HashMap<>();

    public CommandHandler() {
        loadCommands();
    }

    private void loadCommands() {
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

                    // Read annotations
                    if (cls.isAnnotationPresent(CommandInfo.class)) {
                        CommandInfo info = cls.getAnnotation(CommandInfo.class);
                        for (String alias : info.aliases()) {
                            debug("Registered alias " + alias + " for command " + className, false);
                            aliasToCommandMap.put(alias.toLowerCase(), commandName);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void reloadCommands() {
        commands.clear();
        aliasToCommandMap.clear();
        loadCommands();
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
                if(command.getClass().isAnnotationPresent(CommandInfo.class) && command.getClass().getAnnotation(CommandInfo.class).limitIds().length > 0 && !Arrays.asList(command.getClass().getAnnotation(CommandInfo.class).limitIds()).contains(event.getAuthor().getId())){event.getChannel().sendMessage(command.getClass().getAnnotation(CommandInfo.class).limitMsg()).queue(); return;}
                command.execute(event, args);
            } catch (Exception e) {
                // Error handling
            }
        }
    }

    public void onMessageReceived(MessageReceivedEvent event) throws Exception {
        doTheActualShit(event);
    }
}
