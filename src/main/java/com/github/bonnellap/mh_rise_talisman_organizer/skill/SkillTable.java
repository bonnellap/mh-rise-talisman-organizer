package com.github.bonnellap.mh_rise_talisman_organizer.skill;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * An ArrayList of Skills
 * @author Alec Bonnell
 *
 */
public class SkillTable {

	private static List<Skill> data = new ArrayList<Skill>();

	/**
	 * Builds the skill table using a CSV filePath. The CSV file must be in the format Name,Level,[Slot].
	 * @param filePath
	 */
	/*public SkillTable(String filePath){
		BufferedReader csvReader = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(new File(filePath));
			csvReader = new BufferedReader(fileReader);
			String line; // Full line of data from the csv
			boolean headerLine = true; // Keeps track of the header line
			while ((line = csvReader.readLine()) != null) {
				if (!headerLine) {
					// Splits the full line using the comma separator
					String[] skillArray = line.split(",");
					
					// Gets all data from split line
					String name = skillArray[0];
					int level = Integer.parseInt(skillArray[1]);
					int slot = skillArray.length > 2 ? Integer.parseInt(skillArray[2]) : 0;
					
					// Add skill to the data table
					data.add(new Skill(name, level, slot));
				} else {
					headerLine = false;
				}
			}
			csvReader.close();
		} catch (IOException e) {
			e.printStackTrace();
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
	}*/
	public static void setSkills(BufferedReader csvReader) throws IOException {
		String line; // Full line of data from the csv
		boolean headerLine = true; // Keeps track of the header line
		while ((line = csvReader.readLine()) != null) {
			if (!headerLine) {
				// Splits the full line using the comma separator
				String[] skillArray = line.split(",");
				
				// Gets all data from split line
				String name = skillArray[0];
				int level = Integer.parseInt(skillArray[1]);
				int slot = skillArray.length > 2 ? Integer.parseInt(skillArray[2]) : 0;
				
				// Add skill to the data table
				data.add(new Skill(name, level, slot));
			} else {
				headerLine = false;
			}
		}
	}
	
	/**
	 * Gets a Skill in the table with a specific skill name
	 * @param skillName
	 * @return
	 */
	public static Skill getSkill(String skillName) {
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).name.equals(skillName)) {
				return data.get(i);
			}
		}
		return null;
	}
	
	public static List<Skill> getSkillList() {
		List<Skill> skillList = new ArrayList<>();
		for (Skill s: data) {
			skillList.add(new Skill(s));
		}
		return skillList;
	}
	
	public static String[] getSkillNames() {
		ArrayList<String> list = new ArrayList<>();
		for (Skill s: data) {
			list.add(s.name);
		}
		return list.toArray(new String[list.size()]);
	}
	
}
