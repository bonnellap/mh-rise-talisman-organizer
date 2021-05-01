package com.github.bonnellap.mh_rise_talisman_organizer.talisman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.javatuples.Pair;

import com.github.bonnellap.mh_rise_talisman_organizer.skill.Skill;


public class TalismanComparison {

	public static int compare(Talisman t1, Talisman t2) {
		// TODO Auto-generated method stub
		
		Talisman tClone1 = new Talisman(t1);
		Talisman tClone2 = new Talisman(t2);
		boolean t2WithinT1 = true;
		boolean t1WithinT2 = true;
		List<Integer> t1WastedSlots = new ArrayList<Integer>();
		List<Integer> t2WastedSlots = new ArrayList<Integer>();
		// Try to make t2 within t1
		for (Pair<Skill, Integer> t2SkillPair : t2.getSkillList()) {
			// The amount of skill levels needed as decorations
			int remainingSkillLevels = t2SkillPair.getValue1() - tClone1.getSkillLevel(t2SkillPair.getValue0());
			while (remainingSkillLevels > 0 && t2WithinT1) {
				// Try to add the skill as decorations to t1
				int wastedSlots = tClone1.removeSlot(t2SkillPair.getValue0().slot);
				t1WastedSlots.add(wastedSlots);
				if (wastedSlots == -1) {
					t2WithinT1 = false;
				}
				remainingSkillLevels--;
			}
			if (!t2WithinT1) {
				break;
			}
		}
		// Try to make t1 within t2
		for (Pair<Skill, Integer> t1SkillPair : t1.getSkillList()) {
			// The amount of skill levels needed as decorations
			int remainingSkillLevels = t1SkillPair.getValue1() - tClone2.getSkillLevel(t1SkillPair.getValue0());
			while (remainingSkillLevels > 0 && t1WithinT2) {
				// Try to add the skill as decorations to t2
				int wastedSlots = tClone2.removeSlot(t1SkillPair.getValue0().slot);
				t2WastedSlots.add(wastedSlots);
				if (wastedSlots == -1) {
					t1WithinT2 = false;
				}
				remainingSkillLevels--;
			}
			if (!t1WithinT2) {
				break;
			}
		}
		//System.out.println("\ntClone1 = " + tClone1.toString());
		//System.out.println("tClone2 = " + tClone2.toString());
		//System.out.println("t2 within t1 = " + t2WithinT1);
		//System.out.println("t1 within t2 = " + t1WithinT2);
		//printList(t1WastedSlots);
		//printList(t2WastedSlots);
		int slotComparison = compareSlotLists(tClone1, tClone2);
		//System.out.println("slot comparison = " + slotComparison);
		if (t2WithinT1 && t1WithinT2) {
			int slotComparison1 = compareSlotLists(t1, tClone2);
			int slotComparison2 = compareSlotLists(tClone1, t2);
			//System.out.println(slotComparison1);
			//System.out.println(slotComparison2);
			if (slotComparison1 == slotComparison2) {
				return slotComparison1;
			}
			if (slotComparison1 < 1 && slotComparison2 < 1) {
				return -1;
			}
			if (slotComparison1 > -1 && slotComparison2 > -1) {
				return 1;
			}
			return 0;
			//return slotComparison;
		} else if (t2WithinT1 && slotComparison > 0) {
			// t2 is worse than t1
			return 1;
		} else if (t1WithinT2 && slotComparison < 0) {
			// t1 is worse than t2
			return -1;
		}
		
		return 0;
	}
	
	public static void printList(List<Integer> list) {
		System.out.print("[");
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).toString());
			if (i + 1 != list.size()) {
				System.out.print(", ");
			}
		}
		System.out.println("]");
	}
	
	public static int compareSlotLists(Talisman t1, Talisman t2) {
		return compareSlotLists(t1.getSlotList(), t2.getSlotList());
	}
	
	/**
	 * Compares the slots on Talisman t1 and t2. Does not compare skills.
	 * @param t1
	 * @param t2
	 * @return 1 if t1 makes t2 obsolete, -1 if t2 makes t1 obsolete, or 0 if neither Talisman makes the other obsolete
	 */
	public static int compareSlotLists(List<Integer> slotList1, List<Integer> slotList2) {
		List<Integer> t1Slots = new ArrayList<>(slotList1);
		List<Integer> t2Slots = new ArrayList<>(slotList2);
		Collections.reverse(t1Slots);
		Collections.reverse(t2Slots);
		int leaning = 0;
		for (int i = 0; i < Math.max(t1Slots.size(), t2Slots.size()); i++) {
			if (t1Slots.size() == t2Slots.size()) {
				// slot sizes are the same
				if (leaning == -1 && t1Slots.get(i) > t2Slots.get(i)) {
					return 0;
				}
				if (leaning == 1 && t2Slots.get(i) > t1Slots.get(i)) {
					return 0;
				}
				if (leaning == 0 && t1Slots.get(i) > t2Slots.get(i)) {
					leaning = 1;
				}
				if (leaning == 0 && t2Slots.get(i) > t1Slots.get(i)) {
					leaning = -1;
				}
			} else if (t1Slots.size() > t2Slots.size()) {
				// t1 has more slots than t2
				if (i < t2Slots.size() && t1Slots.get(i) < t2Slots.get(i)) {
					return 0;
				}
				if (i >=  t2Slots.size()) {
					return 1;
				}
			} else {
				// t2 has more slots than t1
				if (i < t1Slots.size() && t2Slots.get(i) < t1Slots.get(i)) {
					return 0;
				}
				if (i >=  t1Slots.size()) {
					return -1;
				}
			}
		}
		return leaning;
	}
	
	/**
	 * Compares 2 wasted slot lists
	 * @param wastedSlotList1
	 * @param wastedSlotList2
	 * @return -1, 0, or 1 if wastedSlotList1 is worse, equal to, or better than wastedSlotList2
	 */
	public int compareWastedSlots(List<Integer> wastedSlotList1, List<Integer> wastedSlotList2) {
		// Create lists with only positive values in it
		List<Integer> list1 = new ArrayList<>();
		List<Integer> list2 = new ArrayList<>();
		for (int slot : wastedSlotList1) {
			if (slot > 0) {
				list1.add(slot);
			}
		}
		for (int slot : wastedSlotList2) {
			if (slot > 0) {
				list2.add(slot);
			}
		}
		Collections.sort(list1, Collections.reverseOrder());
		Collections.sort(list2, Collections.reverseOrder());
		for (int i = 0; i < Math.max(list1.size(), list2.size()); i++) {
			if (list1.size() == list2.size()) {
				// both lists have the same size
				if (list1.get(i) > list2.get(i)) {
					return -1;
				}
				if (list2.get(i) > list1.get(i)) {
					return 1;
				}
			} else if (list1.size() > list2.size()) {
				// list1 is larger than list2
				
			} else {
				// list2 is larger than list1
			}
		}
		return 0;
	}
	
	/**
	 * Tests to see if a skill can fit in the slots of a talisman.
	 * @param skill
	 * @param skillLevel
	 * @param talisman
	 * @return true if the skill can fit in the talisman's slots
	 */
	/*public boolean skillCanFit (Skill skill, int skillLevel, Talisman talisman) {
		// If the skill can't be made into a decoration, return false.
		if (!skill.isDeco()) {
			return false;
		}
		// Loop through the skills of the decoration to see how many times the skill can fit
		List<Integer> slotList = talisman.getSlotList();
		for (Integer slot : slotList) {
			if (skill.slot <= slot) {
				skillLevel--;
			}
		}
		// If all levels of the skill could fit, return true, else return false
		if (skillLevel <= 0) {
			return true;
		} else {
			return false;
		}
	}*/

}
