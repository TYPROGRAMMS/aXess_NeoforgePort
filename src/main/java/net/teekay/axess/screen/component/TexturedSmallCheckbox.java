package net.teekay.axess.screen.component;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.teekay.axess.Axess;

import java.util.function.Consumer;

public class TexturedSmallCheckbox extends AbstractButton {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Axess.MODID, "textures/gui/checkbox_small.png");

    private boolean selected;

    @Override
    public void onPress() {
        this.selected = !this.selected;
        callback.accept(this.selected);
    }
    public boolean selected() {
        return this.selected;
    }

    private boolean mouseInBoundingBox = false;
    private int boundMinX = -1;
    private int boundMaxX = -1;
    private int boundMinY = -1;
    private int boundMaxY = -1;
    private boolean hasBounds = false;

    public void setBounds(int minX, int minY, int maxX, int maxY) {
        boundMinX = minX;
        boundMaxX = maxX;

        boundMinY = minY;
        boundMaxY = maxY;

        hasBounds = true;
    }

    private Consumer<Boolean> callback;

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
        // This custom checkbox has no additional narration beyond its label.
    }

    @Override
    public boolean isHovered() {
        return super.isHovered() && this.mouseInBoundingBox;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (!this.mouseInBoundingBox) return false;
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }


    @Override
    public boolean isFocused() {
        return false;
    }

    public TexturedSmallCheckbox(int pX, int pY, int pWidth, int pHeight, Component pMessage, boolean pSelected, Consumer<Boolean> callback) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.height = pHeight;
        this.selected = pSelected;
        this.callback = callback;
    }

    public TexturedSmallCheckbox(int pX, int pY, int pWidth, int pHeight, Component pMessage, boolean pSelected, boolean pShowLabel, Consumer<Boolean> callback) {
        super(pX, pY, pWidth, pHeight, pMessage);
        this.height = pHeight;
        this.selected = pSelected;
        this.callback = callback;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.mouseInBoundingBox = hasBounds ? (pMouseX <= boundMaxX && pMouseX >= boundMinX && pMouseY <= boundMaxY && pMouseY >= boundMinY) : true;
        this.setTooltipDelay(java.time.Duration.ofMillis(mouseInBoundingBox ? 0 : 10000000));
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();

        float xOffset = 0;
        float yOffset = 0;

        if (pMouseX >= this.getX() && pMouseY >= this.getY() && pMouseX < this.getX() + this.width && pMouseY < this.getY() + this.height) xOffset += 16.0F;
        if (this.selected) yOffset += 16.0F;
        if (!this.active) yOffset += 32.0F;

        pGuiGraphics.blit(TEXTURE, this.getX(), this.getY(), xOffset, yOffset, 16, 16, 64, 64);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
