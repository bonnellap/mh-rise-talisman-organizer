package com.github.bonnellap.mh_rise_talisman_organizer.talisman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.javatuples.Pair;

import com.github.bonnellap.mh_rise_talisman_organizer.skill.Skill;


public class Talisman {
	
	private List<Pair<Skill, Integer>> skillList = new ArrayList<Pair<Skill, Integer>>();
	private List<Integer> slotList = new ArrayList<Integer>();

	/**
	 * Creates a talisman with a skill list and slot list
	 * @param skillList
	 * @param slotList
	 */
	public Talisman(List<Pair<Skill, Integer>> skillList, List<Integer> slotList) {
		this.skillList = new ArrayList<>(skillList);
		this.slotList = new ArrayList<>(slotList);
		Collections.sort(this.slotList);
	}
	
	/**
	 * Creates a talisman with the same attributes as another talisman
	 * @param t
	 */
	public Talisman(Talisman t) {
		this.skillList = new ArrayList<>(t.skillList);
		this.slotList = new ArrayList<>(t.slotList);
		Collections.sort(this.slotList);
	}
	
	/**
	 * Creates an empty talisman
	 */
	Talisman() { }

	/**
	 * Returns true if the Talisman has the specified skill, else false
	 * 
	 * @param s
	 * @return
	 */
	public boolean containsSkill(Skill s) {
		if (s == null) {
			return false;
		}
		for (Pair<Skill, Integer> skillPair : skillList) {
			if (skillPair.getValue0().equals(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a list of the talisman's slots
	 * @return
	 */
	public List<Integer> getSlotList() {
		return new ArrayList<>(slotList);
	}
	
	void setSlotList(List<Integer> slotList) {
		this.slotList = new ArrayList<>(slotList);
		Collections.sort(this.slotList);
	}
	
	/**
	 * Removes the smallest slot for a given size from the talisman
	 * @param slotSize
	 * @return the number of wasted slots, -1 if it cannot fit
	 */
	int removeSlot(int slotSize) {
		if (slotSize <= 0) {
			// slot size must be bigger than 0
			return -1;
		}
		for (int i = 0; i < slotList.size(); i++) {
			int slotValue = slotList.get(i);
			if (slotValue >= slotSize) {
				slotList.remove(i);
				return slotValue - slotSize;
			}
		}
		return -1;
	}
	
	/**
	 * Returns a list of skills and their levels
	 * @return
	 */
	public List<Pair<Skill, Integer>> getSkillList() {
		return new ArrayList<>(skillList);
	}
	
	/**
	 * Sets the skill list of the talisman
	 * @param skillList
	 */
	void setSkillList(List<Pair<Skill, Integer>> skillList) {
		this.skillList = new ArrayList<>(skillList);
	}
	
	/**
	 * Adds a skill pair to the list of talisman skills
	 * @param skillPair
	 */
	void addSkill(Pair<Skill, Integer> skillPair) {
		this.skillList.add(skillPair);
	}
	
	/**
	 * Removes a skill pair from the talisman
	 * @param skillPair
	 * @return true if a skill pair was found and removed, else false
	 */
	boolean removeSkill(Pair<Skill, Integer> skillPair) {
		return this.skillList.remove(skillPair);
	}
	
	/**
	 * Removes a skill from the talisman
	 * @param skill
	 * @return true if a skill was found and removed, else false
	 */
	boolean removeSkill(Skill skill) {
		for (int i = 0; i < skillList.size(); i++) {
			if (skillList.get(i).getValue0().equals(skill)) {
				skillList.remove(i);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get a skill at a specific index from the skill list
	 * @param index
	 * @return - The skill, null if the index is out of bounds.
	 */
	public Skill getSkill(int index) {
		if (index < skillList.size()) {
			return skillList.get(index).getValue0();
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the skill pair for a specific skill
	 * @param s
	 * @return
	 */
	public Pair<Skill, Integer> getSkillPair(Skill s) {
		if (s == null) {
			return null;
		}
		for (Pair<Skill, Integer> pair: skillList) {
			if (pair.getValue0().equals(s)) {
				return pair;
			}
		}
		return null;
	}
	
	/**
	 * Get a skill level at a specific index from the skill list
	 * @param index
	 * @return
	 */
	public int getSkillLevel(int index) {
		if (index < skillList.size()) {
			return skillList.get(index).getValue1();
		} else {
			return 0;
		}
	}
	
	/**
	 * Get a skill pair at a specific index from the skill list
	 * @param index
	 * @return
	 */
	public Pair<Skill, Integer> getSkillPair(int index) {
		return skillList.get(index);
	}
	
	/**
	 * Get the total number of skills on this talisman
	 * @return
	 */
	public int getNumOfSkills() {
		return skillList.size();
	}
	
	/**
	 * Get the slot at a specific index from the slot list (in reverse order)
	 * @param index
	 * @return
	 */
	public int getSlot(int index) {
		if (index < slotList.size()) {
			return slotList.get(slotList.size() - 1 - index);
		} else {
			return 0;
		}
	}
	
	/**
	 * Returns the level of a skill. 0 if the skill isn't on the talisman
	 * @param s
	 * @return
	 */
	public int getSkillLevel(Skill s) {
		for (Pair<Skill, Integer> skillPair : skillList) {
			if (skillPair.getValue0().equals(s)) {
				return skillPair.getValue1();
			}
		}
		return 0;
	}
    

	public String toString() {
		String skillPairs = "";
		boolean isFirst = true;
		for (Pair<Skill, Integer> skillPair : skillList) {
			if (!isFirst) {
				skillPairs += ", ";
			}
			skillPairs += "[" + skillPair.getValue0().name + ", " + skillPair.getValue1() + "]";
			isFirst = false;
		}
		String slotStr = "[";
		List<Integer> slotList = getSlotList();
		for (int i = 0; i < slotList.size(); i++) {
			if (slotList.get(i) != 0) {
				slotStr += slotList.get(i);
			}
			if (i + 1 < slotList.size() && slotList.get(i + 1) != 0) {
				slotStr += ", ";
			}
		}
		slotStr += "]";
		return skillPairs + ", " + slotStr;
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Talisman)) {
			return false;
		}
		Talisman talisman = (Talisman) obj;
		return skillList.equals(talisman.getSkillList()) && slotList.equals(talisman.getSlotList());
	}

}
