package moreinventory.blockentity.storagebox;

import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import moreinventory.storagebox.StorageBoxType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GoldStorageBoxBlockEntity extends BaseStorageBoxBlockEntity {
    public GoldStorageBoxBlockEntity(BlockPos pos, BlockState state) {
        super(StorageBoxType.GOLD, pos, state);
    }
}
