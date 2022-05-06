package me.schooltests.timelapse.common.buffer;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;

public class ReallocatingOutputStreamBufferTests {
    // Tests should these values unless the test requires a different value.
    private static final int MAXIMUM_CAPACITY = Short.MAX_VALUE;
    private static final int STARTING_CAPACITY = 8;
    private static final int CAPACITY_INCREMENT = 16;

    private static ReallocatingOutputStreamBuffer defaultBuffer(OutputStream stream) {
        return new ReallocatingOutputStreamBuffer(
                stream,
                MAXIMUM_CAPACITY,
                STARTING_CAPACITY,
                CAPACITY_INCREMENT
        );
    }

    @Test
    public void ReallocatingOutputStreamBuffer_WriteToMaxBuffer_Flushes() {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(MAXIMUM_CAPACITY + 1);
        final ReallocatingOutputStreamBuffer buffer = defaultBuffer(outputStream);

        buffer.write(new byte[MAXIMUM_CAPACITY]);
        buffer.write(new byte[1]);

        assertEquals(outputStream.size(), MAXIMUM_CAPACITY);
    }

    @Test
    public void ReallocatingOutputStreamBuffer_WriteBeyondMaxBuffer_SkipBuffer() {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(MAXIMUM_CAPACITY * 3);
        final ReallocatingOutputStreamBuffer buffer = defaultBuffer(outputStream);

        buffer.write(new byte[MAXIMUM_CAPACITY * 2]);
        assertEquals(outputStream.size(), MAXIMUM_CAPACITY * 2);
    }

    @Test
    public void ReallocatingOutputStreamBuffer_WriteBeyondMaxBufferWithFullBuffer_FlushesThenSkipBuffer() {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(MAXIMUM_CAPACITY * 3);
        final ReallocatingOutputStreamBuffer buffer = defaultBuffer(outputStream);

        buffer.write(new byte[STARTING_CAPACITY]);
        buffer.write(new byte[MAXIMUM_CAPACITY * 2]);

        assertEquals(outputStream.size(), STARTING_CAPACITY + MAXIMUM_CAPACITY * 2);
    }

    @Test
    public void ReallocatingOutputStreamBuffer_WriteLessThanMaxCapacity_DoesNotFlush() {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(MAXIMUM_CAPACITY + 1);
        final ReallocatingOutputStreamBuffer buffer = defaultBuffer(outputStream);
        buffer.write(new byte[STARTING_CAPACITY - 1]);
        assertEquals(outputStream.size(), 0);
    }
}
