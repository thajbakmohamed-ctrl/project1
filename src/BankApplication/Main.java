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
// نستخدم كلاس Account عشان ننشئ حساب جديد للعميل
import BankModels.Account;
// نستخدم كلاس Customer عشان ننشئ العميل الجديد
import BankModels.Customer;
// اري لست للاكاونتات
import java.util.ArrayList;
//الترانزكشن الخاص بكل حساب
import BankModels.Transaction;

public class Main {
    // هذه الميثود هي أول مكان يبدأ منه تشغيل البرنامج
    public static void main(String[] args) {
        // ننشئ نظام البنك عشان نقدر نستخدم الخدمات الموجودة داخله
        BankSystem bankSystem = new BankSystem();
        // نحول كلمة مرور موظف البنك إلى Hash قبل تخزينها
        String bankerPasswordHash = PasswordHashingUtility.hashPassword("1234");
        // ننشئ موظف بنك مبدئي عشان نقدر نجرب تسجيل الدخول
        Banker banker = new Banker(
                "B022",
                "U022",
                "Banker",
                "banker09@acme.com",
                "33330933",
                bankerPasswordHash
        );
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
            // نقرأ اختيار المستخدم من الكيبورد
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
                        // إذا المستخدم موظف بنك نفتح له قائمة موظف البنك
                        if (currentUser.getRole().equals("BANKER")) {
                            showBankerMenu(scanner, bankSystem);
                        }
                    } else {
                        // إذا رقم المستخدم أو كلمة المرور غلط
                        System.out.println("Invalid User ID or Password.");
                    }
                    break;
                case 2:
                    // المستخدم اختار الخروج من النظام
                    System.out.println("Thank you for using ACME Bank System.");
                    // نخلي running تساوي false عشان نوقف الـ while ونطلع من البرنامج
                    running = false;
                    break;
                default:
                    // إذا المستخدم كتب خيار غير موجود في القائمة
                    System.out.println("Invalid option. Please try again.");

            }
        }
        // نقفل ال Scanner بعد ما ينتهي البرنامج
        scanner.close();
    }

    // قائمة الخيارات الخاصة بموظف البنك
    public static void showBankerMenu(Scanner scanner, BankSystem bankSystem) {
        System.out.println("BANKER MENU");
        System.out.println("1. Add New Customer");
        System.out.println("2. View Customers Details");
        System.out.println("3. View Customer Transaction History");
        System.out.println("4. Logout");
        System.out.print("Please select an option: ");
        // نقرأ اختيار موظف البنك من القائمة
        int bankerChoice = scanner.nextInt();
        // نحدد شنو يسوي البرنامج حسب اختيار موظف البنك
        switch (bankerChoice) {
            case 1:
                // موظف البنك اختار إضافة عميل جديد
                System.out.println("Add New Customer");

                // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                scanner.nextLine();
                // نطلب رقم العميل الجديد
                System.out.print("Enter Customer ID: ");
                String customerId = scanner.nextLine();
                // نطلب رقم المستخدم الخاص بالعميل
                System.out.print("Enter User ID: ");
                String userId = scanner.nextLine();
                // نطلب اسم العميل
                System.out.print("Enter Customer Name: ");
                String name = scanner.nextLine();
                // نطلب إيميل العميل
                System.out.print("Enter Customer Email: ");
                String email = scanner.nextLine();
                // نطلب رقم تلفون العميل
                System.out.print("Enter Customer Phone: ");
                String phone = scanner.nextLine();
                // نطلب كلمة المرور الجديدة للعميل
                System.out.print("Enter Customer Password: ");
                String password = scanner.nextLine();
                // نحول كلمة مرور العميل إلى Hash قبل تخزينها
                String customerPasswordHash =
                        PasswordHashingUtility.hashPassword(password);
                // ننشئ العميل الجديد باستخدام البيانات اللي دخلها موظف البنك
                Customer customer = new Customer(
                        customerId,
                        userId,
                        name,
                        email,
                        phone,
                        customerPasswordHash
                );
                // نعرض أنواع الحسابات المتاحة للعميل
                System.out.println("Select Account Type:");
                System.out.println("1. Checking Account");
                System.out.println("2. Savings Account");
                System.out.println("3. Both Checking and Savings");
                System.out.print("Please select an option: ");
                // نقرأ اختيار نوع الحساب
                int accountTypeChoice = scanner.nextInt();
                // نحدد شنو نوع الحساب حسب اختيار موظف البنك
                switch (accountTypeChoice) {
                    case 1:
                        // موظف البنك اختار إنشاء حساب جاري
                        scanner.nextLine();

                        // نطلب رقم الحساب الجاري
                        System.out.print("Enter Checking Account ID: ");
                        String checkingAccountId = scanner.nextLine();
                        // نطلب الرصيد الابتدائي للحساب الجاري
                        System.out.print("Enter Initial Balance: ");
                        double initialBalance = scanner.nextDouble();
                        // ننشئ حساب جاري جديد للعميل
                        Account checkingAccount = new Account(
                                checkingAccountId,
                                customerId,
                                "CHECKING",
                                initialBalance
                        );
                        // نضيف الحساب الجاري الجديد إلى قائمة الحسابات في النظام
                        bankSystem.getAccountService().addAccount(checkingAccount);
                        break;
                    case 2:
                        // موظف البنك اختار إنشاء حساب توفير
                        scanner.nextLine();

                        // نطلب رقم حساب التوفير
                        System.out.print("Enter Savings Account ID: ");
                        String savingsAccountId = scanner.nextLine();
                        // نطلب الرصيد الابتدائي لحساب التوفير
                        System.out.print("Enter Initial Balance: ");
                        double savingsInitialBalance = scanner.nextDouble();
                        // ننشئ حساب توفير جديد للعميل
                        Account savingsAccount = new Account(
                                savingsAccountId,
                                customerId,
                                "SAVINGS",
                                savingsInitialBalance

                        );
                        // نضيف حساب التوفير الجديد إلى قائمة الحسابات في النظام
                        bankSystem.getAccountService().addAccount(savingsAccount);

                        break;
                    case 3:
                        // موظف البنك اختار إنشاء حساب جاري وحساب توفير
                        scanner.nextLine();

                        // نطلب رقم الحساب الجاري
                        System.out.print("Enter Checking Account ID: ");
                        String bothCheckingAccountId = scanner.nextLine();
                        // نطلب الرصيد الابتدائي للحساب الجاري
                        System.out.print("Enter Checking Initial Balance: ");
                        double bothCheckingInitialBalance = scanner.nextDouble();
                        // ننشئ الحساب الجاري للعميل
                        Account bothCheckingAccount = new Account(
                                bothCheckingAccountId,
                                customerId,
                                "CHECKING",
                                bothCheckingInitialBalance
                        );
                        // نضيف الحساب الجاري إلى قائمة الحسابات في النظام
                        bankSystem.getAccountService().addAccount(bothCheckingAccount);
                        // ننظف السطر المتبقي بعد قراءة الرصيد
                        scanner.nextLine();

                        // نطلب رقم حساب التوفير
                        System.out.print("Enter Savings Account ID: ");
                        String bothSavingsAccountId = scanner.nextLine();
                        // نطلب الرصيد الابتدائي لحساب التوفير
                        System.out.print("Enter Savings Initial Balance: ");
                        double bothSavingsInitialBalance = scanner.nextDouble();
                        // ننشئ حساب التوفير للعميل
                        Account bothSavingsAccount = new Account(
                                bothSavingsAccountId,
                                customerId,
                                "SAVINGS",
                                bothSavingsInitialBalance

                        );
                        // نضيف حساب التوفير إلى قائمة الحسابات في النظام
                        bankSystem.getAccountService().addAccount(bothSavingsAccount);

                        break;

                }
                // نضيف العميل إلى قائمة المستخدمين عشان يقدر يسجل دخول
                bankSystem.getLoginService().addUser(customer);
                // نضيف العميل الجديد إلى قائمة العملاء في النظام
                bankSystem.getCustomerService().addCustomer(customer);
                // نطبع رسالة تأكيد بعد إضافة العميل بنجاح
                System.out.println("Customer added successfully.");

                break;
            case 2:
                // موظف البنك اختار عرض بيانات العملاء
                System.out.println("CUSTOMERS DETAILS");

                // نمر على كل العملاء الموجودين في قائمة العملاء
                for (Customer existingCustomer : bankSystem.getCustomerService().getAllCustomers()) {

                    System.out.println("Customer ID: " + existingCustomer.getCustomerId());
                    System.out.println("User ID: " + existingCustomer.getUserId());
                    System.out.println("Name: " + existingCustomer.getName());
                    System.out.println("Email: " + existingCustomer.getEmail());
                    System.out.println("Phone: " + existingCustomer.getPhone());
                }
                break;
            case 3:
                // موظف البنك اختار عرض تاريخ عمليات عميل معين
                System.out.println("CUSTOMER TRANSACTION HISTORY");

                // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                scanner.nextLine();

                // نطلب رقم العميل اللي نبي نشوف عملياته
                System.out.print("Enter Customer ID: ");
                String historyCustomerId = scanner.nextLine();
                // نجيب كل الحسابات الخاصة بالعميل المطلوب
                ArrayList<Account> customerAccounts =
                 bankSystem.getAccountService().getAccountsByCustomerId(historyCustomerId);
                // نمر على كل حسابات العميل
                for (Account account : customerAccounts) {
                    // نطبع رقم الحساب ونوعه
                    System.out.println("Account ID: " + account.getAccountId());
                    System.out.println("Account Type: " + account.getAccountType());
                    // نجيب كل العمليات الخاصة بهذا الحساب
                    ArrayList<Transaction> accountTransactions =
                            bankSystem.getTransactionService()
                                    .getTransactionsByAccountId(account.getAccountId());
                    // نمر على كل العمليات الخاصة بهذا الحساب
                    for (Transaction transaction : accountTransactions) {

                        System.out.println("Transaction ID: " + transaction.getTransactionId());
                        System.out.println("Type: " + transaction.getTransactionType());
                        System.out.println("Amount: " + transaction.getAmount());
                        System.out.println("Balance After: " + transaction.getBalanceAfter());
                        System.out.println("Date/Time: " + transaction.getDateTime());
                    }
                }

                break;
        }
    }
}
