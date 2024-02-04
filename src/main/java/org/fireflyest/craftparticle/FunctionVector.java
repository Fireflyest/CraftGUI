package org.fireflyest.craftparticle;

import org.bukkit.util.Vector;
import org.fireflyest.craftparticle.change.Function;

public class FunctionVector {
    
    private Function x;
    private Function y;
    private Function z;

    public FunctionVector(Function x, Function y, Function z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector getVector(int time) {
        return new Vector(x.f(time), y.f(time), z.f(time));
    }

}
