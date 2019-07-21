package net.alex9849.arm.util;

public class Tuple<X, Y> {
    private X value1;
    private Y value2;

    public Tuple(X value1, Y value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public X getValue1() {
        return this.value1;
    }

    public Y getValue2() {
        return this.value2;
    }

}
