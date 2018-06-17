package _8bitforms.company;

import java.util.List;

public final class CompanyBuilder {
    private Long Id;
    private String name;
    private String address;
    private String city;
    private String country;
    private String email;
    private String phoneNumber;
    private List<Owner> owners;

    private CompanyBuilder() {
    }

    public static CompanyBuilder aCompany() {
        return new CompanyBuilder();
    }

    public CompanyBuilder withId(Long Id) {
        this.Id = Id;
        return this;
    }

    public CompanyBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CompanyBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public CompanyBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public CompanyBuilder withCountry(String country) {
        this.country = country;
        return this;
    }

    public CompanyBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public CompanyBuilder withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public CompanyBuilder withOwners(List<Owner> owners) {
        this.owners = owners;
        return this;
    }

    public Company build() {
        Company company = new Company();
        company.setId(Id);
        company.setName(name);
        company.setAddress(address);
        company.setCity(city);
        company.setCountry(country);
        company.setEmail(email);
        company.setPhoneNumber(phoneNumber);
        company.setOwners(owners);
        return company;
    }
}
