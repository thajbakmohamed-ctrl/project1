package BankUtilities;

// نستخدم RandomAccessFile عشان نقرا ونكتب البيانات داخل الملفات
import java.io.RandomAccessFile;
// نستخدم IOException عشان نتعامل مع أخطاء الملفات
import java.io.IOException;
// نستخدم Customer عشان نحفظ ونقرا بيانات العميل
import BankModels.Customer;
// نستخدم Account عشان نحفظ ونقرا بيانات الحساب
import BankModels.Account;
// نستخدم Transaction عشان نحفظ ونقرا العمليات البنكية
import BankModels.Transaction;
// نستخدم ArrayList عشان نخزن مجموعة من العمليات
import java.util.ArrayList;
// نستخدم Banker عشان نحفظ ونقرا بيانات موظف البنك
import BankModels.Banker;
// نستخدم DebitCard عشان نحفظ ونقرا بيانات البطاقة
import BankModels.DebitCard;

public class FileHandlingUtility {
    // نحفظ بيانات العميل داخل ملف
    public static void saveCustomer(Customer customer) {
        // نسوي اسم الملف حسب اسم العميل ورقم العميل
        String fileName = "Customer-" + customer.getName()
                + "-" + customer.getCustomerId() + ".txt";
        try {
            // نفتح ملف العميل للقراءة والكتابة
            RandomAccessFile file =
                    new RandomAccessFile(fileName, "rw");
            // نكتب رقم العميل داخل الملف
            file.writeUTF(customer.getCustomerId());
            // نكتب اسم العميل داخل الملف
            file.writeUTF(customer.getName());
            // نكتب رقم المستخدم داخل الملف
            file.writeUTF(customer.getUserId());
            // نكتب إيميل العميل داخل الملف
            file.writeUTF(customer.getEmail());
            // نكتب رقم تلفون العميل داخل الملف
            file.writeUTF(customer.getPhone());
            // نكتب كلمة المرور المشفرة داخل الملف
            file.writeUTF(customer.getPasswordHash());
            // نحفظ إذا العميل لازم يغير كلمة المرور المؤقتة
            file.writeBoolean(customer.isMustChangePassword());
            // نقفل ملف العميل بعد ما نخلص
            file.close();
            // نحفظ اسم ملف العميل داخل CustomerFiles عشان نقراه تلقائيًا بعدين
            saveCustomerFileName(fileName);

        } catch (IOException e) {
            // نطبع رسالة إذا صار خطأ أثناء حفظ العميل
            System.out.println("Error saving customer file.");
        }
    }

    // نحفظ بيانات موظف البنك داخل ملف
    public static void saveBanker(Banker banker) {
        // نسوي اسم الملف حسب اسم موظف البنك ورقمه
        String fileName = "Banker-" + banker.getName()
                + "-" + banker.getBankerId() + ".txt";

        try {

            // نفتح ملف موظف البنك للقراءة والكتابة
            RandomAccessFile file =
                    new RandomAccessFile(fileName, "rw");

            // نكتب رقم موظف البنك
            file.writeUTF(banker.getBankerId());
            // نكتب رقم المستخدم
            file.writeUTF(banker.getUserId());
            // نكتب اسم موظف البنك
            file.writeUTF(banker.getName());
            // نكتب إيميل موظف البنك
            file.writeUTF(banker.getEmail());
            // نكتب رقم تلفون موظف البنك
            file.writeUTF(banker.getPhone());
            // نكتب كلمة المرور المشفرة
            file.writeUTF(banker.getPasswordHash());
            // نقفل الملف بعد ما نخلص
            file.close();

        } catch (IOException e) {
            // نطبع رسالة إذا صار خطأ أثناء حفظ موظف البنك
            System.out.println("Error saving banker file.");
        }
    }

    // نقرا بيانات موظف البنك من الملف
    public static Banker readBanker(String fileName) {

        try {
            // نفتح ملف موظف البنك للقراءة فقط
            RandomAccessFile file =
                    new RandomAccessFile(fileName, "r");
            // نقرا رقم موظف البنك
            String bankerId = file.readUTF();
            // نقرا رقم المستخدم
            String userId = file.readUTF();
            // نقرا اسم موظف البنك
            String name = file.readUTF();
            // نقرا إيميل موظف البنك
            String email = file.readUTF();
            // نقرا رقم تلفون موظف البنك
            String phone = file.readUTF();
            // نقرا كلمة المرور المشفرة
            String passwordHash = file.readUTF();

            // نسوي Banker باستخدام البيانات اللي قريناها
            Banker banker = new Banker(
                    bankerId, userId, name, email, phone, passwordHash);

            // نقفل الملف بعد ما نخلص القراءة
            file.close();
            // نرجع موظف البنك
            return banker;
        } catch (IOException e) {
            // نطبع رسالة إذا صار خطأ أثناء قراءة ملف موظف البنك
            System.out.println("Error reading banker file.");
        }
        // إذا ما قدرنا نقرا موظف البنك نرجع null
        return null;
    }

    // نحفظ اسم ملف العميل عشان نقدر نقراه تلقائيًا بعدين
    public static void saveCustomerFileName(String fileName) {

        try {
            // نفتح ملف CustomerFiles للقراءة والكتابة
            RandomAccessFile file =
                    new RandomAccessFile("CustomerFiles.txt", "rw");
            // نفترض في البداية إن اسم الملف مو موجود
            boolean fileNameExists = false;
            // نمر على كل أسماء الملفات المحفوظة
            while (file.getFilePointer() < file.length()) {
                // نقرا اسم ملف محفوظ
                String savedFileName = file.readUTF();
                // نتأكد إذا اسم الملف موجود من قبل
                if (savedFileName.equals(fileName)) {
                    // نغير القيمة إلى true لأن الملف موجود
                    fileNameExists = true;
                    // نوقف البحث لأننا لقينا الملف
                    break;
                }
            }

            // إذا اسم الملف مو موجود من قبل
            if (!fileNameExists) {
                // نروح إلى نهاية الملف
                file.seek(file.length());
                // نضيف اسم ملف العميل الجديد
                file.writeUTF(fileName);
            }
            // نقفل الملف بعد ما نخلص
            file.close();

        } catch (IOException e) {
            // نطبع رسالة إذا صار خطأ
            System.out.println("Error saving customer file name.");
        }
    }

    // نقرا أسماء ملفات العملاء المحفوظة
    public static ArrayList<String> readCustomerFileNames() {
        // نسوي قائمة نخزن فيها أسماء ملفات العملاء
        ArrayList<String> customerFileNames = new ArrayList<>();

        try {
            // نفتح ملف أسماء العملاء للقراءة فقط
            RandomAccessFile file =
                    new RandomAccessFile("CustomerFiles.txt", "r");
            // نستمر في القراءة لين نوصل لنهاية الملف
            while (file.getFilePointer() < file.length()) {
                // نقرا اسم ملف العميل
                String fileName = file.readUTF();
                // نضيف اسم الملف إلى القائمة
                customerFileNames.add(fileName);
            }

            // نقفل الملف بعد ما نخلص
            file.close();
        } catch (IOException e) {
            // إذا الملف مو موجود للحين نرجع قائمة فاضية
            System.out.println("No saved customer files found.");
        }
        // نرجع قائمة أسماء ملفات العملاء
        return customerFileNames;
    }

    // نحفظ بيانات الحساب داخل ملف
    public static void saveAccount(Account account) {
        // نسوي اسم الملف حسب رقم الحساب
        String fileName = "Account-" + account.getAccountId() + ".txt";

        try {
            // نفتح ملف الحساب للقراءة والكتابة
            RandomAccessFile file =
                    new RandomAccessFile(fileName, "rw");
            // نكتب رقم الحساب داخل الملف
            file.writeUTF(account.getAccountId());
            // نكتب رقم العميل صاحب الحساب
            file.writeUTF(account.getCustomerId());
            // نكتب نوع الحساب
            file.writeUTF(account.getAccountType());
            // نكتب رصيد الحساب
            file.writeDouble(account.getBalance());
            // نكتب إذا الحساب فعال أو لا
            file.writeBoolean(account.isActive());
            // نكتب عدد مرات الـ Overdraft
            file.writeInt(account.getOverdraftCount());
            // نقفل ملف الحساب بعد ما نخلص
            file.close();
            // نحفظ اسم ملف الحساب عشان نقدر نقراه تلقائيًا بعدين
            saveAccountFileName(fileName);

        } catch (IOException e) {
            // نطبع رسالة إذا صار خطأ أثناء حفظ الحساب
            System.out.println("Error saving account file.");
        }
    }

    // نحفظ اسم ملف الحساب عشان نقدر نقراه تلقائيًا بعدين
    public static void saveAccountFileName(String fileName) {

        try {
            // نفتح ملف أسماء الحسابات للقراءة والكتابة
            RandomAccessFile file =
                    new RandomAccessFile("AccountFiles.txt", "rw");
            // نفترض في البداية إن اسم الملف مو موجود
            boolean fileNameExists = false;
            // نمر على كل أسماء ملفات الحسابات المحفوظة
            while (file.getFilePointer() < file.length()) {
                // نقرا اسم ملف محفوظ
                String savedFileName = file.readUTF();
                // نتأكد إذا اسم الملف موجود من قبل
                if (savedFileName.equals(fileName)) {
                    // نحدد إن الملف موجود
                    fileNameExists = true;
                    // نوقف البحث
                    break;
                }
            }

            // إذا اسم الملف مو موجود من قبل
            if (!fileNameExists) {
                // نروح إلى نهاية الملف
                file.seek(file.length());
                // نحفظ اسم ملف الحساب
                file.writeUTF(fileName);
            }

            // نقفل الملف بعد ما نخلص
            file.close();
        } catch (IOException e) {
            //نطبع رسالة إذا صار خطأ
            System.out.println("Error saving account file name.");
        }
    }


    // نقرا أسماء ملفات الحسابات المحفوظة
    public static ArrayList<String> readAccountFileNames() {
        // نسوي قائمة نخزن فيها أسماء ملفات الحسابات
        ArrayList<String> accountFileNames = new ArrayList<>();

        try {
            // نفتح ملف أسماء الحسابات للقراءة فقط
            RandomAccessFile file =
                    new RandomAccessFile("AccountFiles.txt", "r");
            // نستمر في القراءة لين نوصل لنهاية الملف
            while (file.getFilePointer() < file.length()) {
                // نقرا اسم ملف الحساب
                String fileName = file.readUTF();
                // نضيف اسم الملف إلى القائمة
                accountFileNames.add(fileName);
            }

            // نقفل الملف بعد ما نخلص
            file.close();
        } catch (IOException e) {
            // إذا الملف مو موجود للحين نرجع قائمة فاضية
            System.out.println("No saved account files found.");
        }
        // نرجع قائمة أسماء ملفات الحسابات
        return accountFileNames;
    }


    // نقرا بيانات العميل من الملف
    public static Customer readCustomer(String fileName) {

        try {
            // نفتح ملف العميل للقراءة فقط
            RandomAccessFile file =
                    new RandomAccessFile(fileName, "r");
            // نقرا رقم العميل
            String customerId = file.readUTF();
            // نقرا اسم العميل
            String name = file.readUTF();
            // نقرا رقم المستخدم
            String userId = file.readUTF();
            // نقرا إيميل العميل
            String email = file.readUTF();
            // نقرا رقم تلفون العميل
            String phone = file.readUTF();
            // نقرا كلمة المرور المشفرة
            String passwordHash = file.readUTF();
            // بشكل افتراضي نعتبر العميل لازم يغير كلمة المرور
            boolean mustChangePassword = true;

            // نتأكد إذا الملف يحتوي على حالة تغيير كلمة المرور
            if (file.getFilePointer() < file.length()) {
                // نقرا حالة تغيير كلمة المرور
                mustChangePassword = file.readBoolean();
            }

            // نسوي Customer باستخدام البيانات اللي قريناها
            Customer customer = new Customer(
                    customerId, userId, name, email, phone, passwordHash);

            // نرجع حالة تغيير كلمة المرور المحفوظة
            customer.setMustChangePassword(mustChangePassword);
            // نقفل الملف بعد ما نخلص القراءة
            file.close();
            // نرجع العميل
            return customer;
        } catch (IOException e) {
            // نطبع رسالة إذا صار خطأ أثناء قراءة العميل
            System.out.println("Error reading customer file.");
        }

        // إذا ما قدرنا نقرا العميل نرجع null
        return null;
    }


    // نقرا بيانات الحساب من الملف
    public static Account readAccount(String fileName) {
        try {
            // نفتح ملف الحساب للقراءة فقط
            RandomAccessFile file =
                    new RandomAccessFile(fileName, "r");
            // نقرا رقم الحساب
            String accountId = file.readUTF();
            // نقرا رقم العميل صاحب الحساب
            String customerId = file.readUTF();
            // نقرا نوع الحساب
            String accountType = file.readUTF();
            // نقرا رصيد الحساب
            double balance = file.readDouble();
            // نقرا إذا الحساب فعال أو لا
            boolean isActive = file.readBoolean();
            // نقرا عدد مرات الـ Overdraft
            int overdraftCount = file.readInt();
            // نسوي Account باستخدام البيانات اللي قريناها
            Account account = new Account(accountId, customerId, accountType, balance);

            // نرجع حالة الحساب مثل ما كانت محفوظة
            account.setActive(isActive);
            // نرجع عدد مرات الـ Overdraft
            account.setOverdraftCount(overdraftCount);
            // نقفل الملف بعد ما نخلص
            file.close();
            // نرجع الحساب
            return account;

        } catch (IOException e) {

            // نطبع رسالة إذا صار خطأ أثناء قراءة الحساب
            System.out.println("Error reading account file.");
        }
        // إذا ما قدرنا نقرا الحساب نرجع null
        return null;
    }

    // نحفظ بيانات بطاقة الخصم داخل ملف
    public static void saveDebitCard(DebitCard debitCard) {
        // نسوي اسم الملف حسب رقم البطاقة
        String fileName = "DebitCard-" + debitCard.getCardId() + ".txt";

        try {

            // نفتح ملف البطاقة للقراءة والكتابة
            RandomAccessFile file =
                    new RandomAccessFile(fileName, "rw");
            // نكتب رقم تعريف البطاقة
            file.writeUTF(debitCard.getCardId());
            // نكتب رقم الحساب المرتبط بالبطاقة
            file.writeUTF(debitCard.getAccountId());
            // نكتب رقم البطاقة
            file.writeUTF(debitCard.getCardNumber());
            // نكتب نوع البطاقة
            file.writeUTF(debitCard.getCardType());
            // نكتب تاريخ انتهاء البطاقة
            file.writeUTF(debitCard.getExpiryDate());
            // نحفظ إذا البطاقة فعالة أو لا
            file.writeBoolean(debitCard.isActive());
            // نقفل ملف البطاقة بعد ما نخلص
            file.close();
            // نحفظ اسم ملف البطاقة عشان نقدر نقراه تلقائيًا بعدين
            saveDebitCardFileName(fileName);

        } catch (IOException e) {

            // نطبع رسالة إذا صار خطأ أثناء حفظ البطاقة
            System.out.println("Error saving debit card file.");
        }
    }

    // نحفظ اسم ملف بطاقة الخصم عشان نقدر نقراه تلقائيًا بعدين
    public static void saveDebitCardFileName(String fileName) {

        try {
            // نفتح ملف أسماء البطاقات للقراءة والكتابة
            RandomAccessFile file =
                    new RandomAccessFile("DebitCardFiles.txt", "rw");
            // نفترض في البداية إن اسم الملف مو موجود
            boolean fileNameExists = false;
            // نمر على كل أسماء ملفات البطاقات المحفوظة
            while (file.getFilePointer() < file.length()) {
                // نقرا اسم ملف محفوظ
                String savedFileName = file.readUTF();
                // نتأكد إذا اسم الملف موجود من قبل
                if (savedFileName.equals(fileName)) {
                    // نحدد إن الملف موجود
                    fileNameExists = true;
                    // نوقف البحث
                    break;
                }
            }

            // إذا اسم الملف مو موجود من قبل
            if (!fileNameExists) {
                // نروح إلى نهاية الملف
                file.seek(file.length());
                // نحفظ اسم ملف البطاقة
                file.writeUTF(fileName);
            }
            // نقفل الملف بعد ما نخلص
            file.close();

        } catch (IOException e) {
            // نطبع رسالة إذا صار خطأ
            System.out.println("Error saving debit card file name.");
        }
    }


    // نقرا بيانات بطاقة الخصم من الملف
    public static DebitCard readDebitCard(String fileName) {

        try {
            // نفتح ملف البطاقة للقراءة فقط
            RandomAccessFile file =
                    new RandomAccessFile(fileName, "r");
            // نقرا رقم تعريف البطاقة
            String cardId = file.readUTF();
            // نقرا رقم الحساب المرتبط بالبطاقة
            String accountId = file.readUTF();
            // نقرا رقم البطاقة
            String cardNumber = file.readUTF();
            // نقرا نوع البطاقة
            String cardType = file.readUTF();
            // نقرا تاريخ انتهاء البطاقة
            String expiryDate = file.readUTF();
            // نقرا إذا البطاقة فعالة أو لا
            boolean isActive = file.readBoolean();

            // نسوي بطاقة باستخدام البيانات اللي قريناها
            DebitCard debitCard = new DebitCard(cardId, accountId, cardNumber, cardType,
                    expiryDate);

            // نرجع حالة البطاقة مثل ما كانت محفوظة
            debitCard.setActive(isActive);
            // نقفل الملف بعد ما نخلص
            file.close();
            // نرجع البطاقة
            return debitCard;

        } catch (IOException e) {
            // نطبع رسالة إذا صار خطأ أثناء قراءة البطاقة
            System.out.println("Error reading debit card file.");
        }

        // إذا ما قدرنا نقرا البطاقة نرجع null
        return null;
    }

    // نحفظ العملية البنكية داخل ملف
    public static void saveTransaction(Transaction transaction) {
        // نسوي اسم ملف العمليات حسب رقم الحساب
        String fileName = "Transactions-" + transaction.getAccountId() + ".txt";

        try {
            // نفتح ملف العمليات للقراءة والكتابة
            RandomAccessFile file =
                    new RandomAccessFile(fileName, "rw");
            // نروح لنهاية الملف عشان ما نمسح العمليات القديمة
            file.seek(file.length());
            // نكتب رقم العملية
            file.writeUTF(transaction.getTransactionId());
            // نكتب رقم الحساب
            file.writeUTF(transaction.getAccountId());
            // نكتب نوع العملية
            file.writeUTF(transaction.getTransactionType());
            // نكتب مبلغ العملية
            file.writeDouble(transaction.getAmount());
            // نكتب الرصيد بعد العملية
            file.writeDouble(transaction.getBalanceAfter());
            // نكتب تاريخ ووقت العملية
            file.writeUTF(transaction.getDateTime());
            // نقفل الملف بعد ما نخلص
            file.close();
            // نحفظ اسم ملف العمليات عشان نقدر نقراه تلقائيًا بعدين
            saveTransactionFileName(fileName);

        } catch (IOException e) {

            // نطبع رسالة إذا صار خطأ أثناء حفظ العملية
            System.out.println("Error saving transaction file.");
        }
    }

    // نحفظ اسم ملف العمليات عشان نقدر نقراه تلقائيًا بعدين
    public static void saveTransactionFileName(String fileName) {

        try {

            // نفتح ملف أسماء ملفات العمليات للقراءة والكتابة
            RandomAccessFile file =
                    new RandomAccessFile("TransactionFiles.txt", "rw");
            // نفترض في البداية إن اسم الملف مو موجود
            boolean fileNameExists = false;
            // نمر على كل أسماء ملفات العمليات المحفوظة
            while (file.getFilePointer() < file.length()) {
                // نقرا اسم ملف محفوظ
                String savedFileName = file.readUTF();
                // نتأكد إذا اسم الملف موجود من قبل
                if (savedFileName.equals(fileName)) {
                    // نحدد إن اسم الملف موجود
                    fileNameExists = true;
                    // نوقف البحث
                    break;
                }
            }

            // إذا اسم الملف مو موجود من قبل
            if (!fileNameExists) {
                // نروح إلى نهاية الملف
                file.seek(file.length());
                // نحفظ اسم ملف العمليات
                file.writeUTF(fileName);
            }

            // نقفل الملف بعد ما نخلص
            file.close();
        } catch (IOException e) {
            // نطبع رسالة إذا صار خطأ
            System.out.println("Error saving transaction file name.");
        }
    }

    // نقرا أسماء ملفات العمليات المحفوظة
    public static ArrayList<String> readTransactionFileNames() {
        // نسوي قائمة نخزن فيها أسماء ملفات العمليات
        ArrayList<String> transactionFileNames = new ArrayList<>();
        try {
            // نفتح ملف أسماء العمليات للقراءة فقط
            RandomAccessFile file =
                    new RandomAccessFile("TransactionFiles.txt", "r");
            // نستمر في القراءة لين نوصل لنهاية الملف
            while (file.getFilePointer() < file.length()) {
                // نقرا اسم ملف العمليات
                String fileName = file.readUTF();
                // نضيف اسم الملف إلى القائمة
                transactionFileNames.add(fileName);
            }
            // نقفل الملف بعد ما نخلص
            file.close();

        } catch (IOException e) {
            // إذا الملف مو موجود للحين نرجع قائمة فاضية
            System.out.println("No saved transaction files found.");
        }

        // نرجع قائمة أسماء ملفات العمليات
        return transactionFileNames;
    }

    // نقرا العمليات البنكية من الملف
    public static ArrayList<Transaction> readTransactions(String fileName) {
        // نسوي قائمة نخزن فيها العمليات
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            // نفتح ملف العمليات للقراءة فقط
            RandomAccessFile file =
                    new RandomAccessFile(fileName, "r");
            // نكرر القراءة لين نهاية الملف
            while (file.getFilePointer() < file.length()) {
                // نقرا رقم العملية
                String transactionId = file.readUTF();
                // نقرا رقم الحساب
                String accountId = file.readUTF();
                // نقرا نوع العملية
                String transactionType = file.readUTF();
                // نقرا مبلغ العملية
                double amount = file.readDouble();
                // نقرا الرصيد بعد العملية
                double balanceAfter = file.readDouble();
                // نقرا تاريخ ووقت العملية
                String dateTime = file.readUTF();
                // نسوي Transaction من البيانات
                Transaction transaction = new Transaction(
                        transactionId, accountId, transactionType, amount,
                        balanceAfter, dateTime);
                // نضيف العملية إلى القائمة
                transactions.add(transaction);
            }
            // نقفل الملف
            file.close();
        } catch (IOException e) {
            // نطبع رسالة إذا صار خطأ
            System.out.println("Error reading transaction file.");
        }
        // نرجع قائمة العمليات
        return transactions;
    }
}
