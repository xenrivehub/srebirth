package com.spearforge.sRebirth.managers;

import com.spearforge.sRebirth.SRebirth;
import com.spearforge.sRebirth.models.PlayerModel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Locale;

public class PlayerManager {

    public void loadPlayerData(Player player){
        PlayerModel playerModel = new PlayerModel();
        ConfigurationSection dataSection = SRebirth.getDataConfig().getConfig().getConfigurationSection("data");
        if (dataSection == null) {
            SRebirth.getDataConfig().getConfig().createSection("data");
            return;
        }
        ConfigurationSection playerSection = dataSection.getConfigurationSection(player.getUniqueId().toString());
        if (playerSection != null) {
            playerModel.setUuid(player.getUniqueId().toString());
            playerModel.setPlayerName(playerSection.getString("name"));
            playerModel.setRebirthPoints(playerSection.getDouble("points"));
            playerModel.setRebirthLevel(playerSection.getInt("level"));
            playerModel.setLevelSpent(playerSection.getInt("levelSpent"));
            SRebirth.getPlayerData().put(player.getUniqueId(), playerModel);
            SRebirth.getInstance().getLogger().info("Loaded data for player: " + player.getName());
        } else {
            playerSection = dataSection.createSection(player.getUniqueId().toString());
            playerSection.set("name", player.getName());
            playerSection.set("points", 0.0);
            playerSection.set("level", 0);
            playerSection.set("levelSpent", 0);
            playerModel.setPlayerName(player.getName());
            playerModel.setRebirthPoints(0.0);
            playerModel.setRebirthLevel(0);
            SRebirth.getPlayerData().put(player.getUniqueId(), playerModel);
            SRebirth.getDataConfig().saveConfig();
        }



    }

    public void savePlayerData(Player player){
        ConfigurationSection dataSection = SRebirth.getDataConfig().getConfig().getConfigurationSection("data");
        if (dataSection == null) {
            SRebirth.getDataConfig().getConfig().createSection("data");
            return;
        }
        ConfigurationSection playerSection = dataSection.getConfigurationSection(player.getUniqueId().toString());
        if (playerSection != null){
            PlayerModel playerModel = SRebirth.getPlayerData().get(player.getUniqueId());
            if (playerModel != null){
                String points = String.format(Locale.US, "%.1f", playerModel.getRebirthPoints());
                playerSection.set("name", playerModel.getPlayerName());
                playerSection.set("points", Double.parseDouble(points));
                playerSection.set("level", playerModel.getRebirthLevel());
                playerSection.set("levelSpent", playerModel.getLevelSpent());
                SRebirth.getDataConfig().saveConfig();
                SRebirth.getInstance().getLogger().info("Saved data for player: " + player.getName());
            } else {
                SRebirth.getInstance().getLogger().warning("No data found for player: " + player.getName());
            }
        }

    }

}
