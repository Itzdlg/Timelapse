/*
 * This file is part of Timelapse.
 *
 * Timelapse is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software
 * Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Timelapse is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with Timelapse in LICENSE.txt. If not, see
 *      <https://www.gnu.org/licenses/gpl-3.0.en.html>
 */

package me.schooltests.timelapse.common.buffer;

import me.schooltests.timelapse.common.error.BufferOverflowError;
import java.io.OutputStream;

/**
 * A buffered byte writer which, upon reaching
 * the maximum capacity, will output the buffer
 * to a file asynchronously.
 *
 * @since 0.1.0
 * @author Timelapse Team and contributors
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public class ReallocatingBuffer extends OutputStream {
    protected byte[] buffer;

    /**
     * The index of the last written byte to the buffer.
     * Starts at -1, and will increment for each byte
     * written to the buffer.
     */
    protected int position = -1;

    /**
     * The maximum allocation, in memory, of the buffer.
     */
    protected int maximumCapacity;

    /**
     * The initial size of the buffer once first initialized.
     */
    protected final int startingCapacity;
    /**
     * The number added to the previous buffer's capacity for
     * reallocation. Used to lessen the number of calls made
     * to {@link System#arraycopy(Object, int, Object, int, int)}
     * if the buffer is being written frequently. Note that the
     * larger this number is, the more memory is allocated each time
     * the buffer reaches its allocated capacity.
     */
    protected final int capacityIncrement;

    /**
     * Initializes the buffered writer with the provided
     * maximum capacity, starting capacity, and capacity
     * increment.
     *
     * @param maximumCapacity the maximum allocation of the buffer
     *                        before being reset and written to
     *                        the {@code file}.
     * @param startingCapacity the initial capacity of the buffer.
     * @param capacityIncrement the addend used for reallocating the
     *                          buffer with a new capacity.
     *
     * @throws IllegalArgumentException if {@code maximumCapacity}
     * is not greater than {@code startingCapacity}, or if
     * {@code initialCapacity} is less than or equal to 0.
     *
     * @see #ensureCapacity(long)
     */
    public ReallocatingBuffer(int maximumCapacity, int startingCapacity, int capacityIncrement)
            throws IllegalArgumentException {
        this.maximumCapacity = maximumCapacity;
        this.startingCapacity = startingCapacity;
        this.capacityIncrement = capacityIncrement;

        if (maximumCapacity <= startingCapacity)
            throw new IllegalArgumentException("You can not set a maximum capacity less than the starting capacity.");

        if (capacityIncrement <= 0)
            throw new IllegalArgumentException("You can not set the capacity increment to a number less than 1.");

        this.buffer = new byte[startingCapacity];
    }

    /**
     * Ensures that the buffer will be able to load the specified
     * number of bytes into memory by reallocating the buffer if
     * necessary.
     *
     * If the capacity required exceeds the maximum capacity, then
     * an error will be thrown. The buffer will never exceed the
     * maximum capacity.
     *
     * @param capacity the number of bytes required in the buffer.
     */
    protected void ensureCapacity(long capacity) {
        if (size() + capacity <= buffer.length)
            return;

        if (capacity > maximumCapacity)
            throw new BufferOverflowError("You may not add " + capacity + " bytes to the buffer as it exceeds the maximum capacity.");

        long newCapacity = buffer.length;
        do newCapacity += capacityIncrement;
        while (newCapacity < size() + capacity);

        int cappedCapacity = (int) Math.min(newCapacity, maximumCapacity);

        byte[] newBuffer = new byte[cappedCapacity];
        System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
        buffer = newBuffer;
    }

    @Override
    public void write(int b) {
        write(new byte[] { (byte) b });
    }

    /**
     * Writes the {@code arr} to the buffer through the
     * {@link System#arraycopy(Object, int, Object, int, int)} method,
     * then increments the position to the index of the last byte
     * written in the buffer.
     *
     * @param arr the bytes to append to the buffer.
     */
    @Override
    public void write(byte[] arr) {
        if (size() + arr.length > maximumCapacity)
            throw new BufferOverflowError("You may not write " + arr.length + " bytes to the buffer, as it is more than the maximum capacity.");

        ensureCapacity(arr.length);
        System.arraycopy(arr, 0, buffer, position + 1, arr.length);
        position = position + arr.length;
    }

    /**
     * Changes the maximum capacity of the buffer.
     *
     * This restriction may not limit the
     * number of bytes written in total, only the
     * maximum allocation of the buffer in memory.
     *
     * @param maximumCapacity the new maximum capacity of the
     *                        buffer.
     * @throws IllegalArgumentException if the maximum capacity
     * provided is not greater than {@link #startingCapacity}.
     */
    public final void setMaximumCapacity(int maximumCapacity) throws IllegalArgumentException {
        if (maximumCapacity <= startingCapacity)
            throw new IllegalArgumentException("You may not set a maximum capacity less than the starting capacity.");

        this.maximumCapacity = maximumCapacity;
    }

    /**
     * Reassigns the buffer variable to a new buffer of
     * the starting capacity. Also resets the position of
     * the buffer to -1, which will be incremented upon
     * calling {@link #write(byte[])} with a non-empty array.
     */
    public void clear() {
        buffer = new byte[startingCapacity];
        position = -1;
    }

    /**
     * @return the number of bytes written to the buffer
     * since the last flush.
     */
    public final int size() {
        return position + 1;
    }
}
