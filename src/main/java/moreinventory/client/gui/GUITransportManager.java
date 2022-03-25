package moreinventory.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import moreinventory.container.ContainerTransportManager;
import moreinventory.tileentity.TileEntityImporter;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

@OnlyIn(Dist.CLIENT)
public class GUITransportManager extends ContainerScreen<ContainerTransportManager> {

    private static ResourceLocation DISPENSER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/dispenser.png");
    private Button isWhiteButton;
    private Button isRegisterButton;

    public GUITransportManager(ContainerTransportManager TransportManagerContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(TransportManagerContainer, inv, titleIn);
    }

    @Override
    public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {//render
        this.func_230446_a_(matrixStack);
        super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrixStack, mouseX, mouseY);

        //        if (this.container.getTile().getIswhite()) {
        //            func_146283_a(fontRendererObj.listFormattedStringToWidth(I18n.format("transportmanager.gui.include.tooltip"), this.xSize / 2), mouseX, mouseY);
        //        } else {
        //            func_146283_a(fontRendererObj.listFormattedStringToWidth(I18n.format("transportmanager.gui.exclude.tooltip"), this.xSize / 2), mouseX, mouseY);
        //        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void func_230450_a_(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {//drawGuiContainerBackgroundLayer
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_230706_i_.getTextureManager().bindTexture(DISPENSER_GUI_TEXTURE);
        int i = (this.field_230708_k_ - this.xSize) / 2;
        int j = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(p_230450_1_, i, j, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void func_230451_b_(MatrixStack matrixStack, int p_230451_2_, int p_230451_3_) {//draw (offsetted by texture)
        super.func_230451_b_(matrixStack, p_230451_2_, p_230451_3_);
        if (this.container.getTile() instanceof TileEntityImporter) {
            TranslationTextComponent registerTxt;
            TranslationTextComponent moveTxt;
            //        this.container.getTile().setIsWhite(isWhiteButton.getVal());
            //        this.container.getTile().setIsRegister(isRegisterButton.getVal());
            if (((TileEntityImporter) this.container.getTile()).getIsRegister()) {
                registerTxt = new TranslationTextComponent("gui.moreinventorymod.importer.register_on");
            } else {
                registerTxt = new TranslationTextComponent("gui.moreinventorymod.importer.register_off");
            }
            if (((TileEntityImporter) this.container.getTile()).getIswhite()) {
                moveTxt = new TranslationTextComponent("gui.moreinventorymod.importer.move_white");
            } else {
                moveTxt = new TranslationTextComponent("gui.moreinventorymod.importer.move_black");
            }
            func_238472_a_(matrixStack, this.field_230712_o_, registerTxt, 30, 40, 14737632);
            func_238472_a_(matrixStack, this.field_230712_o_, moveTxt, xSize - 30, 40, 14737632);
            func_238472_a_(matrixStack, this.field_230712_o_, new TranslationTextComponent("gui.moreinventorymod.importer.register"), 30, 20, 14737632);
            func_238472_a_(matrixStack, this.field_230712_o_, new TranslationTextComponent("gui.moreinventorymod.importer.move"), xSize - 30, 20, 14737632);
            for (Widget widget : this.field_230710_m_) {
                if (widget.func_230449_g_()) {
                    widget.func_230443_a_(matrixStack, p_230451_2_ - this.guiLeft, p_230451_3_ - this.guiTop);
                    break;
                }
            }
        }
    }

    @Override
    protected void func_231160_c_() {//maybe init
        super.func_231160_c_();
        if (this.container.getTile() instanceof TileEntityImporter) {
            isWhiteButton = this.func_230480_a_(new GUITransportManager.Button(guiLeft + xSize - 58, guiTop + 35,
                    new TranslationTextComponent("gui.moreinventorymod.importer.move_white.detail"),
                    new TranslationTextComponent("gui.moreinventorymod.importer.move_black.detail")));
            isRegisterButton = this.func_230480_a_(new GUITransportManager.Button(guiLeft + 5, guiTop + 35,
                    new TranslationTextComponent("gui.moreinventorymod.importer.register_on.detail"),
                    new TranslationTextComponent("gui.moreinventorymod.importer.register_off.detail")));
            isWhiteButton.field_230693_o_ = true;
            isRegisterButton.field_230693_o_ = true;

            //        isWhiteButton.setVal(this.container.getTile().getIswhite());
            //        isRegisterButton.setVal(this.container.getTile().getIsRegister());
        }
    }

    @Override
    public void func_231023_e_() {//callback
        super.func_231023_e_();
        //        MIMLog.warning("" + this.container.getTile());
        //        CompoundNBT tag = this.container.getTile().getUpdateTag();
        //        tag.putBoolean(TileEntityImporter.isWhiteKey, isWhiteButton.getVal());
        //        tag.putBoolean(TileEntityImporter.registerKey, isRegisterButton.getVal());
        //        this.container.getTile().handleUpdateTag(this.container.getTile().getBlockState(), tag);
        //        MIMLog.warning("" + this.container.getTile().getBlockState());//.getTileEntity(new BlockPos(-177, 54, -25)));
    }

    @OnlyIn(Dist.CLIENT)
    class Button extends AbstractButton {
        private boolean val = false;
        private ITextComponent trueTxt, falseTxt;

        protected Button(int p_i50825_1_, int p_i50825_2_, ITextComponent trueDisplayTxt, ITextComponent falseDisplayTxt) {
            super(p_i50825_1_, p_i50825_2_, 53, 20, StringTextComponent.field_240750_d_);
            trueTxt = trueDisplayTxt;
            falseTxt = falseDisplayTxt;
        }

        public boolean getVal() {
            return val;
        }

        public void setVal(boolean valIn) {
            val = valIn;
        }

        @Override
        public void func_230982_a_(double p_230982_1_, double p_230982_3_) {//called by click
            //            val = !val;
        }

        @Override
        public void func_230930_b_() {//called by click when want to close
        }

        public void func_230443_a_(MatrixStack p_230443_1_, int p_230443_2_, int p_230443_3_) {
            ITextComponent txt = val ? trueTxt : falseTxt;
            int length = txt.getString().length();
            GUITransportManager.this.func_238652_a_(p_230443_1_, txt, p_230443_2_ - length / 2 * 5, p_230443_3_);
        }
    }

}
