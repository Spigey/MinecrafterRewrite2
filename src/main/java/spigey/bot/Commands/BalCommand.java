package spigey.bot.Commands;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.simple.parser.ParseException;
import spigey.bot.system.Command;
import spigey.bot.system.db;
import spigey.bot.system.util;

import java.io.IOException;

import static spigey.bot.system.util.msg;

public class BalCommand implements Command {
    @Override
    public void execute(MessageReceivedEvent event, String[] args) throws IOException, ParseException {
        util.init(event, this);
        msg(db.read(event.getAuthor().getId(), "money"));
    }
}