package tech.thatgravyboat.knickknacks.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import tech.thatgravyboat.knickknacks.Knickknacks;
import tech.thatgravyboat.knickknacks.datagen.client.ModItemModelProvider;
import tech.thatgravyboat.knickknacks.datagen.common.ModLootModifiersProvider;
import tech.thatgravyboat.knickknacks.datagen.common.ModLootTableProvider;
import tech.thatgravyboat.knickknacks.datagen.common.ModRecipeProvider;

import java.util.List;
import java.util.Set;

@Mod(Knickknacks.MODID)
public class KnickknacksData {

    public KnickknacksData(IEventBus bus) {
        bus.addListener(this::onGatherData);
    }

    private void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output, event.getLookupProvider()));
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(ModLootTableProvider::new, LootContextParamSets.EMPTY)
        ), event.getLookupProvider()));
        generator.addProvider(event.includeServer(), new ModLootModifiersProvider(output, event.getLookupProvider()));
    }
}
