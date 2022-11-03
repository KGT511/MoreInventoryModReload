package moreinventory.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import moreinventory.blockentity.ImporterBlockEntity;
import moreinventory.container.TransportContainer;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.data.lang.Text;
import moreinventory.network.ServerboundImporterUpdatePacket;
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

    private static final ResourceLocation DISPENSER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/dispenser.png");
    private Button isWhiteButton;
    private Button isRegisterButton;

    public TransportContainerScreen(TransportContainer TransportManagerContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(TransportManagerContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        if (this.menu.getTile() instanceof ImporterBlockEntity) {
            ImporterBlockEntity tileEntity = (ImporterBlockEntity) this.menu.getTile();
            this.isWhiteButton = this.addButton(
                    new TransportContainerScreen.Button(leftPos + imageWidth - 55, topPos + 35,
                            new TranslationTextComponent(Text.importerMoveWhiteDetail), new TranslationTextComponent(Text.importerMoveBlackDetail),
                            tileEntity.getBlockPos(), ImporterBlockEntity.Val.WHITE.ordinal()));
            this.isRegisterButton = this.addButton(
                    new TransportContainerScreen.Button(leftPos + 5, topPos + 35,
                            new TranslationTextComponent(Text.importerRegisterOnDetail), new TranslationTextComponent(Text.importerRegisterOffDetail),
                            tileEntity.getBlockPos(), ImporterBlockEntity.Val.REGISTER.ordinal()));
            this.isWhiteButton.active = true;
            this.isRegisterButton.active = true;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        if (this.menu.getTile() instanceof ImporterBlockEntity) {
            int i = (this.width - this.imageWidth) / 2;
            int j = (this.height - this.imageHeight) / 2;
            int xOffset = i, yOffset = j;
            ImporterBlockEntity tileEntity = ((ImporterBlockEntity) this.menu.getTile());

            boolean isRegister = tileEntity.getIsRegister();
            boolean isWhite = tileEntity.getIswhite();
            TranslationTextComponent registerTxt = new TranslationTextComponent(isRegister ? Text.importerRegisterOn : Text.importerRegisterOff);
            TranslationTextComponent moveTxt = new TranslationTextComponent(isWhite ? Text.importerMoveWhite : Text.importerMoveBlack);
            drawCenteredString(matrixStack, this.font, registerTxt, 30 + xOffset, 40 + yOffset, 14737632);
            drawCenteredString(matrixStack, this.font, moveTxt, imageWidth - 30 + xOffset, 40 + yOffset, 14737632);
            MIMUtils.drawCenteredStringWithoutShadow(matrixStack, this.font, new TranslationTextComponent(Text.importerMove), imageWidth - 30 + xOffset, 20 + yOffset, 328965);
            MIMUtils.drawCenteredStringWithoutShadow(matrixStack, this.font, new TranslationTextComponent(Text.importerRegister), 30 + xOffset, 20 + yOffset, 328965);
            isWhiteButton.onValueUpdate(tileEntity);
            isRegisterButton.onValueUpdate(tileEntity);

        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void renderBg(MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(DISPENSER_GUI_TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(p_230450_1_, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.renderLabels(matrixStack, mouseX, mouseY);
        if (this.menu.getTile() instanceof ImporterBlockEntity) {
            this.isRegisterButton.renderToolTip(matrixStack, mouseX, mouseY);
            this.isWhiteButton.renderToolTip(matrixStack, mouseX, mouseY);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class Button extends ExtendedButton {
        private boolean val = false;
        private ITextComponent trueTxt, falseTxt;
        private int id;

        protected Button(int x, int y, ITextComponent trueDisplayTxt, ITextComponent falseDisplayTxt, BlockPos blockPos, int id) {
            super(x, y, 53, 20, StringTextComponent.EMPTY, (p) -> {
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
        public void renderToolTip(MatrixStack stack, int x, int y) {
            super.renderToolTip(stack, x, y);
            if (this.isHovered()) {
                ITextComponent txt = val ? trueTxt : falseTxt;
                TransportContainerScreen.this.renderTooltip(stack, txt, x - this.x, y / 2);
            }
        }

        public void onValueUpdate(ImporterBlockEntity blockEntity) {
            int val = blockEntity.getValByID(this.id);
            this.setVal(MIMUtils.intToBool(val));
        }
    }

}
