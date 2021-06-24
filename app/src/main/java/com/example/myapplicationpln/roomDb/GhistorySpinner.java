package com.example.myapplicationpln.roomDb;

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertImage(Gimage img);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertIdIdx(GIndeksSpinner idx);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCamera(GCameraValue cmr);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUserData(GUserData userData);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMeterData(GmeterApi gmeterApi);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHistoryiData(Ghistoryi ghistoryi);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertImageTemp(GimageUploaded gimageUploaded);

    @Query("SELECT value_int FROM tbIndeks WHERE type=1")
    int selectIndeks();

    @Query("SELECT * FROM tbSpinner")
//    Gspinner[] selectAllItems();
    List<Gspinner> selectAllItems();

    @Query("SELECT id_pelanggan FROM tbSpinner")
    List<Integer> getAllLItems();

    @Query("SELECT id_pelanggan FROM tbSpinner")
    Integer[] getAllLItemsArray();

    @Query("SELECT address FROM tbSpinner WHERE id_pelanggan =:id_pelanggan")
    int selectIndeks(int id_pelanggan);

    @Query("SELECT meter FROM tbMeterApi WHERE type =1")
    int selectMeter();

    @Query("SELECT * FROM tbSpinner")
    Gspinner[] readDataAddress();

    @Query("SELECT * FROM tbHistory")
    Ghistoryi[] readDataHistory();

    @Query("SELECT name FROM tbUserData")
    String readDataUser();


    @Query("SELECT id_user,username  FROM tbUserData WHERE id_user =18")
    GUserData[] selectDataUser();

    @Query("SELECT image FROM tbImageUploaded WHERE status =0")
//    GimageUploaded[] selectImageStatus();
    List<String> selectImageStatus();
    @Query("SELECT * FROM tbUserData WHERE id_user = :nim LIMIT 1")
    GUserData selectDetailUserData(String nim);

    @Query("SELECT id_pelanggan FROM tbSpinner WHERE user_address_id=26")
    int selectIndeksSpinner();

    @Query("SELECT COUNT (*) FROM tbSpinner WHERE id_pelanggan =:id_pelanggan ")
    int getCount(int id_pelanggan);

    @Query("SELECT COUNT (*) FROM tbIndeks WHERE type = 1")
    int getCountIdx();

    @Query("SELECT COUNT (*) FROM tbUserData WHERE status = 1")
    int getCounTbUser();
    @Update
    int updateUserData(GUserData gUserData);
    @Query("UPDATE tbUserData SET username = :username, email =:email WHERE id_user= :id")
    int updateDataUser(int id,String username, String email);
    @Query("UPDATE tbImageUploaded SET status = :status")
    int updateImageStatus(int status);
    @Update
    int updateGrainSelected(Gspinner gindeks);

    @Update
    int updateGrainSelectedGspinner(GIndeksSpinner gindeks);

    @Query("UPDATE tbSpinner SET id_pelanggan = :id_pelanggan, address =:address WHERE user_address_id = :id")
    int updateIndeks(int id, String address, int id_pelanggan);

    @Query("SELECT image FROM tbImage")
    List<String> getImageStorage();
    @Query("SELECT imagez FROM tbHistory WHERE meter = :meter")
    String getImageHistory(String meter);
    @Query("SELECT meter FROM tbMeterApi")
    List<String> getMetet();

    @Query("SELECT COUNT (*) FROM tbImage WHERE type = 1")
    int getCountimage();
    @Query("SELECT COUNT (*) FROM tbCamera WHERE type = 1")
    int getCountCamera();
    @Update
    int updateImageSelected(Gimage gimage);
    @Update
    int updateMeterApiVal(GmeterApi gmeterApi);
    @Update
    int updateCameraValue(GCameraValue gCameraValue);
}
