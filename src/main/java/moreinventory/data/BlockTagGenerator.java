package moreinventory.data;

import moreinventory.core.MoreInventoryMOD;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagGenerator extends BlockTagsProvider {
    public BlockTagGenerator(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, MoreInventoryMOD.MOD_ID, helper);
    }

    @Override
    protected void addTags() {

    }
}