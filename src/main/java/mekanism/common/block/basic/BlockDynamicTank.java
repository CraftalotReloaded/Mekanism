package mekanism.common.block.basic;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.common.block.interfaces.IHasInventory;
import mekanism.common.block.interfaces.IHasModel;
import mekanism.common.block.interfaces.IHasTileEntity;
import mekanism.common.tile.TileEntityDynamicTank;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDynamicTank extends BlockBasicMultiblock implements IHasModel, IHasInventory, IHasTileEntity<TileEntityDynamicTank> {

    public BlockDynamicTank() {
        super("dynamic_tank");
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityDynamicTank();
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 0;
    }

    @Override
    public int getInventorySize() {
        return 2;
    }

    @Nullable
    @Override
    public Class<? extends TileEntityDynamicTank> getTileClass() {
        return TileEntityDynamicTank.class;
    }
}