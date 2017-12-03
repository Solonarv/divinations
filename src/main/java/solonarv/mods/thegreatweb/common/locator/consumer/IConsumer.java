package solonarv.mods.thegreatweb.common.locator.consumer;

import solonarv.mods.thegreatweb.common.lib.ICheckedConsumer;
import solonarv.mods.thegreatweb.common.locator.result.ILocatorResult;

public interface IConsumer<T extends ILocatorResult> extends ICheckedConsumer<T>, ISimpleConsumer<T> {
}
