package moreinventory.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import moreinventory.block.BlockCatchall;
import moreinventory.container.ContainerCatchall;

public class TileEntityCatchall extends LockableLootTileEntity implements IInventory {
    public TileEntityCatchall() {
        super(TileEntities.CATCHALL_TILE_TYPE);
    }

    public static final int mainInventorySize = 36;
    public static final int armorInventorySize = 4;
    public static final int offHandInventorySize = 1;

    public static final int inventorySize = mainInventorySize + offHandInventorySize;

    private NonNullList<ItemStack> storage = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    private LazyOptional<IItemHandlerModifiable> storageHandler;

    @Override
    public int getSizeInventory() {
        return storage.size();
    }

    @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent("block.moreinventorymod.catchall");
    }

    @Override
    public void func_230337_a_(BlockState state, CompoundNBT nbt) {
        super.func_230337_a_(state, nbt);
        this.storage = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.storage);
        }

    }

    public void read(BlockState state, CompoundNBT nbt) {
        this.func_230337_a_(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.storage);
        }

        return compound;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.storage;
    }

    @Override
    public void setItems(NonNullList<ItemStack> itemsIn) {
        this.storage = itemsIn;
    }

    @Override
    public Container createMenu(int id, PlayerInventory player) {
        return new ContainerCatchall(id, player, this);
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
        if (!(state.getBlock() instanceof BlockCatchall)) {
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
        return storage.get(index);
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
        storage.set(index, stack);

        markDirty();
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return world.getTileEntity(pos) == this
                && player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 64;
    }

    public boolean transferTo(PlayerEntity player) {
        if (isEmpty()) {
            transferToBlock(player);
            return true;
        }

        if (canTransferToPlayer(player)) {
            transferToPlayer(player);
            return true;
        }

        return false;
    }

    private void transferToBlock(PlayerEntity player) {

        for (int i = 0; i < getSizeInventory(); ++i) {
            if (getStackInSlot(i) == ItemStack.EMPTY
                    && player.inventory.getStackInSlot(convertIdx(i)).getItem() != net.minecraft.item.Items.AIR) {
                setInventorySlotContents(i, player.inventory.removeStackFromSlot(convertIdx(i)));
            }
        }

        player.tick();
    }

    public boolean canTransferToPlayer(PlayerEntity player) {
        for (int i = 0; i < getSizeInventory(); ++i) {
            if (getStackInSlot(i) != ItemStack.EMPTY
                    && player.inventory.getStackInSlot(convertIdx(i)).getItem() != net.minecraft.item.Items.AIR) {
                return false;
            }
        }

        return true;
    }

    public void transferToPlayer(PlayerEntity player) {

        for (int i = 0; i < getSizeInventory(); ++i) {
            if (player.inventory.getStackInSlot(convertIdx(i)).getItem() == net.minecraft.item.Items.AIR
                    && getStackInSlot(i) != ItemStack.EMPTY) {
                player.inventory.setInventorySlotContents(convertIdx(i), removeStackFromSlot(i));
            }
        }

        player.tick();
    }

    private int convertIdx(int indexIn) {
        return indexIn < mainInventorySize ? indexIn : indexIn + armorInventorySize;
    }

}
