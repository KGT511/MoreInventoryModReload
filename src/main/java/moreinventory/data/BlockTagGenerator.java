package moreinventory.data;

import moreinventory.block.Blocks;
import moreinventory.blockentity.storagebox.StorageBoxTypeBlockEntity;
import moreinventory.core.MoreInventoryMOD;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagGenerator extends BlockTagsProvider {
    public BlockTagGenerator(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, MoreInventoryMOD.MOD_ID, helper);
    }

    @Override
    protected void addTags() {
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(Blocks.CATCHALL);

        for (var storageBox : StorageBoxTypeBlockEntity.blockMap.values()) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(storageBox);
        }
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Blocks.IMPORTER, Blocks.EXPORTER);
    }

}
