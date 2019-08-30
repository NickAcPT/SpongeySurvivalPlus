package io.github.nickacpt.survivalplus.misc.model;

import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class CustomRawAsset implements Asset {
    private PluginContainer owner;
    private byte[] content;

    public CustomRawAsset(PluginContainer owner, byte[] content) {
        this.owner = owner;
        this.content = content;
    }

    @Override
    public byte[] readBytes() throws IOException {
        return content;
    }

    @Override
    public PluginContainer getOwner() {
        return owner;
    }

    @Override
    public URL getUrl() {
        try {
            return new URI("memory://file-name").toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            return null;
        }
    }
}
