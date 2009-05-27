import javaosc.*;

OSCPortOut out;
OSCPortIn in;

void setup() {
	size(200, 200);
	
	try {
		out = new OSCPortOut("127.0.0.1", 57120);
		in = new OSCPortIn(this, 57120);
	} catch (Exception e) {
		
	}
}

void draw() {
	background(0);
}

void mousePressed() {
	Object args[] = new Object[1];
	args[0] = 10;
	OSCMessage msg = new OSCMessage("/rect", args);
	try {
		out.send(msg);
	} catch (Exception e) {
		println("Couldn't send"+e);
	}
}

void OSCMessage(OSCMessage message) {
	println("Bang");
}