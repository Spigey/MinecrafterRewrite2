package spigey.bot.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public interface Command {
    void execute(MessageReceivedEvent event, String[] args) throws IOException, ParseException, InterruptedException;
}