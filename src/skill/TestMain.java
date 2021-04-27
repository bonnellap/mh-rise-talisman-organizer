package skill;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

import talisman.Talisman;
import talisman.TalismanComparison;
import talisman.TalismanTable;

public class TestMain {

	public static void main(String[] args) {
		//SkillTable skills = new SkillTable("resources/skills.csv");
		//TalismanTable talismans = new TalismanTable("resources/my_talismans.csv", skills);
		
		//printList(talismans.optimizeTalismans(true));
		
		/*
		List<Pair<Skill, Integer>> skillList = new ArrayList<Pair<Skill, Integer>>();
		List<Integer> slotList = new ArrayList<>();
		skillList.add(new Pair<Skill, Integer>(skills.getSkill("Weakness Exploit"), 1));
		Talisman t = new Talisman(skillList, slotList);
		talismans.add(t);
		*/
		
		//printList(talismans.optimizeTalismans());
		//talismans.printTalismans();
		//talismans.removeObsoleteTalismans();
		
		//talismans.writeTalismansToCsv("resources/my_talismans.csv");
		
		/*
		Collections.sort(talismans, tc);
		talismans.printTalismans();
		println("");
		//printList(talismans.optimizeTalismans());
		printTalismanCompare(talismans);*/
		
		
		/*
		List<Talisman> l = mySkills.getTalismansWithSkill(skills.getSkill("Critical Eye"));
		mySkills.printTalismans();
		printList(mySkills.optimizeTalismans());
		*/
	}
	
	public static void println(String str) {
		System.out.println(str);
	}
	
	public static void println(int i) {
		System.out.println(i);
	}
	
	public static void printList(List<Talisman> list) {
		for (int i = 0; i < list.size(); i++) {
			println(list.get(i).toString());
		}
	}
	
	public static void printTalismanCompare(TalismanTable table) {
		TalismanComparison tc = new TalismanComparison();
		for (int i = 0; i < table.size(); i++) {
			println(table.get(i).toString());
			if (i + 1 != table.size()) {
				int comparison = tc.compare(table.get(i), table.get(i + 1));
				if (comparison == -1) {
					println("<");
				} else if (comparison == 0) {
					println("=");
				} else {
					println(">");
				}
			}
		}
	}

	/*
	public static List<List<String>> csvReader(String filePath, boolean removeHeaders) {
		List<List<String>> data = new ArrayList<List<String>>();
		try (BufferedReader csvReader = new BufferedReader(new FileReader(new File(filePath)))) {
			String line;
			int lineIndex = 0;
			while ((line = csvReader.readLine()) != null) {
				if (!removeHeaders || lineIndex != 0) {
					data.add(Arrays.asList(line.split(",")));
				}
				lineIndex++;
			}
			csvReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	public static void print2DList(List<List<String>> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).size(); j++) {
				System.out.print(list.get(i).get(j));
				if (j + 1 != list.get(i).size()) {
					System.out.print(",");
				}
			}
			System.out.println();
		}
	}*/

}
