#!/bin/bash

if [ -z "$SRC" ]
then 
	SRC=/home/hagish/workspace/adt/love-android/sdcard/ 
fi

if [ -z "$DST" ]
then 
	DST=/data/data/net.schattenkind.androidLove/files/ 
fi

if [ -z "$ADB" ]
then 
	ADB=$ANDROID_HOME/platform-tools/adb 
fi

######################################################

echo "using adb : $ADB"

for X in `find $SRC -type f`
do
	D=`echo $X|sed -e "s#$SRC#$DST#g"`
	echo "$X -> $D : "
	$ADB push $X $D
done
