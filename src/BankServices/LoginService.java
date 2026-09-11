package BankServices;
import BankModels.User;
import java.util.ArrayList;
import java.util.Optional;

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
    // ميثود لتسجيل دخول المستخدم باستخدام رقم المستخدم وكلمة المرور
    public Optional<User> login(String userId, String password) {
        // نبحث عن المستخدم باستخدام رقم المستخدم
        Optional<User> foundUser = findUserById(userId);
        // إذا المستخدم غير موجود نوقف تسجيل الدخول
        if (foundUser.isEmpty()) {
            return Optional.empty();
        }
        // نطلع المستخدم الموجود داخل الـ Optional ونخزنه في متغير
        User user = foundUser.get();
        return Optional.empty();
    }
}
