package io.github.mosser.arduinoml.kernel.generator;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.structural.*;
import javafx.util.Pair;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Quick and dirty visitor to support the generation of Wiring code
 */
public class ToWiring extends Visitor<StringBuffer> {

	private final static String CURRENT_STATE = "current_state";

	public ToWiring() {
		this.result = new StringBuffer();
	}

	private void w(String s) {
		result.append(String.format("%s\n",s));
	}

	@Override
	public void visit(App app) {
		w("// Wiring code generated from an ArduinoML model");
		w(String.format("// Application name: %s\n", app.getName()));

		w("void setup(){");
		for(Brick brick: app.getBricks()){
			brick.accept(this);
		}
		w("}\n");

		w("long time = 0; long debounce = 200;\n");
		w( "bool entry = true;\n");

		for(State state: app.getStates()){
			state.accept(this);
		}

		if (app.getInitial() != null) {
			w("void loop() {");
			w(String.format("  state_%s();", app.getInitial().getName()));
			w("}");
		}
	}

	@Override
	public void visit(Actuator actuator) {
	 	w(String.format("  pinMode(%d, OUTPUT); // %s [Actuator]", actuator.getPin(), actuator.getName()));
	}


	@Override
	public void visit(Sensor sensor) {
		w(String.format("  pinMode(%d, INPUT);  // %s [Sensor]", sensor.getPin(), sensor.getName()));
	}

	@Override
	public void visit(State state) {
		w(String.format("void state_%s() {",state.getName()));

		w("if(entry) {");
        for (Pair<Actuator, Integer> tone : state.getTones()) {
            // Build entry tone sequence
            // For each tone in the sequence
            w("  tone(" + tone.getKey().getPin() + ", 880);");
            w("  delay(" + tone.getValue() + ");"); // DURATION_MS
            w("  noTone(" + tone.getKey().getPin() + ");");
            w("  delay(500);");
        }
		w("}");

		for(Action action: state.getActions()) {
			action.accept(this);
		}

		if (state.getTransition() != null) {
			w("  boolean guard = millis() - time > debounce;");
			context.put(CURRENT_STATE, state);
			state.getTransition().accept(this);
			w("}\n");
		}
	}

	@Override
	public void visit(Transition transition) {
		Set<Map.Entry<Sensor, SIGNAL>> conditions = transition.getAnd_conditions().entrySet();

		w("  if(");
		Iterator<Map.Entry<Sensor, SIGNAL>> it = conditions.iterator();
		while (it.hasNext()) {
			Map.Entry<Sensor, SIGNAL> cond = it.next();
			w(String.format(" digitalRead(%d) == %s ",
					cond.getKey().getPin(),cond.getValue()));
			if (it.hasNext()) w(" && ");
		}
		w("&& guard ) {");

		w("    time = millis();");
		w("    entry = true;");
		w(String.format("    state_%s();",transition.getNext().getName()));
		w("  } else {");
		w("    entry = false;");
		w(String.format("    state_%s();",((State) context.get(CURRENT_STATE)).getName()));
		w("  }");
	}

	@Override
	public void visit(Action action) {
		w(String.format("  digitalWrite(%d,%s);",action.getActuator().getPin(),action.getValue()));
	}

}
