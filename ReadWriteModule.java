
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadWriteModule {

    public static BufferedImage processJPG(String path) throws IOException {
        File file = new File(path);
        BufferedImage img;
        img = ImageIO.read(file);
        return img;
    }

    public static float[] processDAT(String path) throws IOException {
        ByteBuffer bb = ByteBuffer.wrap(Files.readAllBytes(Paths.get(path)));
        FloatBuffer fb = bb.asFloatBuffer();

        float[] floatArray = new float[fb.limit()];
        fb.get(floatArray);

        return floatArray;
    }

    public static BufferedImage processXCR(String path, String name) throws IOException {
        ByteBuffer bb = ByteBuffer.wrap(Files.readAllBytes(Paths.get(path)));
        byte[] arr = bb.array();
        BufferedImage img = new BufferedImage(1024,1024,BufferedImage.TYPE_USHORT_GRAY);

        short[] pixels = ((DataBufferUShort) img.getRaster().getDataBuffer()).getData();
        int offset = arr.length/2 - pixels.length;

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = (short) (arr[offset + i * 2] + arr[offset + i * 2 + 1]);
        }
        return img;
    }
}
