package com.github.galatynf.sihywtcamd.cardinal.implem;

import com.github.galatynf.sihywtcamd.cardinal.api.SlimeEntityComponentAPI;
import net.minecraft.nbt.NbtCompound;

public class SlimeEntityComponent implements SlimeEntityComponentAPI {
    private boolean hasMerged = false;

    public boolean hasMerged() {
        return this.hasMerged;
    }

    @Override
    public void setMerged(boolean hasMerged) {
        this.hasMerged = hasMerged;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.setMerged(tag.getBoolean("HasMerged"));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("HasMerged", this.hasMerged);
    }
}
