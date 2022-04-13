package moreinventory.blockentity.storagebox;

import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SilverStorageBoxBlockEntity extends BaseStorageBoxBlockEntity {
    public SilverStorageBoxBlockEntity(BlockPos pos, BlockState state) {
        super(StorageBoxType.SILVER, pos, state);
    }
}
