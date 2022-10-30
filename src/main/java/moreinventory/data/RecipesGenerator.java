package moreinventory.data;

import java.util.function.Consumer;

import moreinventory.block.Blocks;
import moreinventory.recipe.Recipes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
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
        ShapedRecipeBuilder.shaped(Blocks.CATCHALL.get())
                .pattern("P P")
                .pattern("PCP")
                .pattern("SSS")
                .define('P', ItemTags.PLANKS)
                .define('C', Items.CHEST)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(consumer);
        ShapedRecipeBuilder.shaped(moreinventory.item.Items.TRANSPORTER.get())
                .pattern("P P")
                .pattern("PSP")
                .pattern("SSS")
                .define('P', ItemTags.PLANKS)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .save(consumer);
        ShapedRecipeBuilder.shaped(Blocks.WOOD_STORAGE_BOX.get())
                .pattern("MSM")
                .pattern("M M")
                .pattern("MSM")
                .define('M', ItemTags.LOGS)
                .define('S', ItemTags.WOODEN_SLABS)
                .unlockedBy("has_logs", has(ItemTags.LOGS))
                .save(consumer);

        registerStorageBoxRecipe(consumer, Blocks.IRON_STORAGE_BOX.get(), Tags.Items.INGOTS_IRON);
        registerStorageBoxRecipe(consumer, Blocks.GOLD_STORAGE_BOX.get(), Tags.Items.INGOTS_GOLD);
        registerStorageBoxRecipe(consumer, Blocks.DIAMOND_STORAGE_BOX.get(), Tags.Items.GEMS_DIAMOND);
        registerStorageBoxRecipe(consumer, Blocks.EMERALD_STORAGE_BOX.get(), Tags.Items.GEMS_EMERALD);

        registerStorageBoxRecipe(consumer, Blocks.COPPER_STORAGE_BOX.get(), Items.COPPER_INGOT);
        //        registerStorageBoxRecipe(consumer, Blocks.TIN_STORAGE_BOX, Items.IRON_INGOT);
        //        registerStorageBoxRecipe(consumer, Blocks.BRONZE_STORAGE_BOX, Items.IRON_INGOT);
        //        registerStorageBoxRecipe(consumer, Blocks.SILVER_STORAGE_BOX, Items.IRON_INGOT);

        ShapedRecipeBuilder.shaped(Blocks.GLASS_STORAGE_BOX.get(), 32)
                .pattern("MSM")
                .pattern("M M")
                .pattern("MSM")
                .define('M', Items.GLASS)
                .define('S', Items.GLASS_PANE)
                .unlockedBy("has_glass", has(Items.GLASS))
                .unlockedBy("has_glass_pane", has(Items.GLASS_PANE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(Blocks.IMPORTER.get())
                .pattern("SSS")
                .pattern("SHS")
                .pattern("SRS")
                .define('S', Items.COBBLESTONE)
                .define('R', Items.REDSTONE)
                .define('H', Items.HOPPER)
                .unlockedBy("has_chest", has(Items.CHEST))
                .unlockedBy("has_storage_box", has(Blocks.WOOD_STORAGE_BOX.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(Blocks.EXPORTER.get())
                .pattern("SRS")
                .pattern("SHS")
                .pattern("SSS")
                .define('S', Items.COBBLESTONE)
                .define('R', Items.REDSTONE)
                .define('H', Items.HOPPER)
                .unlockedBy("has_chest", has(Items.CHEST))
                .unlockedBy("has_storage_box", has(Blocks.WOOD_STORAGE_BOX.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(moreinventory.item.Items.SPANNER.get())
                .pattern("SSS")
                .pattern(" I ")
                .pattern("SSS")
                .define('S', Items.STONE)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_stone", has(Items.STONE))
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(moreinventory.item.Items.LEATHER_PACK.get())
                .pattern("LLL")
                .pattern("LSL")
                .pattern("LLL")
                .define('S', Items.STRING)
                .define('L', Items.LEATHER)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(consumer);
        ShapedRecipeBuilder.shaped(moreinventory.item.Items.POUCH.get())
                .pattern("LLL")
                .pattern("PDP")
                .pattern("LPL")
                .define('D', Items.DIAMOND)
                .define('L', Items.LEATHER)
                .define('P', moreinventory.item.Items.LEATHER_PACK.get())
                .unlockedBy("has_leather", has(Items.LEATHER))
                .unlockedBy("has_leather_pack", has(moreinventory.item.Items.LEATHER_PACK.get()))
                .save(consumer);

        SpecialRecipeBuilder.special(Recipes.POUCH_RECIPE.get())
                .save(consumer, Recipes.POUCH_RECIPE.getId().getPath());
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
                .define('W', Blocks.WOOD_STORAGE_BOX.get())
                .unlockedBy("has_storage_box", has(Blocks.WOOD_STORAGE_BOX.get()))
                .save(consumer);
    }
}
