package dev.shadowsoffire.placebo.mixin.getters;

import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(TextColor.class)
public interface TextColorGetter {

    @Accessor
    public static Map<String, TextColor> getNAMED_COLORS() {
        throw new AssertionError();
    }

    @Accessor
    @Mutable
    public static void setNAMED_COLORS(Map<String, TextColor> NAMED_COLORS) {
        throw new AssertionError();
    }
}
