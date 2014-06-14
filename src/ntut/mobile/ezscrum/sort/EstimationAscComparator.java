package ntut.mobile.ezscrum.sort;

import java.util.Comparator;

import ntut.mobile.ezscrum.model.StoryObject;

public class EstimationAscComparator implements Comparator<StoryObject> {
	public int compare(StoryObject firstObject, StoryObject secondObject) {
		return Integer.parseInt(firstObject.get_estimation())-Integer.parseInt(secondObject.get_estimation());
	}
}