package moreinventory.recipe;

import java.util.function.Supplier;

import moreinventory.core.MoreInventoryMOD;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Recipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MoreInventoryMOD.MOD_ID);

    public static final RegistryObject<RecipeSerializer<PouchRecipe>> POUCH_RECIPE = RECIPE.register("pouch_recipe", () -> new SimpleCraftingRecipeSerializer<>(PouchRecipe::new));

    public static void register(IEventBus eventBus) {
        RECIPE.register(eventBus);
    }

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> RegistryObject<S> register(String name, Supplier<S> recipe) {
        return RECIPE.register(name, recipe);
    }
}