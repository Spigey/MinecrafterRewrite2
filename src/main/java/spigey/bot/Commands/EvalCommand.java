package spigey.bot.Commands;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import spigey.bot.system.Command;
import spigey.bot.system.util;

import java.beans.Statement;

import static spigey.bot.system.util.content;
import static spigey.bot.system.util.msg;

public class EvalCommand implements Command {
    private String text;
    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        util.init(event, this);
        text = content();
        if(!event.getAuthor().getId().equals("1203448484498243645")){msg("fuck nuh uh"); return;}
        msg(content(1));
    }
}