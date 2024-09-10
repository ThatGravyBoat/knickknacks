package tech.thatgravyboat.knickknacks.client.perks;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import tech.thatgravyboat.knickknacks.client.ModRenderTypes;
import tech.thatgravyboat.knickknacks.common.events.BlockChangeEvent;
import tech.thatgravyboat.knickknacks.common.perks.base.Perk;
import tech.thatgravyboat.knickknacks.common.registries.ModDataComponents;
import tech.thatgravyboat.knickknacks.common.registries.ModItems;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public enum MinersGogglesClient {
    INSTANCE;

    private final Set<BlockPos> positions = new HashSet<>();
    private final Set<BlockPos> toRender = new HashSet<>();
    private final Set<ChunkPos> chunks = new HashSet<>();
    private Block toCheck = Blocks.AIR;
    private ResourceKey<Level> lastDimension = null;

    private boolean hasBlock() {
        return this.toCheck != Blocks.AIR;
    }

    private void addPositions(Set<BlockPos> positions) {
        if (!hasBlock()) return;
        this.positions.addAll(positions);
    }
    
    private void checkChunk(ChunkAccess chunk) {
        if (!hasBlock()) return;
        Set<BlockPos> positions = new HashSet<>();
        chunk.findBlocks(state -> hasBlock() && state.is(this.toCheck), (pos, state) -> positions.add(pos.immutable()));
        nextTick(() -> addPositions(positions));
    }

    private void checkAllChunks() {
        if (!hasBlock()) return;
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        this.chunks.forEach(pos -> checkChunk(level.getChunk(pos.x, pos.z)));
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ClientLevel)) return;
        nextTick(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null && this.lastDimension != level.dimension()) {
                this.lastDimension = level.dimension();
                this.positions.clear();
                this.chunks.clear();
            }
            CompletableFuture.runAsync(() -> checkChunk(event.getChunk()));
            this.chunks.add(event.getChunk().getPos());
        });
    }

    @SubscribeEvent
    public void onChunkUnload(ChunkEvent.Unload event) {
        if (!(event.getLevel() instanceof ClientLevel)) return;
        nextTick(() -> {
            this.chunks.remove(event.getChunk().getPos());
            this.positions.removeIf(pos -> event.getChunk().getPos().equals(new ChunkPos(pos)));
        });
    }

    @SubscribeEvent
    public void onBlockChange(BlockChangeEvent event) {
        if (!(event.getLevel() instanceof ClientLevel)) return;
        nextTick(() -> {
            if (event.getState().is(this.toCheck)) {
                this.positions.add(event.getPos());
            } else {
                this.positions.remove(event.getPos());
            }
        });
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack perk = Perk.getPerk(player, ModItems.MINERS_GOGGLES);
        if (perk.isEmpty()) {
            this.toCheck = Blocks.AIR;
            this.positions.clear();
        } else {
            Block toCheck = perk.getOrDefault(ModDataComponents.MINERS_GOGGLES_DATA, Blocks.AIR);
            if (toCheck != this.toCheck) {
                this.toCheck = toCheck;
                this.positions.clear();
                checkAllChunks();
            }
        }

        this.toRender.clear();
        int range = switch (perk.getOrDefault(DataComponents.RARITY, Rarity.COMMON)) {
            case EPIC -> 8192;
            case RARE -> 4096;
            case UNCOMMON -> 2048;
            default -> 1024;
        };

        BlockPos playerPos = player.blockPosition();
        for (BlockPos position : this.positions) {
            if (position.distSqr(playerPos) < range && player.level().isLoaded(position)) {
                this.toRender.add(position);
            }
        }
    }

    @SubscribeEvent
    public void onLevelRender(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
            );
            VertexConsumer consumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(ModRenderTypes.NO_DEPTH_BLOCK_FILL);
            Vec3 camera = event.getCamera().getPosition();
            PoseStack poseStack = event.getPoseStack();
            for (BlockPos pos : this.toRender) {
                Vec3 offset = Vec3.atLowerCornerOf(pos).subtract(camera);

                poseStack.pushPose();
                poseStack.translate(offset.x, offset.y, offset.z);
                LevelRenderer.addChainedFilledBoxVertices(
                        poseStack, consumer,
                        -0.005, -0.005, -0.005,
                        1.005, 1.005, 1.005,
                        1.0f, 1.0f, 1.0f, 20f/255f
                );
                poseStack.popPose();
            }
        }
    }

    private void nextTick(Runnable operation) {
        Minecraft.getInstance().tell(operation);
    }
}
