package com.spearforge.sRebirth.listeners;

import com.spearforge.sRebirth.SRebirth;
import com.spearforge.sRebirth.managers.PlayerManager;
import com.spearforge.sRebirth.models.PlayerModel;
import com.spearforge.sRebirth.models.ShopItem;
import com.spearforge.sRebirth.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ShopListener implements Listener {

    @EventHandler
    public void onShopClick(InventoryClickEvent e){
        String title = Text.color(SRebirth.getGuiConfig().getString("titles.shop-title", "Rebirth Shop"));
        String viewTitle = e.getView().getTitle();
        if (!viewTitle.equals(title)) return;
        e.setCancelled(true);
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().isAir()) return;
        ItemStack item = e.getCurrentItem();
        ItemMeta itemMeta = item.getItemMeta();

        if (item.getItemMeta() == null || !item.getItemMeta().hasDisplayName()) return;
        NamespacedKey keyName = new NamespacedKey(SRebirth.getInstance(), "item-id");
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        String itemId = container.get(keyName, PersistentDataType.STRING);
        ShopItem shopItem = SRebirth.getShopItems().get(itemId);
        Player player = (Player) e.getWhoClicked();
        if (shopItem == null) {
            player.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("messages.shop-item-not-found"))
                    .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
            return;
        }
        PlayerManager playerManager = new PlayerManager();
        PlayerModel playerModel = SRebirth.getPlayerData().get(player.getUniqueId());
        int currentRebirthLevel = playerModel.getRebirthLevel() - playerModel.getLevelSpent();
        if (currentRebirthLevel >= shopItem.getPrice()){
            playerModel.setLevelSpent(playerModel.getLevelSpent() + shopItem.getPrice());
            for (String cmd : shopItem.getCommands()){
                String command = cmd.replace("%player%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
            player.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("messages.buy-success"))
                    .replace("%price%", String.valueOf(shopItem.getPrice()))
                    .replace("%item%", Text.color(shopItem.getDisplayName()))
                    .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            player.closeInventory();
            playerManager.savePlayerData(player);
        } else {
            player.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("messages.not-enough-rebirths"))
                    .replace("%price%", String.valueOf(shopItem.getPrice()))
                    .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
        }
    }

}
