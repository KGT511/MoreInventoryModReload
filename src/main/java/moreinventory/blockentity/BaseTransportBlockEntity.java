package moreinventory.blockentity;

import moreinventory.block.TransportBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class BaseTransportBlockEntity extends RandomizableContainerBlockEntity implements Container {

    public static final int inventorySize = 9;
    protected NonNullList<ItemStack> slotItems = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    private LazyOptional<IItemHandlerModifiable> storageHandler;

    public int currentSlot = 0;

    private byte updateTime = 0;

    protected BaseTransportBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public int getContainerSize() {
        return slotItems.size();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.slotItems = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, this.slotItems);
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        if (!this.trySaveLootTable(compound)) {
            ContainerHelper.saveAllItems(compound, this.slotItems);
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
    protected abstract Component getDefaultName();

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return null;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (this.storageHandler == null)
                this.storageHandler = LazyOptional.of(this::createHandler);
            return this.storageHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    private IItemHandlerModifiable createHandler() {
        var state = this.getBlockState();
        if (!(state.getBlock() instanceof TransportBlock)) {
            return new InvWrapper(this);
        }
        return new InvWrapper(this);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.getBlockPos(), 0, this.save(new CompoundTag()));
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.save(super.getUpdateTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (storageHandler != null)
            storageHandler.invalidate();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); ++i)
            if (this.getItem(i) != ItemStack.EMPTY)
                return false;

        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return slotItems.get(index);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (!this.getItem(index).isEmpty()) {
            var itemstack = getItem(index);
            this.setItem(index, ItemStack.EMPTY);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        slotItems.set(index, stack);
        this.setChanged();
    }

    protected abstract void doExtract();

    public static void tickFunc(Level level, BlockPos pos, BlockState state, BaseTransportBlockEntity blockEntity) {
        if (blockEntity instanceof BaseTransportBlockEntity) {
            var transportBaseEntity = (BaseTransportBlockEntity) blockEntity;
            transportBaseEntity.tick();
        }
    }

    public void tick() {
        if (!this.level.hasNeighborSignal(this.worldPosition)) {
            ++updateTime;
            if (updateTime % 20 == 0) {
                updateTime = 0;
                doExtract();
            }
        }
    }

    public static boolean canAccessFromSide(Container inventory, int slot, Direction side) {
        if (inventory instanceof WorldlyContainer) {
            for (var i : ((WorldlyContainer) inventory).getSlotsForFace(side)) {
                if (i == slot) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean canExtractFromSide(Container inventory, ItemStack itemstack, int slot, Direction side) {
        return !(inventory instanceof WorldlyContainer) || ((WorldlyContainer) inventory).canTakeItemThroughFace(slot, itemstack, side);
    }

    public static boolean canInsertFromSide(Container inventory, ItemStack itemstack, int slot, Direction side) {
        return inventory.canPlaceItem(slot, itemstack) && (!(inventory instanceof WorldlyContainer) || ((WorldlyContainer) inventory).canPlaceItemThroughFace(slot, itemstack, side));
    }

}
