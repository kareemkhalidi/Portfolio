public class MUX8by1 {

	//inputs
	public Wire[] control;
	public Wire[] in;

	//output
	public Wire out;

	// 
	public MUX8by1() {

		//inputs
		control = new Wire[3];
		for(int i = 0; i < 3; i++) {

			control[i] = new Wire();

		}

		in = new Wire[8];
		for(int i = 0; i < 8; i++) {

			in[i] = new Wire();

		}

		//output
		out = new Wire();

	}

	//execute the mux
	public void execute() {

		//set the output of the mux to a sum of products of the control bits with the 8 input bits
		out.set((!control[0].get() && !control[1].get() && !control[2].get() && in[0].get()) || 
				(control[0].get() && !control[1].get() && !control[2].get() && in[1].get()) || 
				(!control[0].get() && control[1].get() && !control[2].get() && in[2].get()) || 
				(control[0].get() && control[1].get() && !control[2].get() && in[3].get()) || 
				(!control[0].get() && !control[1].get() && control[2].get() && in[4].get()) || 
				(control[0].get() && !control[1].get() && control[2].get() && in[5].get()) || 
				(!control[0].get() && control[1].get() && control[2].get() && in[6].get()) || 
				(control[0].get() && control[1].get() && control[2].get() && in[7].get()));
		
	}
	
}
