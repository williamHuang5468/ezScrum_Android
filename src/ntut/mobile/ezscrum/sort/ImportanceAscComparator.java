package ntut.mobile.ezscrum.sort;

import java.util.Comparator;

import ntut.mobile.ezscrum.model.StoryObject;

public class ImportanceAscComparator implements Comparator<StoryObject> {
	public int compare(StoryObject firstObject, StoryObject secondObject) {
		return Integer.parseInt(firstObject.get_importance())-Integer.parseInt(secondObject.get_importance());
	}
}