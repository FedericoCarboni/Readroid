package dev.carbons.readroid

import dev.carbons.readroid.epub.EpubFile
import org.junit.Test

import org.junit.Assert.*
import org.junit.internal.Classes.getClass
import java.util.zip.ZipFile

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testEpubFile() {
        val epubFile = EpubFile(ZipFile(getClass("dev.carbons.readroid.ExampleUnitTest").getClassLoader().getResource("raw/overlord1.epub").file))
    }
}