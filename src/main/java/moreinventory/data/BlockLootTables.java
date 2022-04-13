package moreinventory.data;

import java.util.HashSet;
import java.util.Set;

import moreinventory.block.Blocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class BlockLootTables extends BlockLoot {
    private final Set<Block> knownBlocks = new HashSet<>();

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }

    @Override
    protected void add(Block block, LootTable.Builder builder) {
        super.add(block, builder);
        knownBlocks.add(block);
    }

    @Override
    protected void addTables() {
        dropSelf(Blocks.CATCHALL);
        dropSelf(Blocks.WOOD_STORAGE_BOX);
        dropSelf(Blocks.IRON_STORAGE_BOX);
        dropSelf(Blocks.GOLD_STORAGE_BOX);
        dropSelf(Blocks.DIAMOND_STORAGE_BOX);
        dropSelf(Blocks.EMERALD_STORAGE_BOX);
        dropSelf(Blocks.COPPER_STORAGE_BOX);
        dropSelf(Blocks.TIN_STORAGE_BOX);
        dropSelf(Blocks.BRONZE_STORAGE_BOX);
        dropSelf(Blocks.SILVER_STORAGE_BOX);
        dropSelf(Blocks.GLASS_STORAGE_BOX);
        dropSelf(Blocks.IMPORTER);
        dropSelf(Blocks.EXPORTER);

    }

    private void registerMultiEnderChest(Block origin, Item alter) {
        LootPoolEntryContainer.Builder<?> sticks = applyExplosionDecay(origin, LootItem.lootTableItem(alter).apply(SetItemCountFunction.setCount(ConstantValue.exactly(22))));
        this.add(origin, createSilkTouchDispatchTable(origin, sticks));
    }

}
