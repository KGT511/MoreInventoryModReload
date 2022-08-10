package moreinventory.data;

import moreinventory.core.MoreInventoryMOD;
import moreinventory.data.lang.EnUsLanguageGenerator;
import moreinventory.data.lang.JaJpLanguageGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        init();

        DataGenerator generator = event.getGenerator();

        generator.addProvider(event.includeServer(), new BlockStateGenerator(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ItemModelGenerator(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new BlockTagGenerator(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new LootTablesGenerator(generator));
        generator.addProvider(event.includeServer(), new RecipesGenerator(generator));
        generator.addProvider(event.includeServer(), new EnUsLanguageGenerator(generator, MoreInventoryMOD.MOD_ID));
        generator.addProvider(event.includeServer(), new JaJpLanguageGenerator(generator, MoreInventoryMOD.MOD_ID));

    }

    private static void init() {
        MoreInventoryMOD.init();
    }
}
