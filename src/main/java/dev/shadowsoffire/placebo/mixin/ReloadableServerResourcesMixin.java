package dev.shadowsoffire.placebo.mixin;

import dev.shadowsoffire.placebo.events.ReloadableServerEvent;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Collections;
import java.util.List;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {

    @ModifyArgs(method = "loadResources", at = @At(value = "INVOKE", target = "net/minecraft/server/packs/resources/SimpleReloadInstance.create (Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/server/packs/resources/ReloadInstance;"))
    private static void fakerlib$addListeners(Args args) {
        List<PreparableReloadListener> listeners = new java.util.ArrayList<>(args.get(1));
        listeners.addAll(ReloadableServerEvent.list);
        args.set(1, Collections.unmodifiableList(listeners));
    }
}
