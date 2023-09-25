package dev.carbons.readroid.epub

import org.w3c.dom.Element
import java.io.InputStream
import java.util.zip.ZipFile
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.NullPointerException

const val BOOK_ID_ID = "BookId"
const val NAMESPACE_OPF = "http://www.idpf.org/2007/opf"
const val NAMESPACE_DUBLIN_CORE = "http://purl.org/dc/elements/1.1/"
const val PREFIX_DUBLIN_CORE = "dc"
const val PREFIX_OPF = "opf"
const val dateFormat = "yyyy-MM-dd"

private val docFactory = DocumentBuilderFactory.newInstance()

class EpubFile constructor(private val zipFile: ZipFile) {
    private val manifest: HashMap<String, ManifestItem> = HashMap()
    private val spine: ArrayList<String> = ArrayList()
    private val guide: ArrayList<String> = ArrayList()

    init {
        val entry = zipFile.getEntry("OEBPS/content.opf")
        val manifestIs = zipFile.getInputStream(entry)
        val documentBuilder = docFactory.newDocumentBuilder()
        val manifestDocument = documentBuilder.parse(manifestIs)
        val manifestElement = manifestDocument.documentElement.getElementsByTagName("manifest").item(0) as? Element
        val manifestItems = manifestElement?.getElementsByTagName("item")
            ?: throw NullPointerException()
        for (i in 0 until manifestItems.length) {
            val element = manifestItems.item(i)
            val id = element.attributes.getNamedItem("id")?.textContent
            val href = element.attributes.getNamedItem("href")?.textContent
            val mediaType = element.attributes.getNamedItem("media-type")?.textContent
            if (id != null && href != null) {
                manifest[id] = ManifestItem(id, href, mediaType)
            }
        }
        println(manifest)
    }

    fun getResourceByPath(path: String): InputStream? {
        val entry = zipFile.getEntry(path)
        if (entry == null || entry.isDirectory) {
            return null
        }
        return zipFile.getInputStream(entry)
    }

    fun getResource(name: String): Pair<InputStream, ManifestItem>? {
        val href = manifest[name]?.href
        val entry = zipFile.getEntry("OEBPS/" + href)
        if (entry == null || entry.isDirectory) {
            return null
        }
        return Pair(zipFile.getInputStream(entry), manifest[name] ?: throw NullPointerException())
    }
}