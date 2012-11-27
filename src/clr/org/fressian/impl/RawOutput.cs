//   Copyright (c) Metadata Partners, LLC. All rights reserved.
//   The use and distribution terms for this software are covered by the
//   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
//   which can be found in the file epl-v10.html at the root of this distribution.
//   By using this software in any fashion, you are agreeing to be bound by
//   the terms of this license.
//   You must not remove this notice, or any other, from this software.

using System;
using System.IO;

using org.fressian;

namespace org.fressian.impl
{    
    public class RawOutput : IDisposable
    {
        private readonly CheckedStream os;
        private int bytesWritten;

        public RawOutput(Stream os)
        {
            this.os = new CheckedStream(os, new Adler32());
        }

        public void writeRawByte(int b)
        {
            os.WriteByte((byte)b);
            notifyBytesWritten(1);
        }

        public void writeRawInt16(int s)
        {
            os.WriteByte((byte)(((uint)s >> 8) & 0xFF));
            os.WriteByte((byte)(s & 0xFF));
            notifyBytesWritten(2);
        }

        public void writeRawInt24(int i)
        {
            os.WriteByte((byte)(((uint)i >> 16) & 0xFF));
            os.WriteByte((byte)(((uint)i >> 8) & 0xFF));
            os.WriteByte((byte)(i & 0xFF));
            notifyBytesWritten(3);
        }

        public void writeRawInt32(int i)
        {
            os.WriteByte((byte)(((uint)i >> 24) & 0xFF));
            os.WriteByte((byte)(((uint)i >> 16) & 0xFF));
            os.WriteByte((byte)(((uint)i >> 8) & 0xFF));
            os.WriteByte((byte)((uint)i & 0xFF));
            notifyBytesWritten(4);
        }

        public void writeRawInt40(long i)
        {
            os.WriteByte((byte)(((ulong)i >> 32) & 0xFF));
            os.WriteByte((byte)(((ulong)i >> 24) & 0xFF));
            os.WriteByte((byte)(((ulong)i >> 16) & 0xFF));
            os.WriteByte((byte)(((ulong)i >> 8) & 0xFF));
            os.WriteByte((byte)((ulong)i & 0xFF));
            notifyBytesWritten(5);
        }

        public void writeRawInt48(long i)
        {
            os.WriteByte((byte)(((ulong)i >> 40) & 0xFF));
            os.WriteByte((byte)(((ulong)i >> 32) & 0xFF));
            os.WriteByte((byte)(((ulong)i >> 24) & 0xFF));
            os.WriteByte((byte)(((ulong)i >> 16) & 0xFF));
            os.WriteByte((byte)(((ulong)i >> 8) & 0xFF));
            os.WriteByte((byte)((ulong)i & 0xFF));
            notifyBytesWritten(6);
        }

        byte[] buffer = new byte[8];
        
        public void writeRawInt64(long l)
        {
            buffer[0] = (byte)((ulong)l >> 56);
            buffer[1] = (byte)((ulong)l >> 48);
            buffer[2] = (byte)((ulong)l >> 40);
            buffer[3] = (byte)((ulong)l >> 32);
            buffer[4] = (byte)((ulong)l >> 24);
            buffer[5] = (byte)((ulong)l >> 16);
            buffer[6] = (byte)((ulong)l >> 8);
            buffer[7] = (byte)((ulong)l >> 0);
            os.Write(buffer, 0, 8);
            notifyBytesWritten(8);
        }

        public void writeRawDouble(double d)
        {
            writeRawInt64(BitConverter.DoubleToInt64Bits(d));
        }

        public void writeRawFloat(float f)
        {
            writeRawInt32(Fns.SingleToInt32Bits(f));
        }

        public void writeRawBytes(byte[] bytes, int off, int len)
        {
            os.Write(bytes, off, len);
            notifyBytesWritten(len);
        }

        public Checksum getChecksum()
        {
            return os.GetChecksum();
        }

        public int getBytesWritten()
        {
            return bytesWritten;
        }

        public void reset()
        {
            bytesWritten = 0;
            getChecksum().Reset();
        }

        private void notifyBytesWritten(int count)
        {
            bytesWritten = bytesWritten + count;
        }

        //public void close()
        //{
        //    os.Close();
        //}

        public void Dispose()
        {
            os.Close();
        }
    }
}