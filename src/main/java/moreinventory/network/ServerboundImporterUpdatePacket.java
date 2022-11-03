package moreinventory.network;

import java.util.function.Supplier;

import moreinventory.blockentity.ImporterBlockEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerboundImporterUpdatePacket {
    private final BlockPos blockPos;
    private final int id;
    private final int val;

    public ServerboundImporterUpdatePacket(BlockPos pos, int id, int val) {
        this.blockPos = pos;
        this.id = id;
        this.val = val;
    }

    public ServerboundImporterUpdatePacket(BlockPos pos, int id) {
        this.blockPos = pos;
        this.id = id;
        this.val = -1;
    }

    public ServerboundImporterUpdatePacket(PacketBuffer buffer) {
        this(buffer.readBlockPos(), buffer.readInt(), buffer.readInt());
    }

    public static ServerboundImporterUpdatePacket decode(PacketBuffer buffer) {
        ServerboundImporterUpdatePacket packet = new ServerboundImporterUpdatePacket(buffer);
        return packet;
    }

    public static void encode(ServerboundImporterUpdatePacket msg, PacketBuffer buffer) {
        buffer.writeBlockPos(msg.blockPos);
        buffer.writeInt(msg.id);
        buffer.writeInt(msg.val);
    }

    public static void handle(ServerboundImporterUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TileEntity tileEntity = ctx.get().getSender().getCommandSenderWorld().getBlockEntity(msg.blockPos);
            if (tileEntity instanceof ImporterBlockEntity) {
                ImporterBlockEntity importerTileEntity = (ImporterBlockEntity) tileEntity;
                int val = (importerTileEntity.getValByID(msg.id) + 1) % 2;
                importerTileEntity.setValByID(msg.id, val);

            }
        });

        ctx.get().setPacketHandled(true);
    }
}