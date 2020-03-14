package rokolabs.com.peoplefirst.report.ui.summary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import rokolabs.com.peoplefirst.R

class ReportSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_summary)
        if (intent.hasExtra("readOnly")) {
            (supportFragmentManager.findFragmentById(R.id.reportSummary) as ReportSummaryFragment?)?.showReadOnly(
                true
            )
        }
    }

    companion object {
        fun showReadOnly(context: Context) {
            val intent = Intent(context, ReportSummaryActivity::class.java)
            intent.putExtra("readOnly", true)
            context.startActivity(intent)
        }
    }
}