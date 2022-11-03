package moreinventory.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import moreinventory.block.StorageBoxBlock;
import moreinventory.blockentity.BaseStorageBoxBlockEntity;
import moreinventory.core.MoreInventoryMOD;
import moreinventory.storagebox.StorageBoxType;
import moreinventory.util.MIMUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TransporterItem extends Item {
    public TransporterItem() {
        super(new Properties()
                .durability(40)
                .tab(MoreInventoryMOD.itemGroup));
    }

    public static final ArrayList<Block> transportableBlocks = new ArrayList<>();
    private static final String tagKey = "tileBlock";
    private static final String blockStateKey = "blockState";

    //チェストだけでなく以下の計6つも対応してる
    public static final void setTransportableBlocks() {
        transportableBlocks.clear();
        transportableBlocks.add(Blocks.CHEST);
        transportableBlocks.add(Blocks.TRAPPED_CHEST);
        transportableBlocks.add(Blocks.FURNACE);
        transportableBlocks.add(Blocks.DISPENSER);
        transportableBlocks.add(Blocks.DROPPER);
        transportableBlocks.add(moreinventory.block.Blocks.WOOD_STORAGE_BOX.get());
        transportableBlocks.add(moreinventory.block.Blocks.IRON_STORAGE_BOX.get());
        transportableBlocks.add(moreinventory.block.Blocks.GOLD_STORAGE_BOX.get());
        transportableBlocks.add(moreinventory.block.Blocks.DIAMOND_STORAGE_BOX.get());
        transportableBlocks.add(moreinventory.block.Blocks.EMERALD_STORAGE_BOX.get());
        transportableBlocks.add(moreinventory.block.Blocks.COPPER_STORAGE_BOX.get());
        transportableBlocks.add(moreinventory.block.Blocks.TIN_STORAGE_BOX.get());
        transportableBlocks.add(moreinventory.block.Blocks.BRONZE_STORAGE_BOX.get());
        transportableBlocks.add(moreinventory.block.Blocks.SILVER_STORAGE_BOX.get());
        transportableBlocks.add(moreinventory.block.Blocks.GLASS_STORAGE_BOX.get());
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getLevel();
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        }

        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();

        if (!transportableBlocks.contains(block)) {
            return ActionResultType.PASS;
        }

        if (holdBlock(context)) {
            setIcon(context);
            world.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;

    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        }

        ItemStack itemStack = context.getItemInHand();
        if (itemStack.getTag().contains(tagKey)) {
            CompoundNBT nbt = itemStack.getTag();

            if (nbt == null) {
                return ActionResultType.PASS;
            }
            ItemStack tileBlock = ItemStack.of(nbt.getCompound(tagKey));

            if (tileBlock == ItemStack.EMPTY) {
                return ActionResultType.PASS;
            }

            int damage = itemStack.getDamageValue() + 1;
            if (placeBlock(context, tileBlock)) {
                PlayerEntity player = context.getPlayer();

                itemStack.hurtAndBreak(damage, player, (tmp) -> {
                    tmp.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
                    world.playSound(tmp, context.getClickedPos(), SoundEvents.WOOD_BREAK, SoundCategory.PLAYERS, 1.5F, 0.85F);
                });
            }
        }

        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getTag() == null) {
            return;
        }
        CompoundNBT contents = stack.getTag().getCompound(tagKey);

        ItemStack hold = ItemStack.of(contents);
        if (hold.getItem() != Items.AIR) {
            IFormattableTextComponent iformattabletextcomponent = hold.getDisplayName().copy();
            tooltip.add(iformattabletextcomponent.withStyle(TextFormatting.AQUA));
        }
        if (hold.getItem() instanceof BlockItem && ((BlockItem) hold.getItem()).getBlock() instanceof StorageBoxBlock) {
            CompoundNBT compoundnbt = stack.getTag();
            if (compoundnbt.contains(BaseStorageBoxBlockEntity.tagKeyContents)) {
                CompoundNBT nbt = compoundnbt.getCompound(BaseStorageBoxBlockEntity.tagKeyContents);
                ItemStack storageContents = ItemStack.of(nbt);
                if (storageContents.getItem() != ItemStack.EMPTY.getItem()) {
                    StorageBoxType type = StorageBoxType.valueOf(compoundnbt.getString(BaseStorageBoxBlockEntity.tagKeyTypeName));
                    NonNullList<ItemStack> storageItems = NonNullList.withSize(BaseStorageBoxBlockEntity.getStorageStackSize(type), ItemStack.EMPTY);
                    MIMUtils.readNonNullListShort(compoundnbt, storageItems);
                    int count = 0;
                    for (ItemStack storageItem : storageItems) {
                        if (storageItem.getItem() == storageContents.getItem()) {
                            count += storageItem.getCount();
                        }
                    }
                    IFormattableTextComponent iformattabletextcomponent = storageContents.getDisplayName().copy();
                    iformattabletextcomponent.append(" x").append(String.valueOf(count));
                    tooltip.add(iformattabletextcomponent.withStyle(TextFormatting.WHITE));
                }
            }
        } else {
            CompoundNBT compoundnbt = stack.getTag();
            if (compoundnbt.contains("Items", 9)) {
                NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(compoundnbt, nonnulllist);
                int i = 0;
                int j = 0;

                for (ItemStack itemstack : nonnulllist) {
                    if (!itemstack.isEmpty()) {
                        ++j;
                        if (i <= 4) {
                            ++i;
                            IFormattableTextComponent iformattabletextcomponent = itemstack.getDisplayName().copy();
                            iformattabletextcomponent.append(" x").append(String.valueOf(itemstack.getCount()));
                            tooltip.add(iformattabletextcomponent);
                        }
                    }
                }

                if (j - i > 0) {
                    tooltip.add((new TranslationTextComponent("container.shulkerBox.more", j - i)).withStyle(TextFormatting.ITALIC));
                }
            }
        }

    }

    private boolean holdBlock(ItemUseContext context) {
        ItemStack itemStack = context.getItemInHand();
        World world = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);

        TileEntity tile = world.getBlockEntity(blockPos);
        if (tile == null || !(tile instanceof IInventory) || itemStack.getTag().contains(tagKey)) {
            return false;
        }
        if (tile != null && checkMatryoshka((IInventory) tile)) {
            CompoundNBT nbt = itemStack.getOrCreateTag();
            tile.save(nbt);

            ItemStack tileBlock = new ItemStack(blockState.getBlock(), 1);

            IInventory inventory = (IInventory) tile;
            inventory.clearContent();

            if (nbt.contains("RecipesUsed")) {
                nbt.remove("RecipesUsed");
            }

            CompoundNBT tag = new CompoundNBT();
            if (tileBlock != ItemStack.EMPTY) {
                tileBlock.save(tag);
            }
            itemStack.setTag(tag);

            nbt.put(tagKey, tag);
            nbt.put(blockStateKey, NBTUtil.writeBlockState(blockState));

            itemStack.setTag(nbt);

            return true;
        }
        return false;
    }

    private boolean checkMatryoshka(IInventory inventory) {
        ItemStack itemstack;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            itemstack = inventory.getItem(i);

            if (itemstack.getItem() == TransporterItem.this && itemstack.getTag().contains(tagKey)
            //                    || itemstack.getItem() == MoreInventoryMod.pouch && !checkMatryoshka(new InventoryPouch(itemstack))
            ) {
                return false;
            }
        }
        return true;
    }

    private boolean placeBlock(ItemUseContext context, ItemStack tileBlock) {

        World world = context.getLevel();
        Block block = Block.byItem(tileBlock.getItem());
        ItemStack itemStack = context.getItemInHand();
        BlockPos blockPos = context.getClickedPos();
        BlockRayTraceResult blockRayTraceResult = new BlockRayTraceResult(context.getClickLocation(), context.getClickedFace(), blockPos, context.isInside());
        BlockItemUseContext blockItemUseContext = new BlockItemUseContext(context.getPlayer(), context.getHand(), tileBlock, blockRayTraceResult);

        if (blockItemUseContext.canPlace()) {
            boolean isReplaceable = world.getBlockState(blockPos).canBeReplaced(new BlockItemUseContext(context));

            TileEntity tile;
            BlockPos setBlockPos = blockPos;
            if (!isReplaceable) {
                setBlockPos = blockPos.relative(context.getClickedFace());
            }
            BlockState state = readBlockState(context, tileBlock);

            world.setBlockAndUpdate(setBlockPos, state);
            tile = world.getBlockEntity(setBlockPos);
            if (tile == null) {
                world.setBlockAndUpdate(setBlockPos, Blocks.AIR.defaultBlockState());
                return false;
            }

            CompoundNBT nbt = itemStack.getOrCreateTag();

            nbt.putInt("x", tile.getBlockPos().getX());
            nbt.putInt("y", tile.getBlockPos().getY());
            nbt.putInt("z", tile.getBlockPos().getZ());
            tile.load(tile.getBlockState(), nbt);

            block.setPlacedBy(world, tile.getBlockPos(), tile.getBlockState(), context.getPlayer(), tileBlock);
            tile.setChanged();
            itemStack.setTag(null);
            return true;
        }
        return false;

    }

    private BlockState readBlockState(ItemUseContext context, ItemStack tileBlock) {
        ItemStack stack = context.getItemInHand();
        CompoundNBT blockStateTag = stack.getTag().getCompound(blockStateKey);

        Block block = Block.byItem(tileBlock.getItem());
        BlockState state = block.getStateForPlacement(new BlockItemUseContext(context));

        if (block instanceof FurnaceBlock) {
            BlockState furnaceState = NBTUtil.readBlockState(blockStateTag);
            state = state.setValue(FurnaceBlock.LIT, furnaceState.getValue(FurnaceBlock.LIT));
        }
        return state;
    }

    private void setIcon(ItemUseContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
        Block block = blockState.getBlock();
        int num = transportableBlocks.contains(block) ? transportableBlocks.indexOf(block) + 1 : 0;
        if (block == Blocks.FURNACE && blockState.getValue(FurnaceBlock.LIT)) {
            num = 99;
        }
        MIMUtils.setIcon(context.getItemInHand(), (byte) num);
    }

}