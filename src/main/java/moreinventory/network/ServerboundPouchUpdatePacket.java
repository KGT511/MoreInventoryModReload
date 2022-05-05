package moreinventory.network;

import java.util.function.Supplier;

import moreinventory.inventory.PouchInventory;
import moreinventory.item.Items;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerboundPouchUpdatePacket {
    private int id;
    private int val;

    public ServerboundPouchUpdatePacket() {
    }

    public ServerboundPouchUpdatePacket(int id, int val) {
        this.id = id;
        this.val = val;
    }

    public ServerboundPouchUpdatePacket(PacketBuffer buffer) {
        this(buffer.readInt(), buffer.readInt());
    }

    public static ServerboundPouchUpdatePacket decode(PacketBuffer buffer) {
        ServerboundPouchUpdatePacket packet = new ServerboundPouchUpdatePacket(buffer);
        return packet;
    }

    public static void encode(ServerboundPouchUpdatePacket msg, PacketBuffer buffer) {
        buffer.writeInt(msg.id);
        buffer.writeInt(msg.val);
    }

    public static void handle(ServerboundPouchUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            ItemStack itemStack = player.getItemInHand(player.getUsedItemHand());
            if (itemStack != null && itemStack.getItem() == Items.POUCH) {
                PouchInventory pouch_ = new PouchInventory(player, itemStack);
                pouch_.setValByID(msg.id, msg.val);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
