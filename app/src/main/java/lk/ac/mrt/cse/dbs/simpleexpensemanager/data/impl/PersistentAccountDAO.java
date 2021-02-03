package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DBHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DBHelper db;
    public PersistentAccountDAO(DBHelper db){
        this.db=db;
    }
    @Override
    public List<String> getAccountNumbersList() {
        List accountNumbers= new ArrayList<String>();
        Cursor res=db.getAccountNumbersList();
        while (res.moveToNext()){
            accountNumbers.add(res.getString(0));
        }
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        Account account;
        List accounts= new ArrayList<Account>();
        Cursor res= db.getAccountDetails();
        while (res.moveToNext()){
            String account_no= res.getString(0);
            String holder_name= res.getString(1);
            Double balance= res.getDouble(2);
            String bank_name=res.getString(3);
            account=new Account(account_no,bank_name,holder_name,balance);
            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account;
        Cursor res= db.getAccountDtl(accountNo);
        if(res.getCount()>0){
            res.moveToFirst();
            String account_no=res.getString(0);
            String holder_name=res.getString(1);
            Double balance= res.getDouble(2);
            String bank_name= res.getString(3);
            account=new Account(accountNo,bank_name,holder_name,balance);
        }else {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        return account;
    }

    @Override
    public void addAccount(Account account) {
        String account_no=account.getAccountNo();
        String holder_name=account.getAccountHolderName();
        String bank_name= account.getBankName();
        double balance= account.getBalance();
        db.insertAddAccountDetails(account_no,bank_name,holder_name,balance);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        Boolean check=db.deleteAccount(accountNo);
        if(!check){
            throw new InvalidAccountException("The account "+accountNo+" is invalid");
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Cursor res= db.getBalance(accountNo);
        if ((res.getCount()<1)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                res.moveToFirst();
                double expensebalance= res.getDouble(0)-amount;
                db.updateBalance(accountNo,expensebalance);
                break;
            case INCOME:
                res.moveToFirst();
                double incomebalance= res.getDouble(0)+amount;
                db.updateBalance(accountNo,incomebalance);
                break;
        }
    }
}
