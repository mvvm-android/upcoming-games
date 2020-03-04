package com.gavinsappcreations.upcominggames.ui.sort

import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gavinsappcreations.upcominggames.App
import com.gavinsappcreations.upcominggames.databinding.FragmentSortBinding
import com.gavinsappcreations.upcominggames.ui.sort.platform.PlatformDialogFragment
import com.gavinsappcreations.upcominggames.utilities.ReleaseDateType
import com.gavinsappcreations.upcominggames.utilities.hideKeyboard


class SortFragment : Fragment() {

    private val viewModel: SortViewModel by lazy {
        ViewModelProvider(this, SortViewModel.Factory(requireActivity().application)).get(
            SortViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSortBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the SortViewModel
        binding.viewModel = viewModel

        binding.upNavigationImageButton.setOnClickListener {
            hideKeyboard(requireActivity())
            // TODO: this should be done in ViewModel
            findNavController().popBackStack()
        }

        binding.applyButton.setOnClickListener {
            hideKeyboard(requireActivity())

            val startNull = binding.startDateTextInputEditText.error
            val endNull = binding.endDateTextInputEditText.error

            // TODO: this should be done in ViewModel, except vibrating
            // When the APPLY button is pressed, save the new ones to SharedPrefs.
            if (viewModel.unsavedSortOptions.value!!.releaseDateType != ReleaseDateType.CustomDate
                || (binding.startDateTextInputEditText.error == null
                        && binding.endDateTextInputEditText.error == null)
            ) {
                viewModel.updateSortOptions()
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    App.applicationContext,
                    "Before proceeding, you must enter valid dates in the \"Release date\" section.",
                    Toast.LENGTH_LONG
                ).show()

                val vibrator =
                    requireActivity().getSystemService(VIBRATOR_SERVICE) as Vibrator?
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator?.vibrate(
                        VibrationEffect.createOneShot(
                            200,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator?.vibrate(200)
                }
            }
        }

        DateInputTextWatcher(binding.startDateTextInputEditText).listen()
        DateInputTextWatcher(binding.endDateTextInputEditText).listen()

        return binding.root
    }


    fun showPlatformDialog() {
        val ft: FragmentTransaction = childFragmentManager.beginTransaction()
        val prev: Fragment? = childFragmentManager.findFragmentByTag("dialog")
        // Remove any previous instances of this dialog.
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        // Create and show the dialog.
        val newFragment: DialogFragment = PlatformDialogFragment()
        newFragment.show(ft, "dialog")
    }


}



