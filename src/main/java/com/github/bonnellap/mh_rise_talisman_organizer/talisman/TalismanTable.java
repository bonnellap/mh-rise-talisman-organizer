package com.github.bonnellap.mh_rise_talisman_organizer.talisman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.javatuples.Pair;

import com.github.bonnellap.mh_rise_talisman_organizer.skill.Skill;
import com.github.bonnellap.mh_rise_talisman_organizer.skill.SkillTable;

/**
 * An ArrayList of Talismans
 * 
 * @author Alec Bonnell
 *
 */
public class TalismanTable extends ArrayList<Talisman> {

	private static final long serialVersionUID = -796283047451470205L;
	
	private static final String csvHeader = "Name1,Level1,Name2,Level2,Slot1,Slot2,Slot3";
	
	public TalismanTable() {};

	/**
	 * Builds the talisman table using a CSV filePath. The CSV file must be in the
	 * format Name1,Level1,[Name2],[Level2],[Slot1],[Slot2],[Slot3].
	 * 
	 * @param filePath
	 * @throws IOException 
	 */
	public TalismanTable(File file) throws IOException {
		BufferedReader csvReader = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
			csvReader = new BufferedReader(fileReader);
			String line; // Full line of data from the csv
			boolean headerLine = true; // Keeps track of the header line
			while ((line = csvReader.readLine()) != null) {
				if (!headerLine) {
					// Splits the full line using the comma separator
					String[] talismanArray = line.split(",");

					// Gets all data from split line
					List<Pair<Skill, Integer>> skillList = new ArrayList<Pair<Skill, Integer>>();
					
					skillList.add(new Pair<Skill, Integer>(SkillTable.getSkill(talismanArray[0]), Integer.parseInt(talismanArray[1])));
					if (talismanArray.length > 2 && !talismanArray[2].equals("")) {
						skillList.add(new Pair<Skill, Integer>(SkillTable.getSkill(talismanArray[2]), Integer.parseInt(talismanArray[3])));
					}
					List<Integer> slotList = new ArrayList<Integer>();
					if (talismanArray.length > 4) {
						slotList.add(Integer.parseInt(talismanArray[4]));
					}
					if (talismanArray.length > 5) {
						slotList.add(Integer.parseInt(talismanArray[5]));
					}
					if (talismanArray.length > 6) {
						slotList.add(Integer.parseInt(talismanArray[6]));
					}

					// Add skill to the data table
					this.add(new Talisman(skillList, slotList));
				} else {
					headerLine = false;
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (csvReader != null) {
					csvReader.close();
				}
				if (fileReader != null) {
					fileReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets a list of Talismans with a specific Skill
	 * 
	 * @param skillName
	 * @return
	 */
	public TalismanTable getTalismansWithSkill(Skill skill) {
		TalismanTable talismanList = new TalismanTable();
		for (Talisman talisman : this) {
			if (talisman.containsSkill(skill)) {
				talismanList.add(talisman);
			}
		}
		return talismanList;
	}
	
	public TalismanTable(TalismanTable table) {
		for (Talisman t : table) {
			this.add(new Talisman(t));
		}
	}
	
	/**
	 * Gets a list of Talismans that are obsolete with an optional explanation
	 * @return
	 */
	public TalismanTable optimizeTalismans(boolean explain) {
		TalismanTable tableClone = new TalismanTable(this);
		TalismanTable obsoleteTalismans = new TalismanTable();
		
		ListIterator<Talisman> i = tableClone.listIterator();
		while (i.hasNext()) {
			Talisman t1 = i.next();
			ListIterator<Talisman> j = tableClone.listIterator();
			while (j.hasNext()) {
				Talisman t2 = j.next();
				if (t1 == t2) {
					// Don't compare a Talisman to itself
					continue;
				}
				int comparison = TalismanComparison.compare(t1, t2);
				if (comparison == -1) {
					// t1 is obsolete
					if (!obsoleteTalismans.contains(t1) ) {
						if (explain) System.out.println("{" + t1.toString() + "} < {" + t2.toString() + "} Decorations Needed: " + skillListToString(TalismanComparison.t2DecosNeeded));
						obsoleteTalismans.add(t1);
					}
					i.remove();
					break;
				} else if (comparison == 1) {
					// t2 is obsolete
					if (!obsoleteTalismans.contains(t2) ) {
						if (explain) System.out.println("{" + t2.toString() + "} < {" + t1.toString() + "} Decorations Needed: " + skillListToString(TalismanComparison.t1DecosNeeded));
						obsoleteTalismans.add(t2);
					}
				} else if (t1.equals(t2)) {
					if (explain) System.out.println("{" + t2.toString() + "} = {" + t1.toString() + "} Duplicated Talisman");
					obsoleteTalismans.add(t2);
				}
			}
		}
		if (explain) System.out.println();
		return obsoleteTalismans;
	}
	
	/**
	 * Gets a list of Talismans that are obsolete
	 * @return
	 */
	public TalismanTable optimizeTalismans() {
		return optimizeTalismans(false);
	}
	
	public void writeTalismansToFile(File file) throws IOException {
		FileWriter writer = null;
		try {
			if (file.createNewFile()) {
				System.out.println("File created: " + file.getName());
		    }
			writer = new FileWriter(file.getAbsolutePath());
			writer.write(csvHeader);
			for (Talisman t : this) {
				writer.write("\r\n");
				String talismanString = "";
				// Add Talisman skills
				List<Pair<Skill, Integer>> skillPairs = t.getSkillList();
				talismanString += skillPairs.get(0).getValue0().name;
				talismanString += "," + skillPairs.get(0).getValue1().toString();
				if (skillPairs.size() == 2) {
					talismanString += "," + skillPairs.get(1).getValue0().name;
					talismanString += "," + skillPairs.get(1).getValue1().toString();
				} else if (t.getSlotList().size() > 0) {
					talismanString += ",,";
				}
				// Add Talisman slots
				for (Integer slot : t.getSlotList()) {
					talismanString += "," + slot.toString();
				}
				writer.write(talismanString);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				// Do nothing
			}
		}
	}
	
	/**
	 * Modifies the TalismanTable and removes all obsolete Talismans
	 * @return - The list of talismans removed
	 */
	public List<Talisman> removeObsoleteTalismans() {
		List<Talisman> talismansToRemove = optimizeTalismans();
		this.removeAll(talismansToRemove);
		return talismansToRemove;
	}
	
	public void printTalismans() {
		for (int i = 0; i < this.size(); i++) {
			System.out.print(this.get(i).toString());
			if (i + 1 != this.size()) {
				System.out.print(", ");
			}
			System.out.println();
		}
	}
	
	public static String skillListToString (List<Skill> skillList) {
		String str = "[";
		for (int i = 0; i < skillList.size(); i++) {
			str += skillList.get(i).name;
			if (i + 1 != skillList.size()) {
				str += ", ";
			}
		}
		str += "]";
		return str;
	}

}
