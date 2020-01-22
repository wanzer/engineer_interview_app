package com.testapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_dialog.*

class AddEmployeeDialogFragment : DialogFragment(), View.OnClickListener {

    private val TAG = AddEmployeeDialogFragment::class.java.simpleName
    private var employeeDialogCallback: AddEmployeersDialogCallback? = null
    private var employeeFirstName: String? = null
    private var employeeSecondName: String? = null

    fun newInstance(bundle: Bundle?): AddEmployeeDialogFragment {
        val fragment = AddEmployeeDialogFragment()
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), theme)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_dialog, container, false)
        v.findViewById<Button>(R.id.send_data).setOnClickListener(this)
        return v
    }

    fun employeeDialogCallback(employeeDialogCallback: AddEmployeersDialogCallback) {
        this.employeeDialogCallback = employeeDialogCallback
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.send_data -> {
                employeeFirstName = employee_first_name_et.text.toString()
                employeeSecondName = employee_second_name_et.text.toString()
                if (!employeeFirstName.isNullOrEmpty() && !employeeSecondName.isNullOrEmpty()) {
                    employeeDialogCallback?.sendEmployeeData(
                        employee_first_name_et.text.toString(),
                        employee_second_name_et.text.toString()
                    ); dismiss()
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.txt_fill_in_information),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    interface AddEmployeersDialogCallback {
        fun sendEmployeeData(firstName: String?, secondName: String?)
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.width_create_snooze_dialog)
        dialog?.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}