package net.wayward_realms.waywardlib.chat;

import net.wayward_realms.waywardlib.WaywardPlugin;
import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Represents a chat plugin
 *
 */
public interface ChatPlugin extends WaywardPlugin {

    /**
     * Gets a collection containing all of the chat channels
     *
     * @return a collection containing all chat channels
     */
    public Collection<? extends Channel> getChannels();

    /**
     * Gets the channel with the given name
     *
     * @param name the name of the channel
     * @return the channel with the given name
     */
    public Channel getChannel(String name);

    /**
     * Adds a channel
     *
     * @param channel the channel to add
     */
    public void addChannel(Channel channel);

    /**
     * Removes a channel
     *
     * @param channel the channel to remove
     */
    public void removeChannel(Channel channel);

    /**
     * Gets the channel the given player is speaking in
     *
     * @param player the player
     * @return the player's current channel
     */
    public Channel getPlayerChannel(Player player);

    /**
     * Sets the channel the player is speaking in
     *
     * @param player the player
     * @param channel the channel to make the player speak in
     */
    public void setPlayerChannel(Player player, Channel channel);

    /**
     * Gets a channel from it's IRC channel
     *
     * @param ircChannel the IRC channel
     * @return the channel
     */
    public Channel getChannelFromIrcChannel(String ircChannel);

    /**
     * Gets users in an IRC channel
     *
     * @param ircChannel the IRC channel
     * @return the user names in the channel
     */
    public Collection<String> getUsersInIrcChannel(String ircChannel);

    /**
     * Gets staff in an IRC channel
     *
     * @param ircChannel the IRC channel
     * @return the staff user names in the channel
     */
    public Collection<String> getStaffInIrcChannel(String ircChannel);

    /**
     * Gets a chat group by name
     *
     * @param name the name of the chat group
     * @return the chat group
     */
    public ChatGroup getChatGroup(String name);

    /**
     * Removes a chat group
     *
     * @param chatGroup the chat group to remove
     */
    public void removeChatGroup(ChatGroup chatGroup);

    /**
     * Adds a chat group
     *
     * @param chatGroup the chat group to add
     */
    public void addChatGroup(ChatGroup chatGroup);

}
