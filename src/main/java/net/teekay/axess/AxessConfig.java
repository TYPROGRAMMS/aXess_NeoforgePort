package net.teekay.axess;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.ModContainer;

public class AxessConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.IntValue MAX_READER_POWERED_TICKS = BUILDER.comment("The maximum amount a reader can stay on for with the PULSE option selected.").defineInRange("max_reader_powered_ticks", 200, 1, 20*60);
    private static final ModConfigSpec.IntValue MAX_NETWORKS_PER_PLAYER = BUILDER.comment("The maximum amount of networks a player can own.").defineInRange("max_networks_per_player", 5, 1, 20);
    private static final ModConfigSpec.IntValue MAX_LEVELS_PER_NETWORK = BUILDER.comment("The maximum amount of access levels a network can have.").defineInRange("max_levels_per_network", 20, 1, 100);
    private static final ModConfigSpec.IntValue OP_MAX_NETWORKS_PER_PLAYER = BUILDER.comment("The maximum amount of networks operators can own.").defineInRange("op_max_networks_per_player", 10, 1, 20);
    private static final ModConfigSpec.IntValue OP_MAX_LEVELS_PER_NETWORK = BUILDER.comment("The maximum amount of access levels a network can have (for server operators).").defineInRange("op_max_levels_per_network", 40, 1, 100);
    private static final ModConfigSpec.IntValue MAX_LINK_DIST = BUILDER.comment("The maximum distance there can be between two linked devices.").defineInRange("max_link_distance", 32, 1, 100);
    private static final ModConfigSpec.IntValue MAX_LINKS_READER = BUILDER.comment("The maximum amount of links a reader can have.").defineInRange("max_links_reader", 5, 0, 20);
    private static final ModConfigSpec.BooleanValue EXPERIMENTAL_LET_EVERYONE_EDIT_EVERYTHING = BUILDER.comment("[EXPERIMENTAL] ENABLE THIS ONLY IF YOU WANT EVERYONE TO BE ABLE TO EDIT EVERY NETWORK (DANGEROUS)").define("experimental_let_everyone_edit_everything", false);
    public static final ModConfigSpec SPEC = BUILDER.build();
    public static int maxReaderPoweredTicks;
    private static int maxNetworksPerPlayer, maxLevelsPerNetwork, opMaxNetworksPerPlayer, opMaxLevelsPerNetwork;
    public static int maxLinkDistance, maxLinksReader;
    public static boolean experimentalLetEveryoneEditEverything;

    public static void registerConfig(ModContainer container) {
        container.registerConfig(ModConfig.Type.SERVER, SPEC);
    }
    public static void onConfigLoading(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) loadValues();
    }
    private static void loadValues() {
        maxReaderPoweredTicks = MAX_READER_POWERED_TICKS.get();
        maxNetworksPerPlayer = MAX_NETWORKS_PER_PLAYER.get();
        maxLevelsPerNetwork = MAX_LEVELS_PER_NETWORK.get();
        opMaxNetworksPerPlayer = OP_MAX_NETWORKS_PER_PLAYER.get();
        opMaxLevelsPerNetwork = OP_MAX_LEVELS_PER_NETWORK.get();
        maxLinkDistance = MAX_LINK_DIST.get();
        maxLinksReader = MAX_LINKS_READER.get();
        experimentalLetEveryoneEditEverything = EXPERIMENTAL_LET_EVERYONE_EDIT_EVERYTHING.get();
    }
    public static int getPlayerMaxNetworks(Player player) { return player.hasPermissions(4) ? opMaxNetworksPerPlayer : maxNetworksPerPlayer; }
    public static int getPlayerMaxLevelsPerNetwork(Player player) { return player.hasPermissions(4) ? opMaxLevelsPerNetwork : maxLevelsPerNetwork; }
}
