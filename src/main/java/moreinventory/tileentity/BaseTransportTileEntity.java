package moreinventory.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;

public abstract class BaseTransportTileEntity extends LockableLootTileEntity implements IInventory, ITickableTileEntity, ISidedInventory {

    public static final int inventorySize = 9;
    protected NonNullList<ItemStack> slotItems = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    private LazyOptional<IItemHandlerModifiable> storageHandler;

    public int currentSlot = 0;

    private byte updateTime = 0;

    protected BaseTransportTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    @Override
    public int getContainerSize() {
        return slotItems.size();
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.slotItems = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.slotItems);
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if (!this.trySaveLootTable(compound)) {
            ItemStackHelper.saveAllItems(compound, this.slotItems);
        }

        return compound;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return slotItems;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        slotItems = itemsIn;
    }

    @Override
    protected abstract ITextComponent getDefaultName();

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }

    @Override
    public void clearCache() {
        super.clearCache();
        if (this.storageHandler != null) {
            this.storageHandler.invalidate();
            this.storageHandler = null;
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return LazyOptional.empty();
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getBlockPos(), 0, this.save(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.load(this.level.getBlockState(pkt.getPos()), pkt.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(super.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.load(this.level.getBlockState(this.worldPosition), tag);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (storageHandler != null)
            storageHandler.invalidate();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); ++i)
            if (getItem(i) != ItemStack.EMPTY)
                return false;

        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return slotItems.get(index);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (!getItem(index).isEmpty()) {
            ItemStack itemstack = getItem(index);
            setItem(index, ItemStack.EMPTY);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        slotItems.set(index, stack);
        setChanged();
    }

    protected abstract void doExtract();

    @Override
    public void tick() {
        if (!this.level.hasNeighborSignal(this.worldPosition)) {
            if (!this.level.isClientSide()) {
                ++updateTime;
                if (updateTime % 20 == 0) {
                    updateTime = 0;
                    doExtract();
                }
            }
        }
    }

    @Override
    public int[] getSlotsForFace(Direction p_180463_1_) {
        return new int[] {};
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_180462_1_, ItemStack p_180462_2_, @Nullable Direction p_180462_3_) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int p_180461_1_, ItemStack p_180461_2_, Direction p_180461_3_) {
        return false;
    }

    @Override
    public boolean canPlaceItem(int p_94041_1_, ItemStack p_94041_2_) {
        return false;
    }
}
