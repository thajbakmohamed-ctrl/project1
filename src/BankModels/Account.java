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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return isActive;
    }
// ماحط قيت لان بوليين هو في قيترز بس ماحط قيت
    // هذا Getter للـ boolean، لذلك اسمه isActive بدل getActiv
    public void setActive(boolean active) {
        isActive = active;
    }

    public int getOverdraftCount() {
        return overdraftCount;
    }

    public void setOverdraftCount(int overdraftCount) {
        this.overdraftCount = overdraftCount;
    }
}

