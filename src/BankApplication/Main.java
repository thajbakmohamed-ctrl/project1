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

                         // اذا المستخدم عميل نفتح له قائمة العميل
                        } else if (currentUser.getRole().equals("CUSTOMER")) {

                            CustomerMenu.showCustomerMenu(scanner, bankSystem, currentUser);
                        }
                    } else {
                        // اذا رقم المستخدم أو كلمة المرور غلط
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
                    // اذا المستخدم كتب خيار غير موجود في القائمة
                    System.out.println("Invalid option. Please try again.");

            }
        }
        // نقفل ال Scanner بعد ما ينتهي البرنامج
        scanner.close();
    }
}
