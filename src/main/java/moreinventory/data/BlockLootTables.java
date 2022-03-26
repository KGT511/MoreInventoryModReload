package moreinventory.data;

import java.util.HashSet;
import java.util.Set;

import moreinventory.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.loot.LootTable;

public class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {
    private final Set<Block> knownBlocks = new HashSet<>();

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }

    @Override
    protected void registerLootTable(Block block, LootTable.Builder builder) {
        super.registerLootTable(block, builder);
        knownBlocks.add(block);
    }

    @Override
    protected void addTables() {
        registerDropSelfLootTable(Blocks.CATCHALL);
        registerDropSelfLootTable(Blocks.WOOD_STORAGE_BOX);
        registerDropSelfLootTable(Blocks.IRON_STORAGE_BOX);
        registerDropSelfLootTable(Blocks.GOLD_STORAGE_BOX);
        registerDropSelfLootTable(Blocks.DIAMOND_STORAGE_BOX);
        registerDropSelfLootTable(Blocks.EMERALD_STORAGE_BOX);
        registerDropSelfLootTable(Blocks.COPPER_STORAGE_BOX);
        registerDropSelfLootTable(Blocks.TIN_STORAGE_BOX);
        registerDropSelfLootTable(Blocks.BRONZE_STORAGE_BOX);
        registerDropSelfLootTable(Blocks.SILVER_STORAGE_BOX);
        registerDropSelfLootTable(Blocks.GLASS_STORAGE_BOX);
        registerDropSelfLootTable(Blocks.IMPORTER);
        registerDropSelfLootTable(Blocks.EXPORTER);

    }

}
