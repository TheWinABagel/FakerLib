package dev.shadowsoffire.placebo.mixin;

import dev.shadowsoffire.placebo.patreon.PatreonPreview;
import dev.shadowsoffire.placebo.patreon.PatreonUtils;
import dev.shadowsoffire.placebo.patreon.TrailsManager;
import dev.shadowsoffire.placebo.patreon.WingsManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.text.WordUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(Player.class)
public class PlayerMixin {

    @Unique
    private static int counter = 0;

    @Environment(EnvType.CLIENT)
    @Inject(method = "tick", at = @At("TAIL"))
    private void patreonWings(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (player.level().isClientSide) {
            if (player.tickCount >= 200) {
                if (player.tickCount % 150 == 0) {
                    Minecraft mc = Minecraft.getInstance();
                    if (PatreonPreview.PARTICLES) {
                        PatreonUtils.PatreonParticleType[] arr = PatreonUtils.PatreonParticleType.values();
                        PatreonUtils.PatreonParticleType p = arr[counter++ % arr.length];
                        Component type = Component.literal(WordUtils.capitalize(p.name().toLowerCase(Locale.ROOT).replace('_', ' ')));
                        mc.gui.setTimes(0, 40, 20);
                        mc.gui.setSubtitle(type);
                        mc.gui.setTitle(Component.literal(""));
                        TrailsManager.TRAILS.put(player.getUUID(), p);
                    }
                    else if (PatreonPreview.WINGS) {
                        PatreonUtils.WingType[] arr = PatreonUtils.WingType.values();
                        PatreonUtils.WingType p = arr[counter++ % arr.length];
                        Component type = Component.literal(WordUtils.capitalize(p.name().toLowerCase(Locale.ROOT).replace('_', ' ')));
                        mc.gui.setTimes(0, 40, 20);
                        mc.gui.setSubtitle(type);
                        mc.gui.setTitle(Component.literal(""));
                        WingsManager.WINGS.put(player.getUUID(), p);
                    }
                }
            }
        }
    }

}
