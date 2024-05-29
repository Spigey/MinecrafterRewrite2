package spigey.bot.Commands;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.simple.parser.ParseException;
import spigey.bot.system.*;

import java.io.IOException;

import static spigey.bot.system.util.author;
import static spigey.bot.system.util.msg;

public class BalCommand implements Command {
    @Override
    public void execute(MessageReceivedEvent event, String[] args) throws IOException, ParseException {
        util.init(event, this);
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(author() + "'s Inventory")
                .addField("Inventory", db.read(event.getAuthor().getId(), "money") + " " + CMoji.Cool, false)
                .addField("Chest", db.read(event.getAuthor().getId(), "chest") + " " + CMoji.Cool, false)
                .setColor(EmbedColor.BLURPLE);
        msg(embed);
    }
}