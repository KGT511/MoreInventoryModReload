package moreinventory.data.lang;

import moreinventory.block.Blocks;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.item.Items;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class EnUsLanguageGenerator extends LanguageProvider {
    public EnUsLanguageGenerator(DataGenerator generator, String modid) {
        super(generator, modid, "en_us");
    }

    @Override
    public String getName() {
        return MoreInventoryMOD.MOD_ID + " " + super.getName();
    }

    @Override
    protected void addTranslations() {
        add(MoreInventoryMOD.creativeModeTab.getDisplayName().getString(), "MoreInventoryMOD");

        add(Blocks.CATCHALL, "catchall");
        add(Items.TRANSPORTER, "transporter");
        add(Items.SPANNER, "spanner");

        add(Blocks.WOOD_STORAGE_BOX, "Wood Container Box");
        add(Blocks.IRON_STORAGE_BOX, "Iron Container Box");
        add(Blocks.GOLD_STORAGE_BOX, "Gold Container Box");
        add(Blocks.DIAMOND_STORAGE_BOX, "Diamond Container Box");
        add(Blocks.EMERALD_STORAGE_BOX, "Emerald Container Box");
        add(Blocks.COPPER_STORAGE_BOX, "Copper Container Box");
        add(Blocks.TIN_STORAGE_BOX, "Tin Container Box");
        add(Blocks.BRONZE_STORAGE_BOX, "Bronze Container Box");
        add(Blocks.SILVER_STORAGE_BOX, "Silver Container Box");
        add(Blocks.GLASS_STORAGE_BOX, "Glass Container Box");

        add(Blocks.IMPORTER, "Importer");
        add(Blocks.EXPORTER, "Exporter");
        add(Text.importerRegister, "Register");
        add(Text.importerRegisterOn, "ON");
        add(Text.importerRegisterOff, "OFF");
        add(Text.importerRegisterOnDetail, "Transport register items to empty Container Box if items didn't register");
        add(Text.importerRegisterOffDetail, "Transport items that already registered to Container Box");
        add(Text.importerMove, "Move");
        add(Text.importerMoveWhite, "white");
        add(Text.importerMoveBlack, "black");
        add(Text.importerMoveWhiteDetail, "List is White List");
        add(Text.importerMoveBlackDetail, "List is Black List");

    }
}
