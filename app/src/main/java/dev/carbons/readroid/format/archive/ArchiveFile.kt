package dev.carbons.readroid.format.archive

import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZFile
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarFile
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipFile
import java.io.Closeable
import java.io.InputStream
import java.nio.channels.SeekableByteChannel
import java.util.Enumeration

sealed interface ArchiveFile : Closeable {
    val entries: Enumeration<ArchiveEntry>

    fun getInputStream(entry: ArchiveEntry): InputStream?

    class Zip(channel: SeekableByteChannel) : ArchiveFile {
        private val inner: ZipFile
        init {
            inner = ZipFile(channel)
        }
        override val entries: Enumeration<ArchiveEntry>
            get() =
                 inner.entries as Enumeration<ArchiveEntry>


        override fun getInputStream(entry: ArchiveEntry): InputStream? {
            return inner.getInputStream(entry as? ZipArchiveEntry ?: return null)
        }

        override fun close() =
            inner.close()

    }

    class Tar(channel: SeekableByteChannel) : ArchiveFile {
        private val inner: TarFile

        init {
            inner = TarFile(channel)
        }

        override val entries: Enumeration<ArchiveEntry>
            get() = inner.entries as Enumeration<ArchiveEntry>

        override fun getInputStream(entry: ArchiveEntry): InputStream? {
            return inner.getInputStream(entry as? TarArchiveEntry ?: return null)
        }

        override fun close() = inner.close()
    }

    class SevenZ(channel: SeekableByteChannel) : ArchiveFile {
        private val inner: SevenZFile
        init {
            inner = SevenZFile(channel)
        }

        override val entries: Enumeration<ArchiveEntry>
            get() = inner.entries as Enumeration<ArchiveEntry>

        override fun getInputStream(entry: ArchiveEntry): InputStream? {
            return inner.getInputStream(entry as? SevenZArchiveEntry ?: return null)
        }

        override fun close() = inner.close()
    }
}