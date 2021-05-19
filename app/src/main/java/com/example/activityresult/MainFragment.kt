package com.example.activityresult

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.activityresult.databinding.FragmentMainBinding

const val REQ_CODE = 101
const val DATA = "data"

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(result)
        }

    private val customLauncher =
        registerForActivityResult(object : ActivityResultContract<Intent, String>() {
            override fun createIntent(context: Context, input: Intent) = input

            override fun parseResult(resultCode: Int, intent: Intent?): String {
                if (resultCode == Activity.RESULT_OK && intent != null)
                    return intent.getStringExtra(DATA) ?: ""
                return ""
            }
        }) { result -> setTextValue(result) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
            binding.clickListener = this.clickListener
        }
        return binding.root
    }

    private val clickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.buttonOld -> startActivityForResult(
                getIntent(R.string.startActivityForResult), REQ_CODE
            )

            R.id.buttonNew -> customLauncher.launch(getIntent(R.string.resultForCallback))
        }
    }

    private fun getIntent(@StringRes stringId: Int) =
        Intent(activity, ResultActivity::class.java).apply {
            putExtra(DATA, "Via ${getString(stringId)}")
        }

    private fun onActivityResult(result: ActivityResult) {
        result.apply {
            if (resultCode == Activity.RESULT_OK) {
                setTextValue(data)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            setTextValue(data)
        }
    }

    private fun setTextValue(data: Intent?) {
        data?.apply {
            binding.text = getStringExtra(DATA)
        }
    }

    private fun setTextValue(text: String) {
        binding.text = text
    }
}


/** Default ActivityContracts
StartActivityForResult()
RequestMultiplePermissions()
RequestPermission()
TakePicturePreview()
TakePicture()
TakeVideo()
PickContact()
CreateDocument()
OpenDocumentTree()
OpenMultipleDocuments()
OpenDocument()
GetMultipleContents()
GetContent()**/


/*
onActivitiyResult was called before onStart, now we get called after / at the end of onStart()

When starting an activity for a result, it is possible (and, in cases of memory-intensive operations such as camera usage, almost certain)
that your process and your activity will be destroyed due to low memory.

For this reason, the Activity Result APIs decouple the result callback from the place in your code where you launch the other activity.
As the result callback needs to be available when your process and activity are recreated,
the callback must be unconditionally registered every time your activity is created,
even if the logic of launching the other activity only happens based on user input or other business logic.*/
