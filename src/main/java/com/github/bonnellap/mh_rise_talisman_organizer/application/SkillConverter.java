package com.github.bonnellap.mh_rise_talisman_organizer.application;

import com.github.bonnellap.mh_rise_talisman_organizer.skill.Skill;
import com.github.bonnellap.mh_rise_talisman_organizer.skill.SkillTable;

import javafx.util.StringConverter;

public class SkillConverter extends StringConverter<Skill> {

	@Override
	public Skill fromString(String string) {
		return SkillTable.getSkill(string);
	}

	@Override
	public String toString(Skill object) {
		return object != null ? object.name : "";
	}

}
