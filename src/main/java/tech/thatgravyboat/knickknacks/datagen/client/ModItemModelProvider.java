package tech.thatgravyboat.knickknacks.datagen.client;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import tech.thatgravyboat.knickknacks.Knickknacks;
import tech.thatgravyboat.knickknacks.common.registries.ModItems;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Knickknacks.MODID, helper);
    }

    @Override
    protected void registerModels() {
        for (var entry : ModItems.ITEMS.getEntries()) {
            ResourceLocation id = entry.getId();
            singleTexture(
                    id.getPath(),
                    mcLoc("item/generated"),
                    "layer0",
                    modLoc("item/" + id.getPath())
            );
        }
    }
}
