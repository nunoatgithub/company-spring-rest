package _8bitforms.company;

public final class OwnerBuilder {
    private String name;

    private OwnerBuilder() {
    }

    public static OwnerBuilder anOwner() {
        return new OwnerBuilder();
    }

    public OwnerBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Owner build() {
        Owner owner = new Owner();
        owner.setName(name);
        return owner;
    }
}
