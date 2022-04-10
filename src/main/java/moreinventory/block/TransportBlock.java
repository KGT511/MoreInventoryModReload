package moreinventory.block;

import javax.annotation.Nullable;

import moreinventory.tileentity.ExporterTileEntity;
import moreinventory.tileentity.ImporterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class TransportBlock extends ContainerBlock {

    public static final DirectionProperty FACING_IN = DirectionProperty.create("facing_in", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);;
    public static final DirectionProperty FACING_OUT = DirectionProperty.create("facing_out", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);;
    private boolean isImporter;

    private static final VoxelShape SHAPE_CENTER = Block.box(7.0D, 7.0D, 7.0D, 9.0D, 9.0D, 9.0D);
    private static final VoxelShape SHAPE_IN_DOWN = VoxelShapes.or(
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D),
            Block.box(4.0D, 1.0D, 4.0D, 12.0D, 2.0D, 12.0D),
            Block.box(5.0D, 3.0D, 5.0D, 11.0D, 4.0D, 11.0D),
            Block.box(6.0D, 5.0D, 6.0D, 10.0D, 6.0D, 10.0D));
    private static final VoxelShape SHAPE_IN_UP = VoxelShapes.or(
            Block.box(2.0D, 15.0D, 2.0D, 14.0D, 16.0D, 14.0D),
            Block.box(4.0D, 14.0D, 4.0D, 12.0D, 15.0D, 12.0D),
            Block.box(5.0D, 12.0D, 5.0D, 11.0D, 13.0D, 11.0D),
            Block.box(6.0D, 10.0D, 6.0D, 10.0D, 11.0D, 10.0D));
    private static final VoxelShape SHAPE_IN_NORTH = VoxelShapes.or(
            Block.box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 1.0D),
            Block.box(4.0D, 4.0D, 1.0D, 12.0D, 12.0D, 2.0D),
            Block.box(5.0D, 5.0D, 3.0D, 11.0D, 11.0D, 4.0D),
            Block.box(6.0D, 6.0D, 5.0D, 10.0D, 10.0D, 6.0D));
    private static final VoxelShape SHAPE_IN_SOUTH = VoxelShapes.or(
            Block.box(2.0D, 2.0D, 15.0D, 14.0D, 14.0D, 16.0D),
            Block.box(4.0D, 4.0D, 14.0D, 12.0D, 12.0D, 15.0D),
            Block.box(5.0D, 5.0D, 12.0D, 11.0D, 11.0D, 13.0D),
            Block.box(6.0D, 6.0D, 11.0D, 10.0D, 10.0D, 16.0D));
    private static final VoxelShape SHAPE_IN_WEST = VoxelShapes.or(
            Block.box(0.0D, 2.0D, 2.0D, 1.0D, 14.0D, 14.0D),
            Block.box(1.0D, 4.0D, 4.0D, 2.0D, 12.0D, 12.0D),
            Block.box(3.0D, 5.0D, 5.0D, 4.0D, 11.0D, 11.0D),
            Block.box(5.0D, 6.0D, 6.0D, 6.0D, 10.0D, 10.0D));
    private static final VoxelShape SHAPE_IN_EAST = VoxelShapes.or(
            Block.box(15.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D),
            Block.box(14.0D, 4.0D, 4.0D, 15.0D, 12.0D, 12.0D),
            Block.box(12.0D, 5.0D, 5.0D, 13.0D, 11.0D, 11.0D),
            Block.box(12.0D, 6.0D, 6.0D, 13.0D, 10.0D, 10.0D));

    private static final VoxelShape SHAPE_OUT_DOWN = VoxelShapes.or(
            Block.box(7.0D, 0.0D, 7.0D, 9.0D, 2.0D, 9.0D),
            Block.box(6.0D, 2.0D, 6.0D, 10.0D, 3.0D, 10.0D),
            Block.box(4.0D, 3.0D, 4.0D, 12.0D, 4.0D, 12.0D),
            Block.box(5.0D, 4.0D, 5.0D, 11.0D, 5.0D, 11.0D),
            Block.box(6.0D, 5.0D, 6.0D, 10.0D, 6.0D, 10.0D));
    private static final VoxelShape SHAPE_OUT_UP = VoxelShapes.or(
            Block.box(7.0D, 14.0D, 7.0D, 9.0D, 16.0D, 9.0D),
            Block.box(6.0D, 13.0D, 6.0D, 10.0D, 14.0D, 10.0D),
            Block.box(4.0D, 12.0D, 4.0D, 12.0D, 13.0D, 12.0D),
            Block.box(5.0D, 11.0D, 5.0D, 11.0D, 12.0D, 11.0D),
            Block.box(6.0D, 10.0D, 6.0D, 10.0D, 11.0D, 10.0D));
    private static final VoxelShape SHAPE_OUT_NORTH = VoxelShapes.or(
            Block.box(7.0D, 7.0D, 0.0D, 9.0D, 9.0D, 2.0D),
            Block.box(6.0D, 6.0D, 2.0D, 10.0D, 10.0D, 3.0D),
            Block.box(4.0D, 4.0D, 3.0D, 12.0D, 12.0D, 4.0D),
            Block.box(5.0D, 5.0D, 4.0D, 11.0D, 11.0D, 5.0D),
            Block.box(6.0D, 6.0D, 5.0D, 10.0D, 10.0D, 6.0D));
    private static final VoxelShape SHAPE_OUT_SOUTH = VoxelShapes.or(
            Block.box(7.0D, 7.0D, 14.0D, 9.0D, 9.0D, 16.0D),
            Block.box(6.0D, 6.0D, 13.0D, 10.0D, 10.0D, 14.0D),
            Block.box(4.0D, 4.0D, 12.0D, 12.0D, 12.0D, 13.0D),
            Block.box(5.0D, 5.0D, 11.0D, 11.0D, 11.0D, 12.0D),
            Block.box(6.0D, 6.0D, 10.0D, 10.0D, 10.0D, 11.0D));
    private static final VoxelShape SHAPE_OUT_WEST = VoxelShapes.or(
            Block.box(0.0D, 7.0D, 7.0D, 2.0D, 9.0D, 9.0D),
            Block.box(2.0D, 6.0D, 6.0D, 3.0D, 10.0D, 10.0D),
            Block.box(3.0D, 4.0D, 4.0D, 4.0D, 12.0D, 12.0D),
            Block.box(4.0D, 5.0D, 5.0D, 5.0D, 11.0D, 11.0D),
            Block.box(5.0D, 6.0D, 6.0D, 6.0D, 10.0D, 10.0D));
    private static final VoxelShape SHAPE_OUT_EAST = VoxelShapes.or(
            Block.box(14.0D, 7.0D, 7.0D, 16.0D, 9.0D, 9.0D),
            Block.box(13.0D, 6.0D, 6.0D, 14.0D, 10.0D, 10.0D),
            Block.box(12.0D, 4.0D, 4.0D, 13.0D, 12.0D, 12.0D),
            Block.box(11.0D, 5.0D, 5.0D, 12.0D, 11.0D, 11.0D),
            Block.box(10.0D, 6.0D, 6.0D, 11.0D, 10.0D, 10.0D));

    protected TransportBlock(boolean isImporterIn) {
        super(Properties.of(Material.STONE)
                .sound(SoundType.STONE)
                .strength(1.0f));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING_IN, Direction.DOWN).setValue(FACING_OUT, Direction.UP));
        isImporter = isImporterIn;
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        if (!world.isClientSide) {
            BlockState newState = rotate(state, world, pos, null);
            world.setBlockAndUpdate(pos, newState);
        }
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        }
        if (!player.isShiftKeyDown()) {
            INamedContainerProvider namedContainerProvider = this.getMenuProvider(state, world, pos);
            if (namedContainerProvider != null) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
                NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider,
                        (packetBuffer -> {
                            packetBuffer.writeBlockPos(pos);
                        }));
            }
        } else if (player.getMainHandItem().getItem() == ItemStack.EMPTY.getItem()) {
            if (state.getBlock() instanceof TransportBlock) {
                world.setBlockAndUpdate(pos, ((TransportBlock) state.getBlock()).rotate(world.getBlockState(pos), world, pos, Rotation.CLOCKWISE_90));
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return Block.box(1.0D, 1.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        VoxelShape shapeIn = SHAPE_IN_DOWN;
        VoxelShape shapeOut = SHAPE_OUT_UP;
        Direction in = state.getValue(FACING_IN);
        Direction out = state.getValue(FACING_OUT);

        switch (in) {
        case DOWN:
            shapeIn = SHAPE_IN_DOWN;
            break;
        case UP:
            shapeIn = SHAPE_IN_UP;
            break;
        case NORTH:
            shapeIn = SHAPE_IN_NORTH;
            break;
        case SOUTH:
            shapeIn = SHAPE_IN_SOUTH;
            break;
        case WEST:
            shapeIn = SHAPE_IN_WEST;
            break;
        case EAST:
            shapeIn = SHAPE_IN_EAST;
            break;
        }
        switch (out) {
        case DOWN:
            shapeOut = SHAPE_OUT_DOWN;
            break;
        case UP:
            shapeOut = SHAPE_OUT_UP;
            break;
        case NORTH:
            shapeOut = SHAPE_OUT_NORTH;
            break;
        case SOUTH:
            shapeOut = SHAPE_OUT_SOUTH;
            break;
        case WEST:
            shapeOut = SHAPE_OUT_WEST;
            break;
        case EAST:
            shapeOut = SHAPE_OUT_EAST;
            break;
        }

        return VoxelShapes.or(shapeIn, SHAPE_CENTER, shapeOut);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return getOcclusionShape(state, worldIn, pos);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader worldIn) {
        return isImporter ? new ImporterTileEntity() : new ExporterTileEntity();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING_IN, FACING_OUT);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING_IN, Direction.DOWN).setValue(FACING_OUT, Direction.UP);
    }

    public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
        boolean flag = false;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                state = rotateTop(state);
                Direction in = state.getValue(FACING_IN);
                Direction out = state.getValue(FACING_OUT);
                if (haveIInventory(in, world, pos) && haveIInventory(out, world, pos)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                break;
            }
        }
        if (!flag) {
            state = state.setValue(FACING_IN, Direction.DOWN).setValue(FACING_OUT, Direction.UP);
        }

        return state;
    }

    private BlockState rotateTop(BlockState state) {
        Direction in = state.getValue(FACING_IN);
        Direction out = state.getValue(FACING_OUT);
        int out_idx = out.get3DDataValue();
        out = Direction.from3DDataValue(out_idx + 1);
        if (out_idx + 1 > Direction.values().length - 1) {
            int in_idx = in.get3DDataValue();
            in = Direction.from3DDataValue(in_idx + 1);
        }
        state = state.setValue(FACING_IN, in).setValue(FACING_OUT, out);
        if (in == out) {
            state = rotateTop(state);
        }
        return state;
    }

    public boolean haveIInventory(Direction side, IWorld world, BlockPos pos) {
        TileEntity tile = world.getBlockEntity(pos.relative(side));
        return tile != null && tile instanceof IInventory;
    }

}
