package mekanism.common.item.block;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.api.EnumColor;
import mekanism.common.block.basic.BlockBin;
import mekanism.common.inventory.InventoryBin;
import mekanism.common.item.ITieredItem;
import mekanism.common.tier.BinTier;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.LangUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemBlockBin extends ItemBlockTooltip implements ITieredItem<BinTier> {

    public ItemBlockBin(BlockBin block) {
        super(block);
        setMaxStackSize(1);
    }

    @Nullable
    @Override
    public BinTier getTier(@Nonnull ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemBlockBin) {
            return ((BlockBin) ((ItemBlockBin) item).block).getTier();
        }
        return null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addStats(@Nonnull ItemStack itemstack, World world, @Nonnull List<String> list, @Nonnull ITooltipFlag flag) {
        InventoryBin inv = new InventoryBin(itemstack);
        if (inv.getItemCount() > 0) {
            list.add(EnumColor.BRIGHT_GREEN + inv.getItemType().getDisplayName());
            String amountStr = inv.getItemCount() == Integer.MAX_VALUE ? LangUtils.localize("gui.infinite") : "" + inv.getItemCount();
            list.add(EnumColor.PURPLE + LangUtils.localize("tooltip.itemAmount") + ": " + EnumColor.GREY + amountStr);
        } else {
            list.add(EnumColor.DARK_RED + LangUtils.localize("gui.empty"));
        }
        BinTier tier = getTier(itemstack);
        if (tier != null) {
            int cap = tier.getStorage();
            list.add(EnumColor.INDIGO + LangUtils.localize("tooltip.capacity") + ": " + EnumColor.GREY +
                     (cap == Integer.MAX_VALUE ? LangUtils.localize("gui.infinite") : cap) + " " + LangUtils.localize("transmission.Items"));
        }
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return ItemDataUtils.hasData(stack, "newCount");
    }

    @Nonnull
    @Override
    public ItemStack getContainerItem(@Nonnull ItemStack stack) {
        if (!ItemDataUtils.hasData(stack, "newCount")) {
            return ItemStack.EMPTY;
        }
        int newCount = ItemDataUtils.getInt(stack, "newCount");
        ItemDataUtils.removeData(stack, "newCount");
        ItemStack ret = stack.copy();
        ItemDataUtils.setInt(ret, "itemCount", newCount);
        return ret;
    }
}