package asu.edu.cse535.locationawarereminder.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Sooraj on 10/20/2016.
 */
public class DBManager {

    private static SQLiteDatabase db;
    private static Context dbContext;
    private static boolean DEBUG = false;

    public DBManager(Context context){
        dbContext = context;
    }

    public void initializeDB(String packageName){
        Constants.setDbPath(packageName);
        db = SQLiteDatabase.openOrCreateDatabase(Constants.DB_PATH + Constants.DB_NAME, null);
        createTaskTable();
        createPropertiesTable();
    }

    public SQLiteDatabase getAppDataBase(){
        return db;
    }

    private static void createTaskTable(){
        String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_TASK + " ( " +
                Task.COLUMN_TASK_ID + " " + Constants.DATATYPE_INT  + " PRIMARY KEY autoincrement" + Constants.COMMA_SEP +
                Task.COLUMN_DESC + " " + Constants.DATATYPE_STRING + " NOT NULL " + Constants.COMMA_SEP +
                Task.COLUMN_TASK_DATE + " " + Constants.DATATYPE_DATETIME + Constants.COMMA_SEP +
                Task.COLUMN_MOT + " " + Constants.DATATYPE_STRING + Constants.COMMA_SEP +
                Task.COLUMN_LAT + " " + Constants.DATATYPE_DOUBLE + "NOT NULL" + Constants.COMMA_SEP +
                Task.COLUMN_LONG + " " + Constants.DATATYPE_DOUBLE + "NOT NULL" + Constants.COMMA_SEP +
                Task.COLUMN_TASK_STATUS + " " + Constants.DATATYPE_STRING + Constants.COMMA_SEP +
                Task.COLUMN_CREATED_DATE + " " + Constants.DATATYPE_DATETIME + " ) ";

        try{
            db.beginTransaction();
            try {
                db.execSQL(CREATE_TABLE_QUERY);
                db.setTransactionSuccessful();
                if(DEBUG)
                    Toast.makeText(dbContext, "Table Task created successfully", Toast.LENGTH_SHORT).show();
            }
            catch (SQLiteException e) {
                Toast.makeText(dbContext , e.getMessage(), Toast.LENGTH_LONG).show();
            }
            finally {
                db.endTransaction();
            }
        }catch (SQLException e){
            Toast.makeText(dbContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static void addTaskToDB(Task t) {
        String INSERT_TABLE_QUERY = "INSERT INTO " + Constants.TABLE_TASK + " ( " + Task.COLUMN_DESC + Constants.COMMA_SEP +
                Task.COLUMN_TASK_DATE + Constants.COMMA_SEP + Task.COLUMN_MOT + Constants.COMMA_SEP + Task.COLUMN_LAT +
                Constants.COMMA_SEP + Task.COLUMN_LONG + Constants.COMMA_SEP + Task.COLUMN_TASK_STATUS + Constants.COMMA_SEP +
                Task.COLUMN_CREATED_DATE + " ) VALUES (" +
                Constants.QUOTE +  t.getDesc() + Constants.QUOTE + Constants.COMMA_SEP + Constants.QUOTE + t.getTaskDate() +
                Constants.QUOTE + Constants.COMMA_SEP + Constants.QUOTE + t.getMot() + Constants.QUOTE + Constants.COMMA_SEP +
                t.getLat() + Constants.COMMA_SEP + t.getLng() + Constants.COMMA_SEP + Constants.QUOTE + t.getStatus() +
                Constants.QUOTE + Constants.COMMA_SEP + Constants.QUOTE + t.getCreatedDate() + Constants.QUOTE + ")";
        try{
            db.beginTransaction();
            try {
                db.execSQL(INSERT_TABLE_QUERY);
                db.setTransactionSuccessful();
                if(DEBUG)
                    Toast.makeText(dbContext, "Task added successfully", Toast.LENGTH_SHORT).show();
            }
            catch (SQLiteException e) {
                Toast.makeText(dbContext, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            finally {
                db.endTransaction();
            }
        }catch (SQLException e){
            Toast.makeText(dbContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private static void createPropertiesTable() {
        String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_PROPERTIES + " ( " +
                Properties.COLUMN_NAME + " " + Constants.DATATYPE_STRING + " PRIMARY KEY" + Constants.COMMA_SEP +
                Properties.COLUMN_VALUE + " " + Constants.DATATYPE_STRING + " ) ";
        try{
            db.beginTransaction();
            try {
                db.execSQL(CREATE_TABLE_QUERY);
                db.setTransactionSuccessful();
                if(DEBUG)
                    Toast.makeText(dbContext, "Table Properties created successfully", Toast.LENGTH_SHORT).show();
            }
            catch (SQLiteException e) {
                Toast.makeText(dbContext , e.getMessage(), Toast.LENGTH_LONG).show();
            }
            finally {
                db.endTransaction();
            }
        }catch (SQLException e){
            Toast.makeText(dbContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static void insertIntoProperties(Properties p) {
        String INSERT_TABLE_QUERY = "INSERT INTO " + Constants.TABLE_PROPERTIES + " ( " +
                Properties.COLUMN_NAME + Constants.COMMA_SEP + Properties.COLUMN_VALUE + " ) VALUES (" +
                Constants.QUOTE +  p.getName() + Constants.QUOTE + Constants.COMMA_SEP +
                Constants.QUOTE + p.getValue() + Constants.QUOTE + ")";
        try{
            db.beginTransaction();
            try {
                db.execSQL(INSERT_TABLE_QUERY);
                db.setTransactionSuccessful();
                if(DEBUG)
                    Toast.makeText(dbContext, "Property " + p.getName() + " added successfully", Toast.LENGTH_SHORT).show();
            }
            catch (SQLiteException e) {
                Toast.makeText(dbContext, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            finally {
                db.endTransaction();
            }
        }catch (SQLException e){
            Toast.makeText(dbContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static void insertPropertyList(HashMap<String, String> propertyList){
        Properties p = new Properties();
        for(HashMap.Entry<String, String> entry : propertyList.entrySet()) {
            p.setName(entry.getKey());
            p.setValue(entry.getValue());
            insertIntoProperties(p);
        }
    }
}
