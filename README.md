# PRIoTE

## About 

A project that containerises GCHQ's HQDM and Magma Core

- [HQDM](https://github.com/gchq/HQDM.git)
- [Magma Core](https://github.com/gchq/MagmaCore.git)

---

## Implementation

To create the container for this environment, we first determine requirements of the two projects, [HQDM](https://github.com/gchq/HQDM.git) and [Magma Core](https://github.com/gchq/MagmaCore.git). All that is required is [Apache Maven](https://maven.apache.org/), and [Java Development Kit version 15](https://openjdk.java.net/projects/jdk/15/).
Luckily for us, there is already a docker image for this which we can use: `maven:maven:3.8.1-openjdk-15`, so we use this image with the line `FROM maven:maven:3.8.1-openjdk-15` in our [Dockerfile](Container/Dockerfile)

Now that we have a base image available for usage, we must add in the files for [HQDM](https://github.com/gchq/HQDM.git) and [Magma Core](https://github.com/gchq/MagmaCore.git). To do this, we clone each repository into a new subfolder `files`. We then add the line `COPY files/ /usr/rsc/mymaven` to our [Dockerfile](Container/Dockerfile) such that our files are copied into our new image. We also add the line `RUN mv files/CustomJava/ /usr/src/mymaven/MagmaCore/src/main/java/ || :` such that our custom java files are moved to the correct location and compiled, allowing us to use our own database system and modelling storage location. We declare our `WORKDIR` as `/usr/src/mymaven` such that we are working from this directory upon boot.

Next, environmental variables must be declared in our [Dockerfile](Container/Dockerfile) to ensure our stripped image is able to successfully locate the appropriate programs and files for successful running.

Finally, we must write two scripts, one to be ran on building of the image, and one to be ran on running of the container. The first of these, [build.sh](Container/files/build.sh), compiles the two projects in the work directory using maven. Whilst the latter, [entrypoint.sh](Container/files/entrypoint.sh), creates some symbolic links for ease of use once the image has been stripped, and then simply runs the [Magma Core](https://github.com/gchq/MagmaCore.git) application using maven. To add these scripts appropriately, we use the `RUN` and `ENTRYPOINT` keywords respectively.

---

## Installation

Prior to any installation, ensure that your repository listings, software packages are updated and pre-requisites are installed.

---

### Pre-requisites
- [Docker](https://docs.docker.com/engine/install)
- Git
- Make
- Policycoreutils
- Selinux-basics
- Selinux-utils

---

### Container

1. Clone this repository:
    - `git clone ssh://git@github.com/SylinsicWMG/priote.git`
2. CD into the new folder for this repository:
    - `cd priote`
3. Clone submodules of this repository:
    - `git submodule update --init --recursive --force --checkout`
4. Launching the container can be done either manually or automatically:
    - To launch manually:
        1. CD into the Container directory:
            - `cd Container`
        2. Build the docker image:
            - `docker build -t priote_i:1.0 -t priote_i:latest .`
        3. Strip the image down to all that is required:
            - ```
              scripts/strip-image \
                -i priote_i:latest \
                -t priote_stripped_i:latest \
                -d Dockerfile \
                -x 3330 \
                -f /etc/group \
                -f /etc/nsswitch.conf \
                -f /etc/passwd \
                -f /root/.m2/ \
                -f /usr/bin/basename \
                -f /usr/bin/bash \
                -f /usr/bin/cd \
                -f /usr/bin/coreutils \
                -f /usr/bin/dirname \
                -f /usr/bin/echo \
                -f /usr/bin/expr \
                -f /usr/bin/ln \
                -f /usr/bin/ls \
                -f /usr/bin/pwd \
                -f /usr/bin/readlink \
                -f /usr/bin/sh \
                -f /usr/bin/test \
                -f /usr/bin/tr \
                -f /usr/bin/uname \
                -f /usr/java/openjdk-15/ \
                -f /usr/lib64/libc.so.6 \
                -f /usr/lib64/libm.so.6 \
                -f '/usr/lib64/libnss*' \
                -f /usr/lib64/libz.so.1 \
                -f /usr/share/maven/ \
                -f /usr/src/mymaven/```
        4. Compile and insert the SELinux policy and port for the container:
            - `cd policies`
            - `sudo make -f /usr/share/selinux/devel/Makefile priote.pp`
            - `sudo semodule -i priote.pp`
            - `sudo semanage port -a -t priote_port_in_t -p tcp 3330`
        4. Run the stripped image:
            - `docker run -d -p 3330:3330 --cap-drop=all --security-opt seccomp=policies/seccomp.json --security-opt label:type:priote_t --name priote_c -v "${PWD}/data":/usr/src/mymaven/data:rw,Z  priote_stripped_i:latest`
    - To launch with an automatic script:
        1. CD into the Container directory:
            - `cd Container`
        2. Run the launch script:</li>
            - `./launch.sh`

---

## Container Operation

The container runs [entrypoint.sh](Container/files/entrypoint.sh) by default upon boot, which launches the application. No user input is required to simply run the container as is.

- Running
    - `docker run -d -p 3330:3330 --cap-drop=all --security-opt seccomp=policies/seccomp.json --security-opt label:type:priote_t --name priote_c -v "${PWD}/data":/usr/src/mymaven/data:rw,Z  priote_stripped_i:latest`
- Killing
    - `docker kill priote_c`
- Starting
    - `docker start priote_c`
- Stopping
    - `docker stop priote_c`
- Restarting
    - `docker restart priote_c`

---

## Hardening

---

### Capabilities

To limit potential exploit points of the container against the host, all capabilities of the container are dropped as they are unnecessary and not required for successful operation.

---

### Seccomp

Seccomp is utilised to limit the system calls available for usage by the container. 

An `strace` binary along with the required libraries was copied into the container, and then utilised to obtain a list of used system calls within the execution process, i.e. `strace -f ./entrypoint.sh -o calls`. This list of calls was then formatted and filtered to obtain the raw list of system call names before being inserted into the [Seccomp profile](Container/policies/seccomp.json).

---

### SELinux

SELinux policies have been implemented to limit the container's abilities to the task it is designed to do. Only specified system calls are able to be made on specified labels.
 For docker to function with SELinux, we must first install SELinux on the host machine and enable it:
    - `sudo apt install policycoreutils selinux-utils selinux-basics`
    - `sudo selinux-activate`
    - `reboot`

Following the installation of SELinux on the host system, we must modify Docker's service file to enable SELinux with the `--selinux-enabled` switch. The docker service file is system dependent, however it can be found with:
    - `sudo find / -name "docker.service"`
    
A common place for this file to reside is `/usr/lib/systemd/system/docker.service`.
Once this file has been located, we must open this file in our chosen editor, and append `--selinux-enabled` to the line responsible for the starting/execution of the service, usually `ExecStart`.

Following the enabling of SELinux in docker, we must restart the docker service with the reloaded service file:
1. sudo systemctl daemon-reload
2. systemctl restart docker

To create the SELinux profile, a blank policy is created in `permissive` mode. [This policy](Container/policies/priote.te) was compiled and then utilised as a security option upon launch of the container. This causes notifications to be thrown in `/var/log/audit/audit.log`. However, this isn't particularly easy on the eye to read, so instead, we use the `ausearch` command line tool to identify the latest errors, then pipe these through to the `audit2allow` command which prints them in a form ready for direct insertion into the policy. This command sequence has been created as the script ['scripts/check_auditlog.sh'](Container/scripts/check_auditlog.sh).

Once notifications are thrown in the audit log, we insert the required policy lines into our policy file and increase our policy version by 0.1, then re-compile and insert it. At each stage, we also attempt to access as many of the functions of the container as possible, i.e. processing a query on the interface. We then relaunch the container with the new policy and perform this process repeatedly until we are no longer thrown any new notifications. Once we reach this point, we remove the `permissive` line in the policy file and try once more. Ideally we should have a working system at this point, however, there are some `dontaudit` rules which are blocking our program from successfully running. We run `sudo semodule -DB` to rebuild the policy with `dontaudit` policies disable. Restart the container and then check the audit log again. We will see some new entries, probably for the `read` or `write` system calls. We add these in and now have our fully functioning profile for SELinux. Once it is fully functioning, we set our policy version to 1.0.

---

### Stripping

The container has been stripped such that only files which are required for operation are remaining within the container.
Stripping down of the image is performed using [strip-image](https://github.com/SylinsicWMG/strip-image), with our fully usage in our [launch script](Container/launch.sh).

To strip the container, we first look for key files we will need in our initial files, i.e. bash (as seen in the shebang of our [entrypoint script](Container/files/entrypoint.sh)), as well as `readlink`, `dirname`, `basename` etc. All the files/programs we see to are used, we add in to the stripping command of our launch script.We also add any other files that we know are required, such as the maven repository. In addition to files, we also add some additional command line arguments to the stripping command, such as the docker tag of the original image, the new image, the Dockerfile to use, published ports etc.

Following this, we run our launch script, and wait for the machine to come online, we check the logs using `docker logs $(docker ps -ql)`, to see what errors are thrown (if any). If there are errors which suggest a file or library we must include, we add them to the stripping command, before relaunching the container and checking the logs again. If an error is thrown although not a useful one, i.e. one that doesn't suggest a file, then we use a combination of `ldd`, `LD_DEBUG=libs` and `strace` to identify any files that the program is attempting to use but can't find. Once there are no longer any errors and the container boots successfully, our stripping command is complete.

---

## Data Persistence

To ensure data persistence across container reboots and migration, we utilise a docker volume in which our databases are stored. The [`data`](Container/data) folder is used which maps to `/usr/src/mymaven/data` within the container, which we can use for persistent storage. To make use of this persistence, we must ensure the database of the java application actually gets stored to our mapped folder. To do this, we must modify/create custom java files for the application, see [Custom Java Implementation](#custom-java-implementation).

---

## Custom Java Implementation

To adjust the MagmaCore application, we may place files into the [`files/CustomJava`](Container/files/CustomJava) folder, as they would be located in the [`MagmaCore/src/main/java`](Container/files/MagmaCore/src/main/java) folder. The file and folder structure placed into this folder will overwrite the pre-existing files cloned from the [MagmaCore github](https://github.com/gchq/MagmaCore.git). This ability allows us to create custom database imlementations, to do this, we create our own class structure, and then create a modified version of the default [`MagmaCore.java`](Container/files/MagmaCore/src/main/java/uk/gov/gchq/magmacore/MagmaCore.java), which calls our custom created implementation.

One example implementation of this can be seen in the [`examples`](Container/examples) folder:
We create our custom class package for `uk.sylinsic.custom` with the directory structure [`uk/sylinsic/custom`](Container/examples/uk/sylinsic/custom), and then create our [`Custom.java`](Container/examples/uk/sylinsic/custom/Custom.java) application file within this. This custom implementation merely builds on the default [`FusekiService.java`](Container/files/MagmaCore/src/main/java/uk/gov/gchq/magmacore/demo/FusekiService.java), in that it provides for the ability to change the database location, and server port from the command line, this ability allows for us to ensure [Data Persistence](#data-persistence) as we control the fixed location of the database. The location variable also provides the location on the web server through which the database can be interacted with:

[custom.java](Container/examples/uk/sylinsic/custom/Custom.java)
```java
/*
 * Copyright 2021 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package uk.sylinsic.custom;

import org.apache.jena.fuseki.main.FusekiServer;
import org.apache.jena.fuseki.system.FusekiLogging;
import org.apache.jena.query.Dataset;

import uk.gov.gchq.magmacore.database.MagmaCoreJenaDatabase;
import uk.gov.gchq.magmacore.demo.ExampleDataObjects;

/**
 * Example custom use-case scenario for hosting {@link MagmaCoreJenaDatabase} on a Fuseki server.
 *
 * <p>
 * The Custom class can be used to host in-memory or persistent Magma Core Jena Datasets over
 * HTTP using a Fuseki server.
 * </p>
 * <p>
 * By default, the Fuseki server is configured to run on localhost:3330, however this can be changed
 * by providing a {@code int port} paramaeter to the {@code run()} function.
 * </p>
 * <p>
 * By default, the Fuseki server is also configured to store it's database in the "tdb" directory, 
 * however this cna be changed by providing a {@code string location} parameter to the {@code run()} function.
 * </p>
 * <p>
 * The Fuseki server can host either in-memory Datasets, or connected TDB stores. Datasets can be
 * added to the server using the {@code add(name, dataset)} method. Datasets are hosted at
 * {@code localhost:<port>/<name>}.
 * </p>
 */
public final class Custom {
    
    /**
     * Run the custom Fuseki Server.
     */
    public void run(final String location, final int port) {
        // Create/Connect to persistent database stored at `location`, hosting the server on port `port`
        final MagmaCoreJenaDatabase db = new MagmaCoreJenaDatabase("/usr/src/mymaven/data/".concat(location));


        // If db is not already populated, create set of example data objects to store in db.
        db.begin();
        if (db.getDataset().isEmpty()) {
            // Build example data objects Dataset.
            final Dataset objects = ExampleDataObjects.buildDataset();

            // Add example objects to default model in persistent dataset.
            db.getDataset().getDefaultModel().add(objects.getDefaultModel());
            db.commit();
        } else {
            db.abort();
        }
        // Build and start Fuseki server.
        final FusekiServer server = FusekiServer
                .create()
                .port(port)
                .add("/".concat(location), db.getDataset(), true).build();
        FusekiLogging.setLogging();
        server.start();
    }

    /**
     * Run the custom Fuseki Server.
     */
    public void run(final String location) {
        // Create/Connect to persistent database stored at `location`, hosting the server on port "3330"
        run(location, 3330);
    }


    /**
     * Run the custom Fuseki Server.
     */
    public void run(final int port) {
        // Create/Connect to persistent database stored at "tdb", hosting the server on port `port`
        run("db", port);
    }


    /**
     * Run the custom Fuseki Server.
     */
    public void run() {
        // Create/Connect to persistent database stored at "tdb", hosting the server on port "3330"
        run("db", 3330);
    }

}
```

We then also create a simple [`package-info.java`](Container/examples/uk/sylinsic/custom/package-info.java) file to declare the class path of our custom package:

[package-info.java](Container/examples/uk/sylinsic/custom/package-info.java)
```java
package uk.sylinsic.custom;
```

Finally, we create a custom [`MagmaCore.java`](Container/examples/uk/gov/gchq/magmacore/MagmaCore.java) file to overwrite the default entrypoint of the program. This allows us to run our custom package, rather than using one of the simple [`demo`](Container/files/MagmaCore/src/main/java/uk/gov/gchq/magmacore/demo) database interactions provided by default. In this file, we only need to call our custom class and entry method, however, for this example, it is slightly more extensive purely for the fact that we've implemented the ability to change the location and port as arguments provided through the command line; it is far from perfect and doesn't check order if tow arguments are passed, it is merely a basic form for a demonstration.

[MagmaCore.java](Container/examples/uk/gov/gchq/magmacore/MagmaCore.java)
```java
/*
 * Copyright 2021 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package uk.gov.gchq.magmacore;

import uk.sylinsic.custom.Custom;

/**
 * Application entry point.
 */
public final class MagmaCore {

    private MagmaCore() {}

    /**
     * Executes FusekiService or selected database example.
     *
     * @param args Application arguments.
     */
    public static void main(final String[] args) {
        switch (args.length) {
            case 0:
                new Custom().run();
                break;
            case 1:
                try {
                    final int port = Integer.parseInt(args[0]);
                    new Custom().run(port);
                } catch (final NumberFormatException e) {
                    new Custom().run(args[0]);
                }
                break;
            default:
                new Custom().run(args[0], Integer.parseInt(args[1]));
                break;
        }
    }
}
```

---

## Database Interaction

To interact with the database, utilising the default hosting method of a [Fuseki Service](https://jena.apache.org/documentation/fuseki2/)  on port 3330 interacting with [TDB2](https://jena.apache.org/documentation/tdb2/index.html), we can use [sparql query language](https://www.w3.org/TR/rdf-sparql-query/). By default the database exists at `/tdb/` however, this can change dependent on one's implementation decisions. At this directory, there exists several endpoints for sparql interactions:
|Description                            |URL                             |Method(s)        |
|---------------------------------------|--------------------------------|-----------------|
|SPARQL Query                           |http://localhost:3330/tdb/query |GET,POST         |
|SPARQL Query                           |http://localhost:3330/tdb/sparql|GET,POST         |
|SPARQL Update                          |http://localhost:3330/tdb/update|POST,PATCH       |
|File Upload                            |http://localhost:3330/tdb/update|POST,PATCH       |
|GSP (Graph Store Protocol) read-write  |http://localhost:3330/tdb/data  |GET,HEAD,PUT,POST|
|Read-write quads                       |http://localhost:3330/tdb       |GET,HEAD,PUT,POST|

Source: https://jena.apache.org/documentation/fuseki2/fuseki-embedded.html#example-1



---

## License

HQDM and MagmaCore are accessed under the GCHQ Contributor Licence Agreement. These projects were developed by GCHQ and are not my own.
