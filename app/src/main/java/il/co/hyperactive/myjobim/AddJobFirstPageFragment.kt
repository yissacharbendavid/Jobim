package il.co.hyperactive.myjobim

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.util.*

private const val ARG_JOB_ID = "job_id"

class AddJobFirstPageFragment: Fragment() {

    private lateinit var employerNameEditText: EditText
    private lateinit var branchEditText: EditText
    private lateinit var job: Job
    private lateinit var secondPageButton: ImageButton
    private lateinit var thirdPageButton: ImageButton
    private lateinit var fourthPageButton: ImageButton
    private lateinit var fifthPageButton: ImageButton

    private val jobDetailsViewModel: JobDetailsViewModel by lazy {
        ViewModelProviders.of(this).get(JobDetailsViewModel::class.java)
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
        val view = inflater.inflate(R.layout.add_job_first_page,container,false)

        employerNameEditText = view.findViewById(R.id.employer_name) as EditText
        branchEditText = view.findViewById(R.id.branch) as EditText
        secondPageButton = view.findViewById(R.id.add_job_page_2)
        thirdPageButton = view.findViewById(R.id.add_job_page_3)
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
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.next_adding_job_page, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.next_add_job_page -> {
                if (job.employer.isEmpty()){
                    Toast.makeText(context,"חובה למלא את שם החברה", Toast.LENGTH_SHORT)
                        .show()
                }
                else {
                    val fragment = AddJobSecondPageFragment.newInstance(job.id)
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

    override fun onStart() {
        super.onStart()
        val employerWatcher = object : TextWatcher {
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
                job.employer = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {
            }
        }
        employerNameEditText.addTextChangedListener(employerWatcher)
        val branchWatcher = object : TextWatcher {
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
                job.branch = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {
            }
        }
        branchEditText.addTextChangedListener(branchWatcher)

        secondPageButton.apply {
            setOnClickListener {
                if (job.employer.isEmpty()) {
                    Toast.makeText(context, "חובה למלא את שם החברה", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val fragment = AddJobSecondPageFragment.newInstance(job.id)
                    val transaction: FragmentTransaction =
                        activity?.supportFragmentManager!!.beginTransaction()
                    transaction.replace(R.id.fragment_container, fragment)
                    transaction.commit()
                }
            }
        }

        thirdPageButton.apply {
            setOnClickListener {
                if (job.employer.isEmpty()) {
                    Toast.makeText(context, "חובה למלא את שם החברה", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (!job.title.isEmpty()) {
                        val fragment = AddJobThirdPageFragment.newInstance(job.id)
                        val transaction: FragmentTransaction =
                            activity?.supportFragmentManager!!.beginTransaction()
                        transaction.replace(R.id.fragment_container, fragment)
                        transaction.commit()
                    }
                }
            }
        }
            fourthPageButton.apply {
                setOnClickListener {
                    if (job.employer.isEmpty()) {
                        Toast.makeText(context, "חובה למלא את שם החברה", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        if (!job.subtitle.isEmpty()) {
                            val fragment = AddJobFourthPageFragment.newInstance(job.id)
                            val transaction: FragmentTransaction =
                                activity?.supportFragmentManager!!.beginTransaction()
                            transaction.replace(R.id.fragment_container, fragment)
                            transaction.commit()
                        }
                    }
                }
            }
                fifthPageButton.apply {
                    setOnClickListener {
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
    }

    override fun onStop() {
        super.onStop()
        jobDetailsViewModel.saveJob(job)
    }

    private fun updateUi(){
        employerNameEditText.setText(job.employer)
        branchEditText.setText(job.branch)
        secondPageButton.apply {
            if(!job.title.isEmpty()) setImageResource(R.drawable.check_icon)
            else setBackgroundResource(R.color.light_orange)
        }
        thirdPageButton.apply {
            if(!job.subtitle.isEmpty()) setImageResource(R.drawable.check_icon)
            else setBackgroundResource(R.color.light_orange)
        }
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
        fun newInstance(jobId: UUID): AddJobFirstPageFragment {
            val args = Bundle().apply {
                putSerializable(ARG_JOB_ID, jobId)
            }
            return AddJobFirstPageFragment().apply {
                arguments = args
            }
        }
    }
}