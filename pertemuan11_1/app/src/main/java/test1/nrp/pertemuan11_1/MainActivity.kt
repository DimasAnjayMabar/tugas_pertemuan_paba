package test1.nrp.pertemuan11_1

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    var data = mutableListOf<String>()

    private fun showUpdateDialog(
        position: Int,
        oldValue: String,
        data: MutableList<String>,
        adapter: ArrayAdapter<String>
    ){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Data")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val tvOld = TextView(this)
        tvOld.text = "Old Data: $oldValue"
        tvOld.textSize = 16f

        val etNew = EditText(this)
        etNew.hint = "Enter new data"
        etNew.setText(oldValue)

        layout.addView(tvOld)
        layout.addView(etNew)

        builder.setView(layout)

        builder.setPositiveButton("Save"){dialog, _ ->
            val newValue = etNew.text.toString().trim()
            if(newValue.isNotEmpty()){
                data[position] = newValue
                adapter.notifyDataSetChanged()
                Toast.makeText(
                    this,
                    "Data updated to $newValue",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(
                    this,
                    "Field cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel"){dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun showActionDialog(
        position: Int,
        selectedItem: String,
        data: MutableList<String>,
        adapter: ArrayAdapter<String>
    ){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Item $selectedItem")
        builder.setMessage("What do you want to do with this data?")

        builder.setPositiveButton("Update"){_, _ ->
            showUpdateDialog(position, selectedItem, data, adapter)
        }
        builder.setNegativeButton("Delete"){_, _ ->
            data.removeAt(position)
            adapter.notifyDataSetChanged()
            Toast.makeText(
                this,
                "$selectedItem deleted",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNeutralButton("Cancel") {dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        data.addAll(listOf("1", "2", "3", "4", "5"))

        val lvAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            data
        )

        val lv1 = findViewById<ListView>(R.id.list_view_1)
        lv1.adapter = lvAdapter

        val addBtn = findViewById<Button>(R.id.add_button)
        addBtn.setOnClickListener {
            var lastData = Integer.parseInt(data.get(data.size - 1)) + 1
            data.add("$lastData")
            lvAdapter.notifyDataSetChanged()
        }

        lv1.setOnItemClickListener{ parent, view, position, id ->
            Toast.makeText(
                this,
                data[position],
                Toast.LENGTH_SHORT
            ).show()
        }

        val gestureDetector = GestureDetector(
            this,
            object : GestureDetector.SimpleOnGestureListener(){
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    val position = lv1.pointToPosition(e.x.toInt(), e.y.toInt())
                    if(position != ListView.INVALID_POSITION){
                        val selectedItem = data[position]
                        showActionDialog(position, selectedItem, data, lvAdapter)
                    }
                    return true
                }
            }
        )

        lv1.setOnTouchListener {_, event ->
            gestureDetector.onTouchEvent(event)
        }


    }
}