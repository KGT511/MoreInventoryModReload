package moreinventory.tileentity;

import moreinventory.block.Blocks;
import moreinventory.block.TransportBlock;
import moreinventory.container.TransportContainer;
import moreinventory.tileentity.storagebox.network.IStorageBoxNetwork;
import moreinventory.util.MIMUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ImporterTileEntity extends BaseTransportTileEntity {
    private boolean register = false;
    private boolean isWhite = false;//falseの時はブラックリストになる

    public enum Val {
        REGISTER, WHITE
    };

    public static final String registerKey = "register";
    public static final String isWhiteKey = "is_white";

    public ImporterTileEntity() {
        super(TileEntities.IMPORTER_TILE_TYPE);
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        register = nbt.getBoolean(registerKey);
        isWhite = nbt.getBoolean(isWhiteKey);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putBoolean(registerKey, register);
        compound.putBoolean(isWhiteKey, isWhite);
        return compound;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent(Blocks.IMPORTER.getDescriptionId());
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new TransportContainer(id, player, this);
    }

    public void putInBox(IInventory inventory) {
        Direction out = this.level.getBlockState(this.worldPosition).getValue(TransportBlock.FACING_OUT);
        BlockPos outPos = this.worldPosition.relative(out);
        Direction in = this.level.getBlockState(this.worldPosition).getValue(TransportBlock.FACING_IN);

        TileEntity tile = this.level.getBlockEntity(outPos);

        if (tile != null && tile instanceof IStorageBoxNetwork) {
            int size = inventory.getContainerSize();

            if (currentSlot >= size) {
                currentSlot = 0;
            }

            for (int i = 0; i < size; i++) {
                int slot = currentSlot;

                if (++currentSlot == size) {
                    currentSlot = 0;
                }

                ItemStack itemstack = inventory.getItem(slot);
                if (itemstack != null && canExtract(itemstack)) {
                    if (MIMUtils.canAccessFromSide(inventory, slot, in.getOpposite()) && MIMUtils.canExtractFromSide(inventory, itemstack, slot, in.getOpposite())) {
                        if (tile instanceof BaseStorageBoxTileEntity && ((BaseStorageBoxTileEntity) tile).getStorageBoxNetworkManager().storeToNetwork(itemstack, register, outPos)) {

                            return;
                        }
                    }
                }
            }
        }
    }

    protected boolean canExtract(ItemStack itemstack) {
        boolean result = !isWhite;

        for (ItemStack itemstack1 : this.slotItems) {
            if (ItemStack.isSame(itemstack1, itemstack)) {
                result = isWhite;
            }
        }

        return result;
    }

    @Override
    protected void doExtract() {

        Direction in = this.level.getBlockState(this.worldPosition).getValue(TransportBlock.FACING_IN);
        BlockPos inPos = this.worldPosition.relative(in);
        IInventory inventory = HopperTileEntity.getContainerAt(this.level, inPos);

        if (inventory != null) {
            putInBox(inventory);
            BlockState newState = this.level.getBlockState(inPos);
            this.level.sendBlockUpdated(inPos, this.getBlockState(), newState, 0);
        }
    }

    public boolean getIswhite() {
        return isWhite;
    }

    public boolean getIsRegister() {
        return register;
    }

    public void setIsWhite(boolean val) {
        isWhite = val;
    }

    public void setIsRegister(boolean val) {
        register = val;
    }

    public void setValByID(int id, int val) {
        if (Val.values().length <= id) {
            return;
        }
        switch (Val.values()[id]) {
        case REGISTER:
            setIsRegister(MIMUtils.intToBool(val));
            break;
        case WHITE:
            setIsWhite(MIMUtils.intToBool(val));
            break;
        }

    }

    public int getValByID(int id) {
        if (Val.values().length <= id) {
            return 0;
        }
        switch (Val.values()[id]) {
        case REGISTER:
            return this.getIsRegister() ? 1 : 0;
        case WHITE:
            return this.getIswhite() ? 1 : 0;
        }

        return 0;
    }
}
