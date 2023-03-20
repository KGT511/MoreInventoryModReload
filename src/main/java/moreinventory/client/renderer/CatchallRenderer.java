package moreinventory.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import moreinventory.block.CatchallBlock;
import moreinventory.blockentity.CatchallBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CatchallRenderer implements BlockEntityRenderer<CatchallBlockEntity> {

    public final int width = 9;
    public final int height = CatchallBlockEntity.inventorySize / width;

    public CatchallRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CatchallBlockEntity blockEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        var direction = blockEntityIn.getBlockState().getValue(CatchallBlock.FACING);
        float f = direction.toYRot();
        matrixStackIn.pushPose();

        matrixStackIn.translate(0.5D, 0.5D, 0.5D);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-f));
        matrixStackIn.translate(-0.5D, -0.5D, -0.5D);

        var nonnulllist = blockEntityIn.getItems();
        final int slotWidth = 3;
        final double scale = 16.D;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width / slotWidth; ++j) {
                for (int k = 0; k < slotWidth; ++k) {
                    int val = i * width + (width / slotWidth - j - 1) * slotWidth + k;
                    var itemstack = nonnulllist.get(val);
                    if (itemstack == ItemStack.EMPTY)
                        continue;
                    if (itemstack.getItem() == Items.AIR)
                        continue;
                    matrixStackIn.pushPose();

                    int x = (k + 1) * 3 + k * 2;
                    int y = (height - i) * 3;
                    int z = (j + 1) * 3 + j * 2;
                    matrixStackIn.translate(x / scale, y / scale, z / scale);
                    matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
                    matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.F));
                    matrixStackIn.scale(0.25F, 0.25F, 0.25F);
                    Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn, 0);
                    matrixStackIn.popPose();
                }
            }
        }
        matrixStackIn.popPose();

    }
}