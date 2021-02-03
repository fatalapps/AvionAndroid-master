package com.avion.app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.avion.app.entity.BankCardEntity;
import com.avion.app.entity.CountryEntity;
import com.avion.app.entity.FaworiteAddressEntity;
import com.avion.app.entity.QuaryAddressEntity;
import com.avion.app.entity.RegionEntity;

@Database(entities = {QuaryAddressEntity.class, RegionEntity.class, BankCardEntity.class, FaworiteAddressEntity.class, CountryEntity.class}, version = 1, exportSchema = false)
public abstract class MyDB extends RoomDatabase {
    private static volatile MyDB INSTANCE;

    public static MyDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MyDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyDB.class, "avion-database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract MyDao myDao();

}

