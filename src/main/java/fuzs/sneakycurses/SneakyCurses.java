package fuzs.sneakycurses;

import fuzs.puzzleslib.api.event.v1.entity.living.LivingEvents;
import fuzs.puzzleslib.api.event.v1.entity.player.AnvilUpdateCallback;
import fuzs.puzzleslib.PuzzlesLib;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.config.ConfigHolderImpl;
import fuzs.sneakycurses.config.ClientConfig;
import fuzs.sneakycurses.config.ServerConfig;
import fuzs.sneakycurses.data.ModBlockTagsProvider;
import fuzs.sneakycurses.data.ModItemTagsProvider;
import fuzs.sneakycurses.handler.CurseRevealHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("removal")
@Mod(SneakyCurses.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SneakyCurses {
    public static final String MOD_ID = "sneakycurses";
    public static final String MOD_NAME = "Sneaky Curses";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @SuppressWarnings({ "Convert2MethodRef", "deprecation" })
    public static final ConfigHolder<ClientConfig, ServerConfig> CONFIG = ConfigHolder
            .of(() -> new ClientConfig(), () -> new ServerConfig());

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ((ConfigHolderImpl<?, ?>) CONFIG).addConfigs(MOD_ID);
        PuzzlesLib.setSideOnly();
        registerHandlers();
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(generator, existingFileHelper);
            generator.addProvider(blockTagsProvider);
            generator.addProvider(new ModItemTagsProvider(generator, existingFileHelper));
        }
    }

    private static void registerHandlers() {
        AnvilUpdateCallback.EVENT.register(CurseRevealHandler::onAnvilUpdate);
        LivingEvents.TICK.register(CurseRevealHandler::onLivingTick);
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
