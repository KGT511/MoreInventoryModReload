package moreinventory.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import moreinventory.block.TransportBlock;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.tileentity.BaseTransportTileEntity;
import moreinventory.tileentity.ImporterTileEntity;
import moreinventory.util.MIMUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class TransportRenderer extends TileEntityRenderer<BaseTransportTileEntity> {
    private static final ResourceLocation IMPORTER_LIGHT_TEXTURE = new ResourceLocation(MoreInventoryMOD.MOD_ID, "textures/block/importer.png");
    private static final ResourceLocation IMPORTER_DARK_TEXTURE = new ResourceLocation(MoreInventoryMOD.MOD_ID, "textures/block/importer_black.png");
    private static final ResourceLocation EXPORTER_LIGHT_TEXTURE = new ResourceLocation(MoreInventoryMOD.MOD_ID, "textures/block/exporter.png");
    private static final ResourceLocation EXPORTER_DARK_TEXTURE = new ResourceLocation(MoreInventoryMOD.MOD_ID, "textures/block/exporter_black.png");

    private final ModelRenderer in1;
    private final ModelRenderer in2;
    private final ModelRenderer center;
    private final ModelRenderer out;

    public TransportRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        in1 = new ModelRenderer(64, 64, 0, 0);
        in1.x = 8.0F;
        in1.y = 8.0F;
        in1.z = 8.0F;
        in1.addBox(-3.0F, -5.0F, -3.0F, 6.0F, 1.0F, 6.F, 0.F);
        in2 = new ModelRenderer(64, 64, 0, 0);
        in2.x = 8.0F;
        in2.y = 8.0F;
        in2.z = 8.0F;
        in2.addBox(-2.0F, -3.0F, -2.0F, 4.0F, 1.0F, 4.F, 0.F);
        center = new ModelRenderer(64, 64, 0, 0);
        center.addBox(7.0F, 7.0F, 7.0F, 2.0F, 2.0F, 2.0F, 0.F);
        out = new ModelRenderer(64, 64, 0, 0);
        out.x = 8.0F;
        out.y = 8.0F;
        out.z = 8.0F;
        out.addBox(-2.0F, 2.0F, -2.0F, 4.0F, 1.0F, 4.F, 0.F);
    }

    private float degToRad(float deg) {
        return (float) (deg * Math.PI / 180.);
    }

    @Override
    public void render(BaseTransportTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.pushPose();
        ResourceLocation lightTexture, darkTexture;
        if (tileEntityIn instanceof ImporterTileEntity) {
            lightTexture = IMPORTER_LIGHT_TEXTURE;
            darkTexture = IMPORTER_DARK_TEXTURE;
        } else {
            lightTexture = EXPORTER_LIGHT_TEXTURE;
            darkTexture = EXPORTER_DARK_TEXTURE;
        }

        Direction inD = tileEntityIn.getBlockState().getValue(TransportBlock.FACING_IN);
        Direction outD = tileEntityIn.getBlockState().getValue(TransportBlock.FACING_OUT);
        rotateModels(inD, in1);
        rotateModels(inD, in2);
        rotateModels(outD.getOpposite(), out);

        byte emitLevel = (byte) (tileEntityIn.getLevel().getGameTime() % 40 / 10);

        ModelRenderer[] models = { in1, in2, center, out };
        IVertexBuilder lightIvertexbuilder = bufferIn.getBuffer(RenderType.entitySolid(lightTexture));
        models[emitLevel].render(matrixStackIn, lightIvertexbuilder, combinedLightIn, combinedOverlayIn);
        IVertexBuilder darkIvertexbuilder = bufferIn.getBuffer(RenderType.entitySolid(darkTexture));
        for (int i = 0; i < 3; ++i)
            models[MIMUtils.normalIndex(emitLevel + i + 1, 4)].render(matrixStackIn, darkIvertexbuilder, combinedLightIn, combinedOverlayIn);

        matrixStackIn.popPose();

    }

    private void rotateModels(Direction side, ModelRenderer model) {
        switch (side) {
        case DOWN:
            model.xRot = degToRad(0);
            model.zRot = degToRad(0);
            break;
        case UP:
            model.xRot = degToRad(180);
            model.zRot = degToRad(0);

            break;
        case NORTH:
            model.xRot = degToRad(90);
            model.zRot = degToRad(0);

            break;
        case SOUTH:
            model.xRot = degToRad(270);
            break;
        case WEST:
            model.zRot = degToRad(270);
            model.xRot = degToRad(0);

            break;
        case EAST:
            model.zRot = degToRad(90);
            model.xRot = degToRad(0);

            break;
        }
    }

}
