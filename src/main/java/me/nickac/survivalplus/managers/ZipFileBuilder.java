package me.nickac.survivalplus.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import me.nickac.survivalplus.model.ResourcePackModel;
import org.spongepowered.api.asset.Asset;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileBuilder {

    private static Gson gson;

    private static JsonSerializer<Double> doubleJsonSerializer;

    private static JsonSerializer<ResourcePackModel.ModelOverride.ModelPredicate.Damaged> damagedJsonSerializer;

    static {
        doubleJsonSerializer = (src, typeOfSrc, context) -> new JsonPrimitive(BigDecimal.valueOf(src));
        damagedJsonSerializer = (src, typeOfSrc, context) -> new JsonPrimitive(src.value());
        gson = new GsonBuilder()
                .registerTypeAdapter(double.class, doubleJsonSerializer)
                .registerTypeAdapter(ResourcePackModel.ModelOverride.ModelPredicate.Damaged.class, damagedJsonSerializer)
                .registerTypeAdapter(Double.class, doubleJsonSerializer)
                .disableHtmlEscaping().create();
    }

    private Stack<String> folderPath = new Stack<>();
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private ZipOutputStream zos = new ZipOutputStream(baos);

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
        String result = String.join(File.separator, folderPath);
        if (!result.isEmpty())
            result += File.separator;
        return result;
    }

    public ZipFileBuilder add(String path, Asset asset) throws IOException {
        return addEntry(path, asset.readBytes());
    }

    public ZipFileBuilder add(String path, Object obj) throws IOException {
        return addEntry(path, gson.toJson(obj).getBytes(StandardCharsets.UTF_8));
    }

    private ZipFileBuilder addEntry(String path, byte[] bytes) throws IOException {
        ZipEntry entry = new ZipEntry(getPathPrefix() + path);
        zos.putNextEntry(entry);
        zos.write(bytes);
        zos.finish();
        zos.flush();
        zos.closeEntry();
        return this;
    }

    public byte[] build() throws IOException {
        zos.finish();
        zos.close();
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
    }
}
