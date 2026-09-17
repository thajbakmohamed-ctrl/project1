package BankingSystem;
// نستخدم خدمة العملاء داخل نظام البنك
import BankServices.CustomerService;
// نستخدم خدمة الحسابات داخل نظام البنك
import BankServices.AccountService;
// نستخدم خدمة العمليات البنكية داخل نظام البنك
import BankServices.TransactionService;
// نستخدم خدمة تسجيل الدخول داخل نظام البنك
import BankServices.LoginService;
// نستخدم DebitCardService عشان ندير بطاقات الخصم
import BankServices.DebitCardService;

public class BankSystem {
    private CustomerService customerService;
    private AccountService accountService;
    private TransactionService transactionService;
    private LoginService loginService;
    // نخزن خدمة بطاقات الخصم داخل نظام البنك
    private DebitCardService debitCardService;
    public BankSystem() {
        // نسوي خدمة العملاء عشان نخزن وندير بيانات العملاء
        customerService = new CustomerService();
      // نسوي خدمة العمليات البنكية عشان نسجل كل ال Transactions
        transactionService = new TransactionService();
       // نسوي خدمة الحسابات ونعطيها نفس TransactionService
        // عشان أي Deposit أو Withdraw أو Transfer ينحفظ في نفس قائمة العمليات
        accountService = new AccountService(transactionService);
      // نسوي خدمة تسجيل الدخول عشان نتحقق من المستخدمين وكلمات المرور
        loginService = new LoginService();
        // ننشئ خدمة بطاقات الخصم
        debitCardService = new DebitCardService();
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }

    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }
    // نرجع خدمة بطاقات الخصم عشان نستخدمها في باقي أجزاء النظام
    public DebitCardService getDebitCardService() {
        return debitCardService;
    }

}
