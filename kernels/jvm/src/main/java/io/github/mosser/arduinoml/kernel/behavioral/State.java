package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.NamedElement;
import io.github.mosser.arduinoml.kernel.generator.Visitable;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class State implements NamedElement, Visitable {

    // Tone type
    public enum ToneTypeEnum {
        SHORT, LONG
    }

	private String name;
	private List<Action> actions = new ArrayList<Action>();
	private Transition transition;

	// List of Tones as Pair<PIN_ID, DURATION_MS>
	private List<Pair<Integer, Integer>> tones = new LinkedList<>();

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
     * Each Pair<PIN_ID, DURATION_MS>
     * @return List<Pair<Integer, Integer>>
     */
    public List<Pair<Integer, Integer>> getTones() {
        return tones;
    }

    /**
     * Add a new tone to the current tone sequence
     * Each Pair<PIN_ID, DURATION_MS>
     */
    public void addTone(Integer pin_id, ToneTypeEnum tone_type) {
        if (tone_type.equals(ToneTypeEnum.LONG)) {
            this.getTones().add(new Pair<>(pin_id, 1000));
        } else if (tone_type.equals(ToneTypeEnum.SHORT)) {
            this.getTones().add(new Pair<>(pin_id, 500));
        }
    }
}
