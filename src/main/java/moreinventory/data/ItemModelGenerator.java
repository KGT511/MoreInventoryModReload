package moreinventory.data;

import java.util.Locale;

import moreinventory.block.Blocks;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.item.Items;
import moreinventory.item.PouchItem;
import moreinventory.item.TransporterItem;
import moreinventory.storagebox.StorageBox;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MoreInventoryMOD.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "MoreInventoryMOD item and itemblock models";
    }

    @Override
    protected void registerModels() {
        toBlock(Blocks.CATCHALL.get());
        for (StorageBox val : StorageBox.storageBoxMap.values()) {
            toBlock(val.block);
        }
        registerTransporter();
        singleTexTool(Blocks.IMPORTER.get());
        singleTexTool(Blocks.EXPORTER.get());
        singleTexTool(Items.SPANNER.get());
        singleTexTool(Items.POUCH.get());
        registerPouch();
        registerPlating();
    }

    public static String name(Block block) {
        return BlockStateGenerator.name(block);
    }

    public static String name(Item item) {
        return item.getRegistryName().getPath();
    }

    private void toBlock(Block b) {
        toBlockModel(b, name(b));
    }

    private void toBlockModel(Block b, String model) {
        toBlockModel(b, prefix("block/" + model));
    }

    private void toBlockModel(Block b, ResourceLocation model) {
        withExistingParent(name(b), model);
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MoreInventoryMOD.MOD_ID, name.toLowerCase(Locale.ROOT));
    }

    private ItemModelBuilder singleTexTool(Block item) {
        return tool(name(item), prefix("item/" + name(item)));
    }

    private ItemModelBuilder singleTexTool(Item item) {
        return tool(name(item), prefix("item/" + name(item)));
    }

    private ItemModelBuilder tool(String name, ResourceLocation... layers) {
        ItemModelBuilder builder = withExistingParent(name, "item/generated");
        for (int i = 0; i < layers.length; ++i) {
            builder = builder.texture("layer" + i, layers[i]);
        }
        return builder;
    }

    public void registerTransporter() {
        for (int i = 0; i < 30; ++i) {
            String name = "transporter_mod_" + String.valueOf(i);
            tool(name, prefix("item/" + name));
        }
        for (Block transportableBlock : TransporterItem.transportableBlocks) {
            String name = "transporter_" + BlockStateGenerator.name(transportableBlock);
            tool(name, prefix("item/" + name));
        }
        String litName = "transporter_furnace_lit";
        tool(litName, prefix("item/" + litName));

        String name = "item/" + name(Items.TRANSPORTER.get());
        ItemModelBuilder builder = tool(name(Items.TRANSPORTER.get()), prefix(name));
        ResourceLocation predicateName = new ResourceLocation("custom_model_data");
        for (Block transportableBlock : TransporterItem.transportableBlocks) {
            String transportableBlockName = BlockStateGenerator.name(transportableBlock);
            int num = TransporterItem.transportableBlocks.indexOf(transportableBlock) + 1;
            builder.override().predicate(predicateName, num).model(getBuilder(name + "_" + transportableBlockName));
        }

        builder.override().predicate(predicateName, 99).model(getBuilder(name + "_furnace_lit"));
    }

    public void registerPouch() {
        for (DyeColor color : DyeColor.values()) {
            singleTexTool(PouchItem.byColor(color));
        }
        singleTexTool(Items.POUCH.get());
        singleTexTool(Items.LEATHER_PACK.get());
    }

    public void registerPlating() {
        singleTexTool(Items.BRUSH.get());
        singleTexTool(Items.IRON_PLATING.get());
        singleTexTool(Items.GOLD_PLATING.get());
        singleTexTool(Items.DIAMOND_PLATING.get());
        singleTexTool(Items.EMERALD_PLATING.get());
        singleTexTool(Items.COPPER_PLATING.get());
        singleTexTool(Items.TIN_PLATING.get());
        singleTexTool(Items.BRONZE_PLATING.get());
        singleTexTool(Items.SILVER_PLATING.get());
        singleTexTool(Items.STEEL_PLATING.get());
    }
}
