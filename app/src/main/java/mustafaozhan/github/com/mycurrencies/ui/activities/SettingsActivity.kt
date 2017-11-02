package mustafaozhan.github.com.mycurrencies.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.row_settings.view.*

import mustafaozhan.github.com.mycurrencies.R
import mustafaozhan.github.com.mycurrencies.model.data.Setting
import mustafaozhan.github.com.mycurrencies.ui.adapters.SettingsAdapter
import ninja.sakib.pultusorm.callbacks.Callback
import ninja.sakib.pultusorm.core.*
import ninja.sakib.pultusorm.exceptions.PultusORMException
import org.jetbrains.anko.doAsync

class SettingsActivity : AppCompatActivity() {
    val settingsList = ArrayList<Setting>()
    val mAdapter = SettingsAdapter(settingsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val myDatabase = PultusORM("myDatabase.db", applicationContext.filesDir.absolutePath)



        getItems()

        selectAll.setOnClickListener {
            doAsync {
                val updater: PultusORMUpdater = PultusORMUpdater.Builder()
                        .set("isActive", "true")
                        .build()

                class ResponseCallback : Callback {
                    override fun onSuccess(type: PultusORMQuery.Type) {
                        log(type.name, "Success")
                    }

                    override fun onFailure(type: PultusORMQuery.Type, exception: PultusORMException) {
                        log(type.name, "Failure")
                        exception.printStackTrace()
                    }
                }
                myDatabase.update(Setting(), updater, ResponseCallback())

                runOnUiThread {
                    getItems()
                }
            }
        }
        deSelectAll.setOnClickListener {
            doAsync {
                val updater: PultusORMUpdater = PultusORMUpdater.Builder()
                        .set("isActive", "false")
                        .build()

                class ResponseCallback : Callback {
                    override fun onSuccess(type: PultusORMQuery.Type) {
                        log(type.name, "Success")
                    }

                    override fun onFailure(type: PultusORMQuery.Type, exception: PultusORMException) {
                        log(type.name, "Failure")
                        exception.printStackTrace()
                    }
                }
                myDatabase.update(Setting(), updater, ResponseCallback())

                runOnUiThread {
                    getItems()
                }
            }
        }

    }

    private fun getItems() {
        val myDatabase = PultusORM("myDatabase.db", applicationContext.filesDir.absolutePath)
        settingsList.clear()
        val items = myDatabase.find(Setting())
        items.mapTo(settingsList) { it -> it as Setting }
        val mLayoutManager = LinearLayoutManager(applicationContext)
        mRecViewSettings.layoutManager = mLayoutManager
        mRecViewSettings.itemAnimator = DefaultItemAnimator()
        mRecViewSettings.adapter = mAdapter

        mAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
