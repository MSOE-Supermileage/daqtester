package smv.msoe.edu.daqtester;

import android.os.Bundle;
import android.util.JsonReader;

import java.io.IOException;
import java.util.List;


/**
 * Created by austin on 1/31/16.
 */
public class Utility {

    private Utility() {}

    public static Bundle parseDataNode(JsonReader reader) throws IOException {
        // Bundle is effectively a hashmap - the view can seek what it wants
        Bundle data = new Bundle();

        reader.beginObject();
        while (reader.hasNext()) {
            data.putDouble(reader.nextName(), reader.nextDouble());
        }
        reader.endObject();

        return data;
    }

    public static long execWithTimer(Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        return System.currentTimeMillis() - startTime;
    }

    public static String stringJoin(List<String> args, String delimiter) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.size() - 1; i++) {
            builder.append(args.get(i));
            builder.append(delimiter);
        }
        builder.append(args.get(args.size() - 1));
        return builder.toString();
    }
}
