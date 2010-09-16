package GUI.supportComponents;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import GUI.GUI;

public class ActionContainer {
	
	private GUI _GUI;
	private AbstractAction exitAction;
	private AbstractAction startServerAction;
	private AbstractAction stopServerAction;	
	private AbstractAction broadcastAction;
	private AbstractAction startPrivateChatAction;
	private AbstractAction banAction;
	private AbstractAction kickAction;
	
	
	public ActionContainer(GUI _GUI){
		this._GUI = _GUI;		
		setExitAction();
		setStartServerAction();
		setStopServerAction();
		setBroadcastAction();
		setStartPrivateChatAction();
		setBanAction();
		setKickAction();
	}


	protected GUI getGUI() {		
		return _GUI;
	}


	private void setExitAction() {
		this.exitAction = new AbstractAction("Exit", null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -1226178302931423268L;

			public void actionPerformed(ActionEvent evt) {
				getGUI().event("Exit", evt);
			}
		};
	}


	public AbstractAction getExitAction() {
		return exitAction;
	}


	private void setStopServerAction() {
		this.stopServerAction = new AbstractAction("Stop server", null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7796346215089121377L;

			public void actionPerformed(ActionEvent evt) {
				getGUI().event("Stop_server", evt);
			}
		};
	}


	public AbstractAction getStopServerAction() {
		return stopServerAction;
	}


	private void setStartServerAction() {
		this.startServerAction = new AbstractAction("Start server", null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2049120758237382403L;

			public void actionPerformed(ActionEvent evt) {				
				getGUI().event("Start_server", evt);
			}
		};
		startServerAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, "Start the server");;
	}


	public AbstractAction getStartServerAction() {
		return startServerAction;
	}


	private void setBroadcastAction() {
		this.broadcastAction = new AbstractAction("Broadcast", null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2663868059302355900L;

			public void actionPerformed(ActionEvent evt) {
				getGUI().event("Broadcast", evt);
			}
		};
		broadcastAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, "Send a short message to all clients (appears ingame)");;
	}


	public AbstractAction getBroadcastAction() {
		return broadcastAction;
	}


	private void setStartPrivateChatAction() {
		this.startPrivateChatAction = new AbstractAction("Start private chat", null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -1686949094033196879L;

			public void actionPerformed(ActionEvent evt) {
				getGUI().event("Start_PVT", evt);
			}
		};
		startPrivateChatAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, "Open a new chat window with the selected client");
	}


	public AbstractAction getStartPrivateChatAction() {
		return startPrivateChatAction;
	}


	private void setBanAction() {
		this.banAction = new AbstractAction("Ban player", null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7311111719876941067L;

			public void actionPerformed(ActionEvent evt) {
				getGUI().event("Ban", evt);
			}
		};
		banAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, "Ban the selected clients IP (permanently or temporarily)");
	}


	public AbstractAction getBanAction() {
		return banAction;
	}


	private void setKickAction() {
		this.kickAction = new AbstractAction("Kick player", null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 4764161490349297108L;

			public void actionPerformed(ActionEvent evt) {
				getGUI().event("Kick", evt);
			}
		};
		kickAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, "Kick the client from the game");
	}


	public AbstractAction getKickAction() {
		return kickAction;
	}
}
