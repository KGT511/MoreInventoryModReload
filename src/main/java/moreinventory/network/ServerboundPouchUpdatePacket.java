package moreinventory.network;

import java.util.function.Supplier;

import moreinventory.inventory.PouchInventory;
import moreinventory.item.PouchItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

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

    public static void handle(ServerboundPouchUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = ctx.get().getSender();
            var itemStack = player.getMainHandItem();
            if (itemStack != null && itemStack.getItem() instanceof PouchItem) {
                var pouch_ = new PouchInventory(player, itemStack);
                pouch_.setValByID(msg.id, msg.val);
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
