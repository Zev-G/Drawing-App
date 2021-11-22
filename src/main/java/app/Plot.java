package app;

import java.util.Objects;

public class Plot {

    private final int x;
    private final int y;

    public Plot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plot plot = (Plot) o;
        return x == plot.x && y == plot.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
