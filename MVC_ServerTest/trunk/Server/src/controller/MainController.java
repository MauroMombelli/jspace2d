package controller;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import view.AbstractView;

import model.AbstractModel;
import model.TestModel;

import GUI.GUI;
import GUI.supportComponents.ObservableFlag;

public class MainController implements Observer{

	public static int argumentsLimit = 200;
	
	private ArrayList<AbstractModel> models;
	private ExecutorService executor = Executors.newCachedThreadPool();
	private ArrayList<AbstractView> views;
	private CommandProcessingThread processingThread;
	private LinkedBlockingQueue<CommandObject> commandQueue = new LinkedBlockingQueue<CommandObject>();
	//private LinkedBlockingQueue<ResponseObject> responseQueue = new LinkedBlockingQueue<ResponseObject>();

	public static void main(String[] args) {
		new MainController();
	}

	public MainController(){
		initializeProcessingThread();
		views = new ArrayList<AbstractView>();
		models = new ArrayList<AbstractModel>();

		addView(new GUI());
		addModel(new TestModel());
		
		System.out.println("initialized GUI. now running on: "+Thread.currentThread().getName());		
	}

	private void addModel(AbstractModel model) {
		models.add(model);
		model._observableFlag.addObserver(this);
	}

	private void addView(AbstractView view) {
		views.add(view);
		view._observableFlag.addObserver(this);
	}

	private void initializeProcessingThread() {
		setProcessingThread(new CommandProcessingThread(commandQueue));
		new Thread(getProcessingThread()).start();
	}

	public void setProcessingThread(CommandProcessingThread processingThread) {
		this.processingThread = processingThread;
		this.processingThread._observableFlag.addObserver(this);
	}

	public CommandProcessingThread getProcessingThread() {
		return processingThread;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(((ObservableFlag)arg0).getOwner() instanceof AbstractView){
			manageViewEvents(arg0, arg1);			
		}else{
			if(((ObservableFlag)arg0).getOwner() instanceof ProcessingThread){
				manageProcessingEvents(arg0, arg1);			
			}
		}
	}

	private void manageProcessingEvents(Observable arg0, Object arg1) {
		executor.submit(new ThreadObject((CommandObject) arg1,models));
	}

	private void manageViewEvents(Observable arg0, Object arg1) {
		try {
			commandQueue.put(new CommandObject((String) arg1));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
