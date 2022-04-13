package moreinventory.data;

import java.util.function.Consumer;

import moreinventory.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

public class RecipesGenerator extends RecipeProvider {
    public RecipesGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(Blocks.CATCHALL)
                .pattern("P P")
                .pattern("PCP")
                .pattern("SSS")
                .define('P', ItemTags.PLANKS)
                .define('C', Items.CHEST)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(consumer);
        ShapedRecipeBuilder.shaped(moreinventory.item.Items.TRANSPORTER)
                .pattern("P P")
                .pattern("PSP")
                .pattern("SSS")
                .define('P', ItemTags.PLANKS)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(consumer);
        ShapedRecipeBuilder.shaped(Blocks.WOOD_STORAGE_BOX)
                .pattern("MSM")
                .pattern("M M")
                .pattern("MSM")
                .define('M', ItemTags.LOGS)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_logs", has(ItemTags.LOGS))
                .save(consumer);

        registerStorageBoxRecipe(consumer, Blocks.IRON_STORAGE_BOX, Tags.Items.INGOTS_IRON);
        registerStorageBoxRecipe(consumer, Blocks.GOLD_STORAGE_BOX, Tags.Items.INGOTS_GOLD);
        registerStorageBoxRecipe(consumer, Blocks.DIAMOND_STORAGE_BOX, Tags.Items.GEMS_DIAMOND);
        registerStorageBoxRecipe(consumer, Blocks.EMERALD_STORAGE_BOX, Tags.Items.GEMS_EMERALD);

        registerStorageBoxRecipe(consumer, Blocks.COPPER_STORAGE_BOX, Items.COPPER_INGOT);
        //        registerStorageBoxRecipe(consumer, Blocks.TIN_STORAGE_BOX, Items.IRON_INGOT);
        //        registerStorageBoxRecipe(consumer, Blocks.BRONZE_STORAGE_BOX, Items.IRON_INGOT);
        //        registerStorageBoxRecipe(consumer, Blocks.SILVER_STORAGE_BOX, Items.IRON_INGOT);

        ShapedRecipeBuilder.shaped(Blocks.GLASS_STORAGE_BOX, 32)
                .pattern("MSM")
                .pattern("M M")
                .pattern("MSM")
                .define('M', Items.GLASS)
                .define('S', Items.GLASS_PANE)
                .unlockedBy("has_glass", has(Items.GLASS))
                .unlockedBy("has_glass_pane", has(Items.GLASS_PANE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Blocks.IMPORTER)
                .pattern("SSS")
                .pattern("SHS")
                .pattern("SRS")
                .define('S', Items.COBBLESTONE)
                .define('R', Items.REDSTONE)
                .define('H', Items.HOPPER)
                .unlockedBy("has_chest", has(Items.CHEST))
                .unlockedBy("has_storage_box", has(moreinventory.item.Items.WOOD_STORAGE_BOX))
                .save(consumer);
        ShapedRecipeBuilder.shaped(Blocks.EXPORTER)
                .pattern("SRS")
                .pattern("SHS")
                .pattern("SSS")
                .define('S', Items.COBBLESTONE)
                .define('R', Items.REDSTONE)
                .define('H', Items.HOPPER)
                .unlockedBy("has_chest", has(Items.CHEST))
                .unlockedBy("has_storage_box", has(moreinventory.item.Items.WOOD_STORAGE_BOX))
                .save(consumer);

        ShapedRecipeBuilder.shaped(moreinventory.item.Items.SPANNER)
                .pattern("SSS")
                .pattern(" I ")
                .pattern("SSS")
                .define('S', Items.STONE)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_stone", has(Items.STONE))
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
    }

    private void registerStorageBoxRecipe(Consumer<FinishedRecipe> consumer, Block block, Tag<Item> material) {
        registerStorageBoxRecipe(consumer, block, Ingredient.of(material));
    }

    private void registerStorageBoxRecipe(Consumer<FinishedRecipe> consumer, Block block, ItemLike material) {
        registerStorageBoxRecipe(consumer, block, Ingredient.of(material));
    }

    private void registerStorageBoxRecipe(Consumer<FinishedRecipe> consumer, Block block, Ingredient material) {
        ShapedRecipeBuilder.shaped(block, 3)
                .pattern("MSM")
                .pattern("MWM")
                .pattern("MSM")
                .define('M', material)
                .define('S', ItemTags.WOODEN_SLABS)
                .define('W', moreinventory.item.Items.WOOD_STORAGE_BOX)
                .unlockedBy("has_storage_box", has(moreinventory.item.Items.WOOD_STORAGE_BOX))
                .save(consumer);
    }
}
