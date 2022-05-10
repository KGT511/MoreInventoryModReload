package moreinventory.recipe;

import moreinventory.core.MoreInventoryMOD;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Recipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MoreInventoryMOD.MOD_ID);

    public static final RegistryObject<SimpleRecipeSerializer<PouchRecipe>> POUCH_RECIPE = RECIPE.register("pouch_recipe", () -> new SimpleRecipeSerializer<>(PouchRecipe::new));

    public static void register(IEventBus eventBus) {
        RECIPE.register(eventBus);
    }
}
