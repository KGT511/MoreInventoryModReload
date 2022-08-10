package moreinventory.blockentity;

import moreinventory.block.StorageBoxBlock;
import moreinventory.blockentity.storagebox.StorageBoxInventorySize;
import moreinventory.blockentity.storagebox.StorageBoxType;
import moreinventory.blockentity.storagebox.StorageBoxTypeBlockEntity;
import moreinventory.blockentity.storagebox.network.IStorageBoxNetwork;
import moreinventory.blockentity.storagebox.network.StorageBoxNetworkManager;
import moreinventory.inventory.PouchInventory;
import moreinventory.item.PouchItem;
import moreinventory.util.MIMUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BaseStorageBoxBlockEntity extends RandomizableContainerBlockEntity implements Container, IStorageBoxNetwork {

    private ItemStack contents = ItemStack.EMPTY;
    protected NonNullList<ItemStack> storageItems;
    private LazyOptional<IItemHandlerModifiable> storageHandler;

    private StorageBoxNetworkManager networkManager = null;

    private StorageBoxType type;
    protected byte clickTime = 0;
    protected byte clickCount = 0;

    public static final String tagKeyContents = "contents";
    public static final String tagKeyTypeName = "typeName";

    public BaseStorageBoxBlockEntity(StorageBoxType typeIn, BlockPos pos, BlockState state) {
        super(StorageBoxTypeBlockEntity.map.get(typeIn), pos, state);
        int inventorySize = getStorageStackSize(typeIn);
        storageItems = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
        this.type = typeIn;
    }

    @Override
    public int getContainerSize() {
        return storageItems.size();
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent(StorageBoxTypeBlockEntity.blockMap.get(this.type).getDescriptionId());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        if (!this.tryLoadLootTable(nbt)) {
            this.type = StorageBoxType.valueOf(nbt.getString(tagKeyTypeName));
            this.storageItems = NonNullList.withSize(getStorageStackSize(type), ItemStack.EMPTY);
            MIMUtils.readNonNullListShort(nbt, this.storageItems);
            var contentsNBT = nbt.getCompound(tagKeyContents);
            var tmp = ItemStack.of(contentsNBT);
            if (tmp.getItem() == ItemStack.EMPTY.getItem() && tmp.getCount() == ItemStack.EMPTY.getCount()) {
                this.contents = ItemStack.EMPTY;
            } else {
                this.contents = tmp;
            }
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);

        if (!this.trySaveLootTable(compound)) {
            compound.putString(tagKeyTypeName, this.type.name());
            MIMUtils.writeNonNullListShort(compound, this.storageItems, true);
            var nbt = new CompoundTag();
            contents.save(nbt);
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
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return ChestMenu.sixRows(id, inventory, this);
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
        if (!(state.getBlock() instanceof StorageBoxBlock)) {
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
        for (int i = 0; i < getContainerSize(); ++i)
            if (!this.getItem(i).isEmpty()) {
                return false;
            }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return storageItems.get(index);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (!getItem(index).isEmpty()) {
            var itemstack = getItem(index);
            setItem(index, ItemStack.EMPTY);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        storageItems.set(index, stack);

        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) < 64;
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

        for (int i = 0; i < this.getContainerSize(); ++i) {
            var slotItem = this.getItem(i);
            if (ItemStack.isSameItemSameTags(stack, slotItem)) {
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
                this.setItem(i, slotItem.copy());
            } else if (slotItem.isEmpty()) {
                //airのスタックに新しく追加
                this.setItem(i, stack.copy());
                stack.setCount(0);
            }

            if (stack.getCount() == 0) {
                return true;
            }
        }

        return false;
    }

    public boolean store(ItemStack stack) {
        if (this.level.getBlockEntity(this.worldPosition) == null)
            return false;

        boolean result = ItemStack.isSameItemSameTags(this.getContents(), stack) && mergeItemStack(stack);
        var newState = this.level.getBlockState(this.worldPosition);
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), newState, 0);
        return result;
    }

    private void storeItemInInventory(Container inventory) {
        if (!hasContents())
            return;

        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            var stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof PouchItem) {
                    var pouch = new PouchInventory(stack);
                    if (pouch.getIsStorageBox()) {
                        pouch.collectedByStorageBox(this);
                    }
                } else {
                    store(stack);
                }
            }
        }

    }

    public boolean rightClickEvent(Level level, Player player) {
        switch (++clickCount) {
        case 1:
            clickTime = 16;
            var itemstack = player.getMainHandItem();
            if (!hasContents()) {
                registerItems(itemstack);
            }

            if (player.isShiftKeyDown()) {
                clearRegister();
            }

            store(itemstack);

            break;
        case 2:
            storeItemInInventory(player.getInventory());
            player.tick();
            break;
        case 3:
            clickCount = 0;

            getStorageBoxNetworkManager().storeInventoryToNetwork(player.getInventory(), this.worldPosition);
            player.tick();
            break;
        default:
            clickCount = 0;
            break;
        }

        return true;
    }

    public static void tickFunc(Level level, BlockPos pos, BlockState state, BaseStorageBoxBlockEntity entity) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BaseStorageBoxBlockEntity) {
            var storageBoxBaseEntity = (BaseStorageBoxBlockEntity) blockEntity;
            storageBoxBaseEntity.tick();
        }
    }

    public void tick() {
        if (clickTime > 0 && --clickTime <= 0) {
            clickCount = 0;
        }
    }

    public void leftClickEvent(Player player) {
        if (getContents() != null) {
            if (player.getInventory().getFreeSlot() != -1) {
                if (player.isShiftKeyDown()) {
                    player.getInventory().add(loadItemStack(1));
                } else {
                    player.getInventory().add(loadItemStack(0));
                }
            } else {
                //インベントリに空きスロットがなくても、同じアイテムを持っていたらぎりぎりまで持てるように
                int count = getContents().getMaxStackSize() -
                        player.getInventory().countItem(getContents().getItem()) % getContents().getMaxStackSize();
                if (0 < count && count % getContents().getMaxStackSize() != 0) {
                    if (player.isShiftKeyDown()) {
                        player.getInventory().add(loadItemStack(1));
                    } else {
                        player.getInventory().add(loadItemStack(count));
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
            var storedStack = storageItems.get(i);
            if (!ItemStack.isSame(storedStack, contents)) {
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

        var ret = contents.copy();
        ret.setCount(retCount);

        var newState = this.level.getBlockState(this.worldPosition);
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), newState, 0);
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
            var slot = getItem(i);
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
        networkManager = new StorageBoxNetworkManager(this.level, this.worldPosition);

    }

    //コンテナが置かれたときに呼び出される。他のコンテナが隣接していれば自身をネットワークに追加する。
    //複数のネットワークとつながった場合、それらを結合する
    public void onPlaced() {
        boolean multiple = false;
        for (Direction d : Direction.values()) {
            var blockEntity = this.level.getBlockEntity(this.worldPosition.relative(d));
            if (blockEntity instanceof BaseStorageBoxBlockEntity) {
                var baseStorageBoxBlockEntity = (BaseStorageBoxBlockEntity) blockEntity;
                if (!multiple) {
                    baseStorageBoxBlockEntity.getStorageBoxNetworkManager().add(this);
                    multiple = true;
                } else {
                    this.getStorageBoxNetworkManager().add(baseStorageBoxBlockEntity.getStorageBoxNetworkManager());
                }
            }
        }
    }

    //隣接したコンテナが破壊されたときに呼ばれる
    //ネットワークから外し、破壊されたことによりネットワークが分断される場合は新たなネットワークを形成する。
    public void onDestroyedNeighbor(BlockPos destroyedPos) {
        int multiple = 0;
        for (Direction d : Direction.values()) {
            var blockEntity = this.level.getBlockEntity(destroyedPos.relative(d));
            if (blockEntity instanceof BaseStorageBoxBlockEntity) {
                var baseStorageBoxBlockEntity = (BaseStorageBoxBlockEntity) blockEntity;
                baseStorageBoxBlockEntity.getStorageBoxNetworkManager().remove(destroyedPos);
                multiple++;
            }
        }
        if (1 < multiple) {
            this.setStorageBoxNetworkManager(new StorageBoxNetworkManager(this.level, this.worldPosition));
        }
    }

}