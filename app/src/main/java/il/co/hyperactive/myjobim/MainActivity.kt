package il.co.hyperactive.myjobim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageButton
import androidx.appcompat.app.ActionBar
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), JobListFragment.Callbacks, MenuFragment.Callbacks {
    private lateinit var menu: ImageButton
    private lateinit var location_buttom: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.tool_bar)

        menu = findViewById(R.id.menu)
        location_buttom = findViewById(R.id.location_button)

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
        location_buttom.setOnClickListener{
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
        location_buttom.visibility = VISIBLE
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
        menu.visibility = INVISIBLE
        location_buttom.visibility = INVISIBLE
        val fragment = AddJobFirstPageFragment.newInstance(jobId)
        onBackPressed()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}