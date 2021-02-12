package reprator.mobiquity

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import reprator.mobiquity.DBFakeData.getFakeDataList
import reprator.mobiquity.database.LocationDao
import reprator.mobiquity.implementation.MobiQuityRoomDb
import reprator.mobiquity.testUtils.MainCoroutineRule
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MobiquityRoomDaoTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @JvmField
    @Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var db: MobiQuityRoomDb
    lateinit var locationDao: LocationDao

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, MobiQuityRoomDb::class.java
        ).build()
        locationDao = db.locationDao()
    }

    @Test
    fun saveLocationEntityItem_fetchItem() = mainCoroutineRule.runBlockingTest {

        val input = getFakeDataList()[0]
        val output = 1
        val outputFetch = getFakeDataList()

        val result = locationDao.insertLocation(input)
        val fetchResult = locationDao.getLocationList()

        Truth.assertThat(result).isEqualTo(output)
        Truth.assertThat(fetchResult[0]).isEqualTo(outputFetch[0])
        Truth.assertThat(fetchResult).hasSize(1)
    }

    @Test
    fun saveItem_deleteItem() = mainCoroutineRule.runBlockingTest {
        val input = getFakeDataList()[0]
        val insertOutput = 1
        val deleteOutputNoItem = 0

        val deleteResultNegative = locationDao.deleteLocation(input)
        Truth.assertThat(deleteResultNegative).isEqualTo(deleteOutputNoItem)

        val insertResult = locationDao.insertLocation(input)
        Truth.assertThat(insertResult).isEqualTo(insertOutput)

        val deleteResult = locationDao.deleteLocation(input)
        Truth.assertThat(deleteResult).isEqualTo(insertOutput)
    }

    @Test
    fun saveItemAndClearTable() = mainCoroutineRule.runBlockingTest {
        val input = getFakeDataList()[0]
        val insertOutput = 1
        val outputFetch = getFakeDataList()

        val insertResult = locationDao.insertLocation(input)
        val fetchResult = locationDao.getLocationList()

        Truth.assertThat(insertResult).isEqualTo(insertOutput)
        Truth.assertThat(fetchResult[0]).isEqualTo(outputFetch[0])
        Truth.assertThat(fetchResult).hasSize(1)

        val deleteResult = locationDao.clearTable()
        Truth.assertThat(deleteResult).isEqualTo(1)

        val newFetchResult = locationDao.getLocationList()
        Truth.assertThat(newFetchResult).hasSize(0)
    }
}