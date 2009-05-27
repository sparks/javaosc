import javaosc.*;

OSCPortOut out;
OSCPortIn in;

void setup() {
	size(200, 200);
	
	out = new OSCPortOut("127.0.0.1", 57120);
	in = new OSCPortIn(this, 57120);
}

void draw() {
	background(0);
}

void mousePressed() {
	Object[] args = {1, 2, "Hello"};
	OSCMessage msg = new OSCMessage("/test", args);
	out.send(msg);
}

void OSCMessage(OSCMessage message) {
	println(message);
}