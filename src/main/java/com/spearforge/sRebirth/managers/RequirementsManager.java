package com.spearforge.sRebirth.managers;

import com.spearforge.sRebirth.SRebirth;
import com.spearforge.sRebirth.models.RebirthRequirement;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class RequirementsManager {

    private static final Map<Integer, RebirthRequirement> requirementsMap = new HashMap<>();

    public static void loadRequirements() {
        ConfigurationSection requirementsSection = SRebirth.getInstance().getConfig().getConfigurationSection("rebirth-levels");
        if (requirementsSection == null) {
            SRebirth.getInstance().getLogger().warning("rebirth-levels section not found in config.yml");
            return;
        }

        for (String key : requirementsSection.getKeys(false)){
            ConfigurationSection levelSection = requirementsSection.getConfigurationSection(key + ".requirements");
            if (levelSection == null) {
                SRebirth.getInstance().getLogger().warning("No configuration section found for rebirth level:" + key);
                continue;
            }
            double moneyReq = levelSection.getDouble("money", 0.0);
            double pointsReq = levelSection.getDouble("points", 0.0);
            RebirthRequirement requirement = new RebirthRequirement(moneyReq, pointsReq);
            requirementsMap.put(Integer.parseInt(key), requirement);
        }
    }

    public static RebirthRequirement getRequirement(int level) {
        return requirementsMap.get(level);
    }

}
