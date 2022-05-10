package moreinventory.container;

import moreinventory.inventory.PouchInventory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class PouchContainerProvider implements MenuProvider {
    private InteractionHand hand;
    private Component name;

    public PouchContainerProvider(InteractionHand hand) {
        this.hand = hand;
    }

    @Override
    public AbstractContainerMenu createMenu(int windowID, Inventory inventory, Player player) {
        this.name = player.getItemInHand(this.hand).getHoverName();
        return new PouchContainer(windowID, inventory, new PouchInventory(player.getItemInHand(this.hand)));
    }

    @Override
    public Component getDisplayName() {
        return this.name;
    }
}