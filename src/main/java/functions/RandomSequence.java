package functions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class RandomSequence 
{
	public static ArrayList<Integer> getRandomSequences(int size)
	{
		Random random = new Random();
		HashSet<Integer> setRandom = new HashSet<>();
		ArrayList<Integer> listSequences = new ArrayList<>();
		while(setRandom.size()<size)
		{
			int temp = random.nextInt(size);
			if(!setRandom.contains(temp))
			{
				setRandom.add(temp);
				listSequences.add(temp);
			}
		}
		
		return listSequences;
	}
	public static void main(String [] args)
	{
		int size = 11;
		System.out.println(getRandomSequences(size));
	}
}
