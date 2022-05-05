package moreinventory.client.screen;

import java.util.function.BiConsumer;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import moreinventory.core.MoreInventoryMOD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ConfigButton extends Widget {
    public static ResourceLocation POUCH_GUI_TEXTURE = new ResourceLocation(MoreInventoryMOD.MODID, "textures/gui/pouch.png");;

    private int iconIndexX, iconIndexY;
    private boolean val;
    private int id;

    private BiConsumer<Integer, Integer> sendFunc;

    public ConfigButton(int id, int x, int y, int width, int height,
            int iconX, int iconY, boolean val, ITextComponent text, BiConsumer<Integer, Integer> func) {
        super(x, y, width, height, text);
        this.id = id;
        this.iconIndexX = iconX;
        this.iconIndexY = iconY;
        this.val = val;
        this.sendFunc = func;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partial) {
        if (visible) {
            Minecraft minecraft = Minecraft.getInstance();

            minecraft.getTextureManager().bind(POUCH_GUI_TEXTURE);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            this.setFocused(mouseX >= this.x && mouseY >= this.y && mouseX < this.x + width && mouseY < this.y + this.height);

            if (this.val) {
                blit(matrixStack, this.x, this.y, 200, 104, 16, 16);
            } else {
                blit(matrixStack, this.x, this.y, 184, 104, 16, 16);
            }

            blit(matrixStack, this.x, this.y, this.iconIndexX, this.iconIndexY, 16, 16);

            if (this.val) {
                blit(matrixStack, this.x, this.y, 216, 104, 16, 16);
            } else {
                blit(matrixStack, this.x, this.y, 232, 104, 16, 16);
            }
            this.renderBg(matrixStack, minecraft, mouseX, mouseY);
        }
    }

    public void setVal(boolean val) {
        this.val = val;
    }

    @Override
    public boolean clicked(double mouseX, double mouseY) {
        if (super.clicked(mouseX, mouseY)) {
            this.val = !this.val;
            this.sendFunc.accept(this.id, this.val ? 1 : 0);
            return true;
        }
        return false;

    }

}
