package net.teekay.axess.screen.component;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import java.time.Duration;

public class HumbleImageButton extends ImageButton {
    private final ResourceLocation texture;
    private final int texX, texY, texYDiff, textureWidth, textureHeight;
    private boolean mouseInBoundingBox = true;
    private int boundMinX = -1, boundMaxX = -1, boundMinY = -1, boundMaxY = -1;
    private boolean hasBounds = false;

    public HumbleImageButton(int x,int y,int w,int h,int tx,int ty,ResourceLocation tex,OnPress press){ this(x,y,w,h,tx,ty,0,tex,256,256,press,Component.empty()); }
    public HumbleImageButton(int x,int y,int w,int h,int tx,int ty,int dy,ResourceLocation tex,OnPress press){ this(x,y,w,h,tx,ty,dy,tex,256,256,press,Component.empty()); }
    public HumbleImageButton(int x,int y,int w,int h,int tx,int ty,int dy,ResourceLocation tex,int tw,int th,OnPress press){ this(x,y,w,h,tx,ty,dy,tex,tw,th,press,Component.empty()); }
    public HumbleImageButton(int x,int y,int w,int h,int tx,int ty,int dy,ResourceLocation tex,int tw,int th,OnPress press,Component message){
        super(x,y,w,h,new net.minecraft.client.gui.components.WidgetSprites(tex,tex,tex,tex),press,message);
        this.texX=tx; this.texY=ty; this.texYDiff=dy; this.texture=tex; this.textureWidth=tw; this.textureHeight=th;
    }
    public void setBounds(int minX,int minY,int maxX,int maxY){boundMinX=minX;boundMaxX=maxX;boundMinY=minY;boundMaxY=maxY;hasBounds=true;}
    @Override public boolean isHovered(){ return super.isHovered() && mouseInBoundingBox; }
    @Override public boolean isFocused(){ return false; }
    @Override public boolean mouseClicked(double x,double y,int button){ if(!mouseInBoundingBox)return false; return super.mouseClicked(x,y,button); }
    @Override public void renderWidget(GuiGraphics g,int mouseX,int mouseY,float partialTick){
        mouseInBoundingBox=hasBounds ? mouseX>=boundMinX&&mouseX<=boundMaxX&&mouseY>=boundMinY&&mouseY<=boundMaxY : true;
        this.setTooltipDelay(Duration.ofMillis(mouseInBoundingBox ? 0 : 10000000));
        int v=texY+(isHovered()?texYDiff:0);
        g.blit(texture,getX(),getY(),texX,v,width,height,textureWidth,textureHeight);
    }
}
