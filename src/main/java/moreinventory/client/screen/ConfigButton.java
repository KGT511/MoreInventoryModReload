package moreinventory.client.screen;

import java.util.function.BiConsumer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import moreinventory.core.MoreInventoryMOD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ConfigButton extends AbstractWidget {
    public static final ResourceLocation POUCH_GUI_TEXTURE = new ResourceLocation(MoreInventoryMOD.MOD_ID, "textures/gui/pouch.png");;

    private int iconIndexX, iconIndexY;
    private boolean val;
    private int id;

    private BiConsumer<Integer, Integer> sendFunc;

    public ConfigButton(int id, int x, int y, int width, int height,
            int iconX, int iconY, boolean val, Component text, BiConsumer<Integer, Integer> func) {
        super(x, y, width, height, text);
        this.id = id;
        this.iconIndexX = iconX;
        this.iconIndexY = iconY;
        this.val = val;
        this.sendFunc = func;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partial) {
        if (visible) {
            var minecraft = Minecraft.getInstance();

            RenderSystem.setShaderTexture(0, POUCH_GUI_TEXTURE);
            //            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            var x = this.getX();
            var y = this.getY();
            this.setFocused(mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + this.height);

            if (this.val) {
                blit(poseStack, x, y, 200, 104, 16, 16);
            } else {
                blit(poseStack, x, y, 184, 104, 16, 16);
            }

            blit(poseStack, x, y, this.iconIndexX, this.iconIndexY, 16, 16);

            if (this.val) {
                blit(poseStack, x, y, 216, 104, 16, 16);
            } else {
                blit(poseStack, x, y, 232, 104, 16, 16);
            }
            this.renderBg(poseStack, minecraft, mouseX, mouseY);
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

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {
        this.defaultButtonNarrationText(p_259858_);

    }

}
