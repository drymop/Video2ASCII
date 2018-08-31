package lmtuan.video2ascii.test;

import java.util.HashSet;
import java.util.Set;

import lmtuan.video2ascii.core.Pixel2Char;

public class TestGrayscale {
	public static void main(String[] args) {
		Pixel2Char gs2ch = new Pixel2Char();
		gs2ch.setFont("Consolas", 0);
		char[] table = gs2ch.getConversionTable();
		
		Set<Character> set = new HashSet<>();
		
		// print the table
		for(int i = 0; i < table.length; i++) {
			System.out.printf("%-3d '%c'%n", i, table[i]);
			set.add(table[i]);
		}
		
		// print number of unique ASCII character used in table
		System.out.println("No. unique characters: " + set.size());
	}
}
