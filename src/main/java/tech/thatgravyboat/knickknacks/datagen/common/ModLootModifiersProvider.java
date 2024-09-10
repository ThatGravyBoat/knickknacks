package tech.thatgravyboat.knickknacks.datagen.common;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import tech.thatgravyboat.knickknacks.Knickknacks;

import java.util.concurrent.CompletableFuture;

public class ModLootModifiersProvider extends GlobalLootModifierProvider {

    public ModLootModifiersProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, Knickknacks.MODID);
    }

    @Override
    protected void start() {
        add("knickknack_perks", new AddTableLootModifier(
                new LootItemCondition[] {
                        AnyOfCondition.anyOf(
                                LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY.location()),
                                LootTableIdCondition.builder(BuiltInLootTables.BASTION_TREASURE.location()),
                                LootTableIdCondition.builder(BuiltInLootTables.END_CITY_TREASURE.location())
                        ).build()
                },
                ResourceKey.create(Registries.LOOT_TABLE, Knickknacks.id("perks"))
        ));
    }
}
