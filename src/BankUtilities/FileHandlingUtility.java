package BankUtilities;
import java.io.RandomAccessFile;
// نستخدم RandomAccessFile عشان نقرا ونكتب البيانات داخل الملفات
import java.io.IOException;
// نستخدم IOException عشان نتعامل مع أخطاء الملفات
import BankModels.Customer;
// نستخدم Customer عشان نحفظ ونقرا بيانات العميل
import BankModels.Account;
// نستخدم Account عشان نحفظ ونقرا بيانات الحساب
import BankModels.Transaction;
// نستخدم Transaction عشان نحفظ ونقرا العمليات البنكية
import BankModels.Banker;
// نستخدم Banker عشان نحفظ ونقرا بيانات موظف البنك
import BankModels.DebitCard;
// نستخدم DebitCard عشان نحفظ ونقرا بيانات البطاقة
import java.util.ArrayList;
// نستخدم ArrayList عشان نخزن مجموعة من البيانات

public class FileHandlingUtility {
    public static void saveCustomer(Customer customer) {
        // نحفظ بيانات العميل داخل ملف نصي واضح

        String fileName = "Customer-" + customer.getName()
                + "-" + customer.getCustomerId() + ".txt";
        // نسوي اسم الملف باستخدام اسم العميل ورقم العميل

        try {
            // نبدأ محاولة حفظ الملف

            RandomAccessFile file = new RandomAccessFile(fileName, "rw");
            // نفتح ملف العميل للقراءة والكتابة

            file.setLength(0);
            // نمسح البيانات القديمة قبل ما نكتب البيانات الجديدة

            file.writeBytes("Customer ID: " + customer.getCustomerId() + "\n");
            // نحفظ رقم العميل

            file.writeBytes("User ID: " + customer.getUserId() + "\n");
            // نحفظ رقم المستخدم

            file.writeBytes("Name: " + customer.getName() + "\n");
            // نحفظ اسم العميل

            file.writeBytes("Email: " + customer.getEmail() + "\n");
            // نحفظ إيميل العميل

            file.writeBytes("Phone: " + customer.getPhone() + "\n");
            // نحفظ رقم تلفون العميل

            file.writeBytes("Password Hash: " + customer.getPasswordHash() + "\n");
            // نحفظ كلمة المرور المشفرة

            file.writeBytes("Must Change Password: "
                    + customer.isMustChangePassword() + "\n");
            // نحفظ إذا العميل لازم يغير كلمة المرور المؤقتة

            file.close();
            // نقفل الملف بعد ما نخلص

            saveCustomerFileName(fileName);
            // نحفظ اسم ملف العميل عشان نقدر نحمله مرة ثانية

        } catch (IOException e) {
            // إذا صار خطأ أثناء الحفظ ندخل هنا

            System.out.println("Error saving customer file.");
            // نطبع رسالة خطأ
        }
    }

    // نحفظ بيانات العميل مع كل الحسابات التابعة له
    public static void saveCustomerWithAccounts(Customer customer,
            ArrayList<Account> accounts) {
        // نسوي اسم ملف العميل باستخدام اسمه ورقمه
        String fileName = "Customer-" + customer.getName()
                + "-" + customer.getCustomerId() + ".txt";

        try {
            // نفتح ملف العميل للقراءة والكتابة
            RandomAccessFile file = new RandomAccessFile(fileName, "rw");
            // نمسح المحتوى القديم عشان نكتب أحدث البيانات
            file.setLength(0);
            // نحفظ رقم العميل
            file.writeBytes("Customer ID: " + customer.getCustomerId() + "\n");
            // نحفظ رقم المستخدم
            file.writeBytes("User ID: " + customer.getUserId() + "\n");
            // نحفظ اسم العميل
            file.writeBytes("Name: " + customer.getName() + "\n");
            // نحفظ إيميل العميل
            file.writeBytes("Email: " + customer.getEmail() + "\n");
            // نحفظ رقم تلفون العميل
            file.writeBytes("Phone: " + customer.getPhone() + "\n");
            // نحفظ كلمة المرور المشفرة
            file.writeBytes("Password Hash: " + customer.getPasswordHash() + "\n");
            // نحفظ إذا العميل لازم يغير كلمة المرور
            file.writeBytes("Must Change Password: " + customer.isMustChangePassword()
                    + "\n");

            // نضيف عنوان قبل بيانات الحسابات
            file.writeBytes("\n ACCOUNTS \n");
            // نمر على كل الحسابات الموجودة في النظام
            for (Account account : accounts) {
                // نتأكد إن الحساب تابع لهذا العميل فقط
                if (account.getCustomerId().equals(customer.getCustomerId())) {

                    // نحفظ رقم الحساب
                    file.writeBytes("\nAccount ID: " + account.getAccountId() + "\n");

                    // نحفظ نوع الحساب
                    file.writeBytes("Account Type: " + account.getAccountType() + "\n");

                    // نحفظ الرصيد الحالي
                    file.writeBytes("Balance: " + account.getBalance() + "\n");

                    // نحفظ إذا الحساب فعال أو لا
                    file.writeBytes("Active: " + account.isActive() + "\n");

                    // نحفظ عدد مرات الـ Overdraft
                    file.writeBytes("Overdraft Count: " + account.getOverdraftCount() + "\n");
                }
            }

            // نقفل الملف بعد ما نخلص
            file.close();
            // نحفظ اسم ملف العميل في CustomerFiles
            saveCustomerFileName(fileName);
        } catch (IOException e) {
            // نطبع رسالة إذا صار خطأ أثناء الحفظ
            System.out.println("Error saving customer with accounts.");
        }
    }

    public static Customer readCustomer(String fileName) {
        // نقرا بيانات العميل من الملف النصي

        try {
            // نبدأ محاولة قراءة الملف
            RandomAccessFile file = new RandomAccessFile(fileName, "r");
            // نفتح ملف العميل للقراءة فقط
            String customerId = file.readLine().replace("Customer ID: ", "");
            // نقرا رقم العميل
            String userId = file.readLine().replace("User ID: ", "");
            // نقرا رقم المستخدم
            String name = file.readLine().replace("Name: ", "");
            // نقرا اسم العميل
            String email = file.readLine().replace("Email: ", "");
            // نقرا إيميل العميل
            String phone = file.readLine().replace("Phone: ", "");
            // نقرا رقم تلفون العميل
            String passwordHash = file.readLine().replace("Password Hash: ", "");
            // نقرا كلمة المرور المشفرة
            boolean mustChangePassword = Boolean.parseBoolean(
                    file.readLine().replace("Must Change Password: ", "")
            );
            // نقرا حالة تغيير كلمة المرور ونحولها إلى boolean

            Customer customer = new Customer(customerId, userId, name, email, phone,
                    passwordHash);
            // نسوي Customer باستخدام البيانات اللي قريناها
            customer.setMustChangePassword(mustChangePassword);
            // نرجع حالة تغيير كلمة المرور مثل ما كانت محفوظة
            file.close();
            // نقفل الملف بعد ما نخلص
            return customer;
            // نرجع العميل للنظام
        } catch (IOException e) {
            // إذا صار خطأ أثناء القراءة ندخل هنا
            System.out.println("Error reading customer file.");
            // نطبع رسالة خطأ
        }

        return null;
        // إذا ما قدرنا نقرا العميل نرجع null
    }
    public static void saveCustomerFileName(String fileName) {
        // نحفظ اسم ملف العميل داخل قائمة ملفات العملاء
        try {
            // نبدأ محاولة حفظ اسم الملف
            RandomAccessFile file = new RandomAccessFile("" +
                    "CustomerFiles.txt", "rw");
            // نفتح ملف أسماء العملاء
            boolean fileNameExists = false;
            // نفترض إن اسم الملف مو موجود
            while (file.getFilePointer() < file.length()) {
                // نمر على كل أسماء الملفات المحفوظة
                String savedFileName = file.readUTF();
                // نقرا اسم ملف محفوظ
                if (savedFileName.equals(fileName)) {
                    // نتأكد إذا الاسم موجود من قبل
                    fileNameExists = true;
                    // نحدد إن الاسم موجود
                    break;
                }
            }
            if (!fileNameExists) {
                // إذا الاسم مو موجود من قبل
                file.seek(file.length());
                // نروح إلى نهاية الملف
                file.writeUTF(fileName);
                // نحفظ اسم الملف
            }
            file.close();
            // نقفل الملف بعد ما نخلص
        } catch (IOException e) {
            // إذا صار خطأ أثناء الحفظ ندخل هنا
            System.out.println("Error saving customer file name.");
            // نطبع رسالة خطأ
        }
    }

    public static ArrayList<String> readCustomerFileNames() {
        // نقرا أسماء ملفات العملاء المحفوظة
        ArrayList<String> customerFileNames = new ArrayList<>();
        // نسوي قائمة نخزن فيها أسماء الملفات
        try {
            // نبدأ محاولة قراءة الملف
            RandomAccessFile file = new RandomAccessFile("CustomerFiles.txt", "r");
            // نفتح ملف أسماء العملاء للقراءة
            while (file.getFilePointer() < file.length()) {
                // نستمر في القراءة لين نهاية الملف
                String fileName = file.readUTF();
                // نقرا اسم ملف العميل
                customerFileNames.add(fileName);
                // نضيف اسم الملف إلى القائمة
            }

            file.close();
            // نقفل الملف بعد ما نخلص
        } catch (IOException e) {
            // إذا الملف مو موجود أو صار خطأ ندخل هنا
            System.out.println("No saved customer files found.");
            // نطبع رسالة توضيحية
        }

        return customerFileNames;
        // نرجع قائمة أسماء ملفات العملاء
    }

    public static void saveBanker(Banker banker) {
        // نحفظ بيانات موظف البنك داخل ملف نصي واضح
        String fileName = "Banker-" + banker.getName()
                + "-" + banker.getBankerId() + ".txt";
        // نسوي اسم الملف باستخدام اسم الموظف ورقمه

        try {
            // نبدأ محاولة حفظ الملف
            RandomAccessFile file = new RandomAccessFile(fileName, "rw");
            // نفتح ملف موظف البنك للقراءة والكتابة
            file.setLength(0);
            // نمسح البيانات القديمة قبل ما نكتب البيانات الجديدة
            file.writeBytes("Banker ID: " + banker.getBankerId() + "\n");
            // نحفظ رقم موظف البنك
            file.writeBytes("User ID: " + banker.getUserId() + "\n");
            // نحفظ رقم المستخدم
            file.writeBytes("Name: " + banker.getName() + "\n");
            // نحفظ اسم موظف البنك
            file.writeBytes("Email: " + banker.getEmail() + "\n");
            // نحفظ إيميل موظف البنك
            file.writeBytes("Phone: " + banker.getPhone() + "\n");
            // نحفظ رقم تلفون موظف البنك
            file.writeBytes("Password Hash: " + banker.getPasswordHash() + "\n");
            // نحفظ كلمة المرور المشفرة
            file.close();
            // نقفل الملف بعد ما نخلص
        } catch (IOException e) {
            // إذا صار خطأ أثناء الحفظ ندخل هنا
            System.out.println("Error saving banker file.");
            // نطبع رسالة خطأ
        }
    }

    public static Banker readBanker(String fileName) {
        // نقرا بيانات موظف البنك من الملف النصي

        try {
            // نبدأ محاولة قراءة الملف
            RandomAccessFile file = new RandomAccessFile(fileName, "r");
            // نفتح ملف موظف البنك للقراءة فقط
            String bankerId = file.readLine().replace("Banker ID: ", "");
            // نقرا رقم موظف البنك
            String userId = file.readLine().replace("User ID: ", "");
            // نقرا رقم المستخدم
            String name = file.readLine().replace("Name: ", "");
            // نقرا اسم موظف البنك
            String email = file.readLine().replace("Email: ", "");
            // نقرا إيميل موظف البنك
            String phone = file.readLine().replace("Phone: ", "");
            // نقرا رقم تلفون موظف البنك
            String passwordHash = file.readLine().replace("Password Hash: ", "");
            // نقرا كلمة المرور المشفرة
            Banker banker = new Banker(bankerId, userId, name, email,
                    phone, passwordHash);
            // نسوي Banker باستخدام البيانات اللي قريناها

            file.close();
            // نقفل الملف بعد ما نخلص
            return banker;
            // نرجع موظف البنك للنظام
        } catch (IOException e) {
            // إذا صار خطأ أثناء القراءة ندخل هنا
            System.out.println("Error reading banker file.");
            // نطبع رسالة خطأ
        }

        return null;
        // إذا ما قدرنا نقرا موظف البنك نرجع null
    }

    public static void saveAccount(Account account) {
        // نحفظ بيانات الحساب داخل ملف نصي واضح
        String fileName = "Account-" + account.getAccountId() + ".txt";
        // نسوي اسم الملف باستخدام رقم الحساب
        try {
            // نبدأ محاولة حفظ الملف
            RandomAccessFile file = new RandomAccessFile(fileName, "rw");
            // نفتح ملف الحساب للقراءة والكتابة
            file.setLength(0);
            // نمسح البيانات القديمة قبل ما نكتب البيانات الجديدة
            file.writeBytes("Account ID: " + account.getAccountId() + "\n");
            // نحفظ رقم الحساب
            file.writeBytes("Customer ID: " + account.getCustomerId() + "\n");
            // نحفظ رقم العميل صاحب الحساب
            file.writeBytes("Account Type: " + account.getAccountType() + "\n");
            // نحفظ نوع الحساب
            file.writeBytes("Balance: " + account.getBalance() + "\n");
            // نحفظ الرصيد الحالي
            file.writeBytes("Active: " + account.isActive() + "\n");
            // نحفظ إذا الحساب فعال أو لا
            file.writeBytes("Overdraft Count: " + account.getOverdraftCount() + "\n");
            // نحفظ عدد مرات الـ Overdraft
            file.close();
            // نقفل الملف بعد ما نخلص
            saveAccountFileName(fileName);
            // نحفظ اسم ملف الحساب عشان نقدر نحمله مرة ثانية

        } catch (IOException e) {
            // إذا صار خطأ أثناء الحفظ ندخل هنا
            System.out.println("Error saving account file.");
            // نطبع رسالة خطأ
        }
    }

    public static Account readAccount(String fileName) {
        // نقرا بيانات الحساب من الملف النصي
        try {
            // نبدأ محاولة قراءة الملف
            RandomAccessFile file = new RandomAccessFile(fileName, "r");
            // نفتح ملف الحساب للقراءة فقط
            String accountId = file.readLine().replace("Account ID: ", "");
            // نقرا رقم الحساب
            String customerId = file.readLine().replace("Customer ID: ", "");
            // نقرا رقم العميل صاحب الحساب
            String accountType = file.readLine().replace("Account Type: ", "");
            // نقرا نوع الحساب
            double balance = Double.parseDouble(
                    file.readLine().replace("Balance: ", "")
            );
            // نقرا الرصيد ونحوله إلى double
            boolean isActive = Boolean.parseBoolean(
                    file.readLine().replace("Active: ", "")
            );
            // نقرا حالة الحساب ونحولها إلى boolean
            int overdraftCount = Integer.parseInt(
                    file.readLine().replace("Overdraft Count: ", "")
            );
            // نقرا عدد مرات الـ Overdraft ونحوله إلى int
            Account account = new Account(accountId, customerId, accountType, balance);
            // نسوي Account باستخدام البيانات اللي قريناها
            account.setActive(isActive);
            // نرجع حالة الحساب مثل ما كانت محفوظة
            account.setOverdraftCount(overdraftCount);
            // نرجع عدد مرات الـ Overdraft
            file.close();
            // نقفل الملف بعد ما نخلص
            return account;
            // نرجع الحساب للنظام
        } catch (IOException e) {
            // إذا صار خطأ أثناء القراءة ندخل هنا
            System.out.println("Error reading account file.");
            // نطبع رسالة خطأ
        }
        return null;
        // إذا ما قدرنا نقرا الحساب نرجع null
    }

    public static void saveAccountFileName(String fileName) {
        // نحفظ اسم ملف الحساب داخل قائمة ملفات الحسابات
        try {
            // نبدأ محاولة حفظ اسم الملف
            RandomAccessFile file = new RandomAccessFile("AccountFiles.txt", "rw");
            // نفتح ملف أسماء الحسابات
            boolean fileNameExists = false;
            // نفترض إن اسم الملف مو موجود
            while (file.getFilePointer() < file.length()) {
                // نمر على كل أسماء الملفات المحفوظة
                String savedFileName = file.readUTF();
                // نقرا اسم ملف محفوظ
                if (savedFileName.equals(fileName)) {
                    // نتأكد إذا الاسم موجود من قبل
                    fileNameExists = true;
                    // نحدد إن الاسم موجود
                    break;
                }
            }

            if (!fileNameExists) {
                // إذا الاسم مو موجود من قبل
                file.seek(file.length());
                // نروح إلى نهاية الملف
                file.writeUTF(fileName);
                // نحفظ اسم الملف
            }

            file.close();
            // نقفل الملف بعد ما نخلص
        } catch (IOException e) {
            // إذا صار خطأ أثناء الحفظ ندخل هنا
            System.out.println("Error saving account file name.");
            // نطبع رسالة خطأ
        }
    }

    public static ArrayList<String> readAccountFileNames() {
        // نقرا أسماء ملفات الحسابات المحفوظة
        ArrayList<String> accountFileNames = new ArrayList<>();
        // نسوي قائمة نخزن فيها أسماء ملفات الحسابات

        try {
            // نبدأ محاولة قراءة الملف
            RandomAccessFile file = new RandomAccessFile("AccountFiles.txt", "r");
            // نفتح ملف أسماء الحسابات للقراءة
            while (file.getFilePointer() < file.length()) {
                // نستمر في القراءة لين نهاية الملف
                String fileName = file.readUTF();
                // نقرا اسم ملف الحساب
                accountFileNames.add(fileName);
                // نضيف اسم الملف إلى القائمة
            }

            file.close();
            // نقفل الملف بعد ما نخلص
        } catch (IOException e) {
            // إذا الملف مو موجود أو صار خطأ ندخل هنا
            System.out.println("No saved account files found.");
            // نطبع رسالة توضيحية
        }

        return accountFileNames;
        // نرجع قائمة أسماء ملفات الحسابات
    }

    // نحفظ بيانات بطاقة الخصم داخل ملف نصي
    public static void saveDebitCard(DebitCard debitCard) {
        // نسوي اسم الملف باستخدام Card ID
        String fileName = "DebitCard-" + debitCard.getCardId() + ".txt";

        try {
            // نفتح ملف البطاقة للكتابة
            RandomAccessFile file = new RandomAccessFile(fileName, "rw");
            // نمسح البيانات القديمة قبل ما نحفظ البيانات الجديدة
            file.setLength(0);
            // نحفظ رقم تعريف البطاقة
            file.writeBytes("Card ID: " + debitCard.getCardId() + "\n");

            // نحفظ رقم الحساب المرتبط بالبطاقة
            file.writeBytes("Account ID: " + debitCard.getAccountId() + "\n");

            // نحفظ رقم البطاقة
            file.writeBytes("Card Number: " + debitCard.getCardNumber() + "\n");

            // نحفظ نوع البطاقة
            file.writeBytes("Card Type: " + debitCard.getCardType() + "\n");

            // نحفظ تاريخ انتهاء البطاقة
            file.writeBytes("Expiry Date: " + debitCard.getExpiryDate() + "\n");

            // نحفظ إذا البطاقة فعالة أو لا
            file.writeBytes("Active: " + debitCard.isActive() + "\n");

            // نحفظ إذا العميل عنده طلب Upgrade معلق
            file.writeBytes("Upgrade Requested: " + debitCard.isUpgradeRequested() + "\n");

            // نحفظ نوع البطاقة اللي العميل طلبها
            file.writeBytes("Requested Card Type: " + debitCard.getRequestedCardType() + "\n");
            // نحفظ مجموع السحب المستخدم اليوم
            file.writeBytes("Daily Withdraw Used: " + debitCard.getDailyWithdrawUsed() + "\n");

             // نحفظ مجموع التحويل العادي المستخدم اليوم
            file.writeBytes("Daily Transfer Used: " + debitCard.getDailyTransferUsed() + "\n");

            // نحفظ مجموع التحويل بين حسابات نفس العميل اليوم
            file.writeBytes("Daily Own Transfer Used: " + debitCard.getDailyOwnTransferUsed() + "\n");

            // نحفظ مجموع الإيداع المستخدم اليوم
            file.writeBytes("Daily Deposit Used: " + debitCard.getDailyDepositUsed() + "\n");

            // نحفظ تاريخ آخر استخدام للحدود اليومية
            file.writeBytes("Daily Usage Date: " + debitCard.getDailyUsageDate() + "\n");

            // نقفل الملف بعد ما نخلص
            file.close();
            // نحفظ اسم ملف البطاقة في قائمة ملفات البطاقات
            saveDebitCardFileName(fileName);
        } catch (IOException e) {
            // نطبع رسالة إذا صار خطأ أثناء الحفظ
            System.out.println("Error saving debit card file.");
        }
    }

    // نقرا بيانات بطاقة الخصم من الملف النصي
    public static DebitCard readDebitCard(String fileName) {

        try {

            // نفتح ملف البطاقة للقراءة فقط
            RandomAccessFile file = new RandomAccessFile(fileName, "r");

            // نقرا رقم تعريف البطاقة
            String cardId = file.readLine().replace("Card ID: ", "");

            // نقرا رقم الحساب المرتبط بالبطاقة
            String accountId = file.readLine().replace("Account ID: ", "");

            // نقرا رقم البطاقة
            String cardNumber = file.readLine().replace("Card Number: ", "");

            // نقرا نوع البطاقة
            String cardType = file.readLine().replace("Card Type: ", "");

            // نقرا تاريخ انتهاء البطاقة
            String expiryDate = file.readLine().replace("Expiry Date: ", "");

            // نقرا إذا البطاقة فعالة أو لا
            boolean isActive = Boolean.parseBoolean(
                            file.readLine().replace("Active: ", ""));


            // بالبداية نفترض إن ما في طلب ترقية
            boolean upgradeRequested = false;
            // بالبداية نخلي نوع البطاقة المطلوبة فاضي
            String requestedCardType = "";
            // بالبداية نفترض إن ما تم استخدام أي حد يومي
            double dailyWithdrawUsed = 0;
           // بالبداية نفترض إن ما تم استخدام أي تحويل عادي
            double dailyTransferUsed = 0;
            // بالبداية نفترض إن ما تم استخدام تحويل بين حسابات نفس العميل
            double dailyOwnTransferUsed = 0;
            // بالبداية نفترض إن ما تم استخدام أي إيداع
            double dailyDepositUsed = 0;
            // بالبداية ما عندنا تاريخ استخدام محفوظ
            String dailyUsageDate = "";
            // نتأكد إذا الملف يحتوي على بيانات Upgrade الجديدة
            if (file.getFilePointer() < file.length()) {
                // نقرا سطر طلب الترقية
                String upgradeRequestedLine = file.readLine();
                // نتأكد إن السطر موجود قبل استخدام replace
                if (upgradeRequestedLine != null) {
                    // نحول قيمة طلب الترقية إلى boolean
                    upgradeRequested = Boolean.parseBoolean(upgradeRequestedLine.replace(
                            "Upgrade Requested: ", ""));
                }
            }


            // نتأكد إذا الملف يحتوي على نوع البطاقة المطلوبة
            if (file.getFilePointer() < file.length()) {
                // نقرا سطر نوع البطاقة المطلوبة
                String requestedCardTypeLine = file.readLine();
                // نتأكد إن السطر موجود قبل استخدام replace
                if (requestedCardTypeLine != null) {
                    // ناخذ نوع البطاقة المطلوبة من السطر
                    requestedCardType = requestedCardTypeLine.replace(
                            "Requested Card Type: ", "");
                    // نتأكد إذا الملف يحتوي على قيمة السحب اليومية
                    if (file.getFilePointer() < file.length()) {
                        // نقرا مجموع السحب المستخدم اليوم
                        String line = file.readLine();
                        if (line != null) {dailyWithdrawUsed = Double.parseDouble(
                                    line.replace("Daily Withdraw Used: ", ""));
                        }
                    }


                    // نتأكد إذا الملف يحتوي على قيمة التحويل اليومية
                    if (file.getFilePointer() < file.length()) {
                        // نقرا مجموع التحويل العادي المستخدم اليوم
                        String line = file.readLine();
                        if (line != null) {dailyTransferUsed = Double.parseDouble(
                                line.replace("Daily Transfer Used: ", ""));
                        }
                    }


                      // نتأكد إذا الملف يحتوي على تحويلات الحسابات الخاصة بالعميل
                    if (file.getFilePointer() < file.length()) {
                        // نقرا مجموع التحويل بين حسابات نفس العميل
                        String line = file.readLine();
                        if (line != null) {dailyOwnTransferUsed = Double.parseDouble(
                                line.replace("Daily Own Transfer Used: ", ""));
                        }
                    }


                    // نتأكد إذا الملف يحتوي على قيمة الإيداع اليومية
                    if (file.getFilePointer() < file.length()) {
                        // نقرا مجموع الإيداع المستخدم اليوم
                        String line = file.readLine();
                        if (line != null) {
                            dailyDepositUsed = Double.parseDouble(
                                    line.replace("Daily Deposit Used: ", ""));
                        }
                    }


                        // نتأكد إذا الملف يحتوي على تاريخ الاستخدام
                    if (file.getFilePointer() < file.length()) {
                        // نقرا تاريخ آخر استخدام للحدود اليومية
                        String line = file.readLine();
                        if (line != null) {
                            dailyUsageDate =
                                    line.replace("Daily Usage Date: ", "");
                        }
                    }
                }
            }


            // ننشئ البطاقة باستخدام البيانات اللي قريناها
            DebitCard debitCard = new DebitCard(cardId, accountId, cardNumber, cardType,
                    expiryDate);
            // نرجع حالة البطاقة مثل ما كانت محفوظة
            debitCard.setActive(isActive);
            // نرجع حالة طلب الترقية
            debitCard.setUpgradeRequested(upgradeRequested);
            // نرجع نوع البطاقة المطلوبة
            debitCard.setRequestedCardType(requestedCardType);
            // نرجع قيمة السحب اليومية المحفوظة
            debitCard.setDailyWithdrawUsed(dailyWithdrawUsed);
            // نرجع قيمة التحويل اليومية المحفوظة
            debitCard.setDailyTransferUsed(dailyTransferUsed);
           // نرجع قيمة التحويل بين حسابات نفس العميل
            debitCard.setDailyOwnTransferUsed(dailyOwnTransferUsed);
            // نرجع قيمة الإيداع اليومية
            debitCard.setDailyDepositUsed(dailyDepositUsed);
            // نرجع تاريخ الاستخدام اليومي
            debitCard.setDailyUsageDate(dailyUsageDate);
            // نقفل الملف بعد ما نخلص
            file.close();
            // نرجع البطاقة للنظام
            return debitCard;

        } catch (IOException e) {
            // نطبع رسالة إذا صار خطأ أثناء قراءة البطاقة
            System.out.println("Error reading debit card file.");
        }
        // إذا ما قدرنا نقرا البطاقة نرجع null
        return null;
    }

    public static void saveDebitCardFileName(String fileName) {
        // نحفظ اسم ملف البطاقة داخل قائمة ملفات البطاقات

        try {
            // نبدأ محاولة حفظ اسم الملف
            RandomAccessFile file = new RandomAccessFile("DebitCardFiles.txt", "rw");
            // نفتح ملف أسماء البطاقات
            boolean fileNameExists = false;
            // نفترض إن اسم الملف مو موجود
            while (file.getFilePointer() < file.length()) {
                // نمر على كل أسماء الملفات المحفوظة
                String savedFileName = file.readUTF();
                // نقرا اسم ملف محفوظ
                if (savedFileName.equals(fileName)) {
                    // نتأكد إذا الاسم موجود من قبل
                    fileNameExists = true;
                    // نحدد إن الاسم موجود
                    break;
                }
            }

            if (!fileNameExists) {
                // إذا الاسم مو موجود من قبل
                file.seek(file.length());
                // نروح إلى نهاية الملف
                file.writeUTF(fileName);
                // نحفظ اسم الملف
            }
            file.close();
            // نقفل الملف بعد ما نخلص
        } catch (IOException e) {
            // إذا صار خطأ أثناء الحفظ ندخل هنا
            System.out.println("Error saving debit card file name.");
            // نطبع رسالة خطأ
        }
    }
    // نقرا أسماء ملفات البطاقات المحفوظة
    public static ArrayList<String> readDebitCardFileNames() {
        // نسوي قائمة نخزن فيها أسماء ملفات البطاقات
        ArrayList<String> debitCardFileNames =
                new ArrayList<>();

        try {
            // نفتح ملف أسماء البطاقات للقراءة
            RandomAccessFile file =
                    new RandomAccessFile("DebitCardFiles.txt", "r");

            // نستمر في القراءة لين نهاية الملف
            while (file.getFilePointer() < file.length()) {
                // نقرا اسم ملف البطاقة
                String fileName = file.readUTF();
                // نضيف اسم الملف إلى القائمة
                debitCardFileNames.add(fileName);
            }

            // نقفل الملف
            file.close();

        } catch (IOException e) {
            // إذا ما كان عندنا ملفات بطاقات محفوظة
            System.out.println("No saved debit card files found.");
        }

        // نرجع أسماء ملفات البطاقات
        return debitCardFileNames;
    }

    public static ArrayList<Transaction> readTransactions(String fileName) {
        // نسوي قائمة نخزن فيها العمليات اللي نقراها من الملف
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            // نفتح ملف العمليات للقراءة فقط
            RandomAccessFile file = new RandomAccessFile(fileName, "r");

            // نستمر في القراءة لين نهاية الملف
            while (file.getFilePointer() < file.length()) {
                // نقرا أول سطر من العملية
                String transactionIdLine = file.readLine();
                // إذا السطر فاضي نتخطاه
                if (transactionIdLine == null || transactionIdLine.trim().isEmpty()) {
                    continue;
                }
                // إذا السطر مو بداية Transaction نتخطاه
                if (!transactionIdLine.startsWith("Transaction ID: ")) {
                    continue;
                }
                // ناخذ رقم العملية
                String transactionId = transactionIdLine.replace("Transaction ID: ", "");

                // نقرا رقم الحساب
                String accountId = file.readLine().replace("Account ID: ", "");

                // نقرا نوع العملية
                String transactionType = file.readLine().replace("Transaction Type: ", "");

                // نقرا مبلغ العملية
                double amount = Double.parseDouble(file.readLine().replace(
                        "Amount: ", ""));

                // نقرا الرصيد بعد العملية
                double balanceAfter = Double.parseDouble(file.readLine().replace(
                        "Balance After: ", ""));

                // نقرا تاريخ ووقت العملية
                String dateTime = file.readLine().replace("Date Time: ", "");

                // بالبداية نفترض إن ما في حساب ثاني مرتبط
                String relatedAccountId = "";
                // نقرا السطر اللي عقب التاريخ
                String nextLine = file.readLine();
                // إذا العملية جديدة وفيها Related Account ID
                if (nextLine != null && nextLine.startsWith("Related Account ID: ")) {
                    // ناخذ رقم الحساب الثاني
                    relatedAccountId = nextLine.replace("Related Account ID: ", "");
                    // نقرا سطر الفاصل ونتخطاه
                    file.readLine();
                }

                // ننشئ العملية باستخدام البيانات اللي قريناها
                Transaction transaction = new Transaction(transactionId, accountId, transactionType,
                        amount, balanceAfter, dateTime);
                // نخزن رقم الحساب الثاني إذا كانت العملية Transfer
                transaction.setRelatedAccountId(relatedAccountId);
                // نضيف العملية إلى القائمة
                transactions.add(transaction);
            }

            // نقفل الملف بعد ما نخلص
            file.close();
        } catch (IOException e) {
            // نطبع رسالة إذا صار خطأ أثناء قراءة الملف
            System.out.println("Error reading transaction file.");
        }
        // نرجع كل العمليات اللي قريناها
        return transactions;
    }
    public static void saveTransaction(Transaction transaction) {
        // نحفظ العملية البنكية داخل ملف نصي واضح
        String fileName = "Transactions-" + transaction.getAccountId() + ".txt";
        // نسوي اسم الملف باستخدام رقم الحساب
        try {
            // نبدأ محاولة حفظ العملية
            RandomAccessFile file = new RandomAccessFile(fileName, "rw");
            // نفتح ملف العمليات للقراءة والكتابة
            file.seek(file.length());
            // نروح لنهاية الملف عشان ما نمسح العمليات القديمة
            file.writeBytes("Transaction ID: " + transaction.getTransactionId() + "\n");
            // نحفظ رقم العملية
            file.writeBytes("Account ID: " + transaction.getAccountId() + "\n");
            // نحفظ رقم الحساب
            file.writeBytes("Transaction Type: " + transaction.getTransactionType() + "\n");
            // نحفظ نوع العملية
            file.writeBytes("Amount: " + transaction.getAmount() + "\n");
            // نحفظ مبلغ العملية
            file.writeBytes("Balance After: " + transaction.getBalanceAfter() + "\n");
            // نحفظ الرصيد بعد العملية
            file.writeBytes("Date Time: " + transaction.getDateTime() + "\n");
            // نحفظ تاريخ ووقت العملية
            // ناخذ رقم الحساب الثاني المرتبط بالتحويل
            String relatedAccountId = transaction.getRelatedAccountId();

            // إذا العملية مو Transfer نخلي القيمة فاضية بدل null
            if (relatedAccountId == null) {
                relatedAccountId = "";
            }

              // نحفظ رقم الحساب الثاني المرتبط بالعملية
            file.writeBytes("Related Account ID: " + relatedAccountId + "\n");
            file.writeBytes("\n");
            // نحط فاصل بين كل عملية والعملية اللي بعدها
            file.close();
            // نقفل الملف بعد ما نخلص
            saveTransactionFileName(fileName);
            // نحفظ اسم ملف العمليات عشان نقدر نحمله مرة ثانية
        } catch (IOException e) {
            // إذا صار خطأ أثناء الحفظ ندخل هنا
            System.out.println("Error saving transaction file.");
            // نطبع رسالة خطأ
        }
    }

    public static void saveTransactionFileName(String fileName) {
        // نحفظ اسم ملف العمليات داخل قائمة ملفات العمليات
        try {
            // نبدأ محاولة حفظ اسم الملف
            RandomAccessFile file = new RandomAccessFile("TransactionFiles.txt", "rw");
            // نفتح ملف أسماء العمليات
            boolean fileNameExists = false;
            // نفترض إن اسم الملف مو موجود
            while (file.getFilePointer() < file.length()) {
                // نمر على كل أسماء الملفات المحفوظة
                String savedFileName = file.readUTF();
                // نقرا اسم ملف محفوظ
                if (savedFileName.equals(fileName)) {
                    // نتأكد إذا الاسم موجود من قبل
                    fileNameExists = true;
                    // نحدد إن الاسم موجود
                    break;
                    // نوقف البحث
                }
            }
            if (!fileNameExists) {
                // إذا الاسم مو موجود من قبل
                file.seek(file.length());
                // نروح إلى نهاية الملف
                file.writeUTF(fileName);
                // نحفظ اسم الملف
            }
            file.close();
            // نقفل الملف بعد ما نخلص
        } catch (IOException e) {
            // إذا صار خطأ أثناء الحفظ ندخل هنا
            System.out.println("Error saving transaction file name.");
            // نطبع رسالة خطأ
        }
    }

    public static ArrayList<String> readTransactionFileNames() {
        // نقرا أسماء ملفات العمليات المحفوظة
        ArrayList<String> transactionFileNames = new ArrayList<>();
        // نسوي قائمة نخزن فيها أسماء ملفات العمليات
        try {
            // نبدأ محاولة قراءة الملف
            RandomAccessFile file = new RandomAccessFile("TransactionFiles.txt", "r");
            // نفتح ملف أسماء العمليات للقراءة
            while (file.getFilePointer() < file.length()) {
                // نستمر في القراءة لين نهاية الملف
                String fileName = file.readUTF();
                // نقرا اسم ملف العمليات
                transactionFileNames.add(fileName);
                // نضيف اسم الملف إلى القائمة
            }
            file.close();
            // نقفل الملف بعد ما نخلص

        } catch (IOException e) {
            // إذا الملف مو موجود أو صار خطأ ندخل هنا
            System.out.println("No saved transaction files found.");
            // نطبع رسالة توضيحية
        }
        return transactionFileNames;
        // نرجع قائمة أسماء ملفات العمليات
    }
}