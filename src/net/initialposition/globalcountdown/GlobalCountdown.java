package net.initialposition.globalcountdown;

import net.initialposition.globalcountdown.util.ConsoleLogger;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Calendar;
import java.util.Date;

import static net.initialposition.globalcountdown.util.TimeConverter.convertDateToTimestamp;
import static net.initialposition.globalcountdown.util.TimeConverter.getCurrentDatePlusThirtyDays;

public class GlobalCountdown extends JavaPlugin {

    private final static String CONFIG_REMAINING_TIME = "confEndingTime";

    long endingTime = 0;
    String banReason;

    int countdownTaskID;

    LoginListener loginListener;

    public boolean EXPIRED = false;

    @Override
    public void onEnable() {

        // unix timestamp for ending date
        long endingTimestamp;

        // check if we have a saved time left and load, otherwise initialize with 30 days
        if (this.getConfig().contains(CONFIG_REMAINING_TIME)) {
            // we have a time, simply load it (and the ban reason)
            ConsoleLogger.debug_log("Config found! Loading time...");

            endingTimestamp = this.getConfig().getLong(CONFIG_REMAINING_TIME);

        } else {
            // we have no time, initialize with 30 days
            ConsoleLogger.debug_log("No saved time found, initializing new config...");

            Date finalDate = getCurrentDatePlusThirtyDays();
            endingTimestamp = convertDateToTimestamp(finalDate);

            this.getConfig().set(CONFIG_REMAINING_TIME, endingTimestamp);

            this.saveConfig();
        }

        // calculate difference between ending timestamp and current timestamp as initial ending time
        long currentTimestamp = convertDateToTimestamp(Calendar.getInstance().getTime());
        endingTime = endingTimestamp - currentTimestamp;

        // enable listener to update list in real time
        loginListener = new LoginListener(banReason);
        getServer().getPluginManager().registerEvents(loginListener, this);

        // if time is <0 on startup, we disable everything
        if (endingTime > 0) {
            // start time
            ConsoleLogger.debug_log("--=[ STARTING THE COUNTDOWN ]=--");
            countdownTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::updateRemainingTime, 0L, 20);
        } else {
            // DISABLE EVERYTHING
            this.getConfig().set(CONFIG_REMAINING_TIME, 0);
            this.saveConfig();

            EXPIRED = true;
            loginListener.setExpired();

            // set everyone to spectator mode
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.setGameMode(GameMode.SPECTATOR);
            }
        }
    }

    @Override
    public void onDisable() {
        ConsoleLogger.debug_log("--=[ STOPPING THE COUNTDOWN ]=--");

        Bukkit.getScheduler().cancelTask(countdownTaskID);
        HandlerList.unregisterAll(this);
    }

    private void updateRemainingTime() {
        // transform current time to human readable format
        if (endingTime >= 0) {
            int days = (int) Math.floor(endingTime / (60.0 * 60 * 24));
            int hours = (int) Math.floor(endingTime % (60.0 * 60 * 24) / (60 * 60));
            int minutes = (int) Math.floor(endingTime % (60.0 * 60) / 60);
            int seconds = (int) Math.floor((endingTime % 60));

            String daysStr = days < 10 ? "0" + days : String.valueOf(days);
            String hoursStr = hours < 10 ? "0" + hours : String.valueOf(hours);
            String minutesStr = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
            String secondsStr = seconds < 10 ? "0" + seconds : String.valueOf(seconds);

            String completeString = ChatColor.BOLD + daysStr + ":" + hoursStr + ":" + minutesStr + ":" + secondsStr;

            // send string to all players (if any exist)
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(completeString));
            }

            // count down
            endingTime -= 1;
        } else {
            ConsoleLogger.debug_log("--=[ TIME UP! ]=--");
            // end timer
            Bukkit.getScheduler().cancelTask(countdownTaskID);

            // DISABLE EVERYTHING
            this.getConfig().set(CONFIG_REMAINING_TIME, 0);
            this.saveConfig();

            EXPIRED = true;
            loginListener.setExpired();

            // set everyone to spectator mode
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BOLD + "[ TIME UP! ]"));
                player.setGameMode(GameMode.SPECTATOR);
            }
        }
    }
}
