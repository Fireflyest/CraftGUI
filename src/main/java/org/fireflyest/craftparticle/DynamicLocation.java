package org.fireflyest.craftparticle;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.fireflyest.craftparticle.change.ConstantFunction;
import org.fireflyest.craftparticle.change.Function;

public class DynamicLocation {
    
    private int time = -1;

    private final Location location;
    private DynamicVector dVector;
    private FunctionVector fVector;

    public DynamicLocation(@Nonnull Location location) {
        this(location, new ConstantFunction(0), new ConstantFunction(0), new ConstantFunction(0));
    }

    public DynamicLocation(@Nonnull Location location, double dx, double dy, double dz) {
        this(location, new ConstantFunction(dx), new ConstantFunction(dy), new ConstantFunction(dz));
    }

    public DynamicLocation(@Nonnull Location location, @Nonnull Function dx, @Nonnull Function dy, @Nonnull Function dz) {
        this.location = location;
        this.fVector = new FunctionVector(dx, dy, dz);
    }
    
    public DynamicLocation(@Nonnull Location location, @Nonnull FunctionVector fVector) {
        this.location = location;
        this.fVector = fVector;
    }

    public DynamicLocation(@Nonnull Location location, @Nonnull DynamicVector dVector) {
        this.location = location;
        this.dVector = dVector;
    }

    public Location getLocation() {
        if (time == -1) {
            time++;
            return location;
        }
        if (dVector != null) {
            location.add(dVector.getVector(time));
        }
        if (fVector != null) {
            location.add(fVector.getVector(time));
        }
        time++;
        return location;
    }

}
