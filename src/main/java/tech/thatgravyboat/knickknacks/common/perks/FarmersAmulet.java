package tech.thatgravyboat.knickknacks.common.perks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockEvent;
import tech.thatgravyboat.knickknacks.common.perks.base.Perk;
import tech.thatgravyboat.knickknacks.common.registries.ModItems;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public record FarmersAmulet(ItemStack stack) implements Perk {

    private float getGrowthChance(Rarity rarity) {
        return switch (rarity) {
            case EPIC -> 0.05f;
            case RARE -> 0.025f;
            case UNCOMMON -> 0.01f;
            default -> 0.005f;
        };
    }

    @Override
    public List<Component> getAttributesTooltip(List<Component> tooltips) {
        addModifierTooltip(tooltips);
        float chance = getGrowthChance(stack.getOrDefault(DataComponents.RARITY, Rarity.COMMON)) * 100f;
        tooltips.add(Component.translatable("knickknacks.tooltip.farmers_amulet.1", chance).withStyle(ChatFormatting.DARK_GREEN));
        return tooltips;
    }

    @Override
    public void curioTick(SlotContext context) {
        Entity entity = context.entity();
        if (!(entity.level() instanceof ServerLevel level)) return;
        Rarity rarity = stack.getOrDefault(DataComponents.RARITY, Rarity.COMMON);
        float chance = getGrowthChance(rarity);
        RandomSource random = level.getRandom();
        if (random.nextFloat() >= chance) return;
        BlockPos pos1 = entity.blockPosition().offset(-3, -3, -3);
        BlockPos pos2 = entity.blockPosition().offset(3, 3, 3);
        for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            if (!(block instanceof BonemealableBlock bonemealable)) continue;
            if (bonemealable.getType() == BonemealableBlock.Type.NEIGHBOR_SPREADER) continue;
            if (!state.is(BlockTags.CROPS)) continue;
            bonemealable.performBonemeal(level, random, pos, state);
        }
    }

    public static void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!Perk.hasPerk(player, ModItems.FARMERS_AMULET)) return;
        event.setCanceled(true);
    }
}
