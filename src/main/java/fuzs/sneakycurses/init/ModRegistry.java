package fuzs.sneakycurses.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModRegistry {
    public static final TagKey<Item> REVEALS_CURSES_ITEM_TAG = ItemTags
            .create(new ResourceLocation("sneakycurses", "reveals_curses"));
}
