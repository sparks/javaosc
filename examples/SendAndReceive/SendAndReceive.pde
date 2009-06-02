import javaosc.*;

OSCPortOut out;
OSCPortIn in;

void setup() {
	size(200, 200);
	
	out = new OSCPortOut("192.168.5.81", 57120);
	in = new OSCPortIn(this, 57120);
	frameRate(2);
	noLoop();
}

void draw() {
	background(0);
}

void keyPressed() {
	Object[] args = {0, 0, 1, 1, 0, 0.01};
	OSCMessage msg = new OSCMessage("/rect", args);
	out.send(msg);
}

void OSCMessage(OSCMessage message) {
	println(message);
	Object[] args = message.getArguments();
	for(int i = 0;i < args.length;i++) {
		println(args[i].getClass().getName());
	}
}