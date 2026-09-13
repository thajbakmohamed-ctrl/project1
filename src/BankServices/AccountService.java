package BankServices;
import BankModels.Account;
import java.util.ArrayList;
import java.util.Optional;
public class AccountService {
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
    //1- يتأكد إن المبلغ أكبر من صفر
    //2- يضيف المبلغ على الرصيد
    public void deposit(Account account, double amount) {
        // إذا مبلغ الإيداع صفر أو أقل، نوقف العملية
        if (amount <= 0) {
            // الريترن بدون قيمة لان الفويد موجودة و بس بتقرا
            return;
        }
        // نجيب الرصيد الحالي ونضيف عليه مبلغ الإيداع ثم نحفظ الرصيد الجديد
        account.setBalance(account.getBalance() + amount);
        // نسجل عملية الإيداع بعد تحديث الرصيد
        transactionService.recordTransaction(account, "DEPOSIT", amount);
        // إذا العميل غطى الرصيد السالب نرجع نفعل الحساب
        if (account.getBalance() >= 0) {
            // نرجع الحساب فعال بعد ما يغطي العميل الرصيد السالب
            account.setActive(true);
            // نصفر عدد مرات الأوفردرافت بعد ما العميل يغطي الرصيد السالب
            account.setOverdraftCount(0);

        }
    }
    // ميثود لسحب مبلغ من الحساب
    public void withdraw(Account account, double amount) {
        // إذا مبلغ السحب صفر أو أقل، نوقف العملية
        if (amount <= 0) {
            return;
        }
        // إذا الحساب مو فعال نوقف عملية السحب
        if (!account.isActive()) {
            return;
        }
        if (account.getBalance() < 0 && amount > 100) {
            return;
        }
        // نحسب شنو بيصير الرصيد بعد عملية السحب
        double newBalance = account.getBalance() - amount;
        // إذا الرصيد الجديد صار بالسالب فهذا يعني صار أوفردرافت
        if (newBalance < 0) {
            // إذا دخل الحساب بالسالب نخصم رسوم أوفردرافت 35
            newBalance = newBalance - 35;
            // نزيد عدد مرات الأوفردرافت بواحد
            account.setOverdraftCount(account.getOverdraftCount() + 1);
            // إذا صار الأوفردرافت مرتين أو أكثر نخلي الحساب غير فعال
            if (account.getOverdraftCount() >= 2) {
                account.setActive(false);
            }

        }
        // نحفظ الرصيد الجديد بعد السحب والرسوم
        account.setBalance(newBalance);
        // نسجل عملية السحب بعد تحديث الرصيد
        transactionService.recordTransaction(account, "WITHDRAW", amount);

    }
    // ميثود لتحويل مبلغ من حساب إلى حساب ثاني
    public void transfer(Account fromAccount, Account toAccount, double amount) {
        // إذا مبلغ التحويل صفر أو أقل نوقف العملية
        if (amount <= 0) {
            return;
        }
        // إذا الحساب اللي بنحول منه مو فعال نوقف التحويل
        if (!fromAccount.isActive()) {
            return;
        }
        // إذا رصيد الحساب المرسل بالسالب والمبلغ أكبر من 100 نوقف التحويل
        if (fromAccount.getBalance() < 0 && amount > 100) {
            return;
        }
        // نسحب مبلغ التحويل من الحساب المرسل
        withdraw(fromAccount, amount);
        // نضيف مبلغ التحويل إلى الحساب المستلم
        deposit(toAccount, amount);

    }

}
