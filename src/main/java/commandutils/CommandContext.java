package commandutils;


import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * This class contains context about a command that is run.
 */
public class CommandContext {

    // The Member that ran the command
    private Member member;

    // The TextChannel the command was ran in
    private TextChannel channel;

    // The Message that was sent containing the command
    private Message message;

    // The label used to call the command
    private String label;

    // The args passed with the command
    private String[] args;

    /**
     *
     * @param member The Member that ran the command
     * @param channel The TextChannel the command was ran in
     * @param message The Message that was sent containing the command
     * @param label The label used to call the command
     * @param args The args passed with the command
     */
    public CommandContext(Member member, TextChannel channel, Message message, String label, String[] args) {
        this.member = member;
        this.channel = channel;
        this.message = message;
        this.label = label;
        this.args = args;
    }

    /**
     *
     * @return The Member that ran the command
     */
    public Member getMember() {
        return member;
    }

    /**
     *
     * @return The TextChannel the command was ran in
     */
    public TextChannel getChannel() {
        return channel;
    }

    /**
     *
     * @return The Message that was sent containing the command
     */
    public Message getMessage() {
        return message;
    }

    /**
     *
     * @return The label used to call the command
     */
    public String getLabel() {
        return label;
    }

    /**
     *
     * @return The args passed with the command
     */
    public String[] getArgs() {
        return args;
    }

}

