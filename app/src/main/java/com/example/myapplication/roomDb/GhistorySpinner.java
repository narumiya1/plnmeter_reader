package com.example.myapplication.roomDb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GhistorySpinner {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertIdx(Gspinner idx);

    @Query("SELECT * FROM tbSpinner")
    Gspinner[] selectAllItems();

    @Query("SELECT id_pelanggan FROM tbSpinner")
    List<Integer> getAllLItems();

    @Query("SELECT address FROM tbSpinner WHERE id_pelanggan =:id_pelanggan")
    int selectIndeks(int id_pelanggan);
    @Query("SELECT id_pelanggan FROM tbSpinner WHERE user_address_id=26")
    int selectIndeksSpinner();
    @Query("SELECT COUNT (*) FROM tbSpinner WHERE id_pelanggan =:id_pelanggan ")
    int getCount(int id_pelanggan);

    @Update
    int updateGrainSelected(Gspinner gindeks);

    @Query("UPDATE tbSpinner SET id_pelanggan = :id_pelanggan, address =:address WHERE user_address_id = :id")
    int updateIndeks(int id,String address, int id_pelanggan);


}
