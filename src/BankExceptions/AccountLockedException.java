package BankExceptions;

// Custom exception used when a user account is locked
public class AccountLockedException extends RuntimeException {

    public AccountLockedException(String message) {
        super(message);
    }
}