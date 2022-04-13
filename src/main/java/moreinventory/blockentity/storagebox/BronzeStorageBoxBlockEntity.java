package moreinventory.blockentity.storagebox;

import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BronzeStorageBoxBlockEntity extends BaseStorageBoxBlockEntity {
    public BronzeStorageBoxBlockEntity(BlockPos pos, BlockState state) {
        super(StorageBoxType.BRONZE, pos, state);
    }
}
