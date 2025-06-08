package com.spearforge.sRebirth.papi;

import com.spearforge.sRebirth.SRebirth;
import com.spearforge.sRebirth.managers.PlayerManager;
import com.spearforge.sRebirth.managers.RequirementsManager;
import com.spearforge.sRebirth.models.PlayerModel;
import com.spearforge.sRebirth.models.RebirthRequirement;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SRebirthExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "srebirth";
    }

    @Override
    public @NotNull String getAuthor() {
        return "xenrive";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        PlayerModel playerModel = SRebirth.getPlayerData().get(player.getUniqueId());

        // level, points, next_level_points, next_level
        if (params.equalsIgnoreCase("level")){
            return String.valueOf(playerModel.getRebirthLevel());
        }
        if (params.equalsIgnoreCase("points")){
            return String.valueOf(playerModel.getRebirthPoints());
        }
        if (params.equalsIgnoreCase("next_level")){
            int nextLevel = playerModel.getRebirthLevel() + 1;
            return String.valueOf(nextLevel);
        }
        if (params.equalsIgnoreCase("next_level_points")){
            return String.valueOf(RequirementsManager.getRequirement(playerModel.getRebirthLevel() + 1).getPointsReq());
        }
        if (params.equalsIgnoreCase("level_spent")){
            return String.valueOf(playerModel.getLevelSpent());
        }
        if (params.equalsIgnoreCase("next_level_money")){
            return String.valueOf(RequirementsManager.getRequirement(playerModel.getRebirthLevel() +1).getMoneyReq());
        }

        return null;
    }
}
