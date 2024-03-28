public class ALUElement {

	//inputs
	public Wire[] aluOp;
	public Wire bInvert;
	public Wire a, b;
	public Wire carryIn;
	public Wire less;

	//mux
	private MUX8by1 mux;

	//outputs
	public Wire result;
	public Wire addResult;
	public Wire carryOut;

	// constructor
	public ALUElement() {

		//inputs
		aluOp = new Wire[3];
		for(int i = 0; i < 3; i++) {

			aluOp[i] = new Wire();

		}
		bInvert = new Wire();
		a = new Wire();
		b = new Wire();
		carryIn = new Wire();
		less = new Wire();

		//mux
		mux = new MUX8by1();

		//outputs
		result = new Wire();
		addResult = new Wire();
		carryOut = new Wire();

	}

	//execute the first pass of the ALUElement
	public void execute_pass1() {
		
		//setting control bits for the mux to match aluOp
		mux.control[0].set(aluOp[0].get());
		mux.control[1].set(aluOp[1].get());
		mux.control[2].set(aluOp[2].get());
		
		//set the first input bit of the mux to a && b
		mux.in[0].set(a.get() && b.get());
		//set the second input bit of the mux to a || b
		mux.in[1].set(a.get()|| b.get());
		//set the third input bit of the mux to a plus b plus carryIn (using sum of products)
		mux.in[2].set((!a.get() && !(b.get() ^ bInvert.get()) && carryIn.get()) || 
					  (!a.get() && (b.get() ^ bInvert.get()) && !carryIn.get()) || 
					  (a.get() && !(b.get() ^ bInvert.get()) && !carryIn.get()) || 
					  (a.get() && (b.get() ^ bInvert.get()) && carryIn.get()));
		//set the fifth input bit of the mux to a xor b
		mux.in[4].set((a.get() && !b.get()) || (!a.get() && b.get()));
		//set the sixth through eight bits of the mux to false since aluOp will never be 5, 6, or 7
		mux.in[5].set(false);
		mux.in[6].set(false);
		mux.in[7].set(false);
		
		//set addResult to a plus b plus carryIn (just copy from mux[2] since mux[2] IS the add result)
		addResult.set(mux.in[2].get());
		//calculate and set carryout using sum of products
		carryOut.set((!a.get() && (b.get() ^ bInvert.get()) && carryIn.get()) || 
					 (a.get() && !(b.get() ^ bInvert.get()) && carryIn.get()) || 
					 (a.get() && (b.get() ^ bInvert.get()) && !carryIn.get()) || 
					 (a.get() && (b.get() ^ bInvert.get()) && carryIn.get()));
		
	}

	//execute the second pass of the ALUElement
	public void execute_pass2() {
		
		//set the fourth input bit of the mux to less
		mux.in[3].set(less.get());
		//execute the mux and set result to the result of the mux
		mux.execute();
		result.set(mux.out.get());
		
	}

}
