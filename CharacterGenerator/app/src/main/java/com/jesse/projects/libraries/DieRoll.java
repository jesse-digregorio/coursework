package com.jesse.projects.libraries; /**
 * Created by jesse on 7/7/2017.
 */

import com.jesse.projects.libraries.Die;

import java.util.Objects;
import java.util.Random;


public class DieRoll {

    private Random rand;

    String lastResultDescription;
    String lastRequestDescription;

    public DieRoll() {

        rand = new Random();
        lastRequestDescription = new String("");
        lastResultDescription = new String("");

    }

    public String getLastResultDescription() {
        return lastResultDescription;
    }

    public String getLastRequestDescription() {
        return lastRequestDescription;
    }

    public int Roll(int quantity, Die die, int modifier) {

        int result = 0;
        int oneRoll = 0;
        lastResultDescription = "";

        for (int i = 0; i < quantity; i++) {
            oneRoll = rand.nextInt(die.getValue()) + 1;

            // build the description of the roll result
            if (!lastResultDescription.isEmpty()) {
                lastResultDescription += "+";
            }
            lastResultDescription += Integer.toString(oneRoll);

            result += oneRoll;
        }
        if (modifier != 0) {
            result += modifier;
            lastResultDescription += "+" + Integer.toString(modifier);
        }

        lastRequestDescription = getRollDescription(quantity, die, modifier);

        return result;
    }

    /**
     *
     * @param quantity the number of dice to roll
     * @param die the kind of die to roll
     * @param modifier value to add to the total of all dice
     * @return String describing the roll requested, e.g. 3D6+4, 2D10
     */
    public String getRollDescription(int quantity, Die die, int modifier) {
        String result = "";
        if (quantity != 0) {
            result += Integer.toString(quantity);
        }
        result += die.getName();
        if (modifier != 0) {
            result += "+" + Integer.toString(modifier);
        }

        return result;
    }

}
