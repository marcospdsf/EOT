/* $Id: UsernameCharacterValidator.java,v 1.5 2012/05/22 22:55:40 edi18028 Exp $ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.core.account;

import marauroa.common.game.Result;

/**
 * validates the character used for the character name.
 * 
 * @author hendrik
 */
public class UsernameCharacterValidator implements AccountParameterValidator {
	private final String parameterValue;

	/**
	 * creates a UsernameCharacterValidator.
	 * 
	 * @param parameterValue
	 *            value to validate
	 */
	public UsernameCharacterValidator(final String parameterValue) {
		this.parameterValue = parameterValue;
	}

	@Override
	public Result validate() {
		if (!parameterValue.contains("@") || !parameterValue.contains(".") || (parameterValue.length() <= 5)) {
			return Result.FAILED_INVALID_CHARACTER_USED;
		}

		// letters , numbers and few signs are allowed
		for (int i = parameterValue.length() - 1; i >= 0; i--) {
			final char chr = parameterValue.charAt(i);
			if (((chr < 'a') || (chr > 'z')) && ((chr < 'A') || (chr > 'Z'))
					&& ((chr < '0') || (chr > '9')) && (chr != '…') && (chr != '‡')
					&& (chr != '™') && (chr != '‚') && (chr != '„') && (chr != '³')
					&& (chr != '›') && (chr != 'º') && (chr != '¼')
					&& (chr != '„') && (chr != '†') && (chr != '˜') && (chr != '§')
					&& (chr != 'ƒ') && (chr != '“') && (chr != 'š') && (chr != '¹')
					&& (chr != '»')
					&& (chr != '-') && (chr != '_') && (chr != '.') && (chr != ' ')
					&& (chr != '!') && (chr != '$') && (chr != '%') && (chr != '^')
					&& (chr != '&') && (chr != '*') && (chr != '(') && (chr != ')')
					&& (chr != '+') && (chr != '=') && (chr != '?') && (chr != '{')
					&& (chr != '}')) {
				return Result.FAILED_INVALID_CHARACTER_USED;
			}
		}

		// at least the first character must be a letter
		final char chr = parameterValue.charAt(0);
		if (((chr < 'a') || (chr > 'z')) && ((chr < 'A') || (chr > 'Z')) && (chr != '‡')
				&& (chr != '‚') && (chr != '›') && (chr != 'º') && (chr != '¼')
				&& (chr != '†') && (chr != '§') && (chr != 'š') && (chr != '¹')
				&& (chr != '»')) {
			return Result.FAILED_INVALID_CHARACTER_USED;
		}

		return null;
	}
}
