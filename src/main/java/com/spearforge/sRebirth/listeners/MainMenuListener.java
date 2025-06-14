package com.spearforge.sRebirth.listeners;

import com.spearforge.sRebirth.SRebirth;
import com.spearforge.sRebirth.managers.GUIManager;
import com.spearforge.sRebirth.managers.RequirementsManager;
import com.spearforge.sRebirth.models.PlayerModel;
import com.spearforge.sRebirth.models.RebirthRequirement;
import com.spearforge.sRebirth.utils.Text;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class MainMenuListener implements Listener {


    @EventHandler
    public void onMainMenuClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        FileConfiguration guiConfig = SRebirth.getGuiConfig().getConfig();
        String title = Text.color(guiConfig.getString("titles.main-menu-title", "Main Menu"));
        if (e.getView().getTitle().equals(title)){
            e.setCancelled(true);
            ItemStack item = e.getCurrentItem();
            if (item == null || item.getType().isAir()) return;
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta == null) return;

            NamespacedKey keyName = new NamespacedKey(SRebirth.getInstance(), "item-id");
            String itemId = itemMeta.getPersistentDataContainer().get(keyName, PersistentDataType.STRING);

            if (itemId == null) return;

            GUIManager guiManager = new GUIManager();
            itemId = itemId.toLowerCase();
            if (itemId.equalsIgnoreCase("rebirth-info")) {
                player.closeInventory();
                guiManager.openWorthGUI(player, 1);
            }
            if (itemId.equalsIgnoreCase("rebirth-panel")) {
                player.closeInventory();
                guiManager.openRebirthPanel(player);
            } else if (itemId.equalsIgnoreCase("rebirth-shop")){
                player.closeInventory();
                guiManager.openRebirthShop(player);
            } else if (itemId.equalsIgnoreCase("close-button")){
                player.closeInventory();
            } else if (itemId.equalsIgnoreCase("rebirth-upgrade")){
                player.closeInventory();
                PlayerModel playerModel = SRebirth.getPlayerData().get(player.getUniqueId());
                RebirthRequirement requirement = RequirementsManager.getRequirement(playerModel.getRebirthLevel() + 1);
                double currentBalance = SRebirth.getEcon().getBalance(player);
                if (currentBalance >= requirement.getMoneyReq() && playerModel.getRebirthPoints() >= requirement.getPointsReq()){
                    playerModel.setRebirthLevel(playerModel.getRebirthLevel() + 1);
                    playerModel.setRebirthPoints(playerModel.getRebirthPoints() - requirement.getPointsReq());
                    SRebirth.getEcon().withdrawPlayer(player, requirement.getMoneyReq());
                    player.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("messages.rebirth-success", "&aRebirth işlemi başarılı! Yeni seviye: &6%level%").replace("%level%", String.valueOf(playerModel.getRebirthLevel())))
                            .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    spawnFirework(player);
                    String titleMsgTitle = ChatColor.translateAlternateColorCodes('&',
                            SRebirth.getMessagesConfig().getString("messages.rebirth-title", "&a&lREBIRTH"));

                    String subtitle = ChatColor.translateAlternateColorCodes('&',
                            SRebirth.getMessagesConfig().getString("messages.rebirth-subtitle", "&6%player% &fadlı oyuncumuz &6Rebirth Seviyesini &fyükseltti! Yeni seviye: &6%level%")
                                    .replace("%player%", player.getName())
                                    .replace("%level%", String.valueOf(playerModel.getRebirthLevel()))
                    );
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.sendTitle(titleMsgTitle, subtitle, 10, 60, 10); // fadeIn, stay, fadeOut tick cinsinden
                    }} else {
                    player.sendMessage(Text.color(SRebirth.getMessagesConfig().getString("messages.rebirth-failed"))
                            .replace("%prefix%", SRebirth.getMessagesConfig().getString("messages.prefix")));
                }
            }

        }
    }

    public void spawnFirework(Player player) {
        Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();

        meta.addEffect(FireworkEffect.builder()
                .withColor(Color.AQUA)
                .withFade(Color.PURPLE)
                .with(FireworkEffect.Type.BALL_LARGE)
                .trail(true)
                .flicker(true)
                .build());

        meta.setPower(1); // Yükselme yüksekliği (1 = düşük, 2 = orta, 3 = yüksek)
        firework.setFireworkMeta(meta);
    }

}
