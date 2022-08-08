package il.co.hyperactive.myjobim.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import il.co.hyperactive.myjobim.Job

@Database(entities = [Job::class ], version=1, exportSchema = false)
@TypeConverters(JobTypeConverters::class)
abstract class JobDatabase : RoomDatabase() {
    abstract fun jobDao(): JobDao
}