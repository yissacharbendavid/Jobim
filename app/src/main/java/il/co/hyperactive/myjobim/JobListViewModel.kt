package il.co.hyperactive.myjobim

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class JobListViewModel: ViewModel() {

    private val jobRepository = JobRepository.get()

    val jobListLiveData: LiveData<List<Job>> = jobRepository.getJobs()

    fun addJob(job: Job) {
        jobRepository.addJob(job)
    }

    fun getJobBackground(jobTitle: String): Int{
        return jobRepository.getJobBackground(jobTitle)
    }
    fun getJobIcon(jobTitle: String): Int{
        return jobRepository.getJobIcon(jobTitle)
    }
}
