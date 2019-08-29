package me.nickac.survivalplus.managers;

import com.google.common.base.Suppliers;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import me.nickac.survivalplus.customitems.internal.CustomItemBaseEnum;
import me.nickac.survivalplus.misc.ImageUtis;
import me.nickac.survivalplus.misc.ImageWrapper;
import me.nickac.survivalplus.misc.model.ResourcePackMeta;
import me.nickac.survivalplus.misc.model.ResourcePackModel;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.resourcepack.ResourcePacks;
import spark.Spark;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static me.nickac.survivalplus.misc.model.ResourcePackModel.ModelOverride.ModelPredicate.Damaged._0;
import static me.nickac.survivalplus.misc.model.ResourcePackModel.ModelOverride.ModelPredicate.Damaged._1;

@SuppressWarnings("UnstableApiUsage")
@Singleton
public class ResourcePackManager {

    private final String path = "/survivalplus/pack.zip";
    @Inject
    PluginContainer container;
    @Inject
    private CustomItemManager itemManager;

    public ResourcePackManager() {
        Supplier<byte[]> packSupplier = Suppliers.memoizeWithExpiration(this::tryGetResourcePackZip, 10,
                TimeUnit.MINUTES);
        Spark.port(8081);

        Spark.get(path, (request, response) -> {
            response.type("application/zip");
            return packSupplier.get();
        });

        Spark.get("/direct/" + path, (request, response) -> {
            response.type("application/zip");
            return tryGetResourcePackZip();
        });

        Spark.get("/direct/cc/image.png", (request, response) -> {
            response.type("image/apng");
            return getColorCorrectedAsset("test-gui.png");
        });
    }

    private static int fixColor(int color) {
        int result = (int) (1.1903 * color + 0.2425);
        if (result > 255) result = 255;
        if (result < 0) result = 0;
        return result;
    }

    private Object getColorCorrectedAsset(String s) {
        byte[] assetInfo;
        try {
            assetInfo = container.getAsset(s).get().readBytes();
            BufferedImage img = ImageUtis.fromByteArray(assetInfo);

            final ImageWrapper wrapper = new ImageWrapper(img);

            wrapper.processImage((colors) -> {
                for (int i = 1; i < colors.length; i++) {
                    int color = colors[i];
                    colors[i] = fixColor(color);
                }
            });
            return ImageUtis.toByteArray(img);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private byte[] tryGetResourcePackZip() {
        try {
            return getResourcePackZip();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private byte[] getResourcePackZip() throws IOException {
        ZipFileBuilder builder = ZipFileBuilder.builder();
        builder.add("pack.mcmeta", new ResourcePackMeta()
                .withPackFormat(3)
                .withDescription("SurvivalPlus Custom Items/Blocks ResourcePack"));
        builder.enterFolder("assets");
        builder.enterFolder("minecraft");
        builder.enterFolder("models");
        builder.enterFolder("block");

        builder.add("custom_block.json", getCustomBlockModelBaseObject());
        builder.add("custom_block-s.json", getCustomBlockModelBaseObject());
        builder.add("custom_block-n.json", getCustomBlockModelBaseObject(-180));
        builder.add("custom_block-e.json", getCustomBlockModelBaseObject(90));
        builder.add("custom_block-w.json", getCustomBlockModelBaseObject(-90));
        itemManager.getRegisteredItems().forEach(i -> {
            try {
                builder.add(i.getModelAssetRawFile(), i.getModelAsset());
            } catch (IOException ignored) {
            }
        });

        builder.exitFolder(); //End Block
        Arrays.stream(CustomItemBaseEnum.values()).forEach(i -> {
            try {
                builder.add(i.getModelLoc().replace('/', File.separatorChar) + ".json", getCustomItem(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        builder.exitFolder(); //End Models

        builder.injectAssets(container.getSource().get(), "assets/survivalplus/assets-minecraft/");

        builder.exitFolder(); //End Minecraft
        builder.exitFolder(); //End Assets
        return builder.build();
    }

    @Listener
    public void onClientConnectionJoin(ClientConnectionEvent.Join event) {
        try {
            event.getTargetEntity().sendResourcePack(ResourcePacks.fromUri(new URI(String.format("http://%s:%s%s",
                    Sponge.getServer().getBoundAddress().get().getHostName(), Spark.port(), path))));
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private ResourcePackModel getCustomItem(CustomItemBaseEnum baseEnum) {
        ResourcePackModel model = new ResourcePackModel();
        model.withParent("item/handheld");
        model.withTextures(new ResourcePackModel.Textures()
                .with("layer0", baseEnum.getTextureLoc())
        );

        model.withOverrides(new ResourcePackModel.ModelOverride()
                .withPredicate(new ResourcePackModel.ModelOverride.ModelPredicate()
                        .withDamaged(_0)
                        .withDamage(0)
                ).withModel(baseEnum.getModelLoc())
        );

        itemManager.getRegisteredItems().stream().filter(i -> i.getItemBase().equals(baseEnum)).forEach(i -> {

            model.withOverrides(new ResourcePackModel.ModelOverride()
                    .withPredicate(new ResourcePackModel.ModelOverride.ModelPredicate()
                            .withDamaged(_0)
                            .withDamage(i.getOrdinal() / (baseEnum.getMaxDamage() + 1d))
                    ).withModel("block/" + Files.getNameWithoutExtension(i.getModelAssetRaw()))
            );
        });

        model.withOverrides(new ResourcePackModel.ModelOverride()
                .withPredicate(new ResourcePackModel.ModelOverride.ModelPredicate()
                        .withDamaged(_1)
                        .withDamage(0)
                ).withModel(baseEnum.getModelLoc())
        );

        return model;
    }

    private ResourcePackModel getCustomBlockModelBaseObject() {
        return getCustomBlockModelBaseObject(0);
    }

    private ResourcePackModel getCustomBlockModelBaseObject(int headRotation) {
        return new ResourcePackModel().withDisplay(new ResourcePackModel.Display()
                .withHead(new ResourcePackModel.Display.ModelTranslation()
                        .withRotation(-30.23, headRotation, 0)
                        .withTranslation(0, -30.75, -7.25)
                        .withScale(3.0125, 3.0125, 3.0125)
                )
                .withGui(new ResourcePackModel.Display.ModelTranslation()
                        .withRotation(30, 225, 0)
                        .withTranslation(0, 0, 0)
                        .withScale(0.625, 0.625, 0.625)
                )
                .withGround(new ResourcePackModel.Display.ModelTranslation()
                        .withRotation(0, 0, 0)
                        .withTranslation(0, 3, 0)
                        .withScale(0.25, 0.25, 0.25)
                )
                .withFixed(new ResourcePackModel.Display.ModelTranslation()
                        .withRotation(0, 0, 0)
                        .withTranslation(0, 0, 0)
                        .withScale(0.5, 0.5, 0.5)
                )
                .withThirdPersonRighthand(new ResourcePackModel.Display.ModelTranslation()
                        .withRotation(75, 45, 0)
                        .withTranslation(0, 2.5, 0)
                        .withScale(0.375, 0.375, 0.375)
                )
                .withFirstPersonRighthand(new ResourcePackModel.Display.ModelTranslation()
                        .withRotation(0, 45, 0)
                        .withTranslation(0, 0, 0)
                        .withScale(0.40, 0.40, 0.40)
                )
                .withFirstPersonLefthand(new ResourcePackModel.Display.ModelTranslation()
                        .withRotation(0, 225, 0)
                        .withTranslation(0, 0, 0)
                        .withScale(0.40, 0.40, 0.40)
                )
        );
    }
}
