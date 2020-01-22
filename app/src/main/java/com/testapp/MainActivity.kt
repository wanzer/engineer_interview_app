package com.testapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.devtides.coroutinesretrofit.view.EmployeesListAdapter
import com.testapp.model.Employee
import kotlinx.android.synthetic.main.activity_main.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File

class MainActivity : AppCompatActivity(), View.OnClickListener,
    AddEmployeeDialogFragment.AddEmployeersDialogCallback, EmployeesListAdapter.AttachListener {

    private var employeesListAdapter = EmployeesListAdapter(arrayListOf())
    val REQUEST_PERMISSION = 200
    var photoList: List<File>? = null
    var photo: File? = null
    var currentEmployeePosition: Int? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val employeeList: ArrayList<Employee> = ArrayList()

        //test employees data
        employeeList.add(Employee("John", "doe", null))
        employeeList.add(Employee("Ben", "Silver", null))
        employeeList.add(Employee("Roby", "White", null))

        recycler_container.layoutManager = LinearLayoutManager(this)
        recycler_container.adapter = employeesListAdapter
        employeesListAdapter.addAttachListener(this)

        employeesListAdapter.updateEmployees(employeeList)

        add_employee.setOnClickListener(this)

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION
            )
        }

        EasyImage.configuration(this)
            .setImagesFolderName(getString(R.string.txt_photo_storage))
            .setCopyTakenPhotosToPublicGalleryAppFolder(true)
            .setCopyPickedImagesToPublicGalleryAppFolder(true)
            .setAllowMultiplePickInGallery(true)

        checkGalleryAppAvailability()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun sendEmployeeData(firstName: String?, secondName: String?) {
        addEmployees(firstName, secondName)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_employee -> showEployeeDialogFragment()
        }
    }

    override fun attachFile(position: Int?) {
        currentEmployeePosition = position
        EasyImage.openChooserWithGallery(this, getString(R.string.txt_choose_sourse), 0)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.txt_thanks_for_permisson), Toast.LENGTH_SHORT).show()
            } else {
                onBackPressed()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
                override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                    //Some error handling
                    e!!.printStackTrace()
                }
                override fun onImagesPicked(imageFiles: List<File>, source: EasyImage.ImageSource, type: Int) {
                    if (imageFiles.isNotEmpty()) {
                        photoList = imageFiles
                        photo = imageFiles[0]
                        if (photo != null && currentEmployeePosition != null) {
                            employeesListAdapter.attachFile(currentEmployeePosition!!, photo!!)
                            currentEmployeePosition = null
                        }
                    }
                }

                override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                }
            })
    }

    private fun showEployeeDialogFragment() {
        val employeeDialogFragment = AddEmployeeDialogFragment().newInstance(null)
        employeeDialogFragment.show(supportFragmentManager, "add_employee_dialog")
        employeeDialogFragment.employeeDialogCallback(this)
    }

    private fun addEmployees(firstName: String?, secondName: String?) {
        val newComerEmployee = Employee(firstName, secondName, null)
        employeesListAdapter.addNewEmployee(newComerEmployee)
    }

    private fun checkGalleryAppAvailability() {
        if (this.let { EasyImage.canDeviceHandleGallery(it) }) {
            //Device has no app that handles gallery intent
        }
    }
}
