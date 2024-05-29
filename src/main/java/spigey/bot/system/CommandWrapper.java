package spigey.bot.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static spigey.bot.system.util.error;
import static spigey.bot.system.util.msg;

public abstract class CommandWrapper implements Command {

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        try {
            executeCommand(event, args);
        } catch (Exception e) {
            util.init(event, this);
            error("An error has occurred while executing Command:\n" + e + "\nMessage: " + event.getMessage().getContentRaw(), false);
            msg("An error occurred while executing Command: ```" + (e.toString().length() > 1000 ? e.toString().substring(0, 1000) + "..." : e) + "```");
        }
    }
    protected abstract void executeCommand(MessageReceivedEvent event, String[] args) throws Exception;
}