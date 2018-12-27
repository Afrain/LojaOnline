package com.afrain.lojaonline.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {

	public static String decoderParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
		
	}
	
	public static List<Integer> decodeIntList(String s){
		String[] vet = s.split(",");
		List<Integer> lista	= new ArrayList<>();
		for (int i = 0; i < vet.length; i++) {
			lista.add(Integer.parseInt(vet[i]));
		}
		return lista;
	}
	
}
