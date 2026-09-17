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
// نستخدم LocalDate و LocalDateTime عشان نحدد فترات الفلترة
import java.time.LocalDate;
import java.time.LocalDateTime;
// نستخدم TemporalAdjusters عشان نحدد بداية ونهاية الأسبوع والشهر السابق
import java.time.temporal.TemporalAdjusters;
// نستخدم DayOfWeek لتحديد أيام الأسبوع
import java.time.DayOfWeek;
// نستخدم Exception خاصة بالحساب غير الفعال
import BankExceptions.InactiveAccountException;
import BankExceptions.DailyLimitExceededException;
// Handles the customer menu and customer banking operations
    public class CustomerMenu {
        // ميثود تعرض قائمة العميل بعد تسجيل الدخول
        // Displays and manages the customer menu after login
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
                System.out.println("6. Request Card Upgrade");
                // العميل يقدر يعرض كشف حساب تفصيلي
                System.out.println("7. Detailed Account Statement");
                System.out.println("8. Filter Transactions");
                System.out.println("9. Logout");
                // نقرا اختيار العميل من القائمة
                int customerChoice = scanner.nextInt();
                // نحدد شنو يسوي البرنامج حسب اختيار العميل
                switch (customerChoice) {
                    case 1:
                        // Displays all accounts owned by the current customer
                        // العميل اختار عرض حساباته
                        System.out.println("VIEW MY ACCOUNTS");
                        // نجيب كل الحسابات الخاصة بالعميل الحالي
                        ArrayList<Account> customerAccounts = bankSystem.getAccountService()
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
                        // Deposits money into the customer's account
                        // العميل اختار إيداع مبلغ
                        System.out.println("DEPOSIT");
                        // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                        scanner.nextLine();
                        // نطلب رقم الحساب اللي العميل يبي يودع فيه
                        System.out.print("Enter Account ID: ");
                        String depositAccountId = scanner.nextLine();
                        // نبحث عن الحساب باستخدام رقم الحساب
                        Optional<Account> depositAccount = bankSystem.getAccountService()
                                        .findAccountById(depositAccountId);

                        // نتأكد إن الحساب موجود
                        if (depositAccount.isPresent()) {
                            // ناخذ الحساب الموجود
                            Account account = depositAccount.get();
                            // نتأكد إن الحساب تابع لنفس العميل
                            if (account.getCustomerId().equals(customer.getCustomerId())) {
                                // نطلب مبلغ الإيداع
                                System.out.print("Enter Deposit Amount: ");
                                double depositAmount = scanner.nextDouble();
                                // نتأكد إن المبلغ أكبر من صفر
                                if (depositAmount > 0) {
                                    // نبحث عن بطاقة الحساب
                                    Optional<DebitCard> depositCard = bankSystem.getDebitCardService()
                                                    .findCardByAccountId(account.getAccountId());
                                    // نتأكد إن الحساب عنده بطاقة
                                    if (depositCard.isPresent()) {
                                        // ناخذ البطاقة
                                        DebitCard card = depositCard.get();
                                        // نصفر استخدام الحدود إذا دخل يوم جديد
                                        bankSystem.getDebitCardService().resetDailyUsageIfNeeded(card);
                                        // لأن العميل يودع في حسابه الشخصي
                                        // نستخدم Own Deposit Limit
                                        double depositLimit = bankSystem.getDebitCardService()
                                                        .getOwnDepositLimit(card.getCardType());

                                        // نحسب مجموع الإيداعات لو تمت العملية
                                        double totalDepositToday = card.getDailyDepositUsed() + depositAmount;
                                        // نتأكد إن مجموع إيداعات اليوم داخل الحد
                                        if (totalDepositToday <= depositLimit) {
                                            // ننفذ عملية الإيداع
                                            bankSystem.getAccountService().deposit(account, depositAmount);
                                            // نحدث مجموع الإيداع المستخدم اليوم
                                            card.setDailyDepositUsed(totalDepositToday);
                                            // نحفظ بيانات البطاقة والحد اليومي
                                            FileHandlingUtility.saveDebitCard(card);
                                            // نحفظ الحساب بعد الإيداع
                                            FileHandlingUtility.saveAccount(account);
                                            // نحدث ملف العميل
                                            FileHandlingUtility.saveCustomerWithAccounts(customer, bankSystem.
                                                    getAccountService().getAllAccounts());
                                            // نطبع رسالة نجاح
                                            System.out.println("Deposit successful.");

                                            // نطبع الرصيد الجديد
                                            System.out.println("New Balance: " + account.getBalance());

                                            // نعرض استخدام حد الإيداع اليومي
                                            System.out.println("Daily Deposit Used: " + card.getDailyDepositUsed()
                                                            + " / " + depositLimit);

                                        } else {
                                            // إذا تجاوز حد الإيداع اليومي
                                            System.out.println("Daily deposit limit exceeded.");
                                            // نعرض الحد اليومي
                                            System.out.println("Daily Limit: " + depositLimit);
                                            // نعرض المستخدم اليوم
                                            System.out.println("Already Used Today: " + card.getDailyDepositUsed());

                                            // نعرض المتبقي اليوم
                                            System.out.println("Remaining Today: " + (depositLimit
                                                            - card.getDailyDepositUsed()));
                                        }

                                    } else {
                                        // إذا الحساب ما عنده بطاقة
                                        System.out.println("No debit card found for this account.");
                                    }

                                } else {

                                    // إذا المبلغ صفر أو سالب
                                    System.out.println("Invalid deposit amount.");
                                }

                            } else {

                                // نمنع العميل من الإيداع في حساب شخص ثاني
                                System.out.println("You cannot deposit into another customer account.");
                            }

                        } else {

                            // إذا رقم الحساب غير موجود
                            System.out.println("Account not found.");
                        }
                        break;

                    case 3:
                        // Withdraws money and checks the daily card limit
                        // العميل اختار سحب مبلغ
                        System.out.println("WITHDRAW");
                        // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                        scanner.nextLine();
                        // نطلب رقم الحساب اللي العميل يبي يسحب منه
                        System.out.print("Enter Account ID: ");
                        String withdrawAccountId = scanner.nextLine();
                        // نبحث عن الحساب باستخدام رقم الحساب
                        Optional<Account> withdrawAccount = bankSystem.getAccountService()
                                        .findAccountById(withdrawAccountId);
                        // نتأكد إن الحساب موجود
                        if (withdrawAccount.isPresent()) {
                            // ناخذ الحساب من Optional
                            Account account = withdrawAccount.get();
                            // نتأكد إن الحساب تابع للعميل الحالي
                            if (account.getCustomerId()
                                    .equals(customer.getCustomerId())) {
                                // نطلب مبلغ السحب
                                System.out.print("Enter Withdraw Amount: ");
                                double withdrawAmount = scanner.nextDouble();
                                // نتأكد إن مبلغ السحب أكبر من صفر
                                if (withdrawAmount > 0) {
                                    // نبحث عن بطاقة الخصم المرتبطة بالحساب
                                    Optional<DebitCard> debitCard = bankSystem.getDebitCardService()
                                                    .findCardByAccountId(account.getAccountId());
                                    // نتأكد إن الحساب عنده بطاقة
                                    if (debitCard.isPresent()) {
                                        // ناخذ البطاقة
                                        DebitCard card = debitCard.get();
                                        // نصفر الاستخدام اليومي إذا دخل يوم جديد
                                        bankSystem.getDebitCardService().resetDailyUsageIfNeeded(card);
                                        // نجيب حد السحب اليومي حسب نوع البطاقة
                                        double withdrawLimit = bankSystem.getDebitCardService()
                                                        .getWithdrawLimit(card.getCardType());

                                        // نحسب شكثر بيصير مجموع السحب اليوم
                                        double totalWithdrawToday = card.getDailyWithdrawUsed() + withdrawAmount;
                                        // نحاول نفحص الحد اليومي ونسوي عملية السحب
                                        // Handles withdrawal limits and account exceptions
                                        try {
                                            // إذا تجاوز الحد اليومي هالميثود ترمي Exception
                                            bankSystem.getDebitCardService().checkWithdrawDailyLimit(
                                                            card, withdrawAmount);

                                            // نحاول نسوي عملية السحب
                                            boolean withdrawSuccessful = bankSystem.getAccountService()
                                                            .withdraw(account, withdrawAmount);

                                            // إذا عملية السحب نجحت
                                            if (withdrawSuccessful) {
                                                // نحدث مجموع السحب المستخدم اليوم
                                                card.setDailyWithdrawUsed(totalWithdrawToday);

                                                // نحفظ البطاقة بعد تحديث الاستخدام اليومي
                                                FileHandlingUtility.saveDebitCard(card);

                                                // نحفظ الرصيد وحالة الحساب
                                                FileHandlingUtility.saveAccount(account);

                                                // نحدث ملف العميل وحساباته
                                                FileHandlingUtility.saveCustomerWithAccounts(customer,
                                                        bankSystem.getAccountService().getAllAccounts());
                                                // نطبع رسالة نجاح
                                                System.out.println("Withdraw completed.");
                                                // نطبع الرصيد الجديد
                                                System.out.println("New Balance: " + account.getBalance());
                                                // نعرض شكثر استخدم من الحد اليومي
                                                System.out.println("Daily Withdraw Used: "
                                                        + card.getDailyWithdrawUsed() + " / " + withdrawLimit);

                                            } else {
                                                // إذا عملية السحب انرفضت لسبب ثاني
                                                System.out.println("Withdraw failed.");
                                            }
                                            // إذا الحساب غير فعال
                                        } catch (InactiveAccountException e) {
                                            // نطبع رسالة الـException
                                            System.out.println(e.getMessage());
                                            // إذا العميل تجاوز حد السحب اليومي
                                        } catch (DailyLimitExceededException e) {
                                            // نطبع سبب رفض العملية
                                            System.out.println(e.getMessage());

                                            // نعرض الحد اليومي
                                            System.out.println("Daily Limit: " + withdrawLimit);

                                            // نعرض شكثر استخدم اليوم
                                            System.out.println("Already Used Today: " + card.getDailyWithdrawUsed());

                                            // نعرض شكثر باقي له
                                            System.out.println("Remaining Today: " + (withdrawLimit
                                                    - card.getDailyWithdrawUsed()));
                                        }
                                    } else {
                                        // إذا الحساب ما عنده بطاقة
                                        System.out.println("No debit card found for this account.");
                                    }

                                } else {
                                    // إذا المبلغ صفر أو سالب
                                    System.out.println("Invalid withdraw amount.");
                                }

                            } else {
                                // نمنع العميل من السحب من حساب شخص ثاني
                                System.out.println("You cannot withdraw from another customer account.");
                            }

                        } else {
                            // إذا رقم الحساب غير موجود
                            System.out.println("Account not found.");
                        }
                        // ننهي case 3
                        break;
                    case 4:
                        // Transfers money between accounts
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
                        // نبحث عن الحساب المرسل
                        Optional<Account> fromAccount = bankSystem.getAccountService()
                                .findAccountById(fromAccountId);

                        // نبحث عن الحساب المستلم
                        Optional<Account> toAccount = bankSystem.getAccountService()
                                        .findAccountById(toAccountId);

                        // نتأكد إن الحسابين موجودين
                        if (fromAccount.isPresent() && toAccount.isPresent()) {
                            // نطلع الحساب المرسل
                            Account sourceAccount = fromAccount.get();
                            // نطلع الحساب المستلم
                            Account destinationAccount = toAccount.get();
                            // نتأكد إن الحساب المرسل تابع للعميل الحالي
                            if (sourceAccount.getCustomerId().equals(customer.getCustomerId())) {
                                // نتأكد إن العميل ما يحول لنفس الحساب
                                if (!sourceAccount.getAccountId()
                                        .equals(destinationAccount.getAccountId())) {
                                    // نطلب مبلغ التحويل
                                    System.out.print("Enter Transfer Amount: ");
                                    double transferAmount = scanner.nextDouble();
                                    // نتأكد إن المبلغ أكبر من صفر
                                    if (transferAmount > 0) {
                                        // نتأكد إن الحساب المرسل فعال
                                        if (sourceAccount.isActive()) {
                                            // إذا الرصيد سالب ما نسمح بأكثر من 100
                                            if (sourceAccount.getBalance() < 0 && transferAmount > 100) {
                                                System.out.println(
                                                        "You cannot transfer more than 100 while the balance is negative"
                                                );

                                            } else {
                                                // نبحث عن بطاقة الحساب المرسل
                                                Optional<DebitCard> transferCard = bankSystem.getDebitCardService()
                                                                .findCardByAccountId(sourceAccount.getAccountId());
                                                // نتأكد إن الحساب عنده بطاقة
                                                if (transferCard.isPresent()) {
                                                    // ناخذ البطاقة
                                                    DebitCard card = transferCard.get();
                                                    // نتأكد إن البطاقة فعالة
                                                    if (card.isActive()) {
                                                        // نصفر الاستخدام اليومي إذا دخل يوم جديد
                                                        bankSystem.getDebitCardService().resetDailyUsageIfNeeded(card);
                                                        // نحدد إذا التحويل بين حسابات نفس العميل
                                                        boolean ownAccountTransfer =
                                                                sourceAccount.getCustomerId().equals(
                                                                                destinationAccount.getCustomerId());

                                                        // إذا التحويل بين حسابات نفس العميل
                                                        if (ownAccountTransfer) {
                                                            // نجيب حد Own Transfer حسب نوع البطاقة
                                                            double ownTransferLimit =
                                                                    bankSystem.getDebitCardService().getOwnTransferLimit(
                                                                                    card.getCardType());

                                                            // نحسب المجموع الجديد لليوم
                                                            double totalOwnTransferToday = card.getDailyOwnTransferUsed()
                                                                            + transferAmount;

                                                            // نتأكد إن المجموع داخل الحد اليومي
                                                            if (totalOwnTransferToday <= ownTransferLimit) {

                                                                // ننفذ التحويل
                                                                boolean transferSuccessful = bankSystem.getAccountService()
                                                                                .transfer(sourceAccount, destinationAccount,
                                                                                        transferAmount);

                                                                // إذا نجح التحويل
                                                                if (transferSuccessful) {
                                                                    // نحدث مجموع Own Transfer اليومي
                                                                    card.setDailyOwnTransferUsed(totalOwnTransferToday);

                                                                    // نحفظ بيانات البطاقة
                                                                    FileHandlingUtility.saveDebitCard(card);

                                                                    // نحفظ الحساب المرسل
                                                                    FileHandlingUtility.saveAccount(sourceAccount);

                                                                    // نحفظ الحساب المستلم
                                                                    FileHandlingUtility.saveAccount(destinationAccount);

                                                                    // نحدث ملف العميل
                                                                    FileHandlingUtility.saveCustomerWithAccounts(customer,
                                                                            bankSystem.getAccountService().getAllAccounts());

                                                                    // نطبع رسالة نجاح
                                                                    System.out.println("Own account transfer completed");

                                                                    // نطبع الرصيد الجديد للحساب المرسل
                                                                    System.out.println("From Account New Balance: "
                                                                            + sourceAccount.getBalance());

                                                                    // نطبع الرصيد الجديد للحساب المستلم
                                                                    System.out.println("To Account New Balance: "
                                                                            + destinationAccount.getBalance());

                                                                    // نعرض استخدام الحد اليومي
                                                                    System.out.println("Daily Own Transfer Used: "
                                                                            + card.getDailyOwnTransferUsed() + " / "
                                                                                    + ownTransferLimit);

                                                                } else {

                                                                    // إذا فشل التحويل
                                                                    System.out.println("Transfer failed.");
                                                                }

                                                            } else {

                                                                // إذا تجاوز حد Own Transfer اليومي
                                                                System.out.println("Daily own account transfer limit exceeded.");

                                                                // نعرض الحد اليومي
                                                                System.out.println("Daily Limit: " + ownTransferLimit);

                                                                // نعرض المستخدم اليوم
                                                                System.out.println("Already Used Today: "
                                                                        + card.getDailyOwnTransferUsed());

                                                                // نعرض المتبقي
                                                                System.out.println("Remaining Today: " + (ownTransferLimit
                                                                        - card.getDailyOwnTransferUsed()));
                                                            }

                                                        } else {
                                                            // نجيب حد التحويل إلى عميل آخر
                                                            double transferLimit = bankSystem.getDebitCardService()
                                                                    .getTransferLimit(card.getCardType());

                                                            // نحسب مجموع التحويلات اليوم
                                                            double totalTransferToday = card.getDailyTransferUsed()
                                                                            + transferAmount;

                                                            // نتأكد إن المجموع داخل الحد اليومي
                                                            if (totalTransferToday <= transferLimit) {

                                                                // ننفذ التحويل
                                                                boolean transferSuccessful = bankSystem.getAccountService()
                                                                        .transfer(sourceAccount, destinationAccount,
                                                                                        transferAmount);
                                                                // إذا نجح التحويل
                                                                if (transferSuccessful) {
                                                                    // نحدث مجموع التحويل اليومي
                                                                    card.setDailyTransferUsed(totalTransferToday);
                                                                    // نحفظ بيانات البطاقة
                                                                    FileHandlingUtility.saveDebitCard(card);

                                                                    // نحفظ الحساب المرسل
                                                                    FileHandlingUtility.saveAccount(sourceAccount);

                                                                    // نحفظ الحساب المستلم
                                                                    FileHandlingUtility.saveAccount(destinationAccount);

                                                                    // نحدث ملف العميل الحالي
                                                                    FileHandlingUtility.saveCustomerWithAccounts(customer,
                                                                            bankSystem.getAccountService().getAllAccounts());

                                                                    // نبحث عن العميل المستلم
                                                                    Optional<Customer> destinationCustomer =
                                                                            bankSystem.getCustomerService().findCustomerById(
                                                                                    destinationAccount.getCustomerId());

                                                                    // إذا العميل المستلم موجود نحدث ملفه بعد
                                                                    if (destinationCustomer.isPresent()) {
                                                                        FileHandlingUtility.saveCustomerWithAccounts(
                                                                                destinationCustomer.get(),
                                                                                bankSystem.getAccountService().getAllAccounts());
                                                                    }
                                                                    // نطبع رسالة نجاح
                                                                    System.out.println("Transfer completed.");

                                                                    // نطبع الرصيد الجديد للحساب المرسل
                                                                    System.out.println("From Account New Balance: "
                                                                            + sourceAccount.getBalance());

                                                                    // نطبع الرصيد الجديد للحساب المستلم
                                                                    System.out.println("To Account New Balance: "
                                                                            + destinationAccount.getBalance());

                                                                    // نعرض استخدام الحد اليومي
                                                                    System.out.println("Daily Transfer Used: "
                                                                            + card.getDailyTransferUsed() + " / " + transferLimit);

                                                                } else {
                                                                    // إذا فشل التحويل
                                                                    System.out.println("Transfer failed.");
                                                                }

                                                            } else {

                                                                // إذا تجاوز حد التحويل اليومي
                                                                System.out.println("Daily transfer limit exceeded.");

                                                                // نعرض الحد اليومي
                                                                System.out.println("Daily Limit: " + transferLimit);

                                                                // نعرض المستخدم اليوم
                                                                System.out.println("Already Used Today: "
                                                                        + card.getDailyTransferUsed());

                                                                // نعرض المتبقي
                                                                System.out.println("Remaining Today: "
                                                                        + (transferLimit - card.getDailyTransferUsed()));
                                                            }
                                                        }

                                                    } else {
                                                        // إذا البطاقة غير فعالة
                                                        System.out.println("Debit card is inactive.");
                                                    }

                                                } else {
                                                    // إذا الحساب ما عنده بطاقة
                                                    System.out.println("No debit card found for this account.");
                                                }
                                            }
                                        } else {
                                            // إذا الحساب غير فعال
                                            System.out.println("This account is inactive.");
                                        }
                                    } else {
                                        // إذا المبلغ صفر أو سالب
                                        System.out.println("Invalid transfer amount.");
                                    }
                                } else {
                                    // إذا حاول يحول إلى نفس الحساب
                                    System.out.println("You cannot transfer to the same account.");
                                }
                            } else {
                                // إذا حاول يحول من حساب مو تابع له
                                System.out.println("You cannot transfer from another customers account.");
                            }
                        } else {
                            // إذا أحد الحسابات غير موجود
                            System.out.println("One or both accounts were not found.");
                        }

                        // ننهي case 4
                        break;
                    case 5:
                        // Displays the customer's transaction history
                        // العميل اختار عرض تاريخ عملياته
                        System.out.println("TRANSACTION HISTORY");
                        // نجيب كل الحسابات الخاصة بالعميل الحالي
                        ArrayList<Account> transactionAccounts = bankSystem.getAccountService()
                                .getAccountsByCustomerId(customer.getCustomerId());
                        // نمر على كل حسابات العميل
                        for (Account account : transactionAccounts) {
                            // نطبع رقم الحساب ونوعه
                            System.out.println("Account ID: " + account.getAccountId());
                            System.out.println("Account Type: " + account.getAccountType());
                            // نجيب كل العمليات الخاصة بهذا الحساب
                            ArrayList<Transaction> accountTransactions = bankSystem.getTransactionService()
                                    .getTransactionsByAccountId(account.getAccountId());

                            // نمر على كل العمليات الخاصة بالحساب
                            for (Transaction transaction : accountTransactions) {
                                System.out.println("Transaction ID: " + transaction.getTransactionId());
                                // نخزن نوع العملية عشان نغير طريقة عرضه للمستخدم
                                String displayType = transaction.getTransactionType();
                                 // إذا المبلغ طلع من الحساب نعرض SENT
                                if (displayType.equals("TRANSFER_OUT")) {
                                    displayType = "SENT";
                                    // إذا المبلغ دخل إلى الحساب نعرض RECEIVED
                                } else if (displayType.equals("TRANSFER_IN")) {
                                    displayType = "RECEIVED";
                                }
                                 // نعرض نوع العملية بطريقة واضحة
                                System.out.println("Type: " + displayType);
                                // إذا العملية تحويل خارج نعرض الحساب المرسل والمستلم
                                if (transaction.getTransactionType().equals("TRANSFER_OUT")
                                        && transaction.getRelatedAccountId() != null
                                        && !transaction.getRelatedAccountId().isEmpty()) {
                                    System.out.println("From Account: " + transaction.getAccountId());
                                    System.out.println("To Account: " + transaction.getRelatedAccountId());
                                }
                                    // إذا العملية تحويل داخل نعرض الحساب المرسل والمستلم
                                else if (transaction.getTransactionType().equals("TRANSFER_IN")
                                        && transaction.getRelatedAccountId() != null
                                        && !transaction.getRelatedAccountId().isEmpty()) {
                                    System.out.println("From Account: " + transaction.getRelatedAccountId());
                                    System.out.println("To Account: " + transaction.getAccountId());
                                }
                                System.out.println("Amount: " + transaction.getAmount());
                                System.out.println("Balance After: " + transaction.getBalanceAfter());
                                System.out.println("Date/Time: " + transaction.getDateTime());
                            }
                        }
                        break;
                    case 6:
                        // Allows the customer to request a debit card upgrade
                        // العميل اختار طلب ترقية البطاقة
                        System.out.println("REQUEST CARD UPGRADE");
                        // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                        scanner.nextLine();
                        // نطلب رقم الحساب اللي العميل يبي يرقي بطاقته
                        System.out.print("Enter Account ID: ");
                        String upgradeAccountId = scanner.nextLine();
                        // نبحث عن الحساب باستخدام رقم الحساب
                        Optional<Account> upgradeAccount = bankSystem.getAccountService()
                                .findAccountById(upgradeAccountId);

                        // نتأكد إن الحساب موجود
                        if (upgradeAccount.isPresent()) {
                            // ناخذ الحساب الموجود
                            Account account = upgradeAccount.get();
                            // نتأكد إن الحساب تابع للعميل الحالي
                            if (account.getCustomerId().equals(customer.getCustomerId())) {
                                // نبحث عن البطاقة المرتبطة بهذا الحساب
                                Optional<DebitCard> upgradeCard = bankSystem.getDebitCardService()
                                        .findCardByAccountId(upgradeAccountId);

                                // نتأكد إن البطاقة موجودة
                                if (upgradeCard.isPresent()) {
                                    // ناخذ البطاقة الموجودة
                                    DebitCard card = upgradeCard.get();
                                    // نتأكد إذا عند العميل طلب ترقية معلق من قبل
                                    if (card.isUpgradeRequested()) {
                                        // نخبر العميل إن عنده طلب ترقية معلق
                                        System.out.println("You already have a pending upgrade request to "
                                                + card.getRequestedCardType());

                                        // نتأكد إذا البطاقة الحالية Platinum
                                    } else if (card.getCardType().equalsIgnoreCase("PLATINUM")) {

                                        // نخبر العميل أن Mastercard Platinum هي أعلى فئة
                                        System.out.println(
                                                "Your card is already at the highest tier: MASTERCARD PLATINUM. "
                                                        + "No further upgrade is available.");

                                        // نتأكد إذا البطاقة الحالية Titanium
                                    } else if (card.getCardType().equalsIgnoreCase("TITANIUM")) {
                                        // نعرض نوع البطاقة الحالي
                                        System.out.println("Current Card Type: TITANIUM");
                                        // Titanium تقدر تترقى فقط إلى Platinum
                                        System.out.println("1. Upgrade to Platinum");
                                        // نطلب اختيار العميل
                                        System.out.print("Please select an option: ");
                                        // نقرا اختيار العميل
                                        int upgradeChoice = scanner.nextInt();
                                        // نتأكد إن العميل اختار Platinum
                                        if (upgradeChoice == 1) {
                                            // نخزن نوع البطاقة المطلوبة
                                            card.setRequestedCardType("PLATINUM");
                                            // نخلي حالة طلب الترقية Pending
                                            card.setUpgradeRequested(true);
                                            // نحفظ طلب الترقية داخل ملف البطاقة
                                            FileHandlingUtility.saveDebitCard(card);
                                            // نطبع رسالة نجاح
                                            System.out.println("Upgrade request to PLATINUM submitted successfully.");

                                        } else {
                                            // إذا العميل اختار رقم غير موجود
                                            System.out.println("Invalid upgrade option.");
                                        }

                                        // إذا البطاقة الحالية Mastercard
                                    } else if (card.getCardType().equalsIgnoreCase("MASTERCARD")) {

                                        // نعرض نوع البطاقة الحالي
                                        System.out.println("Current Card Type: MASTERCARD");
                                        // نعرض أنواع الترقية المتاحة
                                        System.out.println("1. Titanium");
                                        System.out.println("2. Platinum");
                                        // نطلب اختيار العميل
                                        System.out.print("Please select an option: ");
                                        // نقرا اختيار العميل
                                        int upgradeChoice = scanner.nextInt();
                                        // إذا العميل اختار Titanium
                                        if (upgradeChoice == 1) {
                                            // نخزن إن العميل طلب Titanium
                                            card.setRequestedCardType("TITANIUM");
                                            // نخلي حالة طلب الترقية Pending
                                            card.setUpgradeRequested(true);
                                            // نحفظ طلب الترقية داخل ملف البطاقة
                                            FileHandlingUtility.saveDebitCard(card);
                                            // نطبع رسالة نجاح
                                            System.out.println("Upgrade request to TITANIUM submitted successfully.");
                                            // إذا العميل اختار Platinum
                                        } else if (upgradeChoice == 2) {
                                            // نخزن إن العميل طلب Platinum
                                            card.setRequestedCardType("PLATINUM");
                                            // نخلي حالة طلب الترقية Pending
                                            card.setUpgradeRequested(true);
                                            // نحفظ طلب الترقية داخل ملف البطاقة
                                            FileHandlingUtility.saveDebitCard(card);
                                            // نطبع رسالة نجاح
                                            System.out.println("Upgrade request to PLATINUM submitted successfully.");

                                        } else {
                                            // إذا العميل اختار رقم غير موجود
                                            System.out.println("Invalid upgrade option.");
                                        }
                                    }

                                } else {
                                    // إذا ما لقينا بطاقة مرتبطة بالحساب
                                    System.out.println("No debit card found for this account.");
                                }
                            } else {
                                // نمنع العميل من طلب ترقية بطاقة حساب مو تابع له
                                System.out.println("You cannot request an upgrade for another customer's account.");
                            }
                        } else {
                            // إذا رقم الحساب غير موجود
                            System.out.println("Account not found.");
                        }
                        // ننهي case 6
                        break;
                    case 7:
                        // Displays a detailed statement for the selected account
                        // العميل اختار عرض كشف حساب تفصيلي
                        System.out.println("DETAILED ACCOUNT STATEMENT");
                        // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                        scanner.nextLine();
                        // نطلب رقم الحساب
                        System.out.print("Enter Account ID: ");
                        String statementAccountId = scanner.nextLine();
                        // نبحث عن الحساب
                        Optional<Account> statementAccount = bankSystem.getAccountService()
                                .findAccountById(statementAccountId);
                        // نتأكد إن الحساب موجود
                        if (statementAccount.isPresent()) {
                            // ناخذ الحساب
                            Account account = statementAccount.get();
                            // نتأكد إن الحساب تابع للعميل الحالي
                            if (account.getCustomerId().equals(customer.getCustomerId())) {

                                // نطبع معلومات الحساب الأساسية
                                System.out.println(" ACCOUNT STATEMENT");
                                System.out.println("Account ID: " + account.getAccountId());
                                System.out.println("Account Type: " + account.getAccountType());
                                System.out.println("Current Balance: " + account.getBalance());
                                System.out.println("Account Active: " + account.isActive());

                                // نجيب كل العمليات الخاصة بهذا الحساب
                                ArrayList<Transaction> statementTransactions = bankSystem.getTransactionService()
                                        .getTransactionsByAccountId(account.getAccountId());

                                // نطبع عنوان قسم العمليات
                                System.out.println();
                                System.out.println(" TRANSACTIONS");

                                // نتأكد إذا الحساب عنده عمليات أو لا
                                if (statementTransactions.isEmpty()) {

                                    // إذا ما عنده أي عمليات
                                    System.out.println("No transactions found for this account.");

                                } else {

                                    // نمر على كل العمليات ونطبع تفاصيلها
                                    for (Transaction transaction :
                                            statementTransactions) {

                                        // نطبع رقم العملية
                                        System.out.println("Transaction ID: " + transaction.getTransactionId());
                                        // نخزن نوع العملية عشان نغير طريقة عرضه للمستخدم
                                        String statementDisplayType =
                                                transaction.getTransactionType();
                                        // إذا المبلغ طلع من الحساب نعرض SENT
                                        if (statementDisplayType.equals("TRANSFER_OUT")) {
                                            statementDisplayType = "SENT";
                                            // إذا المبلغ دخل إلى الحساب نعرض RECEIVED
                                        } else if (statementDisplayType.equals("TRANSFER_IN")) {
                                            statementDisplayType = "RECEIVED";
                                        }
                                             // نعرض نوع العملية بطريقة واضحة
                                        System.out.println("Type: " + statementDisplayType);
                                        // إذا العملية تحويل خارج نعرض الحساب المرسل والمستلم
                                        if (transaction.getTransactionType().equals("TRANSFER_OUT")
                                                && transaction.getRelatedAccountId() != null
                                                && !transaction.getRelatedAccountId().isEmpty()) {

                                            System.out.println(
                                                    "From Account: " + transaction.getAccountId()
                                            );

                                            System.out.println(
                                                    "To Account: " + transaction.getRelatedAccountId()
                                            );

                                            // إذا العملية تحويل داخل نعرض الحساب المرسل والمستلم
                                        } else if (transaction.getTransactionType().equals("TRANSFER_IN")
                                                && transaction.getRelatedAccountId() != null
                                                && !transaction.getRelatedAccountId().isEmpty()) {

                                            System.out.println("From Account: " + transaction.getRelatedAccountId());

                                            System.out.println("To Account: " + transaction.getAccountId());
                                        }
                                        // نطبع المبلغ
                                        System.out.println("Amount: " + transaction.getAmount());
                                        // نطبع الرصيد بعد العملية
                                        System.out.println("Balance After: " + transaction.getBalanceAfter());
                                        // نطبع تاريخ ووقت العملية
                                        System.out.println("Date/Time: " + transaction.getDateTime());
                                    }
                                }

                                // نطبع الرصيد النهائي في نهاية كشف الحساب
                                System.out.println("Total Amount in Account: " + account.getBalance());

                            } else {

                                // إذا الحساب مو تابع للعميل الحالي
                                System.out.println("You cannot view another customer's statement.");
                            }

                        } else {

                            // إذا الحساب غير موجود
                            System.out.println("Account not found.");
                        }

                        // ننهي case 7
                        break;
                    case 8: {
                        // Filters account transactions by a selected date period
                        // العميل اختار فلترة العمليات حسب التاريخ
                        System.out.println("FILTER TRANSACTIONS");
                        // ننظف السطر المتبقي بعد قراءة رقم الاختيار
                        scanner.nextLine();
                        // نطلب رقم الحساب
                        System.out.print("Enter Account ID: ");
                        String filterAccountId = scanner.nextLine();

                        // نبحث عن الحساب
                        Optional<Account> filterAccount = bankSystem.getAccountService()
                                .findAccountById(filterAccountId);

                        // نتأكد إن الحساب موجود
                        if (filterAccount.isPresent()) {
                            // ناخذ الحساب
                            Account account = filterAccount.get();
                            // نتأكد إن الحساب تابع للعميل الحالي
                            if (account.getCustomerId().equals(customer.getCustomerId())) {
                                // نعرض خيارات الفلترة
                                System.out.println("1. Today");
                                System.out.println("2. Yesterday");
                                System.out.println("3. Last Week");
                                System.out.println("4. Last 7 Days");
                                System.out.println("5. Last Month");
                                System.out.println("6. Last 30 Days");
                                System.out.print("Please select a filter: ");
                                // نقرا اختيار العميل
                                int filterChoice = scanner.nextInt();
                                // نسوي متغير لبداية الفترة
                                LocalDateTime startDate = null;
                                // نسوي متغير لنهاية الفترة
                                LocalDateTime endDate = null;
                                // نجيب الوقت الحالي
                                LocalDateTime now = LocalDateTime.now();
                                // فلتر اليوم
                                if (filterChoice == 1) {
                                    // بداية اليوم
                                    startDate = LocalDate.now().atStartOfDay();
                                    // نهاية الفترة هي الوقت الحالي
                                    endDate = now;


                                    // فلتر أمس
                                } else if (filterChoice == 2) {
                                    // نجيب تاريخ أمس
                                    LocalDate yesterday = LocalDate.now().minusDays(1);
                                    // بداية أمس
                                    startDate = yesterday.atStartOfDay();
                                    // نهاية أمس
                                    endDate = yesterday.atTime(23, 59, 59);

                                    // فلتر الأسبوع السابق
                                } else if (filterChoice == 3) {
                                    // نجيب يوم الاثنين من الأسبوع السابق
                                    LocalDate lastWeekStart = LocalDate.now().minusWeeks(1).with(
                                            TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

                                    // نجيب يوم الأحد من نفس الأسبوع
                                    LocalDate lastWeekEnd = lastWeekStart.plusDays(6);

                                    // بداية الأسبوع السابق
                                    startDate = lastWeekStart.atStartOfDay();

                                    // نهاية الأسبوع السابق
                                    endDate = lastWeekEnd.atTime(23, 59, 59);

                                    // فلتر آخر 7 أيام
                                } else if (filterChoice == 4) {
                                    // من قبل 7 أيام إلى الحين
                                    startDate = now.minusDays(7);
                                    // نهاية الفترة هي الحين
                                    endDate = now;
                                    // فلتر الشهر السابق
                                } else if (filterChoice == 5) {
                                    // نجيب أول يوم من الشهر السابق
                                    LocalDate lastMonthStart = LocalDate.now()
                                            .minusMonths(1).withDayOfMonth(1);
                                    // نجيب آخر يوم من الشهر السابق
                                    LocalDate lastMonthEnd = lastMonthStart.with
                                            (TemporalAdjusters.lastDayOfMonth());
                                    // بداية الشهر السابق
                                    startDate = lastMonthStart.atStartOfDay();
                                    // نهاية الشهر السابق
                                    endDate = lastMonthEnd.atTime(23, 59, 59);


                                    // فلتر آخر 30 يوم
                                } else if (filterChoice == 6) {
                                    // من قبل 30 يوم إلى الحين
                                    startDate = now.minusDays(30);
                                    // نهاية الفترة هي الحين
                                    endDate = now;
                                } else {
                                    // إذا العميل اختار رقم غير موجود
                                    System.out.println("Invalid filter option.");
                                }
                                // نتأكد إن العميل اختار فلتر صحيح
                                if (startDate != null && endDate != null) {
                                    // نجيب العمليات الموجودة داخل الفترة المطلوبة
                                    ArrayList<Transaction> filteredTransactions = bankSystem.getTransactionService()
                                            .filterTransactionsByDate(account.getAccountId(), startDate, endDate);

                                    // نطبع عنوان النتائج
                                    System.out.println(" FILTERED TRANSACTIONS ");

                                    // إذا ما في عمليات بالفترة
                                    if (filteredTransactions.isEmpty()) {
                                        // نخبر العميل
                                        System.out.println("No transactions found for this period.");

                                    } else {

                                        // نمر على العمليات ونطبع تفاصيلها
                                        for (Transaction transaction : filteredTransactions) {
                                            // نطبع رقم العملية
                                            System.out.println("Transaction ID: " + transaction.getTransactionId());
                                            // نخزن نوع العملية عشان نعرضه بشكل أوضح
                                            String filterDisplayType =
                                                    transaction.getTransactionType();
                                            // إذا المبلغ طلع من الحساب نعرض SENT
                                            if (filterDisplayType.equals("TRANSFER_OUT")) {
                                                filterDisplayType = "SENT";

                                            // إذا المبلغ دخل إلى الحساب نعرض RECEIVED
                                            } else if (filterDisplayType.equals("TRANSFER_IN")) {
                                                filterDisplayType = "RECEIVED";
                                            }
                                              // نعرض نوع العملية
                                            System.out.println("Type: " + filterDisplayType);
                                            // نطبع المبلغ
                                            System.out.println("Amount: " + transaction.getAmount());
                                            // نطبع الرصيد بعد العملية
                                            System.out.println("Balance After: " + transaction.getBalanceAfter());
                                            // نطبع التاريخ والوقت
                                            System.out.println("Date/Time: " + transaction.getDateTime());
                                        }
                                    }
                                }

                            } else {

                                // نمنع العميل من مشاهدة عمليات حساب شخص ثاني
                                System.out.println("You cannot view another customer's transactions.");
                            }

                        } else {

                            // إذا الحساب غير موجود
                            System.out.println("Account not found.");
                        }

                        // ننهي case 8
                        break;
                    }
                    case 9:
                        // Logs the customer out of the system
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
