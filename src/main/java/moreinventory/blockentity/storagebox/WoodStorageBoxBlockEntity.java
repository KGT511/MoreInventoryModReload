package moreinventory.blockentity.storagebox;

import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WoodStorageBoxBlockEntity extends BaseStorageBoxBlockEntity {

    public WoodStorageBoxBlockEntity(BlockPos pos, BlockState state) {
        super(StorageBoxType.WOOD, pos, state);
    }

}
