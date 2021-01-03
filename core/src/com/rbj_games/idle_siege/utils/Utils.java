package com.rbj_games.idle_siege.utils;

public class Utils {
	
	public static float ConvertRanges(float oldMax, float oldMin, float newMax, float newMin, float oldValue) {
		float oldRange = oldMax - oldMin;
		float newRange = newMax - newMin;
		float newValue = (((oldValue - oldMin) * newRange) / oldRange) + newMin;
		return newValue;
	}
}
