package mustafaozhan.github.com.mycurrencies.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_settings.*

import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.model.data.Setting
import mustafaozhan.github.com.mycurrencies.ui.adapters.SettingsAdapter
import ninja.sakib.pultusorm.core.PultusORM

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        get()




    }

    private fun get() {

        val settingsList = ArrayList<Setting>()
        val mAdapter = SettingsAdapter(settingsList)


        val myDatabase = PultusORM("myDatabase.db", applicationContext.filesDir.absolutePath)
        val items = myDatabase.find(Setting())

        items.mapTo(settingsList) { it -> it as Setting }
        //myDatabase.close()
        val mLayoutManager = LinearLayoutManager(applicationContext)
        mRecViewSettings.layoutManager = mLayoutManager
        mRecViewSettings.itemAnimator = DefaultItemAnimator() as RecyclerView.ItemAnimator?
        mRecViewSettings.adapter = mAdapter

        mAdapter.notifyDataSetChanged()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
