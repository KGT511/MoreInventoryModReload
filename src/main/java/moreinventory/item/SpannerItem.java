package moreinventory.item;

import moreinventory.block.Blocks;
import moreinventory.core.MoreInventoryMOD;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Rotation;

public class SpannerItem extends Item {
    public SpannerItem() {
        super(new Properties()
                .durability(0)
                .tab(MoreInventoryMOD.creativeModeTab));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        var level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.PASS;
        }

        var player = context.getPlayer();
        var pos = context.getClickedPos();
        if (!player.isShiftKeyDown()) {
            var state = level.getBlockState(pos);
            var block = state.getBlock();
            if (Blocks.blockList.contains(block)) {
                level.setBlockAndUpdate(pos, block.rotate(level.getBlockState(pos), level, pos, Rotation.CLOCKWISE_90));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}