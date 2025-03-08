package lawson.lonchi.morpion.model;

public enum Owner {
    NONE, FIRST, SECOND;

    public Owner opposite() {
        return this == SECOND ? FIRST : this == FIRST ? SECOND : NONE;
    }

    @Override
    public String toString() {
        return this == FIRST ? "X" : this == SECOND ? "O" : "";
    }
}