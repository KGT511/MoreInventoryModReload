package moreinventory.blockentity.storagebox;

import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import moreinventory.storagebox.StorageBoxType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GlassStorageBoxBlockEntity extends BaseStorageBoxBlockEntity {

    public GlassStorageBoxBlockEntity() {
        super(StorageBoxType.GLASS);
    }

    @Override
    public boolean rightClickEvent(World world, PlayerEntity player) {
        switch (++clickCount) {
        case 1:
            clickTime = 16;
            return false;

        case 2:
            return false;

        case 3:
            clickCount = 0;

            getStorageBoxNetworkManager().storeInventoryToNetwork(player.inventory, this.worldPosition);
            player.tick();
            break;
        default:
            clickCount = 0;
            break;
        }

        return true;
    }

    @Override
    public boolean registerItems(ItemStack stack) {
        return false;
    }
}
