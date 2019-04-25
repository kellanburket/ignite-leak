FROM anapsix/alpine-java:8u202b08_jdk

# Ignite version
ENV IGNITE_VERSION 2.7.0

# Ignite home
ENV IGNITE_HOME /opt/ignite/apache-ignite-${IGNITE_VERSION}-bin
RUN apk update && apk add unzip curl
WORKDIR /opt/ignite
RUN curl https://dist.apache.org/repos/dist/release/ignite/${IGNITE_VERSION}/apache-ignite-${IGNITE_VERSION}-bin.zip -o ignite.zip && unzip ignite.zip && rm ignite.zip

RUN apk update && apk --no-cache add perl bash 
# ca-certificates wget && wget -q -O /etc/apk/keys/sgerrand.rsa.pub https://alpine-pkgs.sgerrand.com/sgerrand.rsa.pub && wget https://github.com/sgerrand/alpine-pkg-glibc/releases/download/2.28-r0/glibc-2.28-r0.apk && apk add glibc-2.28-r0.apk

ADD libs/mira-lib-ignite-assembly-0.2.0.jar $IGNITE_HOME/libs/mira-lib-ignite-assembly-0.2.0.jar

ENV CONFIG_HOME $IGNITE_HOME/config
ENV CONFIG_URI $CONFIG_HOME/default-config.xml

RUN rm -f $CONFIG_URI
ADD config/default-config.xml $CONFIG_URI
ADD init/init.sh $IGNITE_HOME/bin/init.sh

ENV DURABLE_MEMORY 21474836480
ENV PERSISTENCE_ENABLED true
ENV STORAGE_PATH /var/lib/ignite/data/db
ENV WORK_PATH /var/lib/ignite/data/work
ENV WAL_PATH /var/lib/ignite/data/wal
ENV WAL_ARCHIVE_PATH /var/lib/ignite/data/wal/archive
ENV WAL_MODE LOG_ONLY
ENV JAVA_HEAP_SIZE 24G
ENV HEAP_DUMP_PATH /dumps/oom.bin
ENV DEFAULT_DATA_REGION_PERSISTENCE_ENABLED true
ENV JDK_MAX_CACHED_BUFFER_SIZE 262144
ENV IGNITE_MAX_DIRECT_MEMORY 256M

CMD $IGNITE_HOME/bin/init.sh