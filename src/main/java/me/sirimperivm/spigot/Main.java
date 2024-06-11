package me.sirimperivm.spigot;

import me.sirimperivm.spigot.utils.colors.Colors;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("all")
public final class Main extends JavaPlugin {
    
    private Main plugin;
    private Colors colors;

    @Override
    public void onEnable() {
        plugin = this;
        colors = new Colors(plugin);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Main getPlugin() {
        return plugin;
    }

    public Colors getColors() {
        return colors;
    }
}
