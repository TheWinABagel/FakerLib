package dev.shadowsoffire.placebo.mixin;

import dev.shadowsoffire.placebo.reload.DynamicRegistry;
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
    public void listeners(CallbackInfoReturnable<List<PreparableReloadListener>> cir) {
        List<PreparableReloadListener> list = cir.getReturnValue();
      //  DynamicRegistry.addReloader(list);
        cir.setReturnValue(list);
    }
}
