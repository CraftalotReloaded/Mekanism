package mekanism.common.tile.prefab;

import java.util.List;
import javax.annotation.Nonnull;
import mekanism.api.NBTConstants;
import mekanism.api.Upgrade;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.ITileCachedRecipeHolder;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

public abstract class TileEntityOperationalMachine<RECIPE extends MekanismRecipe> extends TileEntityMekanism implements ITileCachedRecipeHolder<RECIPE> {

    private int operatingTicks;

    public int BASE_TICKS_REQUIRED;

    public int ticksRequired;
    protected CachedRecipe<RECIPE> cachedRecipe = null;

    protected TileEntityOperationalMachine(IBlockProvider blockProvider, int baseTicksRequired) {
        super(blockProvider);
        ticksRequired = BASE_TICKS_REQUIRED = baseTicksRequired;
    }

    public double getScaledProgress() {
        return getOperatingTicks() / (double) ticksRequired;
    }

    protected void setOperatingTicks(int ticks) {
        this.operatingTicks = ticks;
    }

    public int getOperatingTicks() {
        return operatingTicks;
    }

    @Override
    public int getSavedOperatingTicks(int cacheIndex) {
        return getOperatingTicks();
    }

    @Override
    public void read(CompoundNBT nbtTags) {
        super.read(nbtTags);
        operatingTicks = nbtTags.getInt(NBTConstants.PROGRESS);
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT nbtTags) {
        super.write(nbtTags);
        nbtTags.putInt(NBTConstants.PROGRESS, getOperatingTicks());
        return nbtTags;
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED) {
            ticksRequired = MekanismUtils.getTicks(this, BASE_TICKS_REQUIRED);
        }
    }

    @Override
    public boolean renderUpdate() {
        return true;
    }

    @Override
    public boolean lightUpdate() {
        return true;
    }

    @Override
    public List<ITextComponent> getInfo(Upgrade upgrade) {
        return UpgradeUtils.getMultScaledInfo(this, upgrade);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(() -> operatingTicks, this::setOperatingTicks));
        container.track(SyncableInt.create(() -> ticksRequired, value -> ticksRequired = value));
    }
}