import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProviders
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.AstroDatabase
import com.udacity.asteroidradar.main.MainFragment
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.main.MainViewModelFactory
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {


    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }


    val appCont = appContext

    override suspend fun doWork(): Result {
        val application = appCont.applicationContext as Application
        val datasource =  AstroDatabase.getInstance(application).AstroDatabaseDAO
        val viewModelFactory = MainViewModelFactory(datasource,application,appCont)
        var viewModel = ViewModelProviders.of(MainFragment(),viewModelFactory).get(MainViewModel ::class.java)
        viewModel.update()

        return try {
                viewModel.update()
                Result.success()
            }
            catch (e: HttpException) {
                Result.retry()
            }
        }
    }