package moreinventory.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import moreinventory.block.CatchallBlock;
import moreinventory.blockentity.CatchallBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector3f;

public class CatchallRenderer extends TileEntityRenderer<CatchallBlockEntity> {

    public final int width = 9;
    public final int height = CatchallBlockEntity.inventorySize / width;

    public CatchallRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);

    }

    @Override
    public void render(CatchallBlockEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Direction direction = tileEntityIn.getBlockState().getValue(CatchallBlock.FACING);
        float f = direction.toYRot();
        matrixStackIn.pushPose();

        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-f));
        matrixStackIn.translate(-0.5D, -0.5D, -0.5D);

        NonNullList<ItemStack> nonnulllist = tileEntityIn.getItems();
        final int slotWidth = 3;
        final double scale = 16.D;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width / slotWidth; ++j) {
                for (int k = 0; k < slotWidth; ++k) {
                    int val = i * width + (width / slotWidth - j - 1) * slotWidth + k;
                    ItemStack itemstack = nonnulllist.get(val);
                    if (itemstack == ItemStack.EMPTY)
                        continue;
                    if (itemstack.getItem() == Items.AIR)
                        continue;
                    matrixStackIn.pushPose();

                    int x = (k + 1) * 3 + k * 2;
                    int y = (height - i) * 3;
                    int z = (j + 1) * 3 + j * 2;
                    matrixStackIn.translate(x / scale, y / scale, z / scale);
                    matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                    matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180.F));
                    matrixStackIn.scale(0.25F, 0.25F, 0.25F);
                    Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
                    matrixStackIn.popPose();
                }
            }
        }
        matrixStackIn.popPose();

    }
}