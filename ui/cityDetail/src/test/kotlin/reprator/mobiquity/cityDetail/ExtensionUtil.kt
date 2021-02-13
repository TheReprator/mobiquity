package reprator.mobiquity.cityDetail

import androidx.lifecycle.Observer
import io.mockk.every

inline fun <reified T> Observer<T>.onChangeExtension() {
    every {
        onChanged(any())
    } returns Unit
}