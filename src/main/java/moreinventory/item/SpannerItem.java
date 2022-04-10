package moreinventory.item;

import moreinventory.block.Blocks;
import moreinventory.core.MoreInventoryMOD;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpannerItem extends Item {
    public SpannerItem() {
        super(new Properties()
                .durability(0)
                .tab(MoreInventoryMOD.itemGroup));
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getLevel();
        if (world.isClientSide) {
            return ActionResultType.PASS;
        }

        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        if (!player.isShiftKeyDown()) {
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (Blocks.blockList.contains(block)) {
                world.setBlockAndUpdate(pos, block.rotate(world.getBlockState(pos), world, pos, Rotation.CLOCKWISE_90));
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.FAIL;
    }
}