package hsn.inf333finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    public Database(Context context) {
        super(context, "EmergencyDB", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableUser(db);
        createTableContact(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User;");
        db.execSQL("DROP TABLE IF EXISTS Contact;");
        onCreate(db);
    }

    private void createTableUser(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS User(phone CHAR(8) PRIMARY KEY, name VARCHAR(45), diagnose VARCHAR(45));");
        insertUser(db);
    }

    private void createTableContact(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Contact(phone CHAR(8) PRIMARY KEY, name VARCHAR(45))");
    }

    // User
    public User getUser() {
        // Get the user
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM User;", null);

        if(res.moveToNext()) {
            User user = new User(res.getString(1), res.getString(0), res.getString(2));
            return user;
        }

        return new User("User not found", "N/A", "N/A");
    }

    public void insertUser(SQLiteDatabase db) {
        // Create the user, this user will have N/A fields by default
        User user = new User("N/A", "N/A", "N/A");

        // Insert user into database
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", user.getName());
        contentValues.put("phone", user.getPhone());
        contentValues.put("diagnose", user.getDiagnosis());

        db.insert("User", null, contentValues);
    }

    public void updateUser(User user, String oldPhone) {
        // Change the values of the user
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", user.getName());
        contentValues.put("phone", user.getPhone());
        contentValues.put("diagnose", user.getDiagnosis());

        db.update("User", contentValues, "phone = ?", new String[] {oldPhone});
    }

    public List<Contact> getContacts() {
        // Get all contacts
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Contact", null);

        List<Contact> contacts = new ArrayList<>();
        while(res.moveToNext()) {
            contacts.add(new Contact(res.getString(1), res.getString(0)));
        }

        return contacts;
    }

    public long insertContact(Contact contact) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", contact.getName());
        contentValues.put("phone", contact.getPhone());

        return db.insert("Contact", null, contentValues);
    }

    public void updateContact(Contact contact, String oldPhone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", contact.getName());
        contentValues.put("phone", contact.getPhone());

        db.update("Contact", contentValues, "phone = ?", new String[] {oldPhone});
    }

    public void deleteContact(String phone) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Contact", "phone = ?", new String[] {phone});
    }
}
