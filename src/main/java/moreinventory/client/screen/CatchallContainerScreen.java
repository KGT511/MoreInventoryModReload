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
        this.imageWidth = 176 + 27;
        this.imageHeight = 190;
        this.inventoryLabelY = this.imageHeight - 96 + 2;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(CATCHALL_GUI_TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(p_230450_1_, i, j, 0, 0, this.imageWidth, this.imageHeight * 18 + 17);
    }

}