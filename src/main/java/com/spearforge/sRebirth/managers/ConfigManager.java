package com.spearforge.sRebirth.managers;

import com.spearforge.sRebirth.utils.Text;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class ConfigManager {

    private final JavaPlugin plugin;
    private final String fileName;
    private File configFile;
    private FileConfiguration fileConfiguration;

    /**
     * Özel bir yapılandırma dosyasını yönetir.
     *
     * @param plugin   Plugin'inin ana sınıfının bir örneği.
     * @param fileName Yönetilecek yapılandırma dosyasının adı (örn: "messages.yml").
     * Bu dosyanın, plugin'inin JAR dosyasındaki kaynaklar (resources) klasöründe bulunması önerilir.
     */
    public ConfigManager(JavaPlugin plugin, String fileName) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin null olamaz");
        }
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Dosya adı null veya boş olamaz");
        }
        this.plugin = plugin;
        this.fileName = fileName;
        this.configFile = new File(plugin.getDataFolder(), fileName);

        // Plugin'in veri klasörünün var olduğundan emin ol
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        setupDefaults(); // Dosya yoksa JAR'dan kopyalar
        loadConfig();    // Yapılandırmayı belleğe yükler
    }

    /**
     * Eğer plugin'in veri klasöründe mevcut değilse,
     * varsayılan yapılandırma dosyasını JAR içinden kopyalar.
     */
    private void setupDefaults() {
        if (!configFile.exists()) {
            try {
                plugin.saveResource(fileName, false); // false = var olanın üzerine yazma
            } catch (IllegalArgumentException e) {
                // Bu durum, kaynak JAR içinde bulunamadığında oluşabilir.
                // Eğer kullanıcı dosyayı tamamen programatik olarak oluşturmayı planlıyorsa,
                // bu kritik bir hata olmayabilir.
                plugin.getLogger().warning("'" + fileName + "' için varsayılan kaynak kaydedilemedi. JAR içinde bulunmuyor olabilir. Kaydedilirse yeni boş bir dosya kullanılacak/oluşturulacak.");
            }
        }
    }

    /**
     * Yapılandırma dosyasını belleğe yükler.
     */
    private void loadConfig() {
        this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        // Opsiyonel: Özel varsayılanlar gerekiyorsa veya YamlConfiguration.loadConfiguration(file)
        // yeni kaydedilmiş bir kaynaktan encoding sorunları yaşıyorsa, JAR'ın kaynak akışından
        // UTF-8 ile yüklemeyi dene. Bu, özellikle özel karakterler içeren JAR'daki varsayılanların
        // doğru okunmasını sağlamak içindir.
        InputStream defaultConfigStream = plugin.getResource(fileName);
        if (defaultConfigStream != null) {
            try (InputStreamReader reader = new InputStreamReader(defaultConfigStream, StandardCharsets.UTF_8)) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);
                this.fileConfiguration.setDefaults(defaultConfig);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, fileName + " için JAR'dan varsayılan yapılandırma yüklenemedi", e);
            }
        }
    }

    /**
     * FileConfiguration nesnesini döndürür.
     *
     * @return FileConfiguration nesnesi.
     */
    public FileConfiguration getConfig() {
        if (this.fileConfiguration == null) {
            reloadConfig(); // Bir şekilde null ise yeniden yüklemeyi dene
        }
        return this.fileConfiguration;
    }

    /**
     * Yapılandırma dosyasını diskten yeniden yükler.
     */
    public void reloadConfig() {
        if (this.configFile == null) {
            this.configFile = new File(plugin.getDataFolder(), fileName);
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        InputStream defaultConfigStream = plugin.getResource(fileName);
        if (defaultConfigStream != null) {
            try (InputStreamReader reader = new InputStreamReader(defaultConfigStream, StandardCharsets.UTF_8)) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);
                this.fileConfiguration.setDefaults(defaultConfig);
                plugin.getLogger().info(fileName + " yapılandırmasındaki varsayılanlar başarıyla ayarlandı.");
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, fileName + " için JAR'dan varsayılan yapılandırma yüklenemedi (yeniden yükleme sırasında)", e);
            }
        }
        plugin.getLogger().info(fileName + " başarıyla yeniden yüklendi.");
    }

    /**
     * Mevcut yapılandırmayı diske kaydeder.
     */
    public void saveConfig() {
        if (this.fileConfiguration == null || this.configFile == null) {
            plugin.getLogger().warning("Yapılandırma veya yapılandırma dosyası null, " + fileName + " kaydedilemiyor.");
            return;
        }
        try {
            // Header'ı korumak için (eğer ayarlandıysa)
            if (getConfig().options().header() != null && !getConfig().options().header().isEmpty()) {
                getConfig().options().copyHeader(true);
            }
            getConfig().save(this.configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, configFile + " dosyasına yapılandırma kaydedilemedi", ex);
        }
    }

    /**
     * Varsayılan yapılandırma dosyasını JAR'dan plugin'in veri klasörüne kaydeder.
     * Bu, mevcut dosya farklıysa üzerine yazacaktır.
     * Bir yapılandırmayı en son varsayılana sıfırlamak istediğinizde kullanışlıdır.
     */
    public void saveDefaultConfigForce() {
        if (configFile == null) {
            this.configFile = new File(plugin.getDataFolder(), fileName);
        }
        plugin.saveResource(fileName, true); // true = var olanın üzerine yaz
        reloadConfig(); // Üzerine yazdıktan sonra yeniden yükle
    }

    // --- Kullanışlı Get Metotları ---

    public String getString(String path) {
        return Text.color(getConfig().getString(path));
    }

    public String getString(String path, String defaultValue) {
        return getConfig().getString(path, defaultValue);
    }

    public int getInt(String path) {
        return getConfig().getInt(path);
    }

    public int getInt(String path, int defaultValue) {
        return getConfig().getInt(path, defaultValue);
    }

    public boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    public boolean getBoolean(String path, boolean defaultValue) {
        return getConfig().getBoolean(path, defaultValue);
    }

    public double getDouble(String path) {
        return getConfig().getDouble(path);
    }

    public double getDouble(String path, double defaultValue) {
        return getConfig().getDouble(path, defaultValue);
    }

    public List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }

    public Set<String> getKeys(String path, boolean deep) {
        ConfigurationSection section = getConfig().getConfigurationSection(path);
        if (section != null) {
            return section.getKeys(deep);
        }
        return Collections.emptySet(); // Null yerine boş Set döndürmek daha güvenlidir
    }

    public ConfigurationSection getSection(String path) {
        return getConfig().getConfigurationSection(path);
    }

    public void set(String path, Object value) {
        getConfig().set(path, value);
    }

    /**
     * Yapılandırmada bir değer ayarlar ve ardından yapılandırmayı diske kaydeder.
     * @param path Ayarlanacak yol.
     * @param value Ayarlanacak değer.
     */
    public void setAndSave(String path, Object value) {
        getConfig().set(path, value);
        saveConfig();
    }

    /**
     * Yapılandırmanın verilen yolu içerip içermediğini kontrol eder.
     * @param path Kontrol edilecek yol.
     * @return Yol mevcutsa true, aksi takdirde false.
     */
    public boolean contains(String path) {
        return getConfig().contains(path);
    }
}