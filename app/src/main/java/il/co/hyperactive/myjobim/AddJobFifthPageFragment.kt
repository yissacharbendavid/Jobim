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
import java.util.*import androidx.lifecycle.Observer

private const val ARG_JOB_ID = "job_id"

class AddJobFifthPageFragment: Fragment() {
    private lateinit var job: Job
    private lateinit var phoneEditText: EditText
    private lateinit var mailEditText: EditText
    private lateinit var goodForTeensCheckBox: CheckBox
    private lateinit var addImageImageView: ImageView
    private lateinit var addQuestionTextView: TextView
    private lateinit var firstPageButton: ImageButton
    private lateinit var secondPageButton: ImageButton
    private lateinit var thirdPageButton: ImageButton
    private lateinit var fourthPageButton: ImageButton

    private var isMenuItemPressed = false

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
                if(!isMenuItemPressed) jobDetailsViewModel.deleteJob(job.id)
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
        val view = inflater.inflate(R.layout.add_job_fifth_page,container,false)
        phoneEditText = view.findViewById(R.id.job_phone) as EditText
        mailEditText = view.findViewById(R.id.job_mail) as EditText
        addImageImageView = view.findViewById(R.id.job_image) as ImageView
        goodForTeensCheckBox = view.findViewById(R.id.is_good_for_teen) as CheckBox
        addQuestionTextView = view.findViewById(R.id.add_question) as TextView
        firstPageButton = view.findViewById(R.id.add_job_page_1)
        secondPageButton = view.findViewById(R.id.add_job_page_2)
        thirdPageButton = view.findViewById(R.id.add_job_page_3)
        fourthPageButton = view.findViewById(R.id.add_job_page_4)
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

    override fun onStart() {
        super.onStart()
        val phoneWatcher = object : TextWatcher {
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
                job.phoneNumber = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
            }
        }
        phoneEditText.addTextChangedListener(phoneWatcher)
        val mailWatcher = object : TextWatcher {
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
                job.email = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
            }
        }
        mailEditText.addTextChangedListener(mailWatcher)

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
        thirdPageButton.setOnClickListener {
            val fragment = AddJobThirdPageFragment.newInstance(job.id)
            val transaction: FragmentTransaction =
                activity?.supportFragmentManager!!.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()

        }
        fourthPageButton.setOnClickListener {
                val fragment = AddJobFourthPageFragment.newInstance(job.id)
                val transaction: FragmentTransaction =
                    activity?.supportFragmentManager!!.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment)
                transaction.commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.save_new_job, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_new_job -> {
                isMenuItemPressed = true
                setHasOptionsMenu(false)
                activity?.onBackPressed()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()
        jobDetailsViewModel.saveJob(job)
    }

    private fun updateUi() {
        phoneEditText.setText(job.phoneNumber)
        mailEditText.setText(job.email)
    }

    companion object{
        fun newInstance(jobId: UUID): AddJobFifthPageFragment {
            val args = Bundle().apply {
                putSerializable(ARG_JOB_ID, jobId)
            }
            return AddJobFifthPageFragment().apply {
                arguments = args
            }
        }
    }
}