package tech.thatgravyboat.knickknacks.common.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.knickknacks.common.perks.base.Perk;

import java.util.List;
import java.util.function.Function;

public class PerkItem extends Item {

    private final Function<ItemStack, Perk> factory;

    public PerkItem(Function<ItemStack, Perk> factory) {
        super(new Properties().stacksTo(1));
        this.factory = factory;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        Rarity rarity = stack.getOrDefault(DataComponents.RARITY, Rarity.COMMON);
        Component rarityText = Component.translatable("perk.rarity." + rarity.getSerializedName())
                .withStyle(rarity.getStyleModifier())
                .withStyle(ChatFormatting.BOLD);

        components.add(CommonComponents.EMPTY);
        components.add(rarityText);
    }

    public Perk createPerk(ItemStack stack) {
        return factory.apply(stack);
    }
}
