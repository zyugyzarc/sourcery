package sourcery;

import java.util.*;
import java.io.*;

public class main{

	public static void main(String[] args){
		
		String filename = args[0];
		
		Sourcery interpreter = new Sourcery();

		interpreter.run( filename );

	}

}