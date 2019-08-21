package me.nickac.survivalplus.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import me.nickac.survivalplus.model.ResourcePackModel;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.spongepowered.api.asset.Asset;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Stack;

public class ZipFileBuilder {

    private static Gson gson;

    private static JsonSerializer<Double> doubleJsonSerializer;

    private static JsonSerializer<ResourcePackModel.ModelOverride.ModelPredicate.Damaged> damagedJsonSerializer;

    static {
        doubleJsonSerializer = (src, typeOfSrc, context) -> new JsonPrimitive(BigDecimal.valueOf(src));
        damagedJsonSerializer = (src, typeOfSrc, context) -> new JsonPrimitive(src.value());
        gson = new GsonBuilder()
                .registerTypeAdapter(double.class, doubleJsonSerializer)
                .registerTypeAdapter(ResourcePackModel.ModelOverride.ModelPredicate.Damaged.class,
                        damagedJsonSerializer)
                .registerTypeAdapter(Double.class, doubleJsonSerializer)
                .disableHtmlEscaping().create();
    }

    private Stack<String> folderPath = new Stack<>();
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private ZipArchiveOutputStream zos = new ZipArchiveOutputStream(baos);

    private ZipFileBuilder() {
    }

    public static ZipFileBuilder builder() {
        return new ZipFileBuilder();
    }

    public ZipFileBuilder enterFolder(String name) {
        folderPath.push(name);
        return this;
    }

    public ZipFileBuilder exitFolder() {
        folderPath.pop();
        return this;
    }

    private String getPathPrefix() {
        String result = String.join("/", folderPath);
        if (!result.isEmpty())
            result += "/";
        return result;
    }

    public ZipFileBuilder add(String path, Asset asset) throws IOException {
        return addEntry(path, asset.readBytes());
    }

    public ZipFileBuilder add(String path, Optional<Asset> asset) throws IOException {
        return addEntry(path, asset.get().readBytes());
    }

    public ZipFileBuilder add(String path, Object obj) throws IOException {
        return addEntry(path, gson.toJson(obj).getBytes(StandardCharsets.UTF_8));
    }

    private ZipFileBuilder addEntry(String path, byte[] bytes) throws IOException {
        String endPath = getPathPrefix() + path;
        endPath = endPath.replace('\\', '/');

        zos.putArchiveEntry(new ZipArchiveEntry(endPath));
        zos.write(bytes);
        zos.closeArchiveEntry();
        zos.flush();
        return this;
    }

    public byte[] build() throws IOException {
        zos.close();
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
    }

    public ZipFileBuilder injectAssets(Path source, String s) {

        try (FileInputStream stream = new FileInputStream(source.toFile())) {
            try (ZipArchiveInputStream zip = new ZipArchiveInputStream(stream)) {

                ZipArchiveEntry entry = zip.getNextZipEntry();
                while (entry != null) {
                    if (!entry.isDirectory() && entry.getName().startsWith(s)) {

                        byte[] outBytes;
                        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                            IOUtils.copy(zip, out);
                            outBytes = out.toByteArray();
                        }

                        addEntry(entry.getName().substring(s.length()), outBytes);
                    }
                    entry = zip.getNextZipEntry();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;

    }
}
