package com.spearforge.sRebirth;

import com.spearforge.sRebirth.commands.AdminCommand;
import com.spearforge.sRebirth.commands.MainCommand;
import com.spearforge.sRebirth.listeners.*;
import com.spearforge.sRebirth.managers.ConfigManager;
import com.spearforge.sRebirth.managers.PlayerManager;
import com.spearforge.sRebirth.managers.RequirementsManager;
import com.spearforge.sRebirth.managers.ShopManager;
import com.spearforge.sRebirth.models.PlayerModel;
import com.spearforge.sRebirth.models.ShopItem;
import com.spearforge.sRebirth.papi.SRebirthExpansion;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class SRebirth extends JavaPlugin {

    @Getter
    private static ConfigManager guiConfig;
    @Getter
    private static SRebirth instance;
    @Getter
    private static ConfigManager worthConfig;
    @Getter
    private static ConfigManager messagesConfig;
    @Getter
    private static ConfigManager dataConfig;
    @Getter
    private static ConfigManager shopConfig;
    @Getter
    private static Economy econ = null;
    @Getter
    private static HashMap<UUID, PlayerModel> playerData = new HashMap<>();
    @Getter
    private static HashMap<String, ShopItem> shopItems = new HashMap<>();
    @Getter
    private static final HashMap<Material, Double> worthMap = new HashMap<>();
    @Getter
    private static PlayerManager playerManager = new PlayerManager();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        guiConfig = new ConfigManager(this, "gui.yml");
        worthConfig = new ConfigManager(this, "worth.yml");
        messagesConfig = new ConfigManager(this, "messages.yml");
        dataConfig = new ConfigManager(this, "data.yml");
        shopConfig = new ConfigManager(this, "shop.yml");

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        setupListeners();
        setupCommands();

        // load shop items
        ShopManager.loadShop();
        loadWorthMap();
        RequirementsManager.loadRequirements();

        // Register PlaceholderAPI expansion if available
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            new SRebirthExpansion().register();
        }

    }

    @Override
    public void onDisable() {
        for (PlayerModel model : playerData.values()){
            playerManager.savePlayerData(model);
        }
    }

    private void setupCommands() {
        getCommand("rebirth").setExecutor(new MainCommand());
        getCommand("arebirth").setExecutor(new AdminCommand());
    }

    private void setupListeners() {
        getServer().getPluginManager().registerEvents(new MainMenuListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new ShopListener(), this);
        getServer().getPluginManager().registerEvents(new PanelListener(), this);
        getServer().getPluginManager().registerEvents(new WorthMenuListener(), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void loadWorthMap() {
        worthMap.clear();
        ConfigurationSection worthSection = worthConfig.getConfig().getConfigurationSection("worth");
        if (worthSection == null) {
            getLogger().warning("Worth section not found in worth.yml. Please check the configuration.");
            return;
        }
        for (String key : worthSection.getKeys(false)) {
            Material material = Material.matchMaterial(key);
            if (material != null) {
                double worth = worthSection.getDouble(key);
                worthMap.put(material, worth);
            }
        }
    }
}
