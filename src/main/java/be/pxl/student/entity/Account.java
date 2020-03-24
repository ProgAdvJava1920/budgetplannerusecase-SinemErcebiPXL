package be.pxl.student.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Account {
    private int id;
    private String IBAN;
    private String name;
    private List<Payment> payments = new ArrayList<>(); // tegen NullPointerException

    public Account(int id, String IBAN, String name) {
        this.id = id;
        this.IBAN = IBAN;
        this.name = name;
    }

    public Account() {
    }

    public Account(String name, String IBAN) {
        this.name = name;
        this.IBAN = IBAN;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    @Override
    public String toString() {
        return "Account{" +
                "IBAN='" + IBAN + '\'' +
                ", name='" + name + '\'' +
                ", payments=[" + payments.stream().map(Payment::toString).collect(Collectors.joining(",")) + "]}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(name, account.name) &&
                Objects.equals(IBAN, account.IBAN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, IBAN);
    }
}
