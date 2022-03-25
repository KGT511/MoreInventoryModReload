package moreinventory.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import moreinventory.block.BlockTransportManager;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.tileentity.BaseTileEntityTransportManager;
import moreinventory.tileentity.TileEntityImporter;
import moreinventory.util.MIMUtils;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class TransportManagerRenderer extends TileEntityRenderer<BaseTileEntityTransportManager> {
    private static ResourceLocation IMPORTER_LIGHT_TEXTURE = new ResourceLocation(MoreInventoryMOD.MOD_ID, "textures/block/importer.png");
    private static ResourceLocation IMPORTER_DARK_TEXTURE = new ResourceLocation(MoreInventoryMOD.MOD_ID, "textures/block/importer_black.png");
    private static ResourceLocation EXPORTER_LIGHT_TEXTURE = new ResourceLocation(MoreInventoryMOD.MOD_ID, "textures/block/exporter.png");
    private static ResourceLocation EXPORTER_DARK_TEXTURE = new ResourceLocation(MoreInventoryMOD.MOD_ID, "textures/block/exporter_black.png");

    private final ModelRenderer in1;
    private final ModelRenderer in2;
    private final ModelRenderer center;
    private final ModelRenderer out;

    public TransportManagerRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        in1 = new ModelRenderer(64, 64, 0, 0);
        in1.rotationPointX = 8.0F;
        in1.rotationPointY = 8.0F;
        in1.rotationPointZ = 8.0F;
        in1.addBox(-3.0F, -5.0F, -3.0F, 6.0F, 1.0F, 6.F, 0.F);
        in2 = new ModelRenderer(64, 64, 0, 0);
        in2.rotationPointX = 8.0F;
        in2.rotationPointY = 8.0F;
        in2.rotationPointZ = 8.0F;
        in2.addBox(-2.0F, -3.0F, -2.0F, 4.0F, 1.0F, 4.F, 0.F);
        center = new ModelRenderer(64, 64, 0, 0);
        center.addBox(7.0F, 7.0F, 7.0F, 2.0F, 2.0F, 2.0F, 0.F);
        out = new ModelRenderer(64, 64, 0, 0);
        out.rotationPointX = 8.0F;
        out.rotationPointY = 8.0F;
        out.rotationPointZ = 8.0F;
        out.addBox(-2.0F, 2.0F, -2.0F, 4.0F, 1.0F, 4.F, 0.F);
    }

    private float degToRad(float deg) {
        return (float) (deg * Math.PI / 180.);
    }

    @Override
    public void render(BaseTileEntityTransportManager tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        ResourceLocation lightTexture, darkTexture;
        if (tileEntityIn instanceof TileEntityImporter) {
            lightTexture = IMPORTER_LIGHT_TEXTURE;
            darkTexture = IMPORTER_DARK_TEXTURE;
        } else {
            lightTexture = EXPORTER_LIGHT_TEXTURE;
            darkTexture = EXPORTER_DARK_TEXTURE;
        }

        Direction inD = tileEntityIn.getBlockState().get(BlockTransportManager.FACING_IN);
        Direction outD = tileEntityIn.getBlockState().get(BlockTransportManager.FACING_OUT);
        rotateModels(inD, in1);
        rotateModels(inD, in2);
        rotateModels(outD.getOpposite(), out);

        byte level = tileEntityIn.getLevel();

        ModelRenderer[] models = { in1, in2, center, out };
        IVertexBuilder lightIvertexbuilder = bufferIn.getBuffer(RenderType.getEntitySolid(lightTexture));
        models[level].render(matrixStackIn, lightIvertexbuilder, combinedLightIn, combinedOverlayIn);
        IVertexBuilder darkIvertexbuilder = bufferIn.getBuffer(RenderType.getEntitySolid(darkTexture));
        for (int i = 0; i < 3; ++i)
            models[MIMUtils.normalIndex(level + i + 1, 4)].render(matrixStackIn, darkIvertexbuilder, combinedLightIn, combinedOverlayIn);

        matrixStackIn.pop();

    }

    private void rotateModels(Direction side, ModelRenderer model) {
        switch (side) {
        case DOWN:
            model.rotateAngleX = degToRad(0);
            model.rotateAngleZ = degToRad(0);
            break;
        case UP:
            model.rotateAngleX = degToRad(180);
            model.rotateAngleZ = degToRad(0);

            break;
        case NORTH:
            model.rotateAngleX = degToRad(90);
            model.rotateAngleZ = degToRad(0);

            break;
        case SOUTH:
            model.rotateAngleX = degToRad(270);
            break;
        case WEST:
            model.rotateAngleZ = degToRad(270);
            model.rotateAngleX = degToRad(0);

            break;
        case EAST:
            model.rotateAngleZ = degToRad(90);
            model.rotateAngleX = degToRad(0);

            break;
        }
    }

}
