package tech.thatgravyboat.knickknacks.common.perks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import tech.thatgravyboat.knickknacks.common.perks.base.Perk;
import tech.thatgravyboat.knickknacks.common.registries.ModItems;

import java.util.List;

public record LifeNeedle(ItemStack stack) implements Perk {

    private static final float LIFESTEAL_PERCENT = 0.05f;

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips) {
        addModifierTooltip(tooltips);
        tooltips.add(Component.translatable("knickknacks.tooltip.life_needle.1", LIFESTEAL_PERCENT).withStyle(ChatFormatting.DARK_GREEN));
        Rarity rarity = stack.getOrDefault(DataComponents.RARITY, Rarity.COMMON);
        float maxHeal = (rarity.ordinal() + 1) * 0.25f;
        tooltips.add(Component.translatable("knickknacks.tooltip.life_needle.2", maxHeal).withStyle(ChatFormatting.DARK_GREEN));
        return tooltips;
    }

    private static void setCooldown(Player player, Rarity rarity) {
        ItemCooldowns cooldowns = player.getCooldowns();
        cooldowns.addCooldown(ModItems.LIFE_NEEDLE.get(), switch (rarity) {
            case EPIC -> 20;
            case RARE -> 30;
            case UNCOMMON -> 40;
            default -> 60;
        });
    }

    public static void onDamage(LivingDamageEvent.Post event) {
        if (!(event.getSource().getDirectEntity() instanceof Player player)) return;
        if (!event.getSource().is(DamageTypes.PLAYER_ATTACK)) return;
        ItemStack perk = Perk.getPerk(player, ModItems.LIFE_NEEDLE);
        if (perk.isEmpty()) return;
        if (player.getCooldowns().isOnCooldown(perk.getItem())) return;
        Rarity rarity = perk.getRarity();
        setCooldown(player, rarity);
        float maxHeal = (rarity.ordinal() + 1) * 0.25f;
        player.heal(Math.min(event.getNewDamage() * LIFESTEAL_PERCENT, maxHeal));
    }
}
