package moreinventory.block;

import javax.annotation.Nullable;

import moreinventory.tileentity.BaseStorageBoxTileEntity;
import moreinventory.tileentity.storagebox.StorageBoxType;
import moreinventory.tileentity.storagebox.StorageBoxTileEntityType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class StorageBoxBlock extends ContainerBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private StorageBoxType type;

    protected StorageBoxBlock(StorageBoxType typeIn) {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(2.0F, 10.0F)
                .func_235861_h_()//素手で壊すときに遅くなる？
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
                .notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
        this.type = typeIn;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.isIn(newState.getBlock())) {
            TileEntity tileentity = world.getTileEntity(pos);
            if (tileentity instanceof IInventory) {
                InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileentity);
                world.updateComparatorOutputLevel(pos, this);
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BaseStorageBoxTileEntity tile = (BaseStorageBoxTileEntity) world.getTileEntity(pos);
        tile.onPlaced();
        super.onBlockPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        //隣がコンテナだったときに限って呼ばれるように
        if (blockIn instanceof StorageBoxBlock) {
            BaseStorageBoxTileEntity tile = (BaseStorageBoxTileEntity) world.getTileEntity(pos);
            tile.onDestroyedNeighbor(fromPos);
        }
    }

    @Override
    //right click
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (world.isRemote)
            return ActionResultType.SUCCESS;

        BaseStorageBoxTileEntity tile = (BaseStorageBoxTileEntity) world.getTileEntity(pos);
        ActionResultType ret = tile.rightClickEvent(world, player) ? ActionResultType.SUCCESS : ActionResultType.PASS;
        return ret;

    }

    @Override
    //left click
    public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (world.isRemote)
            return;

        BaseStorageBoxTileEntity tile = (BaseStorageBoxTileEntity) world.getTileEntity(pos);
        tile.leftClickEvent(player);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return createNewTileEntity(world);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        try {
            return StorageBoxTileEntityType.classMap.get(type).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new BaseStorageBoxTileEntity(type);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.with(FACING, direction.rotate(state.get(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

}
