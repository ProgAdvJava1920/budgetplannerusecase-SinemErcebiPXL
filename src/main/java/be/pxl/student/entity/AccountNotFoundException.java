package be.pxl.student.entity;

public class AccountNotFoundException extends AccountException {
    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String s) {
        super(s);
    }

    public AccountNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AccountNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
