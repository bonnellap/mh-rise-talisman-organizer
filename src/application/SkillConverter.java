package application;

import javafx.util.StringConverter;
import skill.Skill;
import skill.SkillTable;

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
