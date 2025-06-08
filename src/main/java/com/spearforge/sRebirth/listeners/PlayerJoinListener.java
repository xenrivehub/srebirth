package com.spearforge.sRebirth.listeners;

import com.spearforge.sRebirth.SRebirth;
import com.spearforge.sRebirth.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        // Load player data when they join
        PlayerManager playerManager = new PlayerManager();
        playerManager.loadPlayerData(player);
        System.out.println("Loaded player data: " + SRebirth.getPlayerData().get(player.getUniqueId()));
    }

}
