package BankServices;
import BankModels.Account;
import BankExceptions.InactiveAccountException;
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
        // نضيف الحساب إلى قائمة الحسابات
        accounts.add(account);
    }

    public Optional<Account> findAccountById(String accountId) {
        // نبحث عن الحساب باستخدام Account ID
        return accounts.stream()

                // نختار الحساب اللي رقمه يساوي الرقم المطلوب
                .filter(account -> account.getAccountId().equals(accountId))
                // نرجع أول حساب مطابق أو Optional فاضي
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

            // نتأكد إن الحساب تابع للعميل المطلوب
            if (account.getCustomerId().equals(customerId)) {
                // نضيف الحساب إلى قائمة حسابات العميل
                customerAccounts.add(account);
            }
        }
        // نرجع حسابات العميل
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
        transactionService.recordTransaction(account, "DEPOSIT", amount);

        // إذا العميل غطى الرصيد السالب والرسوم
        if (account.getBalance() >= 0) {
            // نرجع الحساب فعال
            account.setActive(true);
            // نصفر عدد مرات الـOverdraft
            account.setOverdraftCount(0);
        }
    }

    @Override
    // نسحب مبلغ من الحساب ونرجع true إذا العملية نجحت
    public boolean withdraw(Account account,
            double amount) {
        // إذا المبلغ صفر أو سالب نرفض العملية
        if (amount <= 0) {
            return false;
        }

        // إذا الحساب غير فعال نرمي Exception
        if (!account.isActive()) {
            throw new InactiveAccountException("This account is inactive.");
        }

        // إذا الرصيد موجب نسمح بتجاوز الرصيد بحد أقصى 100
        if (account.getBalance() >= 0 && amount > account.getBalance() + 100) {

            return false;
        }

        // إذا الرصيد سالب ما نسمح بعملية أكبر من 100
        if (account.getBalance() < 0 && amount > 100) {

            return false;
        }

        // نحسب الرصيد الجديد بعد السحب
        double newBalance = account.getBalance() - amount;

        // إذا صار الرصيد سالب نضيف رسوم Overdraft
        if (newBalance < 0) {
            // نخصم رسوم Overdraft بقيمة 35
            newBalance = newBalance - 35;

            // نزيد عدد مرات الـOverdraft
            account.setOverdraftCount(account.getOverdraftCount() + 1);
            System.out.println("Overdraft applied.");
            System.out.println("Overdraft fee: $35");
            System.out.println("Overdraft count: " + account.getOverdraftCount());
            // إذا وصل العميل إلى مرتين Overdraft نعطل الحساب
            if (account.getOverdraftCount() >= 2) {
                account.setActive(false);
                System.out.println("Account is now inactive due to repeated overdrafts.");
            }
        }

        // نخزن الرصيد الجديد
        account.setBalance(newBalance);
        // نسجل عملية السحب في سجل العمليات
        transactionService.recordTransaction(account, "WITHDRAW", amount);
        // العملية نجحت
        return true;
    }

    @Override
    // نحول مبلغ من حساب إلى حساب ثاني
    public boolean transfer(Account fromAccount, Account toAccount, double amount) {
        // نتأكد إن مبلغ التحويل صحيح
        if (amount <= 0) {
            return false;
        }

        // نمنع التحويل إلى نفس الحساب
        if (fromAccount.getAccountId().equals(toAccount.getAccountId())) {
            return false;
        }
        // نتأكد إن الحساب المرسل فعال
        if (!fromAccount.isActive()) {
            return false;
        }
        // إذا الرصيد موجب نسمح بتجاوز الرصيد بحد أقصى 100
        if (fromAccount.getBalance() >= 0 && amount > fromAccount.getBalance() + 100) {
            return false;
        }

        // إذا الرصيد سالب ما نسمح بتحويل أكبر من 100
        if (fromAccount.getBalance() < 0 && amount > 100) {
            return false;
        }
        // نحسب الرصيد الجديد للحساب المرسل
        double newFromBalance = fromAccount.getBalance() - amount;

        // إذا التحويل سبب Overdraft
        if (newFromBalance < 0) {
            // نخصم رسوم الـOverdraft وهي 35
            newFromBalance = newFromBalance - 35;

            // نزيد عدد مرات الـOverdraft
            fromAccount.setOverdraftCount(fromAccount.getOverdraftCount() + 1);
            // نوضح للمستخدم أن ال Overdraft تم تطبيقه
            System.out.println("Overdraft applied.");

              // نوضح رسوم ال Overdraft
            System.out.println("Overdraft fee: $35");

              // نوضح عدد مرات ال Overdraft
            System.out.println("Overdraft count: " + fromAccount.getOverdraftCount());

            if (fromAccount.getOverdraftCount() >= 2) {
                // نعطل الحساب بعد مرتين Overdraft
                fromAccount.setActive(false);
                // نوضح للمستخدم سبب تعطيل الحساب
                System.out.println("Account is now inactive due to repeated overdrafts.");
            }
        }
        // نحدث رصيد الحساب المرسل
        fromAccount.setBalance(newFromBalance);
        // نضيف المبلغ إلى الحساب المستلم
        toAccount.setBalance(toAccount.getBalance() + amount);

        // إذا الحساب المستلم غطى الرصيد السالب
        if (toAccount.getBalance() >= 0) {
            // نعيد تفعيل الحساب
            toAccount.setActive(true);
            // نصفر عدد مرات الـOverdraft
            toAccount.setOverdraftCount(0);
        }

        // نسجل التحويل في الحساب المرسل
        transactionService.recordTransferTransaction(fromAccount, "TRANSFER_OUT",
                amount, toAccount.getAccountId());

        // نسجل التحويل في الحساب المستلم
        transactionService.recordTransferTransaction(toAccount,
                "TRANSFER_IN", amount, fromAccount.getAccountId());
        // نرجع true لأن التحويل نجح
        return true;
    }
}