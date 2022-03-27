package moreinventory.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import moreinventory.container.TransportContainer;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.data.lang.Text;
import moreinventory.network.ServerboundImporterUpdatePacket;
import moreinventory.tileentity.ImporterTileEntity;
import moreinventory.util.MIMUtils;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

@OnlyIn(Dist.CLIENT)
public class TransportContainerScreen extends ContainerScreen<TransportContainer> {

    private static ResourceLocation DISPENSER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/dispenser.png");
    private Button isWhiteButton;
    private Button isRegisterButton;

    public TransportContainerScreen(TransportContainer TransportManagerContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(TransportManagerContainer, inv, titleIn);
    }

    @Override
    protected void func_231160_c_() {//init
        super.func_231160_c_();
        if (this.container.getTile() instanceof ImporterTileEntity) {
            ImporterTileEntity tileEntity = (ImporterTileEntity) this.container.getTile();
            this.isWhiteButton = this.func_230480_a_(
                    new TransportContainerScreen.Button(guiLeft + xSize - 55, guiTop + 35,
                            new TranslationTextComponent(Text.importerMoveWhiteDetail), new TranslationTextComponent(Text.importerMoveBlackDetail),
                            tileEntity.getPos(), ImporterTileEntity.Val.WHITE.ordinal()));
            this.isRegisterButton = this.func_230480_a_(
                    new TransportContainerScreen.Button(guiLeft + 5, guiTop + 35,
                            new TranslationTextComponent(Text.importerRegisterOnDetail), new TranslationTextComponent(Text.importerRegisterOffDetail),
                            tileEntity.getPos(), ImporterTileEntity.Val.REGISTER.ordinal()));
            this.isWhiteButton.field_230693_o_ = true;
            this.isRegisterButton.field_230693_o_ = true;
        }
    }

    @Override
    public void func_230430_a_(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {//render
        this.func_230446_a_(matrixStack);
        super.func_230430_a_(matrixStack, mouseX, mouseY, partialTicks);
        this.func_230459_a_(matrixStack, mouseX, mouseY);
        if (this.container.getTile() instanceof ImporterTileEntity) {
            int i = (this.field_230708_k_ - this.xSize) / 2;
            int j = (this.field_230709_l_ - this.ySize) / 2;
            int xOffset = i, yOffset = j;
            ImporterTileEntity tileEntity = ((ImporterTileEntity) this.container.getTile());

            boolean isRegister = tileEntity.getIsRegister();
            boolean isWhite = tileEntity.getIswhite();
            TranslationTextComponent registerTxt = new TranslationTextComponent(isRegister ? Text.importerRegisterOn : Text.importerRegisterOff);
            TranslationTextComponent moveTxt = new TranslationTextComponent(isWhite ? Text.importerMoveWhite : Text.importerMoveBlack);
            func_238472_a_(matrixStack, this.field_230712_o_, registerTxt, 30 + xOffset, 40 + yOffset, 14737632);
            func_238472_a_(matrixStack, this.field_230712_o_, moveTxt, xSize - 30 + xOffset, 40 + yOffset, 14737632);
            this.drawCenteredStringWithoutShadow(matrixStack, new TranslationTextComponent(Text.importerMove), xSize - 30 + xOffset, 20 + yOffset, 328965);
            this.drawCenteredStringWithoutShadow(matrixStack, new TranslationTextComponent(Text.importerRegister), 30 + xOffset, 20 + yOffset, 328965);
            isWhiteButton.onValueUpdate(tileEntity);
            isRegisterButton.onValueUpdate(tileEntity);

        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void func_230450_a_(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {//renderBg
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_230706_i_.getTextureManager().bindTexture(DISPENSER_GUI_TEXTURE);
        int i = (this.field_230708_k_ - this.xSize) / 2;
        int j = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(p_230450_1_, i, j, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void func_230451_b_(MatrixStack matrixStack, int mouseX, int mouseY) {//renderLabels
        super.func_230451_b_(matrixStack, mouseX, mouseY);
        if (this.container.getTile() instanceof ImporterTileEntity) {
            this.isRegisterButton.func_230443_a_(matrixStack, mouseX, mouseY);
            this.isWhiteButton.func_230443_a_(matrixStack, mouseX, mouseY);
        }
    }

    private void drawCenteredStringWithoutShadow(MatrixStack poseStack, ITextComponent string, float x, float y, int color) {
        this.field_230712_o_.func_243248_b(poseStack, string, x - this.field_230712_o_.func_238414_a_(string) / 2, y, color);
    }

    @OnlyIn(Dist.CLIENT)
    class Button extends ExtendedButton {
        private boolean val = false;
        private ITextComponent trueTxt, falseTxt;
        private int id;

        protected Button(int x, int y, ITextComponent trueDisplayTxt, ITextComponent falseDisplayTxt, BlockPos blockPos, int id) {
            super(x, y, 53, 20, StringTextComponent.field_240750_d_, (p) -> {
                MoreInventoryMOD.CHANNEL.sendToServer(new ServerboundImporterUpdatePacket(blockPos, id));
            });
            this.trueTxt = trueDisplayTxt;
            this.falseTxt = falseDisplayTxt;
            this.id = id;
        }

        public boolean getVal() {
            return val;
        }

        public void setVal(boolean valIn) {
            val = valIn;
        }

        @Override
        public void func_230982_a_(double mouseX, double mouseY) {
            super.func_230982_a_(mouseX, mouseY);
        }

        @Override
        public void func_230443_a_(MatrixStack stack, int x, int y) {
            super.func_230443_a_(stack, x, y);
            if (this.func_230449_g_()) {
                ITextComponent txt = val ? trueTxt : falseTxt;
                TransportContainerScreen.this.func_238652_a_(stack, txt, x - this.field_230690_l_, y / 2);
            }
        }

        public void onValueUpdate(ImporterTileEntity blockEntity) {
            int val = blockEntity.getValByID(this.id);
            this.setVal(MIMUtils.intToBool(val));
        }
    }

}
