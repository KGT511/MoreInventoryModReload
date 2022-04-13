package moreinventory.blockentity.storagebox;

import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TinStorageBoxBlockEntity extends BaseStorageBoxBlockEntity {
    public TinStorageBoxBlockEntity(BlockPos pos, BlockState state) {
        super(StorageBoxType.TIN, pos, state);
    }
}
