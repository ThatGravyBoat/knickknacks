package tech.thatgravyboat.knickknacks.datagen.common;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.knickknacks.Knickknacks;
import tech.thatgravyboat.knickknacks.common.items.PerkItem;
import tech.thatgravyboat.knickknacks.common.registries.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    private static final Rarity[] RARITIES = {
            Rarity.COMMON,
            Rarity.UNCOMMON,
            Rarity.RARE,
            Rarity.EPIC
    };

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        Item[] perkItems = ModItems.ITEMS.getEntries().stream()
                .map(DeferredHolder::get)
                .filter(entry -> entry instanceof PerkItem)
                .toArray(Item[]::new);
        for (int i = 0; i < RARITIES.length; i++) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SCRAP, i + 1)
                    .requires(ModItems.HAMMER)
                    .requires(DataComponentIngredient.of(false, DataComponents.RARITY, RARITIES[i], perkItems))
                    .unlockedBy("has_item", has(ModItems.HAMMER))
                    .save(output, Knickknacks.id("scrap_%s".formatted(RARITIES[i].getSerializedName())));
        }

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.UPGRADE, 1)
                .define('S', ModItems.SCRAP)
                .define('A', Items.AMETHYST_SHARD)
                .pattern(" S ")
                .pattern("SAS")
                .pattern(" S ")
                .unlockedBy("has_item", has(ModItems.HAMMER))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HAMMER, 1)
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .pattern(" IS")
                .pattern(" SI")
                .pattern("S  ")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(output);

        for (var entry : ModItems.ITEMS.getEntries()) {
            if (entry.get() instanceof PerkItem item) {
                for (int i = 0; i < RARITIES.length - 1; i++) {
                    var rarity = RARITIES[i];
                    var nextRarity = RARITIES[i + 1];
                    ItemStack itemStack = new ItemStack(item);
                    itemStack.set(DataComponents.RARITY, nextRarity);
                    output.accept(
                            Knickknacks.id("transform_%s_%s_to_%s".formatted(
                                    entry.getId().getPath(), rarity.getSerializedName(), nextRarity.getSerializedName()
                            )),
                            new SmithingTransformRecipe(
                                    Ingredient.of(ModItems.UPGRADE),
                                    DataComponentIngredient.of(false, DataComponents.RARITY, rarity, item),
                                    Ingredient.EMPTY,
                                    itemStack
                            ),
                            null
                    );
                }
            }
        }
    }
}
