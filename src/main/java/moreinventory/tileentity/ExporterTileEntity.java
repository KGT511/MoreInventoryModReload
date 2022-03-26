package moreinventory.tileentity;

import java.util.List;

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
        return new TranslationTextComponent("block.moreinventorymod.exporter");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new TransportContainer(id, player, this);
    }

    @Override
    protected void doExtract() {
        Direction out = this.world.getBlockState(this.pos).get(TransportBlock.FACING_OUT);
        BlockPos outPos = this.pos.offset(out);
        IInventory inventory = HopperTileEntity.getInventoryAtPosition(this.world, outPos);

        if (inventory != null) {
            extract: for (int i = 0; i < 9; i++) {
                ItemStack itemstack = slotItems.get(currentSlot);

                if (++currentSlot == 9) {
                    currentSlot = 0;
                }

                if (itemstack.getItem() != ItemStack.EMPTY.getItem()
                        && getBoxPos(itemstack) && !(this.pos.equals(boxPos))) {
                    BaseStorageBoxTileEntity tile = (BaseStorageBoxTileEntity) this.world.getTileEntity(boxPos);

                    for (int j = 0; j < tile.getSizeInventory(); j++) {
                        ItemStack itemstack1 = tile.getStackInSlot(j);

                        if (mergeItemStack(itemstack1, inventory, out.getOpposite())) {
                            break extract;
                        }
                    }
                }
            }
        }
        BlockState newState = this.world.getBlockState(boxPos);
        this.world.notifyBlockUpdate(boxPos, this.getBlockState(), newState, 0);
    }

    private boolean getBoxPos(ItemStack itemstack) {
        Direction in = this.world.getBlockState(this.pos).get(TransportBlock.FACING_IN);
        BlockPos inPos = this.pos.offset(in);//pull
        Direction out = this.world.getBlockState(this.pos).get(TransportBlock.FACING_OUT);
        BlockPos outPos = this.pos.offset(out);//put

        TileEntity tile = this.world.getTileEntity(inPos);

        if (tile != null && tile instanceof BaseStorageBoxTileEntity) {
            List<BaseStorageBoxTileEntity> list = ((BaseStorageBoxTileEntity) tile).getStorageBoxNetworkManager().getMatchingList(itemstack, inPos);

            for (BaseStorageBoxTileEntity tileStorageBox : list) {
                if (!tileStorageBox.getPos().equals(outPos)) {//入力と出力が同じネットワークになるのを防ぐ
                    boxPos = tileStorageBox.getPos();
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
        int size = inventory.getSizeInventory();

        if (itemstack.isStackable()) {
            for (int i = 0; i < size; ++i) {
                ItemStack item = inventory.getStackInSlot(i);

                if (item != null && item.getItem() == itemstack.getItem() && itemstack.getDamage() == item.getDamage() && ItemStack.areItemStackTagsEqual(itemstack, item)) {
                    if (canAccessFromSide(inventory, i, side) && canInsertFromSide(inventory, itemstack, i, side)) {
                        int sum = item.getCount() + itemstack.getCount();

                        if (sum <= itemstack.getMaxStackSize()) {
                            itemstack.setCount(0);
                            item.setCount(sum);
                            inventory.setInventorySlotContents(i, item.copy());
                            success = true;
                        } else if (item.getCount() < itemstack.getMaxStackSize()) {
                            itemstack.shrink(itemstack.getMaxStackSize() - item.getCount());
                            item.setCount(itemstack.getMaxStackSize());
                            inventory.setInventorySlotContents(i, item.copy());
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
                ItemStack item = inventory.getStackInSlot(i);

                if (item.getItem() == ItemStack.EMPTY.getItem() && canAccessFromSide(inventory, i, side) && canInsertFromSide(inventory, itemstack, i, side)) {
                    inventory.setInventorySlotContents(i, itemstack.copy());
                    itemstack.setCount(0);
                    success = true;
                    break;
                }
            }
        }

        return success;
    }
}
