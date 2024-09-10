package tech.thatgravyboat.knickknacks.common.perks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import tech.thatgravyboat.knickknacks.common.perks.base.Perk;
import tech.thatgravyboat.knickknacks.common.registries.ModDataComponents;
import tech.thatgravyboat.knickknacks.common.registries.ModItems;

import java.util.List;

public record KillersEye(ItemStack stack) implements Perk {

    private static float getMaxPercent(ItemStack stack) {
        return switch (stack.getOrDefault(DataComponents.RARITY, Rarity.COMMON)) {
            case UNCOMMON -> 0.10f;
            case RARE -> 0.15f;
            case EPIC -> 0.25f;
            default -> 0.05f;
        };
    }

    private static int getMobsKilled(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.KILLERS_EYE_DATA, 0);
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips) {
        int maxMobs = (int) (Math.floor(getMaxPercent(stack) * 100) / 5 * 1000);
        tooltips.add(Component.empty());
        tooltips.add(Component.translatable("knickknacks.tooltip.killers_eye.1", getMobsKilled(stack), maxMobs).withStyle(ChatFormatting.GRAY));

        addModifierTooltip(tooltips);
        tooltips.add(Component.translatable("knickknacks.tooltip.killers_eye.2").withStyle(ChatFormatting.BLUE));
        return tooltips;
    }

    public static void onDamage(LivingIncomingDamageEvent event) {
        if (!(event.getSource().getDirectEntity() instanceof Player player)) return;
        ItemStack perk = Perk.getPerk(player, ModItems.KILLERS_EYE);
        if (perk.isEmpty()) return;
        int mobsKilled = getMobsKilled(perk);
        if (mobsKilled < 1000) return;
        float percent = Math.min(mobsKilled / 1000f * 0.05f, getMaxPercent(perk));
        event.setAmount(event.getAmount() * (1 + percent));
    }

    public static void onKill(LivingDeathEvent event) {
        if (!(event.getSource().getDirectEntity() instanceof Player player)) return;
        ItemStack perk = Perk.getPerk(player, ModItems.KILLERS_EYE);
        if (perk.isEmpty()) return;
        perk.set(ModDataComponents.KILLERS_EYE_DATA, getMobsKilled(perk) + 1);
    }
}
