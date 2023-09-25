package dev.carbons.readroid

import android.app.Application
import org.conscrypt.Conscrypt
import java.security.Security

class ReadroidApp : Application() {
    override fun onCreate() {
        // Older but supported versions of Android have old security libraries which don't support
        // TLS 1.3 which is required for some media servers. By using Conscrypt we ensure all
        // connections use the best ciphers available.
        Security.insertProviderAt(Conscrypt.newProvider(), 1)
        super.onCreate()
    }
}