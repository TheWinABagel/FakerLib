package dev.shadowsoffire.placebo.mixin;

import dev.shadowsoffire.placebo.events.AnvilLandCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {

    @Inject(at = @At("TAIL"), method = "onLand", require = 1)
    public void onLand(Level level, BlockPos pos, BlockState newState, BlockState oldState, FallingBlockEntity entity, CallbackInfo ci) {
        AnvilLandCallback.EVENT.invoker().onLand(level, pos, newState, oldState, entity);
    }

}
