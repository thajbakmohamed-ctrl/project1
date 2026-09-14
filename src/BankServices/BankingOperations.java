package BankServices;

// نستخدم Account لأن العمليات البنكية تعتمد على الحسابات
import BankModels.Account;


// Interface يحدد العمليات البنكية الأساسية
public interface BankingOperations {

    // إيداع مبلغ داخل الحساب
    void deposit(Account account, double amount);

    // سحب مبلغ من الحساب ونرجع true إذا نجحت العملية
    boolean withdraw(Account account, double amount);

    // تحويل مبلغ بين حسابين ونرجع true إذا نجحت العملية
    boolean transfer(Account fromAccount, Account toAccount, double amount);
}