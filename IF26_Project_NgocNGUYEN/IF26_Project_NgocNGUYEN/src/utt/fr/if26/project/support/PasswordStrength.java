package utt.fr.if26.project.support;

public class PasswordStrength {

	/**
	 * Check password strength
	 * 
	 * @param password
	 */
	public static boolean checkPasswordStrength(String password) {
		int strength = 0;
		boolean isStrong = false;

		String[] partialRegexChecks = { ".*[a-z]+.*", // lower
				".*[A-Z]+.*", // upper
				".*[\\d]+.*", // digits
				".*[@#$%]+.*" // symbols
		};

		if (password.length() > 5) {
			strength += 20;
		}
		if (password.matches(partialRegexChecks[0])) {
			strength += 20;
		}
		if (password.matches(partialRegexChecks[1])) {
			strength += 20;
		}
		if (password.matches(partialRegexChecks[2])) {
			strength += 20;
		}
		if (password.matches(partialRegexChecks[3])) {
			strength += 20;
		}

		if (strength == 100) {
			isStrong = true;
		}

		return isStrong;
	}
}
