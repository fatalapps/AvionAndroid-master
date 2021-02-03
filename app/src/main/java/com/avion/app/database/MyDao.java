package com.avion.app.database;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.avion.app.entity.BankCardEntity;
import com.avion.app.entity.CountryEntity;
import com.avion.app.entity.FaworiteAddressEntity;
import com.avion.app.entity.QuaryAddressEntity;
import com.avion.app.entity.RegionEntity;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MyDao {

    @Insert(onConflict = REPLACE)
    void quary_addressSave(QuaryAddressEntity quaryAddressEntity);

    @Query("SELECT * FROM QuaryAddressEntity WHERE name LIKE :txt")
    LiveData<List<QuaryAddressEntity>> quaryAddress(String txt);

    @Insert(onConflict = REPLACE)
    void regionsSaveAll(List<RegionEntity> regionEntityList);

    @Query("SELECT * FROM regionentity WHERE name LIKE :name")
    DataSource.Factory<Integer, RegionEntity> regionList(String name);

    @Insert(onConflict = REPLACE)
    void countriesSaveAll(List<CountryEntity> regionEntityList);

    @Query("SELECT * FROM countryentity WHERE name LIKE :name")
    List<CountryEntity> countryList(String name);

    @Insert(onConflict = REPLACE)
    void bankcard_save(BankCardEntity bankCardEntity);

    @Insert(onConflict = REPLACE)
    void bankcard_saveSAll(List<BankCardEntity> bankCardEntityList);

    @Query("SELECT * FROM BANKCARDENTITY")
    DataSource.Factory<Integer, BankCardEntity> bacnkcard_list();

    @Delete
    void deleteCard(BankCardEntity entity);

    @Query("DELETE FROM BANKCARDENTITY")
    void clearCards();

    @Insert(onConflict = REPLACE)
    void favorite_addressSave(FaworiteAddressEntity faworiteAddressEntity);

    @Query("SELECT * FROM faworiteaddressentity")
    DataSource.Factory<Integer, FaworiteAddressEntity> favorite_addressList();

    @Delete
    void favorite_addressDelete(FaworiteAddressEntity faworiteAddressEntity);

}
