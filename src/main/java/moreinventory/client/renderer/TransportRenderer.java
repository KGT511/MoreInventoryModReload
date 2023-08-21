package moreinventory.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import moreinventory.block.TransportBlock;
import moreinventory.blockentity.BaseTransportBlockEntity;
import moreinventory.blockentity.ImporterBlockEntity;
import moreinventory.client.model.ModelLayers;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.util.MIMUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class TransportRenderer implements BlockEntityRenderer<BaseTransportBlockEntity> {
    private static ResourceLocation IMPORTER_LIGHT_TEXTURE = new ResourceLocation(MoreInventoryMOD.MOD_ID, "textures/block/importer.png");
    private static ResourceLocation IMPORTER_DARK_TEXTURE = new ResourceLocation(MoreInventoryMOD.MOD_ID, "textures/block/importer_black.png");
    private static ResourceLocation EXPORTER_LIGHT_TEXTURE = new ResourceLocation(MoreInventoryMOD.MOD_ID, "textures/block/exporter.png");
    private static ResourceLocation EXPORTER_DARK_TEXTURE = new ResourceLocation(MoreInventoryMOD.MOD_ID, "textures/block/exporter_black.png");

    private final ModelPart in1;
    private final ModelPart in2;
    private final ModelPart center;
    private final ModelPart out;

    private static final String in1Str = "in1";
    private static final String in2Str = "in2";
    private static final String centerStr = "center";
    private static final String outStr = "out";

    public TransportRenderer(Context context) {
        ModelPart modelpart = context.bakeLayer(ModelLayers.TRANSPORTER);
        this.in1 = modelpart.getChild(in1Str);
        this.in2 = modelpart.getChild(in2Str);
        this.center = modelpart.getChild(centerStr);
        this.out = modelpart.getChild(outStr);
    }

    public static LayerDefinition createBodyLayer() {
        var meshDefinition = new MeshDefinition();
        var partDefinition = meshDefinition.getRoot();
        partDefinition.addOrReplaceChild(in1Str, CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 1.0F, 6.F), PartPose.offset(8.0F, 8.0F, 8.0F));
        partDefinition.addOrReplaceChild(in2Str, CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 1.0F, 4.F), PartPose.offset(8.0F, 8.0F, 8.0F));
        partDefinition.addOrReplaceChild(centerStr, CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, 7.0F, 7.0F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
        partDefinition.addOrReplaceChild(outStr, CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 2.0F, -2.0F, 4.0F, 1.0F, 4.F), PartPose.offset(8.0F, 8.0F, 8.0F));
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    private float degToRad(float deg) {
        return (float) (deg * Math.PI / 180.);
    }

    @Override
    public void render(BaseTransportBlockEntity blockEntityIn, float partialTicks, PoseStack poseStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        poseStackIn.pushPose();
        ResourceLocation lightTexture, darkTexture;
        if (blockEntityIn instanceof ImporterBlockEntity) {
            lightTexture = IMPORTER_LIGHT_TEXTURE;
            darkTexture = IMPORTER_DARK_TEXTURE;
        } else {
            lightTexture = EXPORTER_LIGHT_TEXTURE;
            darkTexture = EXPORTER_DARK_TEXTURE;
        }

        var inD = blockEntityIn.getBlockState().getValue(TransportBlock.FACING_IN);
        var outD = blockEntityIn.getBlockState().getValue(TransportBlock.FACING_OUT);
        this.rotateModels(inD, in1);
        this.rotateModels(inD, in2);
        this.rotateModels(outD.getOpposite(), out);

        var emitLevel = (byte) (blockEntityIn.getLevel().getGameTime() % 40 / 10);

        ModelPart[] models = { in1, in2, center, out };
        var lightIvertexbuilder = bufferIn.getBuffer(RenderType.entitySolid(lightTexture));
        models[emitLevel].render(poseStackIn, lightIvertexbuilder, combinedLightIn, combinedOverlayIn);
        var darkIvertexbuilder = bufferIn.getBuffer(RenderType.entitySolid(darkTexture));
        for (int i = 0; i < 3; ++i)
            models[MIMUtils.normalIndex(emitLevel + i + 1, 4)].render(poseStackIn, darkIvertexbuilder, combinedLightIn, combinedOverlayIn);

        poseStackIn.popPose();

    }

    private void rotateModels(Direction side, ModelPart model) {
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
