package moreinventory.block;

import javax.annotation.Nullable;

import moreinventory.tileentity.BaseStorageBoxTileEntity;
import moreinventory.tileentity.storagebox.StorageBoxType;
import moreinventory.tileentity.storagebox.StorageBoxTypeTileEntity;
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
        super(Properties.of(Material.METAL)
                .sound(SoundType.METAL)
                .strength(2.0F, 10.0F)
                .requiresCorrectToolForDrops()//素手で壊すときに遅くなる？
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        this.type = typeIn;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            TileEntity tileentity = world.getBlockEntity(pos);
            if (tileentity instanceof IInventory) {
                InventoryHelper.dropContents(world, pos, (IInventory) tileentity);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BaseStorageBoxTileEntity tile = (BaseStorageBoxTileEntity) world.getBlockEntity(pos);
        tile.onPlaced();
        super.setPlacedBy(world, pos, state, placer, stack);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        //隣がコンテナだったときに限って呼ばれるように
        if (blockIn instanceof StorageBoxBlock) {
            BaseStorageBoxTileEntity tile = (BaseStorageBoxTileEntity) world.getBlockEntity(pos);
            tile.onDestroyedNeighbor(fromPos);
        }
    }

    @Override
    //right click
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (world.isClientSide)
            return ActionResultType.SUCCESS;

        BaseStorageBoxTileEntity tile = (BaseStorageBoxTileEntity) world.getBlockEntity(pos);
        ActionResultType ret = tile.rightClickEvent(world, player) ? ActionResultType.SUCCESS : ActionResultType.PASS;
        return ret;

    }

    @Override
    //left click
    public void attack(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (world.isClientSide)
            return;

        BaseStorageBoxTileEntity tile = (BaseStorageBoxTileEntity) world.getBlockEntity(pos);
        tile.leftClickEvent(player);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return newBlockEntity(world);
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        try {
            return StorageBoxTypeTileEntity.classMap.get(type).newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return new BaseStorageBoxTileEntity(type);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

}
