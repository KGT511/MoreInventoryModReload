package moreinventory.data;

import java.util.HashSet;
import java.util.Set;

import moreinventory.block.Blocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

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

}
