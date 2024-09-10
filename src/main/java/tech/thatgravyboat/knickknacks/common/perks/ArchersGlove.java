package tech.thatgravyboat.knickknacks.common.perks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import tech.thatgravyboat.knickknacks.common.perks.base.Perk;
import tech.thatgravyboat.knickknacks.common.registries.ModItems;

import java.util.List;

public record ArchersGlove(ItemStack stack) implements Perk {

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips) {
        addModifierTooltip(tooltips);
        int speed = switch (stack.getOrDefault(DataComponents.RARITY, Rarity.COMMON)) {
            case UNCOMMON -> 3;
            case RARE -> 5;
            case EPIC -> 7;
            default -> 2;
        };
        tooltips.add(Component.translatable("knickknacks.tooltip.archers_glove.1", speed).withStyle(ChatFormatting.DARK_GREEN));
        return tooltips;
    }

    public static void onBowUse(LivingEntityUseItemEvent.Tick event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!event.getItem().is(Tags.Items.TOOLS_BOW)) return;
        ItemStack perk = Perk.getPerk(player, ModItems.ARACHERS_GLOVE);
        if (perk.isEmpty()) return;
        int speed = switch (perk.getOrDefault(DataComponents.RARITY, Rarity.COMMON)) {
            case UNCOMMON -> 2;
            case RARE -> 4;
            case EPIC -> 6;
            default -> 1;
        };
        event.setDuration(Math.max(event.getDuration() - speed, 0));
    }
}
