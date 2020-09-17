package org.innopolis.kuzymvas.generic.hashmap;

class FixedHash {

    private final int val;
    private final int hash;

    public FixedHash(int val, int hash) {
        this.val = val;
        this.hash = hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FixedHash that = (FixedHash) o;
        return (this.val == that.val);
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
