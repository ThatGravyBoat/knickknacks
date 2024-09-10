package tech.thatgravyboat.knickknacks.common.perks;

import net.minecraft.ChatFormatting;
import net.minecraft.Optionull;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import tech.thatgravyboat.knickknacks.common.perks.base.Perk;
import tech.thatgravyboat.knickknacks.common.registries.ModDataComponents;
import tech.thatgravyboat.knickknacks.common.registries.ModItems;

import java.util.List;

public record MinersGoggles(ItemStack stack) implements Perk {

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips) {
        Component block = Optionull.mapOrDefault(
                stack.get(ModDataComponents.MINERS_GOGGLES_DATA),
                Block::getName,
                Component.translatable("knickknacks.tooltip.misc.nothing")
        );
        tooltips.add(Component.empty());
        tooltips.add(Component.translatable("knickknacks.tooltip.miners_goggles.1", block).withStyle(ChatFormatting.GRAY));

        addModifierTooltip(tooltips);
        int range = switch (stack.getOrDefault(DataComponents.RARITY, Rarity.COMMON)) {
            case EPIC -> 8192;
            case RARE -> 4096;
            case UNCOMMON -> 2048;
            default -> 1024;
        };
        tooltips.add(Component.translatable("knickknacks.tooltip.miners_goggles.2", range).withStyle(ChatFormatting.DARK_GREEN));
        return tooltips;
    }

    public static void useOnBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getItemStack().is(ModItems.MINERS_GOGGLES)) return;
        BlockState state = event.getLevel().getBlockState(event.getPos());
        if (state.isAir()) return;
        if (!state.is(Tags.Blocks.ORES)) return;
        event.getItemStack().set(ModDataComponents.MINERS_GOGGLES_DATA, state.getBlock());
        event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide()));
        event.setCanceled(true);
    }

    public static void useInAir(PlayerInteractEvent.RightClickItem event) {
        if (!event.getItemStack().is(ModItems.MINERS_GOGGLES)) return;
        if (!event.getEntity().isShiftKeyDown()) return;
        event.getItemStack().remove(ModDataComponents.MINERS_GOGGLES_DATA);
        event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide()));
        event.setCanceled(true);
    }
}
