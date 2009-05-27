/* $Id$
 * Created on 28.10.2003
 */
package javaosc.utility;

import javaosc.*;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import processing.core.PApplet;
import java.lang.reflect.Method;

/**
 * @author cramakrishnan
 *
 * Copyright (C) 2003, C. Ramakrishnan / Auracle
 * All rights reserved.
 * 
 * See license.txt (or license.rtf) for license information.
 * 
 * Dispatches OSCMessages to registered listeners.
 * 
 */

public class OSCPacketDispatcher {
	// use Hashtable for JDK1.1 compatability
	private Hashtable<String, OSCListener> addressToClassTable = new Hashtable<String, OSCListener>();
	PApplet parent;
	Method[] eventMethods;
	/**
	 * 
	 */
	public OSCPacketDispatcher(PApplet parent) {
		this.parent = parent;
		eventMethods = new Method[2];
		try {
			eventMethods[0] = parent.getClass().getMethod("OSCMessage", new Class[] { Date.class, OSCMessage.class});
		} catch (Exception e) {
			// no such method, or an error.. which is fine, just ignore
		}
		try {
			eventMethods[1] = parent.getClass().getMethod("OSCMessage", new Class[] { OSCMessage.class});
		} catch (Exception e) {
			// no such method, or an error.. which is fine, just ignore
		}
	}
	
	public OSCPacketDispatcher() {
		this.parent = parent;
		eventMethods = new Method[2];
		eventMethods[0] = null;
		eventMethods[1] = null;
	}

	public void addListener(String address, OSCListener listener) {
		addressToClassTable.put(address, listener);
	}
	
	public void dispatchPacket(OSCPacket packet) {
		if (packet instanceof OSCBundle)
			dispatchBundle((OSCBundle) packet);
		else
			dispatchMessage((OSCMessage) packet);
	}
	
	public void dispatchPacket(OSCPacket packet, Date timestamp) {
		if (packet instanceof OSCBundle)
			dispatchBundle((OSCBundle) packet);
		else
			dispatchMessage((OSCMessage) packet, timestamp);
	}
	
	private void dispatchBundle(OSCBundle bundle) {
		Date timestamp = bundle.getTimestamp();
		OSCPacket[] packets = bundle.getPackets();
		for (int i = 0; i < packets.length; i++) {
			dispatchPacket(packets[i], timestamp);
		}
	}
	
	private void dispatchMessage(OSCMessage message) {
		dispatchMessage(message, null);
	}
	
	private void dispatchMessage(OSCMessage message, Date time) {
		Enumeration keys = addressToClassTable.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			// this supports the OSC regexp facility, but it
			// only works in JDK 1.4, so don't support it right now
			if (message.getAddress().matches(key) || key == "") {
				OSCListener listener = (OSCListener) addressToClassTable.get(key);
				listener.acceptMessage(time, message);
			}
		}
		if(eventMethods[0] != null) {
			try {
				eventMethods[0].invoke(parent, new Object[] { time, message });
			} catch (Exception e) {
				System.err.println("\nJavaOSC event method 0 disabled...");
				e.printStackTrace();
				eventMethods[0] = null;
			}
		}
			if(eventMethods[1] != null) {
				try {
					eventMethods[1].invoke(parent, new Object[] { message });
				} catch (Exception e) {
					System.err.println("\nJavaOSC event method 0 disabled...");
					e.printStackTrace();
					eventMethods[1] = null;
				}
			}
	}
}
