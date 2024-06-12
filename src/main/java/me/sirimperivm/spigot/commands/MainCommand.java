package me.sirimperivm.spigot.commands;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.utils.ConfigManager;
import me.sirimperivm.spigot.utils.ModuleManager;
import me.sirimperivm.spigot.utils.colors.Colors;
import me.sirimperivm.spigot.utils.other.Errors;
import me.sirimperivm.spigot.utils.other.Logger;
import me.sirimperivm.spigot.utils.other.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class MainCommand implements CommandExecutor, TabCompleter {

    private Main plugin;
    private Strings strings;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;
    private ModuleManager moduleManager;

    public MainCommand(Main plugin) {
        this.plugin = plugin;
        strings = plugin.getStrings();
        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
        errors = plugin.getErrors();
        moduleManager = plugin.getModuleManager();
    }

    private void getUsage(CommandSender s, int page) {
        moduleManager.createHelp(s, "main-command", page);
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.user.main"))) {
            return true;
        } else {
            if (a.length == 0) {
                getUsage(s, 1);
            } else if (a.length == 1) {
                if (a[0].equalsIgnoreCase("info")) {
                    if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.user.info"))) {
                        return true;
                    } else {
                        if (errors.noConsole(s)) {
                            return true;
                        } else {
                            Player p = (Player) s;
                            String playerName = p.getName();
                            String channelName = moduleManager.getChannelName(playerName);
                            p.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "chat-channel.info")
                                    .replace("{channel-name}", channelName)
                            );
                        }
                    }
                } else {
                    getUsage(s, 1);
                }
            } else if (a.length == 2) {
                if (a[0].equalsIgnoreCase("set")) {
                    if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.user.set"))) {
                        return true;
                    } else {
                        if (errors.noConsole(s)) {
                            return true;
                        } else {
                            Player p = (Player) s;
                            String playerName = p.getName();
                            String chosenChannel = a[1];
                            moduleManager.switchPlayerChannel(p, chosenChannel);
                        }
                    }
                } else if (a[0].equalsIgnoreCase("help")) {
                    if (moduleManager.containsOnlyNumbers(a[1])) {
                        int page = Integer.parseInt(a[1]);
                        getUsage(s, page);
                    } else {
                        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "invalid-args.only-numbers-here")
                                .replace("{arg}", a[1])
                        );
                    }
                } else {
                    getUsage(s, 1);
                }
            } else {
                getUsage(s, 1);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
        if (a.length == 1) {
            List<String> toReturn = new ArrayList<>();
            if (s.hasPermission(configManager.getSettings().getString("permissions.commands.user.main"))) {
                toReturn.add("help");
                if (s.hasPermission(configManager.getSettings().getString("permissions.commands.user.info"))) {
                    toReturn.add("info");
                }
                if (s.hasPermission(configManager.getSettings().getString("permissions.commands.user.set"))) {
                    toReturn.add("set");
                }
            }
            return toReturn;
        }
        if (a.length == 2) {
            List<String> toReturn = new ArrayList<>();
            if (s.hasPermission(configManager.getSettings().getString("permissions.commands.user.main"))) {
                if (a[0].equalsIgnoreCase("help")) {
                    toReturn.add("[page]");
                }

                if (a[0].equalsIgnoreCase("set")) {
                    if (s.hasPermission(configManager.getSettings().getString("permissions.commands.user.set"))) {
                        toReturn.add("<channel-name>");
                    }
                }
            }
            return toReturn;
        }
        return new ArrayList<>();
    }
}
