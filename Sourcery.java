package sourcery;

import java.util.*;
import java.io.*;

public class Sourcery{

	Integer[] source;
	HashMap<String, Integer> instruction = new HashMap<>();

	public void run( String filename ){
		
		init_instruct();
		init_mem(filename);

		execute();

	}

	void init_instruct(){

		/**
		 	Operations:

		 	0xD0 "<<" | << x   | print out
		 	0xD1 "<-" | <- x   | print ascii

		 	0xD2 "++" | ++ x y | add `y` to value at $x
		 	0xD4 "--" | -- x y | subtract `y` to value at $x
 
		 	0xD3 "==" | == x y | set value at $x to y

		 	0xD5 "0?" | 0? x y | if value at $x is zero, set it to `y`. else set it to `0`.

		**/

		instruction.put("<<", 0xd0);
		instruction.put("<-", 0xd1);

		instruction.put("==", 0xd3);

		instruction.put("++", 0xd2);
		instruction.put("--", 0xd4);

		instruction.put("0?", 0xd5);
	}

	void init_mem( String filename ){

		// initialize memory

		try{

			Scanner s = new Scanner(
				preprocess(filename)
			);

			ArrayList<Integer> buf = new ArrayList<>();

			while(s.hasNext()){
				buf.add( parse_byte( s.next() ) );
			}

			this.source = buf.toArray( new Integer[0] );

		} catch (IOException e){
			System.out.println("File Not Found");
		}

	}

	String preprocess(String filename) throws IOException{

		// preprocess the file, apply all macros

		return new Preprocessor().process(filename);

	}

	int parse_byte(String s){

		// parse a token from the source file, and return it as an encoded int

		if(this.instruction.containsKey(s)){
			return this.instruction.get(s);
		}
		else if(s.charAt(0) == '$'){
			return 1024 + parse_byte( s.substring(1, s.length()) );
		}
		else if(s.charAt(0) == '#'){
			return Integer.parseInt(s.substring(1, s.length()));
		}
		else{
			return Integer.parseInt(s, 16);	
		}

	}

	int getMem(int pc){

		// gets the value (in memory) at address `$pc`

		int val = this.source[pc];
		int ptr = val;

		for(int i = 0; i < ptr/1024; i++){

			val = this.source[val % 1024];

		}

		return val;
	}

	void step(){
		
		// run one instruction

		int pc_prev, pc;

		pc_prev = this.source[0];
		pc = pc_prev;

		//System.out.printf("[exec] %02X (%d)\n", this.source[pc], pc);

		if( getMem(pc) - this.instruction.get("<<") == 0){

			pc++;
			System.out.printf("%02X\n", getMem(pc) );

		}

		else if( getMem(pc) - this.instruction.get("<-") == 0){

			pc++;
			System.out.printf("%c", getMem(pc) );

		}

		else if( getMem(pc) - this.instruction.get("++") == 0){

			pc+=2;

			this.source[ getMem(pc-1) ] +=getMem(pc);

		}

		else if( getMem(pc) - this.instruction.get("--") == 0){

			pc+=2;

			this.source[ getMem(pc-1) ] -=getMem(pc);

		}

		else if( getMem(pc) - this.instruction.get("0?") == 0){

			pc+=2;

			this.source[ getMem(pc-1) ] = (this.source[ getMem(pc-1) ] == 0) ? getMem(pc) : 0;

		}

		else if( getMem(pc) - this.instruction.get("==") == 0){

			pc+=2;
			//System.out.printf("write [%d %d]\n", this.source[pc-1], getMem(pc));
			
			this.source[ getMem(pc-1) ] = getMem(pc);

		}

		else if( getMem(pc) == 0){

		}

		else{
			System.out.println("NOP");
		}

		if(pc_prev == this.source[0]){

			// pc has not changed
			this.source[0] = pc + 1;

		}

	}

	void execute(){

		// execute program in memory

		do{

			this.step();
		
		} while( -1 < this.source[0] &&  this.source[0] < this.source.length );

	}

}