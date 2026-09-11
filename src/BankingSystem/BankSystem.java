package BankingSystem;
// نستخدم خدمة العملاء داخل نظام البنك
import BankServices.CustomerService;
// نستخدم خدمة الحسابات داخل نظام البنك
import BankServices.AccountService;
// نستخدم خدمة العمليات البنكية داخل نظام البنك
import BankServices.TransactionService;
// نستخدم خدمة تسجيل الدخول داخل نظام البنك
import BankServices.LoginService;

public class BankSystem {
    private CustomerService customerService;
    private AccountService accountService;
    private TransactionService transactionService;
    private LoginService loginService;
    public BankSystem() {
        // ننشئ خدمة العملاء أول ما يشتغل نظام البنك
        customerService = new CustomerService();
        // ننشئ خدمة الحسابات أول ما يشتغل نظام البنك
        accountService = new AccountService();
        // ننشئ خدمة العمليات البنكية أول ما يشتغل نظام البنك
        transactionService = new TransactionService();
        // ننشئ خدمة تسجيل الدخول أول ما يشتغل نظام البنك
        loginService = new LoginService();


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

}
