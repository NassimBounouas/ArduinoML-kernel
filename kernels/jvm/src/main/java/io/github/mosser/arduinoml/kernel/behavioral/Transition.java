package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitable;
import io.github.mosser.arduinoml.kernel.generator.Visitor;
import io.github.mosser.arduinoml.kernel.structural.*;

import java.util.HashMap;

public class Transition implements Visitable {

	private State next;
	//private Sensor sensor;
	//private SIGNAL value;
	private HashMap<Sensor, SIGNAL> and_conditions = new HashMap();

	public State getNext() {
		return next;
	}

	public void setNext(State next) {
		this.next = next;
	}

	/*
	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public SIGNAL getValue() {
		return value;
	}

	public void setValue(SIGNAL value) {
		this.value = value;
	}
	*/

	public HashMap<Sensor, SIGNAL> getAnd_conditions() {
		return and_conditions;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
