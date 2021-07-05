package com.example.myapplicationpln.roomDb;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Gspinner.class,GIndeksSpinner.class,Gimage.class,GCameraValue.class, GUserData.class, GMeterApi.class, GHistory.class, GimageUploaded.class}, version = 20)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GhistorySpinner gHistorySpinnerDao();

    //migrate for add table
    @VisibleForTesting
    public static final Migration MIGRATION_1_7 = new Migration(1, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE tbHistory "
//                    + " ADD COLUMN imagez String");
        }
    };
}
