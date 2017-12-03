package solonarv.mods.thegreatweb.common.lib;

public interface ICheckedConsumer<T> {
    boolean canActOnClass(Class<? extends T> cls);

    Class<T> getInputClass();
}
