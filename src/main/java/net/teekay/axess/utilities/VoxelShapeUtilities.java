package net.teekay.axess.utilities;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeUtilities {
    public static VoxelShape rotateShape(VoxelShape shape, Direction from, Direction to) {
        if (from == to) return shape;

        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

        // Figure out rotation steps
        // Horizontal rotations (around Y axis)
        if (from.getAxis().isHorizontal() && to.getAxis().isHorizontal()) {
            int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
            for (int i = 0; i < times; i++) {
                buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                    buffer[1] = Shapes.or(buffer[1],
                            Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX));
                });
                buffer[0] = buffer[1];
                buffer[1] = Shapes.empty();
            }
            return buffer[0];
        }

        // Rotations involving UP or DOWN (around X or Z axis)
        if (to == Direction.UP || to == Direction.DOWN) {
            // Assume "from" is NORTH (front-facing) → rotate around X axis
            int times = (to == Direction.UP ? 1 : 3); // UP = 90° forward, DOWN = 270°
            for (int i = 0; i < times; i++) {
                buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                    buffer[1] = Shapes.or(buffer[1],
                            Shapes.box(minX, 1 - maxZ, minY, maxX, 1 - minZ, maxY));
                });
                buffer[0] = buffer[1];
                buffer[1] = Shapes.empty();
            }
            return buffer[0];
        }

        if (from == Direction.UP || from == Direction.DOWN) {
            // Rotating from vertical to horizontal
            int times = (from == Direction.UP ? 3 : 1); // Inverse of above
            for (int i = 0; i < times; i++) {
                buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                    buffer[1] = Shapes.or(buffer[1],
                            Shapes.box(minX, minZ, 1 - maxY, maxX, maxZ, 1 - minY));
                });
                buffer[0] = buffer[1];
                buffer[1] = Shapes.empty();
            }
            return buffer[0];
        }

        return shape; // fallback
    }
}
