package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private DBHelper db;
    public PersistentTransactionDAO(DBHelper db){
        this.db=db;
    }

    @Override
    public void logTransaction(String date, String accountNo, ExpenseType expenseType, double amount) {
        String typeTXT= expenseType.toString();
        db.insertTransactions(amount,typeTXT,date,accountNo);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List transactionList= new ArrayList<Transaction>();
        Transaction transaction;
        Cursor res=db.getTransactionDetails();
        if(res.getCount()>0){
            while (res.moveToNext()){
                String date= res.getString(0);
                String account_no= res.getString(1);
                String type= res.getString(2);
                double amount= res.getDouble(3);
                SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
                try{
                    Date transactiondate= format.parse(date);
                    transaction= new Transaction(transactiondate,account_no,ExpenseType.valueOf(type.toUpperCase()),amount);
                    transactionList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction>limitedTransactioList;
        limitedTransactioList=getAllTransactionLogs();
        int size = limitedTransactioList.size();
        if (size <= limit) {
            return limitedTransactioList;
        }
        // return the last <code>limit</code> number of transaction logs
        return limitedTransactioList.subList(size - limit, size);
    }
}
