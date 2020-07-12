/*
 * Copyright (c) 2020 LOUBIERE Antonin <https://www.github.com/AntoninLoubiere/>
 *
 * This file is part of Diakôluô project <https://www.github.com/AntoninLoubiere/Diakoluo/>.
 *
 *     Diakôluô is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Diakôluô is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     A copy of the license is available in the root folder of Diakôluô, under the
 *     name of LICENSE.md. You could find it also at <https://www.gnu.org/licenses/gpl-3.0.html>.
 */

package fr.pyjacpp.diakoluo;

public final class Utils {
    public static String removeUselessSpaces(String s) {
        final int length = s.length();
        int realStart = 0;
        int realEnd = length - 1;

        while (realStart < length &&
                (s.charAt(realStart) == ' ' ||
                        s.charAt(realStart) == '\n' || s.charAt(realStart) == '\t')) {
            realStart++;
        }

        if (realStart >= length) {
            return "";
        }

        while (s.charAt(realEnd) == ' ' || s.charAt(realEnd) == '\n' || s.charAt(realEnd) == '\t') {
            realEnd--;
        }

        return s.substring(realStart, realEnd + 1);
    }
}
