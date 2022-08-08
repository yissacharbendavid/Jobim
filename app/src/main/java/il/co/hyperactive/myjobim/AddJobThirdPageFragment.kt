package il.co.hyperactive.myjobim

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import java.util.*
import androidx.lifecycle.Observer


private const val ARG_JOB_ID = "job_id"

class AddJobThirdPageFragment: Fragment() {

    private lateinit var job: Job
    private lateinit var shortDescriptionEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var instructionTextView: TextView
    private lateinit var firstPageButton: ImageButton
    private lateinit var secondPageButton: ImageButton
    private lateinit var fourthPageButton: ImageButton
    private lateinit var fifthPageButton: ImageButton

    private val jobDetailsViewModel: JobDetailsViewModel by lazy {
        ViewModelProviders.of(this)[JobDetailsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        job = Job()
        val jobId: UUID = arguments?.getSerializable(ARG_JOB_ID) as UUID
        jobDetailsViewModel.loadJob(jobId)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                jobDetailsViewModel.deleteJob(job.id)
                setHasOptionsMenu(false)
                isEnabled = false
                activity?.onBackPressed()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_job_third_page,container,false)
        shortDescriptionEditText = view.findViewById(R.id.short_description)
        descriptionEditText = view.findViewById(R.id.description)
        instructionTextView = view.findViewById(R.id.instructions)
        firstPageButton = view.findViewById(R.id.add_job_page_1)
        secondPageButton = view.findViewById(R.id.add_job_page_2)
        fourthPageButton = view.findViewById(R.id.add_job_page_4)
        fifthPageButton = view.findViewById(R.id.add_job_page_5)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jobDetailsViewModel.jobLiveData.observe(
            viewLifecycleOwner,
            Observer { job ->
                job?.let {
                    this.job = job
                    updateUi()
                    instructionTextView.text =
                        getString(R.string.job_title,job.employer,job.title)
                            .replace("is looking for a", "מחפשת")
                }
            })
    }

    override fun onStart() {
        super.onStart()
        val shortDescriptionWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                job.subtitle = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {
            }
        }
        shortDescriptionEditText.addTextChangedListener(shortDescriptionWatcher)
        val descriptionWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }
            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                job.description = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {
            }
        }
        descriptionEditText.addTextChangedListener(descriptionWatcher)

        firstPageButton.setOnClickListener {
            val fragment = AddJobFirstPageFragment.newInstance(job.id)
            val transaction: FragmentTransaction =
                activity?.supportFragmentManager!!.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
        secondPageButton.setOnClickListener {
                val fragment = AddJobSecondPageFragment.newInstance(job.id)
                val transaction: FragmentTransaction =
                    activity?.supportFragmentManager!!.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment)
                transaction.commit()
        }
        fourthPageButton.setOnClickListener {
            if (job.subtitle.isEmpty()){
                Toast.makeText(context,"חובה למלא כותרת", Toast.LENGTH_SHORT)
                    .show()
            }
            else {
                val fragment = AddJobFourthPageFragment.newInstance(job.id)
                val transaction: FragmentTransaction =
                    activity?.supportFragmentManager!!.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment)
                transaction.commit()
            }
        }
        fifthPageButton.setOnClickListener {
            if (job.employer.isEmpty()){
                Toast.makeText(context,"חובה למלא את שם החברה", Toast.LENGTH_SHORT)
                    .show()
            }
            else{
                if(!job.location.isEmpty()){
                    val fragment = AddJobFifthPageFragment.newInstance(job.id)
                    val transaction: FragmentTransaction =
                        activity?.supportFragmentManager!!.beginTransaction()
                    transaction.replace(R.id.fragment_container, fragment)
                    transaction.commit()
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.next_adding_job_page, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.next_add_job_page -> {
                if (job.subtitle.isEmpty()){
                    Toast.makeText(context,"חובה למלא כותרת", Toast.LENGTH_SHORT)
                        .show()
                }
                else {
                    val fragment = AddJobFourthPageFragment.newInstance(job.id)
                    val transaction: FragmentTransaction =
                        activity?.supportFragmentManager!!.beginTransaction()
                    transaction.replace(R.id.fragment_container, fragment)
                    transaction.commit()
                }
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()
        jobDetailsViewModel.saveJob(job)
    }

    private fun updateUi(){
        descriptionEditText.setText(job.description)
        shortDescriptionEditText.setText(job.subtitle)
        fourthPageButton.apply {
            if(!job.location.isEmpty()) setImageResource(R.drawable.check_icon)
            else setBackgroundResource(R.color.light_orange)
        }
        fifthPageButton.apply {
            if(!job.phoneNumber.isEmpty() && !job.email.isEmpty()) setImageResource(R.drawable.check_icon)
            else setBackgroundResource(R.color.light_orange)
        }
    }

        companion object{
        fun newInstance(jobId: UUID): AddJobThirdPageFragment {
            val args = Bundle().apply {
                putSerializable(ARG_JOB_ID, jobId)
            }
            return AddJobThirdPageFragment().apply {
                arguments = args
            }
        }
    }
}