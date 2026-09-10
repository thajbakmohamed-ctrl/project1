package BankModels;

public abstract class User {
    // اخترت برايفت عشان ما اسمح تدخل مباشرة و تغير اليوزر بعدين نسوي getters and setters
    // و اخترت سترنق لان اليوزر ايدي ممكن يحتوي على ارقام و حروف
    //   //و الاثنين يعتبرون يوزرز ف اليوزر صاير بارنت لهم
    private String userId;
    //الاثنين البانكر و الكستمر عندهم نيم ماله
    // داعي نكرره نخليه ك بارنت عقب يرثونه
    private String name;
    // نفس الشي هو مشترك بينهم و اخترنا برايفت عشان نحمي البيانات عقب نسوي قيترز اند سيترز
    private String email;
    // الرقم بعد مشترك و حطيناه برايفت عشان الكلاسات الثانية ماتقدر تغيره مباشرة و خليناه
    // سترنق لان مو عملية حسابية و ممكن يكون فيه فتح الخط
    private String phone;
    //الهاش هو مايخزن الباسوورد هو يخزن نتيجة  الهاشنق
    // نفس اللي يعطيه قيمة ثانيه و عقب يوم اليوزر يرجع يحط الباسوورد
    // يقارن الهاش اللي سواه مع الهاش اللي نتج عن الباسوورد اللي انحط
    private String passwordHash;
    // الرول بيساعدنا في تحديد اذا كان بنكر او كستمر و عقب بيتاكد من اليوزر و الباسوورد اذا كان بنكر بيروح حق سستم البنكر اذا كستمر بيروح حق سستم الكستمر
    private String role;
    public User(String userId, String name, String email,
                String phone, String passwordHash, String role) {
        // اول يوزر اهي الفيلد الحقيقي الموجود في الاوبجكت
        // و ثاني يوزر اهي البارميتر اللي وصل للكونستركتر
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.role = role;
    }
    // getters
    // جنة يقول عطني الايدي و خليناه ببلك لان باقي
    // الكلاسات بيحتاجونه و ماحطيت شي بين القوسين لان بس بيقرا
    public String getUserId() {
        return userId;
    }
    //setters
    // يغسر لنا القيمة بس ماسويت سيترز حق اليوزر ولا الرول لان مانبيهم يتغيرون
    // و حطينا فويد لان بس تغير القيمة

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }




}
