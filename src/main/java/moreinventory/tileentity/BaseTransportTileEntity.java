package moreinventory.tileentity;

import moreinventory.block.TransportBlock;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class BaseTransportTileEntity extends LockableLootTileEntity implements IInventory, ITickableTileEntity {

    public static final int inventorySize = 9;
    protected NonNullList<ItemStack> slotItems = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    private LazyOptional<IItemHandlerModifiable> storageHandler;

    public int currentSlot = 0;

    private byte updateTime = 0;
    private byte clientUpdateTime = 0;
    private byte level = 0;

    protected BaseTransportTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    @Override
    public int getSizeInventory() {
        return slotItems.size();
    }

    @Override
    public void func_230337_a_(BlockState state, CompoundNBT nbt) {
        super.func_230337_a_(state, nbt);
        this.slotItems = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.slotItems);
        }
    }

    public void read(BlockState state, CompoundNBT nbt) {
        this.func_230337_a_(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (!this.checkLootAndWrite(compound)) {
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
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        if (this.storageHandler != null) {
            this.storageHandler.invalidate();
            this.storageHandler = null;
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.removed && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (this.storageHandler == null)
                this.storageHandler = LazyOptional.of(this::createHandler);
            return this.storageHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    private IItemHandlerModifiable createHandler() {
        BlockState state = this.getBlockState();
        if (!(state.getBlock() instanceof TransportBlock)) {
            return new InvWrapper(this);
        }
        return new InvWrapper(this);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 0, this.write(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(this.world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(super.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(this.world.getBlockState(pos), tag);
    }

    @Override
    public void remove() {
        super.remove();
        if (storageHandler != null)
            storageHandler.invalidate();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getSizeInventory(); ++i)
            if (getStackInSlot(i) != ItemStack.EMPTY)
                return false;

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return slotItems.get(index);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (!getStackInSlot(index).isEmpty()) {
            ItemStack itemstack = getStackInSlot(index);
            setInventorySlotContents(index, ItemStack.EMPTY);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        slotItems.set(index, stack);
        markDirty();
    }

    protected abstract void doExtract();

    @Override
    public void tick() {
        if (!this.world.isBlockPowered(this.pos)) {
            if (!this.world.isRemote()) {
                ++updateTime;
                if (updateTime % 20 == 0) {
                    updateTime = 0;
                    doExtract();
                }
            } else {
                ++clientUpdateTime;
                if (clientUpdateTime % 10 == 0) {
                    clientUpdateTime = 0;
                    ++level;
                    level %= 4;
                }
            }
        }
    }

    public static boolean canAccessFromSide(IInventory inventory, int slot, Direction side) {
        if (inventory instanceof ISidedInventory) {
            for (int i : ((ISidedInventory) inventory).getSlotsForFace(side)) {
                if (i == slot) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean canExtractFromSide(IInventory inventory, ItemStack itemstack, int slot, Direction side) {
        return !(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canExtractItem(slot, itemstack, side);
    }

    public static boolean canInsertFromSide(IInventory inventory, ItemStack itemstack, int slot, Direction side) {
        return inventory.isItemValidForSlot(slot, itemstack) && (!(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canInsertItem(slot, itemstack, side));
    }

    public byte getLevel() {
        return level;
    }

}
