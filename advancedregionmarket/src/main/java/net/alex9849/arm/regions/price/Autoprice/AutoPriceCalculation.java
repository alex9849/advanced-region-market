package net.alex9849.arm.regions.price.Autoprice;

public enum AutoPriceCalculation {
    STATIC {
        public String toString() {
            return "static";
        }
    }, PER_M2 {
        public String toString() {
            return "per_m2";
        }
    }, PER_M3 {
        public String toString() {
            return "per_m3";
        }
    };

    public static AutoPriceCalculation getAutoPriceType(String name) {
        if (name.equalsIgnoreCase("static")) {
            return STATIC;
        } else if (name.equalsIgnoreCase("per_m2")) {
            return PER_M2;
        } else if (name.equalsIgnoreCase("perm2")) {
            return PER_M2;
        } else if (name.equalsIgnoreCase("per_m3")) {
            return PER_M3;
        } else if (name.equalsIgnoreCase("perm3")) {
            return PER_M3;
        }
        return null;
    }

    public abstract String toString();
}
