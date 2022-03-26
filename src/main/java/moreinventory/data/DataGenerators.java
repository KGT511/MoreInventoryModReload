package moreinventory.data;

import moreinventory.core.MoreInventoryMOD;
import moreinventory.data.lang.EnUsLanguageGenerator;
import moreinventory.data.lang.JaJpLanguageGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        init();

        DataGenerator generator = event.getGenerator();

        if (event.includeServer()) {
            generator.addProvider(new RecipesGenerator(generator));
            generator.addProvider(new LootTablesGenerator(generator));
            generator.addProvider(new BlockTagGenerator(generator, event.getExistingFileHelper()));
        }

        if (event.includeClient()) {
            generator.addProvider(new BlockStateGenerator(generator, event.getExistingFileHelper()));
            generator.addProvider(new ItemModelGenerator(generator, event.getExistingFileHelper()));
            generator.addProvider(new EnUsLanguageGenerator(generator, MoreInventoryMOD.MODID));
            generator.addProvider(new JaJpLanguageGenerator(generator, MoreInventoryMOD.MODID));
        }

    }

    private static void init() {
        MoreInventoryMOD.init();
    }
}
