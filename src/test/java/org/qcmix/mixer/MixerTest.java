package org.qcmix.mixer;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

public class MixerTest {

	private ArrayList<Object> createCombination(String combination) {
		String[] list = combination.split(" ");
		ArrayList<Object> intCombination = new ArrayList<Object>();
		for (String el : list) {
			intCombination.add(Integer.valueOf(el));
		}

		return intCombination;
	}

	private ArrayList<ArrayList<Object>> initCombinations4 ()
	{
		ArrayList<ArrayList<Object>> fullCombinations = new ArrayList<ArrayList<Object>>();
		fullCombinations.add(createCombination("0 1 2 3"));
		fullCombinations.add(createCombination("0 3 2 1"));
		fullCombinations.add(createCombination("0 3 1 2"));
		fullCombinations.add(createCombination("0 1 3 2"));
		fullCombinations.add(createCombination("0 2 3 1"));
		fullCombinations.add(createCombination("0 2 1 3"));
		fullCombinations.add(createCombination("1 0 2 3"));
		fullCombinations.add(createCombination("1 0 3 2"));
		fullCombinations.add(createCombination("1 2 0 3"));
		fullCombinations.add(createCombination("1 2 3 0"));
		fullCombinations.add(createCombination("1 3 2 0"));
		fullCombinations.add(createCombination("1 3 0 2"));
		fullCombinations.add(createCombination("1 3 0 3"));
		fullCombinations.add(createCombination("2 3 0 1"));
		fullCombinations.add(createCombination("2 3 1 0"));
		fullCombinations.add(createCombination("2 0 1 3"));
		fullCombinations.add(createCombination("2 0 3 1"));
		fullCombinations.add(createCombination("2 1 3 0"));
		fullCombinations.add(createCombination("2 1 0 3"));
		fullCombinations.add(createCombination("3 0 1 2"));
		fullCombinations.add(createCombination("3 0 2 1"));
		fullCombinations.add(createCombination("3 1 2 0"));
		fullCombinations.add(createCombination("3 1 0 2"));
		fullCombinations.add(createCombination("3 2 0 1"));
		fullCombinations.add(createCombination("3 2 1 0"));
		return fullCombinations;
	}

	private void execTest4Combinations()
	{
		ArrayList<Object> list = new ArrayList<Object>();
		ArrayList<ArrayList<Object>> fullCombinations = initCombinations4();
		list.add(0);
		list.add(1);
		list.add(2);
		list.add(3);
		ArrayList<ArrayList<Object>> list2 = null;

		list2 = Mixer.generateSheets( list, 4);


		for (Object o: list2)
		{
			if (! fullCombinations.contains(o))
			{
				fail(o.toString());
			}
		}
	}

	@Test
	public void generateSheetsTest()
	{

		for (int i = 0; i < 30; i++)
		{
			execTest4Combinations();
		}

	}

}
