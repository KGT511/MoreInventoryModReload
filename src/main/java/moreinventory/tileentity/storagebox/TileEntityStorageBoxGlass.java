package moreinventory.tileentity.storagebox;

import moreinventory.tileentity.BaseTileEntityStorageBox;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TileEntityStorageBoxGlass extends BaseTileEntityStorageBox {

    public TileEntityStorageBoxGlass() {
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

            getStorageBoxNetworkManager().storeInventoryToNetwork(player.inventory, this.pos);
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
