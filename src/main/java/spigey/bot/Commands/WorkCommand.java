package spigey.bot.Commands;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.simple.parser.ParseException;
import spigey.bot.system.*;

import java.io.IOException;

import static spigey.bot.system.util.*;

public class WorkCommand implements Command {
    private final CooldownManager cooldown = new CooldownManager(30000);
    @Override
    public void execute(MessageReceivedEvent event, String[] args) throws IOException, ParseException {
        util.init(event, this);
        if(cooldown.isActive(event.getAuthor())){
            msg("You have to wait " + cooldown.parse(event.getAuthor()) + " before working again!");
            return;
        }
        int random = (int) (Math.random() * 30);
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(author() + " is working!")
                .setDescription("You went working and have received " + random + "$ as a reward!")
                .setColor(EmbedColor.BLURPLE);
        msg(embed);
        db.add(event.getAuthor().getId(), "money", random);
        cooldown.update(event.getAuthor());
    }
}