package BankServices;
import BankModels.Transaction;
import java.util.ArrayList;
// اي اكاونت
import BankModels.Account;
// الوقت و التاريخ
import java.time.LocalDateTime;

public class TransactionService {
    private ArrayList<Transaction> transactions;
    // ننشئ قائمة فاضية نقدر نضيف فيها العمليات البنكية
    public TransactionService() {
        transactions = new ArrayList<>();
    }
    // ميثود عشان نضيف عملية بنكية جديدة الى قائمة العمليات
    public void addTransaction(Transaction transaction) {
        // نضيف العملية البنكية الجديدة الى قائمة العمليات
        transactions.add(transaction);

    }
    // ميثود تسجل عملية بنكية جديدة
    public void recordTransaction(Account account, String transactionType, double amount) {
        // نسوي رقم خاص للعملية البنكية يعني بقدر اعرف جم
        // ترانزكشن من رقم الايدي حق الترانزكشن
        String transactionId = "T" + (transactions.size() + 1);
        // ناخذ التاريخ والوقت الحالي للعملية
        String dateTime = LocalDateTime.now().toString();
        // ننشئ عملية بنكية جديدة ونحفظ فيها كل تفاصيل العملية
        Transaction transaction = new Transaction(
                transactionId,
                account.getAccountId(),
                transactionType,
                amount,
                account.getBalance(),
                dateTime
        );
        // نضيف العملية الجديدة إلى قائمة العمليات
        transactions.add(transaction);

    }
    // ترجع كل العمليات البنكية الموجودة في القائمة
    public ArrayList<Transaction> getAllTransactions() {
        return transactions;
    }
    // ترجع العمليات الخاصة بحساب معين
    public ArrayList<Transaction> getTransactionsByAccountId(String accountId) {
        // نسوي قائمة فاضية نحط فيها العمليات الخاصة بالحساب المطلوب
        ArrayList<Transaction> accountTransactions = new ArrayList<>();
        // نمر على كل العمليات الموجودة
        for (Transaction transaction : transactions) {
            // اذا رقم الحساب في العملية يساوي رقم الحساب المطلوب
            if (transaction.getAccountId().equals(accountId)) {
                // نضيف العملية المطابقة الى قائمة عمليات الحساب
                accountTransactions.add(transaction);

            }

        }

        return accountTransactions;

    }

}
