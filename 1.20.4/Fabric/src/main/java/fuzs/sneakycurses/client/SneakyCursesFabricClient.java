package fuzs.sneakycurses.client;

import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.sneakycurses.SneakyCurses;
import net.fabricmc.api.ClientModInitializer;

public class SneakyCursesFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(SneakyCurses.MOD_ID, SneakyCursesClient::new);
    }
}
