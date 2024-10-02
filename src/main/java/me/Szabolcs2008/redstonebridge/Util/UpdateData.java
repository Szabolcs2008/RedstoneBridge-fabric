package me.Szabolcs2008.redstonebridge.Util;

import net.minecraft.util.math.BlockPos;

public class UpdateData {
    String name;
    int powerLevel;
    BlockPos position;

    public UpdateData(String name, int powerLevel, BlockPos pos) {
        this.name = name;
        this.powerLevel = powerLevel;
        this.position = pos;
    }

    public String getName() {
        return name;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public BlockPos getPosition() {
        return position;
    }

    public String toString() {
        return "name="+name+" powerLevel="+powerLevel+" position="+position;
    }
}
