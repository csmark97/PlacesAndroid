package hu.bme.aut.csmark.placesandroid.model.place;

import android.annotation.SuppressLint;
import android.arch.persistence.room.TypeConverter;

public class Contact {
    private static int zip;
    private static String city;
    private static String street;
    private static int houseNumber;
	private static String phoneNumber;
	private static String email;
	private static String web;

	public Contact(){}

    public Contact(int zip, String city, String street, int houseNumber, String phoneNumber, String email, String web) {
        this.zip = zip;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.web = web;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @SuppressLint("DefaultLocale")
    @TypeConverter
    public static String fromContact(Contact contact){
        if (contact == null) {
            return null;
        }
        return String.format("%s,%s,%s,%s,%s,%s,%s", zip, city, street, houseNumber, phoneNumber, email, web);
    }

    @TypeConverter
    public static Contact toCantact(String contactString){
        if(contactString == null) {
            return null;
        }

        String[] pieces = contactString.split(",");
        Contact result = new Contact(Integer.parseInt(pieces[0]), pieces[1], pieces[2], Integer.parseInt(pieces[3]), pieces[4], pieces[5], pieces[6]);

        return result;
    }



    @Override
	public String toString() {
		return "Cím: " + zip + city + street + houseNumber + "\n" +
				"Telefonszám: " + phoneNumber + "\n" +
				"E-Mail: " + email + "\n" +
				"Web: " + web + "\n" ;
	}		
}