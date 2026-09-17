package BankServices;

// نستخدم User عشان نخزن ونتعامل مع المستخدمين
import BankModels.User;
// نستخدم PasswordHashingUtility عشان نحول الباسورد إلى Hash
import BankUtilities.PasswordHashingUtility;
// نستخدم Exception خاصة بقفل الحساب
import BankExceptions.AccountLockedException;
// نستخدم ArrayList لتخزين المستخدمين
import java.util.ArrayList;
// نستخدم Optional للبحث عن المستخدم بدون ما نرجع null
import java.util.Optional;
// Handles user login, password checking, and account locking
public class LoginService {
    // نخزن جميع المستخدمين اللي يقدرون يسجلون دخول
    private ArrayList<User> users;
    // Constructor
    public LoginService() {
        // ننشئ قائمة فاضية للمستخدمين
        users = new ArrayList<>();
    }
    // Adds a user to the login system
    // نضيف مستخدم إلى نظام تسجيل الدخول
    public void addUser(User user) {users.add(user);
    }
    // Finds a user using the User ID
    // نبحث عن مستخدم باستخدام User ID
    public Optional<User> findUserById(String userId) {

        // نستخدم Stream و Lambda للبحث عن المستخدم
        return users.stream().filter(user -> user.getUserId().equals(userId)).findFirst();
    }
    // Handles the user login process
    // ميثود تسجيل الدخول
    public Optional<User> login(String userId, String password) {
        // نبحث عن المستخدم
        Optional<User> optionalUser = findUserById(userId);
        // إذا المستخدم غير موجود
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }
        // ناخذ المستخدم من Optional
        User user = optionalUser.get();
        // نجيب الوقت الحالي
        long currentTime = System.currentTimeMillis();
        // Checks if the account is currently locked
        // إذا الحساب للحين مقفول
        if (user.getLockUntilTime() > 0 && currentTime < user.getLockUntilTime()) {

            // نرمي Exception خاصة بقفل الحساب
            throw new AccountLockedException("Account is locked. Please try again later.");
        }
        // Resets the lock after the lock time has finished
        // إذا مدة القفل انتهت
        if (user.getLockUntilTime() > 0 && currentTime >= user.getLockUntilTime()) {
            // نصفر عدد المحاولات الفاشلة
            user.setFailedLoginAttempts(0);
            // نشيل وقت القفل
            user.setLockUntilTime(0);
        }
        // نحول الباسورد اللي أدخله المستخدم إلى Hash
        String enteredPasswordHash = PasswordHashingUtility.hashPassword(password);
        // Checks if the entered password is correct
        // نتأكد إذا الباسورد صحيح
        if (user.getPasswordHash().equals(enteredPasswordHash)) {
            // إذا نجح الدخول نصفر المحاولات الفاشلة
            user.setFailedLoginAttempts(0);
            // نشيل أي قفل سابق
            user.setLockUntilTime(0);
            // نرجع المستخدم
            return Optional.of(user);
        }

        // إذا الباسورد غلط نزيد عدد المحاولات الفاشلة
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        // Locks the account after three failed login attempts
        // إذا وصل المستخدم إلى 3 محاولات فاشلة
        if (user.getFailedLoginAttempts() >= 3) {

            // نقفل الحساب لمدة دقيقة واحدة
            user.setLockUntilTime(System.currentTimeMillis() + 60000);

            // نرمي Exception خاصة بقفل الحساب
            throw new AccountLockedException(
                    "Account locked for 1 minute after 3 failed login attempts.");
        }
        // إذا الباسورد غلط لكن ما وصل 3 محاولات
        return Optional.empty();
    }
    // Checks if a user account is currently locked
    // نتحقق إذا المستخدم مقفول حالياً
    public boolean isUserLocked(String userId) {
        // نبحث عن المستخدم
        Optional<User> optionalUser = findUserById(userId);
        // إذا المستخدم غير موجود
        if (optionalUser.isEmpty()) {

            return false;
        }
        // ناخذ المستخدم
        User user = optionalUser.get();
        // نتحقق إذا وقت القفل للحين ما انتهى
        return user.getLockUntilTime() > 0 && System.currentTimeMillis()
                < user.getLockUntilTime();
    }
    // Returns all users in the login system
    // نرجع جميع المستخدمين إذا احتجناهم
    public ArrayList<User> getAllUsers() {
        return users;
    }
}