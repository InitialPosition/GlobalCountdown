package net.initialposition.globalcountdown;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginListener implements Listener {
    String banReason;
    boolean expired;

    public LoginListener(String banReason) {
        this.banReason = banReason;
    }

    public void setExpired() {
        this.expired = true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player newPlayer = event.getPlayer();
        if (expired) {
            // send time up message
            newPlayer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BOLD + "[ TIME UP! ]"));

            // set to spectator if expired
            if (newPlayer.getGameMode() != GameMode.SPECTATOR) {
                newPlayer.setGameMode(GameMode.SPECTATOR);
            }
        }
    }
}
