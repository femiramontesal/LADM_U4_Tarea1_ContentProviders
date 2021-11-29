package mx.tecnm.tepic.ladm_u4_tarea1_video

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CallLog
import android.telecom.Call
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCalendario.setOnClickListener{

            listarCalendario()

        }

        btnLlamadas.setOnClickListener {

            listarLlamadas()

        }

    }

    private fun listarCalendario() {

        //Si no se tienen los permisos,-> se piden
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALENDAR)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALENDAR),1)
        }else{

            var calendarios = ArrayList<String>()
            var projection = arrayOf(CalendarContract.Calendars.NAME)
            var cursor:Cursor?=null


            try {

                cursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI,projection,null,null,null)

                while (cursor?.moveToNext()!!){
                    calendarios.add(cursor.getString(0))
                }

            }catch (e: Exception){

                Toast.makeText(this,"Algo salió mal", Toast.LENGTH_LONG).show()

            }finally {

                listaCalendario.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,calendarios)
                cursor?.close()

            }

        }

    }

    @SuppressLint("Range")
    private fun listarLlamadas() {

        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG),1)
        }else{

            var llamadas = ArrayList<String>()
            var selection:String = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE
            var cursor:Cursor?=null

            try {

                cursor = contentResolver.query(Uri.parse("content://call_log/calls"),null,selection,null,null)

                var registro = ""

                while (cursor?.moveToNext()!!){

                    val numero:String = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                    var tipo:String = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))

                    if (tipo=="3"){
                        tipo = "Llamada perdida"
                    }

                    registro = "\nNumero: " + numero + "\nTipo: " + tipo + "\n"

                    llamadas.add(registro)

                }


            }catch (e:Exception){

                Toast.makeText(this,"Algo salió mal", Toast.LENGTH_LONG).show()

            }finally {

                listaLlamadas.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,llamadas)
                cursor?.close()

            }

        }

    }
}


