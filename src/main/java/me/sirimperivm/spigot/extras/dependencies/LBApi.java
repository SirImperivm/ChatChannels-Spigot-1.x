package me.sirimperivm.spigot.extras.dependencies;

import litebans.api.Database;
import me.sirimperivm.spigot.Main;

import java.util.UUID;

@SuppressWarnings("all")
public class LBApi {

    private Main plugin;

    public LBApi(Main plugin) {
        this.plugin = plugin;
    }

    public boolean isMuted(String playerUuid, String playerName) {
        return Database.get().isPlayerMuted(UUID.fromString(playerUuid), playerName);
    }
}
