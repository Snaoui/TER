package org.qcmix.mixer;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		ArrayList<Object> list = new ArrayList<Object>();
		ArrayList<ArrayList<Object>> resultat = new ArrayList<ArrayList<Object>>();
		list.add(0);
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);

		resultat = Mixer.generateSheets(list, 10);

		
		for (ArrayList<Object> t: resultat)
		{
			for (Object i: t)
			{
				System.out.print((Integer)i+" ");
			}
			System.out.println(" ");
		}
	}
}
