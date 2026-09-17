package BankApplication;
// نستخدم نظام البنك عشان نوصل حق خدمات البنك
import BankingSystem.BankSystem;
// نستخدم Scanner عشان نقرا اختيارات موظف البنك
import java.util.Scanner;
// نستخدم Customer عشان نسوي عميل جديد
import BankModels.Customer;
// نستخدم Account عشان نسوي حساب جاري او توفير للعميل
import BankModels.Account;
// نستخدم اداة الهاشنق عشان نخزن كلمة مرور العميل بشكل Hash
import BankUtilities.PasswordHashingUtility;
// نستخدم ArrayList عشان نخزن حسابات العميل وعملياته
import java.util.ArrayList;
// نستخدم Transaction عشان نعرض تفاصيل العمليات البنكية
import BankModels.Transaction;
// نستخدم FileHandlingUtility عشان نحفظ بيانات العميل داخل ملف
import BankUtilities.FileHandlingUtility;
// نستخدم DebitCard عشان ننشئ بطاقة خصم للحساب
import BankModels.DebitCard;
// نستخدم Optional لأن البحث عن الحساب ممكن يلقى نتيجة أو لا
import java.util.Optional;
// Handles the banker menu and banker operations
public class BankerMenu {
    // Checks that the Account ID is unique
    // نطلب Account ID ونتأكد أنه غير مستخدم
    private static String readUniqueAccountId(Scanner scanner, BankSystem
            bankSystem, String message) {
        // نخزن رقم الحساب
        String accountId;
        // نكرر الطلب إلى أن يدخل الموظف رقم حساب جديد
        while (true) {
            // نعرض رسالة إدخال رقم الحساب
            System.out.print(message);
            accountId = scanner.nextLine();
            // إذا رقم الحساب غير موجود نرجعه
            if (bankSystem.getAccountService().findAccountById(accountId).isEmpty()) {
                return accountId;
            }

            // تظهر إذا كان رقم الحساب مستخدمًا
            System.out.println("Account ID already exists");
        }
    }
    // ميثود تعرض قائمة موظف البنك
    public static void showBankerMenu(Scanner scanner, BankSystem bankSystem) {
        // نخلي قائمة موظف البنك تستمر لين يختار Logout
        boolean bankerMenuRunning = true;
        while (bankerMenuRunning) {
            // نعرض قائمة الخيارات الخاصة بموظف البنك
            // Displays and manages the banker menu
            System.out.println("Banker Menu");
            System.out.println("1. Add New Customer");
            System.out.println("2. View Customers Details");
            System.out.println("3. View Customer Transaction History");
            System.out.println("4. Review Card Upgrade Request");
            System.out.println("5. Logout");
            // نقرا اختيار موظف البنك من القائمة
            int bankerChoice = scanner.nextInt();
            // نحدد شنو يسوي البرنامج حسب اختيار موظف البنك
            switch (bankerChoice) {
                case 1:
                    // موظف البنك اختار إضافة عميل جديد
                    System.out.println("Add New Customer");
                    // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                    scanner.nextLine();
                    // نخزن رقم العميل
                    String customerId;

                   // نكرر الطلب إلى أن يدخل الموظف Customer ID غير مستخدم
                    while (true) {
                        // نطلب رقم العميل
                        System.out.print("Enter Customer ID: ");
                        customerId = scanner.nextLine();
                        // نبحث إذا كان رقم العميل مستخدمًا من قبل
                        if (bankSystem.getCustomerService()
                        .findCustomerById(customerId).isEmpty()) {
                        break;
                        }
                        // نعرض رسالة إذا كان رقم العميل مستخدمًا
                        System.out.println("Customer ID already exists");
                    }

                   // نخزن رقم المستخدم
                    String userId;
                    // نستمر في الطلب لين يدخل الموظف User ID غير مستخدم
                    while (true) {
                        // نطلب رقم المستخدم الخاص بالعميل
                        System.out.print("Enter User ID: ");
                        userId = scanner.nextLine();
                        // إذا رقم المستخدم غير موجود نخرج من الحلقة
                        if (bankSystem.getLoginService().findUserById(userId).isEmpty()) {
                        break;
                        }
                        // إذا رقم المستخدم موجود نطلب رقمًا مختلفًا
                        System.out.println("User ID already exists");
                    }
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
                    String customerPasswordHash = PasswordHashingUtility.hashPassword(password);

                    // نسوي العميل الجديد باستخدام البيانات اللي دخلها موظف البنك
                    Customer customer = new Customer(customerId, userId, name, email,
                    phone, customerPasswordHash);
                    // نعرض انواع الحسابات المتاحة للعميل
                    System.out.println("Select Account Type:");
                    System.out.println("1. Checking Account");
                    System.out.println("2. Savings Account");
                    System.out.println("3. Both Checking and Savings");
                    System.out.print("Please select an option: ");

                    // نقرا اختيار نوع الحساب
                    int accountTypeChoice = scanner.nextInt();
                    // نحدد نوع الحساب حسب اختيار موظف البنك
                    switch (accountTypeChoice) {
                        case 1:
                            // موظف البنك اختار انشاء حساب جاري
                            // Creates a checking account for the customer
                            System.out.println("CHECKING ACCOUNT");
                            // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                            scanner.nextLine();
                            // نطلب رقم حساب جاري غير مستخدم
                            String checkingAccountId = readUniqueAccountId(scanner,
                                    bankSystem, "Enter Checking Account ID: ");
                            // نطلب الرصيد الابتدائي للحساب الجاري
                            System.out.print("Enter Initial Balance: ");
                            // نخزن الرصيد الابتدائي
                            double initialBalance = scanner.nextDouble();

                            // ننشئ حساب Checking جديد للعميل
                            Account checkingAccount = new Account(checkingAccountId,
                            customerId, "CHECKING", initialBalance);
                            // نضيف حساب الـChecking إلى النظام
                            bankSystem.getAccountService().addAccount(checkingAccount);
                            // نحفظ حساب الـChecking داخل الملف
                            FileHandlingUtility.saveAccount(checkingAccount);
                            // ننشئ Mastercard افتراضية تلقائيًا لحساب الـChecking الجديد
                            DebitCard defaultCheckingCard = bankSystem.getDebitCardService().
                            createDefaultCard(checkingAccount);
                            // نحفظ البطاقة الافتراضية داخل ملف عشان ما تختفي بعد إغلاق البرنامج
                            FileHandlingUtility.saveDebitCard(defaultCheckingCard);
                            break;
                        case 2:
                            // موظف البنك اختار انشاء حساب توفير
                            System.out.println("SAVINGS ACCOUNT");
                            // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                            scanner.nextLine();
                            // نطلب رقم حساب توفير غير مستخدم
                            String savingsAccountId = readUniqueAccountId(scanner, bankSystem,
                                    "Enter Savings Account ID: ");
                            // نطلب الرصيد الابتدائي لحساب التوفير
                            System.out.print("Enter Initial Balance: ");
                            double savingsInitialBalance = scanner.nextDouble();
                            // نسوي حساب توفير جديد للعميل
                            Account savingsAccount = new Account(savingsAccountId,
                                    customerId, "SAVINGS", savingsInitialBalance);
                            // نضيف حساب التوفير حق قائمة الحسابات في النظام
                            bankSystem.getAccountService().addAccount(savingsAccount);
                            // نحفظ بيانات حساب التوفير داخل ملف
                            FileHandlingUtility.saveAccount(savingsAccount);
                            // ننشئ Mastercard افتراضية تلقائيًا لحساب التوفير الجديد
                            DebitCard defaultSavingsCard = bankSystem.getDebitCardService()
                                    .createDefaultCard(savingsAccount);

                            // نحفظ البطاقة الافتراضية داخل ملف عشان ما تختفي بعد إغلاق البرنامج
                            FileHandlingUtility.saveDebitCard(defaultSavingsCard);

                            break;
                        case 3:
                            // موظف البنك اختار يسوي حساب جاري وحساب توفير
                            System.out.println("CHECKING AND SAVINGS ACCOUNTS");
                            // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                            scanner.nextLine();
                            // نطلب رقم حساب جاري غير مستخدم
                            String bothCheckingAccountId = readUniqueAccountId(scanner, bankSystem,
                                    "Enter Checking Account ID: ");
                            // نطلب الرصيد الابتدائي للحساب الجاري
                            System.out.print("Enter Checking Initial Balance: ");
                            double bothCheckingInitialBalance = scanner.nextDouble();
                            // نسوي الحساب الجاري للعميل
                            Account bothCheckingAccount = new Account(bothCheckingAccountId, customerId,
                                    "CHECKING", bothCheckingInitialBalance);

                            // نضيف الحساب الجاري حق قائمة الحسابات
                            bankSystem.getAccountService().addAccount(bothCheckingAccount);
                            // نحفظ بيانات الحساب الجاري داخل ملف
                            FileHandlingUtility.saveAccount(bothCheckingAccount);
                            // ننشئ Mastercard افتراضية تلقائيًا لحساب الـChecking
                            DebitCard defaultBothCheckingCard = bankSystem.getDebitCardService()
                                    .createDefaultCard(bothCheckingAccount);

                            // نحفظ البطاقة الافتراضية داخل ملف
                            FileHandlingUtility.saveDebitCard(defaultBothCheckingCard);
                            // ننظف السطر المتبقي بعد قراءة الرصيد
                            scanner.nextLine();
                            // نطلب رقم حساب توفير غير مستخدم
                            String bothSavingsAccountId = readUniqueAccountId(scanner, bankSystem,
                                    "Enter Savings Account ID: ");
                            // نطلب الرصيد الابتدائي لحساب التوفير
                            System.out.print("Enter Savings Initial Balance: ");
                            double bothSavingsInitialBalance = scanner.nextDouble();
                            // نسوي حساب التوفير للعميل
                            Account bothSavingsAccount = new Account(bothSavingsAccountId, customerId,
                                    "SAVINGS", bothSavingsInitialBalance);

                            // نضيف حساب التوفير إلى قائمة الحسابات
                            bankSystem.getAccountService().addAccount(bothSavingsAccount);
                            // نحفظ بيانات حساب التوفير داخل ملف
                            FileHandlingUtility.saveAccount(bothSavingsAccount);
                            // ننشئ Mastercard افتراضية تلقائيًا لحساب الـSavings
                            DebitCard defaultBothSavingsCard = bankSystem.getDebitCardService()
                                    .createDefaultCard(bothSavingsAccount);

                            // نحفظ البطاقة الافتراضية داخل ملف
                            FileHandlingUtility.saveDebitCard(defaultBothSavingsCard);
                            break;
                    }
                    // Saves the new customer in the banking system
                    // نضيف العميل حق قائمة المستخدمين عشان يقدر يسجل دخول
                    bankSystem.getLoginService().addUser(customer);
                    // نضيف العميل حق قائمة العملاء في النظام
                    bankSystem.getCustomerService().addCustomer(customer);
                    // نحفظ بيانات العميل داخل ملف
                    FileHandlingUtility.saveCustomer(customer);
                    System.out.println("\nCustomer added successfully.");
                    FileHandlingUtility.saveCustomerWithAccounts(customer,
                            bankSystem.getAccountService().getAllAccounts());

                    break;
                case 2:
                    // موظف البنك اختار عرض بيانات العملاء
                    // Displays all customer details
                    System.out.println("CUSTOMERS DETAILS");
                    // نمر على كل العملاء الموجودين في قائمة العملاء
                    for (Customer existingCustomer :
                            bankSystem.getCustomerService().getAllCustomers()) {
                        System.out.println("Customer ID: " + existingCustomer.getCustomerId());
                        System.out.println("User ID: " + existingCustomer.getUserId());
                        System.out.println("Name: " + existingCustomer.getName());
                        System.out.println("Email: " + existingCustomer.getEmail());
                        System.out.println("Phone: " + existingCustomer.getPhone());
                    }

                    break;
                case 3:
                    // موظف البنك اختار عرض تاريخ عمليات عميل معين
                    // Displays the transaction history for a selected customer
                    System.out.println("CUSTOMER TRANSACTION HISTORY");
                    // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                    scanner.nextLine();
                    // نطلب رقم العميل اللي نبي نشوف عملياته
                    System.out.print("Enter Customer ID: ");
                    String historyCustomerId = scanner.nextLine();
                    // نبحث عن العميل باستخدام Customer ID
                    Optional<Customer> historyCustomer = bankSystem.getCustomerService()
                            .findCustomerById(historyCustomerId);
                    // إذا العميل غير موجود نعرض رسالة ونرجع للقائمة
                    if (historyCustomer.isEmpty()) {
                        System.out.println("Customer not found.");
                        break;
                    }
                    // نجيب كل الحسابات الخاصة بالعميل المطلوب
                    ArrayList<Account> customerAccounts = bankSystem.getAccountService()
                            .getAccountsByCustomerId(historyCustomerId);
                    // نمر على كل حسابات العميل
                    for (Account account : customerAccounts) {
                        // نطبع رقم الحساب ونوعه
                        System.out.println("Account ID: " + account.getAccountId());
                        System.out.println("Account Type: " + account.getAccountType());
                        // نجيب كل العمليات الخاصة بهذا الحساب
                        ArrayList<Transaction> accountTransactions = bankSystem.getTransactionService()
                                .getTransactionsByAccountId(account.getAccountId());
                        // نمر على كل العمليات الخاصة بالحساب
                        for (Transaction transaction : accountTransactions) {
                            System.out.println("Transaction ID: " + transaction.getTransactionId());
                            // نخزن نوع العملية عشان نعرضه بشكل أوضح
                            String bankerDisplayType = transaction.getTransactionType();
                           // إذا المبلغ طلع من الحساب نعرض SENT
                            if (bankerDisplayType.equals("TRANSFER_OUT")) {
                                bankerDisplayType = "SENT";
                               // إذا المبلغ دخل إلى الحساب نعرض RECEIVED
                            } else if (bankerDisplayType.equals("TRANSFER_IN")) {
                                bankerDisplayType = "RECEIVED";
                            }

                             // نعرض نوع العملية بطريقة واضحة
                            System.out.println("Type: " + bankerDisplayType);
                            System.out.println("Amount: " + transaction.getAmount());
                            System.out.println("Balance After: " + transaction.getBalanceAfter());
                            System.out.println("Date/Time: " + transaction.getDateTime());
                        }
                    }

                    break;
                case 4: {
                    // موظف البنك اختار مراجعة طلبات ترقية البطاقات
                    // Reviews pending card upgrade requests
                    System.out.println("PENDING CARD UPGRADE REQUESTS");
                    // نسوي قائمة خاصة بالبطاقات اللي عليها طلب Upgrade
                    ArrayList<DebitCard> pendingUpgradeRequests = new ArrayList<>();
                    // نمر على كل البطاقات الموجودة في النظام
                    for (DebitCard card : bankSystem.getDebitCardService().
                            getAllDebitCards()) {
                        // إذا البطاقة عليها طلب ترقية معلق
                        if (card.isUpgradeRequested()) {
                            // نضيفها إلى قائمة الطلبات المعلقة
                            pendingUpgradeRequests.add(card);
                        }
                    }
                    // نتأكد إذا في طلبات معلقة أو لا
                    if (pendingUpgradeRequests.isEmpty()) {
                        // إذا ما في أي طلب Upgrade
                        System.out.println("There are no pending card upgrade requests."
                        );
                    } else {
                        // نعرض كل طلبات الترقية الموجودة
                        for (int i = 0; i < pendingUpgradeRequests.size(); i++) {
                            // ناخذ البطاقة الحالية
                            DebitCard card = pendingUpgradeRequests.get(i);
                            // نعرض رقم الطلب
                            System.out.println((i + 1) + ". Account ID: " + card.getAccountId());
                            // نعرض نوع البطاقة الحالي
                            System.out.println("Current Card Type: " + card.getCardType());
                            // نعرض نوع البطاقة المطلوبة
                            System.out.println("Requested Card Type: " + card.getRequestedCardType());
                            System.out.println();
                        }
                        // نطلب من موظف البنك اختيار الطلب
                        System.out.print("Select Request: ");
                        // نقرا رقم الطلب
                        int requestChoice = scanner.nextInt();
                        // نتأكد إن رقم الطلب صحيح
                        if (requestChoice >= 1 && requestChoice <= pendingUpgradeRequests.size()) {
                            // ناخذ البطاقة اللي اختارها موظف البنك
                            DebitCard card = pendingUpgradeRequests.get(requestChoice - 1);
                            // نعرض تفاصيل الطلب المختار
                            System.out.println("Account ID: " + card.getAccountId());
                            System.out.println("Current Card Type: " + card.getCardType());
                            System.out.println("Requested Card Type: " + card.getRequestedCardType());
                            // نعرض خيارات الموافقة أو الرفض
                            System.out.println("1. Approve");
                            System.out.println("2. Reject");
                            System.out.print("Please select an option: ");
                            // نقرا قرار موظف البنك
                            int approvalChoice = scanner.nextInt();
                            // إذا وافق موظف البنك
                            if (approvalChoice == 1) {
                                // نغير نوع البطاقة إلى النوع المطلوب
                                card.setCardType(card.getRequestedCardType());
                                // ننهي حالة الطلب المعلق
                                card.setUpgradeRequested(false);
                                // نمسح نوع البطاقة المطلوبة بعد الموافقة
                                card.setRequestedCardType("");
                                // نحفظ البطاقة بعد الترقية
                                FileHandlingUtility.saveDebitCard(card);
                                // نطبع رسالة نجاح
                                System.out.println("Card upgrade approved successfully.");
                                // إذا رفض موظف البنك
                            } else if (approvalChoice == 2) {
                                // ننهي حالة الطلب المعلق
                                card.setUpgradeRequested(false);
                                // نمسح نوع البطاقة المطلوبة
                                card.setRequestedCardType("");
                                // نحفظ البطاقة بعد رفض الطلب
                                FileHandlingUtility.saveDebitCard(card);
                                // نطبع رسالة الرفض
                                System.out.println("Card upgrade request rejected.");

                            } else {
                                // إذا اختار رقم غير موجود
                                System.out.println("Invalid option.");
                            }

                        } else {
                            // إذا اختار رقم طلب غير موجود
                            System.out.println("Invalid request number.");
                        }
                    }
                    // ننهي case 4
                    break;
                }
                case 5:
                    // موظف البنك اختار تسجيل الخروج
                    // Logs the banker out of the system
                    System.out.println("Logout selected.");
                    // نوقف قائمة موظف البنك ونرجع إلى شاشة تسجيل الدخول
                    bankerMenuRunning = false;
                    break;
            }

        }
    }
}