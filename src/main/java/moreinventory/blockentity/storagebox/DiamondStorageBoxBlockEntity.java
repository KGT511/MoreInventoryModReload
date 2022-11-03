package moreinventory.blockentity.storagebox;

import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import moreinventory.storagebox.StorageBoxType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DiamondStorageBoxBlockEntity extends BaseStorageBoxBlockEntity {
    public DiamondStorageBoxBlockEntity(BlockPos pos, BlockState state) {
        super(StorageBoxType.DIAMOND, pos, state);
    }
}
