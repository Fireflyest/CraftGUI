package org.fireflyest.craftparticle.change;

public class ConstantFunction extends Function {

    private double constant;

    public ConstantFunction(double constant) {
        this.constant = constant;
    }

    @Override
    public double f(double x) {
        return constant;
    }

}
