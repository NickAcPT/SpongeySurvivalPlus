package me.nickac.survivalplus.misc.model;

import com.google.common.collect.Streams;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class ResourcePackModel {

    /**
     * Set the parent model for the current model.
     * <p>
     * If 'elements' is defined, it will override the 'elements' which extends from the parent.
     */
    @SerializedName("parent")
    @Expose
    private String parent;
    /**
     * (Block models only) Whether or not to use ambient occlusion
     * <p>
     */
    @SerializedName("ambientocclusion")
    @Expose
    private Boolean ambientocclusion;
    /**
     * Define texture variables
     * <p>
     */
    @SerializedName("textures")
    @Expose
    private Textures textures;
    @SerializedName("display")
    @Expose
    private Display display;
    /**
     * All the elements of the model
     * <p>
     * If both 'parent' and 'elements' are set, the 'elements' tag overrides the 'elements' tag from the previous model
     */
    @SerializedName("elements")
    @Expose
    private List<ModelElement> elements = new ArrayList<ModelElement>();
    /**
     * Determine cases which a different model should be used based on item tags
     * <p>
     */
    @SerializedName("overrides")
    @Expose
    private List<ModelOverride> overrides = new ArrayList<ModelOverride>();

    /**
     * Set the parent model for the current model.
     * <p>
     * If 'elements' is defined, it will override the 'elements' which extends from the parent.
     */
    public String getParent() {
        return parent;
    }

    /**
     * Set the parent model for the current model.
     * <p>
     * If 'elements' is defined, it will override the 'elements' which extends from the parent.
     */
    public void setParent(String parent) {
        this.parent = parent;
    }

    public ResourcePackModel withParent(String parent) {
        this.parent = parent;
        return this;
    }

    /**
     * (Block models only) Whether or not to use ambient occlusion
     * <p>
     */
    public Boolean getAmbientOcclusion() {
        return ambientocclusion;
    }

    /**
     * (Block models only) Whether or not to use ambient occlusion
     * <p>
     */
    public void setAmbientOcclusion(Boolean ambientocclusion) {
        this.ambientocclusion = ambientocclusion;
    }

    public ResourcePackModel withAmbientOcclusion(Boolean ambientocclusion) {
        this.ambientocclusion = ambientocclusion;
        return this;
    }

    /**
     * Define texture variables
     * <p>
     */
    public Textures getTextures() {
        return textures;
    }

    /**
     * Define texture variables
     * <p>
     */
    public void setTextures(Textures textures) {
        this.textures = textures;
    }

    public ResourcePackModel withTextures(Textures textures) {
        this.textures = textures;
        return this;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public ResourcePackModel withDisplay(Display display) {
        this.display = display;
        return this;
    }

    /**
     * All the elements of the model
     * <p>
     * If both 'parent' and 'elements' are set, the 'elements' tag overrides the 'elements' tag from the previous model
     */
    public List<ModelElement> getElements() {
        return elements;
    }

    /**
     * All the elements of the model
     * <p>
     * If both 'parent' and 'elements' are set, the 'elements' tag overrides the 'elements' tag from the previous model
     */
    public void setElements(List<ModelElement> elements) {
        this.elements = elements;
    }

    public ResourcePackModel withElements(List<ModelElement> elements) {
        this.elements = elements;
        return this;
    }

    /**
     * Determine cases which a different model should be used based on item tags
     * <p>
     */
    public List<ModelOverride> getOverrides() {
        return overrides;
    }

    /**
     * Determine cases which a different model should be used based on item tags
     * <p>
     */
    public void setOverrides(List<ModelOverride> overrides) {
        this.overrides = overrides;
    }


    public ResourcePackModel withOverrides(ModelOverride... overrides) {
        this.overrides = Streams.concat(this.overrides.stream(), Arrays.stream(overrides)).collect(Collectors.toList());
        return this;
    }

    /**
     * Define texture variables
     * <p>
     */
    public static class Textures extends HashMap<String, String> {

        public Textures withParticle(String particle) {
            put("particle", particle);
            return this;
        }

        public Textures with(String texture, String loc) {
            put(texture, loc);
            return this;
        }

    }

    public static class Display {

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        @SerializedName("thirdperson_righthand")
        @Expose
        private ModelTranslation thirdPersonRighthand;
        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        @SerializedName("thirdperson_lefthand")
        @Expose
        private ModelTranslation thirdPersonLefthand;
        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        @SerializedName("firstperson_righthand")
        @Expose
        private ModelTranslation firstPersonRighthand;
        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        @SerializedName("firstperson_lefthand")
        @Expose
        private ModelTranslation firstPersonLefthand;
        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        @SerializedName("gui")
        @Expose
        private ModelTranslation gui;
        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        @SerializedName("head")
        @Expose
        private ModelTranslation head;
        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        @SerializedName("ground")
        @Expose
        private ModelTranslation ground;
        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        @SerializedName("fixed")
        @Expose
        private ModelTranslation fixed;

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public ModelTranslation getThirdPersonRighthand() {
            return thirdPersonRighthand;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public void setThirdPersonRighthand(ModelTranslation translation) {
            this.thirdPersonRighthand = translation;
        }

        public Display withThirdPersonRighthand(ModelTranslation translation) {
            this.thirdPersonRighthand = translation;
            return this;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public ModelTranslation getThirdPersonLefthand() {
            return thirdPersonLefthand;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public void setThirdPersonLefthand(ModelTranslation translation) {
            this.thirdPersonLefthand = translation;
        }

        public Display withThirdPersonLefthand(ModelTranslation translation) {
            this.thirdPersonLefthand = translation;
            return this;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public ModelTranslation getFirstPersonRighthand() {
            return firstPersonRighthand;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public void setFirstPersonRighthand(ModelTranslation translation) {
            this.firstPersonRighthand = translation;
        }

        public Display withFirstPersonRighthand(ModelTranslation translation) {
            this.firstPersonRighthand = translation;
            return this;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public ModelTranslation getFirstPersonLefthand() {
            return firstPersonLefthand;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public void setFirstPersonLefthand(ModelTranslation translation) {
            this.firstPersonLefthand = translation;
        }

        public Display withFirstPersonLefthand(ModelTranslation translation) {
            this.firstPersonLefthand = translation;
            return this;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public ModelTranslation getGui() {
            return gui;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public void setGui(ModelTranslation gui) {
            this.gui = gui;
        }

        public Display withGui(ModelTranslation gui) {
            this.gui = gui;
            return this;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public ModelTranslation getHead() {
            return head;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public void setHead(ModelTranslation head) {
            this.head = head;
        }

        public Display withHead(ModelTranslation head) {
            this.head = head;
            return this;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public ModelTranslation getGround() {
            return ground;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public void setGround(ModelTranslation ground) {
            this.ground = ground;
        }

        public Display withGround(ModelTranslation ground) {
            this.ground = ground;
            return this;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public ModelTranslation getFixed() {
            return fixed;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public void setFixed(ModelTranslation fixed) {
            this.fixed = fixed;
        }

        public Display withFixed(ModelTranslation fixed) {
            this.fixed = fixed;
            return this;
        }

        /**
         * Translations are applied to the model before rotations
         * <p>
         */
        public static class ModelTranslation {

            @SerializedName("rotation")
            @Expose
            private double[] rotation = new double[]{};
            @SerializedName("translation")
            @Expose
            private double[] translation = new double[]{};
            @SerializedName("scale")
            @Expose
            private double[] scale = new double[]{};

            public double[] getRotation() {
                return rotation;
            }

            public void setRotation(double[] rotation) {
                this.rotation = rotation;
            }

            public ModelTranslation withRotation(double... rotation) {
                this.rotation = rotation;
                return this;
            }

            public double[] getTranslation() {
                return translation;
            }

            public void setTranslation(double[] translation) {
                this.translation = translation;
            }

            public ModelTranslation withTranslation(double... translation) {
                this.translation = translation;
                return this;
            }

            public double[] getScale() {
                return scale;
            }

            public void setScale(double[] scale) {
                this.scale = scale;
            }

            public ModelTranslation withScale(double... scale) {
                this.scale = scale;
                return this;
            }

        }
    }

    public static class ModelElement {

        @SerializedName("from")
        @Expose
        private double[] from = new double[]{};
        @SerializedName("to")
        @Expose
        private double[] to = new double[]{};
        /**
         * The rotation of the element
         * <p>
         */
        @SerializedName("rotation")
        @Expose
        private Rotation rotation;
        /**
         * Whether or not to render shadows
         * <p>
         */
        @SerializedName("shade")
        @Expose
        private Boolean shade;
        /**
         * Hold all the faces of the cube. If a face is left out, it will not be rendered
         * <p>
         */
        @SerializedName("faces")
        @Expose
        private Faces faces;

        public double[] getFrom() {
            return from;
        }

        public void setFrom(double[] from) {
            this.from = from;
        }

        public ModelElement withFrom(double[] from) {
            this.from = from;
            return this;
        }

        public double[] getTo() {
            return to;
        }

        public void setTo(double[] to) {
            this.to = to;
        }

        public ModelElement withTo(double[] to) {
            this.to = to;
            return this;
        }

        /**
         * The rotation of the element
         * <p>
         */
        public Rotation getRotation() {
            return rotation;
        }

        /**
         * The rotation of the element
         * <p>
         */
        public void setRotation(Rotation rotation) {
            this.rotation = rotation;
        }

        public ModelElement withRotation(Rotation rotation) {
            this.rotation = rotation;
            return this;
        }

        /**
         * Whether or not to render shadows
         * <p>
         */
        public Boolean getShade() {
            return shade;
        }

        /**
         * Whether or not to render shadows
         * <p>
         */
        public void setShade(Boolean shade) {
            this.shade = shade;
        }

        public ModelElement withShade(Boolean shade) {
            this.shade = shade;
            return this;
        }

        /**
         * Hold all the faces of the cube. If a face is left out, it will not be rendered
         * <p>
         */
        public Faces getFaces() {
            return faces;
        }

        /**
         * Hold all the faces of the cube. If a face is left out, it will not be rendered
         * <p>
         */
        public void setFaces(Faces faces) {
            this.faces = faces;
        }

        public ModelElement withFaces(Faces faces) {
            this.faces = faces;
            return this;
        }

        /**
         * The rotation of the element
         * <p>
         */
        public static class Rotation {

            @SerializedName("origin")
            @Expose
            private double[] origin = new double[]{};
            /**
             * The direction of rotation
             * <p>
             */
            @SerializedName("axis")
            @Expose
            private Axis axis;
            /**
             * The angle of rotation
             * <p>
             */
            @SerializedName("angle")
            @Expose
            private Angle angle;
            /**
             * Whether or not to scale the faces across the whole block
             * <p>
             */
            @SerializedName("rescale")
            @Expose
            private Boolean rescale;

            public double[] getOrigin() {
                return origin;
            }

            public void setOrigin(double[] origin) {
                this.origin = origin;
            }

            public Rotation withOrigin(double[] origin) {
                this.origin = origin;
                return this;
            }

            /**
             * The direction of rotation
             * <p>
             */
            public Axis getAxis() {
                return axis;
            }

            /**
             * The direction of rotation
             * <p>
             */
            public void setAxis(Axis axis) {
                this.axis = axis;
            }

            public Rotation withAxis(Axis axis) {
                this.axis = axis;
                return this;
            }

            /**
             * The angle of rotation
             * <p>
             */
            public Angle getAngle() {
                return angle;
            }

            /**
             * The angle of rotation
             * <p>
             */
            public void setAngle(Angle angle) {
                this.angle = angle;
            }

            public Rotation withAngle(Angle angle) {
                this.angle = angle;
                return this;
            }

            /**
             * Whether or not to scale the faces across the whole block
             * <p>
             */
            public Boolean getRescale() {
                return rescale;
            }

            /**
             * Whether or not to scale the faces across the whole block
             * <p>
             */
            public void setRescale(Boolean rescale) {
                this.rescale = rescale;
            }

            public Rotation withRescale(Boolean rescale) {
                this.rescale = rescale;
                return this;
            }

            public enum Angle {

                @SerializedName("-45")
                _45(-45.0D),
                @SerializedName("-22.5")
                _22_5(-22.5D),
                @SerializedName("0")
                _0(0.0D),
                @SerializedName("22.5")
                _22_5_(22.5D),
                @SerializedName("45")
                _45_(45.0D);
                private final static Map<Double, Angle> CONSTANTS = new HashMap<Double, Angle>();

                static {
                    for (Angle c : Angle.values()) {
                        CONSTANTS.put(c.value, c);
                    }
                }

                private final double value;

                private Angle(double value) {
                    this.value = value;
                }

                public static Angle fromValue(double value) {
                    Angle constant = CONSTANTS.get(value);
                    if (constant == null) {
                        throw new IllegalArgumentException((value + ""));
                    } else {
                        return constant;
                    }
                }

                public double value() {
                    return this.value;
                }

            }

            public enum Axis {

                @SerializedName("x")
                X("x"),
                @SerializedName("y")
                Y("y"),
                @SerializedName("z")
                Z("z");
                private final static Map<String, Axis> CONSTANTS = new HashMap<String, Axis>();

                static {
                    for (Axis c : Axis.values()) {
                        CONSTANTS.put(c.value, c);
                    }
                }

                private final String value;

                private Axis(String value) {
                    this.value = value;
                }

                public static Axis fromValue(String value) {
                    Axis constant = CONSTANTS.get(value);
                    if (constant == null) {
                        throw new IllegalArgumentException(value);
                    } else {
                        return constant;
                    }
                }

                @java.lang.Override
                public String toString() {
                    return this.value;
                }

                public String value() {
                    return this.value;
                }

            }

        }
    }

    /**
     * Hold all the faces of the cube. If a face is left out, it will not be rendered
     * <p>
     */
    public static class Faces {

        /**
         * Set the properties of the specified face
         * <p>
         */
        @SerializedName("down")
        @Expose
        private FaceProperties down;
        /**
         * Set the properties of the specified face
         * <p>
         */
        @SerializedName("up")
        @Expose
        private FaceProperties up;
        /**
         * Set the properties of the specified face
         * <p>
         */
        @SerializedName("east")
        @Expose
        private FaceProperties east;
        /**
         * Set the properties of the specified face
         * <p>
         */
        @SerializedName("west")
        @Expose
        private FaceProperties west;
        /**
         * Set the properties of the specified face
         * <p>
         */
        @SerializedName("south")
        @Expose
        private FaceProperties south;
        /**
         * Set the properties of the specified face
         * <p>
         */
        @SerializedName("north")
        @Expose
        private FaceProperties north;

        /**
         * Set the properties of the specified face
         * <p>
         */
        public FaceProperties getDown() {
            return down;
        }

        /**
         * Set the properties of the specified face
         * <p>
         */
        public void setDown(FaceProperties down) {
            this.down = down;
        }

        public Faces withDown(FaceProperties down) {
            this.down = down;
            return this;
        }

        /**
         * Set the properties of the specified face
         * <p>
         */
        public FaceProperties getUp() {
            return up;
        }

        /**
         * Set the properties of the specified face
         * <p>
         */
        public void setUp(FaceProperties up) {
            this.up = up;
        }

        public Faces withUp(FaceProperties up) {
            this.up = up;
            return this;
        }

        /**
         * Set the properties of the specified face
         * <p>
         */
        public FaceProperties getEast() {
            return east;
        }

        /**
         * Set the properties of the specified face
         * <p>
         */
        public void setEast(FaceProperties east) {
            this.east = east;
        }

        public Faces withEast(FaceProperties east) {
            this.east = east;
            return this;
        }

        /**
         * Set the properties of the specified face
         * <p>
         */
        public FaceProperties getWest() {
            return west;
        }

        /**
         * Set the properties of the specified face
         * <p>
         */
        public void setWest(FaceProperties west) {
            this.west = west;
        }

        public Faces withWest(FaceProperties west) {
            this.west = west;
            return this;
        }

        /**
         * Set the properties of the specified face
         * <p>
         */
        public FaceProperties getSouth() {
            return south;
        }

        /**
         * Set the properties of the specified face
         * <p>
         */
        public void setSouth(FaceProperties south) {
            this.south = south;
        }

        public Faces withSouth(FaceProperties south) {
            this.south = south;
            return this;
        }

        /**
         * Set the properties of the specified face
         * <p>
         */
        public FaceProperties getNorth() {
            return north;
        }

        /**
         * Set the properties of the specified face
         * <p>
         */
        public void setNorth(FaceProperties north) {
            this.north = north;
        }

        public Faces withNorth(FaceProperties north) {
            this.north = north;
            return this;
        }

        /**
         * Set the properties of the specified face
         * <p>
         */
        public static class FaceProperties {

            @SerializedName("uv")
            @Expose
            private double[] uv = new double[]{};
            @SerializedName("texture")
            @Expose
            private String texture;
            /**
             * Set whether a face does not need to be rendered when there is a block touching it in the specified
             * position
             * <p>
             */
            @SerializedName("cullface")
            @Expose
            private Cullface cullface;
            /**
             * Rotate the texture by the specified number of degrees
             * <p>
             */
            @SerializedName("rotation")
            @Expose
            private Rotation rotation;
            /**
             * Determine whether to tint the texture using a hardcoded tint index
             * <p>
             * The default is not using the tint, and any number causes it to use tint. Note that only certain blocks
             * have a tint index, all others will be unaffected
             */
            @SerializedName("tintindex")
            @Expose
            private Integer tintindex;

            public double[] getUv() {
                return uv;
            }

            public void setUv(double[] uv) {
                this.uv = uv;
            }

            public FaceProperties withUv(double[] uv) {
                this.uv = uv;
                return this;
            }

            public String getTexture() {
                return texture;
            }

            public void setTexture(String texture) {
                this.texture = texture;
            }

            public FaceProperties withTexture(String texture) {
                this.texture = texture;
                return this;
            }

            /**
             * Set whether a face does not need to be rendered when there is a block touching it in the specified
             * position
             * <p>
             */
            public Cullface getCullface() {
                return cullface;
            }

            /**
             * Set whether a face does not need to be rendered when there is a block touching it in the specified
             * position
             * <p>
             */
            public void setCullface(Cullface cullface) {
                this.cullface = cullface;
            }

            public FaceProperties withCullface(Cullface cullface) {
                this.cullface = cullface;
                return this;
            }

            /**
             * Rotate the texture by the specified number of degrees
             * <p>
             */
            public Rotation getRotation() {
                return rotation;
            }

            /**
             * Rotate the texture by the specified number of degrees
             * <p>
             */
            public void setRotation(Rotation rotation) {
                this.rotation = rotation;
            }

            public FaceProperties withRotation(Rotation rotation) {
                this.rotation = rotation;
                return this;
            }

            /**
             * Determine whether to tint the texture using a hardcoded tint index
             * <p>
             * The default is not using the tint, and any number causes it to use tint. Note that only certain blocks
             * have a tint index, all others will be unaffected
             */
            public Integer getTintindex() {
                return tintindex;
            }

            /**
             * Determine whether to tint the texture using a hardcoded tint index
             * <p>
             * The default is not using the tint, and any number causes it to use tint. Note that only certain blocks
             * have a tint index, all others will be unaffected
             */
            public void setTintindex(Integer tintindex) {
                this.tintindex = tintindex;
            }

            public FaceProperties withTintindex(Integer tintindex) {
                this.tintindex = tintindex;
                return this;
            }

            public enum Cullface {

                @SerializedName("down")
                DOWN("down"),
                @SerializedName("up")
                UP("up"),
                @SerializedName("east")
                EAST("east"),
                @SerializedName("west")
                WEST("west"),
                @SerializedName("south")
                SOUTH("south"),
                @SerializedName("north")
                NORTH("north");
                private final static Map<String, Cullface> CONSTANTS = new HashMap<String, Cullface>();

                static {
                    for (Cullface c : Cullface.values()) {
                        CONSTANTS.put(c.value, c);
                    }
                }

                private final String value;

                private Cullface(String value) {
                    this.value = value;
                }

                public static Cullface fromValue(String value) {
                    Cullface constant = CONSTANTS.get(value);
                    if (constant == null) {
                        throw new IllegalArgumentException(value);
                    } else {
                        return constant;
                    }
                }

                @Override
                public String toString() {
                    return this.value;
                }

                public String value() {
                    return this.value;
                }

            }

            public enum Rotation {

                @SerializedName("0")
                _0(0),
                @SerializedName("90")
                _90(90),
                @SerializedName("180")
                _180(180),
                @SerializedName("270")
                _270(270);
                private final static Map<Integer, Rotation> CONSTANTS = new HashMap<Integer, Rotation>();

                static {
                    for (Rotation c : Rotation.values()) {
                        CONSTANTS.put(c.value, c);
                    }
                }

                private final Integer value;

                private Rotation(Integer value) {
                    this.value = value;
                }

                public static Rotation fromValue(Integer value) {
                    Rotation constant = CONSTANTS.get(value);
                    if (constant == null) {
                        throw new IllegalArgumentException((value + ""));
                    } else {
                        return constant;
                    }
                }

                public Integer value() {
                    return this.value;
                }

            }

        }
    }

    /**
     * A single case
     * <p>
     */
    public static class ModelOverride {

        @SerializedName("predicate")
        @Expose
        private ModelPredicate predicate;
        @SerializedName("model")
        @Expose
        private Object model;

        public ModelPredicate getPredicate() {
            return predicate;
        }

        public void setPredicate(ModelPredicate predicate) {
            this.predicate = predicate;
        }

        public ModelOverride withPredicate(ModelPredicate predicate) {
            this.predicate = predicate;
            return this;
        }

        public Object getModel() {
            return model;
        }

        public void setModel(Object model) {
            this.model = model;
        }

        public ModelOverride withModel(Object model) {
            this.model = model;
            return this;
        }

        public static class ModelPredicate {

            /**
             * Used on compasses to determine the current angle
             * <p>
             * Expressed in a decimal value of less than one
             */
            @SerializedName("angle")
            @Expose
            private transient double angle;
            /**
             * Used on shields to determine if currently blocking
             * <p>
             */
            @SerializedName("blocking")
            @Expose
            private Blocking blocking;
            /**
             * Used on Elytra to determine if broken
             * <p>
             */
            @SerializedName("broken")
            @Expose
            private Broken broken;
            /**
             * Used on fishing rods to determine if the fishing rod has been cast
             * <p>
             */
            @SerializedName("cast")
            @Expose
            private Cast cast;
            /**
             * Used on ender pearls and chorus fruit to determine the remaining cooldown
             * <p>
             * Expressed in a decimal value of less than one
             */
            @SerializedName("cooldown")
            @Expose
            private transient double cooldown;
            /**
             * Used on items with durability to determine the amount of damage
             * <p>
             * Expressed in a decimal value of less than one
             */
            @SerializedName("damage")
            @Expose
            private double damage;
            /**
             * Used on items with durability to determine if it is damaged
             * <p>
             * If an item has the unbreakable tag, this may be 0 while the item has a non-zero 'damage' tag
             */
            @SerializedName("damaged")
            @Expose
            private Damaged damaged;
            /**
             * Determine the model used by left handed players
             * <p>
             */
            @SerializedName("lefthanded")
            @Expose
            private Lefthanded lefthanded;
            /**
             * Determine the amount a bow has been pulled
             * <p>
             * Expressed in a decimal value of less than one
             */
            @SerializedName("pull")
            @Expose
            private transient double pull;
            /**
             * Used on bows to determine if the bow is being pulled
             * <p>
             */
            @SerializedName("pulling")
            @Expose
            private Pulling pulling;
            /**
             * Used on the trident to determine if the trident is ready to be thrown by the player
             * <p>
             */
            @SerializedName("throwing")
            @Expose
            private Throwing throwing;
            /**
             * Used on clocks to determine the current time
             * <p>
             * Expressed in a decimal value of less than one
             */
            @SerializedName("time")
            @Expose
            private transient double time;
            /**
             * Used on any item to apply custom model
             * <p>
             */
            @SerializedName("custom_model_data")
            @Expose
            private Integer customModelData;

            /**
             * Used on compasses to determine the current angle
             * <p>
             * Expressed in a decimal value of less than one
             */
            public double getAngle() {
                return angle;
            }

            /**
             * Used on compasses to determine the current angle
             * <p>
             * Expressed in a decimal value of less than one
             */
            public void setAngle(double angle) {
                this.angle = angle;
            }

            public ModelPredicate withAngle(double angle) {
                this.angle = angle;
                return this;
            }

            /**
             * Used on shields to determine if currently blocking
             * <p>
             */
            public Blocking getBlocking() {
                return blocking;
            }

            /**
             * Used on shields to determine if currently blocking
             * <p>
             */
            public void setBlocking(Blocking blocking) {
                this.blocking = blocking;
            }

            public ModelPredicate withBlocking(Blocking blocking) {
                this.blocking = blocking;
                return this;
            }

            /**
             * Used on Elytra to determine if broken
             * <p>
             */
            public Broken getBroken() {
                return broken;
            }

            /**
             * Used on Elytra to determine if broken
             * <p>
             */
            public void setBroken(Broken broken) {
                this.broken = broken;
            }

            public ModelPredicate withBroken(Broken broken) {
                this.broken = broken;
                return this;
            }

            /**
             * Used on fishing rods to determine if the fishing rod has been cast
             * <p>
             */
            public Cast getCast() {
                return cast;
            }

            /**
             * Used on fishing rods to determine if the fishing rod has been cast
             * <p>
             */
            public void setCast(Cast cast) {
                this.cast = cast;
            }

            public ModelPredicate withCast(Cast cast) {
                this.cast = cast;
                return this;
            }

            /**
             * Used on ender pearls and chorus fruit to determine the remaining cooldown
             * <p>
             * Expressed in a decimal value of less than one
             */
            public double getCooldown() {
                return cooldown;
            }

            /**
             * Used on ender pearls and chorus fruit to determine the remaining cooldown
             * <p>
             * Expressed in a decimal value of less than one
             */
            public void setCooldown(double cooldown) {
                this.cooldown = cooldown;
            }

            public ModelPredicate withCooldown(double cooldown) {
                this.cooldown = cooldown;
                return this;
            }

            /**
             * Used on items with durability to determine the amount of damage
             * <p>
             * Expressed in a decimal value of less than one
             */
            public double getDamage() {
                return damage;
            }

            /**
             * Used on items with durability to determine the amount of damage
             * <p>
             * Expressed in a decimal value of less than one
             */
            public void setDamage(double damage) {
                this.damage = damage;
            }

            public ModelPredicate withDamage(double damage) {
                this.damage = damage;
                return this;
            }

            /**
             * Used on items with durability to determine if it is damaged
             * <p>
             * If an item has the unbreakable tag, this may be 0 while the item has a non-zero 'damage' tag
             */
            public Damaged getDamaged() {
                return damaged;
            }

            /**
             * Used on items with durability to determine if it is damaged
             * <p>
             * If an item has the unbreakable tag, this may be 0 while the item has a non-zero 'damage' tag
             */
            public void setDamaged(Damaged damaged) {
                this.damaged = damaged;
            }

            public ModelPredicate withDamaged(Damaged damaged) {
                this.damaged = damaged;
                return this;
            }

            /**
             * Determine the model used by left handed players
             * <p>
             */
            public Lefthanded getLefthanded() {
                return lefthanded;
            }

            /**
             * Determine the model used by left handed players
             * <p>
             */
            public void setLefthanded(Lefthanded lefthanded) {
                this.lefthanded = lefthanded;
            }

            public ModelPredicate withLefthanded(Lefthanded lefthanded) {
                this.lefthanded = lefthanded;
                return this;
            }

            /**
             * Determine the amount a bow has been pulled
             * <p>
             * Expressed in a decimal value of less than one
             */
            public double getPull() {
                return pull;
            }

            /**
             * Determine the amount a bow has been pulled
             * <p>
             * Expressed in a decimal value of less than one
             */
            public void setPull(double pull) {
                this.pull = pull;
            }

            public ModelPredicate withPull(double pull) {
                this.pull = pull;
                return this;
            }

            /**
             * Used on bows to determine if the bow is being pulled
             * <p>
             */
            public Pulling getPulling() {
                return pulling;
            }

            /**
             * Used on bows to determine if the bow is being pulled
             * <p>
             */
            public void setPulling(Pulling pulling) {
                this.pulling = pulling;
            }

            public ModelPredicate withPulling(Pulling pulling) {
                this.pulling = pulling;
                return this;
            }

            /**
             * Used on the trident to determine if the trident is ready to be thrown by the player
             * <p>
             */
            public Throwing getThrowing() {
                return throwing;
            }

            /**
             * Used on the trident to determine if the trident is ready to be thrown by the player
             * <p>
             */
            public void setThrowing(Throwing throwing) {
                this.throwing = throwing;
            }

            public ModelPredicate withThrowing(Throwing throwing) {
                this.throwing = throwing;
                return this;
            }

            /**
             * Used on clocks to determine the current time
             * <p>
             * Expressed in a decimal value of less than one
             */
            public double getTime() {
                return time;
            }

            /**
             * Used on clocks to determine the current time
             * <p>
             * Expressed in a decimal value of less than one
             */
            public void setTime(double time) {
                this.time = time;
            }

            public ModelPredicate withTime(double time) {
                this.time = time;
                return this;
            }

            /**
             * Used on any item to apply custom model
             * <p>
             */
            public Integer getCustomModelData() {
                return customModelData;
            }

            /**
             * Used on any item to apply custom model
             * <p>
             */
            public void setCustomModelData(Integer customModelData) {
                this.customModelData = customModelData;
            }

            public ModelPredicate withCustomModelData(Integer customModelData) {
                this.customModelData = customModelData;
                return this;
            }

            public enum Blocking {

                @SerializedName("0")
                _0(0),
                @SerializedName("1")
                _1(1);
                private final static Map<Integer, Blocking> CONSTANTS = new HashMap<Integer, Blocking>();

                static {
                    for (Blocking c : Blocking.values()) {
                        CONSTANTS.put(c.value, c);
                    }
                }

                private final Integer value;

                private Blocking(Integer value) {
                    this.value = value;
                }

                public static Blocking fromValue(Integer value) {
                    Blocking constant = CONSTANTS.get(value);
                    if (constant == null) {
                        throw new IllegalArgumentException((value + ""));
                    } else {
                        return constant;
                    }
                }

                public Integer value() {
                    return this.value;
                }

            }

            public enum Broken {

                @SerializedName("0")
                _0(0),
                @SerializedName("1")
                _1(1);
                private final static Map<Integer, Broken> CONSTANTS = new HashMap<Integer, Broken>();

                static {
                    for (Broken c : Broken.values()) {
                        CONSTANTS.put(c.value, c);
                    }
                }

                private final Integer value;

                private Broken(Integer value) {
                    this.value = value;
                }

                public static Broken fromValue(Integer value) {
                    Broken constant = CONSTANTS.get(value);
                    if (constant == null) {
                        throw new IllegalArgumentException((value + ""));
                    } else {
                        return constant;
                    }
                }

                public Integer value() {
                    return this.value;
                }

            }

            public enum Cast {

                @SerializedName("0")
                _0(0),
                @SerializedName("1")
                _1(1);
                private final static Map<Integer, Cast> CONSTANTS = new HashMap<Integer, Cast>();

                static {
                    for (Cast c : Cast.values()) {
                        CONSTANTS.put(c.value, c);
                    }
                }

                private final Integer value;

                private Cast(Integer value) {
                    this.value = value;
                }

                public static Cast fromValue(Integer value) {
                    Cast constant = CONSTANTS.get(value);
                    if (constant == null) {
                        throw new IllegalArgumentException((value + ""));
                    } else {
                        return constant;
                    }
                }

                public Integer value() {
                    return this.value;
                }

            }

            public enum Damaged {

                @SerializedName("0")
                _0(0),
                @SerializedName("1")
                _1(1);
                private final static Map<Integer, Damaged> CONSTANTS = new HashMap<Integer, Damaged>();

                static {
                    for (Damaged c : Damaged.values()) {
                        CONSTANTS.put(c.value, c);
                    }
                }

                private final Integer value;

                private Damaged(Integer value) {
                    this.value = value;
                }

                public static Damaged fromValue(Integer value) {
                    Damaged constant = CONSTANTS.get(value);
                    if (constant == null) {
                        throw new IllegalArgumentException((value + ""));
                    } else {
                        return constant;
                    }
                }

                public Integer value() {
                    return this.value;
                }

            }

            public enum Lefthanded {

                @SerializedName("0")
                _0(0),
                @SerializedName("1")
                _1(1);
                private final static Map<Integer, Lefthanded> CONSTANTS = new HashMap<Integer, Lefthanded>();

                static {
                    for (Lefthanded c : Lefthanded.values()) {
                        CONSTANTS.put(c.value, c);
                    }
                }

                private final Integer value;

                private Lefthanded(Integer value) {
                    this.value = value;
                }

                public static Lefthanded fromValue(Integer value) {
                    Lefthanded constant = CONSTANTS.get(value);
                    if (constant == null) {
                        throw new IllegalArgumentException((value + ""));
                    } else {
                        return constant;
                    }
                }

                public Integer value() {
                    return this.value;
                }

            }

            public enum Pulling {

                @SerializedName("0")
                _0(0),
                @SerializedName("1")
                _1(1);
                private final static Map<Integer, Pulling> CONSTANTS = new HashMap<Integer, Pulling>();

                static {
                    for (Pulling c : Pulling.values()) {
                        CONSTANTS.put(c.value, c);
                    }
                }

                private final Integer value;

                private Pulling(Integer value) {
                    this.value = value;
                }

                public static Pulling fromValue(Integer value) {
                    Pulling constant = CONSTANTS.get(value);
                    if (constant == null) {
                        throw new IllegalArgumentException((value + ""));
                    } else {
                        return constant;
                    }
                }

                public Integer value() {
                    return this.value;
                }

            }

            public enum Throwing {

                @SerializedName("0")
                _0(0),
                @SerializedName("1")
                _1(1);
                private final static Map<Integer, Throwing> CONSTANTS = new HashMap<Integer, Throwing>();

                static {
                    for (Throwing c : Throwing.values()) {
                        CONSTANTS.put(c.value, c);
                    }
                }

                private final Integer value;

                private Throwing(Integer value) {
                    this.value = value;
                }

                public static Throwing fromValue(Integer value) {
                    Throwing constant = CONSTANTS.get(value);
                    if (constant == null) {
                        throw new IllegalArgumentException((value + ""));
                    } else {
                        return constant;
                    }
                }

                public Integer value() {
                    return this.value;
                }

            }

        }
    }
}
