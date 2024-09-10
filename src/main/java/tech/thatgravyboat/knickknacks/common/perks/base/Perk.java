package tech.thatgravyboat.knickknacks.common.perks.base;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;

public interface Perk extends ICurio {

    ItemStack stack();

    @Override
    default ItemStack getStack() {
        return stack();
    }

    default void addModifierTooltip(List<Component> tooltips) {
        tooltips.add(Component.empty());
        tooltips.add(Component.translatable("curios.modifiers.knickknack").withStyle(ChatFormatting.GOLD));
    }

    static ItemStack getPerk(Player player, ItemLike item) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(inventory -> inventory.findFirstCurio(item.asItem()))
                .map(SlotResult::stack)
                .orElse(ItemStack.EMPTY);
    }

    static boolean hasPerk(Player player, ItemLike item) {
        return !getPerk(player, item).isEmpty();
    }

    static Rarity getRarity(Player player, ItemLike item) {
        ItemStack stack = getPerk(player, item);
        if (stack.isEmpty()) return null;
        return stack.getOrDefault(DataComponents.RARITY, Rarity.COMMON);
    }
}
