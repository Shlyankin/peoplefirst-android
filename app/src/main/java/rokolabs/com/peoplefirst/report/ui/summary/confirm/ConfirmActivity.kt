package rokolabs.com.peoplefirst.report.ui.summary.confirm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.report.ui.summary.ReportSummaryActivity
import rokolabs.com.peoplefirst.report.ui.summary.ReportSummaryFragment

class ConfirmActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)
        if (intent.hasExtra("readOnly")) {
            (supportFragmentManager.findFragmentById(R.id.confitmFragment) as ConfirmFragment?)?.showReadOnly(
                true
            )
        }
    }

    companion object {
        //        fun showEdit(context: Context) {
//            val intent = Intent(context, ReportSummaryActivity::class.java)
////            intent.putExtra("readOnly", true)
//            context.startActivity(intent)
//        }
        fun showReadOnly(context: Context) {
            val intent = Intent(context, ConfirmActivity::class.java)
            intent.putExtra("readOnly", true)
            context.startActivity(intent)
        }
    }
}