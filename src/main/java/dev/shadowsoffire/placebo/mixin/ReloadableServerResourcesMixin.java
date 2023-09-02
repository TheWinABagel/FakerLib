package dev.shadowsoffire.placebo.mixin;

import dev.shadowsoffire.placebo.events.ReloadableServerEvent;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {

    @Inject(method = "listeners", at = @At("RETURN"), cancellable = true)
    private void addListeners(CallbackInfoReturnable<List<PreparableReloadListener>> cir){
        List<PreparableReloadListener> listeners = new java.util.ArrayList<>(cir.getReturnValue());
        List<PreparableReloadListener> customListeners = ReloadableServerEvent.list;
        if (customListeners != null){
            listeners.addAll(customListeners);
        }
        cir.setReturnValue(listeners);
    }

}
