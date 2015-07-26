package mtws.engine;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class MtWsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4156184678695314631L;
	
	static StringBuffer sb=new StringBuffer();

	private static Writer wtr= new Writer() {
		
		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			sb.append(cbuf);
		}
		@Override
		public void flush() throws IOException {
		}
		@Override
		public void close() throws IOException {
		}
	};

	static PrintWriter pWriter = new PrintWriter(wtr);
	
	
	public MtWsException() {
		// TODO Auto-generated constructor stub
	}

	public MtWsException(String arg0) {
		sb.append(arg0);
		// TODO Auto-generated constructor stub
	}

	public MtWsException(Throwable arg0) {
		arg0.printStackTrace(pWriter);
		// TODO Auto-generated constructor stub
	}

	public MtWsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public MtWsException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super();
		// TODO Auto-generated constructor stub
	}

}
