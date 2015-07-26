package mtws.engine;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class Password {
	
	
	
	
	public static void main(String arg[]){
		System.out.println(base64ToByte(byteToBase64("passwo")));
		
		
	}
	public static String base64ToByte(String data)  {
		
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			return new String (decoder.decodeBuffer(data));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * From a byte[] returns a base 64 representation
	 * @param hashPasword 
	 * 
	 * @param data
	 *            byte[]
	 * @return String
	 * @throws IOException
	 */
	public static String byteToBase64(String hashPasword) {
		BASE64Encoder endecoder = new BASE64Encoder();
		byte[] data={};
		try {
			data = hashPasword.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return endecoder.encode(data);
	}
}