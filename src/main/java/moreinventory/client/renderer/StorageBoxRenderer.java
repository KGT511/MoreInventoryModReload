package moreinventory.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import moreinventory.block.StorageBoxBlock;
import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class StorageBoxRenderer implements BlockEntityRenderer<BaseStorageBoxBlockEntity> {
    private final Font font;

    public StorageBoxRenderer(Context context) {
        font = context.getFont();
    }

    @Override
    public void render(BaseStorageBoxBlockEntity blockEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        var blockstate = blockEntityIn.getBlockState();
        var contents = blockEntityIn.getContents();

        if (contents.getItem() == ItemStack.EMPTY.getItem())
            return;

        matrixStackIn.pushPose();
        float f = blockstate.getValue(StorageBoxBlock.FACING).toYRot();
        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-f));
        matrixStackIn.translate(0.D, 1.D / 16.D * 2.D, 0.5D);
        float scale = 0.75F;
        matrixStackIn.scale(scale, scale, scale);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.F));
        Minecraft.getInstance().getItemRenderer().renderStatic(contents, ItemDisplayContext.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, blockEntityIn.getLevel(), 0);
        matrixStackIn.popPose();

        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-f + 180));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180));

        int amount = blockEntityIn.getAmount();
        int stackSize = amount / contents.getMaxStackSize();
        int surplus = amount % contents.getMaxStackSize();

        String text = "";
        if (0 < stackSize)
            text += "[" + stackSize + "]";
        if (0 < stackSize && 0 < surplus)
            text += "+";
        if (0 < surplus)
            text += surplus;
        float textScale = 0.0175F;

        matrixStackIn.translate(-text.length() / 2.D * 2. * (5. + (text.length() % 2 == 0 ? 0.5 : 0)) / 7. / 16., 0.5 - 1.D / 16.D * 3.5D, -0.5001D);
        matrixStackIn.scale(textScale, textScale, textScale);
        this.font.drawInBatch(text, 0.F, 0.F, 0xF0F0F0, false, matrixStackIn.last().pose(), bufferIn, Font.DisplayMode.NORMAL, 0, combinedLightIn);
        matrixStackIn.popPose();
    }
}
