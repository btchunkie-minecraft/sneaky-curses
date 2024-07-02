package fuzs.sneakycurses.data;

import fuzs.sneakycurses.SneakyCurses;
import fuzs.sneakycurses.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, new ModBlockTagsProvider(dataGenerator, existingFileHelper), SneakyCurses.MOD_ID,
                existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ModRegistry.REVEALS_CURSES_ITEM_TAG).add(Items.AMETHYST_SHARD);
    }
}
