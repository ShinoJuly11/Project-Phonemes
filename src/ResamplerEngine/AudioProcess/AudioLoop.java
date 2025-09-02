package ResamplerEngine.AudioProcess;
import javax.sound.sampled.AudioFormat;

import NoteEditor.TableEditor2.Note;
import ResamplerEngine.Phoneme;

public class AudioLoop implements AudioProcess {

    Note note;
    AudioFormat audioFormat;
    int ticksPerBeat;

    public AudioLoop(int ticksPerBeat){
        this.ticksPerBeat = ticksPerBeat;
    }

    @Override
    public byte[] run(byte[] byteStream) throws Exception {
        int frameLength = byteStream.length / audioFormat.getFrameSize();
        int bpm = 120; //PLEASE GET RID OF THIS SOON
        long targetFrameLength = desiredLengthCalculator(audioFormat, note.getNoteLength(), frameLength , bpm, ticksPerBeat);
        if (targetFrameLength != frameLength){
            byte[] processedBytes = process(byteStream, note.getPhoneme(), targetFrameLength);
            return processedBytes;
        }
        else{
            return byteStream;

        }
        //wtf am i doing;
        
    }

    private long desiredLengthCalculator(AudioFormat af, int tickLength, int frameLength, int bpm, int ticksPerBeat){
        int tickDuration = (60_000_000 / (bpm * ticksPerBeat));

        // long microSeconds = tickLength * tickDuration;
        // long frameMicroSeconds = (long) Math.abs(frameLength / af.getSampleRate());
        //i never used a balancing equation since A-levels

        float desiredFrameLength =  Math.abs(((tickLength * tickDuration) / 1_000_000) * af.getSampleRate()) / audioFormat.getFrameSize();
        System.out.println("resultedLoop =" + desiredFrameLength);
        
        return (long) desiredFrameLength + frameLength;
        
        //get the desiredLength from the note

    }

    public byte[] process(byte[] byteStream, Phoneme phoneme, long desiredLength){

    int frameSize = audioFormat.getFrameSize();
    int audioLoopStart = phoneme.getConsonant();
    int audioLoopEnd = phoneme.getPreuttrance();

    byte[] loopedByteStream = getAudioLoopByteStream(byteStream, audioLoopStart, audioLoopEnd, frameSize);
    long calcResultLength = getNumLoops(byteStream, loopedByteStream, desiredLength, frameSize);
    byte[] resultedLoop = concatLoops(phoneme, byteStream, loopedByteStream, calcResultLength);
    
    System.out.println("looped bytestream =" + loopedByteStream.length);
    System.out.println("resultedLoop =" + resultedLoop.length);
    

    return resultedLoop;

    }

    private byte[] getAudioLoopByteStream(byte[] byteStream, int start, int end, int frameSize){

        byte[] tempByteStream = byteStream.clone();
        int numFrames = end - start;
        int numBytes = numFrames * frameSize;
        byte[] newByteStream = new byte[numBytes];

        // Copy the correct slice of the byte array
        System.arraycopy(
            tempByteStream,
            start * frameSize, // source start in bytes
            newByteStream,
            0,             // destination start at 0
            numBytes               // total bytes to copy
        );

        return newByteStream;


    }

    private long getNumLoops(byte[] original, byte[] looped, long desiredFrameLength, int frameSize){

        int numFrames1 = original.length / frameSize;
        int numFrames2 = looped.length / frameSize;
        long tempLength = desiredFrameLength - numFrames1;

        if (numFrames2 == 0){
            System.out.println("num2 frame is 0");
            return 0;
        }
        
        long calcResultLength = tempLength / numFrames2;

        if (tempLength <= 0) return 0;
        
        
        return calcResultLength;

    }

    private byte[] concatLoops(Phoneme phoneme, byte[] original, byte[] looped, long numLoops){
    byte[] choppedOriginal = getAudioLoopByteStream(original, 0, phoneme.getConsonant(), audioFormat.getFrameSize());
    byte[] choppedEndOriginal = getAudioLoopByteStream(original, phoneme.getPreuttrance(),original.length / audioFormat.getFrameSize(), audioFormat.getFrameSize());
    byte[] newConcatLoop = new byte[(int) (choppedOriginal.length + (looped.length * numLoops) + choppedEndOriginal.length)];

    // Copy the original array
    System.arraycopy(choppedOriginal, 0, newConcatLoop, 0, choppedOriginal.length);

    // Copy the looped array numLoops times
    for (int i = 0; i < numLoops; i++) {
        System.arraycopy(looped, 0, newConcatLoop, choppedOriginal.length + i * looped.length, looped.length);
    }

    System.arraycopy(choppedEndOriginal, 0, newConcatLoop, (int) (choppedOriginal.length + numLoops * looped.length), choppedEndOriginal.length);

    return newConcatLoop;
    }

    @Override
    public void addNote(Note note) {
        this.note = note;
    }

    @Override
    public void addAudioFormat(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
    }

    




    


    
}
