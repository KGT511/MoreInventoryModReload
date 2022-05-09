package moreinventory.recipe;

import moreinventory.core.MoreInventoryMOD;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Recipes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MoreInventoryMOD.MODID);

    public static final RegistryObject<SpecialRecipeSerializer<PouchRecipe>> POUCH_RECIPE = RECIPE.register("pouch_recipe", () -> new SpecialRecipeSerializer<>(PouchRecipe::new));

    public static void register(IEventBus eventBus) {
        RECIPE.register(eventBus);
    }
}
