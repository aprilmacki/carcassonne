package net.aprilmack.core.api;

import net.aprilmack.core.Player;
import net.aprilmack.core.TurnState;

public interface CarcassonneHandler {
    void turnStarted(Player player, TurnState turnState);

    void scoreUpdate(Player player);

    void gameEnded();

}
