package mekanism.common.block.basic;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.common.block.interfaces.IHasInventory;
import mekanism.common.block.interfaces.IHasTileEntity;
import mekanism.common.tile.TileEntityBoilerValve;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockBoilerValve extends BlockBasicMultiblock implements IHasInventory, IHasTileEntity<TileEntityBoilerValve> {

    public BlockBoilerValve() {
        super("boiler_valve");
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityBoilerValve();
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState blockState) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
        return ((TileEntityBoilerValve) world.getTileEntity(pos)).getRedstoneLevel();
    }

    @Override
    public int getInventorySize() {
        return 2;
    }

    @Nullable
    @Override
    public Class<? extends TileEntityBoilerValve> getTileClass() {
        return TileEntityBoilerValve.class;
    }
}