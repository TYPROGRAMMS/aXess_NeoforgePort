package net.teekay.axess.screen.component;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.teekay.axess.Axess;
import net.teekay.axess.access.AccessNetwork;
import net.teekay.axess.client.AxessClientMenus;

import java.util.function.Consumer;

public class NetworkEntry {
    public static ResourceLocation TRASH_BUTTON = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/delete_button.png");

    private static final Component EDIT_TEXT = Component.translatable("gui."+Axess.MODID+".button.edit");
    private static final Component DELETE_TEXT = Component.translatable("gui."+Axess.MODID+".button.delete");

    public AccessNetwork network;
    public TexturedButton button;
    public HumbleImageButton trashButton;

    public boolean hasOptions;

    public NetworkEntry(NetworkList list, AccessNetwork network, boolean withOptions, Consumer<AccessNetwork> onSelect)
    {
        int pX = list.leftPos;
        int pY = list.topPos;
        int pWidth = list.width;
        int pHeight = list.elemHeight;

        hasOptions = withOptions;

        this.button = new TexturedButton(pX, pY, pWidth-21, pHeight, Component.literal(network.getName()), btn -> {
            onSelect.accept(network);
        });

        this.trashButton = new HumbleImageButton(
                pX + pWidth - 20,
                pY,
                20,
                20,
                0,
                0,
                20,
                TRASH_BUTTON,
                32, 64,
                btn -> {
                    AxessClientMenus.openNetworkDeletionScreen(network);
                });



        this.button.setBounds(pX, pY, pX+pWidth, pY+list.height);
        this.trashButton.setBounds(pX, pY, pX+pWidth, pY+list.height);

        if (!withOptions) {
            this.trashButton.active = false;
            this.trashButton.visible = false;
            this.button.setWidth(pWidth);
        } else {
            this.button.setTooltip(Tooltip.create(EDIT_TEXT));
            this.trashButton.setTooltip(Tooltip.create(DELETE_TEXT));
        }

        this.network = network;
    }

}