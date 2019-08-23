package me.nickac.survivalplus;

import org.spongepowered.api.util.Direction;

public class MiscUtils {
    private static final Direction[] axis = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public static Direction yawToFace(float yaw) {
        return axis[Math.round(yaw / 90f) & 0x3];
    }
}
