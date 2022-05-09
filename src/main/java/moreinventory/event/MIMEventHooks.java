package moreinventory.event;

import moreinventory.core.MoreInventoryMOD;
import moreinventory.inventory.PouchInventory;
import moreinventory.item.PouchItem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MoreInventoryMOD.MODID)
public class MIMEventHooks {

    @SubscribeEvent
    public static void pickupItem(EntityItemPickupEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            ItemStack item = event.getItem().getItem();
            PlayerInventory inventory = player.inventory;

            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack itemstack = inventory.getItem(i);

                if (itemstack != null) {
                    //                    String uuid = player.getStringUUID();

                    if (itemstack.getItem() instanceof PouchItem) {
                        PouchInventory pouch = new PouchInventory(itemstack);

                        if (pouch.canAutoCollect(item)) {
                            PouchInventory.mergeItemStack(item, pouch);
                        }

                        //                        if (Config.isFullAutoCollectPouch.contains(uuid)) {
                        //                            pouch.collectAllItemStack(inventory, false);
                        //                        }
                    }

                    //                    if (Config.isCollectTorch.contains(uuid) && item.getItem() == Item.getItemFromBlock(Blocks.torch) && itemstack.getItem() == MoreInventoryMod.torchHolder ||
                    //                            Config.isCollectArrow.contains(uuid) && item.getItem() == Items.arrow && itemstack.getItem() == MoreInventoryMod.arrowHolder) {
                    //                        int damage = itemstack.getItemDamage();
                    //                        int count = item.stackSize;
                    //
                    //                        if (damage >= count) {
                    //                            itemstack.setItemDamage(damage - count);
                    //                            item.stackSize = 0;
                    //                        } else {
                    //                            itemstack.setItemDamage(0);
                    //                            item.stackSize -= damage;
                    //                        }
                    //                    }
                }
            }
        }
    }

}
