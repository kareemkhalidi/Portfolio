public class ALU {

	//inputs
	public Wire[] aluOp;
	public Wire bNegate;
	public Wire[] a, b;

	//alu elements array
	private ALUElement[] alu;

	//size of alu
	private int x;

	//result
	public Wire[] result;

	//initialize the ALU
	public ALU(int x) {

		//inputs
		aluOp = new Wire[x];
		bNegate = new Wire();
		a = new Wire[x];
		b = new Wire[x];
		for(int i = 0; i < x; i++) {

			aluOp[i] = new Wire();
			a[i] = new Wire();
			b[i] = new Wire();

		}

		//alu elements array
		alu = new ALUElement[x];
		for(int i = 0; i < x; i++) {

			alu[i] = new ALUElement();

		}

		//size of alu
		this.x = x;

		//result
		result = new Wire[x];
		for(int i = 0; i < x; i++) {

			result[i] = new Wire();

		}

	}

	//execute the ALU
	public void execute() {
		
		//set the inputs for the least significant alu element and execute the first pass with an initial carryIn of bNegate
		alu[0].aluOp[0].set(aluOp[0].get());
		alu[0].aluOp[1].set(aluOp[1].get());
		alu[0].aluOp[2].set(aluOp[2].get());
		alu[0].bInvert.set(bNegate.get());
		alu[0].a.set(a[0].get());
		alu[0].b.set(b[0].get());
		alu[0].carryIn.set(bNegate.get());
		alu[0].execute_pass1();
		
		//set the inputs and then execute the first pass for all the other 
		//alu elements with the carry in being the carry out from the previous alu element
		for(int i = 1; i < x; i++) {
			
			alu[i].aluOp[0].set(aluOp[0].get());
			alu[i].aluOp[1].set(aluOp[1].get());
			alu[i].aluOp[2].set(aluOp[2].get());
			alu[i].bInvert.set(bNegate.get());
			alu[i].a.set(a[i].get());
			alu[i].b.set(b[i].get());
			alu[i].carryIn.set(alu[i - 1].carryOut.get());
			alu[i].execute_pass1();
			
		}
		
		//set the less input of the first alu to the addResult of the most significant bit
		//and then set the rest of the less inputs to false
		alu[0].less.set(alu[x - 1].addResult.get());
		for(int i = 1; i < x; i++) {
			
			alu[i].less.set(false);
			
		}
		
		//finally, execute pass 2 and then set the result of the alu to the results of all the alu elements
		for(int i = 0; i < x; i++) {
			
			alu[i].execute_pass2();
			result[i].set(alu[i].result.get());
			
		}
		
	}
	
}
