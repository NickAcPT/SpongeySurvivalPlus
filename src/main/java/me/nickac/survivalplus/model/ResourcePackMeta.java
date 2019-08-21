
package me.nickac.survivalplus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Resource pack information
 * <p>
 * Information about a resource pack
 * 
 */
public class ResourcePackMeta {

    /**
     * Information about the resource pack
     * (Required)
     * 
     */
    @SerializedName("pack")
    @Expose
    private Pack pack;

    /**
     * Information about the resource pack
     * (Required)
     * 
     */
    public Pack getPack() {
        return pack;
    }

    /**
     * Information about the resource pack
     * (Required)
     * 
     */
    public void setPack(Pack pack) {
        this.pack = pack;
    }

    private ResourcePackMeta withPack(Pack pack) {
        this.pack = pack;
        return this;
    }

    public ResourcePackMeta withPackFormat(Integer packFormat) {
        if (pack == null) pack = new Pack();
        pack.packFormat = packFormat;
        return this;
    }
    public ResourcePackMeta withDescription(String description) {
        if (pack == null) pack = new Pack();
        pack.description = description;
        return this;
    }

    /**
     * Information about the resource pack
     *
     */
    public static class Pack {

        /**
         * Resource pack version
         * <p>
         * This will be 1 for pre-1.9 versions, 2 for 1.9-1.10, 3 for 1.11-1.12 and 4 for 1.13
         * (Required)
         *
         */
        @SerializedName("pack_format")
        @Expose
        private Integer packFormat = 3;
        /**
         * Pack description
         * <p>
         * The description for this pack. Any text which doesn't fit on two lines will be removed
         *
         */
        @SerializedName("description")
        @Expose
        private String description;

        /**
         * Resource pack version
         * <p>
         * This will be 1 for pre-1.9 versions, 2 for 1.9-1.10, 3 for 1.11-1.12 and 4 for 1.13
         * (Required)
         *
         */
        public Integer getPackFormat() {
            return packFormat;
        }

        /**
         * Resource pack version
         * <p>
         * This will be 1 for pre-1.9 versions, 2 for 1.9-1.10, 3 for 1.11-1.12 and 4 for 1.13
         * (Required)
         *
         */
        public void setPackFormat(Integer packFormat) {
            this.packFormat = packFormat;
        }

        public Pack withPackFormat(Integer packFormat) {
            this.packFormat = packFormat;
            return this;
        }

        /**
         * Pack description
         * <p>
         * The description for this pack. Any text which doesn't fit on two lines will be removed
         *
         */
        public String getDescription() {
            return description;
        }

        /**
         * Pack description
         * <p>
         * The description for this pack. Any text which doesn't fit on two lines will be removed
         *
         */
        public void setDescription(String description) {
            this.description = description;
        }

        public Pack withDescription(String description) {
            this.description = description;
            return this;
        }

    }
}
