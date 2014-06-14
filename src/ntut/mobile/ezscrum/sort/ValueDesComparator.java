package ntut.mobile.ezscrum.sort;

import java.util.Comparator;

import ntut.mobile.ezscrum.model.StoryObject;

public class ValueDesComparator implements Comparator<StoryObject> {
	public int compare(StoryObject firstObject, StoryObject secondObject) {
		return Integer.parseInt(secondObject.get_value())-Integer.parseInt(firstObject.get_value());
	}
}