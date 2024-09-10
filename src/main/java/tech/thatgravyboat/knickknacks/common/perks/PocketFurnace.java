package tech.thatgravyboat.knickknacks.common.perks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import tech.thatgravyboat.knickknacks.common.perks.base.Perk;
import tech.thatgravyboat.knickknacks.common.registries.ModItems;

import java.util.List;

public record PocketFurnace(ItemStack stack) implements Perk {

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips) {
        int xpPercent = switch (stack.getOrDefault(DataComponents.RARITY, Rarity.COMMON)) {
            case EPIC -> 100;
            case RARE -> 50;
            case UNCOMMON -> 25;
            default -> 10;
        };

        addModifierTooltip(tooltips);
        tooltips.add(Component.translatable("knickknacks.tooltip.pocket_furnace.1").withStyle(ChatFormatting.DARK_GREEN));
        tooltips.add(Component.translatable("knickknacks.tooltip.pocket_furnace.2", xpPercent).withStyle(ChatFormatting.BLUE));
        return tooltips;
    }

    private static float smelt(ItemEntity item) {
        Level level = item.level();
        ItemStack stack = item.getItem();
        SingleRecipeInput input = new SingleRecipeInput(stack);
        BlastingRecipe recipe = level.getRecipeManager()
                .getRecipeFor(RecipeType.BLASTING, input, level)
                .map(RecipeHolder::value)
                .orElse(null);
        if (recipe == null) return 0;
        ItemStack output = recipe.assemble(input, level.registryAccess());
        if (output.isEmpty()) return 0;
        item.setItem(output.copyWithCount(output.getCount() * stack.getCount()));
        return recipe.getExperience() * stack.getCount();
    }

    public static void onBlockDrops(BlockDropsEvent event) {
        if (!(event.getBreaker() instanceof Player player)) return;
        Rarity rarity = Perk.getRarity(player, ModItems.POCKET_FURNACE);
        if (rarity == null) return;
        float experience = 0f;
        for (ItemEntity drop : event.getDrops()) {
            experience += smelt(drop);
        }
        experience *= switch (rarity) {
            case EPIC -> 2;
            case RARE -> 1.5f;
            case UNCOMMON -> 1.25f;
            default -> 1f;
        };
        event.setDroppedExperience((int) (event.getDroppedExperience() + experience));
    }
}
