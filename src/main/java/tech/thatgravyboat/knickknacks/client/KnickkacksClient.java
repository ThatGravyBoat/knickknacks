package tech.thatgravyboat.knickknacks.client;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import tech.thatgravyboat.knickknacks.Knickknacks;
import tech.thatgravyboat.knickknacks.client.perks.LifeNeedleClient;
import tech.thatgravyboat.knickknacks.client.perks.MinersGogglesClient;

@Mod(Knickknacks.MODID)
public class KnickkacksClient {

    public KnickkacksClient() {
        NeoForge.EVENT_BUS.register(MinersGogglesClient.INSTANCE);
        NeoForge.EVENT_BUS.register(LifeNeedleClient.INSTANCE);
    }
}
