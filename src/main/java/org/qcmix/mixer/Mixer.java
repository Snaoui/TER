package org.qcmix.mixer;

import java.util.ArrayList;
import java.util.Random;

public class Mixer {

	/**
	 * For test purposes, allow to reproduce mixes.
	 * 0 means a random seed will be used.
	 */
	public static long seed = 0;

	private static void permuter(ArrayList<Object> list, int i, int j) {

		Object tmp;
		tmp = list.get(i);
		list.set(i, list.get(j));
		list.set(j, tmp);

	}

	public static ArrayList<ArrayList<Object>> generateSheets(ArrayList<Object> initial, int n) {
		if (initial.size() == 0) {
			throw new IllegalArgumentException("La liste passée en paramètre est vide");
		}

//		if (n > initial.size()) {
//			throw new IllegalArgumentException(
//					"Le nombre de combinaisons différentes souhaité est incompatible avec la taille de la liste");
//		}
		ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();

		ArrayList<Object> combinaison = null;


		Random random = null;
		if (seed == 0) {
			random = new Random();
		} else {
			random = new Random(seed);
		}

		for (int i = 0; i < n; i++) {
				combinaison = generate(initial, random);

			result.add(combinaison);
		}
		return result;

	}

	private static ArrayList<Object> generate(ArrayList<Object> initial, Random random) {

		ArrayList<Object> nouvelOrdre = (ArrayList<Object>) initial.clone();
		for (int i = initial.size() - 1; i > 0; i--) {

			int r = random.nextInt(i);
			permuter(nouvelOrdre, r, i);
		}
		return nouvelOrdre;
	}

}
