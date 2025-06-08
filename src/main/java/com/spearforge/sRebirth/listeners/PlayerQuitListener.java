package com.spearforge.sRebirth.listeners;

import com.spearforge.sRebirth.SRebirth;
import com.spearforge.sRebirth.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player player = e.getPlayer();
        // Save player data when they quit
        PlayerManager playerManager = new PlayerManager();
        playerManager.savePlayerData(player);
        SRebirth.getPlayerData().remove(player.getUniqueId());
    }

}
