#!/usr/bin/env bash

IGNITE_LOG=/var/log/ignite.log
OPTION_LIBS=ignite-kubernetes,ignite-zookeeper,ignite-aws,$OPTION_LIBS
export OPTION_LIBS

JVM_OPTS="-XX:NativeMemoryTracking=detail -Xms$JAVA_HEAP_SIZE -Xmx$JAVA_HEAP_SIZE -XX:+AlwaysPreTouch -XX:+UseG1GC -XX:+ScavengeBeforeFullGC -XX:MaxDirectMemorySize=$IGNITE_MAX_DIRECT_MEMORY -Duser.timezone=GMT"
export JVM_OPTS

function parse_config {
	f=$1
	perl -p -i -e 's/\$\{([^}]+)\}/defined $ENV{$1} ? $ENV{$1} : $&/eg' $f
}

parse_config $CONFIG_URI

if [ -d "$STORAGE_PATH" ]; then
	cd $STORAGE_PATH
	rm -Rf -- */
fi
if [ -d "$WAL_PATH" ]; then
	cd $WAL_PATH
	rm -Rf -- */
fi
if [ -d "$WORK_PATH" ]; then
	cd $WORK_PATH
	rm -Rf -- */
fi

if [ ! -z "$OPTION_LIBS" ]; then
  IFS=, LIBS_LIST=("$OPTION_LIBS")

  for lib in ${LIBS_LIST[@]}; do
    cp -r $IGNITE_HOME/libs/optional/"$lib" \
        $IGNITE_HOME/libs/
  done
fi

nohup $IGNITE_HOME/bin/ignite.sh $IGNITE_HOME/config/default-config.xml >> $IGNITE_LOG 2>&1 &

if $PERSISTENCE_ENABLED; then
	while true; do
		E=$($IGNITE_HOME/bin/control.sh --activate | grep Error)
		if [[ -z $E ]]; then
			break
		else
			echo "Returned Status $?"
		fi
	done
fi
tail -f $IGNITE_LOG
