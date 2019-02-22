package hsn.inf333finalproject;

public class User {
    private String name;
    private String phone;
    private String diagnosis;

    public User(String name, String phone, String diagnosis) {
        this.name = name;
        this.phone = phone;
        this.diagnosis = diagnosis;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDiagnosis() {
        return diagnosis;
    }
}
