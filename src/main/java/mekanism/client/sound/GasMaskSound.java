package mekanism.client.sound;

import javax.annotation.Nonnull;
import mekanism.client.ClientTickHandler;
import mekanism.common.Mekanism;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GasMaskSound extends PlayerSound {

    private static final ResourceLocation SOUND = new ResourceLocation(Mekanism.MODID, "item.gasMask");

    public GasMaskSound(@Nonnull PlayerEntity player) {
        super(player, SOUND);
    }

    @Override
    public boolean shouldPlaySound(@Nonnull PlayerEntity player) {
        return ClientTickHandler.isGasMaskOn(player);
    }
}