package com.spearforge.sRebirth.managers;

import com.spearforge.sRebirth.SRebirth;
import com.spearforge.sRebirth.models.PlayerModel;
import com.spearforge.sRebirth.models.RebirthRequirement;
import com.spearforge.sRebirth.models.ShopItem;
import com.spearforge.sRebirth.utils.Text;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GUIManager {

    public void openMainMenu(Player player) {
        FileConfiguration config = SRebirth.getGuiConfig().getConfig();
        String menuName = config.getString("titles.main-menu-title", "Main Menu");
        ConfigurationSection itemsSection = config.getConfigurationSection("items");

        if (itemsSection == null) {
            player.sendMessage(Text.color("&cError: Items section not found in GUI config."));
            return;
        }

        Inventory inventory = Bukkit.createInventory(player, 45, Text.color(menuName));
        for (String key : itemsSection.getKeys(false)){
            ItemStack item = menuItem(key, player);
            if (item != null) {
                int slot = itemsSection.getInt(key + ".slot", 1);
                if (slot < 0 || slot >= inventory.getSize()) {
                    player.sendMessage(Text.color("&cError: Invalid slot for item " + key));
                    continue;
                }
                ItemMeta itemMeta = item.getItemMeta();
                if (itemMeta == null) {
                    player.sendMessage(Text.color("&cError: Item meta is null for item " + key));
                    continue;
                }
                NamespacedKey keyName = new NamespacedKey(SRebirth.getInstance(), "item-id");
                PersistentDataContainer container = itemMeta.getPersistentDataContainer();
                container.set(keyName, PersistentDataType.STRING, key.toLowerCase(Locale.ENGLISH));
                item.setItemMeta(itemMeta);
                inventory.setItem(slot, item);
            }
        }

        fillItem(inventory);
        player.openInventory(inventory);

    }

    public void openRebirthPanel(Player player) {
        FileConfiguration config = SRebirth.getGuiConfig().getConfig();
        String menuName = config.getString("titles.panel-title", "Rebirth Panel");
        Inventory inventory = Bukkit.createInventory(player, 54, Text.color(menuName));
        player.sendMessage(SRebirth.getMessagesConfig().getString("messages.rebirth-panel-opened"));
        ItemStack closeButton = new ItemStack(Material.valueOf(SRebirth.getGuiConfig().getString("panel-close-button.material", "BARRIER").toUpperCase(Locale.ENGLISH)));
        ItemMeta closeMeta = closeButton.getItemMeta();
        if (closeMeta != null){
            closeMeta.setDisplayName(SRebirth.getGuiConfig().getString("panel-close-button.display-name", "Close"));
            List<String> lore = new ArrayList<>();
            for (String line : SRebirth.getGuiConfig().getStringList("panel-close-button.lore")) {
                lore.add(parsePlaceholders(player, Text.color(line)));
            }
            if (SRebirth.getGuiConfig().getBoolean("panel-close-button.glow", false)) {
                closeMeta.addEnchant(Enchantment.SILK_TOUCH, 1, true);
                closeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            closeMeta.setLore(lore);

        }
        inventory.setItem(53, closeButton);
        player.openInventory(inventory);
    }

    public void openRebirthShop(Player player) {
        FileConfiguration config = SRebirth.getGuiConfig().getConfig();
        Inventory shopInventory = Bukkit.createInventory(player, 54, Text.color(config.getString("titles.shop-title", "Rebirth Shop")));
        for (ShopItem item : SRebirth.getShopItems().values()){
            ItemStack itemStack = new ItemStack(item.getMaterial());
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null) continue;

            itemMeta.setDisplayName(Text.color(item.getDisplayName()));
            List<String> lore = new ArrayList<>();
            for (String line : item.getLore()) {
                lore.add(Text.color(line));
            }
            lore.add("");
            lore.add(Text.color(config.getString("last-line")).replace("%price%", String.valueOf(item.getPrice())));
            if (item.isGlow()) {
                itemMeta.addEnchant(Enchantment.SILK_TOUCH, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            itemMeta.setLore(lore);
            NamespacedKey keyName = new NamespacedKey(SRebirth.getInstance(), "item-id");
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            container.set(keyName, PersistentDataType.STRING, item.getId().toLowerCase(Locale.ENGLISH));
            itemStack.setItemMeta(itemMeta);

            shopInventory.setItem(item.getSlot(), itemStack);
        }
        fillItem(shopInventory);
        player.openInventory(shopInventory);
    }

    private ItemStack menuItem(String itemName, Player player){
        FileConfiguration config = SRebirth.getGuiConfig().getConfig();
        String path = "items." + itemName;

        if (itemName != null){
            ItemStack item = new ItemStack(Material.valueOf(config.getString(path + ".material", "STONE").toUpperCase(Locale.ENGLISH)));
            ItemMeta itemMeta = item.getItemMeta();

            if (itemMeta == null) return null;

            String displayName = config.getString(path +".display-name", "Default Item");
            List<String> lore = new ArrayList<>();
            for (String line : config.getStringList(path + ".lore")) {
                lore.add(parsePlaceholders(player, Text.color(line)));
            }
            if (config.getBoolean(path + ".glow", false)){
                itemMeta.addEnchant(Enchantment.SILK_TOUCH, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            itemMeta.setDisplayName(Text.color(displayName));
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);
            return item;
        }
        return null;
    }

    public void openWorthGUI(Player player, int page) {
        FileConfiguration config = SRebirth.getGuiConfig().getConfig();
        FileConfiguration worthConfig = SRebirth.getWorthConfig().getConfig(); // worth.yml yüklü config
        ConfigurationSection worthSection = worthConfig.getConfigurationSection("worth");

        if (worthSection == null) {
            player.sendMessage(ChatColor.RED + "Hiçbir değer yapılandırılmamış.");
            return;
        }

        List<String> keys = new ArrayList<>(worthSection.getKeys(false));
        int itemsPerPage = 45;
        int totalPages = (int) Math.ceil(keys.size() / (double) itemsPerPage);

        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;

        Inventory worthGUI = Bukkit.createInventory(player, 54,
                ChatColor.translateAlternateColorCodes('&',
                        config.getString("titles.material-worth-title", "Material Worth") + " - §7[Sayfa " + page + "/" + totalPages + "]"));

        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, keys.size());

        for (int i = startIndex; i < endIndex; i++) {
            String key = keys.get(i);
            Material material = Material.matchMaterial(key.toUpperCase());
            if (material == null) continue;

            double worth = worthSection.getDouble(key);
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.YELLOW + key.replace("_", " "));
                meta.setLore(Collections.singletonList(ChatColor.GRAY + "Puan: " + worth));
                item.setItemMeta(meta);
            }

            worthGUI.setItem(i - startIndex, item);
        }

        // Sayfa geçiş butonları (isteğe bağlı)
        if (page > 1) {
            ItemStack prev = new ItemStack(Material.ARROW);
            ItemMeta meta = prev.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "Önceki Sayfa");
            prev.setItemMeta(meta);
            worthGUI.setItem(45, prev);
        }

        if (page < totalPages) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta meta = next.getItemMeta();
            meta.setDisplayName(ChatColor.GREEN + "Sonraki Sayfa");
            next.setItemMeta(meta);
            worthGUI.setItem(53, next);
        }

        player.openInventory(worthGUI);
    }

    private void fillItem(Inventory inventory){
        for (int i=0; i<inventory.getSize(); i++){
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR){
                inventory.setItem(i, new ItemStack(Material.valueOf(SRebirth.getGuiConfig().getString("fill-item", "GRAY_STAINED_GLASS_PANE").toUpperCase(Locale.ENGLISH))));
            }
        }
    }

    public static String parsePlaceholders(Player player, String text) {
        PlayerModel model = SRebirth.getPlayerData().get(player.getUniqueId());

        String points = String.format(Locale.US, "%.1f", model.getRebirthPoints());
        RebirthRequirement nextReq = RequirementsManager.getRequirement(model.getRebirthLevel() + 1);

        String nextPoints = (nextReq != null)
                ? String.valueOf(nextReq.getPointsReq())
                : "En yüksek seviyedesin";

        String nextMoney = (nextReq != null)
                ? String.valueOf(nextReq.getMoneyReq())
                : "En yüksek seviyedesin";
        return text
                .replace("%srebirth_points%", points)
                .replace("%srebirth_level%", String.valueOf(model.getRebirthLevel()))
                .replace("%srebirth_next_level_points%", nextPoints)
                .replace("%srebirth_next_level_money%", nextMoney)
                .replace("%srebirth_level_spent%", String.valueOf(model.getLevelSpent()))
                .replace("%srebirth_available_level%", String.valueOf(model.getRebirthLevel() - model.getLevelSpent()));
    }
}
