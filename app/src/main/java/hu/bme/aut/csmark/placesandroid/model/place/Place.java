package hu.bme.aut.csmark.placesandroid.model.place;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;

import java.util.List;

import hu.bme.aut.csmark.placesandroid.model.account.Account;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "place")
public class Place {

	@ColumnInfo(name = "id")
	@PrimaryKey(autoGenerate = true)
	public Long id;

	@ColumnInfo(name = "owner_id")
	public Long ownerId;

	@ColumnInfo(name = "name")
	public String name;

	@ColumnInfo(name = "contact")
	public String contact;

	@ColumnInfo(name = "type")
	public PlaceType placeType;

	@ColumnInfo(name = "rating")
	public Double rating;

	@ColumnInfo(name = "rating_number")
	public Integer ratingNumber;

	public enum PlaceType {
		BOLT{
			public String toString(){
				return "Bolt";
			}
		},INTEZMENY{
			public String toString(){
				return "Intézmény";
			}
		}, ETTEREM{
			public String toString(){
				return "Étterem";
			}
		}, SZALLAS{
			public String toString(){
				return "Szállás";
			}
		}, SZORAKOZAS{
			public String toString(){
				return "Szórakozás";
			}
		}, BEVASARLOKOZPONT{
			public String toString(){
				return "Bevásárlóközpont";
			}
		};
		abstract public String toString();

		@TypeConverter
		public static PlaceType getByOrdinal(int ordinal) {
			PlaceType ret = null;
			for (PlaceType cat : PlaceType.values()) {
				if (cat.ordinal() == ordinal) {
					ret = cat;
					break;
				}
			}
			return ret;
		}

		@TypeConverter
		public static int toInt(PlaceType category) {
			return category.ordinal();
		}
	}


}
