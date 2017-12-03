package solonarv.mods.thegreatweb.common.locator.consumer;

import solonarv.mods.thegreatweb.common.locator.result.ILocatorResult;

public abstract class AbstractConsumer<T extends ILocatorResult> implements IConsumer<T> {
    @Override
    public boolean canActOnClass(Class<? extends T> cls) {
        return getInputClass().isAssignableFrom(cls);
    }
}
