package solonarv.mods.thegreatweb.common.locator.filter;

import solonarv.mods.thegreatweb.common.lib.ICheckedConsumer;
import solonarv.mods.thegreatweb.common.locator.result.ILocatorResult;

public interface IFilter<T extends ILocatorResult> extends ICheckedConsumer<T>, ISimpleFilter<T> {
}
