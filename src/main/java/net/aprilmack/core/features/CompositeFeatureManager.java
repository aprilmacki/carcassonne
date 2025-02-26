package net.aprilmack.core.features;

import com.google.common.collect.Lists;
import net.aprilmack.core.Board;
import net.aprilmack.core.PlayerManager;
import net.aprilmack.core.tiles.Tile;
import net.aprilmack.core.tiles.TileManager;
import net.aprilmack.core.tiles.TileSection;
import net.aprilmack.core.tiles.TileSectionType;

import java.util.*;

public class CompositeFeatureManager implements FeatureManager {
    private final List<FeatureManager> featureManagers;

    public CompositeFeatureManager(PlayerManager playerManager, TileManager tileManager, Board board) {
        this.featureManagers = Lists.newArrayList(
                new GraphFeatureManager(playerManager, tileManager, board),
                new MonasteryFeatureManager(playerManager, board));
    }

    @Override
    public void updateFeatures(Tile tile, int xPosition, int yPosition) {
        for (FeatureManager featureManager : featureManagers) {
            featureManager.updateFeatures(tile, xPosition, yPosition);
        }
    }

    @Override
    public Collection<? extends Feature> getFeatures() {
        List<Feature> features = new ArrayList<>();
        for (FeatureManager featureManager : featureManagers) {
            features.addAll(featureManager.getFeatures());
        }
        return features;
    }

    @Override
    public boolean canPlaceMeeple(Tile tile, TileSection section) {
        if (section.getType() != TileSectionType.CITY
                && section.getType() != TileSectionType.ROAD
                && section.getType() != TileSectionType.MONASTERY) {
            return false;
        }

        if (section.getMeeple().isPresent()) {
            return false;
        }

        // We can only place meeple on a just-placed tile. This means effectively, there's just one meeple per tile
        for (TileSection anotherSection : tile.getSections().values()) {
            if (anotherSection.getMeeple().isPresent()) {
                return false;
            }
        }

        for (FeatureManager featureManager : featureManagers) {
            if (!featureManager.canPlaceMeeple(tile, section)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void scoreFeatures() {
        this.featureManagers.forEach(FeatureManager::scoreFeatures);
    }
}
