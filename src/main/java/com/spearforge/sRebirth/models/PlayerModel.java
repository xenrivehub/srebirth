package com.spearforge.sRebirth.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerModel {

    private String uuid;
    private String playerName;
    private double rebirthPoints;
    private int rebirthLevel;
    private int levelSpent;

}
