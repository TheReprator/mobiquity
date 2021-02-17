package reprator.mobiquity.cityDetail.rule

import androidx.test.espresso.IdlingRegistry
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import reprator.mobiquity.base_android.util.EspressoIdlingResource

class IdlingResourceRule : TestWatcher() {
    override fun starting(description: Description?) {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        super.starting(description)
    }

    override fun finished(description: Description?) {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        super.finished(description)
    }
}
