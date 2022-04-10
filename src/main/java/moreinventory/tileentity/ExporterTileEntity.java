package moreinventory.tileentity;

import java.util.List;

import moreinventory.block.Blocks;
import moreinventory.block.TransportBlock;
import moreinventory.container.TransportContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ExporterTileEntity extends BaseTransportTileEntity {

    public ExporterTileEntity() {
        super(TileEntities.EXPORTER_TILE_TYPE);
    }

    private BlockPos boxPos = BlockPos.ZERO;
    private int currentSlot = 0;

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent(Blocks.EXPORTER.getDescriptionId());
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new TransportContainer(id, player, this);
    }

    @Override
    protected void doExtract() {
        Direction out = this.level.getBlockState(this.worldPosition).getValue(TransportBlock.FACING_OUT);
        BlockPos outPos = this.worldPosition.relative(out);
        IInventory inventory = HopperTileEntity.getContainerAt(this.level, outPos);

        if (inventory != null) {
            extract: for (int i = 0; i < 9; i++) {
                ItemStack itemstack = slotItems.get(currentSlot);

                if (++currentSlot == 9) {
                    currentSlot = 0;
                }

                if (itemstack.getItem() != ItemStack.EMPTY.getItem()
                        && getBoxPos(itemstack) && !(this.worldPosition.equals(boxPos))) {
                    BaseStorageBoxTileEntity tile = (BaseStorageBoxTileEntity) this.level.getBlockEntity(boxPos);

                    for (int j = 0; j < tile.getContainerSize(); j++) {
                        ItemStack itemstack1 = tile.getItem(j);

                        if (mergeItemStack(itemstack1, inventory, out.getOpposite())) {
                            break extract;
                        }
                    }
                }
            }
        }
        BlockState newState = this.level.getBlockState(boxPos);
        this.level.sendBlockUpdated(boxPos, this.getBlockState(), newState, 0);
    }

    private boolean getBoxPos(ItemStack itemstack) {
        Direction in = this.level.getBlockState(this.worldPosition).getValue(TransportBlock.FACING_IN);
        BlockPos inPos = this.worldPosition.relative(in);//pull
        Direction out = this.level.getBlockState(this.worldPosition).getValue(TransportBlock.FACING_OUT);
        BlockPos outPos = this.worldPosition.relative(out);//put

        TileEntity tile = this.level.getBlockEntity(inPos);

        if (tile != null && tile instanceof BaseStorageBoxTileEntity) {
            List<BaseStorageBoxTileEntity> list = ((BaseStorageBoxTileEntity) tile).getStorageBoxNetworkManager().getMatchingList(itemstack, inPos);

            for (BaseStorageBoxTileEntity tileStorageBox : list) {
                if (!tileStorageBox.getBlockPos().equals(outPos)) {//入力と出力が同じネットワークになるのを防ぐ
                    boxPos = tileStorageBox.getBlockPos();
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean mergeItemStack(ItemStack itemstack, IInventory inventory, Direction side) {
        if (itemstack.getItem() == ItemStack.EMPTY.getItem()) {
            return false;
        }

        boolean success = false;
        int size = inventory.getContainerSize();

        if (itemstack.isStackable()) {
            for (int i = 0; i < size; ++i) {
                ItemStack item = inventory.getItem(i);

                if (item != null && item.getItem() == itemstack.getItem() && itemstack.getDamageValue() == item.getDamageValue() && ItemStack.tagMatches(itemstack, item)) {
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
                ItemStack item = inventory.getItem(i);

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
