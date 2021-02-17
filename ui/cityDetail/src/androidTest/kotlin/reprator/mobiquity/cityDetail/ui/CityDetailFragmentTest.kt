package reprator.mobiquity.cityDetail.ui

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.annotation.UiThreadTest
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import reprator.mobiquity.cityDetail.R
import reprator.mobiquity.cityDetail.dispatcher.MockWebServerDispatcher
import reprator.mobiquity.cityDetail.rule.IdlingResourceRule
import reprator.mobiquity.cityDetail.rule.MockWebServerRule
import reprator.mobiquity.cityDetail.util.launchFragmentInHiltContainer

@MediumTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CityDetailFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val mockWebServerRule = MockWebServerRule()

    @get:Rule
    val idlingResourceRule = IdlingResourceRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @UiThreadTest
    @Test
    fun testRecyclerShouldVisibleWhenGetFromRemoteSuccess() {
        mockWebServerRule.server.dispatcher = MockWebServerDispatcher().SuccessDispatcher(200)

        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        ).apply {
            setGraph(R.navigation.mobiquity_nav_graph_home)
            setCurrentDestination(R.id.navigation_cityDetail)
        }

        val bundle = CityDetailFragmentArgs("20.65, 32.09", "Hajipur").toBundle()
        launchFragmentInHiltContainer<CityDetailFragment>(bundle, R.style.Theme_MobiQuity) {
            Navigation.setViewNavController(view!!, navController)
        }

        Espresso.onView(withId(R.id.cityDetail_forecast_rec))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /* @Test
     fun testOpenFirstSurah_thenNNavigateToReadSurahFragment(){
         val navController = mockk(NavController::class.java)
         var testViewModel: ListSurahViewModel? = null
         launchFragmentInHiltContainer<ListSurahFragment>(fragmentFactory = fragmentFactory) {
             Navigation.setViewNavController(requireView(), navController)
             testViewModel = viewModel
         }
         Espresso.onView(withId(R.id.rv_quran_surah)).perform(
             RecyclerViewActions
                 .actionOnItemAtPosition<ReadSurahAdapter.ReadSurahViewHolder>(113,
                     ViewActions.click()
                 )
         )

         val data = testViewModel?.allSurah?.value?.data?.last()!!
         Mockito.verify(navController).navigate(
             ListSurahFragmentDirections.actionQuranFragmentToReadSurahActivity(
                 data.number.toString(), data.englishName, data.englishNameTranslation, false
             ))
     }*/
}