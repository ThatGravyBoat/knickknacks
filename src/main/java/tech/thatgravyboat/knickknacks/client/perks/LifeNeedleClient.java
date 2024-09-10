package tech.thatgravyboat.knickknacks.client.perks;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import tech.thatgravyboat.knickknacks.common.perks.base.Perk;
import tech.thatgravyboat.knickknacks.common.registries.ModItems;

public enum LifeNeedleClient {
    INSTANCE;

    private static boolean isEstradiol(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (!stack.is(ModItems.LIFE_NEEDLE)) return false;
        return stack.getHoverName().getString().equalsIgnoreCase("Estradiol");
    }

    private static Component makeTrans(FormattedText component) {
        String text = component.getString();
        if (text.length() < 5) return Component.literal(text);
        String[] sections = new String[5];
        int length = Math.ceilDiv(text.length(), 5);
        int start = 0;
        sections[0] = text.substring(start, (start += length));
        sections[1] = text.substring(start, (start += length));
        sections[2] = text.substring(start, text.length() % 2 == 0 ? (start += length) : (start += length - 1));
        sections[3] = text.substring(start, (start += length));
        sections[4] = text.substring(start);
        return Component.empty()
                .append(Component.literal(sections[0]).withColor(0x5BCEFA))
                .append(Component.literal(sections[1]).withColor(0xF5A9B8))
                .append(Component.literal(sections[2]).withColor(0xFFFFFF))
                .append(Component.literal(sections[3]).withColor(0xF5A9B8))
                .append(Component.literal(sections[4]).withColor(0x5BCEFA));
    }

    @SubscribeEvent
    public void onRenderNameTag(RenderNameTagEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ItemStack perk = Perk.getPerk(player, ModItems.LIFE_NEEDLE);
        if (!isEstradiol(perk)) return;
        event.setContent(makeTrans(event.getContent()));
    }

    @SubscribeEvent
    public void onRenderTooltipColor(RenderTooltipEvent.Color event) {
        if (!isEstradiol(event.getItemStack())) return;
        event.setBorderStart(0xFF5BCEFA);
        event.setBorderEnd(0xFFF5A9B8);
    }

    @SubscribeEvent
    public void onRenderTooltipText(RenderTooltipEvent.GatherComponents event) {
        if (!isEstradiol(event.getItemStack())) return;
        var components = event.getTooltipElements();
        if (components.isEmpty()) return;
        components.set(0, components.getFirst().mapLeft(LifeNeedleClient::makeTrans));
    }
}
