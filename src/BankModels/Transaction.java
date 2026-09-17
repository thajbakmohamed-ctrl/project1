package BankModels;

public class Transaction {
    private String transactionId;
    private String accountId;
    private String transactionType;
    private double amount;
    private double balanceAfter;
    private String dateTime;
    private String relatedAccountId;

    public Transaction(String transactionId, String accountId, String transactionType,
                       double amount,
                       double balanceAfter, String dateTime) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.dateTime = dateTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    // نرجع رقم الحساب الثاني المرتبط بالعملية
    public String getRelatedAccountId() {
        return relatedAccountId;
    }

    // نخزن رقم الحساب الثاني المرتبط بالعملية
    public void setRelatedAccountId(String relatedAccountId) {
        this.relatedAccountId = relatedAccountId;
    }
}
