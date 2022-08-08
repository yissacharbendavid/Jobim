package il.co.hyperactive.myjobim

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.util.*


private const val ARG_JOB_ID = "job_id"
private const val TAG = "JobFragment"

class JobFragment : Fragment(){

    private lateinit var job: Job
    private lateinit var titleView: TextView
    private lateinit var subtitleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var callButton: ImageButton
    private lateinit var sendMessageButton: ImageButton
    private lateinit var sendMailButton: ImageButton
    private lateinit var shareButton: ImageButton
    private lateinit var moreFromEmployerButton: ImageButton
    private lateinit var hideButton: ImageButton
    private lateinit var addToFavoritesButton: ImageButton
    private lateinit var iconView: ImageView
    private lateinit var topContainer: ConstraintLayout
    private lateinit var exit_button: ImageButton


    private val jobDetailsViewModel: JobDetailsViewModel by lazy {
        ViewModelProviders.of(this).get(JobDetailsViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        job = Job()
        val jobId: UUID = arguments?.getSerializable(ARG_JOB_ID) as UUID
        jobDetailsViewModel.loadJob(jobId)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_job_details, container, false)
        titleView = view.findViewById(R.id.title) as TextView
        subtitleView = view.findViewById(R.id.subtitle) as TextView
        descriptionView = view.findViewById(R.id.description) as TextView
        callButton = view.findViewById(R.id.call_button) as ImageButton
        sendMailButton = view.findViewById(R.id.send_email_button) as ImageButton
        sendMessageButton = view.findViewById(R.id.send_message_button) as ImageButton
        shareButton = view.findViewById(R.id.share) as ImageButton
        moreFromEmployerButton = view.findViewById(R.id.more_from_employer) as ImageButton
        hideButton = view.findViewById(R.id.hide_job) as ImageButton
        addToFavoritesButton = view.findViewById(R.id.add_to_favorites) as ImageButton
        iconView = view.findViewById(R.id.job_icon) as ImageView
        topContainer = view.findViewById(R.id.top_container) as ConstraintLayout
        exit_button = view.findViewById(R.id.exit_button) as ImageButton
        val mapFragment: Fragment = MapFragment()
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.location, mapFragment).commit()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jobDetailsViewModel.jobLiveData.observe(
            viewLifecycleOwner,
            Observer { job ->
                job?.let {
                    this.job = job
                    updateUI()
                }
            })
    }

    override fun onStart() {
        super.onStart()

        sendMailButton.apply {

            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",job.email,null))

            setOnClickListener{
                startActivity(Intent.createChooser(emailIntent, "Email chooser"))
            }
        }

        callButton.apply{

            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:"+job.phoneNumber)

            setOnClickListener{
                startActivity(Intent.createChooser(callIntent, "Call chooser"))
            }
        }

        sendMessageButton.apply {

            val messageIntent = Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",job.phoneNumber,null))

            setOnClickListener{
                startActivity(Intent.createChooser(messageIntent,"Message Chooser"))
            }
        }

        shareButton.apply {

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT,getString(R.string.share_job,job.employer,job.title))
            }
            setOnClickListener{
                startActivity(Intent.createChooser(shareIntent,"Share via:"))
            }
        }

        exit_button.setOnClickListener{
            activity?.onBackPressed()
        }

        addToFavoritesButton.setOnClickListener{
            job.isFavorite = true
            jobDetailsViewModel
        }

        hideButton.setOnClickListener{
            jobDetailsViewModel.deleteJob(job.id)
            fragmentManager?.popBackStack()
        }
    }

    companion object {
        fun newInstance(jobId: UUID): JobFragment {
            val args = Bundle().apply {
                putSerializable(ARG_JOB_ID, jobId)
            }
            return JobFragment().apply {
                arguments = args
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    fun updateUI() {
        titleView.text = getString(R.string.job_title,job.employer,job.title).replace("is looking for a", "מחפשת")
        subtitleView.text = job.subtitle
        descriptionView.text = job.description
        iconView.setImageResource(jobDetailsViewModel.getJobIcon(job.title))
        topContainer.setBackgroundResource(jobDetailsViewModel.getJobBackground(job.title))
        exit_button.setBackgroundResource(jobDetailsViewModel.getJobBackground(job.title))
        if (job.email.isEmpty()) sendMailButton.apply {
            isEnabled = false
            setBackgroundResource(R.drawable.disabled_button)
        }
        if(job.phoneNumber.isEmpty()) callButton.apply {
            isEnabled = false
            setBackgroundResource(R.drawable.disabled_button)
        }
        if(job.phoneNumber.isEmpty() || job.phoneNumber[1] != '5') sendMessageButton.apply {
            isEnabled = false
            setBackgroundResource(R.drawable.disabled_button)
        }
    }
}