package de.Jodu555.NettyBackend.NettyBackend.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class CodeInterpreter {
	
	HashMap<String, Object> map;
	ArrayList<String> lines;
	ArrayList<String> finallines;
	
	public CodeInterpreter(HashMap<String, Object> map) {
		this.map = map;
	}
	
	public void interpret(ArrayList<String> lines) {
		this.lines = lines;
		this.finallines = new ArrayList<>();
		
		
	}
	
	private void interpretVariables(String line) {
		
	}
	
	private void interpretFor(String line) {
		
	}
	
	public String output() {
		return "<h1>Code Interpreter not Found</h1>";
	}
	
}
