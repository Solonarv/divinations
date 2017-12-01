package solonarv.mods.divinations.common.locator.filter;

import solonarv.mods.divinations.common.lib.ICheckedConsumer;
import solonarv.mods.divinations.common.locator.result.ILocatorResult;

public interface IFilter<T extends ILocatorResult> extends ICheckedConsumer<T>, ISimpleFilter<T> {
}
