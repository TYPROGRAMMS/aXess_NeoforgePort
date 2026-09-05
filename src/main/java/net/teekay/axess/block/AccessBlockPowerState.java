package net.teekay.axess.block;

import net.minecraft.util.StringRepresentable;

public enum AccessBlockPowerState implements StringRepresentable {
    NORMAL("normal"),
    ALLOW("allow"),
    DENY("deny"),
    DISABLED("disabled");

    private final String id;

    @Override
    public String getSerializedName() {
        return id;
    }

    AccessBlockPowerState(String id) {
        this.id = id;
    }
}
