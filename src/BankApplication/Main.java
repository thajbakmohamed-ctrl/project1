package BankApplication;

// نستخدم نظام البنك داخل البرنامج الرئيسي
import BankingSystem.BankSystem;
// نستخدم Scanner عشان ناخذ إدخال من المستخدم عن طريق الكيبورد
import java.util.Scanner;
// نستخدم Banker عشان نتعامل مع موظف البنك
import BankModels.Banker;
// نستخدم أداة الهاشنق عشان نخزن كلمات المرور كـ Hash
import BankUtilities.PasswordHashingUtility;
// نستخدم User عشان نخزن المستخدم إذا نجح تسجيل الدخول
import BankModels.User;
// نستخدم Optional لأن تسجيل الدخول ممكن ينجح أو يفشل
import java.util.Optional;
// نستخدم FileHandlingUtility عشان نقرا ونحفظ البيانات في الملفات
import BankUtilities.FileHandlingUtility;
// نستخدم Customer عشان نتعامل مع بيانات العميل
import BankModels.Customer;
// نستخدم Account عشان نتعامل مع الحسابات
import BankModels.Account;
// نستخدم ArrayList عشان نخزن مجموعة من البيانات
import java.util.ArrayList;
// نستخدم Transaction عشان نتعامل مع العمليات البنكية
import BankModels.Transaction;
// نستخدم DebitCard عشان نحمل البطاقات المحفوظة
import BankModels.DebitCard;
// نستخدم Exception خاصة بقفل الحساب
import BankExceptions.AccountLockedException;

public class Main {
    // هذه الميثود هي أول مكان يبدأ منه تشغيل البرنامج
    public static void main(String[] args) {
        // ننشئ نظام البنك عشان نقدر نستخدم الخدمات الموجودة داخله
        BankSystem bankSystem = new BankSystem();
        // نقرا أسماء كل ملفات العملاء المحفوظة
        ArrayList<String> customerFileNames = FileHandlingUtility.readCustomerFileNames();
        // نمر على كل اسم ملف عميل
        for (String customerFileName : customerFileNames) {
            // نقرا بيانات العميل من الملف
            Customer savedCustomer = FileHandlingUtility.readCustomer(customerFileName);
            // نتأكد إن العميل انقرا بنجاح
            if (savedCustomer != null) {
                // نضيف العميل إلى قائمة المستخدمين عشان يقدر يسجل دخول
                bankSystem.getLoginService().addUser(savedCustomer);
                // نضيف العميل إلى قائمة العملاء في النظام
                bankSystem.getCustomerService().addCustomer(savedCustomer);
            }
        }

        // نقرا أسماء كل ملفات الحسابات المحفوظة
        ArrayList<String> accountFileNames = FileHandlingUtility.readAccountFileNames();
        // نمر على كل اسم ملف حساب
        for (String accountFileName : accountFileNames) {
            // نقرا بيانات الحساب من الملف
            Account savedAccount = FileHandlingUtility.readAccount(accountFileName);
            // نتأكد إن الحساب انقرا بنجاح
            if (savedAccount != null) {
                // نضيف الحساب إلى قائمة الحسابات داخل النظام
                bankSystem.getAccountService().addAccount(savedAccount);
            }
        }
        // نقرا أسماء كل ملفات البطاقات المحفوظة
        ArrayList<String> debitCardFileNames = FileHandlingUtility.readDebitCardFileNames();

        // نمر على كل اسم ملف بطاقة
        for (String debitCardFileName : debitCardFileNames) {
            // نقرا بيانات البطاقة من الملف
            DebitCard savedDebitCard = FileHandlingUtility.readDebitCard(debitCardFileName);

            // نتأكد إن البطاقة انقرت بنجاح
            if (savedDebitCard != null) {

                // نضيف البطاقة إلى قائمة البطاقات داخل النظام
                bankSystem.getDebitCardService().addDebitCard(savedDebitCard);
            }
        }

        // نقرا أسماء كل ملفات العمليات المحفوظة
        ArrayList<String> transactionFileNames =
        FileHandlingUtility.readTransactionFileNames();

        // نمر على كل اسم ملف عمليات
        for (String transactionFileName : transactionFileNames) {

            // نقرا كل العمليات الموجودة داخل الملف
            ArrayList<Transaction> savedTransactions = FileHandlingUtility.readTransactions(
            transactionFileName);

            // نمر على كل عملية قريناها من الملف
            for (Transaction transaction : savedTransactions) {
                // نرجع نضيف العملية إلى TransactionService
                bankSystem.getTransactionService().addTransaction(transaction);
            }
        }

        // نحاول نقرا موظف البنك من الملف
        Banker banker = FileHandlingUtility.readBanker("Banker-Banker-B022.txt");

        // إذا ملف موظف البنك مو موجود ننشئ موظف بنك مبدئي
        if (banker == null) {
            // نحول كلمة مرور موظف البنك إلى Hash
            String bankerPasswordHash = PasswordHashingUtility
                    .hashPassword("1234");
            // ننشئ موظف البنك لأول مرة
            banker = new Banker("B022", "U022", "Banker",
                    "banker09@acme.com", "33330933", bankerPasswordHash);
            // نحفظ بيانات موظف البنك في الملف
            FileHandlingUtility.saveBanker(banker);
        }

        // نضيف موظف البنك إلى قائمة المستخدمين عشان يقدر يسجل دخول
        bankSystem.getLoginService().addUser(banker);
        // ننشئ Scanner عشان نقرأ إدخال المستخدم من الكيبورد
        Scanner scanner = new Scanner(System.in);
        // نستخدم running عشان نخلي البرنامج يشتغل لين المستخدم يختار الخروج
        boolean running = true;

        // نخلي القائمة تستمر بالظهور طول ما running تساوي true
        while (running) {
            // نطبع اسم النظام
            System.out.println("Welcome to ACME Bank System");
            // نطبع خيار تسجيل الدخول
            System.out.println("1. Login");
            // نطبع خيار الخروج
            System.out.println("2. Exit");
            // نطلب من المستخدم يختار
            System.out.print("Please select an option: ");
            // نقرا اختيار المستخدم من الكيبورد
            int choice = scanner.nextInt();
            // نحدد شنو نسوي حسب اختيار المستخدم
            switch (choice) {
                case 1:
                    // المستخدم اختار تسجيل الدخول
                    System.out.println("Login selected.");
                    // ننظف السطر المتبقي بعد قراءة الرقم
                    scanner.nextLine();
                    // نطلب من المستخدم يدخل رقم المستخدم
                    System.out.print("Enter User ID: ");
                    // نقرا User ID
                    String userId = scanner.nextLine();
                    // نطلب من المستخدم يدخل كلمة المرور
                    System.out.print("Enter Password: ");
                    // نقرا كلمة المرور
                    String password = scanner.nextLine();
                    // نحاول نسوي تسجيل الدخول
                    try {
                        // نجرب تسجيل الدخول باستخدام User ID وكلمة المرور
                        Optional<User> loggedInUser = bankSystem.getLoginService()
                                .login(userId, password);
                        // إذا تسجيل الدخول نجح
                        if (loggedInUser.isPresent()) {
                            // نطبع رسالة نجاح
                            System.out.println("Login successful.");
                            // نطلع المستخدم من داخل Optional
                            User currentUser = loggedInUser.get();
                            // نتأكد إذا المستخدم موظف بنك
                            if (currentUser.getRole().equals("BANKER")) {
                                // نفتح قائمة موظف البنك
                                BankerMenu.showBankerMenu(scanner, bankSystem);
                            }

                            else if (currentUser.getRole().equals("CUSTOMER")) {
                                // نحول User إلى Customer
                                Customer customer = (Customer) currentUser;

                                // نتأكد إذا العميل للحين يستخدم الباسورد المؤقت
                                if (customer.isMustChangePassword()) {
                                    // نخبر العميل إنه لازم يغير الباسورد
                                    System.out.println("You must change your temporary password.");
                                    // نطلب منه باسورد جديد
                                    System.out.print("Enter New Password: ");
                                    // نقرا الباسورد الجديد
                                    String newPassword = scanner.nextLine();
                                    // نحول الباسورد الجديد إلى Hash
                                    String newPasswordHash = PasswordHashingUtility.hashPassword(
                                            newPassword);

                                    // نخزن الباسورد الجديد المشفر داخل العميل
                                    customer.setPasswordHash(newPasswordHash);
                                    // نحدد إن العميل خلاص غير الباسورد المؤقت
                                    customer.setMustChangePassword(false);
                                    // نحفظ بيانات العميل مع كل حساباته
                                    FileHandlingUtility.saveCustomerWithAccounts(customer,
                                            bankSystem.getAccountService().getAllAccounts());

                                    // نطبع رسالة نجاح
                                    System.out.println("Password changed successfully.");
                                }
                                // نفتح قائمة العميل بعد تسجيل الدخول
                                CustomerMenu.showCustomerMenu(scanner, bankSystem, currentUser);
                            }

                        } else {

                            // إذا المستخدم مو مقفول نطبع رسالة إن بيانات الدخول غلط
                            if (!bankSystem.getLoginService().isUserLocked(userId)) {
                                // نطبع رسالة خطأ
                                System.out.println("Invalid User ID or Password.");
                            }
                        }
                        // إذا الحساب انقفل نمسك الـException هنا
                    } catch (AccountLockedException e) {
                        // نطبع رسالة القفل بدون ما البرنامج يوقف
                        System.out.println(e.getMessage());
                    }
                    // ننهي case 1
                    break;


                case 2:
                    // المستخدم اختار الخروج من النظام
                    System.out.println("Thank you for using ACME Bank System.");
                    // نخلي running تساوي false عشان نوقف البرنامج
                    running = false;
                    // ننهي case 2
                    break;


                default:
                    // إذا المستخدم كتب خيار غير موجود
                    System.out.println("Invalid option. Please try again.");
            }
        }
        // نقفل Scanner بعد ما ينتهي البرنامج
        scanner.close();
    }
}