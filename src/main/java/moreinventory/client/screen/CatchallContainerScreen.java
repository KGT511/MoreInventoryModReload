package moreinventory.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import moreinventory.container.CatchallContainer;
import moreinventory.core.MoreInventoryMOD;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CatchallContainerScreen extends ContainerScreen<CatchallContainer> {
    private static ResourceLocation CATCHALL_GUI_TEXTURE = new ResourceLocation(MoreInventoryMOD.MODID, "textures/gui/catchall.png");

    public CatchallContainerScreen(CatchallContainer catchallContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(catchallContainer, inv, titleIn);
        this.xSize = 176 + 27;
        this.ySize = 190;
        this.field_238745_s_ = this.ySize - 96 + 2;
    }

    @Override
    public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {//render
        this.func_230446_a_(matrixStack);
        super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrixStack, mouseX, mouseY);

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void func_230450_a_(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {//drawGuiContainerBackgroundLayer
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_230706_i_.getTextureManager().bindTexture(CATCHALL_GUI_TEXTURE);
        int i = (this.field_230708_k_ - this.xSize) / 2;
        int j = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(p_230450_1_, i, j, 0, 0, this.xSize, this.ySize * 18 + 17);
    }

}