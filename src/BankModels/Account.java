package BankModels;

public class Account {
    private String accountId;
    private String customerId;
    private String accountType;
    private double balance;
    private boolean isActive;
    // يحسب الحساب جم مرة صار بالماينس
    private int overdraftCount;

    public Account(String accountId, String customerId, String accountType, double balance) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = balance;
        this.isActive = true;
        this.overdraftCount = 0;
    }
}
