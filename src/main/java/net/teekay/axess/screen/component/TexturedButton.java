package net.teekay.axess.screen.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.teekay.axess.utilities.AxessColors;
import net.teekay.axess.utilities.MathUtilities;

public class TexturedButton extends Button {
    public TexturedButton(int x, int y, int width, int height, Component title, OnPress onPress) {
        super(x, y, width, height, title, onPress, btn -> {return Component.empty();});
    }

    public float timePassed = 0f;
    public int textPaddingLeft = 0;

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

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public boolean isHoveredOrFocused() {
        return super.isHoveredOrFocused() && this.mouseInBoundingBox;
    }

    public enum ButtonColor {
        NORMAL(0, AxessColors.MAIN.getRGB()),
        RED(40, AxessColors.RED.getRGB()),
        GREEN(80, AxessColors.GREEN.getRGB()),
        GREEN_LESS(120, AxessColors.GREEN_LESS.getRGB());

        int offset;
        int color;

        public int getOffset() {
            return offset;
        }

        public int getColor() {
            return color;
        }

        ButtonColor(int offset, int color) {
            this.offset = offset;
            this.color = color;
        }
    }

    public ButtonColor getColor() {
        return ButtonColor.NORMAL;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (!this.mouseInBoundingBox) return false;
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    private static void drawButton(GuiGraphics g, int x, int y, int w, int h, int color) {
        if (w <= 0 || h <= 0) return;

        // buttons.png contains a very simple 200x20 button strip: a solid
        // black interior surrounded by a 1px border.  The different strips
        // are separated by differently coloured 1px rows.  Scaling/blitting
        // those strips in 1.21.1 can interpolate across the strip boundary,
        // which makes the neighbouring colours appear as horizontal lines.
        // Reproduce the exact pixels here instead of sampling the texture.
        g.fill(x, y, x + w, y + h, 0xFF000000);
        g.fill(x, y, x + w, y + 1, color);
        g.fill(x, y + h - 1, x + w, y + h, color);
        g.fill(x, y + 1, x + 1, y + h - 1, color);
        g.fill(x + w - 1, y + 1, x + w, y + h - 1, color);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.mouseInBoundingBox = hasBounds ? (mouseX <= boundMaxX && mouseX >= boundMinX && mouseY <= boundMaxY && mouseY >= boundMinY) : true;
        this.setTooltipDelay(java.time.Duration.ofMillis(mouseInBoundingBox ? 0 : 10000000));

        this.isHovered = (mouseX >= getX() && mouseX < getX() + width && mouseY >= getY() && mouseY < getY() + height);

        int borderColor = isHoveredOrFocused() ? 0xFFFFFFFF : (0xFF000000 | getColor().getColor());
        drawButton(graphics, this.getX(), this.getY(), this.width, this.height, borderColor);

        Font font = Minecraft.getInstance().font;
        int textWidth;
        if (this.getMessage() == null || this.getMessage().getString().isEmpty()) {
            textWidth = 0;
        } else {
            textWidth = font.width(this.getMessage());
        }

        int textX = this.getX() + textPaddingLeft +  ((this.width - textPaddingLeft) - textWidth) / 2 + 1;
        int textY = this.getY() + (this.height - 8) / 2;

        graphics.enableScissor(getX() + textPaddingLeft + 2, getY(), getX() + width - 2, getY() + height);

        int offset = 0;
        if (textWidth > this.width - 4) {
            float x = ((textWidth - this.width + 4f) / 2f);
            offset = Math.round(MathUtilities.clampFloat((float) (Math.sin(timePassed / 20f) * x * 2f), -x, x));
        }

        graphics.drawString(font, this.getMessage(), textX + offset, textY, isHoveredOrFocused() ? 0xFFFFFF : getColor().getColor(), false);

        graphics.disableScissor();

        timePassed += partialTick;
    }
}
