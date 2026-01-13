import com.github.jengelman.gradle.plugins.shadow.transformers.Transformer
import com.github.jengelman.gradle.plugins.shadow.transformers.TransformerContext
import org.gradle.api.file.FileTreeElement
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class LicenseTransformer : Transformer {

    private val include: MutableCollection<String> = HashSet()
    private val exclude: MutableCollection<String> = HashSet()
    private val seen: MutableCollection<String> = ArrayList()
    private val data: ByteArrayOutputStream = ByteArrayOutputStream()

    @get:org.gradle.api.tasks.Input
    lateinit var destinationPath: String

    override fun canTransformResource(element: FileTreeElement): Boolean {
        val path = element.relativePath.pathString
        return include.contains(path) && !exclude.contains(path)
    }

    override fun hasTransformedResource(): Boolean = data.size() > 0

    override fun transform(context: TransformerContext) {
        context.`is`.use { inputStream: InputStream ->
            val content = inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
                .replace("\r\n", "\n")
            val trimmed = content.trim()
            
            if (!seen.contains(trimmed)) {
                data.bufferedWriter(StandardCharsets.UTF_8).use { writer ->
                    writer.write(content)
                    writer.write("\n${"-".repeat(20)}\n\n")
                }
                seen.add(trimmed)
            }
        }
    }

    override fun modifyOutputStream(
        zipOutputStream: ZipOutputStream,
        preserveFileTimestamps: Boolean
    ) {
        val entry = ZipEntry(destinationPath)
        // Set timestamp based on preserveFileTimestamps flag
        if (!preserveFileTimestamps) {
            entry.time = 0
        }
        zipOutputStream.putNextEntry(entry)
        
        ByteArrayInputStream(data.toByteArray()).use { input ->
            input.copyTo(zipOutputStream)
        }
        
        data.reset()
        zipOutputStream.closeEntry()
    }

    override fun getName(): String = "licenseTransformer"

    fun include(vararg paths: String) {
        this.include.addAll(paths)
    }

    fun exclude(vararg paths: String) {
        this.exclude.addAll(paths)
    }
}
