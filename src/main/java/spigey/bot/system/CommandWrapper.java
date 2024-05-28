package spigey.bot.system;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class CommandWrapper implements Command {

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        try {
            // Call the actual command's execute method
            executeCommand(event, args);
        } catch (Exception e) {
            // Handle any exceptions
            handleError(event, e);
        }
    }

    // Abstract method for the actual command logic
    protected abstract void executeCommand(MessageReceivedEvent event, String[] args) throws Exception;

    // Method to handle errors
    private void handleError(MessageReceivedEvent event, Exception e) {
        event.getChannel().sendMessage("An error occurred: " + e.getMessage()).queue();
        e.printStackTrace();
    }
}