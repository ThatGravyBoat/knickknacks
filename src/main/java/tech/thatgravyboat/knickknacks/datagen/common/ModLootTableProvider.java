package tech.thatgravyboat.knickknacks.datagen.common;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import tech.thatgravyboat.knickknacks.Knickknacks;
import tech.thatgravyboat.knickknacks.common.items.PerkItem;
import tech.thatgravyboat.knickknacks.common.registries.ModItems;

import java.util.function.BiConsumer;

public record ModLootTableProvider(HolderLookup.Provider registries) implements LootTableSubProvider {

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        LootPool.Builder builder = LootPool.lootPool()
                .name("Knickknacks Perks")
                .setRolls(UniformGenerator.between(0.0f, 1.0f));

        for (DeferredHolder<Item, ? extends Item> entry : ModItems.ITEMS.getEntries()) {
            if (entry.get() instanceof PerkItem item) {
                builder.add(LootItem.lootTableItem(item).when(LootItemRandomChanceCondition.randomChance(0.05f)));
            }
        }

        output.accept(
                ResourceKey.create(Registries.LOOT_TABLE, Knickknacks.id("perks")),
                LootTable.lootTable()
                        .withPool(builder)
                        .setParamSet(LootContextParamSet.builder().build())
        );
    }
}
