package BankModels;

public class DebitCard {
    private String cardId;
    private String accountId;
    private String cardNumber;
    private String cardType;
    private String expiryDate;
    private boolean isActive;
    private boolean upgradeRequested;
    private String requestedCardType;
    private double dailyWithdrawUsed;
    private double dailyTransferUsed;
    private double dailyOwnTransferUsed;
    private double dailyDepositUsed;
    private String dailyUsageDate;

    public DebitCard(String cardId, String accountId, String cardNumber, String cardType,
                     String expiryDate) {
        this.cardId = cardId;
        this.accountId = accountId;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.expiryDate = expiryDate;
        this.isActive = true;
        // بالبداية ما يكون في طلب ترقية
        this.upgradeRequested = false;
         // بالبداية ما يكون في نوع بطاقة مطلوب
        this.requestedCardType = "";
        // بالبداية العميل ما استخدم أي مبلغ من حد السحب اليومي
        this.dailyWithdrawUsed = 0;
       // بالبداية العميل ما استخدم أي مبلغ من حد التحويل اليومي
        this.dailyTransferUsed = 0;
        // بالبداية العميل ما استخدم أي مبلغ من حد التحويل بين حساباته
        this.dailyOwnTransferUsed = 0;
       // بالبداية العميل ما استخدم أي مبلغ من حد الإيداع اليومي
        this.dailyDepositUsed = 0;
      // بالبداية ما عندنا تاريخ استخدام محفوظ
        this.dailyUsageDate = "";
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    // نرجع إذا في طلب ترقية أو لا
    public boolean isUpgradeRequested() {
        return upgradeRequested;
    }

    // نغير حالة طلب الترقية
    public void setUpgradeRequested(boolean upgradeRequested) {
        this.upgradeRequested = upgradeRequested;
    }

    // نرجع نوع البطاقة المطلوبة
    public String getRequestedCardType() {
        return requestedCardType;
    }

    // نخزن نوع البطاقة اللي طلبها العميل
    public void setRequestedCardType(String requestedCardType) {
        this.requestedCardType = requestedCardType;
    }
    // نرجع مجموع السحب المستخدم اليوم
    public double getDailyWithdrawUsed() {
        return dailyWithdrawUsed;
    }

    // نحدث مجموع السحب المستخدم اليوم
    public void setDailyWithdrawUsed(double dailyWithdrawUsed) {
        this.dailyWithdrawUsed = dailyWithdrawUsed;
    }

    // نرجع مجموع التحويل العادي المستخدم اليوم
    public double getDailyTransferUsed() {
        return dailyTransferUsed;
    }

    // نحدث مجموع التحويل العادي المستخدم اليوم
    public void setDailyTransferUsed(double dailyTransferUsed) {
        this.dailyTransferUsed = dailyTransferUsed;
    }

    // نرجع مجموع التحويل بين حسابات نفس العميل المستخدم اليوم
    public double getDailyOwnTransferUsed() {
        return dailyOwnTransferUsed;
    }

    // نحدث مجموع التحويل بين حسابات نفس العميل المستخدم اليوم
    public void setDailyOwnTransferUsed(double dailyOwnTransferUsed) {
        this.dailyOwnTransferUsed = dailyOwnTransferUsed;
    }

    // نرجع مجموع الإيداع المستخدم اليوم
    public double getDailyDepositUsed() {
        return dailyDepositUsed;
    }

    // نحدث مجموع الإيداع المستخدم اليوم
    public void setDailyDepositUsed(double dailyDepositUsed) {
        this.dailyDepositUsed = dailyDepositUsed;
    }

    // نرجع تاريخ استخدام الحدود اليومية
    public String getDailyUsageDate() {
        return dailyUsageDate;
    }

    // نحدث تاريخ استخدام الحدود اليومية
    public void setDailyUsageDate(String dailyUsageDate) {
        this.dailyUsageDate = dailyUsageDate;
    }
}
