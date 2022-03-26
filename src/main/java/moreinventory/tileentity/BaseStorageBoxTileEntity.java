package moreinventory.tileentity;

import moreinventory.block.StorageBoxBlock;
import moreinventory.tileentity.storagebox.StorageBoxInventorySize;
import moreinventory.tileentity.storagebox.StorageBoxType;
import moreinventory.tileentity.storagebox.StorageBoxTileEntityType;
import moreinventory.tileentity.storagebox.network.IStorageBoxNetwork;
import moreinventory.tileentity.storagebox.network.StorageBoxNetworkManager;
import moreinventory.util.MIMUtils;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BaseStorageBoxTileEntity extends LockableLootTileEntity implements IInventory, ITickableTileEntity, IStorageBoxNetwork {

    private ItemStack contents = ItemStack.EMPTY;
    protected NonNullList<ItemStack> storageItems;
    private LazyOptional<IItemHandlerModifiable> storageHandler;

    private StorageBoxNetworkManager networkManager = null;

    private StorageBoxType type;
    protected byte clickTime = 0;
    protected byte clickCount = 0;

    public static final String tagKeyContents = "contents";
    public static final String tagKeyTypeName = "typeName";

    public BaseStorageBoxTileEntity(StorageBoxType typeIn) {
        super(StorageBoxTileEntityType.map.get(typeIn));
        int inventorySize = getStorageStackSize(typeIn);
        storageItems = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
        this.type = typeIn;
    }

    @Override
    public int getSizeInventory() {
        return storageItems.size();
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("block.moreinventorymod.storage_box");
    }

    @Override
    public void func_230337_a_(BlockState state, CompoundNBT nbt) {
        super.func_230337_a_(state, nbt);

        if (!this.checkLootAndRead(nbt)) {
            this.type = StorageBoxType.valueOf(nbt.getString(tagKeyTypeName));
            this.storageItems = NonNullList.withSize(getStorageStackSize(type), ItemStack.EMPTY);
            MIMUtils.readNonNullListShort(nbt, this.storageItems);
            CompoundNBT contentsNBT = nbt.getCompound(tagKeyContents);
            ItemStack tmp = ItemStack.read(contentsNBT);
            if (tmp.getItem() == ItemStack.EMPTY.getItem() && tmp.getCount() == ItemStack.EMPTY.getCount()) {
                this.contents = ItemStack.EMPTY;
            } else {
                this.contents = tmp;
            }
        }
    }

    public void read(BlockState state, CompoundNBT nbt) {
        this.func_230337_a_(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        if (!this.checkLootAndWrite(compound)) {
            compound.putString(tagKeyTypeName, this.type.name());
            MIMUtils.writeNonNullListShort(compound, this.storageItems, true);
            CompoundNBT nbt = new CompoundNBT();
            contents.write(nbt);
            compound.put(tagKeyContents, nbt);
        }

        return compound;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.storageItems;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.storageItems = itemsIn;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return ChestContainer.createGeneric9X6(id, player, this);
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
        if (!(state.getBlock() instanceof StorageBoxBlock)) {
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
            if (getStackInSlot(i).getItem() != ItemStack.EMPTY.getItem()) {
                return false;
            }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return storageItems.get(index);
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
        storageItems.set(index, stack);

        markDirty();
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return world.getTileEntity(pos) == this
                && player.getDistanceSq(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 64;
    }

    public static int getStorageStackSize(StorageBoxType typeIn) {
        return StorageBoxInventorySize.map.get(typeIn).getInventorySize();
    }

    public StorageBoxType getStorageBoxType() {
        return this.type;
    }

    public boolean registerItems(ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().contains("Items")) {
            return false;
        }
        if (!hasContents() && stack.getItem() != ItemStack.EMPTY.getItem()) {
            contents = stack.copy();
            //            getStorageBoxNetworkManager().getBoxList().registerItem(xCoord, yCoord, zCoord, worldObj.provider.dimensionId, getContents());
            //            sendContents();

            return true;
        }

        return false;
    }

    private void clearRegister() {
        if (isEmpty()) {
            contents = ItemStack.EMPTY;
        }
    }

    private boolean mergeItemStack(ItemStack stack) {

        if (stack.getItem() == ItemStack.EMPTY.getItem()) {
            return false;
        }

        for (int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack slotItem = this.getStackInSlot(i);
            if (ItemStack.areItemStackTagsEqual(stack, slotItem) && ItemStack.areItemsEqual(stack, slotItem)) {
                //airじゃないスタックに追加
                if (slotItem.getCount() == slotItem.getMaxStackSize()) {
                    continue;
                }

                int sum = stack.getCount() + slotItem.getCount();
                if (sum <= stack.getMaxStackSize()) {
                    //溢れないならカウントをプラス
                    slotItem.setCount(sum);
                    stack.setCount(0);
                } else if (slotItem.getCount() < stack.getMaxStackSize()) {
                    //あふれるならスロットのカウントをマックスにし、追加するアイテムのカウントを減らす
                    stack.shrink(slotItem.getMaxStackSize() - slotItem.getCount());
                    slotItem.setCount(slotItem.getMaxStackSize());
                }
                this.setInventorySlotContents(i, slotItem.copy());
            } else if (slotItem.getItem() == ItemStack.EMPTY.getItem()) {
                //airのスタックに新しく追加
                this.setInventorySlotContents(i, stack.copy());
                stack.setCount(0);
            }

            if (stack.getCount() == 0) {
                return true;
            }
        }

        return false;
    }

    public boolean store(ItemStack stack) {
        if (this.world.getTileEntity(this.pos) == null)
            return false;

        boolean result = ItemStack.areItemsEqual(this.getContents(), stack)
                && ItemStack.areItemStackTagsEqual(stack, getContents()) && mergeItemStack(stack);
        BlockState newState = this.world.getBlockState(pos);
        this.world.notifyBlockUpdate(pos, this.getBlockState(), newState, 0);
        return result;
    }

    private void storeItemInInventory(IInventory inventory) {
        if (!hasContents())
            return;

        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.getItem() != ItemStack.EMPTY.getItem()) {
                store(stack);
            }
        }

    }

    public boolean rightClickEvent(World world, PlayerEntity player) {
        switch (++clickCount) {
        case 1:
            clickTime = 16;
            ItemStack itemstack = player.getHeldItemMainhand();
            if (!hasContents()) {
                registerItems(itemstack);
            }

            if (player.isSneaking()) {
                clearRegister();
            }

            store(itemstack);

            break;
        case 2:
            storeItemInInventory(player.inventory);
            player.tick();
            break;
        case 3:
            clickCount = 0;

            getStorageBoxNetworkManager().storeInventoryToNetwork(player.inventory, this.pos);
            player.tick();
            break;
        default:
            clickCount = 0;
            break;
        }

        return true;
    }

    @Override
    public void tick() {
        if (clickTime > 0 && --clickTime <= 0) {
            clickCount = 0;
        }
    }

    public void leftClickEvent(PlayerEntity player) {
        if (getContents() != null) {
            if (player.inventory.getFirstEmptyStack() != -1) {
                if (player.isSneaking()) {
                    player.inventory.addItemStackToInventory(loadItemStack(1));
                } else {
                    player.inventory.addItemStackToInventory(loadItemStack(0));
                }
            } else {
                //インベントリに空きスロットがなくても、同じアイテムを持っていたらぎりぎりまで持てるように
                int count = getContents().getMaxStackSize() -
                        player.inventory.count(getContents().getItem()) % getContents().getMaxStackSize();
                if (0 < count && count % getContents().getMaxStackSize() != 0) {
                    if (player.isSneaking()) {
                        player.inventory.addItemStackToInventory(loadItemStack(1));
                    } else {
                        player.inventory.addItemStackToInventory(loadItemStack(count));
                    }
                }
            }
        }
    }

    //アイテムをストレージから取り出す。
    //max:取り出す最大の数。0だと1スタック取り出す
    public ItemStack loadItemStack(int max) {
        int count = max == 0 ? contents.getMaxStackSize() : max;
        int requiredCount = count;
        int retCount = 0;

        for (int i = storageItems.size() - 1; 0 <= i; i--) {
            ItemStack storedStack = storageItems.get(i);
            if (!ItemStack.areItemsEqual(storedStack, contents)) {
                continue;
            }

            if (count <= storedStack.getCount()) {
                retCount += count;
                storedStack.shrink(count);
            } else {
                count -= storedStack.getCount();
                retCount += storedStack.getCount();
                storedStack.setCount(0);
            }

            if (retCount == requiredCount) {
                break;
            }
        }

        ItemStack ret = contents.copy();
        ret.setCount(retCount);

        BlockState newState = this.world.getBlockState(pos);
        this.world.notifyBlockUpdate(pos, this.getBlockState(), newState, 0);
        return ret;
    }

    public ItemStack getContents() {
        return this.contents;
    }

    public boolean hasContents() {
        return getContents().getItem() != ItemStack.EMPTY.getItem();
    }

    public int getAmount() {
        int count = 0;
        for (int i = 0; i < storageItems.size(); ++i) {
            ItemStack slot = getStackInSlot(i);
            if (slot.getItem() == getContents().getItem()) {
                count += slot.getCount();
            }
        }
        return count;
    }

    @Override
    public StorageBoxNetworkManager getStorageBoxNetworkManager() {
        if (networkManager == null) {
            makeNewNetwork();
        }

        return networkManager;
    }

    @Override
    public void setStorageBoxNetworkManager(StorageBoxNetworkManager manager) {
        networkManager = manager;
    }

    private void makeNewNetwork() {
        networkManager = new StorageBoxNetworkManager(this.world, this.pos);

    }

    public void test() {
    }

    //コンテナが置かれたときに呼び出される。他のコンテナが隣接していれば自身をネットワークに追加する。
    //複数のネットワークとつながった場合、それらを結合する
    public void onPlaced() {
        boolean multiple = false;
        for (Direction d : Direction.values()) {
            TileEntity tile = this.world.getTileEntity(this.pos.offset(d));
            if (tile instanceof BaseStorageBoxTileEntity) {
                BaseStorageBoxTileEntity tileStorageBox = (BaseStorageBoxTileEntity) tile;
                if (!multiple) {
                    tileStorageBox.getStorageBoxNetworkManager().add(this);
                    multiple = true;
                } else {
                    this.getStorageBoxNetworkManager().add(tileStorageBox.getStorageBoxNetworkManager());
                }
            }
        }
    }

    //隣接したコンテナが破壊されたときに呼ばれる
    //ネットワークから外し、破壊されたことによりネットワークが分断される場合は新たなネットワークを形成する。
    public void onDestroyedNeighbor(BlockPos destroyedPos) {
        int multiple = 0;
        for (Direction d : Direction.values()) {
            TileEntity tile = this.world.getTileEntity(destroyedPos.offset(d));
            if (tile instanceof BaseStorageBoxTileEntity) {
                BaseStorageBoxTileEntity tileStorageBox = (BaseStorageBoxTileEntity) tile;
                tileStorageBox.getStorageBoxNetworkManager().remove(destroyedPos);
                multiple++;
            }
        }

        if (1 < multiple) {
            for (Direction d : Direction.values()) {
                TileEntity tile = this.world.getTileEntity(this.pos.offset(d));
                if (tile instanceof BaseStorageBoxTileEntity) {
                    this.setStorageBoxNetworkManager(new StorageBoxNetworkManager(this.world, this.pos));
                }
            }
        }
    }
}
