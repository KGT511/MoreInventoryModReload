package moreinventory.blockentity.storagebox;

import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EmeraldStorageBoxBlockEntity extends BaseStorageBoxBlockEntity {
    public EmeraldStorageBoxBlockEntity(BlockPos pos, BlockState state) {
        super(StorageBoxType.EMERALD, pos, state);
    }
}
