package moreinventory.blockentity;

import javax.annotation.Nullable;

import moreinventory.block.Blocks;
import moreinventory.container.CatchallContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;

public class CatchallBlockEntity extends LockableLootTileEntity implements IInventory, ISidedInventory {
    public CatchallBlockEntity() {
        super(BlockEntities.CATCHALL_BLOCK_ENTITY_TYPE.get());
    }

    public static final int mainInventorySize = 36;
    public static final int armorInventorySize = 4;
    public static final int offHandInventorySize = 1;

    public static final int inventorySize = mainInventorySize + offHandInventorySize;

    private NonNullList<ItemStack> storage = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    private LazyOptional<IItemHandlerModifiable> storageHandler;

    @Override
    public int getContainerSize() {
        return storage.size();
    }

    @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent(Blocks.CATCHALL.get().getDescriptionId());
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        this.storage = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt)) {
            ItemStackHelper.loadAllItems(nbt, this.storage);
        }

    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if (!this.trySaveLootTable(compound)) {
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
        return new CatchallContainer(id, player, this);
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
        return storage.get(index);
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
        storage.set(index, stack);

        setChanged();
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.level.getBlockEntity(this.worldPosition) == this
                && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) < 64;
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

        for (int i = 0; i < getContainerSize(); ++i) {
            if (getItem(i) == ItemStack.EMPTY
                    && player.inventory.getItem(convertIdx(i)).getItem() != net.minecraft.item.Items.AIR) {
                setItem(i, player.inventory.removeItemNoUpdate(convertIdx(i)));
            }
        }

        player.tick();
    }

    public boolean canTransferToPlayer(PlayerEntity player) {
        for (int i = 0; i < getContainerSize(); ++i) {
            if (getItem(i) != ItemStack.EMPTY
                    && player.inventory.getItem(convertIdx(i)).getItem() != net.minecraft.item.Items.AIR) {
                return false;
            }
        }

        return true;
    }

    public void transferToPlayer(PlayerEntity player) {

        for (int i = 0; i < getContainerSize(); ++i) {
            if (player.inventory.getItem(convertIdx(i)).getItem() == net.minecraft.item.Items.AIR
                    && getItem(i) != ItemStack.EMPTY) {
                player.inventory.setItem(convertIdx(i), removeItemNoUpdate(i));
            }
        }

        player.tick();
    }

    private int convertIdx(int indexIn) {
        return indexIn < mainInventorySize ? indexIn : indexIn + armorInventorySize;
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
