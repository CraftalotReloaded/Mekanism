package mekanism.common.item.block.machine;

import java.util.List;
import javax.annotation.Nonnull;
import mekanism.api.EnumColor;
import mekanism.client.MekanismClient;
import mekanism.common.block.machine.BlockSeismicVibrator;
import mekanism.common.capabilities.ItemCapabilityWrapper;
import mekanism.common.integration.forgeenergy.ForgeEnergyItemWrapper;
import mekanism.common.item.IItemEnergized;
import mekanism.common.item.IItemSustainedInventory;
import mekanism.common.item.block.ItemBlockAdvancedTooltip;
import mekanism.common.security.ISecurityItem;
import mekanism.common.util.LangUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.SecurityUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemBlockSeismicVibrator extends ItemBlockAdvancedTooltip implements IItemEnergized, IItemSustainedInventory, ISecurityItem {

    public ItemBlockSeismicVibrator(BlockSeismicVibrator block) {
        super(block);
        setMaxStackSize(1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addDetails(@Nonnull ItemStack itemstack, World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flag) {
        list.add(SecurityUtils.getOwnerDisplay(Minecraft.getMinecraft().player, MekanismClient.clientUUIDMap.get(getOwnerUUID(itemstack))));
        list.add(EnumColor.GREY + LangUtils.localize("gui.security") + ": " + SecurityUtils.getSecurityDisplay(itemstack, Side.CLIENT));
        if (SecurityUtils.isOverridden(itemstack, Side.CLIENT)) {
            list.add(EnumColor.RED + "(" + LangUtils.localize("gui.overridden") + ")");
        }
        list.add(EnumColor.BRIGHT_GREEN + LangUtils.localize("tooltip.storedEnergy") + ": " + EnumColor.GREY
                 + MekanismUtils.getEnergyDisplay(getEnergy(itemstack), getMaxEnergy(itemstack)));
        list.add(EnumColor.AQUA + LangUtils.localize("tooltip.inventory") + ": " + EnumColor.GREY +
                 LangUtils.transYesNo(getInventory(itemstack) != null && getInventory(itemstack).tagCount() != 0));
    }

    @Override
    public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull PlayerEntity player, World world, @Nonnull BlockPos pos, Direction side, float hitX, float hitY,
          float hitZ, @Nonnull BlockState state) {
        BlockPos abovePos = pos.up();
        if (!world.isValid(abovePos) || !world.getBlockState(abovePos).getBlock().isReplaceable(world, abovePos)) {
            //If there isn't room then fail
            return false;
        }
        return super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, state);
    }

    @Override
    public double getMaxEnergy(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item instanceof ItemBlockSeismicVibrator) {
            return MekanismUtils.getMaxEnergy(itemStack, ((BlockSeismicVibrator) (((ItemBlockSeismicVibrator) item).block)).getStorage());
        }
        return 0;
    }

    @Override
    public double getMaxTransfer(ItemStack itemStack) {
        return getMaxEnergy(itemStack) * 0.005;
    }

    @Override
    public boolean canReceive(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean canSend(ItemStack itemStack) {
        return false;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new ItemCapabilityWrapper(stack, new ForgeEnergyItemWrapper());
    }
}