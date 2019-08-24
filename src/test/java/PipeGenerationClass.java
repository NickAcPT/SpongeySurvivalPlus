import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.spongepowered.api.util.Direction;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PipeGenerationClass {
    public static void main(String[] args) {
        try {
            new PipeGenerationClass().process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static final Direction[] blockDirections = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST,
            Direction.EAST, Direction.UP, Direction.DOWN};

    private void process() throws IOException {

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        Stream<List<Direction>> of =
                IntStream.range(1, blockDirections.length + 2).mapToObj(n -> Combinations.combination(Arrays.asList(blockDirections), n)).flatMap(Collection::stream);
        of.forEach(directionStream -> {
            JsonObject originalPipe = gson.fromJson(readFile("wire-empty.json"), JsonObject.class);
            final List<Direction> directions = new ArrayList<>(directionStream);

            directions.forEach(dir -> {
                if (dir.isUpright())
                    dir = dir.getOpposite();
                JsonObject pipeSide = gson.fromJson(readFile("wire-" + dir.name().toLowerCase().charAt(0) + ".json"),
                        JsonObject.class);
                originalPipe.getAsJsonArray("elements").addAll(pipeSide.getAsJsonArray("elements"));

            });
            final String name =
                    directions.stream().map(d -> d.name().toLowerCase().charAt(0) + "").collect(Collectors.joining(""));
            System.out.println("registerCustomWire(\"" + name + "\")");
            try {
                Files.write(new File("wire-" + name + ".json").toPath(),
                        gson.toJson(originalPipe).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String readFile(String path) {
        byte[] encoded;
        try {
            encoded = Files.readAllBytes(new File(getClass().getResource(path).getPath()).toPath());
        } catch (IOException e) {
            return "";
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
