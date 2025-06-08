package com.spearforge.sRebirth.listeners;

import com.spearforge.sRebirth.SRebirth;
import com.spearforge.sRebirth.models.PlayerModel;
import com.spearforge.sRebirth.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class PanelListener implements Listener {

    @EventHandler
    public void onPanelClick(InventoryClickEvent e){
        String title = Text.color(SRebirth.getGuiConfig().getString("titles.panel-title", "Rebirth Panel"));
        String viewTitle = e.getView().getTitle();
        if (!viewTitle.equals(title)) return;

        if (e.getSlot() == 53) {
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
        }
    }

    @EventHandler
    public void onPanelClose(InventoryCloseEvent e){
        String title = Text.color(SRebirth.getGuiConfig().getString("titles.panel-title", "Rebirth Panel"));
        String viewTitle = e.getView().getTitle();
        if (!viewTitle.equals(title)) return;

        UUID playerId = e.getPlayer().getUniqueId();
        ItemStack[] contents = e.getInventory().getContents();

        double totalWorth = 0.0;
        Map<Material, Double> itemWorthMap = new HashMap<>();

        PlayerModel playerModel = SRebirth.getPlayerData().get(playerId);
        if (contents != null) {
            for (ItemStack item : contents) {
                if (item == null || item.getType().isAir() || item.getType() == Material.BARRIER) continue;

                Material material = item.getType();
                double worth = SRebirth.getWorthMap().getOrDefault(material, 0.0);
                if (worth > 0) {
                    double itemWorth = worth * item.getAmount();
                    totalWorth += itemWorth;

                    itemWorthMap.put(material, itemWorthMap.getOrDefault(material, 0.0) + itemWorth);
                } else {
                    e.getPlayer().getInventory().addItem(item); // geri ver
                }
            }

            if (playerModel != null) {
                playerModel.setRebirthPoints(playerModel.getRebirthPoints() + totalWorth);

                if (!itemWorthMap.isEmpty()) {
                    e.getPlayer().sendMessage(Text.color(SRebirth.getMessagesConfig().getString("messages.panel-closed"))
                            .replace("%totalWorth%", String.format(Locale.US, "%.1f", totalWorth))
                            .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));

                    for (Map.Entry<Material, Double> entry : itemWorthMap.entrySet()) {
                        e.getPlayer().sendMessage(Text.color(SRebirth.getMessagesConfig().getString("messages.per-item-worth"))
                                .replace("%item%", entry.getKey().name())
                                .replace("%worth%", String.format(Locale.US, "%.1f", entry.getValue()))
                                .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    }
                }
            }
        }
    }
}
