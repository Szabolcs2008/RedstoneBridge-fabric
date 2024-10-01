package me.Szabolcs2008.redstonebridge.Util;

import net.minecraft.util.math.BlockPos;

public class UpdateData {
    String name;
    int powerLevel;
    boolean powered;
    BlockPos position;

    public UpdateData(String name, boolean powered, int powerLevel, BlockPos pos) {
        this.name = name;
        this.powered = powered;
        this.powerLevel = powerLevel;
        this.position = pos;
    }

    public String getName() {
        return name;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public boolean isPowered() {
        return powered;
    }

    public BlockPos getPosition() {
        return position;
    }

    public String toString() {
        return "name="+name+" powered="+powered+" powerLevel="+powerLevel+" position="+position;
    }
}
