SHELL := /bin/bash

include envfile
export

SRC_FILES := $(shell find $(SRC_DIR) -name '*.java') $(shell find $(SRC_DIR) -name '*.scala')

VENDOR_FILES := build.sbt project/plugins.sbt
MAKE_FILES := Makefile envfile

APP_JAR := $(JAVA_APP)_$(SCALA_VERSION)-$(JAVA_APP_VERSION).jar
TARGET_JAR := $(TARGET_DIR)/scala-$(SCALA_VERSION)/$(APP_JAR)

# Install
install:
	sbt assembly

build/lib: $(VENDOR_FILES)
	mkdir -p build
	rm -rf build/lib
	mkdir -p build/lib
	sbt package
	find lib_managed -name '*.jar' -exec cp {} build/lib \;

build/app: build/lib $(SRC_FILES)
	sbt package
	mkdir -p build && touch build/app

# Utilities
clean:
	rm -rf build
	rm -rf lib_managed
	rm -rf target
	rm -rf project/target
	rm -rf project/project/target
	mkdir build
	sbt clean