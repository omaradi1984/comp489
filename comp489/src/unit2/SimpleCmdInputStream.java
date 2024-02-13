package unit2;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class SimpleCmdInputStream implements DataInputStream{

	public SimpleCmdInputStream(InputStream in) {
		super();
	}

	@SuppressWarnings("deprecation")
	public String readString() throws IOException {
		StringBuffer strBuf = new StringBuffer();
		boolean hitSpace = false;
		while (!hitSpace) {
			char c = read_char();
			hitSpace = Character.isSpace(c);
			if (!hitSpace)
				strBuf.append(c);
		}

		String str = new String(strBuf);
		return str;
	}

	public SimpleCmd readCommand() throws IOException {
		SimpleCmd cmd;
		String commStr = readString();
		if(commStr.compareTo("HEAD")==0)
			cmd = new HeadCmd(readString());
		else if (commStr.compareTo("GET")==0)
			cmd = new GetCmd(readString());
		else if(commStr.compareTo("POST")==0)
			cmd = new PostCmd(readString());
		else if (commStr.compareTo("DONE")==0)
			cmd = new DoneCmd(readString());
		else
			throw new IOException("unknown command.");

		return cmd;
	}

	public String[] _truncatable_ids() {
		// TODO Auto-generated method stub
		return null;
	}

	public Any read_any() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean read_boolean() {
		// TODO Auto-generated method stub
		return false;
	}

	public char read_char() {
		// TODO Auto-generated method stub
		return 0;
	}

	public char read_wchar() {
		// TODO Auto-generated method stub
		return 0;
	}

	public byte read_octet() {
		// TODO Auto-generated method stub
		return 0;
	}

	public short read_short() {
		// TODO Auto-generated method stub
		return 0;
	}

	public short read_ushort() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int read_long() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int read_ulong() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long read_longlong() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long read_ulonglong() {
		// TODO Auto-generated method stub
		return 0;
	}

	public float read_float() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double read_double() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String read_string() {
		// TODO Auto-generated method stub
		return null;
	}

	public String read_wstring() {
		// TODO Auto-generated method stub
		return null;
	}

	public org.omg.CORBA.Object read_Object() {
		// TODO Auto-generated method stub
		return null;
	}

	public java.lang.Object read_Abstract() {
		// TODO Auto-generated method stub
		return null;
	}

	public Serializable read_Value() {
		// TODO Auto-generated method stub
		return null;
	}

	public TypeCode read_TypeCode() {
		// TODO Auto-generated method stub
		return null;
	}

	public void read_any_array(AnySeqHolder seq, int offset, int length) {
		// TODO Auto-generated method stub

	}

	public void read_boolean_array(BooleanSeqHolder seq, int offset, int length) {
		// TODO Auto-generated method stub

	}

	public void read_char_array(CharSeqHolder seq, int offset, int length) {
		// TODO Auto-generated method stub

	}

	public void read_wchar_array(WCharSeqHolder seq, int offset, int length) {
		// TODO Auto-generated method stub

	}

	public void read_octet_array(OctetSeqHolder seq, int offset, int length) {
		// TODO Auto-generated method stub

	}

	public void read_short_array(ShortSeqHolder seq, int offset, int length) {
		// TODO Auto-generated method stub

	}

	public void read_ushort_array(UShortSeqHolder seq, int offset, int length) {
		// TODO Auto-generated method stub

	}

	public void read_long_array(LongSeqHolder seq, int offset, int length) {
		// TODO Auto-generated method stub

	}

	public void read_ulong_array(ULongSeqHolder seq, int offset, int length) {
		// TODO Auto-generated method stub

	}

	public void read_ulonglong_array(ULongLongSeqHolder seq, int offset, int length) {
		// TODO Auto-generated method stub

	}

	public void read_longlong_array(LongLongSeqHolder seq, int offset, int length) {
		// TODO Auto-generated method stub

	}

	public void read_float_array(FloatSeqHolder seq, int offset, int length) {
		// TODO Auto-generated method stub

	}

	public void read_double_array(DoubleSeqHolder seq, int offset, int length) {
		// TODO Auto-generated method stub

	}
}
