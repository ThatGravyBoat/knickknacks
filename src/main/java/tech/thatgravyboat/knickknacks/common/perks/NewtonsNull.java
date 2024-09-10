package tech.thatgravyboat.knickknacks.common.perks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import tech.thatgravyboat.knickknacks.common.perks.base.Perk;
import tech.thatgravyboat.knickknacks.common.registries.ModItems;

import java.util.List;

public record NewtonsNull(ItemStack stack) implements Perk {

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips) {
        addModifierTooltip(tooltips);
        int percent = switch (stack.getOrDefault(DataComponents.RARITY, Rarity.COMMON)) {
            case UNCOMMON -> 25;
            case RARE -> 50;
            case EPIC -> 100;
            default -> 10;
        };
        tooltips.add(Component.translatable("knickknacks.tooltip.newtons_null.1", percent).withStyle(ChatFormatting.RED));
        return tooltips;
    }

    public static void onKnockback(LivingKnockBackEvent event) {
        LivingEntity source = event.getEntity().getKillCredit();
        if (!(source instanceof Player player)) return;
        ItemStack perk = Perk.getPerk(player, ModItems.NEWTONS_NULL);
        if (perk.isEmpty()) return;
        float percent = switch (perk.getOrDefault(DataComponents.RARITY, Rarity.COMMON)) {
            case UNCOMMON -> 0.75f;
            case RARE -> 0.5f;
            case EPIC -> 1f;
            default -> 0.9f;
        };
        if (percent == 1f) {
            event.setCanceled(true);
        } else {
            event.setStrength(event.getStrength() * percent);
        }
    }
}
