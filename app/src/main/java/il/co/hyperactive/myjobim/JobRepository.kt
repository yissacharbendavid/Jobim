package il.co.hyperactive.myjobim

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import il.co.hyperactive.myjobim.database.JobDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "job-database"

class JobRepository private constructor(context: Context) {

    private val database : JobDatabase = Room.databaseBuilder(
        context.applicationContext,
        JobDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val jobDao = database.jobDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getJobs(): LiveData<List<Job>> = jobDao.getJobs()
    fun getJob(id: UUID): LiveData<Job?> = jobDao.getJob(id)

    fun addJob(job: Job) {
        executor.execute {
            jobDao.addJob(job)
        }
    }

    fun updateJob(job: Job) {
        executor.execute {
            jobDao.updateJob(job)
        }
    }

    fun deleteJob(jobId: UUID) {
        executor.execute {
            jobDao.deleteJob(jobId)
        }
    }

    fun getJobBackground(jobTitle: String): Int{
        return when (jobTitle) {
            "קופאי/ת", "נהג/ת", "סדרנ/ית" -> R.color.job_color_one
            "נציג/ת שירות","עובד/ת תחזוקה","עובד/ת מטבח" -> R.color.job_color_two
            "טבח/ית","עבודה מהבית","בני נוער" -> R.color.job_color_three
            "מתדלק/ת","עובד/ת מחסן","ספר/ית" -> R.color.job_color_four
            "מאבטח/ת","סוכנ/ת נדלן","דייל/ת מכירות" -> R.color.job_color_five
            "שליח/ה","ברמנ/ית","עובד/ת אריזה" -> R.color.job_color_six
            "עובד/ת ניקיון","מלצר/ית" -> R.color.job_color_seven
            else -> R.color.orange
        }
    }
    fun getJobIcon(jobTitle: String): Int{
        return when (jobTitle) {
            "קופאי/ת" -> R.drawable.cashier_icon
            "נציג/ת שירות" -> R.drawable.support_agent_icon
            "טבח/ית" -> R.drawable.cook_icon
            "מתדלק/ת" -> R.drawable.gas_icon
            "מאבטח/ת" -> R.drawable.security_icon
            "שליח/ה" -> R.drawable.delivery_icon
            "עובד/ת ניקיון" -> R.drawable.cleaning_icon
            "נהג/ת" -> R.drawable.driver_icon
            "עובד/ת תחזוקה" -> R.drawable.handyman_icon
            "עבודה מהבית" -> R.drawable.home_icon
            "עובד/ת מחסן" -> R.drawable.warehouse_icon
            "סוכנ/ת נדלן" -> R.drawable.real_estate_icon
            "ברמנ/ית" -> R.drawable.bartender_icon
            "מלצר/ית" -> R.drawable.waiter_icon
            "סדרנ/ית" -> R.drawable.steward_icon
            "עובד/ת מטבח" -> R.drawable.kitchen_worker_icon
            "בני נוער" -> R.drawable.teenager_icon
            "ספר/ית" -> R.drawable.barbershop_icon
            "דייל/ת מכירות" -> R.drawable.salesman_icon
            "עובד/ת אריזה" -> R.drawable.packing_worker_icon
            else -> R.drawable.ic_launcher_foreground
        }
    }


    companion object {
        private var INSTANCE: JobRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = JobRepository(context)
            }
        }
        fun get(): JobRepository {
            return INSTANCE ?:
            throw IllegalStateException("JobRepository must be initialized")
        }
    }
}
