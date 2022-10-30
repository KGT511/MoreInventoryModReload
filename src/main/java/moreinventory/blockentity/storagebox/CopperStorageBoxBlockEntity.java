package moreinventory.blockentity.storagebox;

import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import moreinventory.storagebox.StorageBoxType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CopperStorageBoxBlockEntity extends BaseStorageBoxBlockEntity {
    public CopperStorageBoxBlockEntity(BlockPos pos, BlockState state) {
        super(StorageBoxType.COPPER, pos, state);
    }
}
