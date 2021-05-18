package com.example.myapplication.roomDb;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Gspinner.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GhistorySpinner gHistorySpinnerDao();

    //migrate for add table
    @VisibleForTesting
    public static final Migration MIGRATION_1_5 = new Migration(1, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tbarang "
                    + " ADD COLUMN image String");
        }
    };
}
