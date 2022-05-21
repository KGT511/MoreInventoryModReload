package moreinventory.container;

import moreinventory.inventory.PouchInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;

public class PouchContainerProvider implements INamedContainerProvider {
    private ITextComponent name;

    public PouchContainerProvider() {
    }

    @Override
    public Container createMenu(int windowID, PlayerInventory inventory, PlayerEntity player) {
        this.name = player.getMainHandItem().getHoverName();
        return new PouchContainer(windowID, inventory, new PouchInventory(player.getMainHandItem()));
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.name;
    }
}