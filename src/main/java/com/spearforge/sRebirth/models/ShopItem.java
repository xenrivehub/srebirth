package com.spearforge.sRebirth.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopItem {

    private String id;
    private Material material;
    private int slot;
    private String displayName;
    private List<String> lore;
    private int price;
    private boolean glow;
    private List<String> commands;

}
