package il.co.hyperactive.myjobim

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG = "JobDetailsViewModel"

class JobDetailsViewModel: ViewModel() {
    private val jobRepository = JobRepository.get()
    private val jobIdLiveData = MutableLiveData<UUID>()

    var jobLiveData: LiveData<Job?> =
        Transformations.switchMap(jobIdLiveData) { jobId ->
            jobRepository.getJob(jobId)
        }

    fun loadJob(jobId: UUID) {
        jobIdLiveData.value = jobId
    }

    fun saveJob(job:Job) {
        jobRepository.updateJob(job)
        Log.d(TAG,"phone: ${job.phoneNumber}")
        Log.d(TAG, "mail: ${job.email}")    }

    fun deleteJob(jobId: UUID) {
        jobRepository.deleteJob(jobId)
    }

    fun getJobBackground(jobTitle: String): Int{
        return jobRepository.getJobBackground(jobTitle)
    }
    fun getJobIcon(jobTitle: String): Int{
        return jobRepository.getJobIcon(jobTitle)
    }
}