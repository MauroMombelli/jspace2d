package controller;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.StringTokenizer;

public class CommandObject {
	private String command;
	private ArrayList<String> arguments;
	private Class[] parametersArray;

	public CommandObject(String _commandAndArguments){
		arguments = new ArrayList<String>();
		processString(_commandAndArguments);
		createArgumentArray();
	}

	private void createArgumentArray() {
		setParametersArray(new Class[arguments.size()]);
		
		ListIterator<String> iterator = arguments.listIterator();
		int counter = 0;
		while(iterator.hasNext()){
			parametersArray[counter] = iterator.next().getClass();
			counter++;
		}
	}

	private void processString(String _commandAndArguments) {
		StringTokenizer tokenizer = new StringTokenizer(_commandAndArguments);
		
		if(tokenizer.hasMoreTokens()){
			setCommand(tokenizer.nextToken());
		}else{
			setCommand("NO COMMAND");
		}
		
		while(tokenizer.hasMoreTokens()){
			arguments.add(tokenizer.nextToken());
		}		
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCommand() {
		return command;
	}

	@SuppressWarnings("unused")
	private void setArguments(ArrayList<String> arguments) {
		this.arguments = arguments;
	}

	public ArrayList<String> getArguments() {
		return arguments;
	}

	private void setParametersArray(Class[] parametersArray) {
		this.parametersArray = parametersArray;
	}

	public Class[] getParametersArray() {
		return parametersArray;
	}

}
