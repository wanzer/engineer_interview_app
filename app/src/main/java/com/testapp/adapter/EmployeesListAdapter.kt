package com.devtides.coroutinesretrofit.view

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.testapp.R
import com.testapp.model.Employee
import kotlinx.android.synthetic.main.item_employee.view.*
import java.io.File

class EmployeesListAdapter(var employees: ArrayList<Employee>) :
    RecyclerView.Adapter<EmployeesListAdapter.CountryViewHolder>() {

    lateinit var attacheListener: AttachListener

    fun updateEmployees(newEmployees: List<Employee>) {
        employees.clear()
        employees.addAll(newEmployees)
        notifyDataSetChanged()
    }

    fun addNewEmployee(newComerEmployee: Employee) {
        employees.add(newComerEmployee)
        notifyDataSetChanged()
    }

    fun attachFile(position: Int, file: File){
        employees.get(position).employeePhoto = file
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = CountryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_employee, parent, false),
        attacheListener
    )

    override fun getItemCount() = employees.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(employees[position], position)
    }

    class CountryViewHolder(view: View, var attacheListener: AttachListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private val firstName = view.employee_fist_name_tv
        private val secondName = view.employee_second_name_tv
        private val photoPreview = view.photo_preview
        private val attachFile = view.attach_file
        private var position: Int? = null

        fun bind(employee: Employee, position: Int) {
            this.position = position
            firstName.text = employee.firstName
            secondName.text = employee.secondName
            if (employee.employeePhoto != null) {
                Picasso.get().load(employee.employeePhoto!!).config(Bitmap.Config.RGB_565)
                    .fit().centerCrop().into(photoPreview)
            }
            attachFile.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.attach_file -> attacheListener.attachFile(position)
            }
        }
    }

    fun addAttachListener(attacheListener: AttachListener) {
        this.attacheListener = attacheListener
    }

    interface AttachListener {
        fun attachFile(position: Int?)
    }
}