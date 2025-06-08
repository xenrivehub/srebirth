package com.spearforge.sRebirth.commands;

import com.spearforge.sRebirth.SRebirth;
import com.spearforge.sRebirth.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AdminCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {


        if (commandSender.hasPermission("srebirth.admin")){
            if (strings.length == 0){
                List<String> helpMessages = SRebirth.getMessagesConfig().getStringList("admin.help");
                for (String line : helpMessages){
                    commandSender.sendMessage(Text.color(line));
                }
                return true;
            }
            switch (strings[0].toLowerCase()) {
                case "reload":
                    SRebirth.getInstance().reloadConfig();
                    SRebirth.getMessagesConfig().reloadConfig();
                    SRebirth.getGuiConfig().reloadConfig();
                    SRebirth.getShopConfig().reloadConfig();
                    SRebirth.getWorthConfig().reloadConfig();
                    commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.reload-success"))
                            .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    break;
                case "givelevel":
                    // arebirth givelevel player level
                    Player targetPlayer = Bukkit.getPlayer(strings[1]);
                    if (targetPlayer == null) {
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.player-not-found"))
                                .replace("%player%", strings[1])
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                        return true;
                    }
                    try {
                        int level = Integer.parseInt(strings[2]);
                        SRebirth.getPlayerData().get(targetPlayer.getUniqueId()).setRebirthLevel(SRebirth.getPlayerData().get(targetPlayer.getUniqueId()).getRebirthLevel() + level);
                        targetPlayer.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.givelevel-success-target"))
                                .replace("%level%", String.valueOf(level))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.givelevel-success"))
                                .replace("%target%", targetPlayer.getName())
                                .replace("%level%", String.valueOf(level))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    } catch (NumberFormatException ex){
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.invalid-level"))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.givelevel-usage"))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    }
                    break;
                case "givepoints":
                    // arebirth givepoints player points
                    Player targetPlayerPoints = Bukkit.getPlayer(strings[1]);
                    if (targetPlayerPoints == null) {
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.player-not-found"))
                                .replace("%player%", strings[1])
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                        return true;
                    }
                    try {
                        int points = Integer.parseInt(strings[2]);
                        SRebirth.getPlayerData().get(targetPlayerPoints.getUniqueId()).setRebirthPoints(SRebirth.getPlayerData().get(targetPlayerPoints.getUniqueId()).getRebirthPoints() + points);
                        targetPlayerPoints.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.givepoints-success-target"))
                                .replace("%points%", String.valueOf(points))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.givepoints-success"))
                                .replace("%target%", targetPlayerPoints.getName())
                                .replace("%points%", String.valueOf(points))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    } catch (NumberFormatException ex){
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.invalid-points"))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.givepoints-usage"))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    }
                    break;
                case "setpoints":
                    // arebirth setpoints player points
                    Player targetPlayerSetPoints = Bukkit.getPlayer(strings[1]);
                    if (targetPlayerSetPoints == null) {
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.player-not-found"))
                                .replace("%player%", strings[1])
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                        return true;
                    }
                    try {
                        int points = Integer.parseInt(strings[2]);
                        SRebirth.getPlayerData().get(targetPlayerSetPoints.getUniqueId()).setRebirthPoints(points);
                        targetPlayerSetPoints.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.setpoints-success-target"))
                                .replace("%points%", String.valueOf(points))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.setpoints-success"))
                                .replace("%target%", targetPlayerSetPoints.getName())
                                .replace("%points%", String.valueOf(points))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    } catch (NumberFormatException ex){
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.invalid-points"))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.setpoints-usage"))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    }
                    break;
                case "setlevel":
                    // arebirth setlevel player level
                    Player targetPlayerSetLevel = Bukkit.getPlayer(strings[1]);
                    if (targetPlayerSetLevel == null) {
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.player-not-found"))
                                .replace("%player%", strings[1])
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                        return true;
                    }
                    try {
                        int level = Integer.parseInt(strings[2]);
                        SRebirth.getPlayerData().get(targetPlayerSetLevel.getUniqueId()).setRebirthLevel(level);
                        targetPlayerSetLevel.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.setlevel-success-target"))
                                .replace("%level%", String.valueOf(level))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.setlevel-success"))
                                .replace("%target%", targetPlayerSetLevel.getName())
                                .replace("%level%", String.valueOf(level))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    } catch (NumberFormatException ex){
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.invalid-level"))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.setlevel-usage"))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    }
                    break;
                case "resetpoints":
                    // arebirth resetpoints player
                    Player targetPlayerResetPoints = Bukkit.getPlayer(strings[1]);
                    if (targetPlayerResetPoints == null) {
                        commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.player-not-found"))
                                .replace("%player%", strings[1])
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                        return true;
                    }
                    SRebirth.getPlayerData().get(targetPlayerResetPoints.getUniqueId()).setRebirthPoints(0);
                    targetPlayerResetPoints.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.resetpoints-success-target"))
                            .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.resetpoints-success"))
                            .replace("%target%", targetPlayerResetPoints.getName())
                            .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    break;
                default:
                    commandSender.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("admin.unknown-command"))
                            .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    break;
            }
        }


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender.hasPermission("srebirth.admin") && strings.length == 0){
            List.of("reload", "givelevel", "givepoints", "setpoints", "setlevel", "resetpoints");
        }
        return null;
    }
}
