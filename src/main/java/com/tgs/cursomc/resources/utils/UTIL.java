package com.tgs.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UTIL {

	public static List<Long> decodeIntList(String s){
		return Arrays.asList(s.split(",")).stream().map(x -> Long.parseLong((String) x)).collect(Collectors.toList());
	}
	
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
