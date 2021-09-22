package eg.com.test.vatask.ui.mainActivity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import eg.com.test.vatask.R
import eg.com.test.vatask.databinding.ActivityMainBinding
import eg.com.test.vatask.model.entities.QuestionsModel
import eg.com.test.vatask.service.AlarmReceiver
import eg.com.test.vatask.ui.baseActivity.BaseActivity
import eg.com.test.vatask.ui.fragments.BottomSheetStringsFragment
import eg.com.test.vatask.ui.questionsResultActivity.QuestionsResultActivity
import eg.com.test.vatask.util.*

class MainActivity : BaseActivity(false),

        MainViewModel.Observer {

lateinit var binding: ActivityMainBinding

    override fun doOnCreate(arg0: Bundle?) {

        binding=putContentView(R.layout.activity_main) as ActivityMainBinding
        binding.viewModel=
                ViewModelProvider(
                        this,
                        MainViewModelFactory(application) ).get(MainViewModel::class.java)

        binding.viewModel!!.baseViewModelObserver=baseViewModelObserver
        binding.viewModel!!.observer=this
        binding.lifecycleOwner=this

        initializeViews()
        setListener()
    }

    override fun initializeViews() {
    }

    override fun setListener() {
        binding.viewModel!!.isGetMyLocation.observe(this, Observer {
            if (it && lifecycle.currentState == Lifecycle.State.RESUMED) {
                updateLocationUI()
            }
        })

        binding.viewModel!!.isShowFirstNumberError.observe(this, Observer {
            if (it && lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.edttextFirstNumber.error = getString(R.string.required_field)
            } else {
                binding.edttextFirstNumber.error = null
            }
        })

        binding.viewModel!!.isShowSecondNumberError.observe(this, Observer {
            if (it && lifecycle.currentState == Lifecycle.State.RESUMED) {
                if (binding.viewModel?.secondNumber?.value?.isEmpty()!!)
                    binding.edttextSecondNumber.error = getString(R.string.required_field)
                else if (binding.viewModel?.selectedOperatorPosition == 3)
                    binding.edttextSecondNumber.error = getString(R.string.you_cant_divide_on_zero)
            } else {
                binding.edttextSecondNumber.error = null
            }
        })

        binding.viewModel!!.isShowOperatorError.observe(this, Observer {
            if (it && lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.edttextOperator.error = getString(R.string.select_math_operator)
            } else {
                binding.edttextOperator.error = null
            }
        })

        binding.viewModel!!.isShowDelayTimeError.observe(this, Observer {
            if (it && lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.edttextDelayTime.error = getString(R.string.required_field)
            } else {
                binding.edttextDelayTime.error = null
            }
        })
    }

    override fun selectOperator() {
        val bottomSheetFragment = BottomSheetStringsFragment()
        var bundle = Bundle()

        bundle.putSerializable(
                "list",
                binding.viewModel!!.methodOperationList
        )
        bundle.putSerializable("title", getString(R.string.select_math_operator))
        bottomSheetFragment.arguments = bundle
        bottomSheetFragment.setOnBottomSheetItemClickObserver(object :
                OnBottomSheetItemClickListener {
            override fun onBottomSheetItemClickListener(position: Int) {
                binding.viewModel!!.selectedOperatorPosition = position
                binding.viewModel!!.operator.value =
                        binding.viewModel!!.methodOperationList[position]
                binding.viewModel!!.isShowOperatorError.value = false
            }
        })
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    override fun onShowHideMessageDialog(title: String, message: String, isShow: Boolean) {
        showHideMessageDialog(isShow, title, message)
    }

    override fun setQuestionAlarm(questionModel: QuestionsModel) {
        var intent1 = Intent(this@MainActivity, AlarmReceiver::class.java)
        intent1.action = "Calculate"
        val bundle = Bundle()
        bundle.putString("firstNumber", questionModel.firstNumber)
        bundle.putString("secondNumber", questionModel.secondNumber)
        bundle.putString("operatorText", questionModel.operatorText)
        bundle.putString("delayTime", questionModel.delayTime.toString())
        bundle.putBoolean("isShowLocation", binding.viewModel?.isGetMyLocation?.value!!)
        if (binding.viewModel?.isGetMyLocation?.value!!) {
            bundle.putString("latitude", binding.viewModel?.latitude?.toString() ?: "0")
            bundle.putString("longitude", binding.viewModel?.longitude?.toString() ?: "0")
        }
        intent1.putExtras(bundle)
        var pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                MathUtils.getRandomNumber(),
                intent1,
                PendingIntent.FLAG_ONE_SHOT
        )
        val alarm =
                getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //start service only after given period
        var triggerTime =
                System.currentTimeMillis() + (binding.viewModel!!.delayTime.value?.toInt()!! * 1000)
        alarm.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
        )
        Toast.makeText(
                this@MainActivity,
                getString(R.string.question_added_successfully),
                Toast.LENGTH_LONG
        ).show()
        finish_activity()
    }

    //Gps
    override fun onDestroy() {
        super.onDestroy()
        if (::locationManager.isInitialized)
            locationManager.removeUpdates(locationListener)
    }

    // location retrieved by the Fused Location Provider.
    lateinit var locationManager: LocationManager

    private fun updateLocationUI() {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LocationPermissionRequest
            )
            return
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10.toLong(),
                    0.1.toFloat(), locationListener
            )
        } else {
            askUserTurnGPS()
        }
    }

    var locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (binding.viewModel?.mLastKnownLocation == null) {
                binding.viewModel?.mLastKnownLocation = location
                binding.viewModel!!.latitude = location.latitude
                binding.viewModel!!.longitude = location.longitude
            }
        }

        override fun onProviderDisabled(provider: String) {
            askUserTurnGPS()
        }

        override fun onProviderEnabled(provider: String) {
            updateLocationUI()
        }
    }

    fun askUserTurnGPS() {
        turnGPSOn(this@MainActivity, object : OnAskUserAction {
            override fun onPositiveAction() {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, LocationPermissionRequest)
            }

            override fun onNegativeAction() {
            }
        })
    }
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LocationPermissionRequest -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    updateLocationUI()
            }
            else -> {
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LocationPermissionRequest) {
            updateLocationUI()
        }
    }
}