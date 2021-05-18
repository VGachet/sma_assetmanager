package fr.smartapps.lib;

import android.content.res.ColorStateList;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SMAStateListColor {

    /*
    Attributes
     */
    //int[][] states;
    //int[] colors;
    List<Integer> states = new ArrayList<>();
    List<Integer> colors = new ArrayList<>();

    /*
    Constructor
     */
    public SMAStateListColor() {
    }

    /*
    Enabled / Disabled
     */
    public SMAStateListColor enabled(String color) {
        states.add(android.R.attr.state_enabled);
        colors.add(Color.parseColor(color));
        return this;
    }

    public SMAStateListColor disabled(String color) {
        states.add(-android.R.attr.state_enabled);
        colors.add(Color.parseColor(color));
        return this;
    }

    /*
    Focused / Unfocused
     */
    public SMAStateListColor focused(String color) {
        states.add(android.R.attr.state_focused);
        colors.add(Color.parseColor(color));
        return this;
    }

    public SMAStateListColor unfocused(String color) {
        states.add(-android.R.attr.state_focused);
        colors.add(Color.parseColor(color));
        return this;
    }

    /*
    Windows focused / Windows unfocused
     */
    public SMAStateListColor windowFocused(String color) {
        states.add(android.R.attr.state_window_focused);
        colors.add(Color.parseColor(color));
        return this;
    }

    public SMAStateListColor windowUnfocused(String color) {
        states.add(-android.R.attr.state_window_focused);
        colors.add(Color.parseColor(color));
        return this;
    }

    /*
    Selected / Unselected
     */
    public SMAStateListColor selected(String color) {
        states.add(android.R.attr.state_selected);
        colors.add(Color.parseColor(color));
        return this;
    }

    public SMAStateListColor unselected(String color) {
        states.add(-android.R.attr.state_selected);
        colors.add(Color.parseColor(color));
        return this;
    }

    /*
    Pressed / Unpressed
     */
    public SMAStateListColor pressed(String color) {
        states.add(android.R.attr.state_pressed);
        colors.add(Color.parseColor(color));
        return this;
    }

    public SMAStateListColor unpressed(String color) {
        states.add(-android.R.attr.state_pressed);
        colors.add(Color.parseColor(color));
        return this;
    }

    /*
    Checked / Unchecked
     */
    public SMAStateListColor checked(String color) {
        states.add(android.R.attr.state_checked);
        colors.add(Color.parseColor(color));
        return this;
    }

    public SMAStateListColor unchecked(String color) {
        states.add(-android.R.attr.state_checked);
        colors.add(Color.parseColor(color));
        return this;
    }

    /*
    Default states (add it at the end because it will add all the inverse states that has not been added
     */
    public ColorStateList inverse(String color) {
        int[][] stateList;
        int[] colorList;
        stateList = new int[states.size() + 1][states.size() + 1];
        colorList = new int[colors.size() + 1];

        for(int position = 0; position < states.size(); position++) {
            stateList[position] = new int[]{states.get(position)};
            colorList[position] = colors.get(position);
        }

        stateList[states.size()] = new int[] {};
        colorList[states.size()] = Color.parseColor(color);

        return new ColorStateList(stateList, colorList);
    }
}
