package moreinventory.blockentity;

import moreinventory.block.Blocks;
import moreinventory.block.TransportBlock;
import moreinventory.container.TransportContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ExporterBlockEntity extends BaseTransportBlockEntity {

    public ExporterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.EXPORTER_BLOCK_ENTITY_TYPE, pos, state);
    }

    private BlockPos boxPos = BlockPos.ZERO;
    private int currentSlot = 0;

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent(Blocks.EXPORTER.getDescriptionId());
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new TransportContainer(id, player, this);
    }

    @Override
    protected void doExtract() {
        var out = this.level.getBlockState(this.worldPosition).getValue(TransportBlock.FACING_OUT);
        var outPos = this.worldPosition.relative(out);
        var inventory = HopperBlockEntity.getContainerAt(this.level, outPos);

        if (inventory != null) {
            extract: for (int i = 0; i < 9; i++) {
                var itemstack = slotItems.get(currentSlot);

                if (++currentSlot == 9) {
                    currentSlot = 0;
                }

                if (itemstack.getItem() != ItemStack.EMPTY.getItem()
                        && getBoxPos(itemstack) && !(this.worldPosition.equals(boxPos))) {
                    var blockEntity = (BaseStorageBoxBlockEntity) this.level.getBlockEntity(boxPos);

                    for (int j = 0; j < blockEntity.getContainerSize(); j++) {
                        var itemstack1 = blockEntity.getItem(j);

                        if (mergeItemStack(itemstack1, inventory, out.getOpposite())) {
                            break extract;
                        }
                    }
                }
            }
        }
        var newState = this.level.getBlockState(boxPos);
        this.level.sendBlockUpdated(boxPos, this.getBlockState(), newState, 0);
    }

    private boolean getBoxPos(ItemStack itemstack) {
        var in = this.level.getBlockState(this.worldPosition).getValue(TransportBlock.FACING_IN);
        var inPos = this.worldPosition.relative(in);//pull
        var out = this.level.getBlockState(this.worldPosition).getValue(TransportBlock.FACING_OUT);
        var outPos = this.worldPosition.relative(out);//put

        var blockEntity = this.level.getBlockEntity(inPos);

        if (blockEntity != null && blockEntity instanceof BaseStorageBoxBlockEntity) {
            var list = ((BaseStorageBoxBlockEntity) blockEntity).getStorageBoxNetworkManager().getMatchingList(itemstack, inPos);

            for (var blockEntityStorageBox : list) {
                if (!blockEntityStorageBox.getBlockPos().equals(outPos)) {//入力と出力が同じネットワークになるのを防ぐ
                    boxPos = blockEntityStorageBox.getBlockPos();
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean mergeItemStack(ItemStack itemstack, Container inventory, Direction side) {
        if (itemstack.getItem() == ItemStack.EMPTY.getItem()) {
            return false;
        }

        boolean success = false;
        int size = inventory.getContainerSize();

        if (itemstack.isStackable()) {
            for (int i = 0; i < size; ++i) {
                var item = inventory.getItem(i);

                if (item != null && item.getItem() == itemstack.getItem() && itemstack.getDamageValue() == item.getDamageValue() && ItemStack.isSameItemSameTags(itemstack, item)) {
                    if (canAccessFromSide(inventory, i, side) && canInsertFromSide(inventory, itemstack, i, side)) {
                        int sum = item.getCount() + itemstack.getCount();

                        if (sum <= itemstack.getMaxStackSize()) {
                            itemstack.setCount(0);
                            item.setCount(sum);
                            inventory.setItem(i, item.copy());
                            success = true;
                        } else if (item.getCount() < itemstack.getMaxStackSize()) {
                            itemstack.shrink(itemstack.getMaxStackSize() - item.getCount());
                            item.setCount(itemstack.getMaxStackSize());
                            inventory.setItem(i, item.copy());
                            success = true;
                        }
                    }
                }

                if (itemstack.getCount() <= 0) {
                    return success;
                }
            }
        }

        if (itemstack.getCount() > 0) {
            for (int i = 0; i < size; ++i) {
                var item = inventory.getItem(i);

                if (item.getItem() == ItemStack.EMPTY.getItem() && canAccessFromSide(inventory, i, side) && canInsertFromSide(inventory, itemstack, i, side)) {
                    inventory.setItem(i, itemstack.copy());
                    itemstack.setCount(0);
                    success = true;
                    break;
                }
            }
        }

        return success;
    }
}
