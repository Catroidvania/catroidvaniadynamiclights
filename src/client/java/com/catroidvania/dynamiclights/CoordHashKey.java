package com.catroidvania.dynamiclights;

import java.util.Objects;

public class CoordHashKey {
    public final int x, y, z, hashcode;

    public CoordHashKey(int xPos, int yPos, int zPos) {
        this.x = xPos;
        this.y = yPos;
        this.z = zPos;
        this.hashcode = Objects.hash(this.x, this.y, this.z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return ((CoordHashKey) obj).x == this.x && ((CoordHashKey) obj).y == this.y && ((CoordHashKey) obj).z == this.z;
    }

    @Override
    public int hashCode() {
        return this.hashcode;
    }
}
