package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tool.tools;

public class userlisttest {

	public static void main(String[] args) {
		/*byte[] b = tools.getUserlist(null);
		System.out.println(b.length);
		tools.printArray(b);*/
		String regex = "\\|";
		Pattern p=Pattern.compile(regex);
		String[] strs = "12|32|2".split(regex);
		System.out.println(strs.length);
		String str ="192.168.0.106";
//		System.out.println(tools.getIp(str));
	}

}
