package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.NamedElement;
import io.github.mosser.arduinoml.kernel.generator.Visitable;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.Actuator;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class State implements NamedElement, Visitable {

	private String name;
	private List<Action> actions = new ArrayList<Action>();
	private Transition transition;

	// List of Tones as Pair<Actuator, DURATION_MS>
	private List<Pair<Actuator, Integer>> tones = new ArrayList<>();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public Transition getTransition() {
		return transition;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

    /**
     * Return the list of tones to play while entering the State
     * Key is the Actuator
     * Value is the duration in MS
     * @return List<Pair<Actuator, Integer>>
     */
    public List<Pair<Actuator, Integer>> getTones() {
        return tones;
    }

    /**
     * Add a new tone to the current tone sequence
     * Key is the Actuator
     * Value is the duration in MS
     * @param actuator
     * @param tone_type
     */
    public void addTone(Actuator actuator, String tone_type) {
        if (tone_type.toUpperCase().equals("LONG")) {
            this.getTones().add(new Pair<>(actuator, 1000));
        } else {
            // "SHORT"
            this.getTones().add(new Pair<>(actuator, 500));
        }
    }
}
