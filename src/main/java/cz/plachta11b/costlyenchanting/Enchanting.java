package cz.plachta11b.costlyenchanting;

public class Enchanting {

	public Enchanting() {
		
	}

	public static int totalLevelToExp(int level) {

		if (level >= 1 && level <= 16) {
			return (int)(Math.pow(level, 2) + 6 * level);
		} else if(level >= 17 && level <= 31) {
			return (int)(2.5 * Math.pow(level, 2) - 40.5 * level + 360);
		} else if(level >= 32) {
			return (int)(4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
		} else {
			return 0;
		}
	}

	public static int currentLevelToExp(int level) {

		if (level >= 1 && level <= 16) {
			return (int)(2 * level + 7);
		} else if(level >= 17 && level <= 31) {
			return (int)(5 * level - 38);
		} else if(level >= 32) {
			return (int)(9 * level - 158);
		} else {
			return 0;
		}
	}
}