package moreinventory.blockentity.storagebox.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import moreinventory.inventory.PouchInventory;
import moreinventory.item.PouchItem;
import moreinventory.storagebox.StorageBoxType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StorageBoxNetworkManager {

    private HashMap<BlockPos, BaseStorageBoxBlockEntity> network = new HashMap<>();

    public StorageBoxNetworkManager(World world, BlockPos pos) {
        this(world, pos, null);
    }

    public StorageBoxNetworkManager(World world, BlockPos pos, @Nullable BlockPos ignorePos) {
        this.createNewNetwork(world, pos, ignorePos);
    }

    //再帰的にネットワークを探索し、登録する
    private void createNewNetwork(World world, BlockPos pos, @Nullable BlockPos ignorePos) {
        if (ignorePos != null && ignorePos.equals(pos)) {
            return;
        }

        TileEntity tile = world.getBlockEntity(pos);

        if (tile != null && tile instanceof IStorageBoxNetwork && tile instanceof BaseStorageBoxBlockEntity) {
            BaseStorageBoxBlockEntity tileStorageBox = (BaseStorageBoxBlockEntity) tile;
            network.put(tileStorageBox.getBlockPos(), tileStorageBox);
            tileStorageBox.setStorageBoxNetworkManager(this);

            for (Direction d : Direction.values()) {
                BlockPos neighborPos = pos.relative(d);
                if (!network.containsKey(neighborPos)) {
                    createNewNetwork(world, neighborPos, ignorePos);
                }
            }
        }
    }

    public HashMap<BlockPos, BaseStorageBoxBlockEntity> getNetwork() {
        return this.network;
    }

    public void storeInventoryToNetwork(IInventory inventory, BlockPos originPos) {
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof PouchItem) {
                    PouchInventory pouch = new PouchInventory(stack);
                    if (pouch.getIsStorageBox()) {
                        pouch.storeToNetwork(this, originPos);
                    }
                } else {
                    storeToNetwork(stack, false, originPos);
                }
            }
        }
    }

    public boolean storeToNetwork(ItemStack stack, boolean register, BlockPos originPos) {
        List<BaseStorageBoxBlockEntity> matchingList = getMatchingList(stack, originPos);
        for (BaseStorageBoxBlockEntity tile : matchingList)
            if (tile.store(stack))
                return true;

        if (register) {
            List<BlockPos> sortedKeys = getSortedKeys(originPos);
            for (BlockPos key : sortedKeys) {
                BaseStorageBoxBlockEntity tile = network.get(key);
                if (tile.getStorageBoxType() != StorageBoxType.GLASS && !tile.hasContents())
                    if (tile.registerItems(stack))
                        if (tile.store(stack))
                            return true;
            }
        }
        return false;
    }

    public List<BaseStorageBoxBlockEntity> getMatchingList(ItemStack stack, BlockPos originPos) {
        List<BaseStorageBoxBlockEntity> list = new ArrayList<>();
        List<BlockPos> sortedKeys = getSortedKeys(originPos);
        for (BlockPos key : sortedKeys) {
            BaseStorageBoxBlockEntity tile = network.get(key);
            if (tile != null && tile.getContents().getItem() == stack.getItem()) {
                list.add(tile);
            }
        }
        return list;
    }

    //引数のposを原点とした近い順にkeysを並べ替える
    private List<BlockPos> getSortedKeys(BlockPos originPos) {
        List<BlockPos> keys = new ArrayList<>(network.keySet());
        Collections.sort(keys, (p1, p2) -> {
            return p1.distManhattan(originPos) - p2.distManhattan(originPos);
        });
        return keys;
    }

    public int size() {
        return network.size();
    }

    //ネットワークにTileを一つ加える。Tileが既にネットワークを持っていてもそのネットワークは追加されない
    public void add(BaseStorageBoxBlockEntity tile) {
        network.put(tile.getBlockPos(), tile);
        tile.setStorageBoxNetworkManager(this);
    }

    //ネットワークを結合する。
    public void add(StorageBoxNetworkManager newNetwork) {
        this.network.putAll(newNetwork.getNetwork());
        for (BaseStorageBoxBlockEntity tile : newNetwork.getNetwork().values()) {
            tile.setStorageBoxNetworkManager(this);
        }
    }

    public void remove(BlockPos pos) {
        this.network.remove(pos);
    }

}
