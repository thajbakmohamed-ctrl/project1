package BankModels;

public class Customer extends User{
    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public Customer(String customerId,String userId, String name, String email,
                    String phone, String passwordHash) {
        super(userId, name, email, phone, passwordHash,"CUSTOMER");
        this.customerId = customerId;
    }
}
