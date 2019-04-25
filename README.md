# Run from anywhere

docker run -v $LOCAL_STORAGE:$CONTAINER_STORAGE -v $LOCAL_WAL:$CONTAINER_WAL -m 40G --cpus=12 --memory-swappiness 0 --name ignite.leak -d miraco/ignite:leak
