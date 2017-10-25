package solonarv.mods.divinations.common.locator;

import net.minecraft.nbt.NBTTagCompound;
import solonarv.mods.divinations.common.locator.base.BaseLocator;
import solonarv.mods.divinations.common.locator.base.IConcreteLocator;

public class EntityLocatorFactory extends BaseLocator {
    @Override
    public IConcreteLocator fromNBT(NBTTagCompound tag) {
        return null;
    }
}
