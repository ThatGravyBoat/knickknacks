package tech.thatgravyboat.knickknacks.common.perks;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import tech.thatgravyboat.knickknacks.Knickknacks;
import tech.thatgravyboat.knickknacks.common.perks.base.Perk;
import top.theillusivec4.curios.api.SlotContext;

public record OrbOfWellness(ItemStack stack) implements Perk {

    private static final ResourceLocation ID = Knickknacks.id("orb_of_wellness");

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id) {
        Multimap<Holder<Attribute>, AttributeModifier> attributes = LinkedHashMultimap.create();
        double modifier = 5.0 * (stack.getOrDefault(DataComponents.RARITY, Rarity.COMMON).ordinal() + 1);
        attributes.put(
                Attributes.MAX_HEALTH,
                new AttributeModifier(ID, modifier, AttributeModifier.Operation.ADD_VALUE)
        );
        return attributes;
    }
}
