package moreinventory.network;

import moreinventory.inventory.PouchInventory;
import moreinventory.item.PouchItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class ServerboundPouchUpdatePacket {
    private int id;
    private int val;

    public ServerboundPouchUpdatePacket() {
    }

    public ServerboundPouchUpdatePacket(int id, int val) {
        this.id = id;
        this.val = val;
    }

    public ServerboundPouchUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readInt());
    }

    public static ServerboundPouchUpdatePacket decode(FriendlyByteBuf buffer) {
        var packet = new ServerboundPouchUpdatePacket(buffer);
        return packet;
    }

    public static void encode(ServerboundPouchUpdatePacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.id);
        buffer.writeInt(msg.val);
    }

    public static void handle(ServerboundPouchUpdatePacket msg, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            var player = ctx.getSender();
            var itemStack = player.getMainHandItem();
            if (itemStack != null && itemStack.getItem() instanceof PouchItem) {
                var pouch_ = new PouchInventory(player, itemStack);
                pouch_.setValByID(msg.id, msg.val);
            }
        });

        ctx.setPacketHandled(true);
    }
}
