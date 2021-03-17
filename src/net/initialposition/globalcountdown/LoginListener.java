package net.initialposition.globalcountdown;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
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
            // BAN IF EXPIRED
            BanList banList = Bukkit.getBanList(BanList.Type.NAME);
            if (!banList.isBanned(newPlayer.getDisplayName())) {
                newPlayer.kickPlayer("--=[ " + banReason + " ]=--");
                banList.addBan(newPlayer.getDisplayName(), banReason, null, null);
            }
        }
    }
}
