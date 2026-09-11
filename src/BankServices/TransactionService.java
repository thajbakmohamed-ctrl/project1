package BankServices;
import BankModels.Transaction;
import java.util.ArrayList;

public class TransactionService {
    private ArrayList<Transaction> transactions;
    // ننشئ قائمة فاضية نقدر نضيف فيها العمليات البنكية
    public TransactionService() {
        transactions = new ArrayList<>();
    }
    // ميثود لإضافة عملية بنكية جديدة إلى قائمة العمليات
    public void addTransaction(Transaction transaction) {
        // نضيف العملية البنكية الجديدة إلى قائمة العمليات
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
            // إذا رقم الحساب في العملية يساوي رقم الحساب المطلوب
            if (transaction.getAccountId().equals(accountId)) {
                // نضيف العملية المطابقة إلى قائمة عمليات الحساب
                accountTransactions.add(transaction);

            }

        }

        return accountTransactions;

    }

}
