package BankServices;
import BankModels.User;
import java.util.ArrayList;
import java.util.Optional;
// نستخدم أداة الهاشنق عشان نحول كلمة المرور المدخلة إلى Hash
import BankUtilities.PasswordHashingUtility;

public class LoginService {
    // استخدمنا يوزر لان مشترك بين البنكر و الكستمر و الاثنين يسسون لوق ان
    private ArrayList<User> users;
    // ننشئ قائمة فاضية نقدر نخزن فيها كل المستخدمين
    public LoginService() {
        users = new ArrayList<>();
    }
    // ميثود لإضافة مستخدم جديد إلى قائمة المستخدمين
    public void addUser(User user) {
        // نضيف المستخدم الجديد إلى قائمة المستخدمين
        users.add(user);

    }
    public Optional<User> findUserById(String userId) {
        // نمر على كل المستخدمين ونبحث عن المستخدم اللي رقمه يساوي الرقم المطلوب
        return users.stream()
                .filter(user -> user.getUserId().equals(userId))
                // يرجع أول مستخدم مطابق، وإذا ما لقى يرجع نتيجة فاضية
                .findFirst();
    }
    // نسجل دخول المستخدم ونتابع عدد المحاولات الفاشلة
    public Optional<User> login(String userId, String password) {

        // نبحث عن المستخدم باستخدام User ID
        Optional<User> foundUser = findUserById(userId);

        // إذا المستخدم مو موجود نرجع نتيجة فاضية
        if (foundUser.isEmpty()) {
            return Optional.empty();
        }

        // نطلع المستخدم من Optional
        User user = foundUser.get();

        // نجيب الوقت الحالي
        long currentTime = System.currentTimeMillis();

        // نتأكد إذا الحساب للحين مقفول
        if (currentTime < user.getLockUntilTime()) {

            // نخبر المستخدم إن الحساب مقفول مؤقتًا
            System.out.println("Account is locked. Please try again after one minute.");

            // نرفض تسجيل الدخول
            return Optional.empty();
        }

        // إذا انتهت مدة القفل نصفر المحاولات
        if (user.getLockUntilTime() > 0 &&
                currentTime >= user.getLockUntilTime()) {

            // نصفر عدد المحاولات الفاشلة
            user.setFailedLoginAttempts(0);

            // نشيل وقت القفل
            user.setLockUntilTime(0);
        }

        // نحول كلمة المرور المدخلة إلى Hash
        String hashedPassword =
                PasswordHashingUtility.hashPassword(password);

        // إذا كلمة المرور صحيحة
        if (hashedPassword.equals(user.getPasswordHash())) {

            // نصفر المحاولات الفاشلة بعد تسجيل دخول ناجح
            user.setFailedLoginAttempts(0);

            // نرجع المستخدم
            return Optional.of(user);
        }

        // إذا كلمة المرور غلط نزيد عدد المحاولات الفاشلة
        user.setFailedLoginAttempts(
                user.getFailedLoginAttempts() + 1
        );

        // إذا وصل إلى 3 محاولات فاشلة
        if (user.getFailedLoginAttempts() >= 3) {

            // نقفل الحساب لمدة دقيقة واحدة
            user.setLockUntilTime(
                    System.currentTimeMillis() + 60000
            );

            // نخبر المستخدم إن الحساب انقفل
            System.out.println(
                    "Too many failed login attempts. Account locked for one minute."
            );
        }

        // تسجيل الدخول فشل
        return Optional.empty();
    }
    // نتحقق إذا المستخدم مقفول مؤقتًا
    public boolean isUserLocked(String userId) {

        // نبحث عن المستخدم
        Optional<User> foundUser = findUserById(userId);

        // إذا المستخدم مو موجود فهو مو مقفول
        if (foundUser.isEmpty()) {
            return false;
        }

        // نطلع المستخدم من Optional
        User user = foundUser.get();

        // نرجع true إذا وقت القفل للحين ما انتهى
        return System.currentTimeMillis() < user.getLockUntilTime();
    }
}
