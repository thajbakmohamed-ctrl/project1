package BankServices;
import BankModels.Account;
import java.util.ArrayList;
import java.util.Optional;
// AccountService يطبق العمليات الموجودة في BankingOperations
public class AccountService implements BankingOperations {
    private ArrayList<Account> accounts;
    // نستخدم TransactionService عشان نسجل العمليات البنكية
    private TransactionService transactionService;

    public AccountService(TransactionService transactionService) {
        // ننشئ قائمة فاضية نقدر نضيف فيها الحسابات
        accounts = new ArrayList<>();

        // نخزن TransactionService عشان نستخدمه في تسجيل العمليات
        this.transactionService = transactionService;
    }
    public void addAccount(Account account) {
        // لو القائمة فيها اوردي و نبي نزيد قمية ف بتزيد عليها
        accounts.add(account);
    }
    public Optional<Account> findAccountById(String accountId) {
        return accounts.stream()
                // نمر على كل الحسابات الموجودة في القائمة
                // ونختار الحساب اللي رقم الحساب ماله يساوي الرقم المطلوب
                .filter(account -> account.getAccountId().equals(accountId))
                // يرجع أول حساب مطابق، وإذا ما لقى يرجع نتيجة فاضية
                .findFirst();
    }
    // ترجع كل الحسابات الموجودة في القائمة
    public ArrayList<Account> getAllAccounts() {
        return accounts;
    }
    // ترجع كل الحسابات الخاصة بعميل معين
    public ArrayList<Account> getAccountsByCustomerId(String customerId) {

        // نسوي قائمة فاضية نحط فيها حسابات العميل
        ArrayList<Account> customerAccounts = new ArrayList<>();

        // نمر على كل الحسابات الموجودة
        for (Account account : accounts) {

            // اذا رقم العميل في الحساب يساوي رقم العميل المطلوب
            if (account.getCustomerId().equals(customerId)) {

                // نضيف الحساب الى قائمة حسابات العميل
                customerAccounts.add(account);
            }
        }

        return customerAccounts;
    }
    @Override
    // نودع مبلغ داخل الحساب
    public void deposit(Account account, double amount) {

        // إذا المبلغ صفر أو سالب نوقف العملية
        if (amount <= 0) {
            return;
        }

        // نضيف المبلغ إلى الرصيد الحالي
        account.setBalance(account.getBalance() + amount);

        // نسجل عملية الإيداع
        transactionService.recordTransaction(
                account,
                "DEPOSIT",
                amount
        );

        // إذا العميل غطى الرصيد السالب والرسوم
        if (account.getBalance() >= 0) {

            // نرجع الحساب فعال
            account.setActive(true);

            // نصفر عدد مرات الـ Overdraft بعد تسوية الرصيد
            account.setOverdraftCount(0);
        }
    }
    @Override
    // نسحب مبلغ من الحساب ونرجع true إذا العملية نجحت
    public boolean withdraw(Account account, double amount) {

        // إذا المبلغ صفر أو سالب نرفض العملية
        if (amount <= 0) {
            return false;
        }

        // إذا الحساب غير فعال نرفض السحب
        if (!account.isActive()) {
            return false;
        }

        // إذا الرصيد سالب والمبلغ أكبر من 100 نرفض العملية
        if (account.getBalance() < 0 && amount > 100) {
            return false;
        }

        // نحسب الرصيد الجديد بعد السحب
        double newBalance = account.getBalance() - amount;

        // إذا صار الرصيد سالب نضيف رسوم Overdraft
        if (newBalance < 0) {

            // نخصم رسوم Overdraft بقيمة 35
            newBalance = newBalance - 35;

            // نزيد عدد مرات الـ Overdraft
            account.setOverdraftCount(
                    account.getOverdraftCount() + 1
            );

            // إذا وصل العميل إلى مرتين Overdraft نعطل الحساب
            if (account.getOverdraftCount() >= 2) {
                account.setActive(false);
            }
        }

        // نخزن الرصيد الجديد
        account.setBalance(newBalance);

        // نسجل عملية السحب في سجل العمليات
        transactionService.recordTransaction(
                account,
                "WITHDRAW",
                amount
        );

        // العملية نجحت
        return true;
    }
    @Override
    // نحول مبلغ من حساب إلى حساب ونرجع true إذا العملية نجحت
    public boolean transfer(Account fromAccount, Account toAccount, double amount) {

        // إذا المبلغ صفر أو سالب نرفض العملية
        if (amount <= 0) {
            return false;
        }

        // نتأكد إن الحساب المرسل والمستلم مو نفس الحساب
        if (fromAccount.getAccountId().equals(toAccount.getAccountId())) {
            return false;
        }

        // نحاول نسحب المبلغ من الحساب المرسل
        boolean withdrawSuccessful =
                withdraw(fromAccount, amount);

        // إذا السحب فشل نوقف التحويل
        if (!withdrawSuccessful) {
            return false;
        }

        // إذا السحب نجح نودع المبلغ في الحساب المستلم
        deposit(toAccount, amount);

        // التحويل نجح
        return true;
    }

}
