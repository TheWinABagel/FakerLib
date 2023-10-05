package dev.shadowsoffire.placebo.loot;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Arrays;

public class PoolBuilder extends LootPool.Builder {

    static int k = 0;

    public PoolBuilder(int minRolls, int maxRolls) {
        this.setRolls(UniformGenerator.between(minRolls, maxRolls));
         this.name("fakerlib_code_pool_" + k++);
    }

    public PoolBuilder addEntries(LootPoolEntryContainer... entries) {
        this.entries.addAll(Arrays.asList(entries));
        return this;
    }

    public PoolBuilder addCondition(LootItemCondition... conditions) {
        this.conditions.addAll(Arrays.asList(conditions));
        return this;
    }

    public PoolBuilder addFunc(LootItemFunction... conditions) {
        this.functions.addAll(Arrays.asList(conditions));
        return this;
    }

}
