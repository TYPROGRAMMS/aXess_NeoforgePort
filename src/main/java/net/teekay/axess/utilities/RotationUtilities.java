package net.teekay.axess.utilities;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class RotationUtilities {

    public static Vector3f rotationFromDirAndFace(Direction facing, AttachFace face) {

        // do not even begin asking what is going on here

        float yRot = switch (facing) {
            case NORTH -> 0f;
            case SOUTH -> 180f;
            case WEST -> 90f;
            case EAST -> 270f;
            default -> 0;
        };
        float xRot = 0;
        float zRot = 0;
        if (face == AttachFace.CEILING) {
            yRot = (yRot + 180f) % 360f;
        }
        if ((facing == Direction.SOUTH && face == AttachFace.CEILING) || (facing == Direction.NORTH && face == AttachFace.FLOOR)) {
            xRot += 90;
        }
        if ((facing == Direction.NORTH && face == AttachFace.CEILING) || (facing == Direction.SOUTH && face == AttachFace.FLOOR)) {
            xRot -= 90;
        }
        if ((facing == Direction.WEST && face == AttachFace.CEILING) || (facing == Direction.EAST && face == AttachFace.FLOOR)) {
            zRot += 90;
        }
        if ((facing == Direction.EAST && face == AttachFace.CEILING) || (facing == Direction.WEST && face == AttachFace.FLOOR)) {
            zRot -= 90;
        }

        if (face == AttachFace.CEILING) {
            xRot *= -1;
            yRot *= -1;
        }

        if (face == AttachFace.CEILING && (facing == Direction.EAST || facing == Direction.WEST)) {
            xRot *= -1;
            yRot *= -1;
            zRot *= -1;
        }

        return new Vector3f(xRot, yRot, zRot);
    }

}
