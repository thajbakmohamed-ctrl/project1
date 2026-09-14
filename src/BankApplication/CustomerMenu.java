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
import BankModels.Transaction;
// نستخدم FileHandlingUtility عشان نحدث بيانات الحساب في الملف
import BankUtilities.FileHandlingUtility;
// نستخدم DebitCard عشان نتحقق من نوع البطاقة وحد السحب
import BankModels.DebitCard;

    public class CustomerMenu {
        // ميثود تعرض قائمة العميل بعد تسجيل الدخول
        public static void showCustomerMenu(
                Scanner scanner, BankSystem bankSystem, User currentUser) {

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
                                    // نحدث بيانات الحساب داخل الملف بعد الإيداع
                                    FileHandlingUtility.saveAccount(account);
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
                                    // نبحث عن بطاقة الخصم المرتبطة بالحساب
                                    Optional<DebitCard> debitCard =
                                            bankSystem.getDebitCardService()
                                                    .findCardByAccountId(account.getAccountId());
                                    // نتأكد إن الحساب عنده بطاقة خصم
                                    if (debitCard.isPresent()) {
                                        // نطلع البطاقة من Optional
                                        DebitCard card = debitCard.get();
                                        // نجيب حد السحب حسب نوع البطاقة
                                        double withdrawLimit =
                                                bankSystem.getDebitCardService()
                                                        .getWithdrawLimit(card.getCardType());
                                        // نتأكد إن المبلغ ما يتجاوز حد البطاقة
                                        if (withdrawAmount <= withdrawLimit) {
                                            // نحاول نسوي عملية السحب ونخزن إذا نجحت أو لا
                                            boolean withdrawSuccessful =
                                                    bankSystem.getAccountService()
                                                            .withdraw(account, withdrawAmount);
                                            // إذا عملية السحب نجحت
                                            if (withdrawSuccessful) {
                                                // نحفظ التغييرات الجديدة في ملف الحساب
                                                FileHandlingUtility.saveAccount(account);
                                                // نطبع رسالة نجاح
                                                System.out.println("Withdraw completed.");
                                                // نطبع الرصيد الجديد
                                                System.out.println("New Balance: " + account.getBalance());

                                            } else {
                                                // إذا عملية السحب انرفضت
                                                System.out.println("Withdraw failed.");
                                            }

                                        } else {
                                            // إذا المبلغ أكبر من الحد المسموح للبطاقة
                                            System.out.println(
                                                    "Withdraw amount exceeds the card limit of "
                                                            + withdrawLimit
                                            );
                                        }

                                    } else {
                                        // إذا الحساب ما عنده بطاقة خصم
                                        System.out.println("No debit card found for this account.");
                                    }

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
                                bankSystem.getAccountService()
                                        .findAccountById(fromAccountId);
                        // نبحث عن الحساب اللي بنحول له
                        Optional<Account> toAccount =
                                bankSystem.getAccountService()
                                        .findAccountById(toAccountId);
                        // نتأكد إن الحسابين موجودين
                        if (fromAccount.isPresent() && toAccount.isPresent()) {
                            // نطلع الحسابين من Optional
                            Account sourceAccount = fromAccount.get();
                            Account destinationAccount = toAccount.get();
                            // نتأكد إن الحساب المرسل تابع لنفس العميل
                            if (sourceAccount.getCustomerId()
                                    .equals(customer.getCustomerId())) {
                                // نتأكد إن الحساب المرسل مو نفس الحساب المستلم
                                if (!sourceAccount.getAccountId()
                                        .equals(destinationAccount.getAccountId())) {
                                    // نطلب مبلغ التحويل
                                    System.out.print("Enter Transfer Amount: ");
                                    double transferAmount = scanner.nextDouble();
                                    // نتأكد إن مبلغ التحويل أكبر من صفر
                                    if (transferAmount > 0) {
                                        // نتأكد إن الحساب المرسل فعال
                                        if (sourceAccount.isActive()) {
                                            // إذا الرصيد سالب ما نسمح بتحويل أكثر من 100
                                            if (sourceAccount.getBalance() < 0
                                                    && transferAmount > 100) {
                                                System.out.println(
                                                        "You cannot transfer more than 100 while the balance is negative."
                                                );

                                            } else {
                                                // نبحث عن بطاقة الخصم المرتبطة بالحساب المرسل
                                                Optional<DebitCard> transferCard =
                                                        bankSystem.getDebitCardService()
                                                                .findCardByAccountId(
                                                                        sourceAccount.getAccountId()
                                                                );
                                                // نتأكد إن الحساب عنده بطاقة خصم
                                                if (transferCard.isPresent()) {
                                                    // نطلع البطاقة من Optional
                                                    DebitCard card = transferCard.get();
                                                    // نجيب حد التحويل حسب نوع البطاقة
                                                    double transferLimit =
                                                            bankSystem.getDebitCardService()
                                                                    .getTransferLimit(
                                                                            card.getCardType()
                                                                    );

                                                    // نتأكد إن مبلغ التحويل داخل الحد
                                                    if (transferAmount <= transferLimit) {
                                                        // نحاول نسوي التحويل
                                                        boolean transferSuccessful =
                                                                bankSystem.getAccountService()
                                                                        .transfer(sourceAccount,
                                                                                destinationAccount,
                                                                                transferAmount
                                                                        );

                                                        // إذا التحويل نجح
                                                        if (transferSuccessful) {
                                                            // نحفظ الحساب المرسل
                                                            FileHandlingUtility.saveAccount(
                                                                    sourceAccount
                                                            );

                                                            // نحفظ الحساب المستلم
                                                            FileHandlingUtility.saveAccount(
                                                                    destinationAccount
                                                            );

                                                            // نطبع رسالة نجاح
                                                            System.out.println(
                                                                    "Transfer completed."
                                                            );

                                                            // نطبع الرصيد الجديد للحساب المرسل
                                                            System.out.println(
                                                                    "From Account New Balance: "
                                                                            + sourceAccount.getBalance()
                                                            );

                                                            // نطبع الرصيد الجديد للحساب المستلم
                                                            System.out.println(
                                                                    "To Account New Balance: "
                                                                            + destinationAccount.getBalance()
                                                            );

                                                        } else {
                                                            // إذا التحويل فشل
                                                            System.out.println(
                                                                    "Transfer failed."
                                                            );
                                                        }

                                                    } else {
                                                        // إذا المبلغ أكبر من حد البطاقة
                                                        System.out.println(
                                                                "Transfer amount exceeds the card limit of "
                                                                        + transferLimit
                                                        );
                                                    }
                                                } else {
                                                    // إذا الحساب ما عنده بطاقة خصم
                                                    System.out.println(
                                                            "No debit card found for this account."
                                                    );
                                                }
                                            }

                                        } else {
                                            // إذا الحساب غير فعال
                                            System.out.println(
                                                    "This account is inactive."
                                            );
                                        }

                                    } else {
                                        // إذا مبلغ التحويل صفر أو سالب
                                        System.out.println(
                                                "Invalid transfer amount."
                                        );
                                    }
                                } else {
                                    // إذا حاول يحول لنفس الحساب
                                    System.out.println(
                                            "You cannot transfer to the same account."
                                    );
                                }

                            } else {
                                // إذا الحساب المرسل مو تابع للعميل
                                System.out.println(
                                        "You cannot transfer from another customer's account."
                                );
                            }

                        } else {
                            // إذا واحد من الحسابات أو الاثنين غير موجودين
                            System.out.println(
                                    "One or both accounts were not found."
                            );
                        }
                        break;
                    case 5:
                        // العميل اختار عرض تاريخ عملياته
                        System.out.println("TRANSACTION HISTORY");

                        // نجيب كل الحسابات الخاصة بالعميل الحالي
                        ArrayList<Account> transactionAccounts =
                                bankSystem.getAccountService()
                                        .getAccountsByCustomerId(customer.getCustomerId());
                        // نمر على كل حسابات العميل
                        for (Account account : transactionAccounts) {
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
