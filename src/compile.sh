#!/bin/bash

mkdir ../bin;
javac -d ../bin/ -classpath /Applications/Processing.app/Contents/Resources/Java/core.jar *.java utility/*.java;
cd ../bin;
jar -cf javaosc.jar javaosc;
rm ../library/javaosc.jar;
mv javaosc.jar ../library/;
rm -r ../bin;
