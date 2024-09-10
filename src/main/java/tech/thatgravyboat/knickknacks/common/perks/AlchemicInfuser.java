package tech.thatgravyboat.knickknacks.common.perks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import tech.thatgravyboat.knickknacks.common.perks.base.Perk;
import tech.thatgravyboat.knickknacks.common.registries.ModItems;

import java.util.List;

public record AlchemicInfuser(ItemStack stack) implements Perk {

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips) {
        addModifierTooltip(tooltips);
        int percent = switch (stack.getOrDefault(DataComponents.RARITY, Rarity.COMMON)) {
            case EPIC -> 50;
            case RARE -> 40;
            case UNCOMMON -> 25;
            default -> 10;
        };
        tooltips.add(Component.translatable("knickknacks.tooltip.alchemic_infuser.1", percent).withStyle(ChatFormatting.BLUE));
        return tooltips;
    }

    public static int onPotionDrank(MobEffectInstance instance, Player player) {
        Rarity rarity = Perk.getRarity(player, ModItems.ALCHEMIC_INFUSER);
        if (rarity == null) return instance.getDuration();
        float percentageIncrease = switch (rarity) {
            case EPIC -> 1.5f;
            case RARE -> 1.4f;
            case UNCOMMON -> 1.25f;
            default -> 1.1f;
        };
        return (int) (instance.getDuration() * percentageIncrease);
    }
}
