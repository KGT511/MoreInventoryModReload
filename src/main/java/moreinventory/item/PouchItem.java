package moreinventory.item;

import java.util.TreeMap;

import moreinventory.container.PouchContainerProvider;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.inventory.PouchInventory;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;

public class PouchItem extends Item {
    private static final TreeMap<DyeColor, PouchItem> ITEM_BY_COLOR = new TreeMap<>();
    public static final int default_color = 0;
    private DyeColor color;

    public PouchItem(DyeColor color) {
        super(new Properties()
                .durability(0)
                .tab(MoreInventoryMOD.creativeModeTab));
        this.color = color;
        if (color != null)
            ITEM_BY_COLOR.put(color, this);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        var level = context.getLevel();
        var blockPos = context.getClickedPos();
        var blockState = level.getBlockState(blockPos);
        var block = blockState.getBlock();

        if (block == Blocks.WATER_CAULDRON) {

            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            }

            int waterLevel = blockState.getValue(LayeredCauldronBlock.LEVEL);
            if (0 < waterLevel && getColor(stack) != default_color) {
                var defaultColorPouch = resetColor(stack);
                var player = context.getPlayer();
                player.setItemInHand(player.getUsedItemHand(), defaultColorPouch);
                LayeredCauldronBlock.lowerFillLevel(blockState, level, blockPos);
                level.playSound(null, context.getClickedPos(), SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundSource.PLAYERS, 1.5F, 0.85F);

                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        var blockPos = context.getClickedPos();
        var player = context.getPlayer();

        if (player.isShiftKeyDown()) {
            var tile = level.getBlockEntity(blockPos);
            var itemStack = player.getMainHandItem();
            var inventory = new PouchInventory(itemStack);

            if (tile == null) {
                inventory.collectAllItemStack(player.getInventory(), true);
            } else if (tile instanceof Container) {
                inventory.transferToChest((Container) tile);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var itemStack = player.getItemInHand(hand);

        if (!level.isClientSide && !player.isShiftKeyDown())
            player.openMenu(new PouchContainerProvider(hand));
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());

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
        var tag = itemStack.getOrCreateTag();
        var newPouch = (color == 0 ? Items.POUCH : byColor(DyeColor.byId(color - 1)));
        itemStack = new ItemStack(newPouch);
        itemStack.setTag(tag);

        return itemStack;
    }

    public static int getColor(ItemStack s) {
        if (!(s.getItem() instanceof PouchItem)) {
            return 0;

        } else {
            var color = ((PouchItem) s.getItem()).color;
            return color == null ? 0 : color.getId() + 1;
        }
    }

    public static PouchItem byColor(DyeColor color) {
        return ITEM_BY_COLOR.get(color);
    }
}
