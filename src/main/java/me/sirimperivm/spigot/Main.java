package me.sirimperivm.spigot;

import me.sirimperivm.spigot.commands.AdminCommand;
import me.sirimperivm.spigot.commands.MainCommand;
import me.sirimperivm.spigot.events.Events;
import me.sirimperivm.spigot.extras.dependencies.LBApi;
import me.sirimperivm.spigot.utils.ConfigManager;
import me.sirimperivm.spigot.utils.ModuleManager;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.other.Errors;
import me.sirimperivm.spigot.utils.other.Logger;
import me.sirimperivm.spigot.utils.other.Strings;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getPluginManager;

@SuppressWarnings("all")
public final class Main extends JavaPlugin {
    
    private Main plugin;
    private Strings strings;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;
    private ModuleManager moduleManager;
    private LBApi litebansApi;

    private String defaultChannelName;
    private boolean foundLiteBans;

    private void setupDependencies() {
        if (getPluginManager().getPlugin("LiteBans") != null) {
            foundLiteBans = true;
            litebansApi = new LBApi(plugin);
        }
    }

    @Override
    public void onEnable() {
        plugin = this;
        strings = new Strings(plugin);
        colors = new Colors(plugin);
        log = new Logger(plugin, "ChatChannels");
        configManager = new ConfigManager(plugin);
        defaultChannelName = configManager.getSettings().getString("settings.default-channel-name");
        errors = new Errors(plugin);
        moduleManager = new ModuleManager(plugin);
        setupDependencies();

        getCommand("chatchannels").setExecutor(new AdminCommand(plugin));
        getCommand("chatchannels").setTabCompleter(new AdminCommand(plugin));

        getCommand("channel").setExecutor(new MainCommand(plugin));
        getCommand("channel").setTabCompleter(new MainCommand(plugin));
        getPluginManager().registerEvents(new Events(plugin), plugin);

        log.success("Plugin attivato correttamente!");
    }

    @Override
    public void onDisable() {
        log.success("Plugin disattivato correttamente!");
    }

    public Main getPlugin() {
        return plugin;
    }

    public Strings getStrings() {
        return strings;
    }

    public Colors getColors() {
        return colors;
    }

    public Logger getLog() {
        return log;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public String getDefaultChannelName() {
        return defaultChannelName;
    }

    public void setDefaultChannelName() {
        defaultChannelName = configManager.getSettings().getString("settings.default-channel-name");
    }

    public Errors getErrors() {
        return errors;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public boolean isFoundLiteBans() {
        return foundLiteBans;
    }

    public LBApi getLitebansApi() {
        return litebansApi;
    }
}
