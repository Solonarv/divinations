package solonarv.mods.thegreatweb.lib;

public interface ICheckedConsumer<T> {
    boolean canActOnClass(Class<? extends T> cls);

    Class<T> getInputClass();
}
