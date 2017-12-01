package solonarv.mods.divinations.common.locator.consumer;

import solonarv.mods.divinations.common.lib.ICheckedConsumer;
import solonarv.mods.divinations.common.locator.result.ILocatorResult;

public interface IConsumer<T extends ILocatorResult> extends ICheckedConsumer<T>, ISimpleConsumer<T> {
}
