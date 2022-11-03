package moreinventory.blockentity;

import java.util.List;

import moreinventory.block.Blocks;
import moreinventory.block.TransportBlock;
import moreinventory.container.TransportContainer;
import moreinventory.util.MIMUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ExporterBlockEntity extends BaseTransportBlockEntity {

    public ExporterBlockEntity() {
        super(BlockEntities.EXPORTER_BLOCK_ENTITY_TYPE.get());
    }

    private BlockPos boxPos = BlockPos.ZERO;
    private int currentSlot = 0;

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent(Blocks.EXPORTER.get().getDescriptionId());
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new TransportContainer(id, player, this);
    }

    @Override
    protected void doExtract() {
        Direction out = this.level.getBlockState(this.worldPosition).getValue(TransportBlock.FACING_OUT);
        BlockPos outPos = this.worldPosition.relative(out);
        IInventory inventory = HopperTileEntity.getContainerAt(this.level, outPos);

        if (inventory != null) {
            extract: for (int i = 0; i < 9; i++) {
                ItemStack itemstack = slotItems.get(currentSlot);

                if (++currentSlot == 9) {
                    currentSlot = 0;
                }

                if (itemstack.getItem() != ItemStack.EMPTY.getItem()
                        && getBoxPos(itemstack) && !(this.worldPosition.equals(boxPos))) {
                    BaseStorageBoxBlockEntity tile = (BaseStorageBoxBlockEntity) this.level.getBlockEntity(boxPos);

                    for (int j = 0; j < tile.getContainerSize(); j++) {
                        ItemStack itemstack1 = tile.getItem(j);

                        if (MIMUtils.mergeItemStack(itemstack1, inventory, out.getOpposite())) {
                            break extract;
                        }
                    }
                }
            }
        }
        BlockState newState = this.level.getBlockState(boxPos);
        this.level.sendBlockUpdated(boxPos, this.getBlockState(), newState, 0);
    }

    private boolean getBoxPos(ItemStack itemstack) {
        Direction in = this.level.getBlockState(this.worldPosition).getValue(TransportBlock.FACING_IN);
        BlockPos inPos = this.worldPosition.relative(in);//pull
        Direction out = this.level.getBlockState(this.worldPosition).getValue(TransportBlock.FACING_OUT);
        BlockPos outPos = this.worldPosition.relative(out);//put

        TileEntity tile = this.level.getBlockEntity(inPos);

        if (tile != null && tile instanceof BaseStorageBoxBlockEntity) {
            List<BaseStorageBoxBlockEntity> list = ((BaseStorageBoxBlockEntity) tile).getStorageBoxNetworkManager().getMatchingList(itemstack, inPos);

            for (BaseStorageBoxBlockEntity tileStorageBox : list) {
                if (!tileStorageBox.getBlockPos().equals(outPos)) {//入力と出力が同じネットワークになるのを防ぐ
                    boxPos = tileStorageBox.getBlockPos();
                    return true;
                }
            }
        }

        return false;
    }

}
