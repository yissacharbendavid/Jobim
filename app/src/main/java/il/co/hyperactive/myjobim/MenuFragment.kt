package il.co.hyperactive.myjobim

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import java.util.*

class MenuFragment: Fragment() {

    interface Callbacks {
        fun onAddJobSelected(jobId: UUID)
    }
    private var callbacks: Callbacks? = null

    private lateinit var detailsImageButton: ImageButton
    private lateinit var detailsTextButton: LinearLayout
    private lateinit var notificationsButton: LinearLayout
    private lateinit var myJobsButton: LinearLayout
    private lateinit var findJobs: LinearLayout
    private lateinit var smartAgentButton: LinearLayout
    private lateinit var aboutUsButton: LinearLayout
    private lateinit var newJobButton: LinearLayout
    private lateinit var sideImageView: ImageView

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
        val view = inflater.inflate(R.layout.fragment_menu,container,false)

        detailsImageButton = view.findViewById(R.id.details_imageButton)
        detailsTextButton = view.findViewById(R.id.menu_item_details)
        notificationsButton = view.findViewById(R.id.menu_item_notifications)
        myJobsButton = view.findViewById(R.id.menu_item_my_jobs)
        findJobs = view.findViewById(R.id.menu_item_find_jobs)
        smartAgentButton = view.findViewById(R.id.menu_item_smart_agent)
        aboutUsButton = view.findViewById(R.id.menu_item_about_us)
        newJobButton = view.findViewById(R.id.menu_item_add_job)
        sideImageView = view.findViewById(R.id.menu_side_imageView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sideImageView.setOnClickListener{
            activity?.onBackPressed()
        }

        newJobButton.setOnClickListener{
            val job = Job()
            jobListViewModel.addJob(job)
            callbacks?.onAddJobSelected(job.id)
        }
    }
    override fun onStart(){
        super.onStart()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

}