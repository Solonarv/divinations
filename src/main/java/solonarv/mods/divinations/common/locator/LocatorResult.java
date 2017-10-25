package solonarv.mods.divinations.common.locator;

import net.minecraft.util.math.Vec3d;

public abstract class  LocatorResult {
    enum LocatorResultType {
        ENTITY,
        BLOCK;
        private String id;
        public final String getId() {
            return id;
        }
    }

    private final LocatorResultType type;

    LocatorResult(LocatorResultType type) {
        this.type = type;
    }

    abstract public Vec3d getPosition();
}
