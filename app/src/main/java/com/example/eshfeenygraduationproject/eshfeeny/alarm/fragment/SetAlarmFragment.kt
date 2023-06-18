package com.example.eshfeenygraduationproject.eshfeeny.alarm.fragment

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.eshfeenygraduationproject.R
import com.example.eshfeenygraduationproject.databinding.FragmentSetAlarmBinding
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText

class SetAlarmFragment : Fragment() {

    // Creating the binding variable to access the views in the SetAlarm Fragment
    // and setting it's intial value to null
    private var binding: FragmentSetAlarmBinding? = null
    private var repetitionState = "onlyToday"
    private var alarmTime: MutableList<Long> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // initializing the value of the binding variable
        binding = FragmentSetAlarmBinding.inflate(inflater)

        // variable to get he number of bills per time
        var repetitionNumber = 1

        // setting a navigation to the backArrow image view to go back to the calender fragment

        // setting the hint attribute to both of medcienNameInput view and DescriptionInput view
        binding?.DescriptionInput?.hint = getString(R.string.notes)
        binding?.medcienNameInput?.hint = getString(R.string.medicenName)

        // calling the setHint function to remove the and set the hint depending on the state of view
        binding?.DescriptionInput?.let {
            setHint(it, R.string.notes)
        }
        binding?.medcienNameInput?.let {
            setHint(it, R.string.medicenName)
        }

        // showing the bottom sheet to set the alarm when press on the chip
        binding?.newAlarmChip?.setOnClickListener {
            showTimePicker()
        }

        binding?.RepetitionStateText?.setOnClickListener {
            val bottomSheet = SelectDaysFragment()
            bottomSheet.show(childFragmentManager, "SelectDaysFragment")
        }

        // increasing and decreasing the number of bills that will be taken in a time depending on
        // which button is pressed
        binding?.plusIcon?.setOnClickListener {
            repetitionNumber += 1
            binding?.repetitionNumber?.text = repetitionNumber.toString()
        }
        // in case of decreasing the minimum amount of bills is 1
        binding?.minusIcon?.setOnClickListener {
            if (repetitionNumber > 1) {
                repetitionNumber -= 1
                binding?.repetitionNumber?.text = repetitionNumber.toString()
            }
        }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        // setting the binding variable to null to ensure there is no memory leak
        binding = null
    }

    // a function to show the timePicker bottom sheet
    private fun showTimePicker() {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(childFragmentManager, "TimePickerFragment")
    }

    // a function that set that pass the time to the chip while creating it
    @RequiresApi(Build.VERSION_CODES.M)
    fun onTimeSelected(timeString: String, timeMillis: Long) {
        alarmTime.add(timeMillis)
        Log.d("alarm", alarmTime.toString())
        val newChip = createChip(timeString)
        // adding the chip to the chipGroup
        binding?.alarmChipsGroup?.addView(newChip)
    }

    // a function that create the chips when the time is selected
    private fun createChip(time: String): Chip {
        // creating a chip variable and setting the text of the chip to the time
        val chip = Chip(context)
        chip.text = time
        chip.chipCornerRadius = 50f
        // making the chip direction in Left to right mode
        chip.layoutDirection = View.LAYOUT_DIRECTION_LTR
        // setting the chip stroke width to 1dp
        chip.chipStrokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            1f,
            context?.resources?.displayMetrics
        )
        // changing the chip background color
        chip.chipBackgroundColor =
            context?.let {
                ContextCompat.getColor(it, R.color.white)
            }?.let {
                ColorStateList.valueOf(it)
            }
        // changing the chip stroke color
        chip.chipStrokeColor =
            context?.let {
                ContextCompat.getColor(it, R.color.blue_main)
            }?.let {
                ColorStateList.valueOf(it)
            }
        // making the close icon visible
        chip.isCloseIconVisible = true
        // changing the close icon color
        chip.closeIconTint =
            context?.let {
                ContextCompat.getColor(it, R.color.blue_main)
            }?.let {
                ColorStateList.valueOf(it)
            }
        // setting a click listener on the close icon to remove the chip from the group
        chip.setOnCloseIconClickListener {
            binding?.alarmChipsGroup?.removeView(it)
        }
        return chip
    }

    fun alarmRepetition(reputationState: String) {
        binding?.RepetitionStateText?.text = reputationState
        repetitionState = reputationState
    }

    // a function to set the hint to the edit text
    private fun setHint(view: TextInputEditText, string: Int) {
        view.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                view.hint = ""
                (v as TextInputEditText).gravity = Gravity.RIGHT
            } else {
                if (view.text?.isEmpty() == true) {
                    view.hint = getString(string)
                    (v as TextInputEditText).gravity = Gravity.RIGHT
                }
            }
        }
    }
}