package il.co.hyperactive.myjobim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.FragmentManager
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
    JobListFragment.Callbacks,
    MenuFragment.Callbacks,
    MyJobsFragment.Callbacks  {

    private lateinit var menu: ImageButton
    private lateinit var locationButton: ImageButton
    private lateinit var toolbarTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.tool_bar)
        toolbarTextView = findViewById(R.id.toolBar_text)

        menu = findViewById(R.id.menu)
        locationButton = findViewById(R.id.location_button)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragment = JobListFragment()
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.up_slide_out,
                    R.anim.fade_in,
                    R.anim.up_slide_out
                )
                .add(R.id.fragment_container, fragment)
                .commit()
        }

        menu.setOnClickListener{
            val fragment = MenuFragment()
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.side_slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.side_slide_out
                )
                .add(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        locationButton.setOnClickListener{
            val fragment = MapFragment()
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.side_slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.side_slide_out
                )
                .add(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onListStart() {
        menu.visibility = VISIBLE
        locationButton.visibility = VISIBLE
        toolbarTextView.setText(R.string.app_name)
    }
    override fun onJobSelected(jobId: UUID) {
        Log.d(TAG, "MainActivity.onJobSelected: $jobId")
        val fragment = JobFragment.newInstance(jobId)
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.up_slide_in,
                R.anim.side_slide_out,
                R.anim.fade_in,
                R.anim.side_slide_out
            )
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onAddJobSelected(jobId: UUID) {
        toolbarTextView.setText(R.string.new_job)
        menu.visibility = INVISIBLE
        locationButton.visibility = INVISIBLE
        val fragment = AddJobFirstPageFragment.newInstance(jobId)
        onBackPressed()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onMyJobsSelected() {
        toolbarTextView.setText(R.string.my_jobs)
        menu.visibility = VISIBLE
        locationButton.visibility = INVISIBLE
        val fragment = MyJobsFragment()
        onBackPressed()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onFindJobsSelected() {
        val fragment = JobListFragment()
        onBackPressed()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun onUserDetailsSelected() {
        toolbarTextView.setText(R.string.details)
        menu.visibility = VISIBLE
        locationButton.visibility = INVISIBLE
        val fragment = UserDetailsFragment()
        onBackPressed()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}