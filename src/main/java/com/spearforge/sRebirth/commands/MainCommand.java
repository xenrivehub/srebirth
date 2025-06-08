package com.spearforge.sRebirth.commands;

import com.spearforge.sRebirth.SRebirth;
import com.spearforge.sRebirth.managers.GUIManager;
import com.spearforge.sRebirth.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    private GUIManager guiManager;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        FileConfiguration messagesConfig = SRebirth.getMessagesConfig().getConfig();
        guiManager = new GUIManager();

        if (commandSender instanceof Player player){
            if (player.hasPermission("srebirth.use")){
                guiManager.openMainMenu(player);
            } else {
                commandSender.sendMessage(Text.color(messagesConfig.getString("messages.no-permission", "&cYou do not have permission to use this command.")));
                return true;
            }
        } else {
            commandSender.sendMessage(Text.color(messagesConfig.getString("messages.not-player", "&cThis command can only be run by a player.")));
            return true;
        }





        return true;
    }
}
