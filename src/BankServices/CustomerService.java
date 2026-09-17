package BankServices;
//أبي أستخدم Customer الموجود في BankModels
import BankModels.Customer;
//أبي أستخدم ArrayList الجاهزة في Java
import java.util.ArrayList;
// حطينا اوبشينال في احتمالين موجود او مو موجود
import java.util.Optional;
// Manages customer data and customer operations
public class CustomerService {
    //سوينا اري لان نسوي حق اكثر من كستمر الاري عدد ثابت الاري لست مو ثابت
    private ArrayList<Customer> customers;
    //كونستركتر حق الكستمر سيرفس
    public CustomerService() {
        customers = new ArrayList<>();
    }
    // void يعني الميثود تنفذ الإضافة بدون ما ترجع قيمة
    // Adds a new customer to the customer list
    public void addCustomer(Customer customer) {
        //الاري اسمها كستمر و احنا يوم سوينا ادد اضفنا الكستمرز
        // ف لو بعدين في عميل جديد بينضاف لي الكستمرز
        customers.add(customer);
    }
    //ArrayList تخزن البيانات، Stream تساعدني أعالج وأبحث في البيانات.
    // Finds a customer using the Customer ID
    public Optional<Customer> findCustomerById(String customerId) {
        //يعني خذي قائمة العملاء و حوليها ستريم
        // وابدئي أمرّ على العملاء الموجودين فيها CUSTOMER STREAM
        return customers.stream()
                //فلتر / اختاري العميل اللي يطابق الشرط
                //بعد الفلتر نحط الشرط
                //لكل customer في القائمة، جيبي الـcustomerId
                // ماله وشوفي هل يساوي الـID اللي إحنا نبحث عنه
                // الايكوالز عشان نقارن الايدي لان سترنق
                .filter(customer -> customer.getCustomerId().equals
                        (customerId))
                //.findFirst(); يعني اول كستمر مطابق حطه
                // ف اذا لقى الكستمر اوكي اذا مالقى يكون ايمتي
                // إذا لقى Customer يرجعه داخل Optional، وإذا ما لقى يرجع Optional.empty()
                .findFirst();
    }
    // Returns all customers in the system
    public ArrayList<Customer> getAllCustomers() {
        return customers;
    }

}
