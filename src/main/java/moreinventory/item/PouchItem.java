package moreinventory.item;

import moreinventory.container.PouchContainerProvider;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.inventory.PouchInventory;
import moreinventory.util.MIMUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PouchItem extends Item {
    public static final int default_color = 0;

    public PouchItem() {
        super(new Properties()
                .durability(0)
                .tab(MoreInventoryMOD.itemGroup));
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getLevel();
        if (world.isClientSide) {
            return ActionResultType.PASS;
        }

        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();

        if (block == Blocks.CAULDRON) {
            int level = blockState.getValue(CauldronBlock.LEVEL);
            if (0 < level && getColor(stack) != default_color) {
                resetColor(context);
                BlockState newState = blockState.setValue(CauldronBlock.LEVEL, level - 1);
                world.setBlockAndUpdate(blockPos, newState);
                world.playSound(null, context.getClickedPos(), SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundCategory.PLAYERS, 1.5F, 0.85F);

                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        PlayerEntity player = context.getPlayer();
        BlockPos blockPos = context.getClickedPos();
        if (player.isShiftKeyDown()) {
            TileEntity tile = world.getBlockEntity(blockPos);
            PouchInventory inventory = new PouchInventory(player.getMainHandItem());

            if (tile == null) {
                inventory.collectAllItemStack(player.inventory, true);
            } else if (tile instanceof IInventory) {
                inventory.transferToChest((IInventory) tile);
            }

            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {

        ItemStack itemStack = player.getItemInHand(hand);
        if (player.isShiftKeyDown()) {
            PouchInventory inventory = new PouchInventory(itemStack);
            inventory.collectAllItemStack(player.inventory, true);
            player.swing(hand);
        }
        if (!world.isClientSide) {
            player.openMenu(new PouchContainerProvider(hand));
        }
        return super.use(world, player, hand);

    }

    public int getColor(ItemStack s) {
        final String key = "CustomModelData";
        CompoundNBT tag = s.getOrCreateTag();
        int color = tag.getByte(key);
        return color;
    }

    public void resetColor(ItemUseContext context) {
        setColor(context, default_color);
    }

    public void setColor(ItemUseContext context, DyeColor color) {
        setColor(context, color.getId() + 1);
    }

    public void setColor(ItemUseContext context, int color) {
        MIMUtils.setIcon(context.getItemInHand(), (byte) color);
    }
}
