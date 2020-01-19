package test;

import net.sf.json.JSONArray;

public class main {
	public static void main(String[] args) {
		int[] a = new int[5];
		for(int i = 0;i<5;i++)
			a[i]=i;
		JSONArray array = new JSONArray();
		for(int i = 0;i<5;i++) {
			array.add(a[i]);
		}
		for(int i = 0;i<5;i++)
			System.out.println(array.get(i));
		
	}
}
