package moreinventory.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import moreinventory.block.StorageBoxBlock;
import moreinventory.tileentity.BaseStorageBoxTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class StorageBoxRenderer extends TileEntityRenderer<BaseStorageBoxTileEntity> {
    public StorageBoxRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(BaseStorageBoxTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        BlockState blockstate = tileEntityIn.getBlockState();
        ItemStack contents = tileEntityIn.getContents();

        if (contents.getItem() == ItemStack.EMPTY.getItem())
            return;

        matrixStackIn.pushPose();
        float f = blockstate.getValue(StorageBoxBlock.FACING).toYRot();
        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-f));
        matrixStackIn.translate(0.D, 1.D / 16.D * 2.D, 0.5D);
        float scale = 0.75F;
        matrixStackIn.scale(scale, scale, scale);
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(180.F));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180.F));
        Minecraft.getInstance().getItemRenderer().renderStatic(contents, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
        matrixStackIn.popPose();

        matrixStackIn.pushPose();
        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-f + 180));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180));

        int amount = tileEntityIn.getAmount();
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
        this.renderer.font.drawInBatch(text, 0.F, 0.F, 0xF0F0F0, false, matrixStackIn.last().pose(), bufferIn, false, 0, combinedLightIn);
        matrixStackIn.popPose();
    }
}
