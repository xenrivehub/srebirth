package com.spearforge.sRebirth.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    private String uuid;
    private String playerName;
    private double rebirthPoints;
    private int rebirthLevel;

}
