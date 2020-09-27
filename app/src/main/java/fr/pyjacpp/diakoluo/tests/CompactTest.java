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

package fr.pyjacpp.diakoluo.tests;

/**
 * An object that represent a test with commons informations stored but no data so it's save memory.
 */
public class CompactTest {
    private static final short MAX_DESCRIPTION_LENGTH = 100;
    private String name;
    private String compactDescription;
    private String filename;
    private boolean playable;

    /**
     * Create a new compact test form a test.
     * @param test the test to wrap
     */
    public CompactTest(Test test) {
        update(test);
    }

    /**
     * Update the compact from the test.
     * @param test the test which is associated with this
     */
    public void update(Test test) {
        name = test.getName();
        String description = test.getDescription();
        compactDescription = description.length() > MAX_DESCRIPTION_LENGTH ?
                description.substring(0, MAX_DESCRIPTION_LENGTH) + '…' : description;
        filename = test.getFilename();
        playable = test.isPlayable();
    }

    /**
     * Get the name of the test.
     * @return the name of the test
     */
    public String getName() {
        return name;
    }

    /**
     * Get a description that doesn't exceeded {@link #MAX_DESCRIPTION_LENGTH}.
     * @return the description of the test may be truncated
     * @see #MAX_DESCRIPTION_LENGTH
     */
    public String getCompactDescription() {
        return compactDescription;
    }

    /**
     * Get the filename that save the test.
     * @return the filename of the test
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Get if the test is playable.
     * @return if the test is playable
     */
    public boolean isPlayable() {
        return playable;
    }
}
