package org.fireflyest.craftparticle;

import javax.annotation.Nonnull;

import org.bukkit.util.Vector;
import org.fireflyest.craftparticle.change.ConstantFunction;
import org.fireflyest.craftparticle.change.Function;

public class DynamicVector {
    
    private final Vector vector;
    private Function dx;
    private Function dy;
    private Function dz;
    private Function drx;
    private Function dry;
    private Function drz;
    private Function dl;

    public DynamicVector(@Nonnull Vector vector) {
        this(vector, 1);
    }

    public DynamicVector(Vector vector, double dl) {
        this(vector, new ConstantFunction(dl));
    }

    public DynamicVector(Vector vector, Function dl) {
        this(vector, 0, 0, 0, 0, 0, 0);
        this.dl = dl;
    }

    public DynamicVector(Function dx, Function dy, Function dz) {
        this(new Vector(), dx, dy, dz, new ConstantFunction(0), new ConstantFunction(0), new ConstantFunction(0));
    }

    public DynamicVector(Vector vector, double dx, double dy, double dz, double drx, double dry,
            double drz) {
        this(vector, new ConstantFunction(dx), new ConstantFunction(dy), new ConstantFunction(dz), new ConstantFunction(drx), new ConstantFunction(dry), new ConstantFunction(drz));
    }

    public DynamicVector(Vector vector, Function dx, Function dy, Function dz, Function drx, Function dry,
            Function drz) {
        this.vector = vector;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.drx = drx;
        this.dry = dry;
        this.drz = drz;
        this.dl = new ConstantFunction(1);
    }

    public Vector getVector(int time) {
        vector.add(new Vector(dx.f(time), dy.f(time), dz.f(time)))
            .rotateAroundX(drx.f(time))
            .rotateAroundY(dry.f(time))
            .rotateAroundZ(drz.f(time))
            .multiply(dl.f(time));
        return vector;
    }

}
