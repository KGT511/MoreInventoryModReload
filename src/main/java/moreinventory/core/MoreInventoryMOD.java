package moreinventory.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import moreinventory.block.Blocks;
import moreinventory.blockentity.BlockEntities;
import moreinventory.blockentity.storagebox.StorageBoxInventorySize;
import moreinventory.blockentity.storagebox.StorageBoxTypeBlockEntity;
import moreinventory.client.model.ModelLayers;
import moreinventory.client.renderer.CatchallRenderer;
import moreinventory.client.renderer.StorageBoxRenderer;
import moreinventory.client.renderer.TransportRenderer;
import moreinventory.client.screen.CatchallContainerScreen;
import moreinventory.client.screen.PouchContainerScreen;
import moreinventory.client.screen.TransportContainerScreen;
import moreinventory.container.Containers;
import moreinventory.item.TransporterItem;
import moreinventory.network.ServerboundImporterUpdatePacket;
import moreinventory.network.ServerboundPouchUpdatePacket;
import moreinventory.recipe.Recipes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import net.minecraftforge.fmlserverevents.FMLServerStartingEvent;

@Mod(MoreInventoryMOD.MOD_ID)
public class MoreInventoryMOD {
    public static final String MOD_ID = "moreinventorymod";
    private static final Logger LOGGER = LogManager.getLogger();
    public static final CreativeModeTab creativeModeTab = new MoreInventoryMODCreativeModeTab();
    //    public static final SimpleNetworkWrapper network = new SimpleNetworkWrapper(MOD_ID);
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(MOD_ID, "main"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    public MoreInventoryMOD() {
        initNetwork();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Recipes.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("SETUP START");
        MoreInventoryMOD.init();

        LOGGER.info("SETUP END");
    }

    public static void init() {
        StorageBoxInventorySize.init();
        StorageBoxTypeBlockEntity.init();
        TransporterItem.setTransportableBlocks();
    }

    public static void initNetwork() {
        var id = 0;
        CHANNEL.messageBuilder(ServerboundImporterUpdatePacket.class, id++)
                .encoder(ServerboundImporterUpdatePacket::encode)
                .decoder(ServerboundImporterUpdatePacket::decode)
                .consumer(ServerboundImporterUpdatePacket::handle)
                .add();
        CHANNEL.messageBuilder(ServerboundPouchUpdatePacket.class, id++)
                .encoder(ServerboundPouchUpdatePacket::encode)
                .decoder(ServerboundPouchUpdatePacket::decode)
                .consumer(ServerboundPouchUpdatePacket::handle)
                .add();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //bind renderers and gui factories
        BlockEntityRenderers.register(BlockEntities.CATCHALL_BLOCK_ENTITY_TYPE, CatchallRenderer::new);
        StorageBoxTypeBlockEntity.map.forEach((key, val) -> {
            BlockEntityRenderers.register(val, StorageBoxRenderer::new);
        });
        ItemBlockRenderTypes.setRenderLayer(Blocks.GLASS_STORAGE_BOX, RenderType.translucent());
        BlockEntityRenderers.register(BlockEntities.IMPORTER_BLOCK_ENTITY_TYPE, TransportRenderer::new);
        BlockEntityRenderers.register(BlockEntities.EXPORTER_BLOCK_ENTITY_TYPE, TransportRenderer::new);

        MenuScreens.register(Containers.CATCHALL_CONTAINER_TYPE, CatchallContainerScreen::new);
        MenuScreens.register(Containers.TRANSPORT_CONTAINER_TYPE, TransportContainerScreen::new);
        MenuScreens.register(Containers.POUCH_CONTAINER_TYPE, PouchContainerScreen::new);

        ForgeHooksClient.registerLayerDefinition(ModelLayers.TRANPORT, TransportRenderer::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {

    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        LOGGER.info("server starting");
    }

}