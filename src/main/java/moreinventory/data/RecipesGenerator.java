package moreinventory.data;

import java.util.function.Consumer;

import moreinventory.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class RecipesGenerator extends RecipeProvider {
    public RecipesGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(Blocks.CATCHALL)
                .patternLine("P P")
                .patternLine("PCP")
                .patternLine("SSS")
                .key('P', ItemTags.PLANKS)
                .key('C', Items.CHEST)
                .key('S', ItemTags.WOODEN_SLABS)
                .addCriterion("has_chest", hasItem(Items.CHEST))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(moreinventory.item.Items.TRANSPORTER)
                .patternLine("P P")
                .patternLine("PSP")
                .patternLine("SSS")
                .key('P', ItemTags.PLANKS)
                .key('S', ItemTags.WOODEN_SLABS)
                .addCriterion("has_planks", hasItem(ItemTags.PLANKS))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Blocks.WOOD_STORAGE_BOX)
                .patternLine("MSM")
                .patternLine("M M")
                .patternLine("MSM")
                .key('M', ItemTags.LOGS)
                .key('S', ItemTags.WOODEN_SLABS)
                .addCriterion("has_logs", hasItem(ItemTags.LOGS))
                .build(consumer);

        registerStorageBoxRecipe(consumer, Blocks.IRON_STORAGE_BOX, Tags.Items.INGOTS_IRON);
        registerStorageBoxRecipe(consumer, Blocks.GOLD_STORAGE_BOX, Tags.Items.INGOTS_GOLD);
        registerStorageBoxRecipe(consumer, Blocks.DIAMOND_STORAGE_BOX, Tags.Items.GEMS_DIAMOND);
        registerStorageBoxRecipe(consumer, Blocks.EMERALD_STORAGE_BOX, Tags.Items.GEMS_EMERALD);

        //        registerStorageBoxRecipe(consumer, Blocks.COPPER_STORAGE_BOX, Items.COPPER_INGOT);
        //        registerStorageBoxRecipe(consumer, Blocks.TIN_STORAGE_BOX, Items.IRON_INGOT);
        //        registerStorageBoxRecipe(consumer, Blocks.BRONZE_STORAGE_BOX, Items.IRON_INGOT);
        //        registerStorageBoxRecipe(consumer, Blocks.SILVER_STORAGE_BOX, Items.IRON_INGOT);

        ShapedRecipeBuilder.shapedRecipe(Blocks.GLASS_STORAGE_BOX, 32)
                .patternLine("MSM")
                .patternLine("M M")
                .patternLine("MSM")
                .key('M', Items.GLASS)
                .key('S', Items.GLASS_PANE)
                .addCriterion("has_glass", hasItem(Items.GLASS))
                .addCriterion("has_glass_pane", hasItem(Items.GLASS_PANE))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(Blocks.IMPORTER)
                .patternLine("SSS")
                .patternLine("SHS")
                .patternLine("SRS")
                .key('S', Items.COBBLESTONE)
                .key('R', Items.REDSTONE)
                .key('H', Items.HOPPER)
                .addCriterion("has_chest", hasItem(Items.CHEST))
                .addCriterion("has_storage_box", hasItem(moreinventory.item.Items.WOOD_STORAGE_BOX))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Blocks.EXPORTER)
                .patternLine("SRS")
                .patternLine("SHS")
                .patternLine("SSS")
                .key('S', Items.COBBLESTONE)
                .key('R', Items.REDSTONE)
                .key('H', Items.HOPPER)
                .addCriterion("has_chest", hasItem(Items.CHEST))
                .addCriterion("has_storage_box", hasItem(moreinventory.item.Items.WOOD_STORAGE_BOX))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(moreinventory.item.Items.SPANNER)
                .patternLine("SSS")
                .patternLine(" I ")
                .patternLine("SSS")
                .key('S', Items.STONE)
                .key('I', Items.IRON_INGOT)
                .addCriterion("has_stone", hasItem(Items.STONE))
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);
    }

    private void registerStorageBoxRecipe(Consumer<IFinishedRecipe> consumer, Block block, IOptionalNamedTag<Item> ingotsIron) {
        registerStorageBoxRecipe(consumer, block, Ingredient.fromTag(ingotsIron));
    }

    private void registerStorageBoxRecipe(Consumer<IFinishedRecipe> consumer, Block block, Ingredient material) {
        ShapedRecipeBuilder.shapedRecipe(block, 3)
                .patternLine("MSM")
                .patternLine("MWM")
                .patternLine("MSM")
                .key('M', material)
                .key('S', ItemTags.WOODEN_SLABS)
                .key('W', moreinventory.item.Items.WOOD_STORAGE_BOX)
                .addCriterion("has_storage_box", hasItem(moreinventory.item.Items.WOOD_STORAGE_BOX))
                .build(consumer);
    }
}
