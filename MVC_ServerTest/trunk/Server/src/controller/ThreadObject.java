package controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.Callable;

import model.AbstractModel;

public class ThreadObject implements Callable<String>{
	private CommandObject object;
	private ArrayList<AbstractModel> models;
	
	public ThreadObject(CommandObject temp, ArrayList<AbstractModel> models) {
		setObject(temp);
		setModels(models);
	}

	public void setObject(CommandObject object) {
		this.object = object;
	}

	public CommandObject getObject() {
		return object;
	}

	@Override
	public String call() throws Exception {
		System.out.println("Started processing: "+object.getCommand());
		
		AbstractModel c = searchModel();
		
		System.out.println("Finished processing: "+object.getCommand());
		return object.getCommand();
	}

	private AbstractModel searchModel() {
		ListIterator<AbstractModel> iterator = models.listIterator();
		
		while(iterator.hasNext()){
			AbstractModel temp = iterator.next();
			Class<? extends AbstractModel> c = temp.getClass();	
			Method temp1;
			try {
				temp1 = c.getDeclaredMethod(object.getCommand(), object.getParametersArray());
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				System.out.println("no such method");
				e.printStackTrace();
			}
				
		}
		return null;
	}

	public void setModels(ArrayList<AbstractModel> models) {
		this.models = models;
	}

	public ArrayList<AbstractModel> getModels() {
		return models;
	}
} 