package tool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class tools {
	public static byte[] int2Bytes(int x,int n){
		byte[] targets = new byte[n];
		for (int i = 0; i < n; i++) {  
            int offset = (n - 1 - i) * 8;  
            targets[i] = (byte) ((x >> offset) & 0xFF);  
        }
		return targets;
	}
	public static void bytes2Int(byte[] b){
		
	}
	public static byte[] concat(byte[] a,byte[] b){
		byte[] c = new byte[a.length+b.length];
		System.arraycopy(a, 0, c, 0, a.length);  
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
	public static void printArray(byte[] b){
		for (int i = 0; i < b.length; i++) {  
			System.out.print(Integer.toHexString(b[i]&0x0ff)+" ");  
		} 
		System.out.println();
	}
	public static String ipv42Str(byte[] b,byte prefix){
		int[] maskIp = new int[4];
        for (int i = 0; i < maskIp.length; i++) {
            maskIp[i] = (prefix >= 8) ? 255 : (prefix > 0 ? (prefix & 0xff) : 0);
            prefix -= 8;
        }
		String ipv4 = "";
		for (int i = 0; i < b.length; i++) {  
			  ipv4 = ipv4+((b[i]&0x0ff)&(maskIp[i]));
			  if(i!=b.length-1)
				  ipv4 = ipv4 + '.';
		} 
		return ipv4;
	}
	public static String bytes2Str(byte[] b){
		String str = "";
		for (int i = 0; i < b.length; i++) {  
			  str = str+(b[i]&0x0ff);
			  if(i!=b.length-1)
				  str = str + '|';
		} 
		return str;
	}
	public static byte[] str2Bytes(String str,int n){
		byte[] strs = str.getBytes();
		return concat(new byte[n-strs.length],strs);
	}
	public static byte[] getUserlist(HashMap<Integer,String> map){
		byte[] userlist = new byte[0];
		Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<Integer, String> entry = it.next();
			userlist = concat(userlist,int2Bytes(entry.getKey(), 2));
			userlist = concat(userlist,str2Bytes(entry.getValue(), 16));
		}
		return userlist;
	}
	public static boolean isRepeat(List<Integer> list) {
		Set<Integer> set = new TreeSet<Integer>();
		set.addAll(list);
        if (set.size() == list.size()) {
            return false;
        } else {
            return true;
        }
    }
}
