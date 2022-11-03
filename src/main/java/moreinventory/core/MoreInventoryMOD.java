package moreinventory.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import moreinventory.block.Blocks;
import moreinventory.blockentity.BlockEntities;
import moreinventory.client.renderer.CatchallRenderer;
import moreinventory.client.renderer.StorageBoxRenderer;
import moreinventory.client.renderer.TransportRenderer;
import moreinventory.client.screen.CatchallContainerScreen;
import moreinventory.client.screen.PouchContainerScreen;
import moreinventory.client.screen.TransportContainerScreen;
import moreinventory.container.Containers;
import moreinventory.item.Items;
import moreinventory.item.TransporterItem;
import moreinventory.network.ServerboundImporterUpdatePacket;
import moreinventory.network.ServerboundPouchUpdatePacket;
import moreinventory.recipe.Recipes;
import moreinventory.storagebox.StorageBox;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
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
    public static final ItemGroup itemGroup = new MoreInventoryMODItemGroup();
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
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::enqueueIMC);
        eventBus.addListener(this::processIMC);
        eventBus.addListener(this::doClientStuff);

        Items.register(eventBus);
        Blocks.register(eventBus);
        BlockEntities.register(eventBus);

        Recipes.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("SETUP START");

        init();

        LOGGER.info("SETUP END");
    }

    public static void init() {
        StorageBox.init();
        TransporterItem.setTransportableBlocks();
    }

    public static void initNetwork() {
        int id = 0;
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

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //bind renderers and gui factories
        ClientRegistry.bindTileEntityRenderer(BlockEntities.CATCHALL_BLOCK_ENTITY_TYPE.get(), CatchallRenderer::new);
        StorageBox.storageBoxMap.forEach((key, val) -> {
            ClientRegistry.bindTileEntityRenderer(val.blockEntity, StorageBoxRenderer::new);
        });
        RenderTypeLookup.setRenderLayer(Blocks.GLASS_STORAGE_BOX.get(), RenderType.translucent());
        ClientRegistry.bindTileEntityRenderer(BlockEntities.IMPORTER_BLOCK_ENTITY_TYPE.get(), TransportRenderer::new);
        ClientRegistry.bindTileEntityRenderer(BlockEntities.EXPORTER_BLOCK_ENTITY_TYPE.get(), TransportRenderer::new);

        ScreenManager.register(Containers.CATCHALL_CONTAINER_TYPE, CatchallContainerScreen::new);
        ScreenManager.register(Containers.TRANSPORT_MANAGER_CONTAINER_TYPE, TransportContainerScreen::new);
        ScreenManager.register(Containers.POUCH_CONTAINER_TYPE, PouchContainerScreen::new);
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