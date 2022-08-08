package il.co.hyperactive.myjobim

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.util.*


private const val ARG_JOB_ID = "job_id"
private const val TAG = "AddJobSecondPage"

class AddJobSecondPageFragment: Fragment() {

    private lateinit var jobTitleRadioGroup: RadioGroup
    private lateinit var job: Job
    private lateinit var firstPageButton: ImageButton
    private lateinit var thirdPageButton: ImageButton
    private lateinit var fourthPageButton: ImageButton
    private lateinit var fifthPageButton: ImageButton

    private var isRadioPressed = false
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
        val view = inflater.inflate(R.layout.add_job_second_page,container,false)
        jobTitleRadioGroup = view.findViewById(R.id.title_radio_group)
        firstPageButton = view.findViewById(R.id.add_job_page_1)
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

    override fun onStart() {
        super.onStart()
        jobTitleRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val isWasEmpty = job.title.isEmpty()
            val checkedView = view?.findViewById(checkedId) as RadioButton
            job.title = checkedView.text.toString()

            if(isWasEmpty || isRadioPressed) {
                val fragment = AddJobThirdPageFragment.newInstance(job.id)
                val transaction: FragmentTransaction =
                    activity?.supportFragmentManager!!.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment)
                transaction.commit()
            } else {
                isRadioPressed = true
            }
        }

        firstPageButton.setOnClickListener {
                val fragment = AddJobFirstPageFragment.newInstance(job.id)
                val transaction: FragmentTransaction =
                    activity?.supportFragmentManager!!.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment)
                transaction.commit()
        }
        thirdPageButton.setOnClickListener {
            if (job.title.isEmpty()){
                Toast.makeText(context,"חובה לבחור מקצוע", Toast.LENGTH_SHORT)
                    .show()
            }
            else{
                val fragment = AddJobThirdPageFragment.newInstance(job.id)
                val transaction: FragmentTransaction =
                    activity?.supportFragmentManager!!.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment)
                transaction.commit()
            }
        }
        fourthPageButton.setOnClickListener {
            if (job.employer.isEmpty()){
                Toast.makeText(context,"חובה למלא את שם החברה", Toast.LENGTH_SHORT)
                    .show()
            }
            else{
                if(!job.subtitle.isEmpty()){
                    val fragment = AddJobFourthPageFragment.newInstance(job.id)
                    val transaction: FragmentTransaction =
                        activity?.supportFragmentManager!!.beginTransaction()
                    transaction.replace(R.id.fragment_container, fragment)
                    transaction.commit()
                }
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
                if (job.title.isEmpty()){
                    Toast.makeText(context,"חובה לבחור מקצוע", Toast.LENGTH_SHORT)
                        .show()
                }
                else {
                    val fragment = AddJobThirdPageFragment.newInstance(job.id)
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

        if(!job.title.isEmpty()){
            val checkedRadioButton: RadioButton? = when(job.title){
                "קופאי/ת" -> view?.findViewById(R.id.cashier_radio_button)
                "נציג/ת שירות" -> view?.findViewById(R.id.support_agent_radio_button)
                "טבח/ית" -> view?.findViewById(R.id.cook_radio_button)
                else -> return
            }
            checkedRadioButton?.apply {
                isChecked = true
                jumpDrawablesToCurrentState()
            }
        }
    }

    companion object{
        fun newInstance(jobId: UUID): AddJobSecondPageFragment {
            val args = Bundle().apply {
                putSerializable(ARG_JOB_ID, jobId)
            }
            return AddJobSecondPageFragment().apply {
                arguments = args
            }
        }
    }
}
