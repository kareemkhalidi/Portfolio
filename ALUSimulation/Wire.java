public class Wire {

	boolean value;

	// sets the wires value to the provided value
	public void set(boolean value) {
		this.value = value;
	}

	// gets the wires value
	public boolean get() {
        	return value;
	}

	// initializes the wire with value 0 (false)
	public Wire(){
		this.value = false;
	}

}
