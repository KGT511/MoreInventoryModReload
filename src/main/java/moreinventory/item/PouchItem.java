package moreinventory.item;

import java.util.TreeMap;

import moreinventory.container.PouchContainerProvider;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.inventory.PouchInventory;
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
    private static final TreeMap<DyeColor, PouchItem> ITEM_BY_COLOR = new TreeMap<>();
    public static final int default_color = 0;
    private DyeColor color;

    public PouchItem(DyeColor color) {
        super(new Properties()
                .durability(0)
                .tab(MoreInventoryMOD.itemGroup));
        this.color = color;
        if (color != null)
            ITEM_BY_COLOR.put(color, this);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getLevel();

        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();

        if (block == Blocks.CAULDRON) {
            if (world.isClientSide) {
                return ActionResultType.SUCCESS;
            }

            int level = blockState.getValue(CauldronBlock.LEVEL);
            if (0 < level && getColor(stack) != default_color) {
                ItemStack defaultColorPouch = resetColor(stack);
                PlayerEntity player = context.getPlayer();
                player.setItemInHand(player.getUsedItemHand(), defaultColorPouch);
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
        BlockPos blockPos = context.getClickedPos();
        PlayerEntity player = context.getPlayer();
        if (player.isShiftKeyDown()) {
            TileEntity tile = world.getBlockEntity(blockPos);
            ItemStack itemStack = player.getMainHandItem();
            PouchInventory inventory = new PouchInventory(itemStack);

            if (tile == null) {
                inventory.collectAllItemStack(player.inventory, true);
            } else if (tile instanceof IInventory) {
                inventory.transferToChest((IInventory) tile);
            }

            return ActionResultType.sidedSuccess(world.isClientSide);
        }

        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!world.isClientSide && !player.isShiftKeyDown() && hand.equals(Hand.MAIN_HAND)) {
            player.openMenu(new PouchContainerProvider());
        }
        return ActionResult.sidedSuccess(itemStack, world.isClientSide());

    }

    public DyeColor getColor() {
        return this.color;
    }

    public static ItemStack resetColor(ItemStack itemStack) {
        return setColor(itemStack, default_color);
    }

    public static ItemStack setColor(ItemStack itemStack, DyeColor color) {
        return setColor(itemStack, (color == null ? 0 : color.getId() + 1));
    }

    public static ItemStack setColor(ItemStack itemStack, int color) {
        if (!(itemStack.getItem() instanceof PouchItem))
            return itemStack;
        CompoundNBT tag = itemStack.getOrCreateTag();
        PouchItem newPouch = (color == 0 ? Items.POUCH : byColor(DyeColor.byId(color - 1)));
        itemStack = new ItemStack(newPouch);
        itemStack.setTag(tag);

        return itemStack;
    }

    public static int getColor(ItemStack s) {
        if (!(s.getItem() instanceof PouchItem)) {
            return 0;

        } else {
            DyeColor color = ((PouchItem) s.getItem()).color;
            return color == null ? 0 : color.getId() + 1;
        }
    }

    public static PouchItem byColor(DyeColor color) {
        return ITEM_BY_COLOR.get(color);
    }
}
