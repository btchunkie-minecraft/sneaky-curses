package fuzs.sneakycurses.data;

import fuzs.sneakycurses.SneakyCurses;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, SneakyCurses.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        // Add block tags here if needed
    }
}
