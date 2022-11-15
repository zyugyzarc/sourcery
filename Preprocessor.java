package sourcery;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.Charset;
import java.io.IOException;

import java.util.HashMap;
import java.util.Scanner;

public class Preprocessor{

	HashMap<String, String> macros = new HashMap<>();

	public String process(String filename) throws IOException{

		String source = Files.readString( Path.of(filename) );

		source = exec_macros(source);

		//System.out.printf("metacompiled: [%s]\n", source);

		return source;

	}

	Token scan(String source, int start){

		for(int i = start; i < source.length(); i++){

			if(source.charAt(i) == '{'){

				start = i;

			}

			if(source.charAt(i) == '}' && source.charAt(i-1) != '\\'){

				return new Token(source.substring(start+1, i).trim(), start, i+1);

			}

		}

		return null;

	}

	String exec_macros(String source){

		boolean complete;

		do{

			complete = true;
		
			int pos = 0;
			Token token;

			while(true){
				token = scan(source, pos + 1);

				if(token != null){
					pos = token.start + 1;

					try{
						source = source.substring(0, token.start) + macro(token, source) + source.substring(token.stop, source.length());
					}
					catch(AbortToken e){
						//System.out.printf("delayed %s\n", token.value);
						complete = false;
						pos += token.value.length() < 2 ? 0 : token.value.length() - 2;
					}

				}
				else{
					break;
				}

			}

		}while(!complete);

		return source;

	}

	String macro(Token token, String source) throws AbortToken{
		
		if(token.value.startsWith("!")){
			return " ";
		}

		else if(token.value.startsWith("TEXT ")){
			return macro_ascii(token);
		}

		else if(token.value.startsWith("DEF ")){

			Scanner sc = new Scanner(token.value); sc.next();

			this.macros.put( sc.next(), sc.nextLine() );
			return " ";
		}

		else if(token.value.startsWith("LABEL ")){

			//System.out.println("label " + token.value);

			Scanner sc = new Scanner(token.value); sc.next();

			this.macros.put( sc.next(), macro_label(token, source) );
			return " ";	
		}

		else{

			String key = token.value;

			if(key.startsWith("$")){
				key = key.substring(1, key.length());				
			}

			if(this.macros.containsKey(key)){
				return this.macros.get(key);
			}
			else{
				//System.out.printf("[val] %s abort\n", key);
				throw new AbortToken();
			}
		}
	}

	String macro_ascii(Token token){
		
		String out = "";
		
		token.value = token.value.substring(5, token.value.length());

		for(char i : token.value.toCharArray()){
			out += "".format( "%02X ", (int)i );
		}

		return out;
	}


	String macro_label(Token token, String source) throws AbortToken{
		
		Scanner sc = new Scanner(source.substring(0, token.start));
		int c = 0;

		while(sc.hasNext()){
			String t = sc.next();

			if(!t.contains("{$") && t.contains("{")){
				throw new AbortToken();
			}
			else if(t.contains("{")){
				while(!t.contains("}")){
					t = sc.next();
				}
			}

			c++;
		}

		return "#" + c;
	}
}