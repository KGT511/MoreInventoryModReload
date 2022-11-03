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
    protected void add(Block block, LootTable.Builder builder) {
        super.add(block, builder);
        knownBlocks.add(block);
    }

    @Override
    protected void addTables() {
        dropSelf(Blocks.CATCHALL.get());
        dropSelf(Blocks.WOOD_STORAGE_BOX.get());
        dropSelf(Blocks.IRON_STORAGE_BOX.get());
        dropSelf(Blocks.GOLD_STORAGE_BOX.get());
        dropSelf(Blocks.DIAMOND_STORAGE_BOX.get());
        dropSelf(Blocks.EMERALD_STORAGE_BOX.get());
        dropSelf(Blocks.COPPER_STORAGE_BOX.get());
        dropSelf(Blocks.TIN_STORAGE_BOX.get());
        dropSelf(Blocks.BRONZE_STORAGE_BOX.get());
        dropSelf(Blocks.SILVER_STORAGE_BOX.get());
        dropSelf(Blocks.GLASS_STORAGE_BOX.get());
        dropSelf(Blocks.IMPORTER.get());
        dropSelf(Blocks.EXPORTER.get());

    }

}
