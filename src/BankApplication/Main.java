package BankApplication;
// نستخدم نظام البنك داخل البرنامج الرئيسي
import BankingSystem.BankSystem;
// نستخدم Scanner عشان ناخذ إدخال من المستخدم عن طريق الكيبورد
import java.util.Scanner;
// نستخدم كلاس Banker عشان ننشئ موظف بنك للتجربة
import BankModels.Banker;
// نستخدم أداة الهاشنق عشان نخزن كلمة مرور موظف البنك كـ Hash
import BankUtilities.PasswordHashingUtility;
// نستخدم User عشان نخزن المستخدم إذا نجح تسجيل الدخول
import BankModels.User;
// نستخدم Optional لأن تسجيل الدخول ممكن ينجح أو يفشل
import java.util.Optional;
import BankUtilities.FileHandlingUtility;
import BankModels.Customer;
import BankModels.Account;
import java.util.ArrayList;
import BankModels.Transaction;

public class Main {
    // هذه الميثود هي أول مكان يبدأ منه تشغيل البرنامج
    public static void main(String[] args) {
        // ننشئ نظام البنك عشان نقدر نستخدم الخدمات الموجودة داخله
        BankSystem bankSystem = new BankSystem();
        // نقرا أسماء كل ملفات العملاء المحفوظة
        ArrayList<String> customerFileNames =
                FileHandlingUtility.readCustomerFileNames();
         // نمر على كل اسم ملف عميل
        for (String customerFileName : customerFileNames) {
            // نقرا بيانات العميل من الملف
            Customer savedCustomer =
                    FileHandlingUtility.readCustomer(customerFileName);
            // نتأكد إن العميل انقرا بنجاح
            if (savedCustomer != null) {
                // نضيف العميل إلى قائمة المستخدمين عشان يقدر يسجل دخول
                bankSystem.getLoginService().addUser(savedCustomer);
                // نضيف العميل إلى قائمة العملاء في النظام
                bankSystem.getCustomerService().addCustomer(savedCustomer);
            }
        }
        // نقرا أسماء كل ملفات الحسابات المحفوظة
        ArrayList<String> accountFileNames =
                FileHandlingUtility.readAccountFileNames();
         // نمر على كل اسم ملف حساب
        for (String accountFileName : accountFileNames) {
            // نقرا بيانات الحساب من الملف
            Account savedAccount =
                    FileHandlingUtility.readAccount(accountFileName);
            // نتأكد إن الحساب انقرا بنجاح
            if (savedAccount != null) {
                // نضيف الحساب إلى قائمة الحسابات داخل النظام
                bankSystem.getAccountService().addAccount(savedAccount);
            }
        }


        // نقرا أسماء كل ملفات العمليات المحفوظة
        ArrayList<String> transactionFileNames =
                FileHandlingUtility.readTransactionFileNames();
        // نمر على كل اسم ملف عمليات
        for (String transactionFileName : transactionFileNames) {
            // نقرا كل العمليات الموجودة داخل الملف
            ArrayList<Transaction> savedTransactions =
                    FileHandlingUtility.readTransactions(transactionFileName);
            // نمر على كل عملية قريناها من الملف
            for (Transaction transaction : savedTransactions) {
                // نرجع نضيف العملية إلى TransactionService
                bankSystem.getTransactionService().addTransaction(transaction);
            }
        }
        // نحاول نقرا موظف البنك من الملف
        Banker banker =
                FileHandlingUtility.readBanker("Banker-Banker-B022.txt");

// إذا ملف موظف البنك مو موجود ننشئ موظف بنك مبدئي
        if (banker == null) {
            // نحول كلمة مرور موظف البنك إلى Hash
            String bankerPasswordHash =
                    PasswordHashingUtility.hashPassword("1234");
            // ننشئ موظف البنك لأول مرة
            banker = new Banker(
                    "B022",
                    "U022",
                    "Banker",
                    "banker09@acme.com",
                    "33330933",
                    bankerPasswordHash
            );

            // نحفظ بيانات موظف البنك في الملف
            FileHandlingUtility.saveBanker(banker);
        }
        // نضيف موظف البنك إلى قائمة المستخدمين عشان يقدر يسجل دخول
        bankSystem.getLoginService().addUser(banker);
        // ننشئ Scanner عشان نقرأ إدخال المستخدم من الكيبورد
        Scanner scanner = new Scanner(System.in);
        // نستخدمه عشان نخلي البرنامج يشتغل لين المستخدم يختار الخروج
        boolean running = true;
        // نخلي القائمة تستمر بالظهور طول ما running تساوي true
        while (running) {
            System.out.println("Welcome to ACME Bank System");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Please select an option: ");
            // نقرا اختيار المستخدم من الكيبورد
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    // المستخدم اختار تسجيل الدخول
                    System.out.println("Login selected.");
                    // ننظف السطر المتبقي بعد قراءة الرقم عشان نقدر نقرأ النص بشكل صحيح
                    scanner.nextLine();
                    // نطلب من المستخدم يدخل رقم المستخدم
                    System.out.print("Enter User ID: ");
                    String userId = scanner.nextLine();
                    // نطلب من المستخدم يدخل كلمة المرور
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    // نجرب تسجيل الدخول باستخدام رقم المستخدم وكلمة المرور
                    Optional<User> loggedInUser =
                            bankSystem.getLoginService().login(userId, password);
                    // إذا تسجيل الدخول نجح والمستخدم موجود
                    if (loggedInUser.isPresent()) {
                        System.out.println("Login successful.");
                        // نطلع المستخدم من داخل Optional عشان نقدر نعرف نوعه وصلاحياته
                        User currentUser = loggedInUser.get();
                        // اذا المستخدم موظف بنك نفتح له قائمة موظف البنك
                        if (currentUser.getRole().equals("BANKER")) {
                            BankerMenu.showBankerMenu(scanner, bankSystem);

                        } else if (currentUser.getRole().equals("CUSTOMER")) {
                            // نحول User إلى Customer عشان نقدر نعرف إذا لازم يغير الباسورد
                            Customer customer = (Customer) currentUser;
                            // إذا العميل للحين يستخدم كلمة المرور المؤقتة
                            if (customer.isMustChangePassword()) {
                                System.out.println("You must change your temporary password.");
                                // نطلب من العميل كلمة مرور جديدة
                                System.out.print("Enter New Password: ");
                                String newPassword = scanner.nextLine();
                                // نحول كلمة المرور الجديدة إلى Hash
                                String newPasswordHash =
                                        PasswordHashingUtility.hashPassword(newPassword);
                                // نخزن كلمة المرور الجديدة
                                customer.setPasswordHash(newPasswordHash);
                                // خلاص العميل غير كلمة المرور المؤقتة
                                customer.setMustChangePassword(false);
                                // نحفظ التغيير في ملف العميل
                                FileHandlingUtility.saveCustomer(customer);
                                System.out.println("Password changed successfully.");
                            }
                            CustomerMenu.showCustomerMenu(scanner, bankSystem, currentUser);
                        }
                    } else {
                        // إذا المستخدم مو مقفول نطبع رسالة إن بيانات الدخول غلط
                        if (!bankSystem.getLoginService().isUserLocked(userId)) {
                            System.out.println("Invalid User ID or Password.");
                        }
                    }
                    break;
                case 2:
                    // المستخدم اختار الخروج من النظام
                    System.out.println("Thank you for using ACME Bank System.");
                    // نخلي running تساوي false عشان نوقف الـ while ونطلع من البرنامج
                    running = false;
                    break;
                default:
                    // اذا المستخدم كتب خيار غير موجود في القائمة
                    System.out.println("Invalid option. Please try again.");

            }
        }
        // نقفل ال Scanner بعد ما ينتهي البرنامج
        scanner.close();
    }
}
