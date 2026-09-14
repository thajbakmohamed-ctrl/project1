package BankModels;
public class Customer extends User {

    private String customerId;

    // نحدد إذا العميل لازم يغير كلمة المرور المؤقتة
    private boolean mustChangePassword;


    public Customer(String customerId, String userId, String name,
                    String email, String phone, String passwordHash) {

        super(userId, name, email, phone, passwordHash, "CUSTOMER");

        this.customerId = customerId;

        // العميل الجديد لازم يغير كلمة المرور المؤقتة
        this.mustChangePassword = true;
    }


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }


    // نرجع إذا العميل لازم يغير كلمة المرور
    public boolean isMustChangePassword() {
        return mustChangePassword;
    }

    // نغير حالة تغيير كلمة المرور
    public void setMustChangePassword(boolean mustChangePassword) {
        this.mustChangePassword = mustChangePassword;
    }
}
