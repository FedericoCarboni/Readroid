package dev.carbons.readroid

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import dev.carbons.readroid.epub.EpubFile
import dev.carbons.readroid.ui.theme.ReadroidTheme
import java.io.File
import java.util.zip.ZipFile

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val file = File.createTempFile("overlord", ".epub", cacheDir)
        val res = resources.openRawResource(R.raw.overlord1)
        val bytes = res.readBytes()
        res.close()
        file.writeBytes(bytes)
        val epubFile = EpubFile(ZipFile(file))
        setContent {
            ReadroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AndroidView(factory = {
                        WebView(applicationContext).apply {
                            webViewClient = EpubWebClient(epubFile)
                            // Allow zoom
                            settings.builtInZoomControls = true
                            settings.displayZoomControls = false
                            settings.setSupportZoom(true)
                            settings.textZoom = 100
                            settings.allowFileAccess = false
                            loadDataWithBaseURL("dev.carbons.readroid.epubfile:///OEBPS/Text/toc.xhtml", epubFile.getResource("toc")?.first?.readBytes()?.decodeToString() ?: "<h1>error</h1>", null, null, null)
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ReadroidTheme {
        Greeting("Android")
    }
}