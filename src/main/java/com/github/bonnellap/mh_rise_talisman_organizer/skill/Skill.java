package com.github.bonnellap.mh_rise_talisman_organizer.skill;

public class Skill {

	/**
	 * The skill name
	 */
	public final String name;
	/**
	 * The maximum skill level
	 */
	public final int level;
	/**
	 * The number of slots that the skill decoration takes to make per level. 0 if it cannot be made into a decoration.
	 */
	public final int slot;
	
	public Skill(String name, int level, int slot){
		this.name = name;
		this.level = level;
		this.slot = slot;
	}
	
	Skill(Skill skill) {
		this.name = skill.name;
		this.level = skill.level;
		this.slot = skill.slot;
	}
	
	public boolean isDeco() {
		return slot > 0;
	}
	
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Skill)) {
			return false;
		}
		Skill skill = (Skill) obj;
		return name.equals(skill.name);
	}
	
	public String toString() {
		return name;
	}
	
}
