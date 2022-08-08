package il.co.hyperactive.myjobim

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


private const val TAG = "JobListFragment"

class JobListFragment: Fragment() {

    interface Callbacks {
        fun onJobSelected(jobId: UUID)
        fun onListStart()
    }

    private var callbacks: Callbacks? = null

    private lateinit var jobRecyclerView: RecyclerView
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
        val view = inflater.inflate(R.layout.fragment_job_list,container,false)

        jobRecyclerView = view.findViewById(R.id.job_recycler_view) as  RecyclerView
        jobRecyclerView.layoutManager = LinearLayoutManager(context)
        jobRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jobListViewModel.jobListLiveData.observe(
            viewLifecycleOwner,
            Observer { jobs ->
                jobs?.let {
                    Log.i(TAG, "Got crimes ${jobs.size}")
                    updateUI(jobs)
                }
            })
    }

    override fun onStart() {
        super.onStart()
        callbacks?.onListStart()
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private inner class JobHolder(view: View)
        : RecyclerView.ViewHolder(view),View.OnClickListener{

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
            Log.d(TAG,"item pressed")
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
        jobRecyclerView.adapter = adapter
    }
}