package com.spearforge.sRebirth.managers;

import com.spearforge.sRebirth.SRebirth;
import com.spearforge.sRebirth.models.ShopItem;
import com.spearforge.sRebirth.utils.Text;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ShopManager {

    public static void loadShop() {
        ConfigurationSection shopSection = SRebirth.getShopConfig().getConfig().getConfigurationSection("shop");
        if (shopSection == null) {
            SRebirth.getInstance().getLogger().warning("Shop configuration section is missing or empty.");
            return;
        }
        for (String key : shopSection.getKeys(false)){
            ConfigurationSection itemSection = shopSection.getConfigurationSection(key);
            if (itemSection == null) {
                SRebirth.getInstance().getLogger().warning("Item section for key '" + key + "' is missing or empty.");
                continue;
            }
            ShopItem item = new ShopItem(
                    key,
                    Material.valueOf(itemSection.getString("material")),
                    itemSection.getInt("slot"),
                    itemSection.getString("display-name"),
                    itemSection.getStringList("lore"),
                    itemSection.getInt("price"),
                    itemSection.getBoolean("glow", false),
                    itemSection.getStringList("commands")
            );
            SRebirth.getShopItems().put(key, item);
            SRebirth.getInstance().getLogger().info("Loaded shop item: " + item.getId() + " - " + Text.color(item.getDisplayName()));
        }

    }

}
