package dev.shadowsoffire.placebo.mixin;

import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.patreon.WingsManager;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Shadow private Map<String, EntityRenderer<? extends Player>> playerRenderers; //TODO swap to LivingEntityFeatureRendererRegistrationCallback

    @Inject(method = "onResourceManagerReload", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void addTrailsAndWings(ResourceManager resourceManager, CallbackInfo ci, EntityRendererProvider.Context context) {
        WingsManager.addLayers(context, this.playerRenderers);
    }
}