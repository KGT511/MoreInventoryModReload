package moreinventory.client.screen;

import java.util.function.BiConsumer;

import moreinventory.core.MoreInventoryMOD;
import net.minecraft.client.gui.GuiGraphics;
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
    public void renderWidget(GuiGraphics poseStack, int mouseX, int mouseY, float partial) {
        if (visible) {

            var x = this.getX();
            var y = this.getY();
            this.setFocused(mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + this.height);

            if (this.val) {
                poseStack.blit(POUCH_GUI_TEXTURE, x, y, 200, 104, 16, 16);
            } else {
                poseStack.blit(POUCH_GUI_TEXTURE, x, y, 184, 104, 16, 16);
            }

            poseStack.blit(POUCH_GUI_TEXTURE, x, y, this.iconIndexX, this.iconIndexY, 16, 16);

            if (this.val) {
                poseStack.blit(POUCH_GUI_TEXTURE, x, y, 216, 104, 16, 16);
            } else {
                poseStack.blit(POUCH_GUI_TEXTURE, x, y, 232, 104, 16, 16);
            }
            //            this.render(poseStack, mouseX, mouseY, partial);
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
