package hu.bme.aut.csmark.placesandroid.model.account;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;

import java.util.List;

@Entity(tableName = "account")
public class Account {

	@Ignore
	public Account(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public Account(String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}

	@ColumnInfo(name = "id")
	@PrimaryKey(autoGenerate = true)
	public Long id;

	@ColumnInfo(name = "email")
	public String email;

	@ColumnInfo(name = "password")
	public String password;

	@ColumnInfo(name = "name")
	public String name;

	public boolean login(List<Account> allAccounts){
		for (Account acc: allAccounts) {
			if (acc.email.equals(this.email)) {
				if (acc.password.equals(this.password)){
					this.name = acc.name;
					this.id = acc.id;
					return true;
				}
			}
		}
		return false;
	}
}
