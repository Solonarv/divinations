package solonarv.mods.divinations.common.lib;

public interface ICheckedConsumer<T> {
    boolean canActOnClass(Class<? extends T> cls);

    Class<T> getInputClass();
}
