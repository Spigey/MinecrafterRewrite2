package spigey.bot.system;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static spigey.bot.DiscordBot.prefix;

public class util {
    private static MessageReceivedEvent eventt;
    private static Object classs;
    public static void init(MessageReceivedEvent event, Object Class){
        eventt = event;
        classs = Class;
    }
    public static String content(){
        if(eventt.getMessage().getContentRaw().length() < (classs.getClass().getSimpleName().replace("Command","").length() + prefix.length() + 1)) return "";
        return eventt.getMessage().getContentRaw().substring((classs.getClass().getSimpleName().replace("Command","").length() + prefix.length() + 1));
    }
    public static String content(int index){
        String out;
        try{out = eventt.getMessage().getContentRaw().split(" ")[index];}
        catch(Exception L){return null;}
        return eventt.getMessage().getContentRaw().split(" ")[index];
    }
    public static void msg(String content){
        if(content == null) {eventt.getChannel().sendMessage("null").queue(); return;}
        eventt.getChannel().sendMessage(content.replaceAll(env.TOKEN, "Fuck nuh uh")).queue();
    }
    public static void msg(EmbedBuilder embed){
        if(embed == null) embed = new EmbedBuilder().setDescription("null");
        eventt.getChannel().sendMessage("").setEmbeds(embed.build()).queue();
    }
    public static void msg(String content, EmbedBuilder embed){
        if(embed == null) embed = new EmbedBuilder().setDescription("null");
        if(content == null) content = "null";
        eventt.getChannel().sendMessage(content).setEmbeds(embed.build()).queue();
    }
    public static String author(){
        return eventt.getMember().getEffectiveName();
    }
    public static String authorId(){
        return eventt.getAuthor().getId();
    }
    public static void log(String content){
        System.out.println(content);
    }
    public static void debug(Object content, boolean chat){ // basically for gemini to understand that this will be debug, not basic output
        log("\u001B[42;30m[DEBUG]: " + content + " \u001B[49m");
        if(chat) msg("```\n" + content + "\n```");
    }
    public static void error(Object content, boolean chat) {
        log("\u001B[41;30m[ERROR]: " + content + "\u001B[0m");
        if(chat) msg("```\n" + content + "\n```");
    }
    public static void warn(Object content, boolean chat) {
        log("\u001B[43;30m[WARN]: " + content + "\u001B[0m");
        if(chat) msg("```\n" + content + "\n```");
    }
}

