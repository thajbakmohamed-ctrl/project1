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

public class BankerMenu {

    // ميثود تعرض قائمة موظف البنك
    public static void showBankerMenu(
            Scanner scanner,
            BankSystem bankSystem) {
        // نعرض قائمة الخيارات الخاصة بموظف البنك
        System.out.println("BANKER MENU");
        System.out.println("1. Add New Customer");
        System.out.println("2. View Customers Details");
        System.out.println("3. View Customer Transaction History");
        System.out.println("4. Logout");
        System.out.print("Please select an option: ");

     // نقرا اختيار موظف البنك من القائمة
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

                // نسوي العميل الجديد باستخدام البيانات اللي دخلها موظف البنك
                Customer customer = new Customer(
                        customerId,
                        userId,
                        name,
                        email,
                        phone,
                        customerPasswordHash
                );
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
                        System.out.println("CHECKING ACCOUNT");
                        // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                        scanner.nextLine();

                        // نطلب رقم الحساب الجاري
                        System.out.print("Enter Checking Account ID: ");
                        String checkingAccountId = scanner.nextLine();

                        // نطلب الرصيد الابتدائي للحساب الجاري
                        System.out.print("Enter Initial Balance: ");
                        double initialBalance = scanner.nextDouble();
                        // نسوي حساب جاري جديد للعميل
                        Account checkingAccount = new Account(
                                checkingAccountId,
                                customerId,
                                "CHECKING",
                                initialBalance
                        );

                         // نضيف الحساب الجاري إلى قائمة الحسابات في النظام
                        bankSystem.getAccountService().addAccount(checkingAccount);
                        break;
                    case 2:
                        // موظف البنك اختار انشاء حساب توفير
                        System.out.println("SAVINGS ACCOUNT");

                        // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                        scanner.nextLine();

                        // نطلب رقم حساب التوفير
                        System.out.print("Enter Savings Account ID: ");
                        String savingsAccountId = scanner.nextLine();

                        // نطلب الرصيد الابتدائي لحساب التوفير
                        System.out.print("Enter Initial Balance: ");
                        double savingsInitialBalance = scanner.nextDouble();
                        // نسوي حساب توفير جديد للعميل
                        Account savingsAccount = new Account(
                                savingsAccountId,
                                customerId,
                                "SAVINGS",
                                savingsInitialBalance
                        );

                        // نضيف حساب التوفير حق قائمة الحسابات في النظام
                        bankSystem.getAccountService().addAccount(savingsAccount);

                        break;
                    case 3:
                        // موظف البنك اختار يسوي حساب جاري وحساب توفير
                        System.out.println("CHECKING AND SAVINGS ACCOUNTS");

                        // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                        scanner.nextLine();

                        // نطلب رقم الحساب الجاري
                        System.out.print("Enter Checking Account ID: ");
                        String bothCheckingAccountId = scanner.nextLine();

                        // نطلب الرصيد الابتدائي للحساب الجاري
                        System.out.print("Enter Checking Initial Balance: ");
                        double bothCheckingInitialBalance = scanner.nextDouble();
                        // نسوي الحساب الجاري للعميل
                        Account bothCheckingAccount = new Account(
                                bothCheckingAccountId,
                                customerId,
                                "CHECKING",
                                bothCheckingInitialBalance
                        );

                       // نضيف الحساب الجاري حق قائمة الحسابات
                        bankSystem.getAccountService().addAccount(bothCheckingAccount);
                        // ننظف السطر المتبقي بعد قراءة الرصيد
                        scanner.nextLine();

                        // نطلب رقم حساب التوفير
                        System.out.print("Enter Savings Account ID: ");
                        String bothSavingsAccountId = scanner.nextLine();

                        // نطلب الرصيد الابتدائي لحساب التوفير
                        System.out.print("Enter Savings Initial Balance: ");
                        double bothSavingsInitialBalance = scanner.nextDouble();
                        // نسوي حساب التوفير للعميل
                        Account bothSavingsAccount = new Account(
                                bothSavingsAccountId,
                                customerId,
                                "SAVINGS",
                                bothSavingsInitialBalance
                        );

                        // نضيف حساب التوفير إلى قائمة الحسابات
                        bankSystem.getAccountService().addAccount(bothSavingsAccount);

                        break;
                }
                // نضيف العميل حق قائمة المستخدمين عشان يقدر يسجل دخول
                bankSystem.getLoginService().addUser(customer);

                // نضيف العميل حق قائمة العملاء في النظام
                bankSystem.getCustomerService().addCustomer(customer);

                // نطبع رسالة تاكيد بعد اضافة العميل بنجاح
                System.out.println("Customer added successfully.");

                break;
            case 2:
                // موظف البنك اختار عرض بيانات العملاء
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
                System.out.println("CUSTOMER TRANSACTION HISTORY");

                // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                scanner.nextLine();

                // نطلب رقم العميل اللي نبي نشوف عملياته
                System.out.print("Enter Customer ID: ");
                String historyCustomerId = scanner.nextLine();

                // نجيب كل الحسابات الخاصة بالعميل المطلوب
                ArrayList<Account> customerAccounts =
                        bankSystem.getAccountService()
                                .getAccountsByCustomerId(historyCustomerId);
                // نمر على كل حسابات العميل
                for (Account account : customerAccounts) {

                    // نطبع رقم الحساب ونوعه
                    System.out.println("Account ID: " + account.getAccountId());
                    System.out.println("Account Type: " + account.getAccountType());
                    // نجيب كل العمليات الخاصة بهذا الحساب
                    ArrayList<Transaction> accountTransactions =
                            bankSystem.getTransactionService()
                                    .getTransactionsByAccountId(account.getAccountId());
                    // نمر على كل العمليات الخاصة بالحساب
                    for (Transaction transaction : accountTransactions) {

                        System.out.println("Transaction ID: " + transaction.getTransactionId());
                        System.out.println("Type: " + transaction.getTransactionType());
                        System.out.println("Amount: " + transaction.getAmount());
                        System.out.println("Balance After: " + transaction.getBalanceAfter());
                        System.out.println("Date/Time: " + transaction.getDateTime());
                    }

                }

                break;
            case 4:
                // موظف البنك اختار تسجيل الخروج
                System.out.println("Logout selected.");
                break;

        }

    }
}