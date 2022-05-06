package me.schooltests.timelapse.common.buffer;

import me.schooltests.timelapse.common.error.BufferOverflowError;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ReallocatingBufferTests {
    // Tests should these values unless the test requires a different value.
    private static final int MAXIMUM_CAPACITY = Short.MAX_VALUE;
    private static final int STARTING_CAPACITY = 8;
    private static final int CAPACITY_INCREMENT = 16;

    public static ReallocatingBuffer defaultBuffer() {
        return new ReallocatingBuffer(MAXIMUM_CAPACITY, STARTING_CAPACITY, CAPACITY_INCREMENT);
    }

    @Test
    public void ReallocatingBuffer_MaximumCapacityLessThanStartingCapacity_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ReallocatingBuffer(
                0,
                STARTING_CAPACITY,
                CAPACITY_INCREMENT
        ));
    }

    @Test
    public void ReallocatingBuffer_CapacityIncrementLessThan1_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new ReallocatingBuffer(
                MAXIMUM_CAPACITY,
                STARTING_CAPACITY,
                0
        ));
    }

    @Test
    public void ReallocatingBuffer_EmptyBufferNoAddend_DoesNotReallocate() {
        final ReallocatingBuffer buffer = defaultBuffer();
        buffer.ensureCapacity(0);

        assertEquals(STARTING_CAPACITY, buffer.buffer.length);
    }

    @Test
    public void ReallocatingBuffer_EmptyBufferWithAddend_Reallocates() {
        final ReallocatingBuffer buffer = defaultBuffer();
        buffer.ensureCapacity(STARTING_CAPACITY + 1);

        assertEquals(STARTING_CAPACITY + CAPACITY_INCREMENT, buffer.buffer.length);
    }

    @Test
    public void ReallocatingBuffer_EmptyBufferWithLargeAddend_Reallocates() {
        final ReallocatingBuffer buffer = defaultBuffer();
        buffer.ensureCapacity(STARTING_CAPACITY + CAPACITY_INCREMENT * 3 + 1);

        assertEquals(STARTING_CAPACITY + CAPACITY_INCREMENT * 4, buffer.buffer.length);
    }

    @Test
    public void ReallocatingBuffer_EmptyBuffer_DefaultPosition() {
        final ReallocatingBuffer buffer = defaultBuffer();
        assertEquals(-1, buffer.position);
    }

    @Test
    public void ReallocatingBuffer_EmptyBuffer_NoSize() {
        final ReallocatingBuffer buffer = defaultBuffer();
        assertEquals(0, buffer.size());
    }

    @Test
    public void ReallocatingBuffer_FullBuffer_CorrectSize() {
        final ReallocatingBuffer buffer = defaultBuffer();
        
        final byte[] arr = new byte[84];
        buffer.write(arr);

        assertEquals(arr.length, buffer.size());
    }

    @Test
    public void ReallocatingBuffer_WriteToMaxBuffer_ThrowsBufferOverflowError() {
        final ReallocatingBuffer buffer = defaultBuffer();
        buffer.write(new byte[MAXIMUM_CAPACITY]);

        assertThrows(BufferOverflowError.class, () -> buffer.write(new byte[1]));
    }
}
