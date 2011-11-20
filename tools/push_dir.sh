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

DEVICE=`$ADB devices|grep -v "List of devices"|awk '{print $1}'|head -n1`

if [ -z "$DB" ]
then 
	DB="$SRC/../tools/push_dir.$DEVICE.db"
fi

if [ -f "$ADB" ]
then
	echo "using adb : $ADB"
else
	echo "could not file ADB, please set ANDROID_HOME environment variable to your android sdk"
	exit 1
fi

######################################################

function file_already_there ()
{
	if [ -f $DB ]
	then
		cat $DB | grep "$1 $2" | wc -l 
	else
		echo 0
	fi
}

function sync_file ()
{
	echo "$1 $2 -> $3"
	$ADB push $2 $3
	T=`tempfile`
	touch "$DB"
	cat "$DB" | grep -v " $2" > $T
	echo "$1 $2" >> $T
	mv $T "$DB"
}

######################################################

echo "to retransmit everything remove '$DB'"

for X in `find $SRC -type f|grep -v .git`
do
	D=`echo $X|sed -e "s#$SRC#$DST#g"`
	#~ echo "$X -> $D : "
	#~ $ADB push $X $D
	MD5=`md5sum $X|awk '{print $1}'`
	C=$(file_already_there "$MD5" "$X")
	
	if [ "$C" -ne 1 ]
	then
		sync_file $MD5 $X $D
	fi
done

echo done


# ${workspace_loc:/love-android/tools/push_dir.sh}
# ADB /opt/android-sdk-linux_x86/platform-tools/adb
# SRC ${workspace_loc}/love-android/sdcard
