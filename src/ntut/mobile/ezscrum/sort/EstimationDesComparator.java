package ntut.mobile.ezscrum.sort;

import java.util.Comparator;

import ntut.mobile.ezscrum.model.StoryObject;

public class EstimationDesComparator implements Comparator<StoryObject> {
	public int compare(StoryObject firstObject, StoryObject secondObject) {
		return Integer.parseInt(secondObject.get_estimation())-Integer.parseInt(firstObject.get_estimation());
	}
}