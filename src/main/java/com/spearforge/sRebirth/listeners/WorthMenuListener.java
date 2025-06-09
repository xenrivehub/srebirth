package com.spearforge.sRebirth.listeners;

import com.spearforge.sRebirth.SRebirth;
import com.spearforge.sRebirth.managers.GUIManager;
import com.spearforge.sRebirth.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WorthMenuListener implements Listener {

    @EventHandler
    public void onWorthMenuClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();
        Inventory inv = e.getInventory();
        String title = Text.color(e.getView().getTitle());

        if (!title.startsWith(SRebirth.getGuiConfig().getString("titles.material-worth-title"))) return;

        e.setCancelled(true); // Her tıklamayı iptal et, sadece gösterim menüsü

        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        ItemMeta meta = clickedItem.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        String displayName = ChatColor.stripColor(meta.getDisplayName());

        int currentPage = 1;

        GUIManager guiManager = new GUIManager();
        // Sayfa numarasını başlıktan çek
        if (title.contains("[Sayfa")) {
            try {
                String[] parts = title.split("\\[Sayfa ");
                if (parts.length > 1) {
                    String pagePart = parts[1].replace("]", "").trim();
                    currentPage = Integer.parseInt(pagePart.split("/")[0]);
                }
            } catch (Exception ex) {
                // Hatalı parse olduysa 1. sayfada kal
            }
        }

        if (displayName.equalsIgnoreCase("Önceki Sayfa")) {
            player.closeInventory();
            guiManager.openWorthGUI(player, currentPage - 1);
        }

        if (displayName.equalsIgnoreCase("Sonraki Sayfa")) {
            player.closeInventory();
            guiManager.openWorthGUI(player, currentPage + 1);
        }
    }

}
