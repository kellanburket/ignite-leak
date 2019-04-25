SHELL := /bin/bash

include envfile
export

.EXPORT_ALL_VARIABLES: docker-run docker-logs

lib:
	cd ignite-memory-leak-reproducer && make install
	make clean
	mkdir -p libs
	cp ignite-memory-leak-reproducer/target/scala-2.11/mira-lib-ignite-assembly-0.2.0.jar libs/mira-lib-ignite-assembly-0.2.0.jar

docker-build:
	docker build -t $(DOCKER_HUB_REPO) .

docker-clean:
	- docker stop $(DOCKER_APP)
	- docker rm $(DOCKER_APP)

docker-push: docker-build
	docker build -t $(DOCKER_HUB_REPO) .
	docker push $(DOCKER_HUB_REPO)

docker-run: docker-build docker-clean
	mkdir -p /tmp/ignite/storage
	mkdir -p /tmp/ignite/wal

	docker run \
		-v /tmp/ignite/storage:$(STORAGE_PATH) \
		-v /tmp/ignite/wal:$(WAL_PATH) \
		-e JAVA_HEAP_SIZE=$(JAVA_HEAP_SIZE) \
		-e WAL_PATH=$(WAL_PATH) \
		-e STORAGE_PATH=$(STORAGE_PATH) \
		-e DURABLE_MEMORY=$(DURABLE_MEMORY) \
		-e PERSISTENCE_ENABLED=$(PERSISTENCE_ENABLED) \
		-m $(CONTAINER_MEMORY_LIMIT) \
		--cpus=$(CPUS) \
		--memory-swappiness 0 \
		--name $(DOCKER_APP) \
		-d $(DOCKER_HUB_REPO)

docker-logs:
	docker logs -f $(DOCKER_APP)

docker-bash:
	docker exec -ti $(DOCKER_APP) bash

top:
	docker exec -ti $(DOCKER_APP) top

clean:
	-rm -rf libs