package BankServices;
import BankModels.Transaction;
import java.util.ArrayList;
// اي اكاونت
import BankModels.Account;
// الوقت و التاريخ
import java.time.LocalDateTime;
import BankUtilities.FileHandlingUtility;
// نستخدم LocalDateTime عشان نتعامل مع تاريخ ووقت العملية
import java.time.LocalDateTime;
// Handles recording, storing, and filtering banking transactions
public class TransactionService {
    private ArrayList<Transaction> transactions;
    // Creates an empty list to store transactions
    // ننشئ قائمة فاضية نقدر نضيف فيها العمليات البنكية
    public TransactionService() {
        transactions = new ArrayList<>();
    }
    // Adds a transaction to the transaction list
    // ميثود عشان نضيف عملية بنكية جديدة الى قائمة العمليات
    public void addTransaction(Transaction transaction) {
        // نضيف العملية البنكية الجديدة الى قائمة العمليات
        transactions.add(transaction);

    }
    // Records and saves a new banking transaction
    // ميثود تسجل عملية بنكية جديدة
    public void recordTransaction(Account account, String transactionType, double amount) {
        // نسوي رقم خاص للعملية البنكية يعني بقدر اعرف جم
        // ترانزكشن من رقم الايدي حق الترانزكشن
        String transactionId = "T" + (transactions.size() + 1);
        // ناخذ التاريخ والوقت الحالي للعملية
        String dateTime = LocalDateTime.now().toString();
        // ننشئ عملية بنكية جديدة ونحفظ فيها كل تفاصيل العملية
        Transaction transaction = new Transaction(transactionId, account.getAccountId(),
                transactionType, amount, account.getBalance(), dateTime);
        // نضيف العملية الجديدة إلى قائمة العمليات
        transactions.add(transaction);
        // نحفظ العملية البنكية داخل ملف
        FileHandlingUtility.saveTransaction(transaction);

    }
    // Records a transfer transaction with the related account
    // نسجل عملية تحويل ونخزن رقم الحساب الثاني المرتبط فيها
    public void recordTransferTransaction(Account account, String transactionType,
            double amount, String relatedAccountId) {

        // نسوي رقم جديد للعملية
        String transactionId = "T" + (transactions.size() + 1);

        // نجيب التاريخ والوقت الحالي
        String dateTime = LocalDateTime.now().toString();

        // ننشئ عملية جديدة
        Transaction transaction = new Transaction(transactionId, account.getAccountId(),
                transactionType, amount, account.getBalance(), dateTime);

        // نخزن رقم الحساب الثاني المرتبط بالتحويل
        transaction.setRelatedAccountId(relatedAccountId);

        // نضيف العملية إلى قائمة العمليات
        transactions.add(transaction);

        // نحفظ العملية داخل الملف
        FileHandlingUtility.saveTransaction(transaction);
    }
    // Returns all transactions in the system
    // ترجع كل العمليات البنكية الموجودة في القائمة
    public ArrayList<Transaction> getAllTransactions() {
        return transactions;
    }
    // Returns all transactions for a specific account
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
    // Filters account transactions between two dates
    // نرجع عمليات حساب معين بين تاريخين محددين
    public ArrayList<Transaction> filterTransactionsByDate(String accountId,
            LocalDateTime startDate, LocalDateTime endDate) {

        // نسوي قائمة جديدة للعمليات اللي تطابق التاريخ المطلوب
        ArrayList<Transaction> filteredTransactions = new ArrayList<>();

        // نمر على كل العمليات الموجودة في النظام
        for (Transaction transaction : transactions) {
            // نتأكد إن العملية تخص الحساب المطلوب
            if (transaction.getAccountId().equals(accountId)) {
                // نحول التاريخ المحفوظ كنص إلى LocalDateTime
                LocalDateTime transactionDate = LocalDateTime.parse(
                                transaction.getDateTime());

                // نتأكد إن العملية مو قبل تاريخ البداية
                boolean afterStart = !transactionDate.isBefore(startDate);

                // نتأكد إن العملية مو بعد تاريخ النهاية
                boolean beforeEnd = !transactionDate.isAfter(endDate);

                // إذا العملية داخل الفترة المطلوبة
                if (afterStart && beforeEnd) {

                    // نضيف العملية إلى النتائج
                    filteredTransactions.add(transaction);
                }
            }
        }

        // نرجع العمليات اللي تطابق الفلتر
        return filteredTransactions;
    }

}
