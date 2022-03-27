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
    public void func_230337_a_(BlockState state, CompoundNBT nbt) {
        super.func_230337_a_(state, nbt);
        register = nbt.getBoolean(registerKey);
        isWhite = nbt.getBoolean(isWhiteKey);
    }

    public void read(BlockState state, CompoundNBT nbt) {
        func_230337_a_(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putBoolean(registerKey, register);
        compound.putBoolean(isWhiteKey, isWhite);
        return compound;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent(Blocks.IMPORTER.getTranslationKey());
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new TransportContainer(id, player, this);
    }

    public void putInBox(IInventory inventory) {
        Direction out = this.world.getBlockState(this.pos).get(TransportBlock.FACING_OUT);
        BlockPos outPos = this.pos.offset(out);
        Direction in = this.world.getBlockState(this.pos).get(TransportBlock.FACING_IN);

        TileEntity tile = this.world.getTileEntity(outPos);

        if (tile != null && tile instanceof IStorageBoxNetwork) {
            int size = inventory.getSizeInventory();

            if (currentSlot >= size) {
                currentSlot = 0;
            }

            for (int i = 0; i < size; i++) {
                int slot = currentSlot;

                if (++currentSlot == size) {
                    currentSlot = 0;
                }

                ItemStack itemstack = inventory.getStackInSlot(slot);
                if (itemstack != null && canExtract(itemstack)) {
                    if (canAccessFromSide(inventory, slot, in.getOpposite()) && canExtractFromSide(inventory, itemstack, slot, in.getOpposite())) {
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
            if (ItemStack.areItemsEqual(itemstack1, itemstack)) {
                result = isWhite;
            }
        }

        return result;
    }

    @Override
    protected void doExtract() {

        Direction in = this.world.getBlockState(this.pos).get(TransportBlock.FACING_IN);
        BlockPos inPos = this.pos.offset(in);
        IInventory inventory = HopperTileEntity.getInventoryAtPosition(this.world, inPos);

        if (inventory != null) {
            putInBox(inventory);
            BlockState newState = this.world.getBlockState(inPos);
            this.world.notifyBlockUpdate(inPos, this.getBlockState(), newState, 0);
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
