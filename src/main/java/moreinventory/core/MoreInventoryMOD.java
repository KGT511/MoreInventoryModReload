package moreinventory.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import moreinventory.block.Blocks;
import moreinventory.client.gui.GUICatchall;
import moreinventory.client.gui.GUITransportManager;
import moreinventory.client.renderer.ItemCatchallRenderer;
import moreinventory.client.renderer.StorageBoxRenderer;
import moreinventory.client.renderer.TransportManagerRenderer;
import moreinventory.container.Containers;
import moreinventory.item.ItemTransporter;
import moreinventory.tileentity.TileEntities;
import moreinventory.tileentity.storagebox.StorageBoxInventorySize;
import moreinventory.tileentity.storagebox.TileEntityStorageBoxType;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(MoreInventoryMOD.MOD_ID)
public class MoreInventoryMOD {
    public static final String MOD_ID = "moreinventorymod";
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ItemGroup itemGroup = new ItemGroupMoreInventoryMOD();
    //    public static final SimpleNetworkWrapper network = new SimpleNetworkWrapper(MOD_ID);
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel network = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    public MoreInventoryMOD() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("SETUP START");

        ItemTransporter.setTransportableBlocks();
        StorageBoxInventorySize.init();
        TileEntityStorageBoxType.init();

        LOGGER.info("SETUP END");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //bind renderers and gui factories
        ClientRegistry.bindTileEntityRenderer(TileEntities.CATCHALL_TILE_TYPE, ItemCatchallRenderer::new);
        TileEntityStorageBoxType.map.forEach((key, val) -> {
            ClientRegistry.bindTileEntityRenderer(val, StorageBoxRenderer::new);
        });
        RenderTypeLookup.setRenderLayer(Blocks.STORAGE_BOX_GLASS, RenderType.getTranslucent());
        ClientRegistry.bindTileEntityRenderer(TileEntities.IMPORTER_TILE_TYPE, TransportManagerRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntities.EXPORTER_TILE_TYPE, TransportManagerRenderer::new);

        ScreenManager.registerFactory(Containers.CATCHALL_CONTAINER_TYPE, GUICatchall::new);
        ScreenManager.registerFactory(Containers.TRANSPORT_MANAGER_CONTAINER_TYPE, GUITransportManager::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("server starting");
    }

}