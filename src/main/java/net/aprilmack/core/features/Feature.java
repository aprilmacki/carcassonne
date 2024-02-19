package net.aprilmack.core.features;

import net.aprilmack.core.tiles.TileSectionType;

public interface Feature {
    boolean isComplete();
    TileSectionType getType();
    void score();
}
