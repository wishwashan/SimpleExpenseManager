package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context,"180244B",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create Table bank(bank_id INTEGER primary key,bank_name TEXT)");
        db.execSQL("create Table account(account_no TEXT primary key,holder_name TEXT,balance REAL,bank_name TEXT)");
        db.execSQL("create Table transactionDetail(transaction_id INTEGER primary key autoincrement,amount REAL,type TEXT,date TEXT,account_no TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //db.execSQL("drop table if exists bank");
        db.execSQL("drop table if exists account");
        db.execSQL("drop table if exists transactionDetail");
    }

    public Boolean insertAddAccountDetails(String account_no,String bank_name,String holder_name,double balance){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("account_no",account_no);
        contentValues.put("holder_name",holder_name);
        contentValues.put("balance",balance);
        contentValues.put("bank_name",bank_name);
        Cursor cursor= db.rawQuery("select * from account where account_no=?",new String[]{account_no});
        if(cursor.getCount()<1) {
            long result = db.insert("account", null, contentValues);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;/**already have an account*/
        }
    }

    public Boolean deleteAccount(String account_no){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select * from account where account_no=?",new String[]{account_no});
        if(cursor.getCount()>0){
            long result= db.delete("account","account_no=?",new String[]{account_no});
            if(result==-1){
                return false;
            }else {
                return true;
            }
        }else {
            return false;
        }
    }

    public Boolean insertTransactions(Double amount,String type,String date,String account_no){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("amount",amount);
        contentValues.put("type",type);
        contentValues.put("date",date);
        contentValues.put("account_no",account_no);
        long result= db.insert("transactionDetail",null,contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Cursor getAccountNumbersList(){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select account_no from account",null);
        return  cursor;
    }

    public Cursor getAccountDetails(){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from account",null);
        return cursor;
    }

    public Cursor getAccountDtl(String account_no){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select * from account where account_no=?",new String[]{account_no});
        return cursor;
    }

    public Boolean updateBalance(String account_no,double balance){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("balance",balance);
        Cursor cursor= db.rawQuery("select * from account where account_no=?",new String[]{account_no});
        if(cursor.getCount()>0){
            long res= db.update("account",contentValues,"account_no=?",new String[]{account_no});
            if(res==-1){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    public Cursor getBalance(String account_no){
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select balance from account where account_no=?",new String[]{account_no});
        return cursor;
    }

    public Cursor getTransactionDetails(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select date,account_no,type,amount from transactionDetail",null);
        return cursor;
    }

}

