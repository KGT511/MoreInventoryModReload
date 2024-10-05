package moreinventory.data;

import java.util.HashSet;
import java.util.Set;

import moreinventory.block.Blocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

public class BlockLootTables extends BlockLootSubProvider {
    private final Set<Block> knownBlocks = new HashSet<>();
    private static final Set<Item> EXPLOSION_RESISTANT = Set.of();

    protected BlockLootTables(HolderLookup.Provider provider) {
        super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags(), provider);
    }

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
    protected void generate() {
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
