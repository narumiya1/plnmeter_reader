package com.example.myapplicationpln.roomDb;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Gspinner.class,GIndeksSpinner.class,Gimage.class,GCameraValue.class}, version = 15)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GhistorySpinner gHistorySpinnerDao();

    //migrate for add table
    @VisibleForTesting
    public static final Migration MIGRATION_1_6 = new Migration(1, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tbarang "
                    + " ADD COLUMN image String");
        }
    };
}
