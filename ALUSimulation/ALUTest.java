public class ALUTest {

	public static void main(String args[]){

		//create a new ALU with 8 bits
		ALU alu = new ALU(8);

		//set the first input to 11 (00001011)
		alu.a[0].set(true);
		alu.a[1].set(true);
		alu.a[3].set(true);

		//set the second input to 37 (00100101)
		alu.b[0].set(true);
		alu.b[2].set(true);
		alu.b[5].set(true);

		//set the aluOp to 010 (add)
		alu.aluOp[1].set(true);

		//set bNegate to false
		alu.bNegate.set(false);

		//execute the ALU
		alu.execute();

		//print the result
		for(int i = 0; i < alu.result.length; i++) {

			System.out.println("Result[" + i + "] = " + alu.result[i].get());

		}
		String result = aluResultToBinary(alu.result);
		System.out.println("Result: 00001011(11) + 00100101(37) = " + result + "(" + binaryToDecimal(result) + ")");

	}

	private static String aluResultToBinary(Wire[] aluResult) {

		String result = "";

		for(int i = aluResult.length - 1; i >= 0; i--) {

			if(aluResult[i].get()) {

				result += "1";

			} else {

				result += "0";

			}

		}

		return result;

	}

	private static int binaryToDecimal(String binary) {

		int result = 0;
		int power = 0;

		for(int i = binary.length() - 1; i >= 0; i--) {

			if(binary.charAt(i) == '1') {

				result += Math.pow(2, power);

			}

			power++;

		}

		return result;

	}

}
