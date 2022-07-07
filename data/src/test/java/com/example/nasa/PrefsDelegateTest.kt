package com.example.nasa


import android.content.SharedPreferences
import com.example.nasa.data.service.prefs.PrefsDelegate
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert
import org.junit.Test

class PrefsDelegateTest {

    @Test
    fun delegateTest() {
        val editor = mockk<SharedPreferences.Editor>(relaxUnitFun = true)
        val prefs = mockk<SharedPreferences> {
            every { edit() } returns editor
        }

        var backingField = 42
        var mutableIntProperty: Int by PrefsDelegate(
            sharedPrefs = prefs,
            getValue = {
                Assert.assertEquals(prefs, this)
                backingField
            },
            setValue = {
                Assert.assertEquals(editor, this)
                Assert.assertEquals(1, it)

                backingField = it
            }
        )

        Assert.assertEquals(42, mutableIntProperty)

        mutableIntProperty = 1
        Assert.assertEquals(1, mutableIntProperty)

        verify {
            editor.apply()
        }
    }
}