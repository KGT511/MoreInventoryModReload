package moreinventory.tileentity.storagebox;

import moreinventory.tileentity.BaseStorageBoxTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GlassStorageBoxTileEntity extends BaseStorageBoxTileEntity {

    public GlassStorageBoxTileEntity() {
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
