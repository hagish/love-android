#!/bin/bash

if [ -z "$SRC" ]
then 
	SRC=/home/hagish/workspace/adt/love-android/sdcard/ 
fi

if [ -z "$DST" ]
then 
	DST=/mnt/sdcard/love/ 
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
