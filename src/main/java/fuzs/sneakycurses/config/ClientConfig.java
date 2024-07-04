package fuzs.sneakycurses.config;

import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.annotation.Config;

@SuppressWarnings("removal")
public class ClientConfig extends AbstractConfig {
    @Config(description = "Hide curse enchantments from the item tooltip.")
    public boolean hideCurses = true;
    @Config(name = "color_item_name", description = "Cursed items have a red hover name instead of aqua.")
    public boolean colorName = true;
    @Config(description = "Hide enchantment glint when the item is solely enchanted with curses.")
    public boolean disguiseItem = true;
    @Config(description = "Hide curses from enchanted books if they also hold other enchantments.")
    public boolean affectBooks = false;
    @Config(name = "shift_shows_curses", description = "Temporarily show tooltip as usual including curses while shift key is held.")
    public boolean shiftShows = false;
    @Config(name = "notify_player_of_decurse_by_wear", description = "Send a message to the client chat to inform the player that an item has been decursed by using or wearing it.")
    public boolean notifyClientOfDecurseByWear = true;

    @SuppressWarnings("deprecation")
    public ClientConfig() {
        super("");
    }
}
