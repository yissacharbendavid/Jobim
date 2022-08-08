package il.co.hyperactive.myjobim.database

import androidx.lifecycle.LiveData
import androidx.room.*
import il.co.hyperactive.myjobim.Job
import java.util.*

@Dao
interface JobDao {

    @Query("SELECT * FROM job")
    fun getJobs(): LiveData<List<Job>>

    @Query("SELECT * FROM job WHERE id=(:id)")
    fun getJob(id: UUID): LiveData<Job?>

    @Insert
    fun addJob(job: Job)

    @Update
    fun updateJob(job:Job)

    @Query("DELETE FROM job WHERE id=(:id)")
    fun deleteJob(id: UUID)

}