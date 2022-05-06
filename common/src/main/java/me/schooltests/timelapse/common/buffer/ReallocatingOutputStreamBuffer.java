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

import java.io.Flushable;
import java.io.IOException;
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
public class ReallocatingOutputStreamBuffer extends ReallocatingBuffer implements Flushable {
    /**
     * After calling {@link #flush()}, or under certain
     * conditions of {@link #ensureCapacity(long)},
     * {@link OutputStream#write(byte[])} is invoked, with
     * the buffer as the parameter, before clearing it from
     * memory.
     *
     * @see #flush()
     */
    protected final OutputStream outputStream;

    /**
     * Initializes the buffered writer with the provided flush
     * file, maximum capacity, starting capacity, and capacity
     * increment.
     *
     * Adds a shutdown hook to the JVM to call {@link #close()}
     * to flush the buffer.
     *
     * @param outputStream the output stream to flush the buffer to.
     * @param maximumCapacity the maximum allocation of the buffer
     *                        before being reset and written to
     *                        the {@code file}.
     * @param startingCapacity the initial capacity of the buffer.
     * @param capacityIncrement the addend used for reallocating the
     *                          buffer with a new capacity.

     * @throws IllegalArgumentException if {@code maximumCapacity}
     * is not greater than {@code startingCapacity}, or if
     * {@code capacityIncrement} is less than or equal to 0.
     *
     * @see #ensureCapacity(long)
     * @see #flush()
     */
    public ReallocatingOutputStreamBuffer(OutputStream outputStream, int maximumCapacity, int startingCapacity, int capacityIncrement)
            throws IllegalArgumentException {
        super(maximumCapacity, startingCapacity, capacityIncrement);
        this.outputStream = outputStream;

        if (maximumCapacity <= startingCapacity)
            throw new IllegalArgumentException("You can not set a maximum capacity less than the starting capacity.");

        if (capacityIncrement <= 0)
            throw new IllegalArgumentException("You can not set the capacity increment to a number less than 1.");
    }

    /**
     * Ensures that the buffer has enough capacity to add the
     * number of bytes indicated in {@code capacity}. If the
     * buffer will not be able to hold the specified number of
     * bytes, it will first flush the remaining buffer before
     * resizing.
     *
     * If the capacity required is greater than the maximum
     * capacity, then the buffer will NOT be able to write
     * the number of bytes requested, the buffer will be
     * limited to the maximum capacity.
     *
     * @param capacity the number of bytes required in the buffer.
     */
    @Override
    protected void ensureCapacity(long capacity) {
        if (size() + capacity > maximumCapacity) {
            try {
                flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return;
        }

        super.ensureCapacity(capacity);
    }

    /**
     * Writes the buffer to the output stream, and then reallocates
     * the buffer.
     */
    @Override
    public void flush() throws IOException {
        outputStream.write(buffer, 0, position + 1);
        clear();
    }

    /**
     * Writes the {@code arr} to the buffer, unless
     * the array's length is greater than the
     * maximum capacity of the array, which will
     * result in writing directly to the output
     * stream after flushing the remaining buffer.
     *
     * @param arr the bytes to append to the buffer.
     */
    @Override
    public void write(byte[] arr) {
        if (arr.length > maximumCapacity) {
            try {
                flush();
                outputStream.write(arr);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return;
        }

        ensureCapacity(arr.length);
        System.arraycopy(arr, 0, buffer, position + 1, arr.length);
        position = position + arr.length;
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
}
