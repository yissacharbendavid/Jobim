package il.co.hyperactive.myjobim

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import java.util.*

class MyJobsFragment: Fragment() {
    interface Callbacks {
        fun onJobSelected(jobId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var jobsRecyclerView: RecyclerView

    private var adapter: JobAdapter = JobAdapter(emptyList())

    private val jobListViewModel: JobListViewModel by lazy {
        ViewModelProviders.of(this).get(JobListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.clearDisappearingChildren()
        val view = inflater.inflate(R.layout.fragment_my_jobs_list,container,false)
        jobsRecyclerView = view.findViewById(R.id.my_jobs_recycler_view) as RecyclerView
        jobsRecyclerView.layoutManager = LinearLayoutManager(context)
        jobsRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jobListViewModel.jobListLiveData.observe(
            viewLifecycleOwner,
            Observer { jobs ->
                jobs?.let {
                    updateUI(jobs.filter { it.isFavorite })
                }
            })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private inner class JobHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener{

        private lateinit var job: Job

        private val titleView = view.findViewById(R.id.list_item_title) as TextView
        private val subtitleView = view.findViewById(R.id.list_item_subtitle) as TextView
        private val addressView = view.findViewById(R.id.list_item_address) as TextView
        private val distanceView = view.findViewById(R.id.list_item_distance) as TextView
        private val iconView = view.findViewById(R.id.list_item_icon) as ImageView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            callbacks?.onJobSelected(job.id)
        }

        fun bind(job:Job){
            this.job = job
            titleView.text = getString(R.string.job_title,job.employer,job.title).replace("is looking for a", "מחפשת")
            subtitleView.text = job.subtitle
            addressView.text = job.location
            distanceView.text = "distance here"
            iconView.setImageResource(jobListViewModel.getJobIcon(job.title))
            itemView.setBackgroundResource(jobListViewModel.getJobBackground(job.title))
        }
    }

    private inner class JobAdapter(var jobs: List<Job>)
        : RecyclerView.Adapter<JobHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : JobHolder {
            val view = layoutInflater.inflate(R.layout.job_list_item, parent, false)
            return JobHolder(view)
        }
        override fun getItemCount() = jobs.size
        override fun onBindViewHolder(holder: JobHolder, position: Int) {

            val job = jobs[position]
                holder.bind(job)
        }
    }
    private fun updateUI(jobs: List<Job>) {
        adapter = JobAdapter(jobs)
        jobsRecyclerView.adapter = adapter
    }
}