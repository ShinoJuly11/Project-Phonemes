import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;

public class TarsosDSPBufferCollector implements AudioProcessor {

    private final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    private final boolean bigEndian;
    private final int overlap; // in samples
    private float[] overlapBuffer = null;
    private boolean isFirstBlock = true;

    public TarsosDSPBufferCollector(boolean bigEndian, int overlap){
        this.bigEndian = bigEndian;
        this.overlap = overlap;
    }

    @Override
    public boolean process(AudioEvent audioEvent){
        float[] floatBuffer = audioEvent.getFloatBuffer();
        int blockSize = floatBuffer.length;

        // Prepare output buffer for this block
        float[] outputBlock;
        int outputLen;

        if (isFirstBlock) {
            // First block: output as is, but save overlap region
            outputBlock = floatBuffer.clone();
            outputLen = blockSize;
            isFirstBlock = false;
        } else {
            // Overlap-add: sum overlapBuffer with start of this block
            outputBlock = new float[blockSize - overlap];
            // Add overlap
            for (int i = 0; i < overlap; i++) {
                floatBuffer[i] += overlapBuffer[i];
            }
            // Copy non-overlapping part
            System.arraycopy(floatBuffer, overlap, outputBlock, 0, blockSize - overlap);
            outputLen = blockSize - overlap;
        }

        // Save new overlap region for next block
        overlapBuffer = new float[overlap];
        System.arraycopy(floatBuffer, blockSize - overlap, overlapBuffer, 0, overlap);

        // Convert outputBlock to PCM and write
        ByteBuffer bb = ByteBuffer.allocate(2 * outputLen)
            .order(this.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < outputLen; i++) {
            int intSample = Math.max(-32768, Math.min(32767, (int)(outputBlock[i] * 32767.0f)));
            bb.putShort((short) intSample);
        }

        try {
            byteOut.write(bb.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void processingFinished(){
        // Optionally, write the remaining overlapBuffer at the end
        if (overlapBuffer != null) {
            ByteBuffer bb = ByteBuffer.allocate(2 * overlap)
                .order(this.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
            for (int i = 0; i < overlap; i++) {
                int intSample = Math.max(-32768, Math.min(32767, (int)(overlapBuffer[i] * 32767.0f)));
                bb.putShort((short) intSample);
            }
            try {
                byteOut.write(bb.array());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getBytes() {
        return byteOut.toByteArray();
    }
}
