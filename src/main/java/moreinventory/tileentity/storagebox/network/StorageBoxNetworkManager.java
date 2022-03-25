package moreinventory.tileentity.storagebox.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import moreinventory.tileentity.BaseTileEntityStorageBox;
import moreinventory.tileentity.storagebox.StorageBoxType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StorageBoxNetworkManager {

    private HashMap<BlockPos, BaseTileEntityStorageBox> network = new HashMap<>();

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

        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof IStorageBoxNetwork && tile instanceof BaseTileEntityStorageBox) {
            BaseTileEntityStorageBox tileStorageBox = (BaseTileEntityStorageBox) tile;
            network.put(tileStorageBox.getPos(), tileStorageBox);
            tileStorageBox.setStorageBoxNetworkManager(this);

            for (Direction d : Direction.values()) {
                BlockPos neighborPos = pos.offset(d);
                if (!network.containsKey(neighborPos)) {
                    createNewNetwork(world, neighborPos, ignorePos);
                }
            }
        }
    }

    public HashMap<BlockPos, BaseTileEntityStorageBox> getNetwork() {
        return this.network;
    }

    public void storeInventoryToNetwork(IInventory inventory, BlockPos originPos) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.getItem() != ItemStack.EMPTY.getItem()) {
                storeToNetwork(stack, false, originPos);
            }
        }
    }

    public boolean storeToNetwork(ItemStack stack, boolean register, BlockPos originPos) {
        List<BaseTileEntityStorageBox> matchingList = getMatchingList(stack, originPos);
        for (BaseTileEntityStorageBox tile : matchingList)
            if (tile.store(stack))
                return true;

        if (register) {
            List<BlockPos> sortedKeys = getSortedKeys(originPos);
            for (BlockPos key : sortedKeys) {
                BaseTileEntityStorageBox tile = network.get(key);
                if (tile.getStorageBoxType() != StorageBoxType.GLASS && !tile.hasContents())
                    if (tile.registerItems(stack))
                        if (tile.store(stack))
                            return true;
            }
        }
        return false;
    }

    public List<BaseTileEntityStorageBox> getMatchingList(ItemStack stack, BlockPos originPos) {
        List<BaseTileEntityStorageBox> list = new ArrayList<>();
        List<BlockPos> sortedKeys = getSortedKeys(originPos);
        for (BlockPos key : sortedKeys) {
            BaseTileEntityStorageBox tile = network.get(key);
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
            return p1.manhattanDistance(originPos) - p2.manhattanDistance(originPos);
        });
        return keys;
    }

    public int size() {
        return network.size();
    }

    //ネットワークにTileを一つ加える。Tileが既にネットワークを持っていてもそのネットワークは追加されない
    public void add(BaseTileEntityStorageBox tile) {
        network.put(tile.getPos(), tile);
        tile.setStorageBoxNetworkManager(this);
    }

    //ネットワークを結合する。
    public void add(StorageBoxNetworkManager newNetwork) {
        this.network.putAll(newNetwork.getNetwork());
        for (BaseTileEntityStorageBox tile : newNetwork.getNetwork().values()) {
            tile.setStorageBoxNetworkManager(this);
        }
    }

    public void remove(BlockPos pos) {
        this.network.remove(pos);
    }

}
