package com.example.myapplicationpln.roomDb;

import androidx.room.Dao;
import androidx.room.Delete;
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
    long insertMeterData(GMeterApi gmeterApi);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHistoryiData(GHistory GHistory);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHistoryiDataMeter(GhistoryMeter ghistoryMeter);

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

    @Query("SELECT * FROM tblHistoryMeter ORDER BY status ASC ")
    GhistoryMeter[] readDataHistory();
    @Query("SELECT * FROM tblHistoryMeter WHERE status = 3 ORDER BY status ASC")
    GhistoryMeter[] readDataHistory3();
//    @Query("SELECT id_user,score_classfy,id  FROM tbHistory WHERE id_user =18")
//    Ghistoryi[] readDataHistoryn();
    @Query("SELECT * FROM tbImageUploaded WHERE status = 0")
    GimageUploaded[] readDataHistoryStatusNol();

    @Query("SELECT name FROM tbUserData")
    String readDataUser();


    @Query("SELECT id_user,username  FROM tbUserData WHERE id_user =18")
    GUserData[] selectDataUser();

    @Query("SELECT image FROM tbImageUploaded WHERE status =0")
//    GimageUploaded[] selectImageStatus();
    List<String> selectImageStatus();

    //20210702
    //get history where row is uncomoleted (status<>3)
//    @Query("SELECT id_user,imagez FROM tbHistory WHERE status <>3 " )
//    GHistory[] selectHistoryUncomplete();

    @Query("SELECT * FROM tbHistory WHERE status in (1,2)")
    List<GHistory> selectHistoryfromRoom();
    @Query("SELECT * FROM tblHistoryMeter WHERE status in (1,2)")
    List<GhistoryMeter> selectHistoryfromRoomMeter12();
    @Query("SELECT * FROM tbHistory WHERE status in (3)")
    List<GHistory> selectHistoryfromRoom3();

    @Query("SELECT * FROM tblHistoryMeter WHERE status in (3)")
    List<GhistoryMeter> selectHistoryfromRoomMeter();
    @Query("SELECT * FROM tblHistoryMeter WHERE  id_pealanggan = :id_pel")
    List<GhistoryMeter> selectHistoryfromRoomMeterSelected(long id_pel);
    @Query("SELECT imagez FROM tbHistory WHERE status in (1,2)")
    String selectHistoryfromRoomImage();
    @Query("SELECT imagez FROM tbHistory")
     List<String> selectImageStatusfromRoomHistory();
//    @Query("SELECT id FROM tbHistory WHERE status =1")
    @Query("SELECT id FROM tbHistory WHERE status in (1,2)")
    List<Integer> selectIdfromRoomHistory();
    @Query("SELECT id FROM tblHistoryMeter")
    List<Integer> selectIdfromRoomHistoryCount();
    @Query("SELECT meter FROM tblHistoryMeter WHERE id = :selectIdFromHistory LIMIT 1 ")
    Double selectRoomMeterLast(int selectIdFromHistory);
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
    @Query("UPDATE tbHistory SET status = :status, meter =:meter,score_classfy =:clsfy,score_identfy =:idtfy")
    int updateImageStatusFromhistory( int status, String meter,String clsfy,String idtfy);
    @Update
    int updateGrainSelected(Gspinner gindeks);

    @Update
    int updateGrainSelectedGspinner(GIndeksSpinner gindeks);
    @Update
    int updateHistoryFroomRoom(GHistory gindeks);
    @Update
    int updateHistoryFroomRoomMeter(GhistoryMeter gindeks);
    @Query("UPDATE tbSpinner SET id_pelanggan = :id_pelanggan, address =:address WHERE user_address_id = :id")
    int updateIndeks(int id, String address, int id_pelanggan);
    @Query("UPDATE tbSpinner SET id_pelanggan = :id_pelanggan")
    int updateSpinnerValue(int id_pelanggan);
    @Query("SELECT image FROM tbImage")
    List<String> getImageStorage();
    @Query("SELECT imagez FROM tblHistoryMeter WHERE phone = :meter")
    String getImageHistory(String meter);
    @Query("SELECT imagez FROM tbHistory WHERE id = :id")
    String getImageHistoryId(int id);
    @Delete
    void delete(GHistory model);
    @Query("DELETE FROM tbHistory WHERE imagez= :imagezNull")
    void deleteByUserId(String imagezNull);
    @Query("SELECT meter FROM tbMeterApi")
    List<String> getMetet();

    @Query("SELECT COUNT (*) FROM tbImage WHERE type = 1")
    int getCountimage();
    @Query("SELECT COUNT (*) FROM tbCamera WHERE type = 1")
    int getCountCamera();
    @Update
    int updateImageSelected(Gimage gimage);
    @Update
    int updateMeterApiVal(GMeterApi gmeterApi);
    @Update
    int updateCameraValue(GCameraValue gCameraValue);

    //query untuk mengambil status paling atas histiry bla bla bla
    @Query("SELECT * FROM tbHistory ORDER BY status ASC")
    List<GHistory> getPersonsSortByDescLastName();

    //query untuk delete bla bla bla
    @Query("delete from tbHistory where status = 1")
    int deleteImageColumnWhereStaus1();
}
