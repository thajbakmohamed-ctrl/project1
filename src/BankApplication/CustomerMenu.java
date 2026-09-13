package BankApplication;

// نستخدم نظام البنك عشان نوصل حق الحسابات والخدمات
import BankingSystem.BankSystem;
// نستخدم Customer عشان نعرف أي عميل مسجل دخوله
import BankModels.Customer;
// نستخدم Scanner عشان نقرأ اختيارات العميل
import java.util.Scanner;
//عشان نتعامل مع اكاونتات العميل
import BankModels.Account;
//لان العميل ممكن يكون عنده سيفنق و جيكنق او الاثنين
import java.util.ArrayList;
import java.util.Optional;
import BankModels.User;

    public class CustomerMenu {
        // ميثود تعرض قائمة العميل بعد تسجيل الدخول
        public static void showCustomerMenu(
                Scanner scanner,
                BankSystem bankSystem,
                User currentUser) {

            // نحول المستخدم الحالي إلى Customer
            Customer customer = (Customer) currentUser;
            // نخلي قائمة العميل تستمر لين يختار Logout
            boolean customerMenuRunning = true;

            while (customerMenuRunning) {
                // نعرض قائمة الخيارات الخاصة بالعميل
                System.out.println("CUSTOMER MENU");
                System.out.println("1. View My Accounts");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Transfer");
                System.out.println("5. View Transaction History");
                System.out.println("6. Logout");
                System.out.print("Please select an option: ");
                // نقرا اختيار العميل من القائمة
                int customerChoice = scanner.nextInt();
                // نحدد شنو يسوي البرنامج حسب اختيار العميل
                switch (customerChoice) {

                    case 1:
                        // العميل اختار عرض حساباته
                        System.out.println("VIEW MY ACCOUNTS");
                        // نجيب كل الحسابات الخاصة بالعميل الحالي
                        ArrayList<Account> customerAccounts =
                                bankSystem.getAccountService()
                                        .getAccountsByCustomerId(customer.getCustomerId());
                        // نمر على كل حسابات العميل ونطبع تفاصيلها
                        for (Account account : customerAccounts) {

                            System.out.println("Account ID: " + account.getAccountId());
                            System.out.println("Account Type: " + account.getAccountType());
                            System.out.println("Balance: " + account.getBalance());
                            System.out.println("Active: " + account.isActive());
                        }
                        break;

                    case 2:
                        // العميل اختار ايداع مبلغ
                        System.out.println("DEPOSIT");
                        // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                        scanner.nextLine();

                        // نطلب رقم الحساب اللي العميل يبي يودع فيه
                        System.out.print("Enter Account ID: ");
                        String depositAccountId = scanner.nextLine();
                        // نبحث عن الحساب باستخدام رقم الحساب
                        Optional<Account> depositAccount =
                                bankSystem.getAccountService().findAccountById(depositAccountId);
                        // نتاكد ان الحساب موجود
                        if (depositAccount.isPresent()) {

                            // نطلع الحساب من داخل Optional
                            Account account = depositAccount.get();

                            // نتأكد إن الحساب تابع لنفس العميل المسجل دخوله
                            if (account.getCustomerId().equals(customer.getCustomerId())) {

                                // نطلب من العميل مبلغ الإيداع
                                System.out.print("Enter Deposit Amount: ");
                                double depositAmount = scanner.nextDouble();
                                // نتأكد إن مبلغ الإيداع أكبر من صفر
                                if (depositAmount > 0) {

                                    // نضيف المبلغ إلى الحساب
                                    bankSystem.getAccountService().deposit(account, depositAmount);

                                    System.out.println("Deposit successful.");
                                    System.out.println("New Balance: " + account.getBalance());

                                } else {

                                    System.out.println("Invalid deposit amount.");
                                }

                            } else {

                                System.out.println("You cannot deposit into another customer's account.");
                            }

                        } else {

                            System.out.println("Account not found.");
                        }
                        break;

                    case 3:
                        // العميل اختار سحب مبلغ
                        System.out.println("WITHDRAW");
                        // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                        scanner.nextLine();

                        // نطلب رقم الحساب اللي العميل يبي يسحب منه
                        System.out.print("Enter Account ID: ");
                        String withdrawAccountId = scanner.nextLine();
                        // نبحث عن الحساب باستخدام رقم الحساب
                        Optional<Account> withdrawAccount =
                                bankSystem.getAccountService().findAccountById(withdrawAccountId);
                        // نتاكد ان الحساب موجود
                        if (withdrawAccount.isPresent()) {

                            // نطلع الحساب من داخل Optional
                            Account account = withdrawAccount.get();

                            // نتاكد ان الحساب تابع لنفس العميل
                            if (account.getCustomerId().equals(customer.getCustomerId())) {

                                // نطلب من العميل مبلغ السحب
                                System.out.print("Enter Withdraw Amount: ");
                                double withdrawAmount = scanner.nextDouble();

                                // نتاكد إن مبلغ السحب أكبر من صفر
                                if (withdrawAmount > 0) {

                                    // نسحب المبلغ من الحساب
                                    bankSystem.getAccountService().withdraw(account, withdrawAmount);

                                    System.out.println("Withdraw completed.");
                                    System.out.println("New Balance: " + account.getBalance());

                                } else {

                                    System.out.println("Invalid withdraw amount.");
                                }

                            } else {

                                System.out.println("You cannot withdraw from another customer's account.");
                            }

                        } else {

                            System.out.println("Account not found.");
                        }
                        break;

                    case 4:
                        // العميل اختار تحويل مبلغ
                        System.out.println("TRANSFER");
                        // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                        scanner.nextLine();

                        // نطلب رقم الحساب اللي بنحول منه
                        System.out.print("Enter From Account ID: ");
                        String fromAccountId = scanner.nextLine();
                        // نطلب رقم الحساب اللي بنحول له
                        System.out.print("Enter To Account ID: ");
                        String toAccountId = scanner.nextLine();
                        // نبحث عن الحساب اللي بنحول منه
                        Optional<Account> fromAccount =
                                bankSystem.getAccountService().findAccountById(fromAccountId);

                        // نبحث عن الحساب اللي بنحول له
                        Optional<Account> toAccount =
                                bankSystem.getAccountService().findAccountById(toAccountId);
                        // نتأكد إن الحسابين موجودين
                        if (fromAccount.isPresent() && toAccount.isPresent()) {

                            // نطلع الحسابين من داخل Optional
                            Account sourceAccount = fromAccount.get();
                            Account destinationAccount = toAccount.get();
                            // نتأكد إن الحساب اللي بنحول منه تابع لنفس العميل
                            if (sourceAccount.getCustomerId().equals(customer.getCustomerId())) {

                                // نتأكد إن الحساب اللي بنحول منه مو نفس الحساب اللي بنحول له
                                if (!sourceAccount.getAccountId().equals(destinationAccount.getAccountId())) {

                                    // نطلب مبلغ التحويل
                                    System.out.print("Enter Transfer Amount: ");
                                    double transferAmount = scanner.nextDouble();
                                    // نتأكد إن مبلغ التحويل أكبر من صفر
                                    if (transferAmount > 0) {

                                        // نتأكد إن الحساب اللي بنحول منه فعال
                                        if (sourceAccount.isActive()) {

                                            // إذا الرصيد بالسالب ما نسمح بتحويل أكثر من 100
                                            if (sourceAccount.getBalance() < 0 && transferAmount > 100) {

                                                System.out.println("You cannot transfer more than 100 while the balance is negative.");

                                            } else {

                                                // نسوي التحويل من الحساب الأول إلى الحساب الثاني
                                                bankSystem.getAccountService().transfer(
                                                        sourceAccount,
                                                        destinationAccount,
                                                        transferAmount
                                                );

                                                 // نطبع رسالة نجاح التحويل
                                                System.out.println("Transfer completed.");

                                                // نعرض الرصيد الجديد للحساب اللي حولنا منه
                                                System.out.println("From Account New Balance: "
                                                        + sourceAccount.getBalance());

                                                // نعرض الرصيد الجديد للحساب اللي حولنا له
                                                System.out.println("To Account New Balance: "
                                                        + destinationAccount.getBalance());
                                            }

                                        } else {

                                            System.out.println("This account is inactive.");
                                        }

                                    } else {

                                        System.out.println("Invalid transfer amount.");
                                    }

                                } else {

                                    System.out.println("You cannot transfer to the same account.");
                                }

                            } else {

                                System.out.println("You cannot transfer from another customer's account.");
                            }

                        } else {

                            System.out.println("One or both accounts were not found.");
                        }
                        break;

                    case 5:
                        // العميل اختار عرض تاريخ عملياته
                        System.out.println("TRANSACTION HISTORY");
                        break;

                    case 6:
                        // العميل اختار تسجيل الخروج
                        System.out.println("Logout selected");

                        // نوقف قائمة العميل ونرجع إلى شاشة تسجيل الدخول
                        customerMenuRunning = false;
                        break;

                    default:
                        // إذا العميل اختار رقم مو موجود
                        System.out.println("Invalid option");
                }

            }
        }
    }
