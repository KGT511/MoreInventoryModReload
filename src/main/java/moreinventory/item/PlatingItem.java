package moreinventory.item;

import moreinventory.block.StorageBoxBlock;
import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.storagebox.StorageBox;
import moreinventory.storagebox.StorageBoxType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlatingItem extends Item {
    StorageBoxType type;

    public PlatingItem(StorageBoxType type) {
        super(new Properties()
                .durability(0)
                .stacksTo(64)
                .tab(MoreInventoryMOD.itemGroup));
        this.type = type;
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World level = context.getLevel();
        if (level.isClientSide) {
            return ActionResultType.PASS;
        }

        BlockPos pos = context.getClickedPos();
        TileEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BaseStorageBoxBlockEntity) {
            BaseStorageBoxBlockEntity storageBoxEntity = (BaseStorageBoxBlockEntity) blockEntity;
            StorageBoxType beforeType = storageBoxEntity.getStorageBoxType();
            int beforeTier = StorageBox.storageBoxMap.get(beforeType).tier;
            StorageBoxType afterType = this.type;
            int afterTier = StorageBox.storageBoxMap.get(afterType).tier;
            if (beforeTier != 0 && (afterTier == beforeTier || afterTier == beforeTier + 1)
                    && StorageBox.storageBoxMap.get(afterType).inventorySize > StorageBox.storageBoxMap.get(beforeType).inventorySize) {
                BaseStorageBoxBlockEntity newStorageBoxEntity = storageBoxEntity.upgrade(afterType);
                BlockState newBlockState = StorageBox.storageBoxMap.get(afterType).block.defaultBlockState().setValue(StorageBoxBlock.FACING,
                        storageBoxEntity.getBlockState().getValue(StorageBoxBlock.FACING));
                level.setBlock(pos, newBlockState, 0);
                level.setBlockEntity(pos, newStorageBoxEntity);
                level.blockEntityChanged(pos, newStorageBoxEntity);
                level.sendBlockUpdated(pos, storageBoxEntity.getBlockState(), level.getBlockState(pos), 0);
                newStorageBoxEntity.onPlaced();

                PlayerEntity player = context.getPlayer();

                if (!player.abilities.instabuild) {
                    stack.shrink(1);
                }

                return ActionResultType.SUCCESS;
            }

        }
        return ActionResultType.PASS;
    }

}
