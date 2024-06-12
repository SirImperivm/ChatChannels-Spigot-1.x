package me.sirimperivm.spigot.events;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.ConfigManager;
import me.sirimperivm.spigot.utils.ModuleManager;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.other.Errors;
import me.sirimperivm.spigot.utils.other.Logger;
import me.sirimperivm.spigot.utils.other.Strings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

@SuppressWarnings("all")
public class Events implements Listener {

    private Main plugin;
    private Strings strings;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;
    private ModuleManager moduleManager;

    public Events(Main plugin) {
        this.plugin = plugin;
        strings = plugin.getStrings();
        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
        errors = plugin.getErrors();
        moduleManager = plugin.getModuleManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String playerName = p.getName();

        if (!moduleManager.getUsedChannels().containsKey(playerName)) {
            moduleManager.switchPlayerChannel(p, plugin.getDefaultChannelName());
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String message = e.getMessage();
        String playerName = p.getName();
        String playerUuid = p.getUniqueId().toString();

        e.setCancelled(true);
        boolean isMuted = false;
        if (plugin.isFoundLiteBans() && plugin.getLitebansApi().isMuted(playerUuid, playerName)) {
            isMuted = true;
        }
        if (isMuted) {
            return;
        }

        HashMap<String, String> chatChannels = moduleManager.getUsedChannels();
        String playerChannel = chatChannels.get(playerName);

        int i=0;
        for (String key : chatChannels.keySet()) {
            String value = chatChannels.get(key);
            if (value.equals(playerChannel)) i++;
        }

        if (i==1) {
            if (!moduleManager.getAloneCooldownList().contains(playerName)) {
                p.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "chat-channel.no-one-hear-you"));
                moduleManager.getAloneCooldownList().add(playerName);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        moduleManager.getAloneCooldownList().remove(playerName);
                    }
                }.runTaskLater(plugin, 5*60*20L);
            }
        }
        for (String pName : chatChannels.keySet()) {
            String channel = chatChannels.get(pName);
            Player player = Bukkit.getPlayerExact(pName);
            if (channel.equals(playerChannel)) {
                if (player != null) {
                    player.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "chat-channel.format")
                            .replace("{channel-name}", channel)
                            .replace("{player-name}", playerName)
                            .replace("{message}", message)
                    );
                }
            }
        }
    }
}
