package moreinventory.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import moreinventory.blockentity.ImporterBlockEntity;
import moreinventory.container.TransportContainer;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.data.lang.Text;
import moreinventory.network.ServerboundImporterUpdatePacket;
import moreinventory.util.MIMUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ExtendedButton;

@OnlyIn(Dist.CLIENT)
public class TransportContainerScreen extends AbstractContainerScreen<TransportContainer> {

    private static ResourceLocation DISPENSER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/dispenser.png");
    private Button isWhiteButton;
    private Button isRegisterButton;

    public TransportContainerScreen(TransportContainer TransportManagerContainer, Inventory inv, Component titleIn) {
        super(TransportManagerContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        if (this.menu.getBlockEntity() instanceof ImporterBlockEntity) {
            var importerBlockEntity = ((ImporterBlockEntity) this.menu.getBlockEntity());
            this.isWhiteButton = this.addRenderableWidget(
                    new TransportContainerScreen.Button(leftPos + imageWidth - 55, topPos + 35,
                            new TranslatableComponent(Text.importerMoveWhiteDetail), new TranslatableComponent(Text.importerMoveBlackDetail),
                            importerBlockEntity.getBlockPos(), ImporterBlockEntity.Val.WHITE.ordinal()));
            this.isRegisterButton = this.addRenderableWidget(
                    new TransportContainerScreen.Button(leftPos + 5, topPos + 35,
                            new TranslatableComponent(Text.importerRegisterOnDetail), new TranslatableComponent(Text.importerRegisterOffDetail),
                            importerBlockEntity.getBlockPos(), ImporterBlockEntity.Val.REGISTER.ordinal()));
            this.isWhiteButton.active = true;
            this.isRegisterButton.active = true;
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);

        if (this.menu.getBlockEntity() instanceof ImporterBlockEntity) {
            int i = (this.width - this.imageWidth) / 2;
            int j = (this.height - this.imageHeight) / 2;
            int xOffset = i, yOffset = j;
            var importerBlockEntity = ((ImporterBlockEntity) this.menu.getBlockEntity());

            var isRegister = importerBlockEntity.getIsRegister();
            var isWhite = importerBlockEntity.getIswhite();
            var registerTxt = new TranslatableComponent(isRegister ? Text.importerRegisterOn : Text.importerRegisterOff);
            var moveTxt = new TranslatableComponent(isWhite ? Text.importerMoveWhite : Text.importerMoveBlack);
            drawCenteredString(poseStack, this.font, registerTxt, 30 + xOffset, 40 + yOffset, 14737632);
            drawCenteredString(poseStack, this.font, moveTxt, imageWidth - 30 + xOffset, 40 + yOffset, 14737632);
            this.drawCenteredStringWithoutShadow(poseStack, new TranslatableComponent(Text.importerMove), imageWidth - 30 + xOffset, 20 + yOffset, 328965);
            this.drawCenteredStringWithoutShadow(poseStack, new TranslatableComponent(Text.importerRegister), 30 + xOffset, 20 + yOffset, 328965);
            isWhiteButton.onValueUpdate(importerBlockEntity);
            isRegisterButton.onValueUpdate(importerBlockEntity);

        }
    }

    @Override
    protected void renderBg(PoseStack poseStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, DISPENSER_GUI_TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        super.renderLabels(poseStack, mouseX, mouseY);
        if (this.menu.getBlockEntity() instanceof ImporterBlockEntity) {

            this.isRegisterButton.renderToolTip(poseStack, mouseX, mouseY);
            this.isWhiteButton.renderToolTip(poseStack, mouseX, mouseY);

        }
    }

    private void drawCenteredStringWithoutShadow(PoseStack poseStack, Component string, float x, float y, int color) {
        this.font.draw(poseStack, string, x - this.font.width(string) / 2, y, color);
    }

    @OnlyIn(Dist.CLIENT)
    class Button extends ExtendedButton {
        private boolean val = false;
        private Component trueTxt, falseTxt;
        private int id;

        protected Button(int x, int y, Component trueDisplayTxt, Component falseDisplayTxt, BlockPos blockPos, int id) {
            super(x, y, 53, 20, TextComponent.EMPTY, (p) -> {
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
        public void onClick(double mouseX, double mouseY) {
            super.onClick(mouseX, mouseY);
        }

        @Override
        public void renderToolTip(PoseStack poseStack, int x, int y) {
            super.renderToolTip(poseStack, x, y);
            if (this.isHoveredOrFocused()) {
                var txt = val ? trueTxt : falseTxt;
                TransportContainerScreen.this.renderTooltip(poseStack, txt, x - this.x, y / 2);
            }
        }

        public void onValueUpdate(ImporterBlockEntity blockEntity) {
            int val = blockEntity.getValByID(this.id);
            this.setVal(MIMUtils.intToBool(val));
        }

    }

}
